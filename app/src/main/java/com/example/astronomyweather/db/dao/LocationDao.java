package com.example.astronomyweather.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.astronomyweather.model.weather.LocationWeather;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Single<Long> insert(LocationWeather locationWeather);

    @Query("SELECT * FROM location WHERE isFavorite=:isFavorite")
    public abstract LiveData<List<LocationWeather>> getLocationsMutable(Boolean isFavorite);

    @Query("SELECT * FROM location WHERE isFavorite=:isFavorite")
    public abstract Single<List<LocationWeather>> getLocations(Boolean isFavorite);

    @Query("SELECT * FROM location WHERE isCurrent=1")
    public abstract Single<LocationWeather> getCurrent();

    @Query("SELECT * FROM location WHERE id=:id")
    public abstract Single<LocationWeather> getLocationById(Long id);

    @Query("UPDATE location SET isFavorite=:favorite WHERE id=:id")
    public abstract Single<Integer> setFavorite(Long id, Boolean favorite);

    @Query("UPDATE location SET isCurrent=:current WHERE id=:id")
    public abstract Single<Integer> setCurrent(Long id, Boolean current);

    @Query("DELETE FROM location WHERE id= :id")
    public abstract void deleteLocation(long id);

    @Transaction
    public void insertUpdate(LocationWeather locationWeather, Long previousCurrent) {
        if (previousCurrent != null)
            setCurrent(previousCurrent, false);
        insert(locationWeather);
    }
}
