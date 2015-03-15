package wiki.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wiki.entity.Paper;
import wiki.entity.PaperResponse;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  11:59
 */


@Service
public class WikiApi {

    static final String ROOT_URL = "https://ru.wikipedia.org/w/api.php";
    static final String ACTION_QUERY = "action=query";
    static final String FORMAT_JSON = "format=json";
    static final String PROP_EXTRACT_INFO = "prop=extracts|info&inprop=url&explaintext&exsectionformat=plain";


    public Paper getPaper(int pageIds) {
        RestTemplate restTemplate = new RestTemplate();
        PaperResponse paperResponse = restTemplate.getForObject(getPaperQuery(pageIds), PaperResponse.class);
        return paperResponse.asPaper(pageIds);
    }

    public Object getCategory() {
        return null;
    }


    private String getPaperQuery(int pageId) {
        return String.format("%s?%s&%s&%s&pageids=%s",
                ROOT_URL,
                ACTION_QUERY,
                FORMAT_JSON,
                PROP_EXTRACT_INFO,
                pageId
        );
    }


}
