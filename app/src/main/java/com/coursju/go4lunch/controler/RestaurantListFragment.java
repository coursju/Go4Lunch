package com.coursju.go4lunch.controler;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.adapter.MyRestaurantListRecyclerViewAdapter;
import com.coursju.go4lunch.base.BaseFragment;
import com.coursju.go4lunch.controler.dummy.DummyContent;
import com.coursju.go4lunch.modele.Restaurant;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantListFragment extends BaseFragment {

    int i;
    private String TAG = "--RestaurantsListFragment--";
    public static List<Bitmap> bitmapList = new ArrayList();
    private RecyclerView recyclerView;
//    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
    public RestaurantListFragment() {
    }

//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static RestaurantListFragment newInstance(int columnCount) {
//        RestaurantListFragment fragment = new RestaurantListFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            showRestaurants(restaurantsIDList);
        }
        return view;
    }

    public void restaurantsListBuilder(List<String> listIDResto){
        //Log.i(TAG,"building : "+listIDResto.toString());
        restaurantsList.clear();
        i = 0;

        final List<Place.Field> placeFields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS,
                    Place.Field.OPENING_HOURS,
                    Place.Field.PHONE_NUMBER,
                    Place.Field.PHOTO_METADATAS,
                    Place.Field.WEBSITE_URI,
                    Place.Field.NAME);

        for (String resto : listIDResto) {

            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(resto, placeFields);
            mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                        Place place = response.getPlace();
                        // (place.getPhotoMetadatas().get(0) != null) {
                           // getBitmapFromMetadata(place.getPhotoMetadatas().get(0));
                        //}
                        String name = place.getName();
                        String address = place.getAddress();
                        Restaurant r = new Restaurant(name,
                               address, //null, //null, null, null, null, null, null);
                        //getBitmapFromMetadata(place.getPhotoMetadatas().get(0)),
                                (place.getPhotoMetadatas() == null) ? null: place.getPhotoMetadatas().get(0),
                        place.getWebsiteUri(),
                        place.getPhoneNumber(),
                        place.getId(),
                        place.getLatLng(),
                        place.getOpeningHours(),
                        place.isOpen());
                        updateRecyclerView(r);
                        Log.i(TAG, i + " " + place.getName()+" "+place.getAddress()+"\n "+place.getPhoneNumber());//+" "+place.getOpeningHours());
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
    }

    private void updateRecyclerView(Restaurant r){
        restaurantsList.add(r);
        recyclerView.setAdapter(new MyRestaurantListRecyclerViewAdapter(restaurantsList));
    }

//    public static Bitmap getBitmapFromMetadata(PhotoMetadata photoMetadata){
//        // Create a FetchPhotoRequest.
//        final Bitmap[] bitmap = new Bitmap[1];
//
//        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
//                //.setMaxWidth(300) // Optional.
//                //.setMaxHeight(300) // Optional.
//                .build();
//        BaseFragment.mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
//            bitmap[0] =  fetchPhotoResponse.getBitmap();
//            Log.i("--fetch bitmap--", " "+bitmap[0]);
//
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                final ApiException apiException = (ApiException) exception;
//                Log.e("--fetch", "Place not found: " + exception.getMessage());
//                final int statusCode = apiException.getStatusCode();
//                // TODO: Handle error with given status code.
//            }
//        });
//        return bitmap[0];
//    }

    @Override
    protected void showRestaurants(List<String> restoList){
         restaurantsListBuilder(restoList);
    }

}