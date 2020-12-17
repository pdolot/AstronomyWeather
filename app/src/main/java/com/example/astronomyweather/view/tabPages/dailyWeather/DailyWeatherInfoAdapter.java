package com.example.astronomyweather.view.tabPages.dailyWeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.AstronomyInfo;

import java.util.ArrayList;
import java.util.List;

public class DailyWeatherInfoAdapter extends RecyclerView.Adapter<DailyWeatherInfoAdapter.ViewHolder> {

    private List<AstronomyInfo> data = new ArrayList<>();

    public void setData(List<AstronomyInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_weather_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView label = holder.itemView.findViewById(R.id.label);
        TextView value = holder.itemView.findViewById(R.id.value);
        ImageView icon = holder.itemView.findViewById(R.id.icon);

        AstronomyInfo info = data.get(position);

        label.setText(info.getLabel());
        value.setText(info.getValue());
        icon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), info.getIcon()));

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
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
