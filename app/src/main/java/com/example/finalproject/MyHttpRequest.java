package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpRequest extends AsyncTask< String, Integer, String> {

    public Bitmap loadImageFromURL(String url) {
        try {
            URL imageUrl = new URL(url);
            publishProgress(25);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            publishProgress(50);
            InputStream input = (InputStream) connection.getInputStream();
            publishProgress(75);
            Bitmap image = BitmapFactory.decodeStream(input);
            publishProgress(100);
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
