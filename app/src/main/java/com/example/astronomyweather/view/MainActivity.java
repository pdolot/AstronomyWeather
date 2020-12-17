package com.example.astronomyweather.view;

import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.astronomyweather.ConnectivityReceiver;
import com.example.astronomyweather.R;
import com.example.astronomyweather.Units;
import com.example.astronomyweather.view.tabPages.CustomSnapHelper;
import com.example.astronomyweather.view.tabPages.MenuPage;
import com.example.astronomyweather.view.tabPages.locations.LocationsPage;
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
    private TextView errorMessage;
    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MainViewModel.class);
        viewModel.startObserve(this);

        findViews();
        setTabNamesRecyclerView();
        setTabsRecyclerView();
        setListeners();
        bindViews();


//        viewModel.tabNames.observe(this, tabs -> {
//            tabNamesAdapter.setTabNames(tabs);
//        });
//
//        viewModel.tabs.observe(this, tabs -> {
//            tabAdapter.setTabs(tabs);
//            tabNamesAdapter.setCurrentTab(viewModel.adapterPosition, true);
//        });

        viewModel.adapterPosition.observe(this, position -> {
            if (tabAdapter.currentPosition != position) {
                tabAdapter.currentPosition = position;
                tabs.scrollToPosition(tabAdapter.currentPosition);
            }

            if (tabNamesAdapter.currentPosition != position) {
                tabNamesAdapter.currentPosition = position;
                tabNamesAdapter.notifyDataSetChanged();
                scrollTabNamesAdapter(tabNamesAdapter.currentPosition);
            }
        });

        viewModel.pages.observe(this, pages -> {
            tabNamesAdapter.setTabNames(pages.second);
            tabAdapter.setTabs(pages.first);
        });

        viewModel.error.observe(this, error -> {
            if (error != null) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText(error);
            } else {
                errorMessage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectivityReceiver.connectivityReceiverListener = isConnected -> {
            viewModel.isInternetConnection.postValue(isConnected);
        };
    }

    private void findViews() {
        tabNames = findViewById(R.id.tabNames);
        tabs = findViewById(R.id.tabs);
        menu = findViewById(R.id.menu);
        errorMessage = findViewById(R.id.error);
        currentLatitude = findViewById(R.id.currentLatitude);
        currentLongitude = findViewById(R.id.currentLongitude);
    }

    private void setTabNamesRecyclerView() {
        tabNames.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        tabNames.setAdapter(tabNamesAdapter);
        tabNamesAdapter.setTabAdapterInterface((position) -> {
            viewModel.setAdapterPosition(position);
        });
    }

    private void scrollTabNamesAdapter(int position) {
        int tabWidth = getResources().getDimensionPixelSize(R.dimen.tab_width);
        int centerOfScreen = tabNames.getWidth() / 2 - tabWidth / 2;
        ((LinearLayoutManager) tabNames.getLayoutManager()).scrollToPositionWithOffset(position, centerOfScreen);
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
                            tabAdapter.currentPosition = position;
                            viewModel.setAdapterPosition(position);
                        }
                    }
                }
            }
        });

        snapHelper.attachToRecyclerView(tabs);
    }

    private void setListeners() {
        menu.setOnClickListener(v -> {
            viewModel.addMenuPage();
        });

        tabAdapter.setMenuListener(new MenuPage.MenuListener() {
            @Override
            public void onApplyChanges(String lat, String lng, int timeInterval, Units units) {
                viewModel.lat = lat;
                viewModel.lng = lng;
                viewModel.timeInterval = timeInterval;
                viewModel.removeMenuPage();
                viewModel.currentUnits = units;
                bindViews();
                viewModel.fetchAstronomyData();
                viewModel.viewModelState.postValue(MainViewModel.ViewModelState.CHANGED_UNITS);
            }

            @Override
            public void onRefreshData() {
                viewModel.viewModelState.postValue(MainViewModel.ViewModelState.REFRESH_DATA);
            }
        });

        tabAdapter.setLocationsListener(new LocationsPage.LocationsListener() {
            @Override
            public void onAddCity(String city) {
                viewModel.checkLocations(city);
            }

            @Override
            public void onChangeFilter(Boolean favorite) {
                viewModel.setFavoriteFilterEnable(favorite);
            }

            @Override
            public void onFavoriteChange(Long id, Boolean favorite) {
                viewModel.changeFavorite(id, favorite);
            }

            @Override
            public void onSetCurrent(Long id) {
                viewModel.setCurrentLocation(id);
            }
        });
    }

    private void bindViews() {
        currentLatitude.setText(getString(R.string.current_latitude, viewModel.lat));
        currentLongitude.setText(getString(R.string.current_longitude, viewModel.lng));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(connectivityReceiver);
        super.onDestroy();
    }
}