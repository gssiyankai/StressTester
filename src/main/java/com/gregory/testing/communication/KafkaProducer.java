package com.gregory.testing.communication;

public class KafkaProducer implements Channel {

    private final String broker;

    public KafkaProducer(String broker) {
        this.broker = broker;
    }

}
