package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import utils.indexSearchUtils;

import java.io.IOException;

/**
 * 高亮
 */
public class IndexSearchhighterTest {
    /**
     * 高亮原理：对关键字加<高亮前缀>关键字</高亮后缀>
     * 索引库中没有高亮(只存了词条在文档的位置信息)
     * 在查询结果出来后，二次添加高亮信息
     */
    @Test
    public void highLightTest() throws IOException, ParseException, InvalidTokenOffsetsException {
        //1. 创建索引搜索器
        IndexSearcher indexSearcher = new indexSearchUtils().getSearch();

        //2. 创建查询对象
        /**
         * 带分词的查询，对你输入的内容先分词，在进行查询
         */
        QueryParser parser = new QueryParser("tiele", new IKAnalyzer());
        Query query = parser.parse("蓝瘦香菇是好东西，我们都需要");

        //设置高亮信息
        String preTag = "<font color='#cc0000'>";//前缀
        String postTag = "</font>";//后缀

        //格式化器
        Formatter formatter = new SimpleHTMLFormatter(preTag,postTag);

        //查询关键字和高亮器之间的联系
        Scorer scorer = new QueryScorer(query);;

        Highlighter highlighter = new Highlighter(formatter, scorer);

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

            //用高亮器工具处理普通的擦汗讯结果，参数:分词器，要高亮的字段名称，高亮字段的原始值
            //第二个参数  高亮的内容
            //第三个参数  未高亮的内容
            String htiele = highlighter.getBestFragment(new IKAnalyzer(), "tiele", tiele);

            System.out.println("htiele = " + htiele);
            System.out.println("得分"+scoreDoc.score);
        }
    }


}
