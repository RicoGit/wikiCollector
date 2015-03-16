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
    private ApplicationConfig applicationConfig;

    private volatile int parallelism ;
    private ForkJoinPool forkJoinPool;

    @PostConstruct
    void postConstruct() {
        parallelism = applicationConfig.getNumberOfThread();
        forkJoinPool = new ForkJoinPool(parallelism);
    }

    public void processCategory(final Category category) {
        processCategoryRecursive(category, new AtomicInteger(), applicationConfig.getResultFolderPath());
    }

    private void processCategoryRecursive(final Category category, AtomicInteger pagesCount, String folder ) {

        System.out.println(parallelism + " " + category + " " + pagesCount.get());

        List<Member> pages;

        if (category.getCmpageid() != null) {
             pages = wikiApi.getAllPagesOfCategoryById(category.getCmpageid());
        } else {
            pages = wikiApi.getAllPagesOfCategoryByName(category.getTitle());
        }
        sort(pages);

        ConstantOptions options = new ConstantOptions(category, pages, folder, wikiApi::getPaper);
        PaperMiningTask paperMiningTask = new PaperMiningTask(options, parallelism, 0, pages.size());

        forkJoinPool.invoke(paperMiningTask);

        // if pages in current category not enough, go deeper
        if(pagesCount.addAndGet(pages.size()) < 400) {

            List<Member> subCategories = wikiApi.getSubCategoriesOfCategoryByName(category.getTitle());
            sort(subCategories);

            for (int index = 0; index < subCategories.size(); index++) {
                Member member= subCategories.get(index);                                                              // todo
                processCategoryRecursive(new Category(member.getId(), index, member.getTitle(), category), pagesCount, folder + category.getCode() + "_" + category.getTitle() + "/");
            }

            subCategories.stream()
                    .forEach(subCategory -> {

//                        List<Member> pagesOfSubCategory = wikiApi.getAllPagesOfCategoryById(subCategory.getId());
//
//                        sort(pagesOfSubCategory);
//
//                        ConstantOptions options = new ConstantOptions(
//                                subCategory,
//                                pagesOfSubCategory,
//                                resultFolderPath + subCategory.getTitle(),
//                                wikiApi::getPaper);
//                        PaperMiningTask paperMiningTask = new PaperMiningTask(options, parallelism, 0, pages.size());
//
//                        forkJoinPool.invoke(paperMiningTask);

                        // todo сохранить страницы из всех сублистов, создать для них таски
                    });
            // todo если 400 статей не собралось спускаемся на уровень ниже
        }

    }

//    private List<Paper> processSubCategory(int categoryId, AtomicInteger pagesCount) {
//
//        int parallelism = applicationConfig.getNumberOfThread();
//
//        ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
//
//        List<Member> pages = wikiApi.getAllPagesOfCategoryById(categoryId);
//
//        //todo
//
//        return null;
//    }


}
