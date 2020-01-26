package com.network;

import com.data.Data;
import com.data.Header;
import com.data.Utils;
import com.security.AES;
import com.security.RSA;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.logging.Logger;

public class NetworkUtils {
//    static Logger logger = Logger.getLogger(NetworkUtils.class.getName());
    private Network network;
    private RSA rsa;
    private boolean isConnection;
    private String aesKey = null;
    private Data lastSendObject;

    public NetworkUtils(Network network) {
        this.network = network;
        this.rsa = new RSA();
    }

    public Object processResponsePacket(DatagramPacket packet, byte[] buf, Network network) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
        ObjectInputStream is = null;
        Object obj = null;
        try {
            is = new ObjectInputStream(new BufferedInputStream(byteArrayInputStream));
            obj = is.readObject();

            if (checkReceivedData((Data) obj) == null) {
                return obj;
            }
            if (!isConnection) {
                handleConnection((Data) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void handleConnection(Data data) throws Exception {
        String message = data.getMessage();
        Data response = null;
        Header responseHeader = new Header();

        if (message.equals("Connection Request")) {
            responseHeader.setRsaPublicKey(rsa.getPublicKeyAsString());
            response = new Data("Approved", responseHeader);
            network.send(response, 1);
        }
        if (message.equals("Approved")) {
            /*Applied For Client*/
            rsa.setPublicKey(data.getHeader().getRsaPublicKey());
            String check_server = "Sign:" + RSA.encrypt("Check Server", rsa.getPublicKeyAsString());
            network.send(check_server, 1);

        }
        if (message.startsWith("Sign:")) {
            String messageToBeSigned = message.split(":")[1];
            messageToBeSigned = rsa.decrypt(messageToBeSigned);
            messageToBeSigned = "Verify:" + rsa.sign(messageToBeSigned);
            network.send(messageToBeSigned, 1);

        }
        if (message.startsWith("Verify:")) {
            /*Applied For Client*/
            String signature = message.split(":")[1];
            boolean isValid = RSA.verify("Check Server", signature, rsa.getPublicKeyAsString());
            if (isValid) {
                this.aesKey = "Secure64BitKey";
                responseHeader = new Header();
                data = new Data("Secret", responseHeader);
                data.getHeader().setSecretKey(RSA.encrypt(aesKey, rsa.getPublicKeyAsString()));
                network.send(data);
                isConnection = true;
                System.out.println("Secure Connection was established ...");
            } else {
                System.out.println("Connection can not be established ...");
            }

        }
        if (message.startsWith("Secret")) {
            aesKey = data.getHeader().getSecretKey();
            aesKey = rsa.decrypt(aesKey);
            isConnection = true;
            System.out.println("Client Connection established");

        }

    }

    public Data secureData(Data data) {
        /*save last sent data*/
        try {
            lastSendObject = (Data) data.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if (!isConnection) {
            return data;
        }
        data.setMessage(AES.encrypt(data.getMessage(), aesKey));
        long checksum = Utils.getCheckSum(data);
        if (data.getHeader() != null) {
            data.getHeader().setCheckSum(checksum);
        } else {
            data.setHeader(new Header(checksum));
        }

        return data;
    }

    public Data checkReceivedData(Data data) {

        if (isConnection) {
            String message = AES.decrypt(data.getMessage(), aesKey);
            if (handleRetransmission(message)) {
                data.setMessage(message);
                return data;
            }
            if (!Utils.isDataCorrupted(data)) {
                data.setMessage(message);
            } else {
//                logger.info("Data is corrupted");
                network.send(new Data("Retransmission"), 1);
                return null;
            }
        }
        return data;
    }

    private boolean handleRetransmission(String message) {
        if (message != null && message.equals("Retransmission") ) {
            network.send(lastSendObject);
            return true;
        }
        return false;
    }

}
