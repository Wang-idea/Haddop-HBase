package utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class executeQueryutils {

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
        System.out.println("totalHits总记录数:" + totalHits);


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
            String title = document.get("tiele");

            //文档的内容
            String content = document.get("content");

            //文档的点击量
            String click = document.get("click");

            //文档的url
            String url = document.get("url");


            //打印结果
            System.out.println("id: " + id);
            System.out.println("tiele: " + title);
            System.out.println("content: " + content);
            System.out.println("url: " + url);
            System.out.println("click: " + click);
        }

        //8. 关闭资源
        directoryReader.close();
        directory.close();
    }
}
