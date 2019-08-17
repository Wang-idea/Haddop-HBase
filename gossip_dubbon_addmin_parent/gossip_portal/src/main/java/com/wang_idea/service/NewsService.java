package com.wang_idea.service;

import com.wang_idea.pojo.News;
import com.wang_idea.pojo.PageBean;
import com.wang_idea.pojo.ResultBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻的服务实现类
 */

public interface NewsService {
    /**
     * 调用dao，获取sql库中新闻列表数据,再调用远程的索引写入服务，将新闻数据写入索引库
     */
    public void newsIndexWriter() throws Exception;

    /**
     * 根据关键字进行新闻搜索
     * @param resultBean 搜索关键字
     * @return 新闻列表
     * @throws Exception
     */
    public List<News> findByKeyWords(ResultBean resultBean) throws Exception;

    /**
     * 能够支持分页查询的方法
     * @param resultBean 关键字  过滤条件  分页条件
     * @return  分页结果pageBean对象
     * @throws Exception
     */
    public PageBean findByPageQuery(ResultBean resultBean) throws  Exception;
}
