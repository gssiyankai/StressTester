package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.gregory.testing.strategy.TestingStrategy.*;
import static org.fest.assertions.Assertions.assertThat;

public class TestRunnerTest {

    private static final List<Message> messages =
            Arrays.asList(
                    new Message("hello".getBytes()),
                    new Message("bonjour".getBytes()),
                    new Message("hola".getBytes()),
                    new Message("hallo".getBytes()),
                    new Message("hej".getBytes()),
                    new Message("ciao".getBytes()));

    private final InputChannel input = new InputChannel() {
        int timestamp = 0;

        @Override
        public TimestampedMessage sendMessage(Message message) {
            return new TimestampedMessage(timestamp++, message);
        }
    };
    private final OutputChannel output = new OutputChannel() {
        int timestamp = 100;
        int i = 0;

        @Override
        public TimestampedMessage getMessage() {
            return new TimestampedMessage(timestamp++, messages.get(i++ % messages.size()));
        }
    };
    private final Server server = new Server("test", input, output);

    @Test
    public void it_should_send_same_amount_of_messages_in_constant_load() throws Exception {
        TestCase testCase = new TestCase(server, messages, CONSTANT_LOAD, 1, 1);
        TestRunner runner = new TestRunner(testCase);
        List<BatchResult> results = runner.run();
        assertThat(results).hasSize(6);
        for (int i = 0; i < 6; i++) {
            assertThat(results.get(i).batchId()).isEqualTo(i);
            assertThat(results.get(i).runs()).hasSize(1);
            assertThat(results.get(i).runs().get(0).runId()).isEqualTo(0);
            assertThat(results.get(i).runs().get(0).requests()).hasSize(1);
            assertThat(results.get(i).runs().get(0).responses()).hasSize(1);
        }
    }

    @Test
    public void it_should_send_increasing_amount_of_messages_in_incremental_load() throws Exception {
        TestCase testCase = new TestCase(server, messages, INCREMENTAL_LOAD, 1, 1);
        TestRunner runner = new TestRunner(testCase);
        List<BatchResult> results = runner.run();
        assertThat(results).hasSize(3);
        for (int i = 0; i < 3; i++) {
            assertThat(results.get(i).batchId()).isEqualTo(i);
            assertThat(results.get(i).runs()).hasSize(1);
            assertThat(results.get(i).runs().get(0).runId()).isEqualTo(0);
            assertThat(results.get(i).runs().get(0).requests()).hasSize(i + 1);
            assertThat(results.get(i).runs().get(0).responses()).hasSize(i + 1);
        }
    }

    @Test
    public void it_should_send_all_messages_in_stress_load() throws Exception {
        Server server = new Server("test", input, output);
        TestCase testCase = new TestCase(server, messages, STRESS_LOAD, 1, 1);
        TestRunner runner = new TestRunner(testCase);
        List<BatchResult> results = runner.run();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).batchId()).isEqualTo(0);
        assertThat(results.get(0).runs()).hasSize(1);
        assertThat(results.get(0).runs().get(0).runId()).isEqualTo(0);
        assertThat(results.get(0).runs().get(0).requests()).hasSize(6);
        assertThat(results.get(0).runs().get(0).responses()).hasSize(6);
    }

}
