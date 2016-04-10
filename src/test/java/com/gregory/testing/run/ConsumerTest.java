package com.gregory.testing.run;

import com.gregory.testing.communication.OutputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ConsumerTest {

    @Test
    public void it_should_receive_messages() throws Exception {
        final List<TimestampedMessage> messages =
                Collections.singletonList(
                        new TimestampedMessage(100, new Message("hello".getBytes())));
        OutputChannel output = new OutputChannel() {
            @Override
            public TimestampedMessage getMessage() {
                return messages.get(0);
            }
        };
        Consumer consumer = new Consumer(output, 1);
        List<TimestampedMessage> receivedMessages = consumer.call();
        Assertions.assertThat(receivedMessages).isEqualTo(messages);
    }

}
