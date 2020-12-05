package com.example.astronomyweather.model;

public class AstronomyData {
    private SunData sunData;
    private MoonData moonData;

    public AstronomyData(SunData sunData, MoonData moonData) {
        this.sunData = sunData;
        this.moonData = moonData;
    }

    public SunData getSunData() {
        return sunData;
    }

    public void setSunData(SunData sunData) {
        this.sunData = sunData;
    }

    public MoonData getMoonData() {
        return moonData;
    }

    public void setMoonData(MoonData moonData) {
        this.moonData = moonData;
    }
}
