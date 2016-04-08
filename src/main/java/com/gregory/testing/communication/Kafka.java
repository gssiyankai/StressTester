package com.gregory.testing.communication;

abstract class Kafka {

    private final String topic;

    Kafka(String topic) {
        this.topic = topic;
    }

}
