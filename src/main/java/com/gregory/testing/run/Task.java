package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.Communication;
import com.gregory.testing.result.MessageResolver;
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
    private final MessageResolver messageResolver;

    public Task(int runId, Server server, List<Message> messages, MessageResolver messageResolver) {
        this.runId = runId;
        this.server = server;
        this.messages = messages;
        this.messageResolver = messageResolver;
    }

    public RunResult run() throws ExecutionException, InterruptedException {
        InputChannel input = server.input();
        OutputChannel output = server.output();
        Producer producer = new Producer(input, messages);
        Consumer consumer = new Consumer(output, messages.size());

        Future<List<TimestampedMessage>> responseFutures = EXECUTOR_SERVICE.submit(consumer);
        Future<List<TimestampedMessage>> requestFutures = EXECUTOR_SERVICE.submit(producer);

        List<TimestampedMessage> requests = requestFutures.get();
        List<TimestampedMessage> responses = responseFutures.get();
        List<Communication> communications = messageResolver.resolve(requests, responses);

        return new RunResult(runId, communications);
    }

}
