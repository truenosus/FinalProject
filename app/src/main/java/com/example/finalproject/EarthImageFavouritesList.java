package com.example.finalproject;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EarthImageFavouritesList {


   //private static ArrayList<EarthImageObject> earthImageObjectListArray = new ArrayList<>();

   static EarthImageDatabaseHelper dbOpener = new EarthImageDatabaseHelper(MainActivity.getContext());

   public static boolean add(EarthImageObject earthImageObject){


      ContentValues newRowValues = new ContentValues();

      //put values into new rowvalues
      newRowValues.put(dbOpener.columnID, earthImageObject.hashCode());
      newRowValues.put(dbOpener.columnLat, earthImageObject.getLat());
      newRowValues.put(dbOpener.columnLon, earthImageObject.getLon());
      newRowValues.put(dbOpener.columnDate, earthImageObject.getDate());
      newRowValues.put(dbOpener.columnImageKey, BitmapConverter.BitMapToString(earthImageObject.getImage()));

      //insert into db
      dbOpener.getWritableDatabase().insert(dbOpener.tableName, null, newRowValues);

      return false;
   }

   public static int getColumnCount(){

      return (int) DatabaseUtils.queryNumEntries(dbOpener.getReadableDatabase(),"EarthImageDB");
   }


   public static SQLiteDatabase getEarthImageObjectDatabase(){
      return dbOpener.getWritableDatabase();
   }


   public static void remove(EarthImageObject earthImageObject) {
      dbOpener.getWritableDatabase().delete(dbOpener.tableName,dbOpener.columnID + "=" + Integer.toString(earthImageObject.getId()),null);
   }
}
