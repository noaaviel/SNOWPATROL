package com.example.snowpatrol;

public class Coordinates {

    private int randomLane;
    private int obsNumm;

    public Coordinates(){

    }

    public Coordinates(int cori,int corj){
        this.randomLane =cori;
        this.obsNumm =corj;
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

    public void setRandomLane(int randomLane) {
        this.randomLane = randomLane;
    }

    public void addOneToRow(){
        this.obsNumm +=1;
    }
}
