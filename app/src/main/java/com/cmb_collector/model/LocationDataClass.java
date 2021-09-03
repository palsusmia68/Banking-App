package com.cmb_collector.model;

import java.io.Serializable;

public class LocationDataClass  implements Serializable{
    private String date;
    private String time;
    private String type;
    private String lat;
    private String lang;
    private String address;

    public LocationDataClass(String date, String time, String type, String lat, String lang,String address) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.lat = lat;
        this.lang = lang;
        this.address=address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
