package wiki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import wiki.config.ApplicationConfig;
import wiki.entity.Paper;
import wiki.service.CrawlingService;
import wiki.service.PersistentRepository;
import wiki.service.WikiApi;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class WikiParserApplication implements CommandLineRunner {

    @Resource
    private WikiApi wikiApi;
    @Resource
    private PersistentRepository<Paper> persistentRepository;
    @Resource
    private CrawlingService crawlingService;

    @Resource
    private ApplicationConfig applicationConfig;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("application start");

        List<String> categories = applicationConfig.getCategories();

        categories
                .stream()
                .limit(1) // todo remove
                .forEach(crawlingService::processCategory);
    }


    public static void main(String[] args) {
        SpringApplication.run(WikiParserApplication.class, args);
    }
}
