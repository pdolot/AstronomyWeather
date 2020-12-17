package com.example.astronomyweather.rest;

import com.example.astronomyweather.model.weather.DailyWeather;
import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.model.weather.LocationWeatherResponse;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherRestRepository {
    private WeatherRestService weatherRestService;
    private static String lang = "pl";
    private static String exclude = "hourly,minutely";
    ;

    public WeatherRestRepository(WeatherRestService weatherRestService) {
        this.weatherRestService = weatherRestService;
    }

    public Single<LocationWeather> getCurrentWeather(String city, String apiKey) {
        return weatherRestService.getCurrentWeather(city, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(locationWeather -> {
                    locationWeather.setCity(city);
                    locationWeather.setLatitude(locationWeather.getCoordinates().getLatitude());
                    locationWeather.setLongitude(locationWeather.getCoordinates().getLongitude());
                    locationWeather.setFavorite(true);
                    locationWeather.setCurrent(true);
                    return locationWeather;
                });
    }

    public Single<LocationWeatherResponse> getCurrentWeather(LocationWeather locationWeather, String apiKey, String units) {
        Long locationId = locationWeather.getId();
        String lat = String.valueOf(locationWeather.getLatitude());
        String lon = String.valueOf(locationWeather.getLongitude());
        return weatherRestService.getCurrentWeather(lat, lon, exclude, units, lang, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    response.getCurrent().setLocationId(locationId);
                    response.getCurrent().setIcon(response.getCurrent().getWeather().get(0).getIcon());
                    response.getCurrent().setDescription(response.getCurrent().getWeather().get(0).getDescription());

                    for (DailyWeather dailyWeather : response.getDaily()) {
                        dailyWeather.setLocationId(locationId);
                        dailyWeather.setIcon(dailyWeather.getWeather().get(0).getIcon());
                        dailyWeather.setDescription(dailyWeather.getWeather().get(0).getDescription());

                        dailyWeather.setTemp_day(dailyWeather.getTemp().getDay());
                        dailyWeather.setTemp_max(dailyWeather.getTemp().getMax());
                        dailyWeather.setTemp_min(dailyWeather.getTemp().getMin());
                        dailyWeather.setTemp_night(dailyWeather.getTemp().getNight());
                        dailyWeather.setTemp_eve(dailyWeather.getTemp().getEve());
                        dailyWeather.setTemp_morn(dailyWeather.getTemp().getMorn());

                        dailyWeather.setFeelsTemp_day(dailyWeather.getFeelsTemp().getDay());
                        dailyWeather.setFeelsTemp_night(dailyWeather.getFeelsTemp().getNight());
                        dailyWeather.setFeelsTemp_eve(dailyWeather.getFeelsTemp().getEve());
                        dailyWeather.setFeelsTemp_morn(dailyWeather.getFeelsTemp().getMorn());
                    }
                    return response;
                });
    }
}
