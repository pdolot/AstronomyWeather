package com.example.astronomyweather.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.astronomyweather.Units;
import com.example.astronomyweather.db.repository.DailyWeatherRepository;
import com.example.astronomyweather.db.repository.LocationRepository;
import com.example.astronomyweather.db.repository.WeatherRepository;
import com.example.astronomyweather.model.SyncData;
import com.example.astronomyweather.model.weather.LocationWeather;
import com.example.astronomyweather.model.weather.LocationWeatherResponse;
import com.example.astronomyweather.rest.AstronomyRestRepository;
import com.example.astronomyweather.rest.AstronomyRestService;
import com.example.astronomyweather.rest.WeatherRestRepository;
import com.example.astronomyweather.rest.WeatherRestService;
import com.example.astronomyweather.view.tabPages.BasePage;
import com.example.astronomyweather.view.tabPages.MenuPage;
import com.example.astronomyweather.view.tabPages.MoonPage;
import com.example.astronomyweather.view.tabPages.SunPage;
import com.example.astronomyweather.view.tabPages.WeatherPage;
import com.example.astronomyweather.view.tabPages.WeatherWeeklyPage;
import com.example.astronomyweather.view.tabPages.locations.LocationsPage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private static final String API_KEY = "c517372b13b44b3096a47f7d0c6f4a2b";
    private static final String OPEN_WEATHER_API_KEY = "3bf993b129060a4cb85b2d8ab6020708";

    private final CompositeDisposable compositeDisposable;
    private Disposable request;

    public int timeInterval;
    public String lat;
    public String lng;

    private Boolean favoriteFilterEnable = true;
    //  private MutableLiveData<LocationWeather> currentLocation = new MutableLiveData<>();
    public MutableLiveData<ViewModelState> viewModelState = new MutableLiveData<>();
    private Long currentLocationId = -1L;
    private Long oldLocationId = -1L;
    private LocationWeather currentLocation;
    private List<LocationWeather> locations;
    private Boolean currentLocationWasChanged = false;

    private LocationWeatherResponse weatherResponse;
    private LocationWeather locationResponse;

    public Units currentUnits = Units.METRIC;

    private boolean menuExist = false;
    private int previousAdapterPosition = -1;
    public MutableLiveData<Integer> adapterPosition = new MutableLiveData<>();
    public MutableLiveData<Pair<List<BasePage<?>>, List<String>>> pages = new MutableLiveData<>();
    public MutableLiveData<Boolean> isInternetConnection = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    // rest repositories
    private final AstronomyRestRepository astronomyRestRepository = buildAstronomyRestRepository();
    private final WeatherRestRepository weatherRestRepository = buildWeatherRestRepository();

    // db repositories
    private final LocationRepository locationRepository = new LocationRepository(getApplication());
    private final WeatherRepository weatherRepository = new WeatherRepository(getApplication());
    private final DailyWeatherRepository dailyWeatherRepository = new DailyWeatherRepository(getApplication());

    // pages
    private Pair<String, LocationsPage> locationsPage = null;
    private Pair<String, WeatherPage> currentWeatherPage = null;
    private Pair<String, WeatherWeeklyPage> weeklyWeatherPage = null;
    private Pair<String, MoonPage> moonPage = null;
    private Pair<String, SunPage> sunPage = null;
    private Pair<String, MenuPage> menuPage = null;

    public void invalidatePages() {
        ArrayList<String> tabNames = new ArrayList<>();
        ArrayList<BasePage<?>> tabs = new ArrayList<>();

        if (locationsPage != null) {
            tabNames.add(locationsPage.first);
            tabs.add(locationsPage.second);
        }

        if (currentWeatherPage != null) {
            tabNames.add(currentWeatherPage.first);
            tabs.add(currentWeatherPage.second);
        }

        if (weeklyWeatherPage != null) {
            tabNames.add(weeklyWeatherPage.first);
            tabs.add(weeklyWeatherPage.second);
        }

        if (sunPage != null) {
            tabNames.add(sunPage.first);
            tabs.add(sunPage.second);
        }

        if (moonPage != null) {
            tabNames.add(moonPage.first);
            tabs.add(moonPage.second);
        }

        if (menuPage != null) {
            tabNames.add(menuPage.first);
            tabs.add(menuPage.second);
        }

        this.pages.postValue(new Pair<>(tabs, tabNames));
    }

    public MainViewModel(Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        adapterPosition.setValue(0);
        timeInterval = 5;
        lat = "51.032334";
        lng = "18.340034";

        getCurrentLocation();
    }

    public void startObserve(LifecycleOwner owner) {

        viewModelState.observe(owner, state -> {
            switch (state) {
                case NEW_LOCATION_IS_CORRECT:
                    insertNewLocation();
                    break;
                case INSERTED_NEW_LOCATION:
                    changeCurrentLocation();
                    break;
                case CHANGED_CURRENT_LOCATION:
                    currentLocationWasChanged = true;
                    currentLocation.setUnits(currentUnits);
                    getLocations();
                    break;
                case CHANGED_FAVORITE_FILTER:
                case CHANGED_FAVORITE_LOCATION:
                    currentLocationWasChanged = false;
                    getLocations();
                    break;
                case FETCHED_LOCATIONS_FROM_DB:
                    addLocationPage();
                    break;
                case LOCATION_PAGE_ADDED:
                    if (currentLocationWasChanged)
                        fetchWeather();
                    break;
                case CHANGED_UNITS:
                    currentLocation.setUnits(currentUnits);
                    fetchWeather();
                    break;
                case REFRESH_DATA:
                    fetchWeather();
                    fetchAstronomyData();
                    break;
                case FETCHED_WEATHER:
                    deleteCurrentWeather();
                    break;
                case DELETED_CURRENT_WEATHER:
                    deleteDailyWeather();
                    break;
                case DELETED_WEEKLY_WEATHER:
                    insertCurrentWeather();
                    break;
                case INSERTED_NEW_CURRENT_WEATHER:
                    insertDailyWeather();
                    break;
                case INSERTED_NEW_WEEKLY_WEATHER:
                    getCurrentWeather();
                    break;
                case FETCHED_CURRENT_WEATHER_FROM_DB:
                    getWeeklyWeather();
                    break;
                case FETCHED_WEEKLY_WEATHER_FROM_DB:
                    break;
                case MENU_PAGE_ADDED:
                    // move adapter to menu page
                    adapterPosition.postValue(pages.getValue().first.size() - 1);
                    break;
            }
        });

        isInternetConnection.observe(owner, isConnected -> {
            if (!isConnected) {
                error.postValue("Brak połączenia z internetem");
                getCurrentWeather();
                getWeeklyWeather();
            } else {
                error.postValue(null);
                fetchAstronomyData();
            }
        });
    }

    private void getCurrentWeather() {
        compositeDisposable.add(weatherRepository.getCurrentWeather(currentLocationId)
                .subscribe(weather -> {
                    currentWeatherPage = new Pair<>("Pogoda", new WeatherPage(new Pair<>(currentLocation, weather)));
                    invalidatePages();
                    viewModelState.postValue(ViewModelState.FETCHED_CURRENT_WEATHER_FROM_DB);
                }, error -> {
                    currentWeatherPage = null;
                    invalidatePages();
                    viewModelState.postValue(ViewModelState.FETCHED_CURRENT_WEATHER_FROM_DB);
                    Log.e(TAG, error.getLocalizedMessage());
                }));
    }

    private void getWeeklyWeather() {
        compositeDisposable.add(dailyWeatherRepository.getWeatherForNextSevenDays(currentLocationId)
                .subscribe(weather -> {
                    if (weather.isEmpty()) {
                        weeklyWeatherPage = null;
                    } else {
                        weeklyWeatherPage = new Pair<>("Prognoza", new WeatherWeeklyPage(new Pair<>(currentLocation, weather)));
                    }
                    invalidatePages();
                    viewModelState.postValue(ViewModelState.FETCHED_WEEKLY_WEATHER_FROM_DB);
                }, error -> {
                    weeklyWeatherPage = null;
                    invalidatePages();
                    viewModelState.postValue(ViewModelState.FETCHED_WEEKLY_WEATHER_FROM_DB);
                    Log.e(TAG, error.getLocalizedMessage());
                }));
    }

    private void getCurrentLocation() {
        compositeDisposable.add(locationRepository.getCurrent()
                .subscribe(locationWeather -> {
                    this.currentLocation = locationWeather;
                    this.currentLocationId = locationWeather.getId();
                    viewModelState.postValue(ViewModelState.CHANGED_CURRENT_LOCATION);
                }, error -> {
                    addLocationPage();
                    Log.e(TAG, error.getLocalizedMessage());
                }));
    }

    public void setAdapterPosition(int position) {
        previousAdapterPosition = adapterPosition.getValue();
        adapterPosition.postValue(position);
    }

    public void addMenuPage() {
        if (!menuExist) {
            menuExist = true;
            MenuPage menuPage = new MenuPage();
            menuPage.setData(new SyncData(lat, lng, timeInterval, currentUnits));
            this.menuPage = new Pair<>("Menu", menuPage);

            invalidatePages();
        }
        previousAdapterPosition = adapterPosition.getValue();
        viewModelState.postValue(ViewModelState.MENU_PAGE_ADDED);
    }

    public void removeMenuPage() {
        if (menuExist) {
            menuExist = false;
            this.menuPage = null;

            invalidatePages();
            adapterPosition.postValue(previousAdapterPosition);
            viewModelState.postValue(ViewModelState.MENU_PAGE_REMOVED);
        }
    }

    private AstronomyRestRepository buildAstronomyRestRepository() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ipgeolocation.io/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return new AstronomyRestRepository(retrofit.create(AstronomyRestService.class));
    }

    private WeatherRestRepository buildWeatherRestRepository() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return new WeatherRestRepository(retrofit.create(WeatherRestService.class));
    }

    private void getLocations() {
        compositeDisposable.add(locationRepository.getLocations(favoriteFilterEnable)
                .subscribe(locationWeathers -> {
                    locations = locationWeathers;
                    viewModelState.postValue(ViewModelState.FETCHED_LOCATIONS_FROM_DB);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    public void fetchAstronomyData() {
        if (request != null) {
            compositeDisposable.remove(request);
        }
        request = astronomyRestRepository.getAstronomyInfo(API_KEY, lat, lng, timeInterval)
                .subscribe(data -> {
                    this.sunPage = new Pair<>("Słońce", new SunPage(data.getSunData()));
                    this.moonPage = new Pair<>("Księżyc", new MoonPage(data.getMoonData()));

                    invalidatePages();
                }, error -> {
                    Log.e(TAG, error.getMessage());
                });

        compositeDisposable.add(request);
    }

    public void changeFavorite(Long id, Boolean isFavorite) {
        compositeDisposable.add(locationRepository.setFavorite(id, isFavorite)
                .subscribe(onSuccess -> {
                    viewModelState.postValue(ViewModelState.CHANGED_FAVORITE_LOCATION);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    public void setFavoriteFilterEnable(boolean favoriteFilterEnable) {
        this.favoriteFilterEnable = favoriteFilterEnable;
        viewModelState.postValue(ViewModelState.CHANGED_FAVORITE_FILTER);
    }

    private void addLocationPage() {
        this.locationsPage = new Pair<>("Lokalizacje", new LocationsPage(favoriteFilterEnable, locations));
        invalidatePages();
        viewModelState.postValue(ViewModelState.LOCATION_PAGE_ADDED);
    }

    public void setCurrentLocation(Long currentLocation) {
        this.oldLocationId = this.currentLocationId;
        this.currentLocationId = currentLocation;
        changeCurrentLocation();
    }

    private void changeCurrentLocation() {
        if (oldLocationId == -1L) {
            compositeDisposable.add(
                    locationRepository.getLocationById(currentLocationId)
                            .subscribe(location -> {
                                this.currentLocationId = location.getId();
                                this.currentLocation = location;
                                compositeDisposable.add(locationRepository.setCurrent(currentLocationId, true)
                                        .subscribe(onSuccessChange -> {
                                            viewModelState.postValue(ViewModelState.CHANGED_CURRENT_LOCATION);
                                        }, error -> {
                                            Log.e(TAG, error.getMessage());
                                        }));
                            }));
        } else {
            compositeDisposable.add(locationRepository.setCurrent(oldLocationId, false)
                    .subscribe(onSuccess -> {
                        compositeDisposable.add(
                                locationRepository.getLocationById(currentLocationId)
                                        .subscribe(location -> {
                                            this.currentLocationId = location.getId();
                                            this.currentLocation = location;
                                            compositeDisposable.add(locationRepository.setCurrent(currentLocationId, true)
                                                    .subscribe(onSuccessChange -> {
                                                        viewModelState.postValue(ViewModelState.CHANGED_CURRENT_LOCATION);
                                                    }, error -> {
                                                        Log.e(TAG, error.getMessage());
                                                    }));
                                        })
                        );
                    }, error -> {
                        Log.e(TAG, error.getMessage());
                    }));
        }

    }

    public void checkLocations(String city) {
        compositeDisposable.add(weatherRestRepository.getCurrentWeather(city.trim(), OPEN_WEATHER_API_KEY)
                .subscribe(data -> {
                    error.postValue(null);
                    locationResponse = data;
                    viewModelState.postValue(ViewModelState.NEW_LOCATION_IS_CORRECT);
                }, error -> {
                    this.error.postValue("Błędna lokalizacja");
                    Log.e(TAG, error.getMessage());
                }));
    }

    private void insertNewLocation() {
        compositeDisposable.add(locationRepository.insert(locationResponse)
                .subscribe(id -> {
                    this.oldLocationId = this.currentLocationId;
                    this.currentLocationId = id;
                    viewModelState.postValue(ViewModelState.INSERTED_NEW_LOCATION);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    private void fetchWeather() {
        if (currentLocation != null) {
            compositeDisposable.add(
                    weatherRestRepository.getCurrentWeather(currentLocation, OPEN_WEATHER_API_KEY, currentUnits.name().toLowerCase())
                            .subscribe(data -> {
                                weatherResponse = data;
                                viewModelState.postValue(ViewModelState.FETCHED_WEATHER);
                            }, error -> {
                                Log.e(TAG, error.getMessage());
                            })
            );
        }
    }

    private void insertCurrentWeather() {
        compositeDisposable.add(weatherRepository.insert(weatherResponse.getCurrent())
                .subscribe(weatherId -> {
                    viewModelState.postValue(ViewModelState.INSERTED_NEW_CURRENT_WEATHER);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    private void deleteCurrentWeather() {
        compositeDisposable.add(weatherRepository.delete()
                .subscribe(onSuccess -> {
                    viewModelState.postValue(ViewModelState.DELETED_CURRENT_WEATHER);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    private void insertDailyWeather() {
        compositeDisposable.add(dailyWeatherRepository.insert(weatherResponse.getDaily())
                .subscribe(onSuccess -> {
                    viewModelState.postValue(ViewModelState.INSERTED_NEW_WEEKLY_WEATHER);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    private void deleteDailyWeather() {
        compositeDisposable.add(dailyWeatherRepository.delete()
                .subscribe(onSuccess -> {
                    viewModelState.postValue(ViewModelState.DELETED_WEEKLY_WEATHER);
                }, error -> {
                    Log.e(TAG, error.getMessage());
                }));
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public enum ViewModelState {
        INSERTED_NEW_LOCATION,
        NEW_LOCATION_IS_CORRECT,

        CHANGED_CURRENT_LOCATION,
        CHANGED_FAVORITE_LOCATION,
        CHANGED_FAVORITE_FILTER,
        FETCHED_LOCATIONS_FROM_DB,

        INSERTED_NEW_CURRENT_WEATHER,
        DELETED_CURRENT_WEATHER,
        FETCHED_CURRENT_WEATHER_FROM_DB,

        DELETED_WEEKLY_WEATHER,
        INSERTED_NEW_WEEKLY_WEATHER,
        FETCHED_WEEKLY_WEATHER_FROM_DB,

        FETCHED_WEATHER,

        LOCATION_PAGE_ADDED,
        REFRESH_DATA,
        CHANGED_UNITS,

        MENU_PAGE_ADDED,
        MENU_PAGE_REMOVED
    }
}
