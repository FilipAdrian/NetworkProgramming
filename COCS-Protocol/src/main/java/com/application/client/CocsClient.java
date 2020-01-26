package com.application.client;

import com.network.Network;

public class CocsClient {
    private Network socket;

    public CocsClient(String host, int port) {
        this.socket = new Network(host, port);
    }

    public void execute() {
        socket.connect("localhost", 6666, 3000);
        new ClientWrite(socket).start();
        new ClientReader(socket).start();

    }
}
