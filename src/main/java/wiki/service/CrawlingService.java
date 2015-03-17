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


@Service   //todo refactor very ugly
public class CrawlingService {

    @Resource
    private WikiApi wikiApi;

    @Resource
    private ApplicationConfig applicationConfig;

    private volatile int parallelism ;
    private ForkJoinPool forkJoinPool;

    @PostConstruct
    void postConstruct() {
        parallelism = applicationConfig.getNumberOfThread();
        forkJoinPool = new ForkJoinPool(parallelism);
    }

    public void processCategory(final Category category) {

        AtomicInteger atomicPageCounter = new AtomicInteger();

        processRootCategory(category, atomicPageCounter, applicationConfig.getResultFolderPath());

        System.out.printf("%d pages downloaded\n", atomicPageCounter.get());
    }

    private void processRootCategory(final Category category, AtomicInteger pagesCount, String folder){

        System.out.println(" Root category:" + category);

        List<Member>pages = wikiApi.getAllPagesOfCategory(category);
        sort(pages);

        ConstantOptions options = new ConstantOptions(category, pages, folder, wikiApi::getPaper);
        PaperMiningTask paperMiningTask = new PaperMiningTask(options, parallelism, 0, pages.size());

        forkJoinPool.invoke(paperMiningTask);

        if(pagesCount.addAndGet(pages.size()) < 400) {

            List<Member> subCategories = wikiApi.getAllSubCategoriesOfCategory(category);
            sort(subCategories);

            for (int index = 0; index < subCategories.size(); index++) {

                Member member= subCategories.get(index);
                processPagesOfCategory(
                        new Category(member.getId(), index, member.getTitle(), category),
                        pagesCount,
                        folder + String.format("%02d_%s/", category.getCode(), category.getTitle()) // todo add common method
                );
            }

            if(pagesCount.get() < 400) {
                for (int index = 0; index < subCategories.size(); index++) {

                    Member member= subCategories.get(index);
                    processSubCategoriesOfCategory(
                            new Category(member.getId(), index, member.getTitle(), category),
                            pagesCount,
                            folder + String.format("%02d_%s/", category.getCode(), category.getTitle())  // todo add common method
                    );
                }
                System.out.println("go deeper");
            } else {
                System.out.printf("Finish with '%s' category\n", category.getTitle());
            }

        }

    }

    private void processPagesOfCategory(Category category, AtomicInteger pagesCount, String folder) {

        System.out.println("process pages of category " + category + " " + pagesCount.get());

        List<Member> pages = wikiApi.getAllPagesOfCategory(category);
        sort(pages);

        ConstantOptions options = new ConstantOptions(category, pages, folder, wikiApi::getPaper);
        PaperMiningTask paperMiningTask = new PaperMiningTask(options, parallelism, 0, pages.size());

        forkJoinPool.invoke(paperMiningTask);

        pagesCount.addAndGet(pages.size());
    }

    private void processSubCategoriesOfCategory(Category category, AtomicInteger pagesCount, String folder) {

        System.out.println("process subcategories of category " + category);

        List<Member> pages = wikiApi.getAllSubCategoriesOfCategory(category);
        sort(pages);

    }

}
