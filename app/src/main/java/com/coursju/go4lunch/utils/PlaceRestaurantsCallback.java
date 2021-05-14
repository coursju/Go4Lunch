package com.coursju.go4lunch.utils;

import com.coursju.go4lunch.modele.Restaurant;

import java.util.List;

public interface PlaceRestaurantsCallback {

    public void onFinish(List<Restaurant> restaurantList);
}
