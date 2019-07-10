package com.wang_idea.news.constant;

/**
 * 常量
 */
public class SpiderConstant {
    /**
     * 常量:163 爬虫 存放爬取的url的队列
     */
    public static final String SPIDER_NEWS_URLLIST = "bigData:spider:urlList";

    /**
     * 常量:url和163 公用的常量，用于存放已经爬取过的url的set集合
     */
    public static final String SPIDER_NEWS_URLSET = "bigData:spider:urlSet";

    /**
     * 常量:url和163 公用List队列，存放已经爬取到news对象的json数据
     */
    public static final String SPIDER_NEWS_NewsJsonList = "bigData:spider:newsJsonList";
}

