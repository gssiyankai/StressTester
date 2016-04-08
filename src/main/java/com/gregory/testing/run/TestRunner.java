package com.gregory.testing.run;

import com.google.common.collect.Lists;
import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
            case STRESS_LOAD:
                return runStressLoad();
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

    private List<BatchResult> runStressLoad() throws ExecutionException, InterruptedException {
        int numberOfThreads = 16;

        Server server = testCase.server();
        List<Message> messages = testCase.messages();
        List<BatchTask> tasks = new ArrayList<>();
        int batchId = 0;
        for (List<Message> batchMessages : Lists.partition(messages, messages.size()/numberOfThreads)) {
            BatchTask task = new BatchTask(batchId++, server, batchMessages);
            tasks.add(task);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<BatchResult>> futures = new ArrayList<>(tasks.size());
        for (BatchTask task : tasks) {
            futures.add(executorService.submit(task));
        }
        List<BatchResult> results = new ArrayList<>();
        for (Future<BatchResult> future : futures) {
            results.add(future.get());
        }
        return results;
    }

}
