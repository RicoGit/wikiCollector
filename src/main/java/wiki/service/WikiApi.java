package wiki.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wiki.entity.Member;
import wiki.entity.Paper;
import wiki.response.MembersResponse;
import wiki.response.PaperResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  11:59
 */

@Service
public class WikiApi {

    private static final String ROOT_URL = "https://ru.wikipedia.org/w/api.php";
    private static final String COMMON_PARAMS= ROOT_URL +"?action=query&format=json";

    private static final String CONTINUE = "&cmcontinue=";

    private static final String PAGE_BY_ID = "&prop=extracts|info&inprop=url&explaintext&exsectionformat=plain&pageids=";

    private static final String PAGES = "&list=categorymembers&cmprop=title|type|ids&cmlimit=500&cmtype=page";
    private static final String PAGES_BY_CATEGORY_NAME = PAGES + "&cmtitle=Category:";
    private static final String PAGES_BY_CATEGORY_ID = PAGES + "&cmpageid=";

    private static final String SUBCATEGORY = "&list=categorymembers&cmprop=title|type|ids&cmlimit=500&cmtype=subcat&";
    private static final String SUBCATEGORY_BY_CATEGORY_NAME = SUBCATEGORY + "cmtitle=Category:";
    private static final String SUBCATEGORY_BY_CATEGORY_ID = SUBCATEGORY + "&cmpageid=";

    // restTemplate is thread save
    private RestTemplate restTemplate = new RestTemplate();

    public Paper getPaper(int pageIds) {
        PaperResponse paperResponse = restTemplate.getForObject(getPaperById(pageIds), PaperResponse.class);
        return paperResponse.asPaper(pageIds);
    }

    /**
     * @param category - requested category
     * @return all pages of current category
     */
    public List<Member> getAllPagesOfCategoryByName(String category){

        MembersResponse pagesResponse = restTemplate.getForObject(getPagesByCategoriesName(category), MembersResponse.class);

        List<Member> result = new ArrayList<>(pagesResponse.getMemberList());

        while(pagesResponse.hasContinue()) {
            pagesResponse = restTemplate.getForObject(getPagesByCategoriesNameContinue(category, pagesResponse.getQueryContinue()), MembersResponse.class);
            result.addAll(pagesResponse.getMemberList());
        }

        return result;
    }

    /**
     * @param categoryId - (cmpageid) of category
     * @return all pages of current category
     */
    public List<Member> getAllPagesOfCategoryById(int categoryId){

        MembersResponse pagesResponse = restTemplate.getForObject(getPagesByCategoriesId(categoryId), MembersResponse.class);

        List<Member> result = new ArrayList<>(pagesResponse.getMemberList());

        while(pagesResponse.hasContinue()) {
            pagesResponse = restTemplate.getForObject(getPagesByCategoriesId(categoryId), MembersResponse.class);
            result.addAll(pagesResponse.getMemberList());
        }

        return result;
    }

    /**
     * @param category - requested category
     * @return all subcategories of current category
     */
    public List<Member> getSubCategoriesOfCategoryByName(String category) {

        MembersResponse membersResponse = restTemplate.getForObject(getCategorySubCategories(category), MembersResponse.class);

        List<Member> result = new ArrayList<>(membersResponse.getMemberList());

        while(membersResponse.hasContinue()) {
            membersResponse = restTemplate.getForObject(getCategorySubCategories(category), MembersResponse.class);
            result.addAll(membersResponse.getMemberList());
        }

        return result;
    }

    /* Get pages*/

    private String getPaperById(int pageId) {
        return COMMON_PARAMS + COMMON_PARAMS + PAGE_BY_ID + pageId;
    }

    private String getPagesByCategoriesName(String category) {
        return COMMON_PARAMS + PAGES_BY_CATEGORY_NAME + category;
    }

    private String getPagesByCategoriesNameContinue(String category, String queryContinue) {
        return COMMON_PARAMS + PAGES_BY_CATEGORY_NAME + category + CONTINUE + queryContinue;
    }

    private String getPagesByCategoriesId(int category) {
        return COMMON_PARAMS + PAGES_BY_CATEGORY_ID + category;
    }

    private String getPagesByCategoriesIdContinue(int category, String queryContinue) {
        return COMMON_PARAMS + PAGES_BY_CATEGORY_ID + category + CONTINUE + queryContinue;
    }

    /* Get categories */

    private String getCategorySubCategories(String category) {
        return COMMON_PARAMS + SUBCATEGORY_BY_CATEGORY_NAME + category;
    }


}
