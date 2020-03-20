package com.example.wah;

public class ProfilePosts {
    private int image;
    private String destination, days, source, date;
    private static final String label = " day(s) trip from ";

    public ProfilePosts(int image, String destination, String days, String source, String date) {
        this.image = image;
        this.destination = destination;
        this.days = days;
        this.source = source;
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public String getDestination() {
        return destination;
    }

    public String getDays() {
        return days;
    }

    public String getSource() {
        return source;
    }

    public String getDate() {
        return date;
    }

    public  String getLabel() {
        return label;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
