package com.coursju.go4lunch.utils;

import com.coursju.go4lunch.modele.Restaurant;

import java.util.List;

public interface Callback {

    public void onFinish(List<Restaurant> restaurantList);
}
