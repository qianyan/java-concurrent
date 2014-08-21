package com.tw.filecounts;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class FileCounter {
    private AtomicLong totalFileCounts = new AtomicLong();
    private AtomicLong pendingFilesToVisit = new AtomicLong();
    private final CountDownLatch latch = new CountDownLatch(1);
    ExecutorService service;

    public FileCounter() {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(availableProcessors);
    }

    public void countFiles(final File file) {
        long fileCounts = 0;
        if (file.isFile()) {
            fileCounts = 1;
        } else {
            final File[] children = file.listFiles();
            if (children != null) {
                for (final File child : children) {
                    if (child.isFile()) {
                        fileCounts += 1;
                    } else {
                        pendingFilesToVisit.incrementAndGet();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                countFiles(child);
                            }
                        });
                    }
                }
            }
        }
        totalFileCounts.addAndGet(fileCounts);
        if(pendingFilesToVisit.decrementAndGet() == 0) latch.countDown();
    }

    public long totalFilesCount(final String fileName) throws InterruptedException {
        pendingFilesToVisit.incrementAndGet();
        countFiles(new File(fileName));
        try {
            latch.await(1000, TimeUnit.SECONDS);
            return totalFileCounts.longValue();
        } finally {
            service.shutdown();
        }
    }
}
