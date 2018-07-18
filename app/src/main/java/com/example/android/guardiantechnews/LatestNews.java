package com.example.android.guardiantechnews;

/**
 * Created by javier on 2018.03.23..
 */

public class LatestNews {
    private String Location;
    private String Time;
    private String Thumbnail;
    private String URLGuardian;
    private String Author;
    private String Section;

    public LatestNews(String time, String location, String thumbnail, String url, String author, String section) {
        Location = location;
        Time = time;
        Thumbnail = thumbnail;
        URLGuardian = url;
        Author = author;
        Section = section;
    }

    public String getLocation() {
        return Location;
    }

    public String getTime() {
        return Time;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getURLGuardian() {
        return URLGuardian;
    }

    public String getAuthor() {
        return Author;
    }

    public String getSection() {
        return Section;
    }
}
