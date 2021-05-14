package com.coursju.go4lunch.viewmodel;

import android.view.MenuItem;

import androidx.lifecycle.ViewModel;

import com.coursju.go4lunch.controler.MapsFragment;
import com.coursju.go4lunch.controler.RestaurantListFragment;
import com.coursju.go4lunch.controler.WorkmatesListFragment;
import com.coursju.go4lunch.modele.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Go4LunchViewModel extends ViewModel {

    private MapsFragment mMapsFragment;
    private RestaurantListFragment mRestaurantListFragment;
    private WorkmatesListFragment mWorkmatesListFragment;
    private Integer bottomNavItem = 0;
    private Boolean searchZoneVisible = false;
    private List<Restaurant> restaurantsList = new ArrayList<>();

    public MapsFragment getmMapsFragment() {
        return mMapsFragment;
    }

    public void setmMapsFragment(MapsFragment mMapsFragment) {
        this.mMapsFragment = mMapsFragment;
    }

    public RestaurantListFragment getmRestaurantListFragment() {
        return mRestaurantListFragment;
    }

    public void setmRestaurantListFragment(RestaurantListFragment mRestaurantListFragment) {
        this.mRestaurantListFragment = mRestaurantListFragment;
    }

    public WorkmatesListFragment getmWorkmatesListFragment() {
        return mWorkmatesListFragment;
    }

    public void setmWorkmatesListFragment(WorkmatesListFragment mWorkmatesListFragment) {
        this.mWorkmatesListFragment = mWorkmatesListFragment;
    }

    public Integer getBottomNavItem() {
        return bottomNavItem;
    }

    public void setBottomNavItem(Integer bottomNavItem) {
        this.bottomNavItem = bottomNavItem;
    }

    public Boolean getSearchZoneVisible() {
        return searchZoneVisible;
    }

    public void setSearchZoneVisible(Boolean searchZoneVisible) {
        this.searchZoneVisible = searchZoneVisible;
    }

    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }

    public void setRestaurantsList(List<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;
    }
}

