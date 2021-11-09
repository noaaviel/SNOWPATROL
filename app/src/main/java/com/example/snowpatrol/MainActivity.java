package com.example.snowpatrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //android:background="#8C0000FF" layout1 and layout3
    // android:background="#8C00FFFF" layout2
    //  android:divider="?android:listDivider"
    //        android:dividerPadding="20dp"
    //android:showDividers="middle"

    private final int MAX_LIVES = 3;
    private ImageView panel_BTN_right;
    private ImageView panel_BTN_left;
    private final int NUM_OF_OBS = 4;
    private ImageView[] panel_IMG_lives;
    Button panel_BTN_tryAgain;
    private ImageView panel_IMG_l1snow;
    private ImageView panel_IMG_l2snow;
    private ImageView panel_IMG_l3snow;
    private RelativeLayout upperLayout;
    boolean resume = false;
    ImageView[][] mat;
    private ImageView[] Lane1;
    private ImageView[] Lane2;
    private ImageView[] Lane3;
    int curr = 0;
    final int min = 0;
    boolean generate = true;
    final int max = 2;
    int deltaTime = 500;//[ms]
    int numOfLives = 3;
    int numOfObs = NUM_OF_OBS;
    int randomLane;
    Coordinates[] onScreen = new Coordinates[4];
    int onScreenIndex = 0;
    Handler time = new Handler();
    Coordinates c;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            //generate new obstacle
            if (generate) {
                randomLane = new Random().nextInt((max - min) + 1) + min;
                mat[randomLane][0].setVisibility(View.VISIBLE); //first of random
                c = new Coordinates(randomLane, 0);
                onScreen[onScreenIndex] = c;
                if (onScreenIndex == 3) {
                    onScreenIndex = 0;
                    onScreen[onScreenIndex].setRandomLane(randomLane);
                    onScreen[onScreenIndex].setObsNumm(0);
                } else {
                    onScreen[onScreenIndex].setRandomLane(randomLane);
                    onScreen[onScreenIndex].setObsNumm(0);
                    c = onScreen[onScreenIndex];

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
                        mat[onScreen[i].getRandomLane()][obsNum].setVisibility(View.INVISIBLE);
                        onScreen[i].addOneToRow();
                        if (onScreen[i].getRandomLane() == playerPosition + 1 && onScreen[i].getObsNumm() == 5) {
                            numOfLives -= 1;
                            ouch();
                            vibrate(400);
                            updateLivesViews();
                        }
                    }
                    if (onScreen[i].getObsNumm() < 5) {
                        mat[onScreen[i].getRandomLane()][onScreen[i].getObsNumm()].setVisibility(View.VISIBLE);
                        int log = onScreen[i].getObsNumm();
                    }
                }
            }

            if (resume) {
                time.postDelayed(run, deltaTime);
            }

        }
    };

    private int playerPosition = 0; //1 will indicate its already on the right, -1 will indicate its already on the left side 0 in the middle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //benEking();
        setContentView(R.layout.activity_main);
        findViews();
        panel_BTN_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                if (playerPosition == -1) {//player on the left
                    panel_IMG_l1snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l2snow.setVisibility(View.VISIBLE);
                    playerPosition = 0;
                } else if (playerPosition == 0) {//mid to right
                    panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l3snow.setVisibility(View.VISIBLE);
                    playerPosition = 1;
                } else if (playerPosition == 1) {
                    //do nothing
                    playerPosition = 1;
                }
            }
        });

        panel_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (playerPosition == -1) {//player on the left
                    playerPosition = -1;
                    //do nothing
                } else if (playerPosition == 0) {//mid to left
                    panel_IMG_l2snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l1snow.setVisibility(View.VISIBLE);
                    playerPosition = -1;
                } else if (playerPosition == 1) {//on right
                    panel_IMG_l3snow.setVisibility(View.INVISIBLE);
                    panel_IMG_l2snow.setVisibility(View.VISIBLE);
                    playerPosition = 0;
                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();
        time.postDelayed(run, deltaTime);
        //benEking();
        resume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        resume = false;
        time.removeCallbacks(run);
    }

    private void findViews() {

        panel_BTN_right = findViewById(R.id.panel_BTN_right);
        panel_BTN_left = findViewById(R.id.panel_BTN_left);
        panel_BTN_right.setClickable(true);
        panel_BTN_left.setClickable(true);
        upperLayout = findViewById(R.id.upperLayout);
        panel_BTN_tryAgain = findViewById(R.id.panel_BTN_tryAgain);
        panel_IMG_lives = new ImageView[]{
                findViewById(R.id.panel_IMG_sf1),
                findViewById(R.id.panel_IMG_sf2),
                findViewById(R.id.panel_IMG_sf3),
        };

        Lane1 = new ImageView[]{
                findViewById(R.id.panel_IMG_l1fire1),
                findViewById(R.id.panel_IMG_l1fire2),
                findViewById(R.id.panel_IMG_l1fire3),
                findViewById(R.id.panel_IMG_l1fire4),
                findViewById(R.id.panel_IMG_l1fire5)
        };

        Lane2 = new ImageView[]{
                findViewById(R.id.panel_IMG_l2fire1),
                findViewById(R.id.panel_IMG_l2fire2),
                findViewById(R.id.panel_IMG_l2fire3),
                findViewById(R.id.panel_IMG_l2fire4),
                findViewById(R.id.panel_IMG_l2fire5)
        };

        Lane3 = new ImageView[]{
                findViewById(R.id.panel_IMG_l3fire1),
                findViewById(R.id.panel_IMG_l3fire2),
                findViewById(R.id.panel_IMG_l3fire3),
                findViewById(R.id.panel_IMG_l3fire4),
                findViewById(R.id.panel_IMG_l3fire5)
        };
       /* mat = new ImageView[][]{
                //Lane1,Lane2,Lane3
                {findViewById(R.id.panel_IMG_l1fire1), findViewById(R.id.panel_IMG_l2fire1), findViewById(R.id.panel_IMG_l3fire1)},
                {findViewById(R.id.panel_IMG_l1fire2), findViewById(R.id.panel_IMG_l2fire2), findViewById(R.id.panel_IMG_l3fire2)},
                {findViewById(R.id.panel_IMG_l1fire3), findViewById(R.id.panel_IMG_l2fire3), findViewById(R.id.panel_IMG_l3fire3)},
                {findViewById(R.id.panel_IMG_l1fire4), findViewById(R.id.panel_IMG_l2fire4), findViewById(R.id.panel_IMG_l3fire4)},
                {findViewById(R.id.panel_IMG_l1fire5), findViewById(R.id.panel_IMG_l2fire5), findViewById(R.id.panel_IMG_l3fire5)}
        };*/
        mat = new ImageView[][]{
                Lane1,
                Lane2,
                Lane3
        };

        panel_IMG_l1snow = findViewById(R.id.panel_IMG_l1snow);
        panel_IMG_l2snow = findViewById(R.id.panel_IMG_l2snow);
        panel_IMG_l3snow = findViewById(R.id.panel_IMG_l3snow);
    }

    public void tryAgain() {
        //Toast.makeText(getPanel_BTN_tryAgain().getContext(),"New Game (:",Toast.LENGTH_SHORT).show();
        //this.panel_LBL_score.setText("0000");
        this.panel_IMG_lives[0].setVisibility(View.VISIBLE);
        this.panel_IMG_lives[1].setVisibility(View.VISIBLE);
        this.panel_IMG_lives[2].setVisibility(View.VISIBLE);
        this.panel_BTN_right.setVisibility(View.VISIBLE);
        this.panel_BTN_left.setVisibility(View.VISIBLE);
        //this.index =0;
        this.numOfLives = 3;
        this.panel_BTN_tryAgain.setVisibility(View.INVISIBLE);

    }

    private void updateLivesViews() {
        if (numOfLives == 0) {
            panel_IMG_lives[numOfLives].setVisibility(View.INVISIBLE);
            panel_IMG_lives[0].setVisibility(View.VISIBLE);
            panel_IMG_lives[1].setVisibility(View.VISIBLE);
            panel_IMG_lives[2].setVisibility(View.VISIBLE);
            numOfLives = 3;
            Toast.makeText(getApplicationContext(), "New Game", Toast.LENGTH_SHORT).show();
            //time.removeCallbacks(run);
            /*onStop();
            panel_BTN_right.setVisibility(View.INVISIBLE);
            panel_BTN_left.setVisibility(View.INVISIBLE);
            panel_BTN_tryAgain.setBackgroundColor(Color.RED);
            panel_BTN_tryAgain.setTextColor(Color.WHITE);
            panel_BTN_tryAgain.setVisibility(View.VISIBLE);
            panel_BTN_tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    panel_BTN_tryAgain.setVisibility(View.INVISIBLE);
                    panel_BTN_right.setVisibility(View.VISIBLE);
                    panel_BTN_left.setVisibility(View.VISIBLE);
                    panel_IMG_lives[0].setVisibility(View.VISIBLE);
                    panel_IMG_lives[1].setVisibility(View.VISIBLE);
                    panel_IMG_lives[2].setVisibility(View.VISIBLE);
                    numOfLives = 3;
                    Toast.makeText(getApplicationContext(), "New Game", Toast.LENGTH_SHORT).show();
                    onStop();
                    onStart();
                    //time.removeCallbacks(run);
                    //time.postDelayed(run,deltaTime);
                    //time.removeCallbacks(run);
                    //time.postDelayed(run,deltaTime);

                }
            });*/
        } else
            panel_IMG_lives[numOfLives].setVisibility(View.INVISIBLE);

    }

    private void initNewGame(){

        playerPosition=0;
        time.removeCallbacks(run);
        for (int i = 0; i < 3; i++) {
            onScreen[i] = null;
        }
        for(int i =0;i<3;i++){
            for(int j=0;j<5;j++){
                mat[i][j].setVisibility(View.INVISIBLE);
            }
        }
        time.postDelayed(run,deltaTime);

    }

    private void ouch() {
          final MediaPlayer song = MediaPlayer.create(this,R.raw.ouchsound);
          song.start();
    }

    private void vibrate(int millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(millisecond);
        }
    }

}

