package wiki.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import wiki.entity.Paper;

import java.util.Map;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  14:06
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperResponse {

    private static final String QUERY = "query";
    private static final String PAGES = "pages";

    private Map<String, Map<String, Paper>> query;

    @JsonCreator
    private PaperResponse(@JsonProperty(QUERY) Map<String, Map<String, Paper>> query) {
        this.query = query;
    }

    public Paper asPaper(int pageIds) {
        return query.get(PAGES).get(String.valueOf(pageIds));
    }
}
