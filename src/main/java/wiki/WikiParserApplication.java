package wiki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import wiki.config.ApplicationConfig;
import wiki.entity.Category;
import wiki.service.CrawlingService;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class WikiParserApplication implements CommandLineRunner {

    @Resource
    private CrawlingService crawlingService;
    @Resource
    private ApplicationConfig applicationConfig;

    @Override
    public void run(String... args) throws Exception {

        long start = System.currentTimeMillis();
        System.out.println("Application start");

        List<Category> categoryList = applicationConfig.getCategories();

        categoryList
                .stream()
                .limit(1) // todo remove
                .forEach(crawlingService::processCategory);

        System.out.println("Application end, time spend " + (System.currentTimeMillis() - start));
    }


    public static void main(String[] args) {
        SpringApplication.run(WikiParserApplication.class, args);
    }
}
