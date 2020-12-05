package com.example.astronomyweather.view.tabPages;

import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;
import com.example.astronomyweather.model.SunData;
import com.example.astronomyweather.model.SyncData;

public class MenuPage extends BasePage<SyncData> {

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_MENU;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Button applyChanges = viewHolder.itemView.findViewById(R.id.applyChanges);
        EditText lat = viewHolder.itemView.findViewById(R.id.latitude);
        EditText lng = viewHolder.itemView.findViewById(R.id.longitude);
        EditText timeInterval = viewHolder.itemView.findViewById(R.id.timeInterval);

        lat.setText(getData().getLat());
        lng.setText(getData().getLng());
        timeInterval.setText(String.valueOf(getData().getTimeInterval()));

        applyChanges.setOnClickListener(v -> {
            if (getListener() instanceof MenuListener){
                ((MenuListener) getListener()).onApplyChanges(lat.getText().toString(),lng.getText().toString(), Integer.valueOf(timeInterval.getText().toString()));
            }
        });
    }

    public interface MenuListener extends BaseListener{
        void onApplyChanges(String lat, String lng, int timeInterval);
    }
}
