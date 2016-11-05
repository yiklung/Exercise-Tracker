package com.example.ext.exercise_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4325966 on 21/9/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pedometerDB";
    // Contacts table name
    private static final String TABLE_SESSION = "session";
    private static final String TABLE_CONNECTION = "connection";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IP = "ip";
    private static final String KEY_FOLDER = "folder";
    private static final String KEY_FILE = "file";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_DATE = "date";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SESSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STEPS + " TEXT,"
                + KEY_CALORIES + " TEXT," + KEY_DATE + " TEXT" + ")";
        String CREATE_CONNECTION_TABLE = "CREATE TABLE " + TABLE_CONNECTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IP + " TEXT," + KEY_FOLDER + " TEXT," + KEY_FILE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONNECTION_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
// Creating tables again
        onCreate(db);
    }

    // Adding new datawh
    public void adddataWH(dataWH datawh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STEPS, datawh.getSteps());
        values.put(KEY_CALORIES, datawh.getCalories());
        values.put(KEY_DATE, datawh.getDate());
// Inserting Row
        db.insert(TABLE_SESSION, null, values);
        db.close(); // Closing database connection
    }
    public void add2Settings(dataSettings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IP, settings.getIP());
        values.put(KEY_FOLDER, settings.getFolder());
        values.put(KEY_FILE, settings.getFile());
// Inserting Row
        db.insert(TABLE_CONNECTION, null, values);
        db.close(); // Closing database connection
    }

    // Getting one datawh
    public dataWH getdataWH(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SESSION, new String[]{KEY_ID,
                        KEY_STEPS, KEY_CALORIES, KEY_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        dataWH contact = new dataWH(
                Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),
                cursor.getString(3)
        );
        // return dataWH
        return contact;
    }

    // Getting All dataWH, Settings
    public List<dataWH> getAlldataWH() {
        List<dataWH> datawhList = new ArrayList<dataWH>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SESSION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dataWH datawh = new dataWH();
                datawh.setId(Integer.parseInt(cursor.getString(0)));
                datawh.setSteps(Integer.parseInt(cursor.getString(1)));
                datawh.setCalories(Integer.parseInt(cursor.getString(2)));
                datawh.setDate(cursor.getString(3));
                // Adding contact to list
                datawhList.add(datawh);
            } while (cursor.moveToNext());
        }
        // return contact list
        return datawhList;
    }
    public List<dataSettings> getAlldataSettings() {
        List<dataSettings> dataSettingsList = new ArrayList<dataSettings>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONNECTION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dataSettings dataSetting = new dataSettings();
                dataSetting.setId(Integer.parseInt(cursor.getString(0)));
                dataSetting.setSteps(cursor.getString(1));
                dataSetting.setCalories(cursor.getString(2));
                dataSetting.setDate(cursor.getString(3));
                // Adding contact to list
                dataSettingsList.add(dataSetting);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dataSettingsList;
    }

    // Getting dataWH Count
    public int getdataWHCount() {
        String countQuery = "SELECT * FROM " + TABLE_SESSION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }
    public int getdataSettingsCount() {
        String countQuery = "SELECT * FROM " + TABLE_CONNECTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        try{

            if (cursor != null) {
                return cursor.getCount();
            }else {return 0;}
        }finally{
            if(cursor != null)
                cursor.close();
        }
    }


    // Updating a datawh
    public int updatedataWH(dataWH datawh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STEPS, datawh.getSteps());
        values.put(KEY_CALORIES, datawh.getCalories());
        values.put(KEY_DATE, datawh.getDate());
        // updating row
        return db.update(TABLE_SESSION, values, KEY_ID + " = ?",
                new String[]{String.valueOf(datawh.getId())});
    }
    // Updating Settings
    public int updateSettings(dataSettings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IP, settings.getIP());
        values.put(KEY_FILE, settings.getFile());
        values.put(KEY_FOLDER, settings.getFolder());
        // updating row
        return db.update(TABLE_CONNECTION, values, KEY_ID + " = ?",
                new String[]{String.valueOf(settings.getId())});
    }

    // Deleting a dataWH
    public void deletedataWH(dataWH datawh) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSION, KEY_ID + " = ?",
                new String[]{String.valueOf(datawh.getId())});
        db.close();
    }


}

