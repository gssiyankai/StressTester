package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

import java.util.List;

public final class RunResult {

    private final int runId;
    private final List<TimestampedMessage> requests;
    private final List<TimestampedMessage> responses;

    public RunResult(int runId, List<TimestampedMessage> requests, List<TimestampedMessage> responses) {
        this.runId = runId;
        this.requests = requests;
        this.responses = responses;
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
