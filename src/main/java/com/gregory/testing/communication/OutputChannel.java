package com.gregory.testing.communication;

import com.gregory.testing.message.TimestampedMessage;

public interface OutputChannel {
    TimestampedMessage getMessage();
}
