package com.example.astronomyweather.view.tabPages.dailyWeather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.astronomyweather.R;
import com.example.astronomyweather.Units;
import com.example.astronomyweather.model.AstronomyInfo;
import com.example.astronomyweather.model.weather.DailyWeather;
import com.example.astronomyweather.model.weather.LocationWeather;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {

    private List<DailyWeather> dailyWeathers = new ArrayList<>();
    private Units units;
    private LocationWeather location;

    public DailyWeatherAdapter(LocationWeather location) {
        this.units = location.getUnits();
        this.location = location;
    }

    public void setDailyWeathers(List<DailyWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        ImageView weatherIcon = holder.itemView.findViewById(R.id.weatherIcon);
        TextView dayCity = holder.itemView.findViewById(R.id.day_city);
        TextView weatherDescription = holder.itemView.findViewById(R.id.weatherDescription);
        TextView temperatureMaxMin = holder.itemView.findViewById(R.id.temperatureMaxMin);
        TextView temperatureDayValue = holder.itemView.findViewById(R.id.temperatureDayValue);
        TextView temperatureMornValue = holder.itemView.findViewById(R.id.temperatureMornValue);
        TextView temperatureEveValue = holder.itemView.findViewById(R.id.temperatureEveValue);
        TextView temperatureNightValue = holder.itemView.findViewById(R.id.temperatureNightValue);
        TextView feelTemperatureDayValue = holder.itemView.findViewById(R.id.feelTemperatureDayValue);
        TextView feelTemperatureMornValue = holder.itemView.findViewById(R.id.feelTemperatureMornValue);
        TextView feelTemperatureEveValue = holder.itemView.findViewById(R.id.feelTemperatureEveValue);
        TextView feelTemperatureNightValue = holder.itemView.findViewById(R.id.feelTemperatureNightValue);

        RecyclerView recyclerView = holder.itemView.findViewById(R.id.viewDailyWeatherItem_recyclerView);
        DailyWeatherInfoAdapter adapter = new DailyWeatherInfoAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);

        DailyWeather dailyWeather = dailyWeathers.get(position);

        adapter.setData(createInfo(dailyWeather, context));

        dayCity.setText(getDate(dailyWeather.getDate()) + " | " + location.getCity());
        Glide.with(context).load("https://openweathermap.org/img/wn/" + dailyWeather.getIcon() + "@2x.png").into(weatherIcon);
        weatherDescription.setText(capitalize(dailyWeather.getDescription()));

        temperatureMaxMin.setText(context.getString(R.string.temperatureMaxMin, dailyWeather.getTemp_max(), units.getTempUnit(), dailyWeather.getTemp_min(), units.getTempUnit()));
        temperatureDayValue.setText(context.getString(R.string.temperature, dailyWeather.getTemp_day(), units.getTempUnit()));
        temperatureMornValue.setText(context.getString(R.string.temperature, dailyWeather.getTemp_morn(), units.getTempUnit()));
        temperatureEveValue.setText(context.getString(R.string.temperature, dailyWeather.getTemp_eve(), units.getTempUnit()));
        temperatureNightValue.setText(context.getString(R.string.temperature, dailyWeather.getTemp_night(), units.getTempUnit()));

        feelTemperatureDayValue.setText(context.getString(R.string.temperature, dailyWeather.getFeelsTemp_day(), units.getTempUnit()));
        feelTemperatureMornValue.setText(context.getString(R.string.temperature, dailyWeather.getFeelsTemp_morn(), units.getTempUnit()));
        feelTemperatureEveValue.setText(context.getString(R.string.temperature, dailyWeather.getFeelsTemp_eve(), units.getTempUnit()));
        feelTemperatureNightValue.setText(context.getString(R.string.temperature, dailyWeather.getFeelsTemp_night(), units.getTempUnit()));

    }

    private List<AstronomyInfo> createInfo(DailyWeather dailyWeather, Context context) {
        List<AstronomyInfo> astronomyInfo = new ArrayList<>();
        astronomyInfo.add(new AstronomyInfo("Wschód słońca", getTime(dailyWeather.getSunrise()), R.drawable.ic_sunrise));
        astronomyInfo.add(new AstronomyInfo("Zachód słońca", getTime(dailyWeather.getSunset()), R.drawable.ic_sunset));
        astronomyInfo.add(new AstronomyInfo("Ciśnienie",
                context.getString(R.string.pressure, dailyWeather.getPressure()), R.drawable.ic_pressure));
        astronomyInfo.add(new AstronomyInfo("Wilgotność",
                context.getString(R.string.humidity, dailyWeather.getHumidity()), R.drawable.ic_humidity));
        astronomyInfo.add(new AstronomyInfo("Zachmurzenie",
                context.getString(R.string.clouds, dailyWeather.getClouds()), R.drawable.ic_clouds));
        astronomyInfo.add(new AstronomyInfo("Prędkość wiatru",
                context.getString(R.string.windSpeed, dailyWeather.getWindSpeed(), units.getSpeedUnit()), R.drawable.ic_wind));
        astronomyInfo.add(new AstronomyInfo("Kierunek wiatru",
                context.getString(R.string.windDegrees, dailyWeather.getWindDeg()), R.drawable.ic_wind_degree));

        return astronomyInfo;
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String getTime(Long date) {
        // dates from API are in seconds -> to millisecond need to multiply by 1000
        LocalDateTime dateTime = new LocalDateTime(new Date(date * 1000));
        return dateTime.toString("HH:mm:ss");
    }

    private String getDate(Long date) {
        // dates from API are in seconds -> to millisecond need to multiply by 1000
        LocalDateTime dateTime = new LocalDateTime(new Date(date * 1000));
        return dateTime.toString("dd-MM-yyyy");
    }

    @Override
    public int getItemCount() {
        if (dailyWeathers != null) {
            return dailyWeathers.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
