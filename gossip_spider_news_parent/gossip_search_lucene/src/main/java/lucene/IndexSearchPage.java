package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.surround.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import utils.indexSearchUtils;

import java.io.IOException;

public class IndexSearchPage {
    /**
     * 分页查询: 不需要掌握   后面掌握solr的分页
     */
    @Test
    public void pageQuery() throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {

        Integer page = 1;//起始页

        Integer pageSize = 15;//最后一页

        //获取索引搜索器
        IndexSearcher indexSearcher = new indexSearchUtils().getSearch();

        //1. 获取查询对象
        QueryParser parser = new QueryParser("tiele", new IKAnalyzer());

        Query query = parser.parse("蓝瘦香菇是好东西，我们都需要");

        //2. 执行查询
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);

        System.out.println("本次搜索共" + topDocs.totalHits + "条数据");

        //3. 获取分页结果
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //获取第二页的数据

        //第二页的第一个数据
        Integer start = (page - 1) * pageSize;

        //第二页的最后一个数据
        Integer end = start + pageSize - 1;

        for (int i = start; i <= end; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];

            // 获取文档编号
            int docID = scoreDoc.doc;
            Document doc = indexSearcher.doc(docID);
            System.out.println("id: " + doc.get("id"));

            String title = doc.get("title");
            System.out.println("未高亮的：" + title);

            String click = doc.get("click");
            System.out.println("点击量:" + click);

            // 获取文档的得分
            System.out.println("得分：" + scoreDoc.score);

        }

    }
}
