package com.example.astronomyweather.view.tabPages;

import com.example.astronomyweather.R;

public enum ViewType {
    VIEW_SUN(R.layout.view_sun),
    VIEW_MOON(R.layout.view_moon),
    VIEW_MENU(R.layout.view_menu),
    VIEW_LOCATIONS(R.layout.view_locations),
    VIEW_WEATHER_WEEKLY(R.layout.view_weather_daily),
    VIEW_WEATHER(R.layout.view_weather);

    private int layoutId;

    public int getLayoutId() {
        return layoutId;
    }

    ViewType(int layoutId) {
        this.layoutId = layoutId;
    }
}
