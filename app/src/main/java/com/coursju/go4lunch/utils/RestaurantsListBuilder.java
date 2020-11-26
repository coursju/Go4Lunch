package com.coursju.go4lunch.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.modele.Restaurant;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coursju.go4lunch.base.BaseFragment.mPlacesClient;

public class RestaurantsListBuilder {

    private String TAG = "--RestaurantsListBuilder--";
    int i;

    private Bitmap bitmap;

    public RestaurantsListBuilder() {
    }

    public List<Restaurant> restaurantsListBuilder(List<String> listIDResto){
        List<Restaurant> restoList = new ArrayList<>();

        i = 0;
        for (String resto : listIDResto){
// Specify the fields to return.
            final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
                                                                Place.Field.LAT_LNG,
                                                                Place.Field.ADDRESS,
                                                                Place.Field.OPENING_HOURS,
                                                                Place.Field.PHONE_NUMBER,
                                                                Place.Field.PHOTO_METADATAS,
                                                                Place.Field.WEBSITE_URI,
                                                                Place.Field.NAME);

// Construct a request object, passing the place ID and fields array.
            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(resto, placeFields);

            mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();

                restoList.add(new Restaurant(place.getName(),
                        /*place.getAddress()*/null, null, null,null, null, null, null, null));
                        //getBitmapFromMetadata(place.getPhotoMetadatas().get(0)),
//                        place.getWebsiteUri(),
//                        place.getPhoneNumber(),
//                        place.getId(),
//                        place.getLatLng(),
//                        place.getOpeningHours(),
//                        place.isOpen()));
                Log.i(TAG,i+" "+place.getName());
                i++;

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            });
        }
        Log.i(TAG,"building restaurants list: "+i+" item");

        return restoList;
    }


    public Bitmap getBitmapFromMetadata(PhotoMetadata photoMetadata){
        // Create a FetchPhotoRequest.
        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                //.setMaxWidth(500) // Optional.
                //.setMaxHeight(300) // Optional.
                .build();
        BaseFragment.mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
            bitmap = fetchPhotoResponse.getBitmap();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });
        return bitmap;
    }
}
