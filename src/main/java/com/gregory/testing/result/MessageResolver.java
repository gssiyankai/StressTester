package com.gregory.testing.result;

import com.gregory.testing.message.TimestampedMessage;

import java.util.List;

public interface MessageResolver {

    List<Communication> resolve(List<TimestampedMessage> requests, List<TimestampedMessage> responses);

}
