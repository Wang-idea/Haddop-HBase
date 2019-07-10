package utils;

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

    public getDocumentUtils() {
    }

    public Document getDocument(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String html = EntityUtils.toString(response.getEntity(), "utf-8");

        document = Jsoup.parse(html);
        return document;
    }
}

