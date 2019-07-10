package search.pojo;

import org.apache.solr.client.solrj.beans.Field;

public class News {

    @Field
    private String id;

    @Field
    private String title;

    @Field
    private String content;

    //如果属性名字和字段与配置文件中不一致  要添加value属性值对应
    //将url与docurl匹配
    @Field(value = "docurl")
    private String url;

    @Field
    private Long click;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setClick(Long click) {
        this.click = click;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Long getClick() {
        return click;
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", conten='" + content + '\'' +
                ", url='" + url + '\'' +
                ", click=" + click +
                '}';
    }
}
