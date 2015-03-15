package wiki.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  14:06
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperResponse {

    @JsonProperty("query")
    private Map<String, Map<String, Paper>> pages;

    @JsonCreator
    private PaperResponse(Map<String, Map<String, Paper>> pages) {
        this.pages = pages;
    }

    public Paper asPaper(int pageIds) {
        return pages.get("pages").get(String.valueOf(pageIds));
    }
}
