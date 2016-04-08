package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

import java.util.List;

public final class BatchResult {

    private final int id;
    private final List<TimestampedMessage> requests;
    private final List<TimestampedMessage> responses;

    public BatchResult(int id, List<TimestampedMessage> requests, List<TimestampedMessage> responses) {
        this.id = id;
        this.requests = requests;
        this.responses = responses;
    }

    public int id() {
        return id;
    }

    public List<TimestampedMessage> requests() {
        return requests;
    }

    public List<TimestampedMessage> responses() {
        return responses;
    }

}
