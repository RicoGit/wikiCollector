package wiki.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wiki.entity.Category;
import wiki.entity.Member;
import wiki.entity.Paper;
import wiki.response.MembersResponse;
import wiki.response.PaperResponse;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  11:59
 */


/**
 * This class isn't thread save!!!
 */
@Service
public class WikiApi {

    private static final String ROOT_URL = "https://ru.wikipedia.org/w/api.php";
    private static final String COMMON_PARAMS= "?action=query&format=json";
    private static final String CONTINUE = "&cmcontinue=";

    private static final String PAGE_BY_ID = "&prop=extracts|info&inprop=url&explaintext&exsectionformat=plain&pageids=";

    private static final String PAGES_BY_CATEGORY_NAME = "&list=categorymembers&cmprop=title|type|ids&cmlimit=40&cmtype=page&cmtitle=Category:";
    private static final String PAGES_BY_CATEGORY_ID = "&list=categorymembers&cmprop=title|type|ids&cmlimit=400&cmtype=page&cmpageid=";

    private static final String SUBCATEGORY_BY_CATEGORY_NAME = "&list=categorymembers&cmprop=title|type|ids&cmtype=subcat&cmtitle=Category:";
    private static final String SUBCATEGORY_BY_CATEGORY_ID = "list=categorymembers&cmprop=title|type|ids&cmtype=subcat&cmlimit=500&cmpageid=";


    private  RestTemplate restTemplate = new RestTemplate();


    public static Paper getPaper(int pageIds) {
        RestTemplate restTemplate = new RestTemplate();
        PaperResponse paperResponse = restTemplate.getForObject(getPaperById(pageIds), PaperResponse.class);
        return paperResponse.asPaper(pageIds);
    }

    /**
     * @param category - title of category
     * @return all pages of current category
     */
    public List<Member> getAllPagesOfCategoryByName(String category){

        MembersResponse pagesResponse = restTemplate.getForObject(getPagesByCategoriesName(category), MembersResponse.class);

        List<Member> result = new ArrayList<>(pagesResponse.getMemberList());

        while(pagesResponse.hasContinue()) {
            pagesResponse = restTemplate.getForObject(getPagesByCategoriesNameContinue(category, pagesResponse.getQueryContinue()), MembersResponse.class);
            result.addAll(pagesResponse.getMemberList());
        }

        sort(result);

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

        sort(result);

        return result;
    }

    public List<Member> getSubCategoriesOfCategoryByName(Category category) {
        RestTemplate restTemplate = new RestTemplate();
        MembersResponse membersResponse = restTemplate.getForObject(getCategorySubCategories(category.getTitle()), MembersResponse.class);
        return membersResponse.getMemberList();
    }

    /* Get pages*/

    private static String getPaperById(int pageId) {
        return ROOT_URL + COMMON_PARAMS + PAGE_BY_ID + pageId;
    }

    private String getPagesByCategoriesName(String category) {
        return ROOT_URL + COMMON_PARAMS + PAGES_BY_CATEGORY_NAME + category;
    }

    private String getPagesByCategoriesNameContinue(String category, String queryContinue) {
        return ROOT_URL + COMMON_PARAMS + PAGES_BY_CATEGORY_NAME + category + CONTINUE + queryContinue;
    }

    private String getPagesByCategoriesId(int category) {
        return ROOT_URL + COMMON_PARAMS + PAGES_BY_CATEGORY_ID + category;
    }

    private String getPagesByCategoriesIdContinue(int category, String queryContinue) {
        return ROOT_URL + COMMON_PARAMS + PAGES_BY_CATEGORY_ID + category + CONTINUE + queryContinue;
    }

    /* Get categories */

    private String getCategorySubCategories(String category) {
        return ROOT_URL + COMMON_PARAMS + SUBCATEGORY_BY_CATEGORY_NAME + category;
    }


}
