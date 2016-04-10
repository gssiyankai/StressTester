package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class TestRunner {

    private final TestCase testCase;

    public TestRunner(TestCase testCase) {
        this.testCase = testCase;
    }

    public List<BatchResult> run() throws ExecutionException, InterruptedException {
        switch (testCase.strategy()) {
            case CONSTANT_LOAD:
                return runConstantLoad();
            case INCREMENTAL_LOAD:
                return runIncrementalLoad();
            case STRESS_LOAD:
                return runStressLoad();
            default:
                throw new RuntimeException("Strategy " + testCase.strategy().name() + " is not supported yet!");
        }
    }

    private List<BatchResult> runConstantLoad() throws ExecutionException, InterruptedException {
        int batchSize = testCase.initialBatchSize();
        int runs = testCase.runs();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while (messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            for (int runId = 0; runId < runs; runId++) {
                BatchTask task = new BatchTask(batchId, runId, server, batchMessages);
                results.add(task.run());
            }
            batchId++;
        }
        return results;
    }

    private List<BatchResult> runIncrementalLoad() throws ExecutionException, InterruptedException {
        int batchSize = testCase.initialBatchSize();
        int runs = testCase.runs();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while (messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            batchSize++;
            for (int runId = 0; runId < runs; runId++) {
                BatchTask task = new BatchTask(batchId, runId, server, batchMessages);
                results.add(task.run());
            }
            batchId++;
        }
        return results;
    }

    private List<BatchResult> runStressLoad() throws ExecutionException, InterruptedException {
        int runs = testCase.runs();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        for (int runId = 0; runId < runs; runId++) {
            BatchTask task = new BatchTask(0, runId, server, messages);
            results.add(task.run());
        }
        return results;
    }

}
