package com.example.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDB extends SQLiteOpenHelper {



    static final String dbName="AMS";
    static final String Table="LocationData";
    //static final String userID="UserID";
    static final String colName="latitude";
    static final String colName1="longitude";


    public SQLiteDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName,null, 33);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE LocationData " +
                "( latitude VARCHAR(500) NOT NULL , longitude VARCHAR(500) NOT NULL , battery VARCHAR(500) NOT NULL )");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
