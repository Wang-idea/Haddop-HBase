package com.wang_idea_spider.jsoup;

import com.wang_idea_spider.utils.getDocumentUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;


/**
 * 使用jsoup解析转换好的document对象
 */
public class JsoupParseDocument {
    /**
     * 使用原生JavaScript类似的getElement的方法解析数据
     */
    @Test
    public void testBygetElement() throws IOException {
        String  url = "http://www.itcast.cn/";

        Document document = new getDocumentUtils().getDocument(url);

        //解析document，获取想要的数据
        //getElementsByClass 除了id 都是数组
        Elements nav_txt = document.getElementsByClass("nav_txt");
        Element div = nav_txt.get(0);


        Elements as = div.getElementsByTag("a");
        for (Element a : as) {
            //将a的文本
            System.out.println("a.text() = " + a.text());
            //a.attr
            System.out.println(a.attr("href"));
        }
    }

    /**
     * 使用选择器
     */

    @Test
    public void testBygetElementSelector() throws IOException {
        String  url = "http://www.itcast.cn/";

        Document document = new getDocumentUtils().getDocument(url);

        //使用选择器获取标签值
        //cssQuery: 选择器 标签名
        //class选择器：class=.nav_txt   a标签
        Elements list = document.select(".nav_txt a");
        for (Element element : list) {
            System.out.println(element.text()+"...."+element.attr("href"));
        }
    }
}
