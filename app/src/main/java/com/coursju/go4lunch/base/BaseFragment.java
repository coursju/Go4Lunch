package com.coursju.go4lunch.base;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.utils.PlaceRestaurantsCallback;
import com.coursju.go4lunch.utils.RestaurantListBuilder;
import com.coursju.go4lunch.viewmodel.Go4LunchViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment--";

    protected Go4LunchViewModel go4LunchViewModel;

    protected FusedLocationProviderClient mFusedLocationProviderClient;
//    public static LatLng currentPosition;
    public static Location currentLocation;
//    protected Location mLastKnownLocation;
//    public PlacesClient mPlacesClient;

    protected static List<Restaurant> restaurantsList = new ArrayList<>();

    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    protected Boolean mLocationPermissionGranted = false;
    protected Boolean isFiltered = false;
    protected RestaurantListBuilder mRestaurantListBuilder;
    protected PlaceRestaurantsCallback placeRestaurantsCallback;

    public static double BOUNDS = 0.02;

    protected MainActivity mMainActivity;

    protected void showRestaurants(List<Restaurant> restoList){}

        //TODO find a solution for apiKey
    protected String apiKey = "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";//getActivity().getResources().getString(R.string.google_key);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);

        configureViewModel();
//        placesInitialisation();
        configureCallback();
        mRestaurantListBuilder = new RestaurantListBuilder(getContext(), placeRestaurantsCallback);
    }

    @Override
    public void onResume() {
        configureInputSearch();
        super.onResume();
    }

    protected void configureViewModel(){
        go4LunchViewModel = new ViewModelProvider(requireActivity()).get(Go4LunchViewModel.class);
    }

//    private void placesInitialisation(){
//        Places.initialize(getContext(), apiKey);
//        mPlacesClient = Places.createClient(getContext());
//    }

    private void configureInputSearch(){
        mMainActivity = (MainActivity) getActivity();
        if (mMainActivity != null) {
            mMainActivity.getInputSearch().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    isFiltered = true;
                    searchByFilteringRestaurants(s.toString());
                }
            });
        }
    }

    protected void inputSearchIsEmpty(){
        if (mMainActivity != null){
            if (!mMainActivity.getInputSearch().getText().toString().equals("")){
                searchByFilteringRestaurants((mMainActivity.getInputSearch().getText().toString()));
            }
        }
    }

    private void configureCallback(){
        placeRestaurantsCallback = new PlaceRestaurantsCallback() {
            @Override
            public void onFinish(List<Restaurant> restaurantList) {
                addToRestaurantsList(restaurantList);
                showRestaurants(restaurantList);
                if (mMainActivity != null) {
                    mMainActivity.getProgressBar().setVisibility(View.GONE);
                    mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
                }
            }
        };
    }

    protected void addToRestaurantsList(List<Restaurant> mList){
        go4LunchViewModel.setRestaurantsList(mList);
        restaurantsList.clear();
        restaurantsList.addAll(mList);
    }

    public void searchByFilteringRestaurants(String query){
        Log.i(TAG, "searchByFilteringRestaurants: "+query);
        List<Restaurant> filteredRestaurantsList = new ArrayList();
        String toUpperQuery = query.toUpperCase();

            for (Restaurant restaurant: restaurantsList){
                if (restaurant.getName().toUpperCase().contains(toUpperQuery)){
                    filteredRestaurantsList.add(restaurant);
                }
            }
            updateRestaurantsList(filteredRestaurantsList);
    }

    protected void updateRestaurantsList(List<Restaurant> filteredRestoList){
        showRestaurants(filteredRestoList);
    }

    public List<Restaurant> getFilteredRestaurantsList(String query){
        List<Restaurant> filteredRestaurantsList = new ArrayList();
        String toUpperQuery = query.toUpperCase();

        for (Restaurant restaurant: restaurantsList){
            if (restaurant.getName().toUpperCase().contains(toUpperQuery)){
                filteredRestaurantsList.add(restaurant);
            }
        }
        return filteredRestaurantsList;
    }

}
