package wiki.service;

import org.springframework.util.Assert;
import wiki.entity.Member;
import wiki.entity.Paper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountedCompleter;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.IntStream.range;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  19:38
 */


public class PaperMiningTask extends CountedCompleter<Void> {

    private ConstantOptions options;

    private final int start;
    private final int end;
    private int availableThreads;

    public PaperMiningTask( PaperMiningTask parent, ConstantOptions options, int availableThreads, int start, int end ) {
        super(parent);

        Assert.notNull(options, "ConstantOptions required");
        Assert.isTrue(start <= end, "Invalid list bounds");
        Assert.isTrue(availableThreads >= 0, "available threads is negative");

        this.options = options;
        this.availableThreads = availableThreads;
        this.start = start;
        this.end = end;
    }


    @Override
    public void compute() {

        if (availableThreads < 2) {

            getAndSave(options.getPages(), start, end);

        } else {

            int mid = ( start + end ) / 2;
            availableThreads -= 2;

            setPendingCount(2);
            PaperMiningTask task1 = new PaperMiningTask(this, options, availableThreads / 2, start, mid);
            PaperMiningTask task2 = new PaperMiningTask(this, options, availableThreads / 2, mid, end);

            task1.fork();
            task2.fork();
        }

        tryComplete();
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

            Path path = Paths.get(options.getCategory().getFileFullPath(index));
            String dataToWrite = String.format("%s\n\n\n%s\n", paper.getUrl(), paper.getContent());
            Files.createDirectories(path.getParent());
            Files.write(path, dataToWrite.getBytes(), WRITE, CREATE);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
