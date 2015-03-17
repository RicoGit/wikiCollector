package wiki.service;

import wiki.entity.Category;
import wiki.entity.Member;
import wiki.entity.Paper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * User: Constantine Solovev
 * Created: 16.03.15  22:59
 */


public final class ConstantOptions {

    private final Category category;
    private final List<Member> pages;
    private final String outPutFolder;
    private final Function<Integer, Optional<Paper>> getPaperFn;

    public ConstantOptions(
            Category category,
            List<Member> pages,
            String outPutFolder,
            Function<Integer, Optional<Paper>> getPaperFn
    ) {

        this.category = category;
        this.pages = pages;
        this.outPutFolder = outPutFolder;
        this.getPaperFn = getPaperFn;
    }

    public Category getCategory() {
        return category;
    }

    public List<Member> getPages() {
        return pages;
    }

    public String getOutPutFolder() {
        return outPutFolder;
    }

    public Function<Integer, Optional<Paper>> getPaperFn() {
        return getPaperFn;
    }
}
