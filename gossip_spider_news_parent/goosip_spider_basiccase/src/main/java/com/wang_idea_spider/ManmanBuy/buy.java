package com.wang_idea_spider.ManmanBuy;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用post请求模拟登陆慢慢买，获取用户的积分信息
 */
public class buy {
    public static void main(String[] args) throws IOException {
        //1.确定登录的url
        String url = "http://home.manmanbuy.com/login.aspx?tourl=http%3a%2f%2fhome.manmanbuy.com%2fuserCenter.aspx";

        //2.发送post请求，请求登录
        //2.1创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.2创建httpPost对象
        HttpPost httpPost = new HttpPost(url);

        //封装请求头
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        httpPost.setHeader("Referer", "http://home.manmanbuy.com/login.aspx?tourl=http%3a%2f%2fhome.manmanbuy.com%2fuserCenter.aspx");


        //封装请求参数
        //服务端请求需要的参数
//        __VIEWSTATE: /wEPDwULLTIwNjQ3Mzk2NDFkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBQlhdXRvTG9naW4voj01ABewCkGpFHsMsZvOn9mEZg==
//        __EVENTVALIDATION: /wEWBQLW+t7HAwLB2tiHDgLKw6LdBQKWuuO2AgKC3IeGDJ4BlQgowBQGYQvtxzS54yrOdnbC
//        txtUser: itcast
//        txtPass: www.itcast.cn
//        autoLogin: on
//        btnLogin: (unable to decode value)

        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwULLTIwNjQ3Mzk2NDFkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBQlhdXRvTG9naW4voj01ABewCkGpFHsMsZvOn9mEZg=="));
        list.add(new BasicNameValuePair("__EVENTVALIDATION", "/wEWBQLW+t7HAwLB2tiHDgLKw6LdBQKWuuO2AgKC3IeGDJ4BlQgowBQGYQvtxzS54yrOdnbC"));
        list.add(new BasicNameValuePair("txtUser", "itcast"));
        list.add(new BasicNameValuePair("txtPass", "www.itcast.cn"));
        list.add(new BasicNameValuePair("autoLogin", "on"));
        list.add(new BasicNameValuePair("btnLogin", "登录"));

        HttpEntity httpEntity = new UrlEncodedFormEntity(list);
        httpPost.setEntity(httpEntity);

        //2.3 发送http骑牛，获取响应
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //2.4 判断响应的状态码  是否是302
        if (response.getStatusLine().getStatusCode() == 302) {
            //2.5 获取location头和set——cookie头
            //"Location" 请求头中的Location  请求头传输回来的location数据 是一个数组
            // 但是location在此案例中只有一个跳转的页面 所以数组只有一个元素 下标直接为0
            Header[] locations = response.getHeaders("Location");


            //需重定向的url地址
            String locationurl = locations[0].getValue();

            //获取登录的cookie信息 set-cookie
            Header[] cookies = response.getHeaders("Set-Cookie");

            //3. 发送获取用户积分的请求
            HttpGet httpGet = new HttpGet(locationurl);
            //3.1 封装请求头
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
            httpGet.setHeader("Referer", "http://home.manmanbuy.com/login.aspx?tourl=http%3a%2f%2fhome.manmanbuy.com%2fuserCenter.aspx");

            //3.2 封装cookie信息
            httpGet.setHeaders(cookies);

            //3.3 发送httpget请求，获取带积分的html页面
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println(httpResponse.getStatusLine().getStatusCode());

            //3.3 获取html页面
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String html = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                //3.4 获取document对象
                Document document = Jsoup.parse(html);

                //3.5 解析document对象,获取节分
                String jifen = document.select("#aspnetForm > div.udivright > div:nth-child(2) > table > tbody > tr > td:nth-child(1) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div:nth-child(1) > font").text();
                System.out.println("jifen = " + jifen);
            }
        }

    }
}
