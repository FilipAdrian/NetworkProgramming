package com.application.server;

import com.data.Data;
import com.network.Network;

import java.net.SocketTimeoutException;
import java.util.logging.Logger;

public class CocsServer {
    static Logger logger = Logger.getLogger(CocsServer.class.getName());
    private Network socket;

    public CocsServer(String host, int port) {
        logger.info("Server Init");
        this.socket = new Network(host, port);
    }

    public void run() {
        new ServerRead(socket).start();
        new ServerWrite(socket).start();
    }
}
