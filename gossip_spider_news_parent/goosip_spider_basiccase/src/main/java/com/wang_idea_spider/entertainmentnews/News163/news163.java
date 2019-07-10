package com.wang_idea_spider.entertainmentnews.News163;

import com.google.gson.Gson;
import com.wang_idea_spider.entertainmentnews.News163.pojo.SpiderConstant;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import redis.clients.jedis.Jedis;
import com.wang_idea_spider.entertainmentnews.News163.dao.NewsDao;
import com.wang_idea_spider.entertainmentnews.News163.pojo.NewPojo;
import utils.HttpClientUtils;
import utils.IdWorker;
import utils.JedisUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 163娱乐爬虫
 */
public class news163 {
    //json转换对象
    public static final Gson gson = new Gson();

    //id生成器 需要两个参数
    //0 0是参数  每次添加不同参数即可
    public static final IdWorker idworker = new IdWorker(0, 0);


    //创建dao对象
    public static final NewsDao newsdao =new NewsDao();

    public static void main(String[] args) throws IOException {
        //1.确定url地址
        String url = "https://ent.163.com/special/000380VU/newsdata_index_02.js?callback=data_callback";

        //2.使用httpClientUtils发送请求，获取相应数据（url 列表）
       // String jsonString = HttpClientUtils.doGet(url);

        //3.解析json数据
        //parseNewsJson(jsonString);

        //爬取数据
        page163(url);
        System.out.println("主函数");


    }

    /**
     * 进行分页 将所有的页码数据全部爬取  不再只爬取单个页面
     * @param url
     */
    private static void page163(String url) throws IOException {
        int i=2;
//        while(i<=10){
//            //2.使用httpClientUtils发送请求，获取相应数据（url 列表）
//            String jsonString = HttpClientUtils.doGet(url);
//
//            //3.解析json数据
//            parseNewsJson(jsonString);
//
//            //4.当前页爬取结束,构造下一页的url
//            if (i<10){
//                String index ="0"+i;
//                url ="https://ent.163.com/special/000380VU/newsdata_index"+"_"+index+".js?callback=data_callback";
//
//            }
//            if (i==10){
//                url ="https://ent.163.com/special/000380VU/newsdata_index_10.js?callback=data_callback";
//
//            }
//
//            i++;
            while(true){
                //2.使用httpClientUtils发送请求,获取响应数据(url列表)
                String jsonString = HttpClientUtils.doGet(url);


                //跳出循环的逻辑
                if(StringUtils.isEmpty(jsonString)){
                    System.out.println("爬完了......");
                    break;
                }


                //3. 解析json数据
                parseNewsJson(jsonString);


                //4. 构造下一页的url地址,赋值给url
                String pageString = "";
                if(i < 10){
                    pageString = "0" + i;
                }else{
                    pageString = i +"";
                }

                i++;
                url  = "https://ent.163.com/special/000380VU/newsdata_index_" + pageString + ".js";
        }
        System.out.println("循环结束");
    }

    /**
     * 解析json数据
     *
     * @param jsonString
     */
    private static void parseNewsJson(String jsonString) throws IOException {
        //1.处理json数据，处理成格式良好的json数据
        jsonString = formatJson(jsonString);
        //System.out.println("jsonString = " + jsonString);

        //2.将json字符串转换成 List<Map<String,Object>>
        List<Map<String, Object>> list = gson.fromJson(jsonString, List.class);

        //3.遍历List
        for (Map<String, Object> stringObjectMap : list) {

            //通过键：title 得到Object值
            String title = (String) stringObjectMap.get("title");

            //获取所有标题对应的url：docurl
            String docurl = (String) stringObjectMap.get("docurl");

            //筛选过滤掉含有图集的url
            if (docurl.contains("photoview")) {
                continue;
            }

            //纯新闻网页url中包含该字段  如果是视频、图片或其他则过滤掉
            if (!docurl.contains("ent.163.com")) {
                continue;
            }

            //判断当前url是否已经爬取过
           if (hasPasered(docurl)){
                continue;
           }

            //System.out.println(title + ":" + docurl);

            //解析每一个url的新闻数据：、时间
            NewPojo newPojo = parseNewsItem(docurl);

            //保存到数据库中
            newsdao.saveNews(newPojo);

            //将爬取后的url保存到redis的set集合中，防止下次重复爬取
            Jedis jedis = JedisUtils.getJedis();
            jedis.sadd(SpiderConstant.SPIDER_NEW163,docurl);

            jedis.close();
        }
    }

    /**
     * 判断是否爬取过
     * @param docurl
     * @return
     */
    private static boolean hasPasered(String docurl) {
        Jedis jedis = JedisUtils.getJedis();
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEW163, docurl);
        jedis.close();
        return sismember;
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

    /**
     * 处理json数据成一个格式正确的json字符串
     *
     * @param jsonString
     * @return
     */
    private static String formatJson(String jsonString) {
        //json中
        //lastIndexOf ：从后往前遍历查找对应字符串，找到对应字符串结束返回数据，返回值为int类型，返回查找字符串首个字符位置（从0开始查找），未找到返回 -1；
        //indexOf ：从前往后遍历查找对应字符串，找到对应字符串结束返回数据，返回值为int类型，返回查找字符串首个字符位置（从0开始查找），未找到返回 -1；
        //lastIndexOf:从后向前差，查询的下标是从前往后的下标  比如差一个不重复的数字 正序 第三  那么lastof查询也是三
        //使用indexOf和lastIndexOf  注意解析出来的数据 是否完整
        String substring = jsonString.substring(jsonString.indexOf("["), jsonString.lastIndexOf(")"));
        return substring;

    }


}
