package wiki.service;

import wiki.entity.Member;
import wiki.entity.Paper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  19:38
 */


public class PaperMiningTask extends RecursiveTask<List<Paper>> {

    private final List<Member> pages;
    private final int start;
    private final int end;
    private int availableThreads;

    public PaperMiningTask(List<Member> pages, int availableThreads, int start, int end) {
        if (availableThreads < 0) {
            throw new IllegalArgumentException("Available thread < 0");
        }
        this.pages = pages;
        this.availableThreads = availableThreads;
        this.start = start;
        this.end = end;
    }


    @Override
    protected List<Paper> compute() {

        if (availableThreads == 0) {

            return get(pages.subList(start, end));

        } else if (availableThreads == 1) {

            System.out.printf("thread left %s \n", availableThreads);

            int mid = ( start + end ) / 2;

            PaperMiningTask task = new PaperMiningTask(pages, 0, start, mid);
            task.fork();

            List<Paper> papers = get(pages.subList(mid, end));

            return merge(papers, task.join());

        } else {

            int mid = ( start + end ) / 2;
            availableThreads -= 2;

            PaperMiningTask task1 = new PaperMiningTask(pages, availableThreads / 2, start, mid);
            PaperMiningTask task2 = new PaperMiningTask(pages, availableThreads / 2, start, mid);

            task1.fork();
            task2.fork();

            System.out.printf("fork (%d, %d) - (%d, %d) thread left - %s\n", start, mid, mid, end, availableThreads);

            return merge(task1.join(), task2.join());
        }
    }

    private List<Paper> get(List<Member> pages) {
        System.out.println(pages.size());
        return pages
                .stream()
                .map(page -> WikiApi.getPaper(page.getId()))
                .collect(Collectors.toList());
    }

    private List<Paper> merge(List<Paper> paperList1, List<Paper> paperList2) {
        ArrayList<Paper> papers = new ArrayList<>(paperList1);
        papers.addAll(paperList2);
        return papers;
    }

}
