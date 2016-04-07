package com.gregory.testing.message;

public class TimestampedMessage {

    private final long timestamp;
    private final Message message;

    public TimestampedMessage(long timestamp, Message message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
