package com.example.android.quakereport;

/**
 * Created by javier on 2018.03.23..
 */

public class QuakeData {
    private String Location;
    private String Time;
    private String Magnitude;

    public QuakeData(String time, String location, String magnitude) {
        Location = location;
        Time = time;
        Magnitude = magnitude;
    }

    public String getLocation() {
        return Location;
    }

    public String getTime() {
        return Time;
    }

    public String getMagnitude() {
        return Magnitude;
    }
}
