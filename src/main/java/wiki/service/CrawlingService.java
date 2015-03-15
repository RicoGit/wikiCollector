package wiki.service;

import org.springframework.stereotype.Service;
import wiki.config.ApplicationConfig;
import wiki.entity.Member;
import wiki.entity.Paper;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  19:47
 */


@Service
public class CrawlingService {

    @Resource
    private WikiApi wikiApi;

    @Resource
    private ApplicationConfig applicationConfig;


    public List<Paper> processCategory(String category) {

        AtomicInteger pagesCount = new AtomicInteger();

        int parallelism = applicationConfig.getNumberOfThread();

        ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);

        List<Member> pages = wikiApi.getPagesOfCategoryByName(category);

        if (pages.size() < 400) {

            pagesCount.addAndGet(pages.size());

            List<Member> subCategories = wikiApi.getSubCategoriesOfCategoryByName(category);

            subCategories.stream()
                    .forEach(subCategory -> {
                        List<Member> pagesOfSubCategory = wikiApi.getPagesOfCategoryById(subCategory.getId());
                        // todo сохранить страницы из всех сублистов, создать для низ таски
                    });
             // todo если 400 статей не собралось спускаемся на уровень ниже
        }

        PaperMiningTask paperMiningTask = new PaperMiningTask(pages, parallelism, 0, pages.size());

        return forkJoinPool.invoke(paperMiningTask);
    }

    private List<Paper> processSubCategory(int categoryId, AtomicInteger pagesCount) {

        int parallelism = applicationConfig.getNumberOfThread();

        ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);

        List<Member> pages = wikiApi.getPagesOfCategoryById(categoryId);

        //todo

        return null;
    }




}
