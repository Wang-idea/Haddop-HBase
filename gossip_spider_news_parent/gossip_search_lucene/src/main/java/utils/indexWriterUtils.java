package utils;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class indexWriterUtils {
    public IndexWriter getIndexWriter() throws IOException {
        FSDirectory directory = FSDirectory.open(new File("IndexSearchTest/database"));
        IKAnalyzer ikAnalyzer = new IKAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, ikAnalyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        return indexWriter;
    }
}
