package com.example.snowpatrol;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends Fragment {

    private MapView myMap;
    private GoogleMap googleMap;
    private AppCompatActivity activity;

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);
        myMap.onCreate(savedInstanceState);
        myMap.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        myMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        myMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        myMap.onLowMemory();
    }

    public void setLocation(String name, String score, double lat, double lon) {
        googleMap.clear();
        LatLng location = new LatLng(lat, lon);
        score = "score: " + score;
        googleMap.addMarker(new MarkerOptions().position(location).title(name).snippet(score));
        //zoom
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void findViews(View view) {
        myMap = view.findViewById(R.id.my_map);
    }
}
