package com.example.snowpatrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.snowpatrol.CallbacksTopTen.CallbackList;
import com.example.snowpatrol.GameDB.TopTenComponent;

import java.util.ArrayList;

public class FragmentList extends Fragment {

    private AppCompatActivity activity;
    private CallbackList callbackList;
    private ArrayList<TopTenComponent> topTen;
    private LinearLayout[] rowsList;
    private TextView[] playersList;
    private TextView[] scoresList;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }
    public void setCallbackList(CallbackList callbackList) {

        this.callbackList = callbackList;
    }
    public void setRecords(ArrayList<TopTenComponent> topTen) {
        this.topTen = topTen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        updateListView();
        return view;
    }

    private void initViews() {
        for (int i = 0; i < 10; i++) {
            rowsList[i].setOnClickListener(setOnMap(i));
        }
    }

    private View.OnClickListener setOnMap(int index) {
        return view -> {
            callbackList.func(index);
        };
    }

    private void updateListView() {
        int i = 0;
        for (TopTenComponent comp: this.topTen) {
            playersList[i].setText(comp.getName());
            scoresList[i].setText("  "+comp.getScore());
            rowsList[i].setVisibility(View.VISIBLE);
            i++;
        }
    }

    private void findViews(View view) {
        playersList = new TextView[]{
                view.findViewById(R.id.list_player_player0),
                view.findViewById(R.id.list_player_player1),
                view.findViewById(R.id.list_player_player2),
                view.findViewById(R.id.list_player_player3),
                view.findViewById(R.id.list_player_player4),
                view.findViewById(R.id.list_player_player5),
                view.findViewById(R.id.list_player_player6),
                view.findViewById(R.id.list_player_player7),
                view.findViewById(R.id.list_player_player8),
                view.findViewById(R.id.list_player_player9),
        };
        scoresList = new TextView[]{
                view.findViewById(R.id.list_score_score0),
                view.findViewById(R.id.list_score_score1),
                view.findViewById(R.id.list_score_score2),
                view.findViewById(R.id.list_score_score3),
                view.findViewById(R.id.list_score_score4),
                view.findViewById(R.id.list_score_score5),
                view.findViewById(R.id.list_score_score6),
                view.findViewById(R.id.list_score_score7),
                view.findViewById(R.id.list_score_score8),
                view.findViewById(R.id.list_score_score9),
        };
        rowsList = new LinearLayout[]{
                view.findViewById(R.id.list_player_row0),
                view.findViewById(R.id.list_player_row1),
                view.findViewById(R.id.list_player_row2),
                view.findViewById(R.id.list_player_row3),
                view.findViewById(R.id.list_player_row4),
                view.findViewById(R.id.list_player_row5),
                view.findViewById(R.id.list_player_row6),
                view.findViewById(R.id.list_player_row7),
                view.findViewById(R.id.list_player_row8),
                view.findViewById(R.id.list_player_row9),
        };
    }

}
