package wiki.entity;

import static java.io.File.separator;
import static java.lang.String.format;

/**
 * User: Constantine Solovev
 * Date: 16.03.15
 * Time: 11:17
 */

public class Category {

    private Integer cmpageid;
    private int code;
    private String title;
    private Category parent;

    private String fullPath;
    private String childrenPrefix = "";

    public Category(int code, String title, String outputFolder) {
        this.code = code;
        this.title = title;
        init(outputFolder);
    }

    public Category(int cmpageid, int code, String title, Category parent) {
        this.cmpageid = cmpageid;
        this.code = code;
        this.title = title;
        this.parent = parent;
        init(parent.getFolderFullPath());
    }


    public String getFolderFullPath() {
        return fullPath;
    }

    public String getFileFullPath(int index) {
        return this.getFolderFullPath() + separator + format("%s%03d.txt", this.childrenPrefix, index);
    }


    private void init(String outputFolder) {

        Category category = this;

        do {
            this.childrenPrefix = format("%02d_%s", category.getCode(), this.childrenPrefix);
            category = category.getParent();
        } while (category != null);

        String folderName =  this.childrenPrefix + this.getTitle();
        this.fullPath  = outputFolder + separator + folderName;
    }


    public Integer getCmpageid() {
        return cmpageid;
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

    @Override
    public String toString() {
        return "{" + title + "}";
    }
}