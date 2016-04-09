package com.gregory.testing.run;

import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class BatchProducer implements Callable<List<TimestampedMessage>> {

    private final InputChannel input;
    private final List<Message> messages;

    public BatchProducer(InputChannel input, List<Message> messages) {
        this.input = input;
        this.messages = messages;
    }

    @Override
    public List<TimestampedMessage> call() throws Exception {
        List<TimestampedMessage> requests = new ArrayList<>(messages.size());
        for (Message message : messages) {
            System.out.println("Sending: " + new String(message.data()));
            TimestampedMessage request = input.sendMessage(message);
            requests.add(request);
        }
        return requests;
    }

}
