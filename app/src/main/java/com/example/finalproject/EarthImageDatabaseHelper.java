package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EarthImageDatabaseHelper extends SQLiteOpenHelper {
    protected final static String DB_NAME="EarthImageDB";
    protected final static int version=1;


    public final static String tableName="IMAGES";
    public final static String columnID="ID";
    public final static String columnLat="LAT";
    public final static String columnLon="LON";
    public final static String columnDate="DATE";
    public final static String columnImageKey = "IMAGE_KEY";

    public EarthImageDatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + tableName + "("
                + columnID + " INTEGER,"
                + columnLat + " INTEGER,"
                + columnLon + " INTEGER,"
                + columnImageKey  + " BLOB,"
                + columnDate + " STRING);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
        onCreate(db);
    }


}


