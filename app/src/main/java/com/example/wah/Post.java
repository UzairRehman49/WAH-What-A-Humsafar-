package com.example.wah;

import java.util.ArrayList;

public class Post {

    private String source;
    private  Stop finalDest;
    private int days;
    private int date;
    private  ArrayList<Stop> allstops= new ArrayList();

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Post(String source, Stop finalDest, ArrayList<Stop> allstops, int days, int date) {
        this.source = source;
        this.finalDest = finalDest;
        this.allstops = allstops;
        this.days = days;
        this.date = date;
    }


    public String getSource() {
        return source;
    }

    public Stop getFinalDest() {
        return finalDest;
    }

    public ArrayList<Stop> getAllstops() {
        return allstops;
    }


    public void setSource(String source) {
        this.source = source;
    }

    public void setFinalDest(Stop finalDest) {
        this.finalDest = finalDest;
    }

    public void setAllstops(ArrayList<Stop> allstops) {
        this.allstops = allstops;
    }
}
