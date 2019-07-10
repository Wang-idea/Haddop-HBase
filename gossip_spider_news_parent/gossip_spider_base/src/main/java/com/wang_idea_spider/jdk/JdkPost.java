package com.wang_idea_spider.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * jdk的post请求方式  url 不需要带参数
 */
public class JdkPost {
    public static void main(String[] args) throws IOException {
        //1. 确定爬取url地址
        String indexUrl = "http://www.itcast.cn";

        //2.创建URl对象
        URL url = new URL(indexUrl);

        //3.打开连接，获取数据
        //HttpURLConnection 可用于向指定网站发送GET请求、POST请求
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        //post请求
        urlConnection.setRequestMethod("POST");

        //默认不能向外部写出数据，默认是false，需要打开这个开关
        //setDoOutput()默认是false，需要手动设置为true，完了就可以调用getOutputStream()方法从服务器端获得字节输出流
        urlConnection.setDoOutput(true);

        //post请求的参数：输出流的形式请求参数
        //参数没什么意义  为什么还要加这个参数
        OutputStream outputStream = urlConnection.getOutputStream();
        String param = "username=zhangsan&age=18";
        outputStream.write(param.getBytes());
        outputStream.flush();
        outputStream.close();

        //4.按照流的形式获取数据
        InputStream inputStream = urlConnection.getInputStream();

        //5打印数据
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes) )!= -1){
            System.out.println(new String(bytes,0,len));
        }

        //6.释放资源
        inputStream.close();
    }
}
