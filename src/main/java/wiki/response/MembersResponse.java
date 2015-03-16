package wiki.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import wiki.entity.Member;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  17:55
 */

/**
 * This class can't be create manually.Created automatically.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MembersResponse {

    private static final String QUERY = "query";
    private static final String CATEGORY_MEMBERS = "categorymembers";

    private static final String QUERY_CONTINUE = "query-continue";
    private static final String CMCONTINUE = "cmcontinue";

    private Map<String, List<Member>> query;
    private Map<String, Map<String, String>> queryContinue;

    @JsonCreator
    private MembersResponse(
            @JsonProperty(QUERY) Map<String, List<Member>> query,
            @JsonProperty(QUERY_CONTINUE) Map<String, Map<String, String>> queryContinue
    ) {
        this.query = query;
        this.queryContinue = queryContinue;
    }

    public List<Member> getMemberList() {
        return Collections.unmodifiableList(query.get(CATEGORY_MEMBERS));
    }

    public String getQueryContinue() {
        return queryContinue.get(CATEGORY_MEMBERS).get(CMCONTINUE);
    }

    public boolean hasContinue(){
        return queryContinue != null;
    }
}
