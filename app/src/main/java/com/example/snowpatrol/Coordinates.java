package com.example.snowpatrol;

public class Coordinates {

    private int randomLane;
    private int obsNumm;
    private String imgSrc;
    public Coordinates(){

    }

    public Coordinates(int cori,int corj,String imgSrc){
        this.randomLane =cori;
        this.obsNumm =corj;
        this.imgSrc=imgSrc;
    }

    public int getObsNumm() {
        return obsNumm;
    }

    public void setObsNumm(int obsNumm) {
        this.obsNumm = obsNumm;
    }

    public int getRandomLane() {
        return randomLane;
    }

    public String getImgSrc() {
        return this.imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setRandomLane(int randomLane) {

        this.randomLane = randomLane;
    }

    public void addOneToRow(){

        this.obsNumm +=1;
    }
}
