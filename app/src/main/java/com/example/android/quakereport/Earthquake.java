package com.example.android.quakereport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Earthquake {

    private String mag;
    private double magnitude;
    private String dateAsText;
    private String timeAsText;
    private String locationOff;
    private String primaryLocation;
    private String url;

    public String getLocationOff() {
        return locationOff;
    }

    public String getPrimaryLocation() {
        return primaryLocation;
    }

    public String getMag() {
        return mag;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getDate() {
        return dateAsText;
    }

    public String getTime() {
        return timeAsText;
    }

    public String getUrl (){
        return url;
    }

    Earthquake(String location, double mag, long milliseconds, String url) {
        Date date;
        this.url = url;
        try {

            //split locations
            if (location.contains("km")) {
                int p = location.indexOf(" of ");
                locationOff = location.substring(0, p + 3);
                primaryLocation = location.substring(p + 4);
            } else {
                locationOff = "Near the";
                primaryLocation = location;
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            this.mag = decimalFormat.format(mag);
            this.magnitude = mag;

            date = new Date(milliseconds);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd, yyyy");
            dateAsText = simpleDateFormat.format(date);
            simpleDateFormat = new SimpleDateFormat("h:mm a");
            timeAsText = simpleDateFormat.format(date);
        } catch (Exception e) {
            dateAsText = e.getMessage();
            timeAsText = "*";
        }
    }

  }
