package com.coursju.go4lunch.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.coursju.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class BaseFragment extends Fragment {

    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected LatLng currentPosition;
    protected Location mLastKnownLocation;
    protected PlacesClient mPlacesClient;
    protected final LatLng mDefaultLocation = new LatLng(48.864716, 2.349014);

    protected static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    protected static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    protected static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    protected Boolean mLocationPermissionGranted = false;

    //TODO find a solution for apiKey
    protected String apiKey = "AIzaSyBM42q3bmSdlAnPGzGesADPLjRVD6KPLbk";//getActivity().getResources().getString(R.string.google_key);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placesInitialisation();

    }


    private void placesInitialisation(){
        Places.initialize(getContext(), apiKey);
        mPlacesClient = Places.createClient(getContext());
    }

}
