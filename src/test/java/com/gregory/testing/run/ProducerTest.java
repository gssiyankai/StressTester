package com.gregory.testing.run;

import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProducerTest {

    @Test
    public void it_should_send_messages() throws Exception {
        final List<Message> sentMessages = new ArrayList<>();
        InputChannel input = new InputChannel() {
            @Override
            public TimestampedMessage sendMessage(Message message) {
                sentMessages.add(message);
                return null;
            }
        };
        List<Message> messages = Collections.singletonList(new Message("hello".getBytes()));
        Producer producer = new Producer(input, messages);
        producer.call();
        Assertions.assertThat(sentMessages).isEqualTo(messages);
    }

}
