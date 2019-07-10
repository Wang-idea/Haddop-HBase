package solrcloud;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solrcloud.pojo.News;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 索引写入相关操作:索引创建 索引修改  索引删除
 */
public class IndexWriteTest {
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
     *索引创建
     */
    @Test
    public void indexWriter() throws IOException, SolrServerException {
        //1. 创建SolrCloud的连接对象
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);

        //2. 设置默认写入的Collection
        cloudSolrServer.setDefaultCollection("collection");

        //3. 创建document文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id","1146349044164067330");
        document.setField("title","《大力水手》作者莫迪洛逝世，他曾表示：我的第三次重生在中国");
        document.setField("content","《大力水手》作者、漫画大师莫迪洛逝世");
        document.setField("url","https://xw.qq.com/cmsid/20190702V0HEXF00");
        //不是utc标准给事：默认进去减少八个小时
        document.setField("time",new Date());
        document.setField("source","腾讯");
        document.setField("editor","腾讯");

        //4. 写入索引
        cloudSolrServer.add(document);

        //5. 提交
        cloudSolrServer.commit();

        //6. 释放
        cloudSolrServer.shutdown();
    }


    /**
     * JavaBen 索引写入的操作
     */
    @Test
    public void indexWriteJavaBean() throws IOException, SolrServerException {

        //1、2 已经初始化

        //3. 创建News对象
        News news = new News();
        news.setId("1146941842215010306");
        news.setTitle("外媒曝BCC将拍《安娜卡列尼娜》剧集，《名利场》编剧加盟");
        news.setContent("外媒曝BCC将拍《安娜卡列尼娜》剧集，《名利场》编剧加盟");
        news.setEditor("猫眼电影");

        //需要给solr标准的日期格式数据
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        news.setTime(format.format(new Date()));
        news.setSource("猫眼电影");
        news.setUrl("https://xw.qq.com/cmsid/20190704A0PVUC00");

        //4. 写入索引库
        cloudSolrServer.addBean(news);

        //5. 提交
        cloudSolrServer.commit();

        //6. 释放资源

    }

    /**
     * 修改索引库
     */
    @Test
    public void modify() throws IOException, SolrServerException {
        //3. 创建News对象
        News news = new News();
        news.setId("1146941842215010306");
        news.setTitle("刘德华喊话四大天王合体做节目了！有生之年能等到吗");
        news.setContent("刘德华喊话四大天王合体做节目了！有生之年能等到吗");
        news.setEditor("猫眼电影");

        //需要给solr标准的日期格式数据
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        news.setTime(format.format(new Date()));
        news.setSource("猫眼电影");
        news.setUrl("https://xw.qq.com/cmsid/20190703V03THA00");

        //4. 修改索引库
        cloudSolrServer.addBean(news);

        //5. 提交
        cloudSolrServer.commit();
    }

    /**
     * 索引写入多个javabean的操作 :javabean
     */
    @Test
    public  void  indexWriterJavaBeans() throws IOException, SolrServerException {
        //1,2 已经初始化



        //3. 创建News对象
        List<News> list = new ArrayList<News>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        for(int i=1;i<=2000;i++){
            News news = new News();
            news.setId("1146941842215010306"+i);
            news.setTitle("外媒曝BCC将拍《安娜卡列尼娜》剧集，《名利场》编剧加盟"+i);
            news.setContent("外媒曝BCC将拍《安娜卡列尼娜》剧集，《名利场》编剧加盟"+i);
            news.setEditor("猫眼电影"+i);

            //需要给solr标准的日期格式数据
            Date date = new Date();
            news.setTime(format.format(date));
            news.setSource("猫眼电影");
            news.setUrl("https://xw.qq.com/cmsid/20190704A0PVUC00");
            list.add(news);
        }


        //4. 写入索引库
        cloudSolrServer.addBeans(list);


        //5. 提交
        cloudSolrServer.commit();

    }



    /**
     * 修改索引库
     */
    @Test
    public void updateIndex() throws IOException, SolrServerException {
        //1.2
        //3. 创建News对象
        News news = new News();
        news.setId("1146941842215010306");
        news.setTitle("迪士尼，快把我的小美人鱼还给我");
        news.setContent("迪士尼，快把我的小美人鱼还给我");
        news.setEditor("狗眼电影");

        //需要给solr标准的日期格式数据
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        news.setTime(format.format(date));
        news.setSource("狗眼电影");
        news.setUrl("https://xw.qq.com/cmsid/20190704A0PVUC00888");


        //4. 修改索引
        cloudSolrServer.addBean(news);

        //5. 提交
        cloudSolrServer.commit();


    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException, SolrServerException {
        //3. 删除索引
        //3.1 根据id删除
        cloudSolrServer.deleteById("1146941842215010306");
        //3.2 删除所有
        cloudSolrServer.deleteByQuery("*:*");

        //4. 提交
        cloudSolrServer.commit();
    }
    /**
     * 释放资源
     */
    @After
    public void Release(){
        cloudSolrServer.shutdown();
    }
}
