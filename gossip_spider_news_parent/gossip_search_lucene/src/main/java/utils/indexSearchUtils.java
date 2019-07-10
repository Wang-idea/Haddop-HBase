package utils;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class indexSearchUtils {
    public IndexSearcher getSearch() throws IOException {
        //1. 创建索引库目录  要进行索引搜索，必须有一个索引库  才能搜索
        FSDirectory directory = FSDirectory.open(new File("IndexSearchTest/database"));

        //2. 索引读取器：读取索引库
        DirectoryReader directoryReader = DirectoryReader.open(directory);

        //3. 创建索引搜索器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        return indexSearcher;
    }
}
