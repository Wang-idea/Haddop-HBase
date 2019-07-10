package com.wang_idea_spider.jdk;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 字符流读取
 * jdk原生方式post请求获取html页面
 */
public class JdkPostOutputStream {
    public static void main(String[] args) throws IOException {
        //1. 确定爬取的url（不要丢失协议）
        String domain = "http://www.itcast.cn";


        //2. 发送请求，获取数据html页面
        URL url = new URL(domain);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


        //原生jdk默认关闭了输出流,传递请求参数需要设置为true
        urlConnection.setDoOutput(true);

        //设置请求方式及请求参数：
        urlConnection.setRequestMethod("POST");
        //以输出流的方式写post数据
        OutputStream outputStream = urlConnection.getOutputStream();
        String postparam = "username=zhangsan&age=32";
        outputStream.write(postparam.getBytes());
        outputStream.flush();
        outputStream.close();

        //获取html输入流
        InputStream inputStream = urlConnection.getInputStream();

        //字符流读取数据
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = null;
        while((line = bufferedReader.readLine())!=null){
            System.out.println(line);
        }

        inputStream.close();
        bufferedReader.close();
    }
}
