package com.wang_idea_spider.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * httpClient工具包的get请求方式
 * get请求  参数在url后面
 */
public class HttpclientGet {
    public static void main(String[] args) throws IOException {
        //1. 确定爬取的url地址
        String indexUrl = "http://www.itcast.cn?username=zhangsan&age=18";

        //创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //3.  创建httpGet对象   设置请求头  设置请求参数
        HttpGet httpGet = new HttpGet(indexUrl);

        //浏览器内核,  伪装自己,伪装成一个浏览器访问
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

        //4.发送请求，获取响应
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //获取响应码
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode==200){
            //5.打印响应头信息：响应头，响应体
            Header[] allHeaders = response.getAllHeaders();
            for (Header allHeader : allHeaders) {
                System.out.println("响应头" + allHeader.getName()+"响应头的value"+allHeader.getValue());
            }

            //获取响应体
            HttpEntity entity = response.getEntity();

            //通过httpClient的工具类 将响应体中的html获取出来
            String html = EntityUtils.toString(entity, "utf-8");
            System.out.println("html = " + html);
        }

        //关闭资源
        httpClient.close();

    }
}
