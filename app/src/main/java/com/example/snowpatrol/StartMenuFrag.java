package com.example.snowpatrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class StartMenuFrag extends Fragment {

    private TextView menu_TXT_snowpatrol;
    private Button menu_BTN_play_buttons;
    private Button menu_BTN_top;
    private Button menu_BTN_play_sens;
    private FrameLayout menuFrame;
    private AppCompatActivity activity;
    private MenuCallback menu_callback;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackMenu(MenuCallback menu_callback) {
        this.menu_callback = menu_callback;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        findViews(view);
        startView();
        return view;
    }

    private void startView() {
        menu_BTN_play_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_callback!=null){
                    menu_callback.buttonMode();
                }
            }
        });

        menu_BTN_play_sens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_callback!=null){
                    menu_callback.sensorMode();
                }
            }
        });

        menu_BTN_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_callback!=null){
                    menu_callback.TopTen();

                }
            }
        });
    }

    private void findViews(View view) {
        menu_BTN_play_buttons = view.findViewById(R.id.menu_BTN_play_buttons);
        menu_BTN_play_sens = view.findViewById(R.id.menu_BTN_play_sens);
        menu_BTN_top = view.findViewById(R.id.menu_BTN_top);
        menu_TXT_snowpatrol = view.findViewById(R.id.menu_TXT_snowpatrol);
    }
}
