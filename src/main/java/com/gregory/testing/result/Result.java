package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

public final class Result {

    private final TimestampedMessage request;
    private final TimestampedMessage response;

    public Result(TimestampedMessage request, TimestampedMessage response) {
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
