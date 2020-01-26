package com.application.client;

import com.data.Data;
import com.network.Network;

import java.net.SocketTimeoutException;

public class ClientReader extends Thread {
    private Network socket;

    public ClientReader(Network socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Data response = (Data) socket.receive(0);
                if (socket.isClosed()){
                    break;
                }
                System.out.println(response.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
