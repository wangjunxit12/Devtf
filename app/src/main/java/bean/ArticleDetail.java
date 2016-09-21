package bean;

/**
 * Created by Administrator on 2016/9/21.
 */

public class ArticleDetail {
    private String content;
    private String post_id;

    public ArticleDetail() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public ArticleDetail(String content, String post_id) {
        this.content = content;
        this.post_id = post_id;
    }
}
