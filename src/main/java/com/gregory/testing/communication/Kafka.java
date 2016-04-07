package com.gregory.testing.communication;

abstract class Kafka implements Channel {

    private final String topic;

    public Kafka(String topic) {
        this.topic = topic;
    }

}
