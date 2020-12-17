package com.example.astronomyweather.db.repository;

import android.app.Application;

import com.example.astronomyweather.db.WeatherDatabase;
import com.example.astronomyweather.db.dao.DailyWeatherDao;
import com.example.astronomyweather.model.weather.DailyWeather;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DailyWeatherRepository {

    private DailyWeatherDao dailyWeatherDao;

    public DailyWeatherRepository(Application application) {
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        dailyWeatherDao = db.dailyWeatherDao();
    }

    public Single<List<DailyWeather>> getWeatherForNextSevenDays(Long locationId) {
        return dailyWeatherDao.getDailyWeatherByLocation(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Long>> insert(List<DailyWeather> dailyWeathers) {
        return dailyWeatherDao.insert(dailyWeathers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> delete() {
        return dailyWeatherDao.delete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
