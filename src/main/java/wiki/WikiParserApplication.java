package wiki;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wiki.service.WikiApi;

import javax.annotation.Resource;

@SpringBootApplication
public class WikiParserApplication implements CommandLineRunner {

    @Resource
    private WikiApi wikiApi;


    @Override
    public void run(String... args) throws Exception {

        System.out.println("application start");

        wikiApi.getPaper(3349460);

    }


    public static void main(String[] args) {
        SpringApplication.run(WikiParserApplication.class, args);
    }
}
