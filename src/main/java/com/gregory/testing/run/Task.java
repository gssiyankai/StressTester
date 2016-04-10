package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.RunResult;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Task {

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    private final int runId;
    private final Server server;
    private final List<Message> messages;

    public Task(int runId, Server server, List<Message> messages) {
        this.runId = runId;
        this.server = server;
        this.messages = messages;
    }

    public RunResult run() throws ExecutionException, InterruptedException {
        InputChannel input = server.input();
        OutputChannel output = server.output();
        Producer producer = new Producer(input, messages);
        Consumer consumer = new Consumer(output, messages.size());

        Future<List<TimestampedMessage>> responses = EXECUTOR_SERVICE.submit(consumer);
        Future<List<TimestampedMessage>> requests = EXECUTOR_SERVICE.submit(producer);

        return new RunResult(runId, requests.get(), responses.get());
    }

}
