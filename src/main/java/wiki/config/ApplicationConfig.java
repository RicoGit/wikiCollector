package wiki.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wiki.entity.Category;

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

    private List<Category> categoryList;


    public List<Category> getCategories() {

        if (categoryList == null) {
            categoryList = asListOfCategories(categories);
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

    private List<Category> asListOfCategories(String[] categories) {

        int length = categories.length;

        List<Category> categoryList = new ArrayList<>(length);

        for(int i=0; i < length; i++){
            categoryList.add(new Category(i, convertToUtf8(categories[i].trim())));
        }

        return categoryList;
    }

    private String convertToUtf8(String Iso8859String) {
        byte[] stringAsBytes = Iso8859String.getBytes(Charset.forName("ISO8859-1"));
        return new String(stringAsBytes, Charset.forName("UTF8"));
    }
}
