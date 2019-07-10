package lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;
import utils.indexWriterUtils;

import java.io.IOException;

public class IndexSearchWeighting {
    /**
     * 使用ik分词器创建索引: 使用了加权
     */
    @Test
    public void indexWriteWeightTest() throws IOException {

        IndexWriter indexWriter = new indexWriterUtils().getIndexWriter();


        //4. 创建document文档对象
        Document doc = new Document();
        /**
         * stringField: 不分词  可以搜索   存储到索引库
         */
        doc.add(new StringField("id","999999999999999", Field.Store.YES));

        /**
         * TextField:  分词   可以搜索    存储到索引
         */
        TextField title = new TextField("tiele", "我爱中华人民共和国,盘她", Field.Store.YES);

        // 加权重
        title.setBoost(10000f);
        doc.add(title);


        doc.add(new TextField("content","张柏芝腿玩年和儿子生活丰富多彩，次子当街热舞潮盘她范十足", Field.Store.YES));


        /**
         * 只存储, 不分词  不能搜索
         */
        doc.add(new StoredField("url","https://xw.qq.com/cmsid/20190630V0BKPQ00"));


        /**
         * LongField:  可以搜索   分词    存储到索引库
         */
        doc.add(new LongField("click",888888888l, Field.Store.YES));


        //5. 调用索引写入器的索引写入方法
        indexWriter.addDocument(doc);


        //6. 提交
        indexWriter.commit();

        //7. 关闭资源
        indexWriter.close();

    }
}
