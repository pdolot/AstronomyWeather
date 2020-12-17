package com.example.astronomyweather.db.repository;

import android.app.Application;

import com.example.astronomyweather.db.WeatherDatabase;
import com.example.astronomyweather.db.dao.WeatherDao;
import com.example.astronomyweather.model.weather.Weather;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepository {

    private WeatherDao weatherDao;

    public WeatherRepository(Application application) {
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        weatherDao = db.weatherDao();
    }

    public Single<Weather> getCurrentWeather(Long locationId) {
        return weatherDao.getWeatherByLocation(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> insert(Weather weather) {
        return weatherDao.insert(weather)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> delete() {
        return weatherDao.delete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
