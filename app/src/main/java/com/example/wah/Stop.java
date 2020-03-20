package com.example.wah;

public class Stop {

    private String name;
    private String meansOfTransport;
    private String stayPlace;
    private int stayCost;
    private int transportCost;

    public Stop(String name, String meansOfTransport, String stayPlace, int stayCost, int transportCost) {
        this.name = name;
        this.meansOfTransport = meansOfTransport;
        this.stayPlace = stayPlace;
        this.stayCost = stayCost;
        this.transportCost = transportCost;
    }


    public String getName() {
        return name;
    }

    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    public String getStayPlace() {
        return stayPlace;
    }

    public int getStayCost() {
        return stayCost;
    }

    public int getTransportCost() {
        return transportCost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMeansOfTransport(String meansOfTransport) {
        this.meansOfTransport = meansOfTransport;
    }

    public void setStayPlace(String stayPlace) {
        this.stayPlace = stayPlace;
    }

    public void setStayCost(int stayCost) {
        this.stayCost = stayCost;
    }

    public void setTransportCost(int transportCost) {
        this.transportCost = transportCost;
    }
}
