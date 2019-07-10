package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import utils.executeQueryutils;

import java.io.File;
import java.io.IOException;

/**
 * 进行索引搜索
 */
public class IndexSearchTest {
    @Test
    public void indexSeacherTest() throws IOException, ParseException {
        //1. 创建索引库目录  要进行索引搜索，必须有一个索引库  才能搜索
        FSDirectory directory = FSDirectory.open(new File("IndexSearchTest/database"));

        //2. 索引读取器：读取索引库
        DirectoryReader directoryReader = DirectoryReader.open(directory);

        //3. 创建索引搜索器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        //4. 创建查询对象
        //"tiele" 要和索引库中的一模一样
        String fileName = "tiele";
        QueryParser queryParser = new QueryParser(fileName, new IKAnalyzer());
        Query query = queryParser.parse("云雷");
        //5.调用索引搜索器的方法进行搜索
        /**
         * 第一个参数：查询对象
         * 第二个对象：返回记录的个数
         */
        TopDocs topDocs = indexSearcher.search(query,Integer.MAX_VALUE);

        //6. 获取查询结果总条数
        int totalHits = topDocs.totalHits;
        System.out.println("totalHits = " + totalHits);

        //获取搜索结果的文档id数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //文档的数量
            float score = scoreDoc.score;
            System.out.println("score = " + score);
            //获取文档的id
            int id = scoreDoc.doc;
            //根据文档id获取文档对象
            Document document = indexSearcher.doc(id);
            //字段id
            String id1 = document.get("id");
            String title = document.get("title");
            String content = document.get("content");
            System.out.println(id1+ title+ content);
        }
        directory.close();
    }

    /**
     * 使用TermQuery查询对象进行查询
     */
    @Test
    public void indexTermSearch() throws IOException {
        FSDirectory directory = FSDirectory.open(new File("IndexSearchTest/database"));

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        //创建TermQuery对象
        /**
         * 词条不会进行分词，必须是完全匹配索引库的词条才能搜索出结果
         */
        String filedName = "tiele";

        String queryTermString ="蓝瘦";
        TermQuery termQuery = new TermQuery(new Term(filedName, queryTermString));

        TopDocs topDocs = indexSearcher.search(termQuery, Integer.MAX_VALUE);

        int totalHits = topDocs.totalHits;

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            float score = scoreDoc.score;
            System.out.println("score = " + score);

            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);

            System.out.println("doc = " + document);
            String id = document.get("id");
            String title = document.get("title");
            String content = document.get("content");

            System.out.println(id+ title+ content);
        }

        directory.close();
    }


    /**
     * 调用executeQuery方法进行查询
     */

    @Test
    public void indexSearchTermQuery() throws IOException {
        //创建TermQuery对象
        /**
         * 词条不会进行分词的, 词条可以是一个字, 也可以是一句话
         *
         * 必须完全匹配索引库中的一个词条,才能搜索出来结果
         *
         *
         */
        String fieldName = "tiele";
        String queryTermString = "蓝瘦";
        TermQuery query = new TermQuery(new Term(fieldName, queryTermString));

        //调用共用的执行查询的方法
        executeQuery(query);
    }

    /**
     * 抽取出来的方法
     */
    public void executeQuery(Query query) throws IOException {
        //1. 创建索引库的目录对象
        FSDirectory directory = FSDirectory.open(new File("IndexSearchTest/database"));

        //2. 创建索引读取器
        DirectoryReader directoryReader = DirectoryReader.open(directory);

        //3. 创建索引搜索器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        //4.执行查询
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);


        //5. 获取结果总记录数
        int totalHits = topDocs.totalHits;
        System.out.println("获取总记录数:" + totalHits);


        //6. 获取搜索到的文档id集合
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //7.遍历集合,获取文档对象, 打印结果
        for (ScoreDoc scoreDoc : scoreDocs) {

            float score = scoreDoc.score;
            System.out.println("得分: " + score);

            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);

            //文档字段的id
            String id = document.get("id");

            //文档的标题
            String title = document.get("title");

            //文档的内容
            String content = document.get("content");

            //文档的url
            String url = document.get("url");
            //文档的点击量
            String click = document.get("click");

            //打印结果
            System.out.println("id: " + id );
            System.out.println("title: "  + title);
            System.out.println("content: "  + content );
            System.out.println("url: " + url);
            System.out.println("click: "  + click);

        }

        //8. 关闭资源
        directoryReader.close();
        directory.close();
    }

    /**
     * 通配符查询：* 0-n ? 1个字符
     */
    @Test
    public void indexWildcardSearch() throws IOException {
        //1. 创建一个查询对象
        String fieldName = "tiele";
        WildcardQuery wildcardQuery = new WildcardQuery(new Term(fieldName, "云*"));
        System.out.println("wildcardQuery = " + wildcardQuery);//tiele:云*

        //2. 调用查询的方法
        new executeQueryutils().executeQuery(wildcardQuery);
    }

    /**
     * 模糊查询 只要你的查询字符串经过最多两次编辑 能够匹配上索引库中的词条
     * 移动位置，调换位置 补位 移动
     * 最大编辑次数：2次
     */
    @Test
    public void FuzzQueryTest() throws IOException {
        //1. 创建查询对象
        String fieldName = "tiele";
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(fieldName, "云"));

        //张~2
        System.out.println("fuzzyQuery = " + fuzzyQuery);
        //2. 执行查询打印结果
        new executeQueryutils().executeQuery(fuzzyQuery);
    }

    /**
     * 范围查询：点击量[10000,20000]  价格[6720,8200]
     * 应该对于可量化的字段进行：点击量 价格 销量 排名 成绩
     */
    @Test
    public void numberRangeQuery() throws IOException {
        //1. 创建查询对象
        String fieldName = "click";
        /**
         * 第一个参数：搜索的字段
         * 第二个参数：最小值
         * 第三个参数：最大值
         * 第四个参数：是否包含最小值
         * 第五个参数：是否包含最大值
         */
        NumericRangeQuery<Long> rangeQuery = NumericRangeQuery.newLongRange(fieldName, 20000L, 20050L, false, true);

        //查询语法：click:[10000 TO 20050] 查询语法：click :{10000 TO 20050}
        System.out.println("rangeQuery = " + rangeQuery);

        //2. 执行查询
        new executeQueryutils().executeQuery(rangeQuery);
    }

    /**
     * 布尔查询: && 与  || 或  ！非
     *
     * MUST 与
     * MUST_NOT  非
     * SHOULD  或
     */
    @Test
    public void booleanQuery() throws IOException {
        BooleanQuery booleanClauses = new BooleanQuery();

        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("tiele", "云"));
        booleanClauses.add(fuzzyQuery, BooleanClause.Occur.MUST);

        booleanClauses.add(fuzzyQuery,BooleanClause.Occur.MUST_NOT);

        booleanClauses.add(fuzzyQuery,BooleanClause.Occur.SHOULD);

        //2. 执行查询
        new executeQueryutils().executeQuery(fuzzyQuery);
        new executeQueryutils().executeQuery(fuzzyQuery);
        new executeQueryutils().executeQuery(fuzzyQuery);
    }
}
