package com.example.astronomyweather.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.astronomyweather.model.weather.Weather;

import io.reactivex.Single;

@Dao
public abstract class WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insert(Weather Weather);

    @Query("SELECT * FROM weather WHERE locationId=:locationId ORDER BY date DESC LIMIT 1")
    public abstract Single<Weather> getWeatherByLocation(Long locationId);

    @Query("DELETE FROM weather")
    public abstract Single<Integer> delete();
}
