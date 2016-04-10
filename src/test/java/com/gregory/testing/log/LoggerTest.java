package com.gregory.testing.log;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static com.gregory.testing.log.Logger.saveAsCsv;
import static org.fest.assertions.Assertions.assertThat;

public class LoggerTest {

    @Test
    public void it_should_save_results_as_csv() throws IOException {
        TimestampedMessage request = new TimestampedMessage(100, new Message("Hello".getBytes()));
        TimestampedMessage response = new TimestampedMessage(101, new Message("Hi".getBytes()));
        BatchResult result = new BatchResult(0, 0, Collections.singletonList(request), Collections.singletonList(response));
        File actualResultFile = new File("target/LoggerTest/results.csv");
        File expectedResultFile = new File("src/test/resources/results/results.txt");
        if (actualResultFile.exists()) {
            actualResultFile.delete();
        }
        assertThat(actualResultFile).doesNotExist();
        saveAsCsv(actualResultFile.getPath(), "\t", Collections.singletonList(result));
        assertThat(actualResultFile).exists().hasSameContentAs(expectedResultFile);
    }

}