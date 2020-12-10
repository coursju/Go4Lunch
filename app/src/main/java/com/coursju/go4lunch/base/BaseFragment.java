package com.coursju.go4lunch.base;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.utils.Callback;
import com.coursju.go4lunch.utils.RestaurantListBuilder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class BaseFragment extends Fragment {

    private static final String TAG = "--BaseFragment--";
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    public static LatLng currentPosition;
    public static Location currentLocation;
    public static String radius = "200";
    protected Location mLastKnownLocation;
    public static PlacesClient mPlacesClient;
    protected final LatLng mDefaultLocation = new LatLng(-21.052633331, 55.2267300518);

    protected static List<Restaurant> restaurantsList = new ArrayList<>();

    protected static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    protected static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    protected Boolean mLocationPermissionGranted = false;

    protected RestaurantListBuilder mRestaurantListBuilder;
    protected Callback callback;

    public static double BOUNDS = 0.02;

    protected MainActivity mMainActivity;

    protected void showRestaurants(List<Restaurant> restoList){}

        //TODO find a solution for apiKey
    protected String apiKey = "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";//getActivity().getResources().getString(R.string.google_key);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
        placesInitialisation();
        configureInputSearch();
        configureCallback();
        mRestaurantListBuilder = new RestaurantListBuilder(getContext(),callback);
        mMainActivity = (MainActivity) getActivity();
    }

    private void placesInitialisation(){
        Places.initialize(getContext(), apiKey);
        mPlacesClient = Places.createClient(getContext());
    }

    private void configureInputSearch(){
        if (mMainActivity != null) {
            mMainActivity.getInputSearch().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchByFilteringRestaurants(s.toString());
                }
            });
        }
    }

    private void configureCallback(){
        callback = new Callback() {
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
        restaurantsList.clear();
        restaurantsList.addAll(mList);
    }

    public void searchByFilteringRestaurants(String query){
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



}
