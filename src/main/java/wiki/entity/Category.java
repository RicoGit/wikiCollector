package wiki.entity;

/**
 * Project: Shamrock Web Portal.
 * User: Constantine Solovev
 * Date: 16.03.15
 * Time: 11:17
 */

public class Category {

    private int code;
    private String title;
    private Category parent; //?

    public Category(int code, String title) {
        this.code = code;
        this.title = title;
    }

    public Category(int code, String title, Category parent) {
        this.code = code;
        this.title = title;
        this.parent = parent;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}