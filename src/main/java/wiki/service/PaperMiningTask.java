package wiki.service;

import wiki.entity.Category;
import wiki.entity.Member;
import wiki.entity.Paper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.IntStream.range;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  19:38
 */


public class PaperMiningTask extends RecursiveAction {

    private final Category currentCategory;
    private final List<Member> pages;
    private final int start;
    private final int end;
    private final String outPutFolder;
    private int availableThreads;

    public PaperMiningTask(
            Category currentCategory,
            List<Member> pages,
            int availableThreads,
            String outPutFolder,
            int start,
            int end
    ) {

        if (availableThreads < 0) {
            throw new IllegalArgumentException("Available thread < 0");
        }

        this.currentCategory = currentCategory;
        this.pages = Collections.unmodifiableList(pages);
        this.availableThreads = availableThreads;
        this.outPutFolder = outPutFolder;
        this.start = start;
        this.end = end;
    }


    @Override
    protected void compute() {

        if (availableThreads == 0) {

            getAndSave(pages, start, end);

        } else if (availableThreads == 1) {

            int mid = ( start + end ) / 2;

            PaperMiningTask task = new PaperMiningTask(currentCategory, pages, 0, outPutFolder, start, mid);
            task.fork();

            getAndSave(pages, mid, end);

            task.join();

        } else {

            int mid = ( start + end ) / 2;
            availableThreads -= 2;

            PaperMiningTask task1 = new PaperMiningTask(currentCategory, pages, availableThreads / 2, outPutFolder, start, mid);
            PaperMiningTask task2 = new PaperMiningTask(currentCategory, pages, availableThreads / 2, outPutFolder, mid, end);

            task1.fork();
            task2.fork();

            task1.join();
            task2.join();

            System.out.printf("fork (%d, %d) - (%d, %d) thread left - %s\n", start, mid, mid, end, availableThreads);

        }
    }

    private void getAndSave(List<Member> pages, int start, int end) {

        range(start, end).forEach(index -> {

            Member member = pages.get(index);
            Paper paper = WikiApi.getPaper(member.getId()); // todo investigate
            writePaperToFile(paper, index);

        });

    }

    private void writePaperToFile(Paper paper, int index) {

        try {

            Path path = Paths.get(composePath(index));
            String dataToWrite = String.format("%s\n\n\n%s\n", paper.getUrl(), paper.getContent());
            Files.createDirectories(path.getParent());
            Files.write(path, dataToWrite.getBytes(), WRITE, CREATE);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private String composePath(int index) {

        StringBuilder path = new StringBuilder();
        String fileName = "";

        path.append(outPutFolder);

        Category category = currentCategory;

        do {

             fileName += category.getCode() + "_";
             path.append(String.format("%02d_%s/", category.getCode(), category.getTitle()));
             category = category.getParent();

         } while (category != null);

        path.append(String.format("%s%04d.txt", fileName, index));

        return path.toString();
    }

}
