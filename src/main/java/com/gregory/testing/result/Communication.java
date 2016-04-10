package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

public class Communication {

    private final TimestampedMessage request;
    private final TimestampedMessage response;

    public Communication(TimestampedMessage request, TimestampedMessage response) {
        this.request = request;
        this.response = response;
    }

    public TimestampedMessage request() {
        return request;
    }

    public TimestampedMessage response() {
        return response;
    }

}
