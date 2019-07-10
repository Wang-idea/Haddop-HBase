package com.wang_idea.news.news163;

import com.google.gson.Gson;
import com.wang_idea.news.constant.SpiderConstant;
import com.wang_idea.news.news163.pojo.NewPojo;
import com.wang_idea.news.utils.HttpClientUtils;
import com.wang_idea.news.utils.IdWorker;
import com.wang_idea.news.utils.JedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * 获取list队列中的url，然后发送请求，获取数据，解析成一个NewPojo对象，将NewPojo对象保存到list队列中(bogData:spider:newsJsonList)
 */
public class News163Slave {
    //json转换对象
    public static final Gson gson = new Gson();

    //id生成器 需要两个参数
    //打两个包  10 11
    public static final IdWorker idworker = new IdWorker(1, 1);

    public static void main(String[] args) throws IOException {

        //如果slave运行时候，Master还未将数据存储到SPIDER_NEWS_URLLIST中
        // 循环判断：当第一次从SPIDER_NEWS_URLLIST拿取url的时候，list集合中数据可能为空，会跳出循环，所以需要一个阻塞
        while (true) {

            //1. 从master中存储url的集合：SPIDER_NEWS_URLLIST中 获取url
            Jedis jedis = JedisUtils.getJedis();
            //带阻塞的弹出方法：如果没有数据，阻塞一会，这里设为20s，如果在时间内有新数据，就继续运行，如果超出时间则停止运行
            //String url = jedis.rpop(SpiderConstant.SPIDER_NEWS_URLLIST);

            //返回值:两个元素  下标为0 ：key 下标为1: url值
            List<String> list = jedis.brpop(20, SpiderConstant.SPIDER_NEWS_URLLIST);
            //20s之后会执行if语句
            if (list == null || list.size() == 0) {
                break;
            }
            //如果没有跳出循环，需要获取url，下标为1
            String url = list.get(1);
            jedis.close();

            //2. 调用parseNewsItem，获取解析后的News对象
            NewPojo newPojo = parseNewsItem(url);
            String json = gson.toJson(newPojo);

            //3.将News对象保存到redis的list队列中
            jedis = JedisUtils.getJedis();
            jedis.lpush(SpiderConstant.SPIDER_NEWS_NewsJsonList, json);
            jedis.close();
        }
    }

    /**
     * 解析每条标题对应的url：docurl 发送请求，获取html，解析html，将获得的数据，封装成JavaBean
     *
     * @param docurl
     */
    private static NewPojo parseNewsItem(String docurl) throws IOException {
        //1.根据url地址发送http请求
        String html = HttpClientUtils.doGet(docurl);

        //2.根据html页面，转换成document对象
        Document document = Jsoup.parse(html);

        //3.解析document对象：标题、内容、编辑、时间、来源、新闻的url
        //标题
        String title = document.select("#epContentLeft h1").text();
        //System.out.println(title);

        //时间
        String time = document.select(".post_time_source").text();
        // 截取时间  将后面去掉
        //2019-06-25 10:42:27　来源: 网易娱乐
        time = time.substring(0, 19);
        //System.out.println(time);

        //来源
        String source = document.select("#ne_article_source").text();
        //System.out.println(source);

        //内容
        String context = document.select("#endText p").text();
        //System.out.println(context);

        //编辑
        String editor = document.select(".ep-editor").text();
        //截取：
        //责任编辑：杨明_NV5736
        String[] split = editor.split("_");
        //System.out.println(split[0]);


        //4.封装成javaBean对象，返回
        NewPojo newPojo = new NewPojo();

        //id使用分布式雪花id生成器 IdWorker
        newPojo.setId(idworker.nextId());

        newPojo.setTitle(title);

        newPojo.setSource(source);

        newPojo.setTime(time);

        newPojo.setUrl(docurl);

        newPojo.setEditor(split[0]);

        newPojo.setContent(context);
        newPojo.toString();

        return newPojo;

    }
}
