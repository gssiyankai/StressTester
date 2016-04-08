package com.gregory.testing.communication;

import com.gregory.testing.message.Message;
import com.gregory.testing.message.TimestampedMessage;

public interface InputChannel {
    TimestampedMessage sendMessaage(Message message);
}
