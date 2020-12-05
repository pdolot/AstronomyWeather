package com.example.astronomyweather.view;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.view.tabPages.BaseListener;
import com.example.astronomyweather.view.tabPages.BasePage;
import com.example.astronomyweather.view.tabPages.MenuPage;
import com.example.astronomyweather.view.tabPages.ViewType;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    private List<BasePage<? extends Object>> tabs = new ArrayList<>();
    private MenuPage.MenuListener menuListener;
    private int screenWidth = 0;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private boolean isDualPane = false;

    public void setDualPane(boolean dualPane) {
        isDualPane = dualPane;
    }

    public void setTabs(List<BasePage<? extends Object>> tabs) {
        this.tabs.clear();
        this.tabs.addAll(tabs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ((MainActivity) parent.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(ViewType.values()[viewType].getLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tabs.get(position).onBindViewHolder(holder, position);
        if (tabs.get(position) instanceof MenuPage) {
            tabs.get(position).setListener(menuListener);
        }

        if (isDualPane){
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.width = screenWidth / 2;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        if (tabs != null) {
            return tabs.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (tabs.get(position) != null) {
            return tabs.get(position).getViewType().ordinal();
        } else {
            return -1;
        }
    }

    public void setMenuListener(MenuPage.MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
