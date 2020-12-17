package com.example.astronomyweather.view.tabPages;

import android.util.Pair;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.weather.DailyWeather;
import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.view.tabPages.dailyWeather.DailyWeatherAdapter;

import java.util.List;


public class WeatherWeeklyPage extends BasePage<Pair<LocationWeather, List<DailyWeather>>> {

    public WeatherWeeklyPage(Pair<LocationWeather, List<DailyWeather>> data) {
        setData(data);
    }

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_WEATHER_WEEKLY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerView recyclerView = viewHolder.itemView.findViewById(R.id.viewDailyWeather_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext()));
        DailyWeatherAdapter adapter = new DailyWeatherAdapter(getData().first);
        recyclerView.setAdapter(adapter);

        adapter.setDailyWeathers(getData().second);
    }

}
