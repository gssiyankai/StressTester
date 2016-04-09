package com.gregory.testing.run;

import com.gregory.testing.application.Server;
import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.BatchResult;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.fest.assertions.Assertions.assertThat;

public class BatchTaskTest {

    @Test
    public void it_should_send_a_request_and_get_a_reponse() throws ExecutionException, InterruptedException {
        final List<Message> messages =
                Collections.singletonList(new Message("hello".getBytes()));

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
        BatchTask task = new BatchTask(0, server, messages);
        BatchResult result = task.run();
        assertThat(result.id()).isEqualTo(0);
        assertThat(result.requests()).hasSize(1);
        assertThat(result.responses()).hasSize(1);
    }

}
