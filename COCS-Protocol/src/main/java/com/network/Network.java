package com.network;

import com.data.Data;
import com.data.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Network {
//    Logger logger = Logger.getLogger(Network.class.getName());
    private DatagramSocket socket;
    private String destinationHost;
    private int destinationPort;
    private NetworkUtils networkUtils;
    private int timeOut = 3000;
    private Set<String> clientList = new HashSet<>();


    public Network(String host, int port) {
        socket = init(host, port);

    }

    private DatagramSocket init(String host, int port) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port, InetAddress.getByName(host));
            networkUtils = new NetworkUtils(this);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        return socket;

    }

    public void connect(String serverHost, int serverPort, int timeOut) {
        Data data = new Data("Connection Request");
        send(data, serverHost, serverPort);
        try {
            receive(timeOut);
        } catch (SocketTimeoutException e) {
            System.out.println("Error: Connection TimeOut");
            System.exit(1);
        }
//        logger.info("Connection Established");


    }

    public void send(String message, int flag) {
        send(message);
        if (flag == 1) {
            try {
                receive(timeOut);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Serializable obj, int flag) {
        send(obj);
        if (flag == 1) {
            try {
                receive(timeOut);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String message) {
        send(new Data(message), destinationHost, destinationPort);
    }

    public void send(Serializable obj) {
        send(obj, destinationHost, destinationPort);
    }

    public void broadcast(Serializable obj) {
        for (String client : clientList) {
            send(obj, client.split(":")[0], Integer.parseInt(client.split(":")[1]));
        }
    }

    private void send(Serializable obj, String host, int port) {
//        logger.info((obj).toString());
        networkUtils.secureData((Data) obj);
        try {
            byte[] buf = Utils.dataToByteArray(obj);
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        } catch (SocketTimeoutException e) {
            System.out.println("TimeOut error Send");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Object receive(int timeOut) throws SocketTimeoutException {
        try {
            socket.setSoTimeout(timeOut);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return receive();
    }

    public Object receive() throws SocketTimeoutException {
        byte[] buf = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        Object obj = null;
        try {
            if (socket.isClosed()){
                return null;
            }
            socket.receive(packet);
            this.destinationHost = packet.getAddress().getHostAddress();
            this.destinationPort = packet.getPort();
            /*store all clients*/
            clientList.add(destinationHost + ":" + destinationPort);

            obj = networkUtils.processResponsePacket(packet, buf, this);
        } catch (IOException e) {
            if (e.getClass().getName().equals("java.net.SocketTimeoutException")) {
                throw new SocketTimeoutException("Error: Socket Timeout");
            }

            System.out.println("Connection Closed");
        }
//        logger.info("Received: " + obj.toString());

        return obj;

    }


    public void close()
    {
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }


}
