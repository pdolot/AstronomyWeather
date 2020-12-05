package com.example.astronomyweather.model;

public class AstronomyInfo {
    private String label;
    private String value;
    private int icon;

    public AstronomyInfo(String label, String value, int icon) {
        this.label = label;
        this.value = value;
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
