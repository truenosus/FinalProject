package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EarthImageDownloader extends AsyncTask<String, Void, Bitmap>{

    Bitmap earthBP;
    String imageDate;

    public Bitmap loadImageFromURL(String url) {
        try {

            URL sentUrl = new URL(url.toString());

            HttpURLConnection connection = (HttpURLConnection) sentUrl.openConnection();
            connection.setDoInput(true);

            connection.connect();

            InputStream input = (InputStream) connection.getInputStream();


            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();


            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString(); //result is the whole string


            // convert string to JSON:
            JSONObject nasaJSONReturn = new JSONObject(result);


            String imageURLString = nasaJSONReturn.getString("url");

            //sets image date
            setImageDate(nasaJSONReturn.getString("date"));



            Bitmap image = BitmapFactory.decodeStream(decodeReceivedURL(imageURLString));

            return image;
        } catch (Exception e) {
            return null;
        }
    }


    private InputStream decodeReceivedURL(String url) {
        InputStream imageInputStream = null;

        try {
            URL imageURL = new URL(url);

            HttpURLConnection imageURLConnection = (HttpURLConnection) imageURL.openConnection();

            imageInputStream = imageURLConnection.getInputStream();
        } catch (Exception e) {


        }
        return imageInputStream;

    }


    @Override
    protected Bitmap doInBackground(String... url) {

        final String sentURL= url[0];

        earthBP= loadImageFromURL(sentURL);
        return earthBP;
    }

    public void setImageDate(String date){

        imageDate = date;
        MainActivity.imageDate = imageDate;

    }

    public String getImageDate(){
        return imageDate;
    }

}
