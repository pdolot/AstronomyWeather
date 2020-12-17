package com.example.astronomyweather.model;

import com.example.astronomyweather.Units;

public class SyncData {
    private String lat;
    private String lng;
    private int timeInterval;
    private Units units;

    public SyncData(String lat, String lng, int timeInterval, Units units) {
        this.lat = lat;
        this.lng = lng;
        this.timeInterval = timeInterval;
        this.units = units;
    }

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
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
