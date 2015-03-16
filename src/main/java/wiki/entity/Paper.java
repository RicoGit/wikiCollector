package wiki.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  12:02
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class Paper {

    @JsonProperty("pageid")
    int id;
    @JsonProperty("fullurl")
    String url;
    @JsonProperty("extract")
    String content;
    @JsonProperty("title")
    String title;

    Category parent;

    public int getId() {
        return id;
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Paper {id: %s, title: %s}", id, title);
    }
}
