package search.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import search.pojo.News;

import java.util.List;
import java.util.Map;

/**
 * 高亮 排序  分页  加权因子
 */
public class IndexSeniorSearchTest {
    /**
     * solr服务器的url地址
     * 浏览器中的地址：http://localhost:8080/solr/#/solr-test
     * java代码连接的地址：http://localhost:8080/solr/solr-test
     */

    private static final String Url = "http://localhost:8080/solr/solr-test";
    private HttpSolrServer httpSolrServer = null;

    /**
     * 初始化方法
     */
    @Before
    public void init() {
        httpSolrServer = new HttpSolrServer(Url);
    }

    /**
     * 排序条件查询
     */
    @Test
    public void sortSearch() throws SolrServerException {
        //1. 创建solr的连接对象

        //2. 进行查询
        SolrQuery solrQuery = new SolrQuery("*:*");

        //3. 设置排序条件
        //排序字段 ORDER.desc 降序   ORDER.asc 升序

        solrQuery.setSort("click", SolrQuery.ORDER.desc);
        solrQuery.setRows(50);

        //4. 执行查询
        QueryResponse response = httpSolrServer.query(solrQuery);

        //5. 获取数据打印
        List<News> list = response.getBeans(News.class);
        for (News news : list) {
            System.out.println("news = " + news);
        }

        //6. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * 分页查询
     */
    @Test
    public void indexSearchPage() throws SolrServerException {
        Integer page = 3;

        Integer pagesize = 15;

        //计算起始下标
        Integer start = (page -1) * pagesize;

        //1.创建solr的连接对象

        //2.  进行查询
        SolrQuery solrQuery = new SolrQuery("*:*");

        //3. 设置分页查询条件 start rows
        solrQuery.setStart(start);
        solrQuery.setRows(pagesize);

        //4. 执行查询
        QueryResponse response = httpSolrServer.query(solrQuery);

        //5. 获取数据打印
        List<News> list = response.getBeans(News.class);

        for (News news : list) {
            System.out.println("news = " + news);
        }

        //6. 释放资源
        httpSolrServer.shutdown();
    }

    /**
     * 高亮查询
     */
    @Test
    public void indexSearchHighlight() throws SolrServerException {
        //1. 创建solr的连接对象

        //2. 创建查询对象
        SolrQuery solrQuery = new SolrQuery("title:蓝瘦香菇");

        //2.1 开启高亮查询
        solrQuery.setHighlight(true);

        //2.2 设置高亮字段
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");

        //2.3 设置高亮的前缀和后缀
        solrQuery.setHighlightSimplePre("<em style='color:red'>");
        solrQuery.setHighlightSimplePost("</em>");

        //3. 执行查询
        QueryResponse response = httpSolrServer.query(solrQuery);

        //4. 获取相应数据，打印结果
        List<News> list = response.getBeans(News.class);

        //获取所有的高亮内容
        //key:String  value: Map
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        //将高亮的内容获取出来，替换news对象上不高亮的内容:title content
        for (News news : list) {
            //根据文档的id，获取当前文档的高亮内容
            String id = news.getId();
            //key:id   value:map
            Map<String, List<String>> map = highlighting.get(id);

            //获取title的高亮部分
            List<String> titleList = map.get("title");
            if (titleList != null&&titleList.size()>0){
                String htitle = titleList.get(0);
                //替换不亮的title
                news.setTitle(htitle);
            }
            //获取content的高亮部分
            List<String> contentList = map.get("content");
            if(contentList != null && contentList.size() > 0){
                String hiContent = contentList.get(0);
                //替换高亮content
                news.setContent(hiContent);
            }


            System.out.println(news);
        }

        //5.释放资源
        httpSolrServer.shutdown();
        }


}

