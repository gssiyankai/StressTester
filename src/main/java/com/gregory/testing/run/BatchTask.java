package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class BatchTask implements Callable<BatchResult> {

    private final int id;
    private final Server server;
    private final List<Message> messages;

    public BatchTask(int id, Server server, List<Message> messages) {
        this.id = id;
        this.server = server;
        this.messages = messages;
    }

    @Override
    public BatchResult call() throws Exception {
        InputChannel input = server.input();
        OutputChannel output = server.output();
        List<TimestampedMessage> requests = new ArrayList<>(messages.size());
        List<TimestampedMessage> responses = new ArrayList<>(messages.size());
        for (Message message : messages) {
            System.out.println("Sending: " + new String(message.data()));
            TimestampedMessage request = input.sendMessage(message);
            requests.add(request);
        }
        for (int i = 0; i < messages.size(); i++) {
            TimestampedMessage response = output.getMessage();
            System.out.println("Received: " + new String(response.message().data()));
            responses.add(response);
        }
        return new BatchResult(id, requests, responses);
    }

}
