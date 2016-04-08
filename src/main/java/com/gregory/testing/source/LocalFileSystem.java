package com.gregory.testing.source;

import com.gregory.testing.message.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class LocalFileSystem {

    private LocalFileSystem() {
    }

    public static List<Message> messagesFromPath(String path) throws FileNotFoundException {
        List<Message> messages = new ArrayList<>();
        File file = new File(path);
        if(file.isDirectory()) {
            for (File f : file.listFiles()) {
                messages.addAll(messagesFromPath(f.getPath()));
            }
        } else {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    messages.add(new Message(line.getBytes()));
                }
            }
        }
        return messages;
    }

    public static List<Message> messagesFromPath(String... paths) throws FileNotFoundException {
        List<Message> messages = new ArrayList<>();
        for (String path : paths) {
            messages.addAll(messagesFromPath(path));
        }
        return messages;
    }

}
