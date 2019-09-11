package com.hencesimplified.wallpaperpro;

public class sample_photos {
    private String Name;
    private String Url;

    public sample_photos() {
    }

    public sample_photos(String name, String url) {
        Name = name;
        Url = url;
    }

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
