package com.example.snowpatrol.GameDB;

public class TopTenComponent {
    private String name="";
    private int score = 0;
    private double lat = 0.0;
    private double lon = 0.0;

    public TopTenComponent(){

    }

    public String getName() {

        return name;
    }

    public TopTenComponent setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public TopTenComponent setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public TopTenComponent setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public TopTenComponent setLon(double lon) {
        this.lon = lon;
        return this;
    }
}
