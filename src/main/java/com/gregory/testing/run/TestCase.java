package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.strategy.TestingStrategy;

import java.util.List;

public final class TestCase {

    private final Server server;
    private final List<Message> messages;
    private final TestingStrategy strategy;
    private final int initialBatchSize;
    private final int runs;

    public TestCase(Server server, List<Message> messages, TestingStrategy strategy, int initialBatchSize, int runs) {
        this.server = server;
        this.messages = messages;
        this.strategy = strategy;
        this.initialBatchSize = initialBatchSize;
        this.runs = runs;
    }

    public Server server() {
        return server;
    }

    public List<Message> messages() {
        return messages;
    }

    public TestingStrategy strategy() {
        return strategy;
    }

    public int initialBatchSize() {
        return initialBatchSize;
    }

    public int runs() {
        return runs;
    }

}
