package com.data;

import java.io.Serializable;

public class Data implements Serializable, Cloneable {
    private String message;
    private Header header;

    public Data(String message) {
        this.message = message;
    }

    public Data(String message, Header header) {
        this.message = message;
        this.header = header;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "Data{" +
                "message='" + message + '\'' +
                ", header=" + header +
                '}';
    }
}
