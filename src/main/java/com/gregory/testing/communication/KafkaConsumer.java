package com.gregory.testing.communication;

public class KafkaConsumer implements Channel {

    private final String zookeeper;

    public KafkaConsumer(String zookeeper) {
        this.zookeeper = zookeeper;
    }

}
