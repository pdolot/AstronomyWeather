package com.example.astronomyweather.view.tabPages.locations;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.view.tabPages.BaseListener;
import com.example.astronomyweather.view.tabPages.BasePage;
import com.example.astronomyweather.view.tabPages.ViewType;

import java.util.List;

public class LocationsPage extends BasePage<List<LocationWeather>> {

    private boolean favoriteFilterEnable = false;
    private LocationsAdapter locationsAdapter = new LocationsAdapter();

    public LocationsPage(boolean favoriteFilterEnable, List<LocationWeather> data) {
        super();
        this.favoriteFilterEnable = favoriteFilterEnable;
        setData(data);
    }

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_LOCATIONS;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Context context = viewHolder.itemView.getContext();
        Button addCity = viewHolder.itemView.findViewById(R.id.addCity);
        EditText city = viewHolder.itemView.findViewById(R.id.city);
        TextView all = viewHolder.itemView.findViewById(R.id.locationsAll);
        TextView favorite = viewHolder.itemView.findViewById(R.id.locationsFavorites);
        RecyclerView recyclerView = viewHolder.itemView.findViewById(R.id.locationsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(locationsAdapter);

        locationsAdapter.setLocations(getData());

        addCity.setOnClickListener(v -> {
            if (!city.getText().toString().isEmpty()) {
                ((LocationsListener) getListener()).onAddCity(city.getText().toString());
            }
        });

        if (favoriteFilterEnable) {
            favorite.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)));
            all.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.transparent_10)));
        } else {
            favorite.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.transparent_10)));
            all.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)));
        }

        all.setOnClickListener(v -> {
            ((LocationsListener) getListener()).onChangeFilter(false);
        });

        favorite.setOnClickListener(v -> {
            ((LocationsListener) getListener()).onChangeFilter(true);
        });

        locationsAdapter.setLocationListener(((LocationsListener) getListener()));
    }

    public interface LocationsListener extends BaseListener {
        void onAddCity(String city);

        void onChangeFilter(Boolean favorite);

        void onFavoriteChange(Long id, Boolean favorite);

        void onSetCurrent(Long id);
    }
}
