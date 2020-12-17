package com.example.astronomyweather.view.tabPages.locations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.weather.LocationWeather;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder> {

    private List<LocationWeather> locations = new ArrayList<>();
    private LocationsPage.LocationsListener listener;

    public void setLocations(List<LocationWeather> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView city = holder.itemView.findViewById(R.id.city);
        CheckBox favorite = holder.itemView.findViewById(R.id.setFavorite);
        CheckBox current = holder.itemView.findViewById(R.id.setCurrent);

        LocationWeather location = locations.get(position);
        city.setText(location.getCity());
        favorite.setChecked(location.getFavorite());
        current.setChecked(location.getCurrent());

        favorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onFavoriteChange(location.getId(), isChecked);
            }
        });

        current.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onSetCurrent(location.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (locations != null) {
            return locations.size();
        } else {
            return 0;
        }
    }


    public void setLocationListener(LocationsPage.LocationsListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
