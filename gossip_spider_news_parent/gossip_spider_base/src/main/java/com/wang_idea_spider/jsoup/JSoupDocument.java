package com.wang_idea_spider.jsoup;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

/**
 * 使用jsoup将html页面转换成document对象
 */
public class JSoupDocument {
    /**
     * 获取document对象的方式之一: connect(url).get()  一般用于测试
     *
     * 真正代码：使用httpclient发送请求，获取html页面，使用jsoup转换成document对象
     */

    /**
     * 测试形式获取document对象
     *
     * @throws IOException
     */
    @Test
    public void documentTest() throws IOException {
        String url = "http://www.itcast.cn/";

        //使用jsoup发送请求，获取html，转成document对象
        Document document = Jsoup.connect(url).get();

        String title = document.title();
        System.out.println("title = " + title);
    }

    /**
     * 使用httpClient工具，获取html页面
     * 使用jsoup将html页面转成document对象
     */
    @Test
    public void documentTestByhttpClient() throws IOException {
        //1.确定url
        String url = "http://www.itcast.cn/";

        //2.发送请求，获取响应html页面
        //创建HttpClients对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置HttpGet对象参数

        //将url封装，模拟创建一个get请求
        HttpGet httpGet = new HttpGet(url);
        //将get请求发送给url
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String html = EntityUtils.toString(response.getEntity(), "utf-8");

        //3.转换成document对象
        Document document = Jsoup.parse(html);

        String title = document.title();
        System.out.println("title = " + title);

    }

    /**
     * 了解：读取本地的html页面，转换成document
     */
    @Test
    public void documentTestOfLocalHtml() {
        //读取本地的html文件，转换成document对象
        //new File("")  将本地html文件注入
        // Document parse = Jsoup.parse(new File(""), "utf-8");

    }

    /**
     * html 片段转换成document对象
     */
    @Test
    public void documentTestOfFragment() {
        //指定一个HTML的片段, 获取document对象
        String html = "<a>获取document的第四种方式</a>";

        Document document = Jsoup.parseBodyFragment(html);

        //Document document4 = Jsoup.parse(html);
        System.out.println(document.text());
    }
}
