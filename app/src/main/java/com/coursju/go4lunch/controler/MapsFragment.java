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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment = null;
    private static final float DEFAULT_ZOOM = 19f;
    private FloatingActionButton mapsFloatingButton;
    private String TAG = "--test--";




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
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        updateLocationUI();
        getDeviceLocation();
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
                            Location currentLocation = (Location) task.getResult();
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

    //**********************************

//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Use fields to define the data types to return.
//            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
//                    Place.Field.LAT_LNG);
//
//            // Use the builder to create a FindCurrentPlaceRequest.
//            FindCurrentPlaceRequest request =
//                    FindCurrentPlaceRequest.newInstance(placeFields);
//
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//            Task<FindCurrentPlaceResponse> placeResult =
//                    mPlacesClient.findCurrentPlace(request);
//            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
//                @Override
//                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        FindCurrentPlaceResponse likelyPlaces = task.getResult();
//
//                        // Set the count, handling cases where less than 5 entries are returned.
//                        int count;
//                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
//                            count = likelyPlaces.getPlaceLikelihoods().size();
//                        } else {
//                            count = M_MAX_ENTRIES;
//                        }
//
//                        int i = 0;
//                        mLikelyPlaceNames = new String[count];
//                        mLikelyPlaceAddresses = new String[count];
//                        mLikelyPlaceAttributions = new List[count];
//                        mLikelyPlaceLatLngs = new LatLng[count];
//
//                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
//                            // Build a list of likely places to show the user.
//                            mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
//                            mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
//                            mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
//                                    .getAttributions();
//                            mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                            i++;
//                            if (i > (count - 1)) {
//                                break;
//                            }
//                        }
//
//                        // Show a dialog offering the user the list of likely places, and add a
//                        // marker at the selected place.
//                        MapsActivityCurrentPlace.this.openPlacesDialog();
//                    }
//                    else {
//                        Log.e(TAG, "Exception: %s", task.getException());
//                    }
//                }
//            });
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }
//
    public void findNearbyRestaurants(){
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.TYPES,
                   Place.Field.LAT_LNG, Place.Field.ID);//Collections.singletonList(Place.Field.NAME);

// Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();

                    List<String> listIDresto = new ArrayList<>();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        if (placeLikelihood.getPlace().getTypes().contains(Place.Type.RESTAURANT)
                            || placeLikelihood.getPlace().getTypes().contains(Place.Type.BAR)
                            || placeLikelihood.getPlace().getTypes().contains(Place.Type.CAFE)
                            || placeLikelihood.getPlace().getTypes().contains(Place.Type.MEAL_TAKEAWAY)){

                            listIDresto.add(placeLikelihood.getPlace().getId());

                            Log.i(TAG, String.format("Place '%s' has ID: %s ",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getPlace().getId()));
                        }
                    }
                    addToRestaurantsIDList(listIDresto);
                    showRestaurants(restaurantsIDList);

                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            getLocationPermission();
        }

    }

    @Override
    protected void showRestaurants(List<String> restaurantsIDList){
        Log.i(TAG,"showRestaurants: "+restaurantsIDList.toString());
        mMap.clear();
        for (String resto : restaurantsIDList){
// Specify the fields to return.
            final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);

// Construct a request object, passing the place ID and fields array.
            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(resto, placeFields);

            mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
            mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName())).setTag(place.getId());
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            });
        }
    }

    @Override
    protected void updateRestaurantsIDList(List idList){
        restaurantsIDList.clear();
        restaurantsIDList.addAll(idList);
        showRestaurants(restaurantsIDList);
    }

}