package com.wang_idea_spider.entertainmentnews.News163.pojo;

/**
 * 新闻数据的pojo
 */
public class NewPojo {


    /**
     * 唯一标识id： String    分布式id
     */
    private long id;

    //标题
    private String title;

    //来源
    private String source;

    //时间
    private String time;

    //链接
    private String url;

    //编辑
    private String editor;

    //内容
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NewPojo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", editor='" + editor + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

