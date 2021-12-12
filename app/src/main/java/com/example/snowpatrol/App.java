package com.example.snowpatrol;

import android.app.Application;

import com.example.snowpatrol.GameDB.LocationService;
import com.example.snowpatrol.GameDB.MSPV3;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MSPV3.initHelper(this);
        LocationService.initHelper(this);
    }
}
