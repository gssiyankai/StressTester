package com.gregory.testing.result;

import java.util.List;

public final class RunResult {

    private final int runId;
    private final List<Communication> communications;

    public RunResult(int runId, List<Communication> communications) {
        this.runId = runId;
        this.communications = communications;
    }

    public int runId() {
        return runId;
    }

    public List<Communication> communications() {
        return communications;
    }

}
