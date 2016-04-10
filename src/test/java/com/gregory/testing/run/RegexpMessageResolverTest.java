package com.gregory.testing.run;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;
import com.gregory.testing.result.Communication;
import com.gregory.testing.result.MessageResolver;
import com.gregory.testing.result.RegexpMessageResolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.fest.assertions.Assertions.assertThat;

public class RegexpMessageResolverTest {

    @Test
    public void it_should_resolve_messages() throws ExecutionException, InterruptedException {
        final List<TimestampedMessage> requests = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Message message = new Message(String.format("hello%03d", i).getBytes());
            requests.add(new TimestampedMessage(0, message));
        }
        final List<TimestampedMessage> responses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Message message = new Message(String.format("hi%03d", i).getBytes());
            responses.add(new TimestampedMessage(0, message));
        }

        MessageResolver resolver = new RegexpMessageResolver("hello00(\\d)", "$1", "hi00(\\d)", "$1");
        List<Communication> results = resolver.resolve(requests, responses);

        assertThat(results).hasSize(3);
        for (int i = 0; i < 3; i++) {
            assertThat(results.get(i).request().message().data()).isEqualTo(String.format("hello%03d", i).getBytes());
            assertThat(results.get(i).response().message().data()).isEqualTo(String.format("hi%03d", i).getBytes());
        }
    }

    @Test(expected = RuntimeException.class)
    public void it_should_throw_an_exception_when_resolution_fails() throws ExecutionException, InterruptedException {
        final List<TimestampedMessage> requests = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Message message = new Message(String.format("hello%03d", i).getBytes());
            requests.add(new TimestampedMessage(0, message));
        }
        final List<TimestampedMessage> responses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Message message = new Message(String.format("hi%03d", i + 1).getBytes());
            responses.add(new TimestampedMessage(0, message));
        }

        MessageResolver resolver = new RegexpMessageResolver("hello00(\\d)", "$1", "hi00(\\d)", "$1");
        resolver.resolve(requests, responses);
    }


}
