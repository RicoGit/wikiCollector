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


@JsonIgnoreProperties(ignoreUnknown = true)
public class MembersResponse {

    private static final String QUERY = "query";
    private static final String CATEGORY_MEMBERS = "categorymembers";

    private Map<String, List<Member>> query;

    @JsonCreator
    private MembersResponse(@JsonProperty(QUERY) Map<String, List<Member>> query) {
        this.query = query;
    }

    public List<Member> getMemberList() {
        return Collections.unmodifiableList(query.get(CATEGORY_MEMBERS));
    }
}
