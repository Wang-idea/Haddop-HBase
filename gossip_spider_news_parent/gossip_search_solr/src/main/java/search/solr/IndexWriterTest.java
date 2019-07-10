package search.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import search.pojo.News;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 索引写入：创建索引 修改索引 删除索引
 */
public class IndexWriterTest {
    /**
     * solr服务器的url地址：
     * 浏览器中的地址：http://localhost:8080/solr/#/solr-test
     * java代码连接的地址：http://localhost:8080/solr/solr-test
     */
    private static final String baseUrl = "http://localhost:8080/solr/solr-test";

    /**
     * 创建索引
     */
    @Test
    public void indexWriterTest() throws IOException, SolrServerException {
        //1.创建solr的连接对象,连接集群版本使用CloudSolrServer
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建document对象
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        //2.1 添加
        solrInputDocument.addField("id", "1");
        solrInputDocument.addField("title", "我爱我的祖国,中华人民共和国");
        solrInputDocument.addField("content", "岳云鹏见粉丝摔倒不扶还超有梗");
        solrInputDocument.addField("click", "10000");
        solrInputDocument.addField("docurl", "https://xw.qq.com/cmsid/20190701V09GRE00");

        //3. 调用httpSolrServer俩接对象，发送http请求，完成创建索引的操作
        httpSolrServer.add(solrInputDocument);

        //4. 提交
        httpSolrServer.commit();

        //5. 关闭资源
        httpSolrServer.shutdown();
    }


    /**
     * 一次写入多个document对象写入索引库
     * 将docment加入到list中 然后进行一次提交
     * 如果不用list  而是document对象进行提交 则每一个document对象都要提交一次  很慢
     */
    @Test
    public void indexWritersTest() throws IOException, SolrServerException {
        //1. 创建solr连接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建document对象的列表
        ArrayList<SolrInputDocument> list = new ArrayList<>();

        for (int i = 0; i <= 100; i++) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", i);
            document.addField("title", "我爱我的祖国,中华人民共和国" + i);
            document.addField("content", "岳云鹏见粉丝摔倒不扶还超有梗" + i);
            document.addField("click", "10000" + i);
            document.addField("docurl", "https://xw.qq.com/cmsid/20190701V09GRE00" + i);
            list.add(document);
        }
        //3. 调用httpSolrServer连接对象，发送http请求，完成创建索引的操作
        httpSolrServer.add(list);

        //4.提交
        httpSolrServer.commit();

        //5. 释放资源
        httpSolrServer.shutdown();

    }

    /**
     * javaBean写入索引
     * 将原始文档(数据库新闻数据) 封装成javaBean，将javaBean写入索引库
     */
    @Test
    public void indexWriterJavaBean() throws IOException, SolrServerException {
        //1.  创建solr的连接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2.创建javaBean对象
        News news = new News();
        news.setId("888");
        news.setTitle("我是特殊的标题大家盘他，看起来腿真长，腿玩年");
        news.setContent("我是特殊的标题");
        news.setUrl("http://www.itcast.cn");
        news.setClick(888888l);

        //3. 将javaBean添加到索引库
        httpSolrServer.addBean(news);

        //4. 提交
        httpSolrServer.commit();

        //5. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * javaBean：修改索引
     * 修改索引:只要id相同，就是修改：先删除后添加
     */
    @Test
    public void modifyIndexBean() throws IOException, SolrServerException {
        //1. 创建solr的连接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        // 2. 创建要修改的文档对象
        News news = new News();
        news.setId("666");
        news.setTitle("我是被修改的文档,蓝瘦香菇");
        news.setContent("我是被修改的文档,蓝瘦香菇");
        news.setUrl("http://www.baidu.com");
        news.setClick(20000l);


        //3. 修改  (调用添加的方法)
        httpSolrServer.addBean(news);


        //4. 提交
        httpSolrServer.commit();

        //5. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * document
     * 修改索引：只要id相同，就是修改：先删除，后添加
     */
    @Test
    public void modifyIndexDocument() throws IOException, SolrServerException {
        //1. 创建solr连接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建要修改的文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id","666");
        document.setField("title","我是被修改的文档,蓝瘦香菇");
        document.setField("content","我是被修改的文档,蓝瘦香菇");
        document.setField("docurl","http://www.baidu.com");
        document.setField("click","66666");

        //3. 修改
        httpSolrServer.add(document);

        //4. 提交
        httpSolrServer.commit();

        //5. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * 删除索引库:  根据id删除     根据查询删除   删除所有
     */
    @Test
    public void deleteIndex() throws IOException, SolrServerException {
        //1.创建solr索引连接
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 执行删除
        //2.1 根据id删除
        httpSolrServer.deleteById("");

        //2.2 根据查询删除：写各种查询语法
        //查询语句："id:1"
        httpSolrServer.deleteByQuery("id:1");

        //2.3 删除所有
        httpSolrServer.deleteByQuery("*:*");
        //3. 提交
        httpSolrServer.commit();

        //4. 释放资源
        httpSolrServer.shutdown();

    }

    /**
     * 加权因子
     */
    @Test
    public void indexWriterBoost() throws IOException, SolrServerException {
        //1. 创建solr的连接对象，连接集群版本使用CloudSolrServer
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建document对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","999");

        //带加权因子的字段: 加权默认是1
        document.addField("title","我爱我的祖国,中华人民共和国蓝瘦香菇盘她",10000);
        document.addField("content","岳云鹏见粉丝摔倒不扶还超有梗");
        document.addField("click","100008");
        document.addField("docurl","https://xw.qq.com/cmsid/20190701V09GRE00");

        //3. 调用httpSolrServer连接对象,发送http请求,完成创建索引的操作
        httpSolrServer.add(document);

        //4. 提交
        httpSolrServer.commit();

        //5. 释放资源
        httpSolrServer.shutdown();
    }
}
