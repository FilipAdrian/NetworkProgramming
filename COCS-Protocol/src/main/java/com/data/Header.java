package com.data;

import java.io.Serializable;

public class Header implements Serializable {
    private String rsaPublicKey;
    private String secretKey;
    private long checkSum;
    private Integer packetNumber;

    public Header() {
    }

    public Header(long checkSum) {
        this.checkSum = checkSum;
    }

    public Header(long checkSum, Integer packetNumber) {
        this.checkSum = checkSum;
        this.packetNumber = packetNumber;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(long checkSum) {
        this.checkSum = checkSum;
    }

    public Integer getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(Integer packetNumber) {
        this.packetNumber = packetNumber;
    }

    @Override
    public String toString() {
        return "Header{" +
                "rsaPublicKey='" + rsaPublicKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", checkSum='" + checkSum + '\'' +
                ", packetNumber=" + packetNumber +
                '}';
    }
}
