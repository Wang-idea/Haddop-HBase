package solrcloud;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * 查询索引
 */
public class IndexSeacherTest {
    //zookeeper 集群地址
    private static final String zkHost="node01:2181,node02:2181,node03:2181";

    private CloudSolrServer cloudSolrServer =null;
    /**
     * 初始化，构建连接对象
     */
    @Before
    public void init(){
        //1. 创建SolrCloud的连接对象
        cloudSolrServer = new CloudSolrServer(zkHost);

        //2. 设置默认写入的Collection
        cloudSolrServer.setDefaultCollection("collection");
    }


    /**
     * 查询索引
     */
    @Test
    public void indexSearch() throws SolrServerException, IOException {

        //3. 创建查询对象
        SolrQuery solrQuery = new SolrQuery("*:*");

        //4. 执行查询
        QueryResponse response = cloudSolrServer.query(solrQuery);

        //5. 遍历打印结果
        SolrDocumentList documents = response.getResults();

        for (SolrDocument document : documents) {
            String id = (String) document.get("id");
            System.out.println("id = " + id);
            String title = (String) document.get("title");
            System.out.println("title = " + title);
            String content = (String) document.get("content");
            System.out.println("content = " + content);
            String url = (String) document.get("url");
            System.out.println("url = " + url);
            String editor = (String) document.get("editor");
            System.out.println("editor = " + editor);
            String resource = (String) document.get("resource");
            System.out.println("resource = " + resource);
            Date time = (Date) document.get("time");
            System.out.println("time = " + time);
        }

        //5. 提交
        cloudSolrServer.commit();

        //6. 释放资源
        cloudSolrServer.shutdown();
    }
}
