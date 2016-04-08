package com.gregory.testing.log;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

import java.io.*;
import java.util.List;

public final class Logger {

    private Logger() {
    }

    public static void saveAsCsv(String path, String separator, List<BatchResult> results) throws IOException {
        for (BatchResult result : results) {
            try (FileWriter fw = new FileWriter(new File(path, result.id() + ".csv"));
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                List<TimestampedMessage> requests = result.requests();
                for (TimestampedMessage request : requests) {
                    String line = request.timestamp() + separator
                            + new String(request.message().data()) + separator
                            + "1"
                            + "\n";
                    out.append(line);
                }
                List<TimestampedMessage> responses = result.responses();
                for (TimestampedMessage response : responses) {
                    String line = response.timestamp() + separator
                            + new String(response.message().data()) + separator
                            + "0"
                            + "\n";
                    out.append(line);
                }
            }
        }
    }

}
