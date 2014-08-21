package com.tw.filecounts;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class FileCounterTest {
    @Test
    public void should_get_right_directories_count() throws Exception {
        final String fileName = "dir";
        final FileCounter counter = new FileCounter();

        assertThat(counter.totalFilesCount(fileName), is(2L));
    }
}
