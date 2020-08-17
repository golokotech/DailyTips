package com.dennohpeter.dailytips;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "games.db";
    private static final String CREATE_QUERY = "CREATE TABLE football (teams TEXT PRIMARY KEY NOT NULL, start_time TEXT, date TEXT, pick TEXT, result TEXT, won_or_lost TEXT, odds FLOAT, is_live TEXT);";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);

    }

    public void create_or_update_matches(SQLiteDatabase db, String table, String teams, String start_time, String date, String pick, String result, String won_or_lost, String odds, String is_live) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("teams", teams);
        contentValues.put("start_time", start_time);
        contentValues.put("date", date);
        contentValues.put("pick", pick);
        contentValues.put("result", result);
        contentValues.put("won_or_lost", won_or_lost);
        contentValues.put("odds", odds);
        contentValues.put("is_live", is_live);

        int id = (int) db.insertWithOnConflict(table, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            db.update(table, contentValues, "teams=?", new String[]{teams});
        }
    }

    Cursor get_matches(SQLiteDatabase db, String table, String date, String is_live) {
        String[] columns = {"teams", "start_time", "date", "pick", "result", "won_or_lost", "odds"};
        String where = "date=? AND is_live=?";
        String[] whereArgs = new String[]{date, is_live};
        // TODO add orderBy to function parameters for dynamic filtering.
        String orderBy = "start_time " + "ASC";
        return db.query(table, columns, where, whereArgs, null, null, orderBy);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
