package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.result.BatchResult;
import com.gregory.testing.result.RunResult;

import java.util.ArrayList;
import java.util.Collections;
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
        List<BatchResult> batchResults = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while (messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            List<RunResult> runResults = new ArrayList<>();
            for (int runId = 0; runId < runs; runId++) {
                Task task = new Task(runId, server, batchMessages);
                runResults.add(task.run());
            }
            batchResults.add(new BatchResult(batchId++, runResults));
        }
        return batchResults;
    }

    private List<BatchResult> runIncrementalLoad() throws ExecutionException, InterruptedException {
        int batchSize = testCase.initialBatchSize();
        int runs = testCase.runs();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> batchResults = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while (messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            batchSize++;
            List<RunResult> runResults = new ArrayList<>();
            for (int runId = 0; runId < runs; runId++) {
                Task task = new Task(runId, server, batchMessages);
                runResults.add(task.run());
            }
            batchResults.add(new BatchResult(batchId++, runResults));
        }
        return batchResults;
    }

    private List<BatchResult> runStressLoad() throws ExecutionException, InterruptedException {
        int runs = testCase.runs();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<RunResult> runResults = new ArrayList<>();
        for (int runId = 0; runId < runs; runId++) {
            Task task = new Task(runId, server, messages);
            runResults.add(task.run());
        }
        return Collections.singletonList(new BatchResult(0, runResults));
    }

}
