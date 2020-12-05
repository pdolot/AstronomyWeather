package com.example.astronomyweather.model;

public class SunData {
    private String sunrise;
    private String sunset;
    private String sunAltitude;
    private String sunDistance;
    private String sunAzimuth;
    private String updateDate;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunAltitude() {
        return sunAltitude;
    }

    public void setSunAltitude(String sunAltitude) {
        this.sunAltitude = sunAltitude;
    }

    public String getSunDistance() {
        return sunDistance;
    }

    public void setSunDistance(String sunDistance) {
        this.sunDistance = sunDistance;
    }

    public String getSunAzimuth() {
        return sunAzimuth;
    }

    public void setSunAzimuth(String sunAzimuth) {
        this.sunAzimuth = sunAzimuth;
    }
}
