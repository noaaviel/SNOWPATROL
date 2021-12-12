package com.example.snowpatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class EndGameActivity extends AppCompatActivity {
    RelativeLayout endGame;
    Button panel_BTN_submit;
    EditText panel_LBL_submitName;
    private int score;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        this.bundle = getIntent().getBundleExtra("BUNDLE");
        findViews();
        score = bundle.getInt("SCORE",0);
        initSubmitButton();
    }

    private void initSubmitButton() {
        panel_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = panel_LBL_submitName.getText().toString();
                sendData(playerName);
                finish();

            }
        });
    }
    private void sendData(String playerName) {
        Intent myIntent = new Intent(this,TopTenActivity.class);
        bundle.putString("PLAYER NAME",playerName);
        bundle.putInt("SCORE",score);
        myIntent.putExtra("BUNDLE", bundle);
        startActivity(myIntent);
        finish();
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

    void findViews(){
        endGame = findViewById(R.id.panel_endGame);
        panel_BTN_submit = findViewById(R.id.panel_BTN_submit);
        panel_LBL_submitName = findViewById(R.id.panel_LBL_submitName);
        endGame.setVisibility(View.VISIBLE);
        panel_BTN_submit.setVisibility(View.VISIBLE);
        panel_LBL_submitName.setVisibility(View.VISIBLE);
    }
}