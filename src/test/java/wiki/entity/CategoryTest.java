package wiki.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Constantine Solovev
 * Created: 17.03.15  16:35
 */


public class CategoryTest {


    Category category = new Category(8, "Java", "/home/JBloch/");
    Category child0 = new Category(132, 0, "child0", category);
    Category child1 = new Category(555, 3, "child1", child0);

    @Test
    public void test_getFullPath_noParent() {
        String expected = "/home/JBloch/08_Java/";
        Assert.assertTrue(expected.equals(category.getFolderFullPath()));
    }

    @Test
    public void test_getFullPath_withParent() {
        String expected = "/home/JBloch/08_Java/08_00_child0/";
        Assert.assertTrue(expected.equals(child0.getFolderFullPath()));
    }

    @Test
    public void test_getFullPath_with2Parent() {
        String expected = "/home/JBloch/08_Java/08_00_child0/08_00_03_child1/";
        Assert.assertTrue(expected.equals(child1.getFolderFullPath()));
    }

    @Test
    public void test_getFileFullPath_noParent() {
        String expected = "/home/JBloch/08_Java/08_045.txt";
        Assert.assertTrue(expected.equals(category.getFileFullPath(45)));
    }

    @Test
    public void test_getFileFullPath_withParent() {
        String expected = "/home/JBloch/08_Java/08_00_child0/08_00_045.txt";
        Assert.assertTrue(expected.equals(child0.getFileFullPath(45)));
    }

    @Test
    public void test_getFileFullPath_with2Parent() {
        String expected = "/home/JBloch/08_Java/08_00_child0/08_00_03_child1/08_00_03_045.txt";
        Assert.assertTrue(expected.equals(child1.getFileFullPath(45)));
    }
}
