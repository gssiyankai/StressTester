package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TestRunner {

    private final TestCase testCase;

    public TestRunner(TestCase testCase) {
        this.testCase = testCase;
    }

    public List<BatchResult> run() {
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

    private List<BatchResult> runConstantLoad() {
        int batchSize = testCase.initialBatchSize();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while (messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            BatchTask task = new BatchTask(batchId++, server, batchMessages);
            results.add(task.run());
        }
        return results;
    }

    private List<BatchResult> runIncrementalLoad() {
        int batchSize = testCase.initialBatchSize();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while (messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            batchSize++;
            BatchTask task = new BatchTask(batchId++, server, batchMessages);
            results.add(task.run());
        }
        return results;
    }

    private List<BatchResult> runStressLoad() {
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        BatchTask task = new BatchTask(0, server, messages);
        BatchResult result = task.run();
        return Collections.singletonList(result);
    }

}
