package com.example.snowpatrol.GameDB;

import java.util.ArrayList;

public class MyDB {
    private ArrayList<TopTenComponent> topTenComponents = new ArrayList<>();

    public MyDB() {
    }

    public ArrayList<TopTenComponent> getTopTenComponents() {

        return topTenComponents;
    }

    public MyDB setTopTenComponents(ArrayList<TopTenComponent> topTenComponents) {
        this.topTenComponents = topTenComponents;
        return this;
    }
}
