package com.gregory.testing.log;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

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
            List<TimestampedMessage> requests = result.requests();
            for (TimestampedMessage request : requests) {
                builder.append(messageToRow(result.batchId(), result.runId(), "request", request, separator));
                builder.append("\n");
            }
            List<TimestampedMessage> responses = result.responses();
            for (TimestampedMessage response : responses) {
                builder.append(messageToRow(result.batchId(), result.runId(), "response", response, separator));
                builder.append("\n");
            }
        }
        writeToFile(path, builder.toString());
    }

    private static String messageToRow(int batchId, int runId, String messageType, TimestampedMessage message, String separator) {
        return join(separator, batchId, runId, messageType, message.timestamp(), new String(message.message().data()));
    }

}
