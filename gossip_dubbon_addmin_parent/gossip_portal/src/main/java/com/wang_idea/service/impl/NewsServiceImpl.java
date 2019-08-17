package com.wang_idea.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang_idea.constant.GossipConstant;
import com.wang_idea.mapper.NewsMapper;
import com.wang_idea.pojo.News;
import com.wang_idea.pojo.PageBean;
import com.wang_idea.pojo.ResultBean;
import com.wang_idea.service.IndexSearchService;
import com.wang_idea.service.IndexWriterService;
import com.wang_idea.service.NewsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    /**
     * 注入dao代理对象
     */
    @Autowired
    private NewsMapper newsMapper;

    /**
     * jedis连接池
     */
    @Autowired
    private JedisPool jedisPool;

    /**
     * 将数据写入索引库  调用远程服务
     * 需要找注册中心注入远程服务
     */
    @Reference(timeout = 3000)
    private IndexWriterService indexWriterService;

    /**
     * 找注册中心，远程注入搜索服务
     */
    @Reference(timeout = 3000)
    private IndexSearchService indexSearchService;

    /**
     * 打印日志
     */
   // private Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    /**
     * 调用dao，获取sql库中新闻列表数据,再调用远程的索引写入服务，将新闻数据写入索引库
     */
    @Override
    public void newsIndexWriter() throws Exception {
        //1. 注入dao的代理对象

        //从redis中获取maxId，如果不存在，初始化为0，如果存在，就是用这个最大的maxId
        Jedis jedis = jedisPool.getResource();
        String maxId = jedis.get(GossipConstant.bigData_GOSSIP_maxID);
        jedis.close();
        if (StringUtils.isEmpty(maxId)) {
            maxId = "0";
        }

        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        while (true) {
            //2. 调用dao层，获取新闻列表数据
            List<News> list = newsMapper.queryData(maxId);

            //跳出循环的逻辑，更新最大的id值到redis中
            if (list == null || list.size() == 0) {
                jedis = jedisPool.getResource();
                jedis.set(GossipConstant.bigData_GOSSIP_maxID, maxId);
                jedis.close();
                break;
            }

            // 做日期处理
            for (News news : list) {
                String time = news.getTime();
                Date date = oldFormat.parse(time);
                String newTime = newFormat.format(date);
                //将日期转换成solrCloud的标准格式
                news.setTime(newTime);
            }

            //3. 调用远程服务，将数据写入索引库
            indexWriterService.newsIndexWriter(list);

            System.out.println("写入索引的条数" + list.size());
            //logger.info("写入solrCloud索引库的数据条数：" + list.size());
            //4. 更新maxId值：当前页的最大id值
            maxId = newsMapper.queryMaxId(maxId);


        }
    }

    /**
     * @param resultBean 搜索关键字
     * @return 新闻列表
     * @throws Exception
     */
    @Override
    public List<News> findByKeyWords(ResultBean resultBean) throws Exception {
        //1. 注入远程索引搜索服务

        //2. 调用远程搜索服务，返回结果
        List<News> newsList = indexSearchService.findByKeyWords(resultBean);

        //3. 处理内容太多的情况
        for (News news : newsList) {
            String content = news.getContent();
            if (content.length() > 70) {
                content = content.substring(0, 69) + "....";
                //注意：将处理后的结果放回到内容中
                news.setContent(content);
            }
        }
        //4. 返回列表
        return newsList;
    }

    /**
     * 能够支持分页查询的方法
     * @param resultBean 关键字  过滤条件  分页条件
     * @return 分页结果pageBean对象
     * @throws Exception
     */
    @Override
    public PageBean findByPageQuery(ResultBean resultBean) throws Exception {
        //1. 调用远程服务

        //2. 调用远程搜索服务，返回结果
        PageBean pageBean = indexSearchService.findByPageQuery(resultBean);

        //3. 处理内容太多的情况
        for (News news : pageBean.getNewsList()) {
            String content = news.getContent();
            if (content.length() > 70) {
                content = content.substring(0, 69) + "....";
                //注意：将处理后的结果放回到内容中
                news.setContent(content);
            }
        }

        return pageBean;
    }

}
