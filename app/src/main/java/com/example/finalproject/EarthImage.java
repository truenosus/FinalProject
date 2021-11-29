package com.example.finalproject;

import android.graphics.Bitmap;

public class EarthImage {

    private String lat;
    private String lon;
    private Bitmap image;

    public EarthImage(String lat, String lon, Bitmap image){
        setLat(lat);
        setLon(lon);
        setImage(image);
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

}
