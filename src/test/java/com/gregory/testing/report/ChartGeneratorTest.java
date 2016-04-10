package com.gregory.testing.report;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import com.gregory.testing.result.Communication;
import com.gregory.testing.result.RunResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static com.gregory.testing.strategy.TestingStrategy.INCREMENTAL_LOAD;
import static org.fest.assertions.Assertions.assertThat;

public class ChartGeneratorTest {

    @Test
    public void it_should_generate_an_html_report() throws IOException, URISyntaxException {
        TimestampedMessage request = new TimestampedMessage(0, new Message("foo".getBytes()));
        TimestampedMessage response = new TimestampedMessage(0, new Message("bar".getBytes()));
        RunResult runResult = new RunResult(0, Collections.singletonList(new Communication(request, response)));
        BatchResult result = new BatchResult(0, Collections.singletonList(runResult));
        ChartGenerator charts = new ChartGenerator(INCREMENTAL_LOAD, Collections.singletonList(result));
        charts.generate("target/ChartGeneratorTest");
        assertThat(new File("target/ChartGeneratorTest/incremental_load_chart.html")).exists();
    }

}
