package com.example.astronomyweather.model;

public class MoonData {
    private String moonrise;
    private String moonAltitude;
    private String moonDistance;
    private String moonAzimuth;
    private String moonParallacticAngle;
    private String updateDate;

    public String getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(String moonrise) {
        this.moonrise = moonrise;
    }

    public String getMoonAltitude() {
        return moonAltitude;
    }

    public void setMoonAltitude(String moonAltitude) {
        this.moonAltitude = moonAltitude;
    }

    public String getMoonDistance() {
        return moonDistance;
    }

    public void setMoonDistance(String moonDistance) {
        this.moonDistance = moonDistance;
    }

    public String getMoonAzimuth() {
        return moonAzimuth;
    }

    public void setMoonAzimuth(String moonAzimuth) {
        this.moonAzimuth = moonAzimuth;
    }

    public String getMoonParallacticAngle() {
        return moonParallacticAngle;
    }

    public void setMoonParallacticAngle(String moonParallacticAngle) {
        this.moonParallacticAngle = moonParallacticAngle;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
