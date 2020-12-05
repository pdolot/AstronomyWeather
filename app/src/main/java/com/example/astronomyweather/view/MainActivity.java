package com.example.astronomyweather.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.astronomyweather.R;
import com.example.astronomyweather.view.tabPages.CustomSnapHelper;
import com.example.astronomyweather.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private final TabNamesAdapter tabNamesAdapter = new TabNamesAdapter();
    private final TabAdapter tabAdapter = new TabAdapter();
    private RecyclerView tabNames;
    private RecyclerView tabs;
    private final SnapHelper snapHelper = new CustomSnapHelper();
    private ImageView menu;
    private TextView currentLatitude;
    private TextView currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        findViews();
        setTabNamesRecyclerView();
        setTabsRecyclerView();
        setListeners();
        bindViews();

        viewModel.tabNames.observe(this, tabs -> {
            tabNamesAdapter.setTabNames(tabs);
        });

        viewModel.tabs.observe(this, tabs -> {
            tabAdapter.setTabs(tabs);
            tabNamesAdapter.setCurrentTab(viewModel.adapterPosition, true);
        });

    }

    private void findViews() {
        tabNames = findViewById(R.id.tabNames);
        tabs = findViewById(R.id.tabs);
        menu = findViewById(R.id.menu);
        currentLatitude = findViewById(R.id.currentLatitude);
        currentLongitude = findViewById(R.id.currentLongitude);
    }

    private void setTabNamesRecyclerView() {
        tabNames.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        tabNames.setAdapter(tabNamesAdapter);
        tabNamesAdapter.setTabAdapterInterface((position, isClicked) -> {
            viewModel.adapterPosition = position;
            int tabWidth = getResources().getDimensionPixelSize(R.dimen.tab_width);
            int centerOfScreen = tabNames.getWidth() / 2 - tabWidth / 2;
            ((LinearLayoutManager) tabNames.getLayoutManager()).scrollToPositionWithOffset(position, centerOfScreen);

            if (isClicked)
                tabs.scrollToPosition(position);
        });

    }

    private void setTabsRecyclerView() {
        if (getResources().getBoolean(R.bool.isTablet)) {
            tabAdapter.setDualPane(true);
        } else {
            switch (getResources().getConfiguration().orientation) {
                case Configuration.ORIENTATION_PORTRAIT:

                    tabAdapter.setDualPane(false);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    tabAdapter.setDualPane(true);
                    break;
            }
        }
        tabs.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        tabs.setAdapter(tabAdapter);

        tabs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_DRAGGING) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        View snapView = snapHelper.findSnapView(layoutManager);
                        if (snapView != null) {
                            int position = layoutManager.getPosition(snapView);
                            tabNamesAdapter.setCurrentTab(position, false);
                        }
                    }
                }
            }
        });

        snapHelper.attachToRecyclerView(tabs);
    }

    private void setListeners() {
        menu.setOnClickListener(v -> {
            viewModel.addMenu();
        });

        tabAdapter.setMenuListener((lat, lng, timeInterval) -> {
            viewModel.lat = lat;
            viewModel.lng = lng;
            viewModel.timeInterval = timeInterval;
            viewModel.removeMenu();
            bindViews();
            viewModel.fetchAstronomyData();
        });
    }

    private void bindViews() {
        currentLatitude.setText(getString(R.string.current_latitude, viewModel.lat));
        currentLongitude.setText(getString(R.string.current_longitude, viewModel.lng));
    }

}