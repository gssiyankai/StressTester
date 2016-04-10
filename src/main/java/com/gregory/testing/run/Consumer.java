package com.gregory.testing.run;

import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.TimestampedMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class Consumer implements Callable<List<TimestampedMessage>> {

    private final OutputChannel output;
    private final int expectedResponses;

    public Consumer(OutputChannel output, int expectedResponses) {
        this.output = output;
        this.expectedResponses = expectedResponses;
    }

    @Override
    public List<TimestampedMessage> call() throws Exception {
        List<TimestampedMessage> responses = new ArrayList<>(expectedResponses);
        for (int i = 0; i < expectedResponses; i++) {
            TimestampedMessage response = output.getMessage();
            System.out.println("Received: " + new String(response.message().data()));
            responses.add(response);
        }
        return responses;
    }

}
