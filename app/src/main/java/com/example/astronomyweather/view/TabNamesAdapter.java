package com.example.astronomyweather.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astronomyweather.R;

import java.util.ArrayList;
import java.util.List;

public class TabNamesAdapter extends RecyclerView.Adapter<TabNamesAdapter.ViewHolder> {

    private List<String> tabNames = new ArrayList<>();
    private TabAdapterInterface tabAdapterInterface;
    public int currentPosition = 0;

    public void setTabNames(List<String> tabNames) {
        this.tabNames = tabNames;
        notifyDataSetChanged();
    }

    public void setTabAdapterInterface(TabAdapterInterface tabAdapterInterface) {
        this.tabAdapterInterface = tabAdapterInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView tabName = holder.itemView.findViewById(R.id.tabName);
        ConstraintLayout bg = holder.itemView.findViewById(R.id.activeBackground);
        tabName.setText(tabNames.get(position));

        if (position == currentPosition) {
            bg.setVisibility(View.VISIBLE);
        } else {
            bg.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            tabAdapterInterface.onTabSelected(position);
        });
    }

    @Override
    public int getItemCount() {
        if (tabNames != null) {
            return tabNames.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface TabAdapterInterface {
        void onTabSelected(int position);
    }
}
