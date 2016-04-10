package com.gregory.testing.report;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import com.gregory.testing.result.Communication;
import com.gregory.testing.result.RunResult;
import com.gregory.testing.strategy.TestingStrategy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.gregory.testing.utils.FileUtils.readResourceLines;
import static com.gregory.testing.utils.FileUtils.writeToFile;
import static com.gregory.testing.utils.StringUtils.join;

public final class ChartGenerator {

    private final TestingStrategy strategy;
    private final List<BatchResult> results;

    public ChartGenerator(TestingStrategy strategy, List<BatchResult> results) {
        this.strategy = strategy;
        this.results = results;
    }

    public void generate(String output) throws IOException, URISyntaxException {
        String chartHtml = "chart.html";
        String template = readResourceLines(chartHtml);
        writeToFile(
                output + "/" + strategy.name().toLowerCase() + "_" + chartHtml,
                template.replace("${TITLE}", strategy.name())
                        .replace("${DATA}", extractDataFromBatchResults(results)));
    }

    private String extractDataFromBatchResults(List<BatchResult> results) {
        String data = "";
        for (int i = 0; i < results.size(); i++) {
            data += extractDataFromBatchResult(results.get(i));
            if (i < results.size() - 1) {
                data += ",\n";
            }
        }
        return data;
    }

    private String extractDataFromBatchResult(BatchResult result) {
        long sum = 0;
        long n = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (RunResult run : result.runs()) {
            for (Communication communication : run.communications()) {
                TimestampedMessage request = communication.request();
                TimestampedMessage response = communication.response();
                long delta = response.timestamp() - request.timestamp();
                sum += delta;
                min = Math.min(min, delta);
                max = Math.max(max, delta);
                n++;
            }
        }
        double mean = sum * 1.0 / n;
        long var = 0;
        for (RunResult run : result.runs()) {
            for (Communication communication : run.communications()) {
                TimestampedMessage request = communication.request();
                TimestampedMessage response = communication.response();
                long delta = response.timestamp() - request.timestamp();
                var += (delta - mean) * (delta - mean);
            }
        }
        double std = Math.sqrt(var * 1.0 / n);
        String data = "[";
        data += join(",", result.batchId(), mean, min, max, mean - std, mean + std);
        data += "]";
        return data;
    }

}
