package com.wang_idea_spider.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**\
 * httpClient工具包的post请求方式
 */
public class HttpClientPost {
    public static void main(String[] args) throws IOException {
        //1. 确定爬取的url地址
        String indexUrl = "http://www.itcast.cn";

        //2. 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //3.创建一个httpPost对象
        HttpPost httpPost = new HttpPost(indexUrl);

        //post请求头和请求参数怎么传递
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

        //将表单数据封装到请求体中
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("username","itcast"));
        list.add(new BasicNameValuePair("password","wwww.itcast.cn"));
        list.add(new BasicNameValuePair("age","12"));
        list.add(new BasicNameValuePair("bithday","xxx"));

        //form表单的entity
        HttpEntity formEntity = new UrlEncodedFormEntity(list);
        httpPost.setEntity(formEntity);


        //4. 发送请求，获取响应
        CloseableHttpResponse response = httpClient.execute(httpPost);


        int statusCode = response.getStatusLine().getStatusCode();

        //5. 获取响应状态吗   200
        if(statusCode == 200){
            //获取响应头相关数据
            Header[] allHeaders = response.getAllHeaders();
            for (Header allHeader : allHeaders) {
                System.out.println("响应头name:" + allHeader.getName() + "  响应头的value:" +  allHeader.getValue());
            }


            //获取响应体相关数据
            HttpEntity entity = response.getEntity();

            String html = EntityUtils.toString(entity, "utf-8");


            System.out.println(html);

        }

        //6. 释放资源
        httpClient.close();
    }
}