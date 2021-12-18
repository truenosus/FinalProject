package com.example.finalproject;

import android.graphics.Bitmap;

public class EarthImageObject {

    private String date;
    private int id;
    private String lat;
    private String lon;
    private Bitmap image;

    public EarthImageObject(int id, String lat, String lon, String date, Bitmap image) {
        this.id=id;
        this.lat=lat;
        this.lon=lon;
        this.date=date;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public EarthImageObject(String lat, String lon){
        setLat(lat);
        setLon(lon);
        setImage(null);
        setDate(null);
    }

    public EarthImageObject(String lat, String lon, Bitmap image, String date){
        setLat(lat);
        setLon(lon);
        setImage(image);
        setDate(date);

    }

    public EarthImageObject(EarthImageObject item) {
        this.lat=item.getLat();
        this.lon=item.getLon();
        this.image=item.getImage();
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
