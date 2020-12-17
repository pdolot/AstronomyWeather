package com.example.astronomyweather.rest;

import com.example.astronomyweather.model.AstronomyResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AstronomyRestService {
    @GET("astronomy")
    Single<AstronomyResponse> getAstronomyInfo(@Query("apiKey") String key, @Query("lat") String lat, @Query("long") String lng);
}
