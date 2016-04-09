package com.gregory.testing.utils;

import com.gregory.testing.message.Message;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static com.gregory.testing.source.LocalFileSystem.messagesFromPath;
import static com.gregory.testing.source.LocalFileSystem.messagesFromPaths;
import static org.fest.assertions.Assertions.assertThat;

public final class LocalFileSystemTest {

    @Test
    public void it_should_create_messages_from_path() throws FileNotFoundException {
        List<Message> messages = messagesFromPath("src/test/resources/messages1");
        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).data()).isEqualTo("Hello world!".getBytes());
        assertThat(messages.get(1).data()).isEqualTo("Wazza bro!".getBytes());
    }

    @Test
    public void it_should_create_messages_from_multiple_paths() throws FileNotFoundException {
        List<Message> messages = messagesFromPaths("src/test/resources/messages1", "src/test/resources/messages2");
        assertThat(messages).hasSize(3);
        assertThat(messages.get(0).data()).isEqualTo("Hello world!".getBytes());
        assertThat(messages.get(1).data()).isEqualTo("Wazza bro!".getBytes());
        assertThat(messages.get(2).data()).isEqualTo("Yo!".getBytes());
    }



}
