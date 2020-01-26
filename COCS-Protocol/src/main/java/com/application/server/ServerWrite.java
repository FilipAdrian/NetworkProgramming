package com.application.server;

import com.data.Data;
import com.network.Network;

import java.util.Scanner;

public class ServerWrite extends Thread{
    private Network socket;
    public ServerWrite(Network socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String input =scanner.nextLine();
            socket.broadcast(new Data("Server :" + input));
            if (input.equals("close")){

                break;
            }
        }
        socket.broadcast(new Data("Server Closed Connection"));
        socket.close();

    }
}
