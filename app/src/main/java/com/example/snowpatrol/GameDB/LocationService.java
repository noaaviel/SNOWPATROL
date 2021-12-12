package com.example.snowpatrol.GameDB;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.example.snowpatrol.CallbacksTopTen.CallbackMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class LocationService {
    private static LocationService location;
    Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public static LocationService getLocation() {
        return location;
    }

    private LocationService(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static void initHelper(Context context) {
        if (location == null) {
            location = new LocationService(context);
        }
    }

    private boolean checkPermission(){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    public void getLocation(CallbackMap callbackMap){
        if (checkPermission()) {
            fusedLocationClient.getLastLocation().addOnCompleteListener( task->{
                Location location = task.getResult();
                if(location!=null){
                    callbackMap.showLocation(location.getLatitude(), location.getLongitude());
                }
                else{
                    callbackMap.showLocation(0.0D, 0.0D);
                }
            });
        }
        else {
            callbackMap.showLocation(0.0D, 0.0D);
        }
    }
}