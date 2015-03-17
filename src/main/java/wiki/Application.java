package wiki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import wiki.config.ApplicationConfig;
import wiki.entity.Category;
import wiki.service.CrawlingService;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class Application implements CommandLineRunner {

    @Resource
    private CrawlingService crawlingService;
    @Resource
    private ApplicationConfig config;


    @Bean(name = "simpleRestTemplate")
    public RestTemplate getSimpleRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name ="restTemplateWithLimitedTimeout")
    public RestTemplate getRestTemplateWithLimitedTimeout(ApplicationConfig config) {

        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
        requestFactory.setConnectTimeout(config.getMaxRequestDelay());

        return restTemplate;
    }


    @Override
    public void run(String... args) throws Exception {

        long start = System.currentTimeMillis();
        List<Category> categoryList = config.getCategories();

        System.out.printf(
                "\nApplication start\nCategories - %s\nParallelism - %d\nTimeout -%d\n\n",
                categoryList,
                config.getNumberOfThread(),
                config.getMaxRequestDelay()
        );

        categoryList
                .stream()
                .limit(1) // todo remove
                .forEach(crawlingService::processCategory);

        System.out.println("\nApplication end, time spend\n" + (System.currentTimeMillis() - start));
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
