package com.application;

import com.application.client.CocsClient;

public class MainClient {
    public static void main(String[] args) {
        CocsClient client = new CocsClient("localhost", 6668);
        client.execute();
    }
}
