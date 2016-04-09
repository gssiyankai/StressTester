package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class BatchTask {

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    private final int id;
    private final Server server;
    private final List<Message> messages;

    public BatchTask(int id, Server server, List<Message> messages) {
        this.id = id;
        this.server = server;
        this.messages = messages;
    }

    public BatchResult run() throws ExecutionException, InterruptedException {
        InputChannel input = server.input();
        OutputChannel output = server.output();
        BatchProducer producer = new BatchProducer(input, messages);
        BatchConsumer consumer = new BatchConsumer(output, messages.size());

        Future<List<TimestampedMessage>> responses = EXECUTOR_SERVICE.submit(consumer);
        Future<List<TimestampedMessage>> requests = EXECUTOR_SERVICE.submit(producer);

        return new BatchResult(id, requests.get(), responses.get());
    }

}
