package com.gregory.testing.log;

import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import com.gregory.testing.result.Communication;
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
                for (Communication communication : runResult.communications()) {
                    TimestampedMessage request = communication.request();
                    TimestampedMessage response = communication.response();
                    builder.append(messageToRow(batchId, runId, request, response, separator));
                    builder.append("\n");
                }
            }
        }
        writeToFile(path, builder.toString());
    }

    private static String messageToRow(int batchId, int runId, TimestampedMessage request, TimestampedMessage response, String separator) {
        return join(separator, batchId, runId,
                request.timestamp(), new String(request.message().data()),
                response.timestamp(), new String(response.message().data()));
    }

}
