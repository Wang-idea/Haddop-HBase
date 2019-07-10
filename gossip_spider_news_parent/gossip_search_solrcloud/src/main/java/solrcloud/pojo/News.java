package solrcloud.pojo;

import org.apache.solr.client.solrj.beans.Field;

public class News {
    @Field
    private String id;

    @Field
    private String title;

    @Field
    private String url;

    @Field
    private String content;

    @Field
    private String editor;

    @Field
    private String source;

    @Field
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", editor='" + editor + '\'' +
                ", source='" + source + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
