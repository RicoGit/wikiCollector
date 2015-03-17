package wiki.service;

import org.springframework.util.Assert;
import wiki.entity.Category;
import wiki.entity.Member;
import wiki.entity.Paper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.RecursiveAction;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.IntStream.range;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  19:38
 */


public class PaperMiningTask extends RecursiveAction {

    private ConstantOptions options;

    private final int start;
    private final int end;
    private int availableThreads;

    public PaperMiningTask( ConstantOptions options, int availableThreads, int start, int end ) {

        Assert.notNull(options, "ConstantOptions required");
        Assert.isTrue(start <= end, "Invalid list bounds");
        Assert.isTrue(availableThreads >= 0, "available threads is negative");

        this.options = options;
        this.availableThreads = availableThreads;
        this.start = start;
        this.end = end;
    }


    @Override
    protected void compute() {

        if (availableThreads == 0) {

            getAndSave(options.getPages(), start, end);

        } else if (availableThreads == 1) {

            int mid = ( start + end ) / 2;

            PaperMiningTask task = new PaperMiningTask(options, 0, start, mid);
            task.fork();

            getAndSave(options.getPages(), mid, end);

            task.join();

        } else {

            int mid = ( start + end ) / 2;
            availableThreads -= 2;

            PaperMiningTask task1 = new PaperMiningTask(options, availableThreads / 2, start, mid);
            PaperMiningTask task2 = new PaperMiningTask(options, availableThreads / 2, mid, end);

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
            Optional<Paper> optionalPaper = options.getPaperFn().apply(member.getId());
            optionalPaper.ifPresent(paper -> writePaperToFile(paper, index));

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

        String fileName = "";
        String subCatFolder = "";

        Category category = options.getCategory();

        do {
             fileName =     String.format("%02d_%s", category.getCode(), fileName);
             subCatFolder = String.format("%02d_%s", category.getCode(), subCatFolder);
             category = category.getParent();

         } while (category != null);

        subCatFolder =  String.format("%s%s/", subCatFolder, options.getCategory().getTitle());
        fileName =      String.format("%s%03d.txt", fileName, index);

        return options.getOutPutFolder() + subCatFolder + fileName;
    }

}
