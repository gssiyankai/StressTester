package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.message.Message;
import com.gregory.testing.strategy.TestingStrategy;

import java.util.List;

public class TestCase {

    private final Server server;
    private final List<Message> messages;
    private final TestingStrategy strategy;

    public TestCase(Server server, List<Message> messages, TestingStrategy strategy) {
        this.server = server;
        this.messages = messages;
        this.strategy = strategy;
    }

}
