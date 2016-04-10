package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.MessageResolver;
import com.gregory.testing.result.RegexpMessageResolver;
import com.gregory.testing.result.RunResult;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.fest.assertions.Assertions.assertThat;

public class TaskTest {

    @Test
    public void it_should_send_a_request_and_get_a_reponse() throws ExecutionException, InterruptedException {
        final List<Message> messages =
                Collections.singletonList(new Message("hello".getBytes()));
        final MessageResolver messageResolver =
                new RegexpMessageResolver("(.*)", "$1", "(.*)", "$1");

        InputChannel input = new InputChannel() {
            @Override
            public TimestampedMessage sendMessage(Message message) {
                return new TimestampedMessage(100, message);
            }
        };
        OutputChannel output = new OutputChannel() {
            @Override
            public TimestampedMessage getMessage() {
                return new TimestampedMessage(200, messages.get(0));
            }
        };
        Server server = new Server("server", input, output);
        Task task = new Task(0, server, messages, messageResolver);
        RunResult result = task.run();
        assertThat(result.runId()).isEqualTo(0);
        assertThat(result.communications()).hasSize(1);
    }

}
