package wiki.service;

import org.springframework.stereotype.Service;
import wiki.config.ApplicationConfig;
import wiki.entity.Category;
import wiki.entity.Member;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.sort;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  19:47
 */


@Service
public class CrawlingService {

    @Resource
    private WikiApi wikiApi;

    @Resource
    private ApplicationConfig config;

    private int parallelism ;
    private int pagesLimit ;
    private ForkJoinPool forkJoinPool;

    @PostConstruct
    void postConstruct() {
        parallelism = config.getNumberOfThread();
        pagesLimit = config.getPagesLimit();
        forkJoinPool = new ForkJoinPool(parallelism);
    }


    public void processCategory(Category category) {

        AtomicInteger atomicPageCounter = new AtomicInteger();

        // get all pages of root category
        processPagesOfCategory(category, atomicPageCounter);
        // get all nested pages recursive
        processCategoryRecursive(category, atomicPageCounter);

        System.out.printf("%d pages downloaded\n", atomicPageCounter.get());
    }

    private void processCategoryRecursive(Category category, AtomicInteger pagesCount){

        System.out.printf("Process category %s, pages %d\n", category, pagesCount.get());

        if(pagesCount.get() < pagesLimit) {

            // get all subCategories
            List<Member> subCategories = wikiApi.getAllSubCategoriesOfCategory(category);
            sort(subCategories);

            // get all pages of all subCategories
            for (int index = 0; index < subCategories.size(); index++) {

                Member member= subCategories.get(index);

                Category subCategory = new Category(member.getId(), index, member.getTitle(), category);

                processPagesOfCategory( subCategory, pagesCount);
            }

            if(pagesCount.get() > pagesLimit) {
                // enough
                return;
            }

            // go deeper, run recursive processing for each subcategories
            for (int index = 0; index < subCategories.size(); index++) {

                Member member= subCategories.get(index);

                Category subCategory = new Category(member.getId(), index, member.getTitle(), category);

                processCategoryRecursive( subCategory, pagesCount);
            }

        }

    }

    private void processPagesOfCategory(Category category, AtomicInteger pagesCount) {

        System.out.println("process pages of category " + category + " " + pagesCount.get());

        List<Member> pages = wikiApi.getAllPagesOfCategory(category);
        sort(pages);

        ConstantOptions options = new ConstantOptions(category, pages, wikiApi::getPaper);
        PaperMiningTask paperMiningTask = new PaperMiningTask(options, parallelism, 0, pages.size());

        forkJoinPool.invoke(paperMiningTask);

        pagesCount.addAndGet(pages.size());
    }

}
