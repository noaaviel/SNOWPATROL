package com.example.snowpatrol;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snowpatrol.CallbacksTopTen.CallbackList;
import com.example.snowpatrol.GameDB.AdminDB;
import com.example.snowpatrol.GameDB.LocationService;
import com.example.snowpatrol.GameDB.MyDB;
import com.example.snowpatrol.GameDB.TopTenComponent;

public class TopTenActivity extends AppCompatActivity {

    private Bundle bundle;
    private MyDB myDB;
    private FragmentList fragmentList;
    private FragmentMap fragmentMap;
    //private Button menu = findViewById(R.id.topten_BTN_menu);

    CallbackList cbList = (i -> {
        TopTenComponent ttc = myDB.getTopTenComponents().get(i);
        String playerName = ttc.getName();
        double lat = ttc.getLat();
        double lon = ttc.getLon();
        String score = ""+ttc.getScore();
        fragmentMap.setLocation(playerName, score, lat, lon);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_ten_activity);
        this.bundle = getIntent().getBundleExtra("BUNDLE");
        fragmentList = new FragmentList();
        fragmentList.setActivity(this);
        fragmentList.setCallbackList(cbList);
        updateDB();
        /*menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        fragmentMap = new FragmentMap();
        fragmentMap.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_map, fragmentMap).commit();

    }

    private void updateDB() {
        if (getIntent().getBundleExtra("BUNDLE") != null){
            this.bundle = getIntent().getBundleExtra("BUNDLE");
        } else {
            this.bundle = new Bundle();
        }
        String playerName=bundle.getString("PLAYER NAME","");
        int score = bundle.getInt("SCORE",0);

        myDB = AdminDB.getRecordDBFromSP();

        LocationService.getLocation().getLocation((lat, lon) -> {
            updateLastRecord(lat, lon, score,playerName );
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.setClass(this,MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }

    private void updateLastRecord(double lat, double lon,int score,String playerName){

        TopTenComponent ttc = new TopTenComponent();
        ttc.setName(playerName);
        ttc.setScore(score);
        ttc.setLat(lat);
        ttc.setLon(lon);
        AdminDB.updateRecord(myDB,ttc);
        updateList();

    }

    private void updateList(){
        fragmentList.setRecords(myDB.getTopTenComponents());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_list, fragmentList).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
