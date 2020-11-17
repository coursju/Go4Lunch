package com.coursju.go4lunch.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.controler.MainActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class BaseFragment extends Fragment {

    private static final String TAG = "--testbase--";
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected LatLng currentPosition;
    protected Location mLastKnownLocation;
    protected PlacesClient mPlacesClient;
    protected final LatLng mDefaultLocation = new LatLng(-21.052633331, 55.2267300518);

    protected List<String> restaurantsIDListBase = new ArrayList<>();
    protected List<String> restaurantsIDList = new ArrayList<>();

    protected static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    protected static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    protected Boolean mLocationPermissionGranted = false;

    public static double BOUNDS = 0.02;

    protected MainActivity mMainActivity;

    protected void showRestaurants(List<String> restaurantsIDList){}
    protected void updateRestaurantsIDList(List idList){}



        //TODO find a solution for apiKey
    protected String apiKey = "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";//getActivity().getResources().getString(R.string.google_key);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placesInitialisation();
        configureInputSearch();
    }


    private void placesInitialisation(){
        Places.initialize(getContext(), apiKey);
        mPlacesClient = Places.createClient(getContext());
    }

    private void configureInputSearch(){
        mMainActivity = (MainActivity)getActivity();
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

    protected void addToRestaurantsIDList(List<String> restaurantsID){
        restaurantsIDList.clear();
        restaurantsIDList.addAll(restaurantsID);

        restaurantsIDListBase.clear();
        restaurantsIDListBase.addAll(restaurantsID);
    }

    public void searchByFilteringRestaurants(String query){
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        //AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        List<String> idList = new ArrayList<>();
        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(currentPosition.latitude - BOUNDS, currentPosition.longitude - BOUNDS),
                new LatLng(currentPosition.latitude + BOUNDS, currentPosition.longitude + BOUNDS));

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                //.setLocationBias(bounds)
                .setLocationRestriction(bounds)
                .setOrigin(currentPosition)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .build();

        if (query.equals("")){
            updateRestaurantsIDList(restaurantsIDListBase);
        }else {
            mPlacesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    if (prediction.getPlaceTypes().contains(Place.Type.RESTAURANT)) {
                        Log.i(TAG, prediction.getPlaceId());
                        Log.i(TAG, prediction.getPrimaryText(null).toString());
                        idList.add(prediction.getPlaceId());
                        updateRestaurantsIDList(idList);
                    }
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                }
            });
        }

    }


}
