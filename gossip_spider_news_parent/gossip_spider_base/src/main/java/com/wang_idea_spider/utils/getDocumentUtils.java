package com.wang_idea_spider.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class getDocumentUtils {
    private String url =null;
    private Document document = null;

    public void setUrl(String url) {
        this.url = url;
    }

    public getDocumentUtils() {
    }

    public Document getDocument(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String html = EntityUtils.toString(response.getEntity(), "utf-8");

        document = Jsoup.parse(html);
        return document;
    }
}
