package com.example.astronomyweather.view.tabPages;

import android.content.Context;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.astronomyweather.R;
import com.example.astronomyweather.model.AstronomyInfo;
import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.model.weather.Weather;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherPage extends BasePage<Pair<LocationWeather, Weather>> {

    private final AstronomyInfoAdapter adapter = new AstronomyInfoAdapter();

    public WeatherPage(Pair<LocationWeather, Weather> data) {
        setData(data);
    }

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_WEATHER;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Context context = viewHolder.itemView.getContext();
        ImageView weatherIcon = viewHolder.itemView.findViewById(R.id.weatherIcon);
        TextView location = viewHolder.itemView.findViewById(R.id.location);
        TextView weatherDescription = viewHolder.itemView.findViewById(R.id.weatherDescription);
        TextView temperature = viewHolder.itemView.findViewById(R.id.temperature);
        TextView updateTime = viewHolder.itemView.findViewById(R.id.updateTime);

        String tempMetric = getData().first.getUnits().getTempUnit();
        String speedMetric = getData().first.getUnits().getSpeedUnit();

        Weather weather = getData().second;
        location.setText(getData().first.getCity());
        Glide.with(context).load("https://openweathermap.org/img/wn/" + weather.getIcon() + "@2x.png").into(weatherIcon);
        weatherDescription.setText(capitalize(weather.getDescription()));
        temperature.setText(context.getString(R.string.temperature, weather.getTemp(), tempMetric));

        RecyclerView recyclerView = viewHolder.itemView.findViewById(R.id.viewWeather_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        adapter.setData(getAstronomyInfo(weather, context, tempMetric, speedMetric));

        updateTime.setText("Dane z dnia:\n" + getDate(weather.getDate()));
    }

    private List<AstronomyInfo> getAstronomyInfo(Weather weather, Context context, String tempUnits, String speedUnit) {
        List<AstronomyInfo> astronomyInfo = new ArrayList<>();
        astronomyInfo.add(new AstronomyInfo("Wschód słońca", getTime(weather.getSunrise()), R.drawable.ic_sunrise));
        astronomyInfo.add(new AstronomyInfo("Zachód słońca", getTime(weather.getSunset()), R.drawable.ic_sunset));
        astronomyInfo.add(new AstronomyInfo("Odczuwalna temperatura",
                context.getString(R.string.temperature, weather.getFeelsTemp(), tempUnits), R.drawable.ic_temperature));
        astronomyInfo.add(new AstronomyInfo("Ciśnienie",
                context.getString(R.string.pressure, weather.getPressure()), R.drawable.ic_pressure));
        astronomyInfo.add(new AstronomyInfo("Wilgotność",
                context.getString(R.string.humidity, weather.getHumidity()), R.drawable.ic_humidity));
        astronomyInfo.add(new AstronomyInfo("Zachmurzenie",
                context.getString(R.string.clouds, weather.getClouds()), R.drawable.ic_clouds));
        astronomyInfo.add(new AstronomyInfo("Widoczność",
                context.getString(R.string.visibility, weather.getVisibility()), R.drawable.ic_visibility));
        astronomyInfo.add(new AstronomyInfo("Prędkość wiatru",
                context.getString(R.string.windSpeed, weather.getWindSpeed(), speedUnit), R.drawable.ic_wind));
        astronomyInfo.add(new AstronomyInfo("Kierunek wiatru",
                context.getString(R.string.windDegrees, weather.getWindDeg()), R.drawable.ic_wind_degree));

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
        return dateTime.toString("dd-MM-yyyy HH:mm:ss");
    }

}
