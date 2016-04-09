package com.gregory.testing.log;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.gregory.testing.utils.Utils.join;

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
        return join(separator, batchId, messageType, new String(message.message().data()));
    }

}
