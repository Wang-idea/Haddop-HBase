package com.wang_idea.timing;

import com.wang_idea.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
//@Component
public class IndexWriterTiming {

    /**
     * 这里调用不属于远程调用，不需要dubbo的@Service
     */
    @Autowired
    private NewsService newsService;

    private Logger logger = LoggerFactory.getLogger(IndexWriterTiming.class);

    /**
     * 执行什么任务  什么时候执行
     * 定时任务的定义：
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void newsIndexWriterTiming(){
        try {
            logger.info("开始写入数据！！");
            newsService.newsIndexWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
