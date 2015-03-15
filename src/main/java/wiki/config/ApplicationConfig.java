package wiki.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  15:14
 */


@Component
public class ApplicationConfig {

    @Value("${categories}")
    private String[] categories;

    @Value("${numberOfThreads}")
    private int numberOfThread;

    @Value("${maxRequestsDelayInSecond}")
    private int maxRequestDelay;

    @Value("${resultFolderPath}")
    private String resultFolderPath;

    private List<String> categoryList;

    public List<String> getCategories() {

        if (categoryList == null) {

            categoryList = new ArrayList<>(categories.length);
            for(String category : categories) {
                byte[] stringAsBytes = category.getBytes(Charset.forName("ISO8859-1"));
                categoryList.add(new String(stringAsBytes, Charset.forName("UTF8")));
            }
        }

        return categoryList;
    }

    public int getNumberOfThread() {
        return numberOfThread;
    }

    public int getMaxRequestDelay() {
        return maxRequestDelay;
    }

    public String getResultFolderPath() {
        return resultFolderPath;
    }
}
