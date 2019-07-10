package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import utils.indexSearchUtils;

import java.io.IOException;

/**
 * 索引排序
 */
public class IndexSearchSort {
    @Test
    public  void sortTest() throws IOException, ParseException {
        //1. 创建索引搜索器
        IndexSearcher indexSearcher = new indexSearchUtils().getSearch();

        //2. 创建查询对象
        /**
         * 带分词的查询，对你输入的内容先分词，在进行查询
         */
        QueryParser parser = new QueryParser("tiele", new IKAnalyzer());
        Query query = parser.parse("蓝瘦香菇是好东西，我们都需要");

        //添加排序：默认 打分排序  改排序条件
        /**
         * 创建排序条件：
         * 第一参数排序字段
         * 第二参数字段的类型
         * 第三参数升序: false  降序: true
         */
        String sortField ="click";
        Sort sort = new Sort(new SortField(sortField, SortField.Type.LONG, true));
        //3. 执行查询
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);

        //4. 遍历结果
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //获取文档编号
            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println("document = " + document);

            String tiele = document.get("tiele");
            System.out.println("未高亮 = " + tiele);
            String click = document.get("click");
            System.out.println("click = " + click);
            System.out.println("得分"+scoreDoc.score);
        }
    }
}