package com.example.astronomyweather.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.astronomyweather.db.dao.DailyWeatherDao;
import com.example.astronomyweather.db.dao.LocationDao;
import com.example.astronomyweather.db.dao.WeatherDao;
import com.example.astronomyweather.model.weather.DailyWeather;
import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.model.weather.Weather;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LocationWeather.class, Weather.class, DailyWeather.class}, version = 3, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract LocationDao locationDao();

    public abstract WeatherDao weatherDao();

    public abstract DailyWeatherDao dailyWeatherDao();

    private static volatile WeatherDatabase INSTANCE;

    public static WeatherDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherDatabase.class, "weather_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}