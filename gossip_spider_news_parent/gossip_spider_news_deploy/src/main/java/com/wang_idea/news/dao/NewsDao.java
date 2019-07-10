package com.wang_idea.news.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wang_idea.news.news163.pojo.NewPojo;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;

/**
 * 连接数据库
 * dao层
 */
public class NewsDao extends JdbcTemplate {
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
    public NewsDao() {
        super(comboPooledDataSource);
    }

    public void  saveNews(NewPojo newPojo){
        String sql ="insert into news(id,title,url,content,editor,time,source) values(?,?,?,?,?,?,?)";
        this.update(sql,newPojo.getId(),newPojo.getTitle(),newPojo.getUrl(),newPojo.getContent(),newPojo.getEditor(),newPojo.getTime(),newPojo.getSource());

    }
}