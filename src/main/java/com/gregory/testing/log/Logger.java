package com.gregory.testing.log;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import com.gregory.testing.result.RunResult;

import java.io.IOException;
import java.util.List;

import static com.gregory.testing.utils.FileUtils.writeToFile;
import static com.gregory.testing.utils.StringUtils.join;

public final class Logger {

    private Logger() {
    }

    public static void saveAsCsv(String path, String separator, List<BatchResult> results) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (BatchResult result : results) {
            int batchId = result.batchId();
            for (RunResult runResult : result.runs()) {
                int runId = runResult.runId();
                List<TimestampedMessage> requests = runResult.requests();
                for (TimestampedMessage request : requests) {
                    builder.append(messageToRow(batchId, runId, "request", request, separator));
                    builder.append("\n");
                }
                List<TimestampedMessage> responses = runResult.responses();
                for (TimestampedMessage response : responses) {
                    builder.append(messageToRow(batchId, runId, "response", response, separator));
                    builder.append("\n");
                }
            }
        }
        writeToFile(path, builder.toString());
    }

    private static String messageToRow(int batchId, int runId, String messageType, TimestampedMessage message, String separator) {
        return join(separator, batchId, runId, messageType, message.timestamp(), new String(message.message().data()));
    }

}
