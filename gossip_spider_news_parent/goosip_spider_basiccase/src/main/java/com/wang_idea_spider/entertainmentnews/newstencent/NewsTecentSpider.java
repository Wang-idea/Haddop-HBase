package com.wang_idea_spider.entertainmentnews.newstencent;

import com.google.gson.Gson;
import com.wang_idea_spider.entertainmentnews.News163.pojo.SpiderConstant;
import com.wang_idea_spider.entertainmentnews.newstencent.dao.NewsTencentDao;
import com.wang_idea_spider.entertainmentnews.newstencent.pojo.NewsTencent;
import redis.clients.jedis.Jedis;
import utils.HttpClientUtils;
import utils.IdWorker;
import utils.JedisUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 腾讯新闻爬虫
 */
public class NewsTecentSpider {

    //json转换对象
    public static Gson gson = new Gson();

    //id生成器 需要两个参数
    //0 0是参数  每次添加不同参数即可
    public static IdWorker idworker = new IdWorker(0, 1);

    //创建新的实体类
    public static NewsTencent newsTencent = new NewsTencent();

    //数据库
    public static NewsTencentDao newsTencentDao = new NewsTencentDao();

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
        List<NewsTencent> hotNewList = parseNewTencent(hotJson);
        saveNewList(hotNewList);


        //非热点数据
        int i =1;
        while (true){
            String nohotJson = HttpClientUtils.doGet(nohoturl);
            List<NewsTencent> nohotnewsList = parseNewTencent(nohotJson);
            if (nohotnewsList==null||nohotnewsList.size()==0){
                System.out.println("爬完了");
                break;
            }
            saveNewList(nohotnewsList);

            //获取下一页的url，更新nohoturl
            nohoturl="https://pacaio.match.qq.com/irs/rcd?cid=58&token=c232b098ee7611faeffc46409e836360&ext=ent&page="+i;
            i++;
        }

    }

    /**
     * 将list的用户数据存储到数据库中
     *
     * @param hotNewList
     */
    public static void saveNewList(List<NewsTencent> hotNewList) {
        for (NewsTencent tencent : hotNewList) {
            newsTencentDao.saveNews(tencent);
            //将当前新闻的url保存到redis的set集合中
            Jedis jedis = JedisUtils.getJedis();
            jedis.sadd(SpiderConstant.SPIDER_NEWS_TENCENT,tencent.getUrl());
            jedis.close();
        }

    }

    /**
     * 根据新闻的json数据，解析成一个新闻列表
     *
     * @param
     * @return jspnphotnews（
     */
    public static List<NewsTencent> parseNewTencent(String newJson) {

        List<NewsTencent> newsTencentList = new ArrayList<NewsTencent>();

        //进行数据分割
        //newJson = newJson.substring(newJson.indexOf("{"), newJson.lastIndexOf(")"));
        //System.out.println("newsTencentList = " + newJson);

        //1.进行json转换 ，String --->转换成Map<String,Object>
        Map<String, Object> map = gson.fromJson(newJson, Map.class);

        //2.从map中获取data新闻数据
        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("data");

        //判断是否为空


        //3.遍历打他数据(新闻数据列表)
        for (Map<String, Object> datum : data) {
            //注意这个对象 方法哦for循环里面
            NewsTencent newsTencent = new NewsTencent();

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
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_TENCENT, url);
        jedis.close();
        return sismember;
    }

}