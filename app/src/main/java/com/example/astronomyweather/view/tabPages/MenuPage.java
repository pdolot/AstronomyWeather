package com.example.astronomyweather.view.tabPages;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.Units;
import com.example.astronomyweather.R;
import com.example.astronomyweather.model.SyncData;

public class MenuPage extends BasePage<SyncData> {

    @Override
    public ViewType getViewType() {
        return ViewType.VIEW_MENU;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Button applyChanges = viewHolder.itemView.findViewById(R.id.applyChanges);
        Button refreshData = viewHolder.itemView.findViewById(R.id.refreshData);
        EditText lat = viewHolder.itemView.findViewById(R.id.latitude);
        EditText lng = viewHolder.itemView.findViewById(R.id.longitude);
        EditText timeInterval = viewHolder.itemView.findViewById(R.id.timeInterval);
        RadioGroup radioGroup = viewHolder.itemView.findViewById(R.id.units);

        lat.setText(getData().getLat());
        lng.setText(getData().getLng());
        timeInterval.setText(String.valueOf(getData().getTimeInterval()));

        applyChanges.setOnClickListener(v -> {
            if (getListener() instanceof MenuListener) {
                ((MenuListener) getListener()).onApplyChanges(lat.getText().toString(), lng.getText().toString(),
                        Integer.parseInt(timeInterval.getText().toString()), getSelectedMetric(viewHolder.itemView, radioGroup));
            }
        });

        refreshData.setOnClickListener(v -> {
            if (getListener() instanceof MenuListener) {
                ((MenuListener) getListener()).onRefreshData();
            }
        });
    }

    private Units getSelectedMetric(View view, RadioGroup radioGroup) {
        int index = radioGroup.indexOfChild(view.findViewById(radioGroup.getCheckedRadioButtonId()));
        if (index == 1) {
            return Units.STANDARD;
        } else if (index == 2) {
            return Units.IMPERIAL;
        } else {
            return Units.METRIC;
        }
    }

    public interface MenuListener extends BaseListener {
        void onApplyChanges(String lat, String lng, int timeInterval, Units units);
        void onRefreshData();
    }
}
