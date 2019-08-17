package com.wang_idea.news.dao.impl;

import com.google.gson.Gson;
import com.wang_idea.news.constant.SpiderConstant;
import com.wang_idea.news.dao.NewsDao;
import com.wang_idea.news.kafka.KafkaSpiderProducer;
import com.wang_idea.news.news163.pojo.NewPojo;
import com.wang_idea.news.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 读取list队列中的news对象，
 * 判断url是否已经爬过了，防止数据库中存放重复的数据
 * 保存到mysql数据库中，然后将新闻的url存储到redis的set集合中
 */
public class PublicdaoNode {
    //json转换对象
    public static final Gson gson = new Gson();

    //创建dao对象
    public static final NewsDao newsdao =new NewsDao();

    //kafka生产者对象
    private static  final KafkaSpiderProducer kafkaSpiderProducer = new KafkaSpiderProducer();

    public static void main(String[] args) {
        while (true) {
            //1. 读取list队列中的news对象
            Jedis jedis = JedisUtils.getJedis();
            List<String> list = jedis.brpop(20, SpiderConstant.SPIDER_NEWS_NewsJsonList);
            jedis.close();

            if (list == null || list.size() == 0) {
                break;
            }
            String NewPojoJson = list.get(1);

            NewPojo newPojo = gson.fromJson(NewPojoJson, NewPojo.class);

            //2. 判断url是否已经爬取过
            if (hasPasered(newPojo.getUrl())){
                continue;
            }

            //3. 保存New对象保存到mysql数据中
            newsdao.saveNews(newPojo);

            //4. 将news Json类型数据保存到kafka集群
            kafkaSpiderProducer.sendSpider(NewPojoJson);

            //5. 将新闻对象的url保存到set集合中
            jedis = JedisUtils.getJedis();
            jedis.sadd(SpiderConstant.SPIDER_NEWS_URLSET,newPojo.getUrl());
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
        Boolean sismember = jedis.sismember(SpiderConstant.SPIDER_NEWS_URLSET, docurl);
        jedis.close();
        return sismember;
    }
}
