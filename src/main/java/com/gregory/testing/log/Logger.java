package com.gregory.testing.log;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.TestResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public final class Logger {

    private Logger() {
    }

    public static void saveAsCsv(String path, String separator, List<TestResult> results) throws IOException {
        try(FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            for (TestResult result : results) {
                TimestampedMessage request = result.request();
                TimestampedMessage response = result.response();
                String line = request.timestamp() + separator
                        + new String(request.message().data()) + separator
                        + response.timestamp() + separator
                        + new String(response.message().data())
                        + "\n";
                out.append(line);
            }
        }
    }

}
