package com.application;


import com.application.server.CocsServer;

public class MainServer {
    public static void main(String[] args) {
        CocsServer server = new CocsServer("localhost", 6666);
        server.run();
    }
}
