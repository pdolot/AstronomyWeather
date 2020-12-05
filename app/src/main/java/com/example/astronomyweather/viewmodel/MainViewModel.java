package com.example.astronomyweather.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.astronomyweather.model.SyncData;
import com.example.astronomyweather.rest.RestRepository;
import com.example.astronomyweather.rest.RestService;
import com.example.astronomyweather.view.tabPages.BasePage;
import com.example.astronomyweather.view.tabPages.MenuPage;
import com.example.astronomyweather.view.tabPages.MoonPage;
import com.example.astronomyweather.view.tabPages.SunPage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {

    private RestRepository repository;
    public MutableLiveData<ArrayList<BasePage<?>>> tabs;
    public MutableLiveData<ArrayList<String>> tabNames;
    private CompositeDisposable compositeDisposable;
    private Disposable request;

    private static final String API_KEY = "c517372b13b44b3096a47f7d0c6f4a2b";

    public int timeInterval;
    public String lat;
    public String lng;

    public boolean menuExist = false;
    public int adapterPosition = 0;

    public MainViewModel() {
        repository = buildRestRepository();
        compositeDisposable = new CompositeDisposable();
        tabs = new MutableLiveData<>();
        tabNames = new MutableLiveData<>();

        timeInterval = 5;
        lat = "51.032334";
        lng = "18.340034";

        fetchAstronomyData();
    }

    public void addMenu() {
        if (!menuExist) {
            menuExist = true;
            ArrayList<String> tabNames = new ArrayList<>();
            tabNames.addAll(this.tabNames.getValue());
            tabNames.add("Menu");

            ArrayList<BasePage<?>> tabs = new ArrayList<>();
            tabs.addAll(this.tabs.getValue());

            MenuPage menuPage = new MenuPage();
            menuPage.setData(new SyncData(lat, lng, timeInterval));
            tabs.add(menuPage);
            adapterPosition = tabNames.size() - 1;
            this.tabNames.postValue(tabNames);
            this.tabs.postValue(tabs);
        }
    }

    public void removeMenu() {
        if (menuExist) {
            menuExist = false;

            ArrayList<String> tabNames = this.tabNames.getValue();
            tabNames.remove(tabNames.size() - 1);
            this.tabNames.postValue(tabNames);

            ArrayList<BasePage<?>> tabs = this.tabs.getValue();
            tabs.remove(tabs.size() - 1);
            this.tabs.postValue(tabs);
            if (adapterPosition - 1 < 0){
                adapterPosition = 0;
            }else{
                adapterPosition = adapterPosition - 1;
            }
        }
    }

    private RestRepository buildRestRepository() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ipgeolocation.io/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return new RestRepository(retrofit.create(RestService.class));
    }

    public void fetchAstronomyData() {
        if (request != null) {
            compositeDisposable.remove(request);
        }

        request = repository.getAstronomyInfo(API_KEY, lat, lng, timeInterval)
                .subscribe(data -> {
                    ArrayList<BasePage<?>> tabs = new ArrayList<>();
                    tabs.add(new SunPage(data.getSunData()));
                    tabs.add(new MoonPage(data.getMoonData()));

                    if (menuExist){
                        MenuPage menuPage = new MenuPage();
                        menuPage.setData(new SyncData(lat, lng, timeInterval));
                        tabs.add(menuPage);
                    }

                    ArrayList<String> tabNames = new ArrayList<>();
                    tabNames.add("Słońce");
                    tabNames.add("Księżyc");

                    if (menuExist){
                        tabNames.add("Menu");
                    }

                    this.tabNames.postValue(tabNames);
                    this.tabs.postValue(tabs);
                }, error -> {

                });

        compositeDisposable.add(request);
    }

}
