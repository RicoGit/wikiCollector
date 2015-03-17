package wiki.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import wiki.config.ApplicationConfig;
import wiki.entity.Category;
import wiki.entity.Member;
import wiki.entity.Type;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.reflect.Whitebox.setInternalState;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static wiki.entity.Type.PAGE;
import static wiki.entity.Type.SUB_CATEGORY;

/**
 * User: Constantine Solovev
 * Created: 17.03.15  16:16
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({CrawlingService.class})
public class CrawlingServiceTest {

    CrawlingService crawlingSrvSpy = spy(new CrawlingService());
    ApplicationConfig config = mock(ApplicationConfig.class);
    WikiApi wikiApi = mock(WikiApi.class);
    ForkJoinPool forkJoinPool = mock(ForkJoinPool.class);

    @Before
    public void before() {

        setField(crawlingSrvSpy, "config", config);
        setField(crawlingSrvSpy, "wikiApi", wikiApi);
        setField(crawlingSrvSpy, "forkJoinPool", forkJoinPool);
        setField(crawlingSrvSpy, "parallelism", 2);
        setField(crawlingSrvSpy, "pagesLimit", 400);

    }

    @Test
    public void test_processCategory() throws Exception {

        Category category = getCategory();

        crawlingSrvSpy.processCategory(category);

        verifyPrivate(crawlingSrvSpy, atLeastOnce()).invoke("processPagesOfCategory", eq(category), any(AtomicInteger.class));
        verifyPrivate(crawlingSrvSpy, atLeastOnce()).invoke("processCategoryRecursive", eq(category), any(AtomicInteger.class));
    }

    @Test
    public void test_processCategoryRecursive() throws Exception {

        Category category = getCategory();

        when(wikiApi.getAllSubCategoriesOfCategory(any(Category.class))).thenReturn(getSubCategoriesList());
        when(wikiApi.getAllPagesOfCategory(any(Category.class))).thenReturn(getPagesList());

        crawlingSrvSpy.processCategory(category);

        verifyPrivate(crawlingSrvSpy, Mockito.times(133)).invoke("processCategoryRecursive", any(Category.class), any(AtomicInteger.class));

    }

    // todo do others tests

    private List<Member> getSubCategoriesList() {
        return Arrays.asList(
                getMember(0, "Java", SUB_CATEGORY),
                getMember(1, "Pyton", SUB_CATEGORY),
                getMember(2, "Scala", SUB_CATEGORY));
    }

    private List<Member> getPagesList() {
        return Arrays.asList(
                getMember(0, "Page 1", PAGE),
                getMember(1, "page 2", PAGE),
                getMember(2, "page 3", PAGE));
    }

    private Member getMember(int id, String title, Type type) {
        Member member = new Member();
        setInternalState(member, "id", id);
        setInternalState(member, "title", title);
        setInternalState(member, "type", type);
        return member;
    }


    public Category getCategory() {
        int CODE = 1;
        String TITLE = "Java";
        String OUTPUT_FOLDER = "/home/bloch";
        return new Category(CODE, TITLE, OUTPUT_FOLDER);
    }
}
