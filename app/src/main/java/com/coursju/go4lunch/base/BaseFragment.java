package com.coursju.go4lunch.base;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.PlaceRestaurantsCallback;
import com.coursju.go4lunch.utils.RestaurantListBuilder;
import com.coursju.go4lunch.viewmodel.Go4LunchViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment--";

    protected Go4LunchViewModel go4LunchViewModel;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected Boolean mLocationPermissionGranted = false;
    protected Boolean isFiltered = false;
    protected RestaurantListBuilder mRestaurantListBuilder;
    protected PlaceRestaurantsCallback placeRestaurantsCallback;
    protected MainActivity mMainActivity;

    protected void showRestaurants(List<Restaurant> restoList){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        configureCallback();
        mRestaurantListBuilder = new RestaurantListBuilder(getContext(), placeRestaurantsCallback);
    }

    @Override
    public void onResume() {
        getFirebaseWorkmateList();
        configureInputSearch();
        super.onResume();
    }

    protected void configureViewModel(){
        go4LunchViewModel = new ViewModelProvider(requireActivity()).get(Go4LunchViewModel.class);
    }

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

    public void getFirebaseWorkmateList(){
        WorkmateHelper.getWorkmatesCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                go4LunchViewModel.setWorkmateList(queryDocumentSnapshots.toObjects(Workmate.class));
            }
        });
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
    }

    public void searchByFilteringRestaurants(String query){
        Log.i(TAG, "searchByFilteringRestaurants: "+query);
        List<Restaurant> filteredRestaurantsList = new ArrayList();
        String toUpperQuery = query.toUpperCase();

        for (Restaurant restaurant: go4LunchViewModel.getRestaurantsList()){
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

        for (Restaurant restaurant: go4LunchViewModel.getRestaurantsList()){
            if (restaurant.getName().toUpperCase().contains(toUpperQuery)){
                filteredRestaurantsList.add(restaurant);
            }
        }
        return filteredRestaurantsList;
    }

}
