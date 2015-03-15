package wiki.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wiki.entity.Member;
import wiki.entity.Paper;
import wiki.response.MembersResponse;
import wiki.response.PaperResponse;

import java.util.List;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  11:59
 */


@Service
public class WikiApi {

    private static final String ROOT_URL = "https://ru.wikipedia.org/w/api.php";
    private static final String COMMON_PARAMS= "?action=query&format=json";

    private static final String PAGE_BY_ID = "&prop=extracts|info&inprop=url&explaintext&exsectionformat=plain&pageids=";

    private static final String PAGES_BY_CATEGORY_NAME = "&list=categorymembers&cmprop=title|type|ids&cmlimit=400&cmtype=page&cmtitle=Category:";
    private static final String PAGES_BY_CATEGORY_ID = "&list=categorymembers&cmprop=title|type|ids&cmlimit=400&cmtype=page&cmpageid=";
    private static final String SUBCATEGORY_BY_CATEGORY_NAME = "&list=categorymembers&cmprop=title|type|ids&cmtype=subcat&cmtitle=Category:";
    private static final String SUBCATEGORY_BY_CATEGORY_ID = "list=categorymembers&cmprop=title|type|ids&cmtype=subcat&cmlimit=500&cmpageid=";


    public static Paper getPaper(int pageIds) {
        RestTemplate restTemplate = new RestTemplate();
        PaperResponse paperResponse = restTemplate.getForObject(getPaperById(pageIds), PaperResponse.class);
        return paperResponse.asPaper(pageIds);
    }


    public List<Member> getPagesOfCategoryByName(String category){
        RestTemplate restTemplate = new RestTemplate();
        MembersResponse pagesResponse = restTemplate.getForObject(getPagesByCategoriesName(category), MembersResponse.class);
        return pagesResponse.getMemberList();
    }

    public List<Member> getPagesOfCategoryById(int categoryId){
        RestTemplate restTemplate = new RestTemplate();
        MembersResponse pagesResponse = restTemplate.getForObject(getPagesByCategoriesId(categoryId), MembersResponse.class);
        return pagesResponse.getMemberList();
    }

    public List<Member> getSubCategoriesOfCategoryByName(String category) {
        RestTemplate restTemplate = new RestTemplate();
        MembersResponse membersResponse = restTemplate.getForObject(getCategorySubCategories(category), MembersResponse.class);
        return membersResponse.getMemberList();
    }


    private static String getPaperById(int pageId) {
        return ROOT_URL + COMMON_PARAMS + PAGE_BY_ID + pageId;
    }

    private String getPagesByCategoriesName(String category) {
        return ROOT_URL + COMMON_PARAMS + PAGES_BY_CATEGORY_NAME + category;
    }

    private String getPagesByCategoriesId(int category) {
        return ROOT_URL + COMMON_PARAMS + PAGES_BY_CATEGORY_ID + category;
    }

    private String getCategorySubCategories(String category) {
        return ROOT_URL + COMMON_PARAMS + SUBCATEGORY_BY_CATEGORY_NAME + category;
    }


}
