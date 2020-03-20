package com.example.wah;

public class ProfilePosts2 {
    //private int image;
    private int stopNum;
    private String destination_name, transport , stay, price;
    //private static final String label = " day(s) trip from ";

    public ProfilePosts2( int stopNum, String destination_name, String transport, String stay, String price) {
        this.destination_name = destination_name;
        this.transport = transport;
        this.stay = stay;
        this.price = price;
        this.stopNum= stopNum;

    }

    public String getDestination_name() {
        return destination_name;
    }

    public String getTransport() {
        return transport;
    }

    public String getStay() {
        return stay;
    }

    public String getPrice() {
        return price;
    }

    public void setDestination(String destination_name) {
        this.destination_name = destination_name;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public void setStay(String stay) {
        this.stay = stay;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStopNum(int stopNum) {
        this.stopNum = stopNum;
    }

    public int getStopNum() {
        return stopNum;
    }
}
