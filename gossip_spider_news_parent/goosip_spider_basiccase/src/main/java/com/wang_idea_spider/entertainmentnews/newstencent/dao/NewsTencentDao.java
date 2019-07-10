package com.wang_idea_spider.entertainmentnews.newstencent.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import com.wang_idea_spider.entertainmentnews.newstencent.pojo.NewsTencent;

import java.beans.PropertyVetoException;

public class NewsTencentDao extends JdbcTemplate {
    /**
     * 连接数据库
     * dao层
     */
        private static ComboPooledDataSource comboPooledDataSource;

        //初始化数据源
        static {
            comboPooledDataSource = new ComboPooledDataSource();
            try {
                comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
                comboPooledDataSource.setJdbcUrl("jdbc:mysql://192.168.72.141/gossip?characterEncoding=utf-8");
                comboPooledDataSource.setUser("root");
                comboPooledDataSource.setPassword("123456");
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

        }
        //在子类中调用父类的构造方法
        public NewsTencentDao() {
            super(comboPooledDataSource);
        }

        public void  saveNews(NewsTencent newsTencent){
            String sql ="insert into news(id,title,url,content,time,source) values(?,?,?,?,?,?)";
            this.update(sql,newsTencent.getId(),newsTencent.getTitle(),newsTencent.getUrl(),newsTencent.getContent(),newsTencent.getTime(),newsTencent.getSource());

        }
    }


