package com.wang_idea_spider.book;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.getDocumentUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 爬取起点中文网小说
 */
public class book {
    public static void main(String[] args) throws IOException {
        //1.确定爬取的url地址
        String url = "https://read.qidian.com/chapter/vW56kShhqNFH9vdK3C5yvw2/ZNgCC6rIU1Fp4rPq4Fd4KQ2";

        //将爬起的内容写入搭配文件中
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("我有一扇洪荒门.txt")));
        while (true) {
            //2.根据url地址，获取document对象
            Document document = new getDocumentUtils().getDocument(url);

            //3.通过document对象获取数据:章节名、作者、内容
            //class属性选择器: .class
            //书名
            String bookName = document.select(".book-cover-wrap h1").text();
            System.out.println(bookName);

            //章节名 h3为什么不能加
            // String chapterName= document.select(".j_chapterName h3").text();
            String chapterName = document.select(".j_chapterName").text();
            System.out.println(chapterName);
            bufferedWriter.write(chapterName);
            bufferedWriter.newLine();

            //内容
            //class 值有空格  就直接令等于class
            //方法一：直接打印text  打印结果会与原本的格式出现问题
            // String text = document.select("[class = read-content j_readContent]").text();
            // System.out.println(text);

            //加入 p 标签  document.select返回的是一个list类型  将element.text文本内容打印   直接打印element会将<p>也打印出来
            Elements elements = document.select("[class = read-content j_readContent] p");
            for (Element element : elements) {
                System.out.println(element.text());
                bufferedWriter.write(element.text());
                bufferedWriter.newLine();//一个空行
            }

            //获取下一章的url地址
            //id选择器   #id
            // <a id="j_chapterNext" href="//read.qidian.com/chapter/vW56kShhqNFH9vdK3C5yvw2/X4UdS4sCs6r6ItTi_ILQ7A2" data-eid="qd_R109" >下一章</a>
            String href = document.select("#j_chapterNext").attr("href");
            url = "https:" + href;

            //System.out.println("url = " + url);

            //跳出最后一页
            //url = https://read.qidian.com/lastpage/1015538036
            if (url.contains("lastpage")) {
                break;
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();

    }
}
