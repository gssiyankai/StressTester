package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.TestResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TestRunner {

    private final TestCase testCase;

    public TestRunner(TestCase testCase) {
        this.testCase = testCase;
    }

    public void run() throws Exception {
        switch (testCase.strategy()) {
            case CONSTANT_LOAD:
                runConstantLoad();
                break;
            default:
                throw new RuntimeException("Strategy " + testCase.strategy().name() + " is not supported yet!");
        }
    }

    private List<TestResult> runConstantLoad() throws IOException {
        List<TestResult> results = new ArrayList<>();
        Server server = testCase.server();
        InputChannel input = server.input();
        OutputChannel output = server.output();
        for (Message message : testCase.messages()) {
            TimestampedMessage request = input.sendMessaage(message);
            TimestampedMessage response = output.getMessage();
            results.add(new TestResult(request, response));
        }
        return results;
    }

}
