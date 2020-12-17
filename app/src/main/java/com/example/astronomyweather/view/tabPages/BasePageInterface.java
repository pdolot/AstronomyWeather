package com.example.astronomyweather.view.tabPages;

import androidx.recyclerview.widget.RecyclerView;

public interface BasePageInterface {
    ViewType getViewType();

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);
}
