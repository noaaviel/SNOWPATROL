package com.example.snowpatrol;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    private static final String TAG = ActivitySplash.class.getSimpleName();

    final int ANIM_DURATION = 5500;
    private ImageView splash_IMG_logo;
    private MediaPlayer musicSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViews();
        splash_IMG_logo.setVisibility(View.INVISIBLE);
        showViewSlideDown(splash_IMG_logo);


    }

    public void showViewSlideDown(final View v) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height / 2);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        playBells();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        musicSound.stop();
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);
        this.finish();
    }


    private void animationDone() {
        openHomeActivity();
    }

    private void playBells() {
        musicSound = MediaPlayer.create(this,R.raw.splash_music);
        musicSound.setVolume(0.3f,0.3f);
        musicSound.start();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
    }
}