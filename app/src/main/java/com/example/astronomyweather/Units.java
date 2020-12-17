package com.example.astronomyweather;

public enum Units {
    STANDARD("K", "m/s"),
    METRIC("°C", "m/s"),
    IMPERIAL("°F", "mile/s");

    private String tempUnit;
    private String speedUnit;

    Units(String tempUnit, String speedUnit) {
        this.tempUnit = tempUnit;
        this.speedUnit = speedUnit;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    public void setSpeedUnit(String speedUnit) {
        this.speedUnit = speedUnit;
    }
}
