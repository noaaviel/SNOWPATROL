package com.example.snowpatrol.GameDB;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AdminDB {

    private final static int MAX_COMPONENTS =10;
    public final static String TOP_TEN_LIST= "TOP TEN LIST";

    public static void updateRecord(MyDB myDB, TopTenComponent ttc) {
        ArrayList<TopTenComponent> records = myDB.getTopTenComponents();
        records.add(ttc);
        records.sort((a, b) -> (int) (b.getScore() - a.getScore()));
        if(records.size() > MAX_COMPONENTS){
            records.remove(MAX_COMPONENTS);
        }
        saveRecordDBToSP(myDB);
    }

    private static void saveRecordDBToSP(MyDB myDB) {
        MSPV3.getMe().putString(TOP_TEN_LIST, new Gson().toJson(myDB));
    }

    public static MyDB getRecordDBFromSP() {
        MyDB myDB = new Gson().fromJson(MSPV3.getMe().getString(TOP_TEN_LIST, null), MyDB.class);
        if (myDB == null){
            myDB = new MyDB();
        }
        return myDB;
    }
}
