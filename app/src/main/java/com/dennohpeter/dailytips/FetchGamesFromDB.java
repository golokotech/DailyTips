package com.dennohpeter.dailytips;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.dennohpeter.dailytips.football.FootballModel;

import java.util.ArrayList;

public class FetchGamesFromDB {
    private String match_type, date, is_live;
    private DataBaseHelper dataBaseHelper;
    public FetchGamesFromDB(Context context, String match_type, String date, String is_live) {
        this.match_type = match_type;
        this.date = date;
        this.is_live = is_live;
        dataBaseHelper = new DataBaseHelper(context);
    }

    public ArrayList<FootballModel> get_timeline_games() {
        ArrayList<FootballModel> games = new ArrayList<>();
        String teams, start_time, match_date, pick, result, won_or_lost, odds;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = dataBaseHelper.get_matches(db, match_type, date, is_live);
        while (cursor.moveToNext()) {
            teams = cursor.getString(cursor.getColumnIndex("teams"));
            start_time = cursor.getString(cursor.getColumnIndex("start_time"));
            match_date = cursor.getString(cursor.getColumnIndex("date"));
            pick = cursor.getString(cursor.getColumnIndex("pick"));
            result = cursor.getString(cursor.getColumnIndex("result"));
            won_or_lost = cursor.getString(cursor.getColumnIndex("won_or_lost"));
            odds = cursor.getString(cursor.getColumnIndex("odds"));
            int icon;
            if (won_or_lost.equals("won")) {
                icon = R.drawable.won;
            } else if (won_or_lost.equals("lost")) {
                icon = R.drawable.lost;
            } else {
                icon = R.drawable.waiting;
            }
            games.add(new FootballModel(teams, start_time, match_date, pick, result, odds, icon));

        }
        cursor.close();
        db.close();
        return games;
    }
}
