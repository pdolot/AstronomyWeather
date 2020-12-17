package com.example.astronomyweather.rest;

import com.example.astronomyweather.model.AstronomyData;
import com.example.astronomyweather.model.MoonData;
import com.example.astronomyweather.model.SunData;

import org.joda.time.LocalTime;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AstronomyRestRepository {

    private AstronomyRestService astronomyRestService;

    public AstronomyRestRepository(AstronomyRestService astronomyRestService) {
        this.astronomyRestService = astronomyRestService;
    }

    public Flowable<AstronomyData> getAstronomyInfo(String key, String lat, String lng, Integer timeInterval) {
        return astronomyRestService.getAstronomyInfo(key, lat, lng)
                .repeatWhen(completed -> completed.delay(timeInterval, TimeUnit.MINUTES))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(astronomyResponse -> {
                    String currentTime = LocalTime.now().toString("HH:mm:ss");
                    SunData sunData = new SunData();
                    sunData.setSunset(astronomyResponse.getSunset());
                    sunData.setSunrise(astronomyResponse.getSunrise());
                    sunData.setSunDistance(astronomyResponse.getSunDistance());
                    sunData.setSunAzimuth(astronomyResponse.getSunAzimuth());
                    sunData.setSunAltitude(astronomyResponse.getSunAltitude());
                    sunData.setUpdateDate(currentTime);

                    MoonData moonData = new MoonData();
                    moonData.setMoonrise(astronomyResponse.getMoonrise());
                    moonData.setMoonParallacticAngle(astronomyResponse.getMoonParallacticAngle());
                    moonData.setMoonDistance(astronomyResponse.getMoonDistance());
                    moonData.setMoonAzimuth(astronomyResponse.getMoonAzimuth());
                    moonData.setMoonAltitude(astronomyResponse.getMoonAltitude());
                    moonData.setUpdateDate(currentTime);

                    return new AstronomyData(sunData, moonData);
                });
    }
}
