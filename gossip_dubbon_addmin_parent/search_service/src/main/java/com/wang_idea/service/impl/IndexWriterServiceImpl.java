package com.wang_idea.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang_idea.pojo.News;
import com.wang_idea.service.IndexWriterService;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * 索引写入服务的实现
 */
//添加dubbo的service的注解

@Service
public class IndexWriterServiceImpl implements IndexWriterService {

    @Autowired//类型注入
    private CloudSolrServer cloudSolrServer;

    /**
     * 索引写入的方法
     *
     * @param list
     */
    @Override
    public void newsIndexWriter(List<News> list) throws IOException, SolrServerException {
        //1. 创建CloudSolr对象
        //applicationContext已经引入SolrCloud  所以只需要注入CloudSolr对象

        //2. 调用CloudSolrServer的索引写入方法
        cloudSolrServer.addBeans(list);

        //3. 提交
        cloudSolrServer.commit();

        //4. 千万不要关闭  配置文件自动关闭
    }
}
