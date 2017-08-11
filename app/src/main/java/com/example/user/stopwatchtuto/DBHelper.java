package com.example.user.stopwatchtuto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 03-08-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME ="timerEntry.db";
    final static String TABLE_NAME ="timer";
    final static String PRIMARY_ID ="primaryId";
    final static String START_TIME ="startTime";
    final static String TOTAL_HOUR = "totalHour";
    final static String EVENT ="event";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "
                + TABLE_NAME + " ("
                + PRIMARY_ID + " INTEGER PRIMARY KEY, "
                + START_TIME + " TEXT, "
                + EVENT + " TEXT, "
                + TOTAL_HOUR + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    public void insertTimer(String startTime,String currentEvent){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(START_TIME,startTime);
        contentValues.put(EVENT,currentEvent);
        database.insert(TABLE_NAME,null,contentValues);
        database.close();
    }
    public Cursor getTimer(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        return cursor;

    }
}
