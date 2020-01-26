package com.application.server;

import com.data.Data;
import com.network.Network;

import java.net.SocketTimeoutException;

public class ServerRead extends Thread{
    private Network socket;
    public ServerRead(Network socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()){
            try {
                Data receivedData =(Data) socket.receive(0);
                System.out.println(receivedData.getMessage());
            } catch (SocketTimeoutException e) {
                System.out.println("Connection Closed");
            }
        }
    }
}
