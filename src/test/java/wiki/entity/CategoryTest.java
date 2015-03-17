package wiki.entity;

import org.junit.Assert;
import org.junit.Test;

import static java.io.File.separator;
import static java.lang.String.format;

/**
 * User: Constantine Solovev
 * Created: 17.03.15  16:35
 */


public class CategoryTest {
                                                // unix                  // win
    String rootPath = "/".equals(separator) ? "/home/JBloch" : "c:\\home\\JBloch";

    Category category = new Category(8, "Java", rootPath);
    Category child0 = new Category(132, 0, "child0", category);
    Category child1 = new Category(555, 3, "child1", child0);

    @Test
    public void test_getFullPath_noParent() {
        String expected = format("%s%s08_Java", rootPath, separator);
        Assert.assertTrue(expected.equals(category.getFolderFullPath()));
    }

    @Test
    public void test_getFullPath_withParent() {
        String expected = format("%1$s%2$s08_Java%2$s08_00_child0", rootPath, separator);
        Assert.assertTrue(expected.equals(child0.getFolderFullPath()));
    }

    @Test
    public void test_getFullPath_with2Parent() {
        String expected = format("%1$s%2$s08_Java%2$s08_00_child0%2$s08_00_03_child1", rootPath, separator);
        Assert.assertTrue(expected.equals(child1.getFolderFullPath()));
    }

    @Test
    public void test_getFileFullPath_noParent() {
        String expected = format("%1$s%2$s08_Java%2$s08_045.txt", rootPath, separator);
        Assert.assertTrue(expected.equals(category.getFileFullPath(45)));
    }

    @Test
    public void test_getFileFullPath_withParent() {
        String expected = format("%1$s%2$s08_Java%2$s08_00_child0%2$s08_00_045.txt", rootPath, separator);
        Assert.assertTrue(expected.equals(child0.getFileFullPath(45)));
    }

    @Test
    public void test_getFileFullPath_with2Parent() {
        String expected = format("%1$s%2$s08_Java%2$s08_00_child0%2$s08_00_03_child1%2$s08_00_03_045.txt", rootPath, separator);
        Assert.assertTrue(expected.equals(child1.getFileFullPath(45)));
    }
}
