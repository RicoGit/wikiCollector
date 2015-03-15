package wiki.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  12:02
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class Paper {

    @JsonProperty(value="pageids")
    int id;
    @JsonProperty(value="fullurl")
    String url;
    @JsonProperty(value="extract")
    String content;

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

}
