package com.example.astronomyweather.db.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.astronomyweather.db.WeatherDatabase;
import com.example.astronomyweather.db.dao.LocationDao;
import com.example.astronomyweather.model.weather.LocationWeather;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocationRepository {

    private LocationDao locationDao;

    public LocationRepository(Application application) {
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        locationDao = db.locationDao();
    }

    public LiveData<List<LocationWeather>> getLocationsMutable(Boolean isFavorite) {
        return locationDao.getLocationsMutable(isFavorite);
    }

    public Single<List<LocationWeather>> getLocations(Boolean isFavorite) {
        return locationDao.getLocations(isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LocationWeather> getCurrent() {
        return locationDao.getCurrent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LocationWeather> getLocationById(Long id) {
        return locationDao.getLocationById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> setFavorite(Long id, Boolean isFavorite) {
        return locationDao.setFavorite(id, isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Single<Integer> setCurrent(Long id, Boolean isCurrent) {
        return locationDao.setCurrent(id, isCurrent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Single<Long> insert(LocationWeather location) {
        return locationDao.insert(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
