package com.gregory.testing.run;

import com.google.common.collect.Lists;
import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.List;

public final class TestRunner {

    private final TestCase testCase;

    public TestRunner(TestCase testCase) {
        this.testCase = testCase;
    }

    public List<BatchResult> run() throws Exception {
        switch (testCase.strategy()) {
            case CONSTANT_LOAD:
                return runConstantLoad();
            case INCREMENTAL_LOAD:
                return runIncrementalLoad();
            default:
                throw new RuntimeException("Strategy " + testCase.strategy().name() + " is not supported yet!");
        }
    }

    private List<BatchResult> runConstantLoad() throws Exception {
        int batchSize = testCase.initialBatchSize();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        int batchId = 0;
        for (List<Message> batchMessages : Lists.partition(messages, batchSize)) {
            BatchTask task = new BatchTask(batchId++, server, batchMessages);
            results.add(task.call());
        }
        return results;
    }

    private List<BatchResult> runIncrementalLoad() throws Exception {
        int batchSize = testCase.initialBatchSize();
        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchResult> results = new ArrayList<>();
        int batchId = 0;
        int messageIndex = 0;
        while(messageIndex < messages.size()) {
            List<Message> batchMessages = messages.subList(messageIndex, Math.min(messages.size(), messageIndex + batchSize));
            messageIndex += batchSize;
            batchSize++;
            BatchTask task = new BatchTask(batchId++, server, batchMessages);
            results.add(task.call());
        }
        return results;
    }

}
