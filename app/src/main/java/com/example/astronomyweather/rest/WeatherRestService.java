package com.example.astronomyweather.rest;

import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.model.weather.LocationWeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRestService {

    @GET("weather")
    Single<LocationWeather> getCurrentWeather(@Query("q") String city, @Query("appid") String apiKey);

    @GET("onecall")
    Single<LocationWeatherResponse> getCurrentWeather(@Query("lat") String lat, @Query("lon") String lon,
                                                      @Query("exclude") String exclude, @Query("units") String units,
                                                      @Query("lang") String lang, @Query("appid") String apiKey);
}
