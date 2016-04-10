package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

import java.util.List;

public final class BatchResult {

    private final int batchId;
    private final int runId;
    private final List<TimestampedMessage> requests;
    private final List<TimestampedMessage> responses;

    public BatchResult(int batchId, int runId, List<TimestampedMessage> requests, List<TimestampedMessage> responses) {
        this.batchId = batchId;
        this.runId = runId;
        this.requests = requests;
        this.responses = responses;
    }

    public int batchId() {
        return batchId;
    }

    public int runId() {
        return runId;
    }

    public List<TimestampedMessage> requests() {
        return requests;
    }

    public List<TimestampedMessage> responses() {
        return responses;
    }

}
