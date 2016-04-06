package com.gregory.testing.application;

import com.gregory.testing.communication.Channel;

public class Server implements System {

    private final String name;
    private final Channel input;
    private final Channel output;

    public Server(String name, Channel input, Channel output) {
        this.name = name;
        this.input = input;
        this.output = output;
    }

}
