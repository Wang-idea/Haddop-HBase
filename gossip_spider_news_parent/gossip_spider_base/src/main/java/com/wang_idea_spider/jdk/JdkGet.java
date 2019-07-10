package com.wang_idea_spider.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * jdk的get请求方式
 * get请求  参数在url后面
 */
public class JdkGet {
    public static void main(String[] args) throws IOException {
        //1.确定要爬取的url
        String  indexUrl = "http://www.itcast.cn/?username=zhangsna&age=18";

        //2.创建url对象
        URL url = new URL(indexUrl);

        //3.向url发送请求
        //URLConnection urlConnection = url.openConnection();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        //4.设置请求方法
        urlConnection.setRequestMethod("GET");

        //5.获取数据(流的形式获取数据： 输入流)
        InputStream inputStream = urlConnection.getInputStream();

        //6.打印流里面的内容
        byte[] bytes = new byte[1024];
        int len = 0;

        //遍历流，转换成字符串 输出
        while ((len =inputStream.read(bytes))!=-1){
            String s = new String(bytes, 0, len);
            System.out.println("s = " + s);
        }
        inputStream.close();

    }
}
