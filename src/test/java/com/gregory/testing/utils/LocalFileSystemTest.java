package com.gregory.testing.utils;

import com.gregory.testing.message.Message;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static com.gregory.testing.source.LocalFileSystem.messagesFromPath;
import static org.fest.assertions.Assertions.assertThat;

public final class LocalFileSystemTest {

    @Test
    public void it_should_create_messages_from_path() throws FileNotFoundException {
        List<Message> messages = messagesFromPath("src/test/resources/messages");
        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).data()).isEqualTo("Hello world!".getBytes());
        assertThat(messages.get(1).data()).isEqualTo("Wazza bro!".getBytes());
    }

}
