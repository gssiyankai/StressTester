package com.gregory.testing.message;

public final class Message {

    private final byte[] data;

    public Message(byte[] data) {
        this.data = data;
    }

    public byte[] data() {
        return data;
    }

}
