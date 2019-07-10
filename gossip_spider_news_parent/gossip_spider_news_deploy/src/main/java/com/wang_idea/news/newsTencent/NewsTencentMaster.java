package com.wang_idea.news.newsTencent;

import com.google.gson.Gson;
import com.wang_idea.news.constant.SpiderConstant;
import com.wang_idea.news.dao.NewsDao;
import com.wang_idea.news.news163.pojo.NewPojo;
import com.wang_idea.news.utils.HttpClientUtils;
import com.wang_idea.news.utils.IdWorker;
import com.wang_idea.news.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取news新闻数据
 * 判断url是否爬取过
 * 将获取到的news数据保存到redis的list队列(bigDataJsonList)
 */
public class NewsTencentMaster {

    //json转换对象
    public static Gson gson = new Gson();

    //id生成器 需要两个参数
    //0 0是参数  每次添加不同参数即可
    public static IdWorker idworker = new IdWorker(0, 1);

    //创建新的实体类
    public static NewPojo newsTencent = new NewPojo();

    //数据库
    public static NewsDao newsTencentDao = new NewsDao();

    public static void main(String[] args) throws IOException {
        //1.确定url：热点url  非热点url
        String hoturl = "https://pacaio.match.qq.com/irs/rcd?cid=137&token=d0f13d594edfc180f5bf6b845456f3ea&id=&ext=ent&num=60";
        String nohoturl = "https://pacaio.match.qq.com/irs/rcd?cid=58&token=c232b098ee7611faeffc46409e836360&ext=ent&page=0";

        pageTencent(hoturl,nohoturl);
    }

    /**
     * 分页爬取腾讯新闻的方法:热点数据没哟与分页数据  非热点有分页
     */
    public static void pageTencent(String hoturl,String nohoturl) throws IOException {
        //2.使用httpClientUtils工具发送http请求，获得数据(Json)
        String hotJson = HttpClientUtils.doGet(hoturl);
        List<NewPojo> hotNewList = parseNewTencent(hotJson);
        saveNewsToRedis(hotNewList);


        //非热点数据
        int i =1;
        while (true){
            String nohotJson = HttpClientUtils.doGet(nohoturl);
            List<NewPojo> nohotnewsList = parseNewTencent(nohotJson);
            if (nohotnewsList==null||nohotnewsList.size()==0){
                System.out.println("爬完了");
                break;
            }

            //将新闻列表从村大redis的List列表中
            saveNewsToRedis(nohotnewsList);
            //获取下一页的url，更新nohoturl
            nohoturl="https://pacaio.match.qq.com/irs/rcd?cid=58&token=c232b098ee7611faeffc46409e836360&ext=ent&page="+i;
            i++;
        }

    }

    /**
     * 将新闻数据保存到redis接种
     * @param nohotnewsList
     */
    private static void saveNewsToRedis(List<NewPojo> nohotnewsList) {
        for (NewPojo newPojo : nohotnewsList) {
            //将对象转换成json对象
            String newsJson = gson.toJson(newPojo);
            Jedis jedis = JedisUtils.getJedis();
            //保存到redis中
            jedis.lpush(SpiderConstant.SPIDER_NEWS_NewsJsonList,newsJson);
            jedis.close();
        }
    }


    /**
     * 根据新闻的json数据，解析成一个新闻列表
     *
     * @param
     * @return jspnphotnews（
     */
    public static List<NewPojo> parseNewTencent(String newJson) {

        List<NewPojo> newsTencentList = new ArrayList<NewPojo>();

        //1.进行json转换 ，String --->转换成Map<String,Object>
        Map<String, Object> map = gson.fromJson(newJson, Map.class);

        //2.从map中获取data新闻数据
        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("data");

        //3.遍历打他数据(新闻数据列表)
        for (Map<String, Object> datum : data) {
            //注意这个对象 方法哦for循环里面
            NewPojo newsTencent = new NewPojo();

            //获取url地址
            String url = (String) datum.get("url");

            //过滤条件：判断当前url是否已经爬取过
            if (hasParsed(url)){
                continue;
            }

            //过滤视频(视频类型的新闻不要)
            if (url.contains("video")) {
                continue;
            }

            String title = (String) datum.get("title");
            String update_time = (String) datum.get("update_time");
            String source = (String) datum.get("source");
            String content = (String) datum.get("intro");

            newsTencent.setId(idworker.nextId());
            newsTencent.setUrl(url);
            newsTencent.setTitle(title);
            newsTencent.setTime(update_time);
            newsTencent.setSource(source);
            newsTencent.setContent(content);
            newsTencent.setEditor(source);
            newsTencentList.add(newsTencent);


        }
        return newsTencentList;
    }

    /**
     * 判断当前的url是否已经爬取过
     * @param url
     * @return
     */
    private static boolean hasParsed(String url) {
        Jedis jedis = JedisUtils.getJedis();
        //判断是不是爬取过的元素
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_URLSET, url);
        jedis.close();
        return sismember;
    }

}