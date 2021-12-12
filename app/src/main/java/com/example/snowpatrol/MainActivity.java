package com.example.snowpatrol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.snowpatrol.GameDB.LocationService;
import com.example.snowpatrol.GameDB.MSPV3;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView panel_BTN_right;
    private TextView panel_LBL_score;
    private ImageView panel_BTN_left;
    private ImageView[] panel_IMG_lives;
    private RelativeLayout upperLayout;
    private RelativeLayout layoutBottom;
    private LinearLayout panel_LL_lanes;
    private ImageView panel_IMG_l1snow;
    private ImageView panel_IMG_l2snow;
    private ImageView panel_IMG_l3snow;
    private ImageView panel_IMG_l4snow;
    private ImageView panel_IMG_l5snow;
    private boolean resume = false;
    private ImageView[][] mat;
    private ImageView[] Lane1;
    private ImageView[] Lane2;
    private ImageView[] Lane3;
    private ImageView[] Lane4;
    private ImageView[] Lane5;
    private final int min = 0;
    private boolean generate = true;
    private final int max = 4;
    private int DELTA_TIME = 500;//[ms]
    private int numOfLives = 3;
    private int randomLane;
    private Coordinates[] onScreen = new Coordinates[4];
    private int onScreenIndex = 0;
    private int score = 0;
    private String imgSrc;
    private Coordinates c;
    private Bundle bundle;
    Context context = this;
    private Handler time = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            //generate new obstacle
            if (generate) {
                randomLane = new Random().nextInt((max - min) + 1) + min;
                int cocoaOrFlame = new Random().nextInt((1-0) + 1) + 0;
                if (cocoaOrFlame == 0) { //fire
                    //stay with fire img source
                    imgSrc = FIRE_IMG_SRC;
                    mat[randomLane][0].setImageResource(R.drawable.flame);
                } else if (cocoaOrFlame == 1) {
                    //change image src to hot cocoa
                    imgSrc = COCOA_IMG_SRC;
                    mat[randomLane][0].setImageResource(R.drawable.hotchocolate);
                }
                mat[randomLane][0].setVisibility(View.VISIBLE); //first of random
                c = new Coordinates(randomLane, 0, imgSrc);
                onScreen[onScreenIndex] = c;
                if (onScreenIndex == 3) {
                    onScreenIndex = 0;
                    onScreen[onScreenIndex].setRandomLane(randomLane);
                    onScreen[onScreenIndex].setObsNumm(0);
                } else {
                    onScreen[onScreenIndex].setRandomLane(randomLane);
                    onScreen[onScreenIndex].setObsNumm(0);
                    onScreen[onScreenIndex].setImgSrc(imgSrc);
                    c = onScreen[onScreenIndex];
                }
                if (cocoaOrFlame == 0) { //fire
                    //stay with fire img source
                    imgSrc = FIRE_IMG_SRC;
                    mat[c.getRandomLane()][c.getObsNumm()].setImageResource(R.drawable.flame);
                } else if (cocoaOrFlame == 1) {
                    //do same but change image src to hot cocoa
                    imgSrc = COCOA_IMG_SRC;
                    mat[c.getRandomLane()][c.getObsNumm()].setImageResource(R.drawable.hotchocolate);
                }
                mat[c.getRandomLane()][c.getObsNumm()].setVisibility(View.VISIBLE);
                onScreenIndex += 1;
                generate = false;
            } else {
                generate = true;
            }
            for (int i = 0; i < 3; i++) {
                int obsNum = 0;
                if (onScreen[i] != null) {
                    if (onScreenIndex - 1 != i || generate) {
                        obsNum = onScreen[i].getObsNumm();
                        if (onScreen[i].getImgSrc().equals(FIRE_IMG_SRC)) {
                            mat[onScreen[i].getRandomLane()][obsNum].setImageResource(R.drawable.flame);
                        } else if (onScreen[i].getImgSrc().equals(COCOA_IMG_SRC)) {
                            mat[onScreen[i].getRandomLane()][obsNum].setImageResource(R.drawable.hotchocolate);
                        }
                        mat[onScreen[i].getRandomLane()][obsNum].setVisibility(View.INVISIBLE);
                        onScreen[i].addOneToRow();
                        checkTouch(i);
                        checkScoreForSpeed();
                    }
                    if (onScreen[i].getObsNumm() < 5) {
                        if (onScreen[i].getImgSrc() == COCOA_IMG_SRC) {
                            mat[onScreen[i].getRandomLane()][onScreen[i].getObsNumm()].setImageResource(R.drawable.hotchocolate);
                        } else if (onScreen[i].getImgSrc() == FIRE_IMG_SRC) {
                            mat[onScreen[i].getRandomLane()][onScreen[i].getObsNumm()].setImageResource(R.drawable.flame);
                        }
                        mat[onScreen[i].getRandomLane()][onScreen[i].getObsNumm()].setVisibility(View.VISIBLE);
                    }
                }
            }
            score += 5;
            panel_LBL_score.setText("" + score);
            if (resume) {
                time.postDelayed(run, DELTA_TIME);
            }
        }
    };

    public void checkScoreForSpeed(){
        if(score%1000 == 0){
            DELTA_TIME -= 30;
        }
    }

    public void checkTouch(int i){
        if (onScreen[i].getRandomLane() == playerPosition + 1 && onScreen[i].getObsNumm() == 5 && onScreen[i].getImgSrc().equals(FIRE_IMG_SRC)) {
            numOfLives -= 1;
            ouch();
            vibrate(300);
            updateLivesViews();
        } else if (onScreen[i].getRandomLane() == playerPosition + 1 && onScreen[i].getObsNumm() == 5 && onScreen[i].getImgSrc() == COCOA_IMG_SRC) {
            cocoa();
            score += 50;
            panel_LBL_score.setText("" + score);
        }
    }
    //would have put it as enum but it takes way more memory
    private static final int LANE1 = -1;
    private static final int LANE2 = 0;
    private static final int LANE3 = 1;
    private static final int LANE4 = 2;
    private static final int LANE5 = 3;
    private static final int MODE_STANDARD = 0;
    private static final int MODE_SENSOR = 1;
    private final String FIRE_IMG_SRC = "fire";
    private final String COCOA_IMG_SRC = "cocoa";
    private int playerPosition = 1;
    private boolean firstTime;

    private StartMenuFrag menuFrag;
    private FrameLayout menuFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestLocPermissions();
        MSPV3.initHelper(this);

        LocationService.initHelper(this);

        if (getIntent().getBundleExtra("BUNDLE") != null){
            this.bundle = getIntent().getBundleExtra("BUNDLE");
        } else {
            this.bundle = new Bundle();
        }
        findViews();

        menuFrag = new StartMenuFrag();
        menuFrag.setActivity(this);
        menuFrag.setCallBackMenu(menuCallback);
        getSupportFragmentManager().beginTransaction().add(R.id.frameStart, menuFrag).commit();

        panel_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                if (playerPosition == LANE1) {//player on the left
                    panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l2snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE2;
                } else if (playerPosition == LANE2) {//mid to right
                    panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l3snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE3;
                } else if (playerPosition == LANE3) {
                    panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l4snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE4;
                } else if (playerPosition == LANE4) {
                    panel_IMG_l4snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l5snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE5;
                } else if (playerPosition == LANE5) {
                    //do nothing
                    playerPosition = LANE5;

                }
            }
        });

        panel_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                if (playerPosition == LANE1) {//player on the left
                    playerPosition = LANE1;
                    //do nothing
                } else if (playerPosition == LANE2) {
                    panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l1snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE1;
                } else if (playerPosition == LANE3) {
                    panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l2snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE2;
                } else if (playerPosition == LANE4) {
                    panel_IMG_l4snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l3snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE3;
                } else if (playerPosition == LANE5) {
                    panel_IMG_l5snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l4snow.setVisibility(View.VISIBLE);
                    playerPosition = LANE4;
                }
            }
        });
    }

    MenuCallback menuCallback = new MenuCallback() {
        @Override
        public void buttonMode() {
            upperLayout.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.VISIBLE);
            panel_LL_lanes.setVisibility(View.VISIBLE);
            menuFrame.setVisibility(View.GONE);
            sensOrButt(MODE_STANDARD);
            choose = true;
            onStart();
        }
        @Override
        public void sensorMode() {
            sensOrButt(MODE_SENSOR);
            upperLayout.setVisibility(View.VISIBLE);
            panel_LL_lanes.setVisibility(View.VISIBLE);
            menuFrame.setVisibility(View.GONE);
            choose = true;
            onResume(); // start sensors
            onStart();
        }
        @Override
        public void TopTen() {
            Intent topTenIntent = new Intent(context, TopTenActivity.class);
            startActivity(topTenIntent);
        }
    };
    //ask for location permission
    private void requestLocPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    boolean choose = false;

    @Override
    protected void onStart() {
        super.onStart();
        if(choose){
            time.postDelayed(run, DELTA_TIME);
        }
        resume = true;
        firstTime = true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        resume = false;
        time.removeCallbacks(run);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (gameMode == MODE_SENSOR) {
            sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (gameMode == MODE_SENSOR) {
            sensorManager.unregisterListener(accSensorEventListener);
        }
    }
    @Override
    public void finish() {
        resume = false;
        time.removeCallbacks(run);
        panel_LL_lanes.setVisibility(View.INVISIBLE);
        layoutBottom.setVisibility(View.INVISIBLE);
        panel_IMG_lives[0].setVisibility(View.INVISIBLE);
        panel_IMG_lives[1].setVisibility(View.INVISIBLE);
        panel_IMG_lives[2].setVisibility(View.INVISIBLE);
        if(!onBack) {
            Intent myIntent = new Intent(this, EndGameActivity.class);
            bundle.putInt("SCORE", score);
            myIntent.putExtra("BUNDLE", bundle);
            startActivity(myIntent);
        }
    }
    boolean onBack = false;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack =true;
        Intent intent=new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        this.finish();

    }

    private int gameMode;
    private SensorManager sensorManager;
    private Sensor accSensor;

    public boolean isSensorExist(int sensorType) {
        return (sensorManager.getDefaultSensor(sensorType) != null);
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    private float curX;
    private float curT;
    private float cutZ;
    private final SensorEventListener accSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (firstTime) {
                curX = event.values[0];
                curT = event.values[1];
                cutZ = event.values[2];
                firstTime = false;
            }
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            Log.i("ab", "" + x);
            if (x > 5 && x <= 9) {//the position is LANE1
                panel_IMG_l1snow.setVisibility(View.VISIBLE);
                panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                panel_IMG_l4snow.setVisibility(View.INVISIBLE);
                panel_IMG_l5snow.setVisibility(View.INVISIBLE);
                playerPosition=LANE1;
            }
            if (x >= 1 && x <= 5) {//the position is LANE2
                //moveTheSnowman(LANE2);
                panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                panel_IMG_l2snow.setVisibility(View.VISIBLE);
                panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                panel_IMG_l4snow.setVisibility(View.INVISIBLE);
                panel_IMG_l5snow.setVisibility(View.INVISIBLE);
                playerPosition=LANE2;
            }
            if (x >= -1 && x <= 1) {//the position is LANE3
                panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                panel_IMG_l3snow.setVisibility(View.VISIBLE);
                panel_IMG_l4snow.setVisibility(View.INVISIBLE);
                panel_IMG_l5snow.setVisibility(View.INVISIBLE);
                playerPosition=LANE3;
            }
            if (x <= -1 && x >= -5) {//the position is LANE4
                panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                panel_IMG_l4snow.setVisibility(View.VISIBLE);
                panel_IMG_l5snow.setVisibility(View.INVISIBLE);
                playerPosition=LANE4;
            }
            if (x < -5 && x >= -9) {//the position is LANE5
                panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                panel_IMG_l4snow.setVisibility(View.INVISIBLE);
                panel_IMG_l5snow.setVisibility(View.VISIBLE);
                playerPosition=LANE5;
            }
        }
    };

    public void sensOrButt(int gameMode) {
        this.gameMode = gameMode;
        if (gameMode == MODE_STANDARD) {//press
            panel_BTN_right.setVisibility(View.VISIBLE);
            panel_BTN_left.setVisibility(View.VISIBLE);
            //panel_below_layout.setVisibility(View.VISIBLE);
        }
        if (gameMode == MODE_SENSOR) {//sensor
            panel_BTN_right.setVisibility(View.INVISIBLE);
            panel_BTN_left.setVisibility(View.INVISIBLE);
            initSensor();
        }
    }
    private void updateLivesViews() {
        if (numOfLives <= 0) {
            endGame();
        } else
            panel_IMG_lives[numOfLives].setVisibility(View.INVISIBLE);
    }
    public void endGame(){
        if(numOfLives==0){
            finish();
        }
    }
    //SFX
    private void ouch() {
        final MediaPlayer song = MediaPlayer.create(this, R.raw.ouchsound);
        song.start();
    }
    //SFX
    private void cocoa() {
        final MediaPlayer song = MediaPlayer.create(this, R.raw.coffee);
        song.start();
    }
    //SFX
    private void vibrate(int millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(millisecond);
        }
    }

    private void findViews() {
        //right and left buttons
        menuFrame = findViewById(R.id.frameStart);
        panel_BTN_right = findViewById(R.id.panel_BTN_right);
        panel_BTN_left = findViewById(R.id.panel_BTN_left);
        panel_BTN_right.setClickable(true);
        panel_BTN_left.setClickable(true);
        upperLayout = findViewById(R.id.upperLayout);
        layoutBottom = findViewById(R.id.layoutBottom);
        panel_LL_lanes = findViewById(R.id.panel_LL_lanes);
        //lives
        panel_IMG_lives = new ImageView[]{
                findViewById(R.id.panel_IMG_sf1), findViewById(R.id.panel_IMG_sf2), findViewById(R.id.panel_IMG_sf3),
        };
        //all lanes
        Lane1 = new ImageView[]{
                findViewById(R.id.panel_IMG_l1fire1), findViewById(R.id.panel_IMG_l1fire2), findViewById(R.id.panel_IMG_l1fire3), findViewById(R.id.panel_IMG_l1fire4),
                findViewById(R.id.panel_IMG_l1fire5)
        };
        Lane2 = new ImageView[]{
                findViewById(R.id.panel_IMG_l2fire1), findViewById(R.id.panel_IMG_l2fire2), findViewById(R.id.panel_IMG_l2fire3), findViewById(R.id.panel_IMG_l2fire4),
                findViewById(R.id.panel_IMG_l2fire5)
        };
        Lane3 = new ImageView[]{
                findViewById(R.id.panel_IMG_l3fire1), findViewById(R.id.panel_IMG_l3fire2), findViewById(R.id.panel_IMG_l3fire3), findViewById(R.id.panel_IMG_l3fire4),
                findViewById(R.id.panel_IMG_l3fire5)
        };
        Lane4 = new ImageView[]{
                findViewById(R.id.panel_IMG_l4fire1), findViewById(R.id.panel_IMG_l4fire2), findViewById(R.id.panel_IMG_l4fire3), findViewById(R.id.panel_IMG_l4fire4),
                findViewById(R.id.panel_IMG_l4fire5)
        };
        Lane5 = new ImageView[]{
                findViewById(R.id.panel_IMG_l5fire1), findViewById(R.id.panel_IMG_l5fire2), findViewById(R.id.panel_IMG_l5fire3), findViewById(R.id.panel_IMG_l5fire4),
                findViewById(R.id.panel_IMG_l5fire5)
        };
        //matrix
        mat = new ImageView[][]{
                Lane1, Lane2, Lane3, Lane4, Lane5
        };
        //player of each lane
        panel_IMG_l1snow = findViewById(R.id.panel_IMG_l1snow);
        panel_IMG_l2snow = findViewById(R.id.panel_IMG_l2snow);
        panel_IMG_l3snow = findViewById(R.id.panel_IMG_l3snow);
        panel_IMG_l4snow = findViewById(R.id.panel_IMG_l4snow);
        panel_IMG_l5snow = findViewById(R.id.panel_IMG_l5snow);
        panel_LBL_score = findViewById(R.id.panel_LBL_score);
    }
}