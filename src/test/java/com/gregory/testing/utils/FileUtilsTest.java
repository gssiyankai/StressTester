package com.gregory.testing.utils;

import org.junit.Test;

import java.io.File;

import static com.gregory.testing.utils.FileUtils.writeToFile;
import static org.fest.assertions.Assertions.assertThat;

public final class FileUtilsTest {

    @Test
    public void it_should_read_resource_file() {
        assertThat(FileUtils.readResourceLines("messages2/third.txt")).isEqualTo("Yo!");
    }

    @Test
    public void it_should_write_file() {
        String path = "target/FileUtilsTest/dummy.txt";
        writeToFile(path, "bar");
        assertThat(new File(path)).exists();
    }

}
