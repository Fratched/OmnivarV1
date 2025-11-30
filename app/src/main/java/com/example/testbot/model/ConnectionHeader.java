package com.example.testbot.model;

public class ConnectionHeader {

    private String from;
    private String to;

    public ConnectionHeader(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
}
