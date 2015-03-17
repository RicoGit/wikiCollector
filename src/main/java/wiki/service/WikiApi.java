package wiki.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import wiki.entity.Category;
import wiki.entity.Member;
import wiki.entity.Paper;
import wiki.response.MembersResponse;
import wiki.response.PaperResponse;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  11:59
 */

@Service
public class WikiApi {

    @Resource
    private RestTemplate simpleRestTemplate;
    @Resource
    private RestTemplate restTemplateWithLimitedTimeout;

    private static final String ROOT_URL = "https://ru.wikipedia.org/w/api.php";
    private static final String COMMON_PARAMS = ROOT_URL +"?action=query&format=json";

    private static final String CONTINUE = "&cmcontinue=";

    private static final String PAGE_BY_ID = "&prop=extracts|info&inprop=url&explaintext&exsectionformat=plain&pageids=";

    private static final String PAGES = "&list=categorymembers&cmprop=title|type|ids&cmlimit=500&cmtype=page";
    private static final String PAGES_BY_CATEGORY_NAME = PAGES + "&cmtitle=Category:";
    private static final String PAGES_BY_CATEGORY_ID   = PAGES + "&cmpageid=";

    private static final String SUBCATEGORY = "&list=categorymembers&cmprop=title|type|ids&cmlimit=500&cmtype=subcat&";
    private static final String SUBCATEGORY_BY_CATEGORY_NAME = SUBCATEGORY + "cmtitle=Category:";
    private static final String SUBCATEGORY_BY_CATEGORY_ID   = SUBCATEGORY + "&cmpageid=";

    public Optional<Paper> getPaper(int pageIds) {

        PaperResponse paperResponse = null;
        try {
            paperResponse = restTemplateWithLimitedTimeout.getForObject(getPaperById(pageIds), PaperResponse.class);
            System.out.printf("'%8s' - success\n", pageIds);
        } catch (ResourceAccessException e) {
            System.out.printf("'%8s' - connection timeout\n", pageIds);
        }
        return Optional.ofNullable(paperResponse != null ? paperResponse.asPaper(pageIds) : null);
    }

    /**
     * @param category - requested category
     * @return all pages of current category
     */
    public List<Member> getAllPagesOfCategory(Category category){

        MembersResponse pagesResponse =
                simpleRestTemplate.getForObject(getPagesByCategory(category), MembersResponse.class);

        List<Member> result = new ArrayList<>(pagesResponse.getMemberList());

        while(pagesResponse.hasContinue()) {

            pagesResponse = simpleRestTemplate.getForObject(
                    getPagesByCategoryContinue(category, pagesResponse.getQueryContinue()),
                    MembersResponse.class
            );
            result.addAll(pagesResponse.getMemberList());
        }

        return result;
    }

    /**
     * @param category - requested category
     * @return all subcategories of current category
     */
    public List<Member> getAllSubCategoriesOfCategory(Category category) {

        MembersResponse membersResponse =
                simpleRestTemplate.getForObject(getSubcategoriesByCategory(category), MembersResponse.class);

        List<Member> result = new ArrayList<>(membersResponse.getMemberList());

        while(membersResponse.hasContinue()) {

            membersResponse = simpleRestTemplate.getForObject(
                    getSubcategoriesByCategoryContinue(category, membersResponse.getQueryContinue()),
                    MembersResponse.class
            );
            result.addAll(membersResponse.getMemberList());
        }

        return result;
    }

    /* Get pages*/

    private String getPaperById(int pageId) {
        return COMMON_PARAMS + PAGE_BY_ID + pageId;
    }

    private String getPagesByCategory(Category category) {
        return category.getCmpageid() != null
                        ? COMMON_PARAMS + PAGES_BY_CATEGORY_ID + category.getCmpageid()
                        : COMMON_PARAMS + PAGES_BY_CATEGORY_NAME + category.getTitle();
    }

    private String getPagesByCategoryContinue(Category category, String queryContinue) {
        return getPagesByCategory(category) + CONTINUE + queryContinue;
    }

    /* Get categories */

    private String getSubcategoriesByCategory(Category category) {
        return category.getCmpageid() != null
                ? COMMON_PARAMS + SUBCATEGORY_BY_CATEGORY_ID + category.getCmpageid()
                : COMMON_PARAMS + SUBCATEGORY_BY_CATEGORY_NAME + category.getTitle();
    }

    private String getSubcategoriesByCategoryContinue(Category category, String queryContinue) {
        return getSubcategoriesByCategory(category) + CONTINUE + queryContinue;
    }

}
