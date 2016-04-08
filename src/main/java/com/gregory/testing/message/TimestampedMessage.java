package com.gregory.testing.message;

public final class TimestampedMessage {

    private final long timestamp;
    private final Message message;

    public TimestampedMessage(long timestamp, Message message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public long timestamp() {
        return timestamp;
    }

    public Message message() {
        return message;
    }

}
