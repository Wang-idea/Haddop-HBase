package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import utils.indexWriterUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 索引写入测试：创建索引  修改索引 删除索引
 */
public class IndexWriterTest {
    /**
     * 创建索引的方法
     */
    @Test
    public void indexWriterTest01() throws IOException {
        //1. 创建索引库存放的目录结构：磁盘的目录  内存目录
        Directory directory = FSDirectory.open(new File("IndexSearchTest/database"));

        //2. 创建分词器和索引写入器的配置对象
        //标准分词器 不支持中文
        Analyzer analyzer = new StandardAnalyzer();
        /**
         * 第一个参数：版本
         * 第二个参数：分词器
         */
        IndexWriterConfig Config = new IndexWriterConfig(Version.LATEST, analyzer);

        //3. 索引写入器
        /**
         * 第一个参数：写入的目录
         * 第二个参数：写入器配置对象
         */
        IndexWriter indexWriter = new IndexWriter(directory, Config);


        //4. document文档对象
        Document document = new Document();

        /**
         *StringFiled：不分词 可以搜索  存储到索引库
         */
        document.add(new StringField("id", "1146349044109541376", Field.Store.YES));

        /**
         * TextFiled: 可以分词 可以搜索  存储到索引
         */
        document.add(new TextField("tiele", "张云雷调侃事件处罚结果：举办单位吊销演出许可 德云社公开道歉", Field.Store.YES));
        document.add(new TextField("content", "青岛新闻网7月3日讯（见习记者 宋波鸿）近日，青岛市文化市场行政执法局对", Field.Store.YES));

        /**
         * 只存储，不分词
         */
        document.add(new StoredField("url", "https://xw.qq.com/zt/20190513002391/ENT2019051300239100"));

        /**
         * Lonfiled:可以搜索  分词 存储到索引
         */
        document.add(new LongField("click", 20000, Field.Store.YES));

        //5. 调用索引写入器的索引写入方法
        indexWriter.addDocument(document);

        //6. 提交
        indexWriter.commit();

        //7. 关闭资源
        indexWriter.close();
        directory.close();
    }

    @Test
    public void indexWriterTest() throws IOException {
        //1. 创建索引库存放的目录结构：磁盘的目录  内存目录
        Directory directory = FSDirectory.open(new File("IndexSearchTest/database"));

        //2. 创建分词器和索引写入器的配置对象
        //IK分词器:对中文分词
        IKAnalyzer ikanalyzer = new IKAnalyzer();

        /**
         * 第一个参数：版本
         * 第二个参数：分词器
         */
        IndexWriterConfig Config = new IndexWriterConfig(Version.LATEST, ikanalyzer);

        //3. 索引写入器
        /**
         * 第一个参数：写入的目录
         * 第二个参数：写入器配置对象
         */

        IndexWriter indexWriter = new IndexWriter(directory, Config);


        //4. document文档对象
        Document document = new Document();

        /**
         *StringFiled：不分词 可以搜索  存储到索引库
         */
        document.add(new StringField("id", "1146349044109541375", Field.Store.YES));

        /**
         * TextFiled: 可以分词 可以搜索  存储到索引
         */
        document.add(new TextField("tiele", "张云雷调侃事件处罚结果：举办单位吊销演出许可 德云社公开道歉 蓝瘦香菇", Field.Store.YES));
        document.add(new TextField("content", "青岛新闻网7月3日讯（见习记者 宋波鸿）近日，青岛市文化市场行政执法局对张云雷盘他", Field.Store.YES));

        /**
         * 只存储，不分词
         */
        document.add(new StoredField("url", "https://xw.qq.com/zt/20190513002391/ENT2019051300239100"));

        /**
         * Lonfiled:可以搜索  分词 存储到索引
         */
        document.add(new LongField("click", 20000, Field.Store.YES));

        //5. 调用索引写入器的索引写入方法
        indexWriter.addDocument(document);

        //6. 提交
        indexWriter.commit();

        //7. 关闭资源
        indexWriter.close();
        directory.close();
    }

    /**
     * 批量写入索引库  使用ik分词器 进行索引写入
     */
    @Test
    public void indexWriterIk() throws IOException {
        FSDirectory directory = FSDirectory.open(new File("IndexSearchTest/database"));
        IKAnalyzer ikAnalyzer = new IKAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);


        List<Document> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            /**
             *StringFiled：不分词 可以搜索  存储到索引库
             */
            document.add(new StringField("id", "1146349044109541375" + i, Field.Store.YES));

            /**
             * TextFiled: 可以分词 可以搜索  存储到索引
             */
            document.add(new TextField("tiele", "张云雷调侃事件处罚结果：举办单位吊销演出许可 德云社公开道歉 蓝瘦香菇" + i, Field.Store.YES));
            document.add(new TextField("content", "青岛新闻网7月3日讯（见习记者 宋波鸿）近日，青岛市文化市场行政执法局对张云雷盘他" + i, Field.Store.YES));

            /**
             * 只存储，不分词
             */
            document.add(new StoredField("url", "https://xw.qq.com/zt/20190513002391/ENT2019051300239100" + i));

            /**
             * Lonfiled:可以搜索  分词 存储到索引
             */
            document.add(new LongField("click", 20000 + i, Field.Store.YES));

            list.add(document);

        }
        indexWriter.addDocuments(list);
        indexWriter.commit();
        indexWriter.close();
        directory.close();
    }

    /**
     *修改索引库  先删除原来的  然后创建新的
     */
    @Test
    public void editIndex() throws IOException {
        //1. 获取索引写入器
        IndexWriter indexWriter = new indexWriterUtils().getIndexWriter();

        //2. 修改索引库
        //2.1 创建新的文档对象
        Document document = new Document();
        document.add(new StringField("id","888",Field.Store.YES));
        document.add(new TextField("tiele","我爱中华人民共和国，盘她，蓝瘦香菇真实哈东西，腿玩年",Field.Store.YES));
        document.add(new LongField("click",10000,Field.Store.YES));
        document.add(new StoredField("url","http:www.itcast.cn"));

        //根据id字段进行修改：id使我们自己的字段id
        indexWriter.updateDocument(new Term("id","666"),document);

        //3. 提交
        indexWriter.commit();

        //4.关闭资源
        indexWriter.close();
    }

    /**
     * 删除索引库
     */
    @Test
    public void deleteIndex() throws IOException {
        //1. 获取索引写入器
        IndexWriter indexWriter = new indexWriterUtils().getIndexWriter();

        //2 进行删除
        //2.1 根据查询删除
        Query query = new TermQuery(new Term("id","666"));
        indexWriter.deleteDocuments(query);

        //2.2 根据term删除
        indexWriter.deleteDocuments(new Term("id","66"));

        //2.3 删除所有
        indexWriter.deleteAll();
        //3. 提交
        indexWriter.commit();

        //4. 关闭资源
        indexWriter.close();
    }
}
