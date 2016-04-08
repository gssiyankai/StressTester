package com.gregory.testing.application;

import com.gregory.testing.communication.InputChannel;
import com.gregory.testing.communication.OutputChannel;

public final class Server {

    private final String name;
    private final InputChannel input;
    private final OutputChannel output;

    public Server(String name, InputChannel input, OutputChannel output) {
        this.name = name;
        this.input = input;
        this.output = output;
    }

    public String name() {
        return name;
    }

    public InputChannel input() {
        return input;
    }

    public OutputChannel output() {
        return output;
    }

}
