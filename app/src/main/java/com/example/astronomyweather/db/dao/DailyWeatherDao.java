package com.example.astronomyweather.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.astronomyweather.model.weather.DailyWeather;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class DailyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<List<Long>> insert(List<DailyWeather> dailyWeather);

    @Query("SELECT * FROM daily_weather WHERE locationId=:locationId ORDER BY date ASC LIMIT 7")
    public abstract Single<List<DailyWeather>> getDailyWeatherByLocation(Long locationId);

    @Query("DELETE FROM daily_weather")
    public abstract Single<Integer> delete();
}
