package com.coursju.go4lunch.controler;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.utils.Callback;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment = null;
    private static final float DEFAULT_ZOOM = 18f;
    private FloatingActionButton mapsFloatingButton;
    private String TAG = "--test_MapFragment--";

//    private static MapsFragment instance;
//
//    private MapsFragment(){}
//
//    public static MapsFragment getInstance(){
//        if (instance == null) {
//            instance = new MapsFragment();
//        }
//        return instance;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getLocationPermission();

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        return inflater.inflate(R.layout.fragment_maps, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        mapsFloatingButton = view.findViewById(R.id.maps_floating_button);
        mapsFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();
        getDeviceLocation();
        configureMarkersListeners();
        //mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try{
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, DEFAULT_ZOOM));
                            findNearbyRestaurants();

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Log.e(TAG, "Exception: %s", task.getException());
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mapsFloatingButton.setVisibility(View.VISIBLE);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mapsFloatingButton.setVisibility(View.GONE);

                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void findNearbyRestaurants(){
        mRestaurantListBuilder.buildRestaurantsList();
        mMainActivity.getProgressBar().setVisibility(View.VISIBLE);
        mMainActivity.getBottomNavigationView().setVisibility(View.INVISIBLE);
    }

    @Override
    protected void showRestaurants(List<Restaurant> restoList){
        Log.i(TAG,"showRestaurants-: "+restoList.toString());

        mMap.clear();
        for (Restaurant resto: restoList){
            Log.i(TAG,"showRestaurants loop: ");
            mMap.addMarker(new MarkerOptions().position(resto.getLatLng()).title(resto.getName()));
        }
    }

    private void configureMarkersListeners(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(),(String)marker.getTag(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

}