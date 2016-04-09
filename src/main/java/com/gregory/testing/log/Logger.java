package com.gregory.testing.log;

import com.google.common.base.Joiner;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

import java.io.*;
import java.util.List;

public final class Logger {

    private Logger() {
    }

    public static void saveAsCsv(String path, String separator, List<BatchResult> results) throws IOException {
        try (FileWriter fw = new FileWriter(path + ".csv");
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (BatchResult result : results) {
                List<TimestampedMessage> requests = result.requests();
                for (TimestampedMessage request : requests) {
                    out.append(messageToRow(result.id(), "request", request, separator));
                }
                List<TimestampedMessage> responses = result.responses();
                for (TimestampedMessage response : responses) {
                    out.append(messageToRow(result.id(), "response", response, separator));
                }
            }
        }
    }

    private static String messageToRow(int batchId, String messageType, TimestampedMessage message, String separator) {
        return Joiner.on(separator)
                .join(batchId, messageType, new String(message.message().data()));
    }

}
