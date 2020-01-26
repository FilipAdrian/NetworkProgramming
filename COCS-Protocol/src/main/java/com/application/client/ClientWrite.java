package com.application.client;

import com.data.Data;
import com.network.Network;

import java.util.Scanner;

public class ClientWrite extends Thread {
    private Network socket;

    public ClientWrite(Network socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner console = new Scanner(System.in);
        System.out.println("Enter Nickname");
        String user = console.nextLine();
        String input;
        while (!(input = console.nextLine()).equals("close")) {
            socket.send(user + " : " + input);
        }
        socket.broadcast(new Data(user + ": Closed Connection"));
        socket.close();

    }
}
