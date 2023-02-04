package com.example.sourcecode_timothymendez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gestures.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "gestures_table";
    private static final String COLUMN_GESTURE = "gesture";
    private static final String COLUMN_PRACTICE_NUMBER = "practice_number";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_GESTURE + " TEXT PRIMARY KEY, " +
                COLUMN_PRACTICE_NUMBER + "INTEGER DEFAULT 0)";
//        db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PRACTICE_NUMBER + " INTEGER DEFAULT 0");
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String gesture, int practiceNumber, boolean editValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        String checkIfGestureHasData = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GESTURE + " = '" + gesture + "';";
        Cursor cursor = db.rawQuery(checkIfGestureHasData, null);
        if (cursor.getCount() == 0 || editValues) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_GESTURE, gesture);
            contentValues.put(COLUMN_PRACTICE_NUMBER, practiceNumber);

            long result = db.insert(TABLE_NAME, null, contentValues);
            cursor.close();
            return result != -1;
        }
        cursor.close();
        // False means the value already existed I think.
        return false;
    }

    public Cursor getData(String gesture) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_PRACTICE_NUMBER + " FROM " + TABLE_NAME + " WHERE "
                + COLUMN_GESTURE + " = '" + gesture + "';";
        return db.rawQuery(query, null);
    }

    public String getColumnPracticeNumber() {
        return COLUMN_PRACTICE_NUMBER;
    }

    public String getColumnGesture() {
        return COLUMN_GESTURE;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String renameGesture(String selectedAction) {
        switch (selectedAction) {
            case "0":
                return "Num0";
            case "1":
                return "Num1";
            case "2":
                return "Num2";
            case "3":
                return "Num3";
            case "4":
                return "Num4";
            case "5":
                return "Num5";
            case "6":
                return "Num6";
            case "7":
                return "Num7";
            case "8":
                return "Num8";
            case "9":
                return "Num9";
            case "Turn On Light":
                return "LightOn";
            case "Turn Off Light":
                return "LightOff";
            case "Turn On Fan":
                return "FanOn";
            case "Turn Off Fan":
                return "FanOff";
            case "Increase Fan Speed":
                return "FanUp";
            case "Decrease Fan Speed":
                return "FanDown";
            case "Set Thermostat to Specified Temperature":
                return "SetThermo";
            default:
                return "";
        }
    }

}
