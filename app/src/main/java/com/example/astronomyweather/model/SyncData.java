package com.example.astronomyweather.model;

public class SyncData {
    private String lat;
    private String lng;
    private int timeInterval;

    public SyncData(String lat, String lng, int timeInterval) {
        this.lat = lat;
        this.lng = lng;
        this.timeInterval = timeInterval;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }
}
