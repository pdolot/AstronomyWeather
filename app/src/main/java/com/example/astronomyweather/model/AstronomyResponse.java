package com.example.astronomyweather.model;

import com.google.gson.annotations.SerializedName;

public class AstronomyResponse {
    @SerializedName("sunrise")
    private String sunrise;
    @SerializedName("sunset")
    private String sunset;
    @SerializedName("solar_noon")
    private String solarNoon;
    @SerializedName("day_length")
    private String dayLength;
    @SerializedName("sun_altitude")
    private String sunAltitude;
    @SerializedName("sun_distance")
    private String sunDistance;
    @SerializedName("sun_azimuth")
    private String sunAzimuth;
    @SerializedName("moonrise")
    private String moonrise;
    @SerializedName("moon_altitude")
    private String moonAltitude;
    @SerializedName("moon_distance")
    private String moonDistance;
    @SerializedName("moon_azimuth")
    private String moonAzimuth;
    @SerializedName("moon_parallactic_angle")
    private String moonParallacticAngle;

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

    public String getSolarNoon() {
        return solarNoon;
    }

    public void setSolarNoon(String solarNoon) {
        this.solarNoon = solarNoon;
    }

    public String getDayLength() {
        return dayLength;
    }

    public void setDayLength(String dayLength) {
        this.dayLength = dayLength;
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
}
