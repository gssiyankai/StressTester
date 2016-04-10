package com.gregory.testing.result;

import java.util.List;

public final class BatchResult {

    private final int batchId;
    private final List<RunResult> runs;

    public BatchResult(int batchId, List<RunResult> runs) {
        this.batchId = batchId;
        this.runs = runs;
    }

    public int batchId() {
        return batchId;
    }

    public List<RunResult> runs() {
        return runs;
    }

}
