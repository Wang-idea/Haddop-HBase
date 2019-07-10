package search.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import search.pojo.News;

import java.util.List;

/**
 * 索引搜索
 */
public class IndexSearchTest {
    /**
     * solr服务器的url地址：
     * 浏览器中的地址：http://localhost:8080/solr/#/solr-test
     * java代码连接的地址：http://localhost:8080/solr/solr-test
     */
    private static final String baseUrl = "http://localhost:8080/solr/solr-test";

    /**
     * 搜索索引：各种查询
     */
    @Test
    public void indexSearch() throws SolrServerException {
        //1. 创建solr的连接诶对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建查询对象
        //2.1 查询所有数据
        //SolrQuery solrQuery = new SolrQuery("*:*");

        //根据查询语法查询
        SolrQuery solrQuery = new SolrQuery("title:国");

        //3. 执行查询
        QueryResponse response = httpSolrServer.query(solrQuery);

        //4. 获取响应数据
        SolrDocumentList results = response.getResults();

        //5. 遍历打印文档
        for (SolrDocument document : results) {
            //获取文档内容
            String id = (String) document.get("id");
            System.out.println("id = " + id);

            String title = (String) document.get("title");
            System.out.println("title = " + title);

            String content = (String) document.get("content");
            System.out.println("content = " + content);

            Long click = (Long) document.get("click");
            System.out.println("click = " + click);
        }
        //5. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * 查询结果返回javaBean对象  javabean的list列表
     */
    @Test
    public void indexSearchJavaBean() throws SolrServerException {
        //1. 创建httpSolrServer对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建查询对象
        //2.1 查询所有数据
        //SolrQuery solrQuery = new SolrQuery("*:*");

        //2.2  条件查询
        SolrQuery solrQuery = new SolrQuery("title:国");

        //3. 执行查询
        QueryResponse response = httpSolrServer.query(solrQuery);

        //4. 获取响应数据
        List<News> list = response.getBeans(News.class);

        //5. 遍历查询的数据
        for (News news : list) {
            System.out.println("news = " + news);
        }

        //6. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * 高级查询内容:   通配符   模糊    区间范围查询
     * 布尔查询:AND  与   OR  或    NOT  非    子查询(改变优先级)
     */
    @Test
    public void seniorSearch() throws SolrServerException {

        //1. 创建solr的连接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);

        //2. 创建查询休息：根据查询语法查询
        //2.1 通配符查询：* ？
        // SolrQuery solrQuery = new SolrQuery("title:祖?");

        //2.2 模糊查询:~
        //腿玩年
        //SolrQuery solrQuery = new SolrQuery("title:腿~2");

        //2.3 区间范围查询: {min TO max}  [min TO max] {min TO max] [min TO max}
        //SolrQuery solrQuery = new SolrQuery("click:[10000 TO 20000]");

        //2.4 布尔查询 AND  与   OR  或    NOT  非
        //2.5 子查询  改变优先级
        SolrQuery solrQuery = new SolrQuery("( title:祖?  AND click:[10000 TO *] ) OR ( title:腿~ ) NOT click:[10050 TO 10080]");

        //设置打印出来的个数
        solrQuery.setRows(Integer.MAX_VALUE);


        //3. 执行查询
        QueryResponse response = httpSolrServer.query(solrQuery);

        //4. 获取响应数据
        List<News> list = response.getBeans(News.class);

        //5. 打印结果
        for (News news : list) {
            System.out.println("news = " + news);
        }
        //6. 释放资源
        httpSolrServer.shutdown();
    }
}
