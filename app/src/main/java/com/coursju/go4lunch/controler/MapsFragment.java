package com.coursju.go4lunch.controler;

import android.content.Intent;
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
import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback {
    private final String TAG = "MapFragment--";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FloatingActionButton mapsFloatingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getLocationPermission();
        updateConstantUser();
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        mapFragment.getMapAsync(this);
        return inflater.inflate(R.layout.fragment_maps, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        configureMapsFloatingButton(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        inputSearchIsEmpty();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"onMapReady");
        mMap = googleMap;
        updateLocationUI();
        getDeviceLocation();
        configureMarkersListeners();
    }

    private void configureMapsFloatingButton(View view){
        mapsFloatingButton = view.findViewById(R.id.maps_floating_button);
        mapsFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go4LunchViewModel.setRestaurantsList(new ArrayList<>());
                getDeviceLocation();
            }
        });
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
                            Constants.CURRENT_LOCATION = (Location) task.getResult();
                            LatLng currentPosition = new LatLng(Constants.CURRENT_LOCATION.getLatitude(), Constants.CURRENT_LOCATION.getLongitude());
                            if (go4LunchViewModel.getRestaurantsList().isEmpty()) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, Constants.DEFAULT_ZOOM));
                                findNearbyRestaurants();
                            }else{
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, Constants.DEFAULT_ZOOM));
                                showRestaurants(go4LunchViewModel.getRestaurantsList());
                            }

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
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        try {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mapsFloatingButton.setVisibility(View.VISIBLE);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected void getLocationPermission() {
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
        if (mMainActivity != null) {
            mMainActivity.getProgressBar().setVisibility(View.VISIBLE);
            mMainActivity.getBottomNavigationView().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void showRestaurants(List<Restaurant> restoList){
        Log.i(TAG,"showRestaurants-: "+restoList.toString());
        List<Restaurant> listOfRestaurants = restoList;
        if (mMainActivity != null){
            if (!mMainActivity.getInputSearch().getText().toString().equals("")
            && !isFiltered){
                listOfRestaurants = getFilteredRestaurantsList((mMainActivity.getInputSearch().getText().toString()));
                Log.i(TAG, "filter "+String.valueOf(isFiltered));
            }
        }
        if (mMap != null) {
            mMap.clear();

            for (Restaurant resto : listOfRestaurants) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(resto.getLatLng()).title(resto.getName());
                for (Workmate workmateResto : go4LunchViewModel.getWorkmateList()){
                    if (workmateResto.getYourLunch().getName() != null){
                        if (workmateResto.getYourLunch().getName().equals(resto.getName())){
                            markerOptions .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                    }
                }
                mMap.addMarker(markerOptions);
            }
            isFiltered = false;
        }
    }

    private void configureMarkersListeners(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (Restaurant resto : go4LunchViewModel.getRestaurantsList()){
                    if (resto.getName().equals(marker.getTitle())){
                        Constants.DETAILS_RESTAURANT = resto;
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
    }

    private void updateConstantUser(){
        Constants.CURRENT_USER = FirebaseAuth.getInstance().getCurrentUser();
        WorkmateHelper.getUser(Constants.CURRENT_USER.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Constants.CURRENT_WORKMATE = documentSnapshot.toObject(Workmate.class);
            }
        });
    }
}