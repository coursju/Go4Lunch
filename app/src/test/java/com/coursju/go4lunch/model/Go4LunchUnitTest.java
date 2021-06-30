package com.coursju.go4lunch.model;

import com.coursju.go4lunch.modele.Expected;
import com.coursju.go4lunch.modele.Favorite;
import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class Go4LunchUnitTest {

    @Test
    public void expectedTest(){
        Expected expected = new Expected("1", "test1", "http://test.com");
        assertTrue(expected.getUid() == "1");
        assertTrue(expected.getWorkmateName() == "test1");
        assertTrue(expected.getWorkmatePicture() == "http://test.com");
    }

    @Test
    public void favoriteTest(){
        Favorite favorite = new Favorite("test1", 20);
        assertTrue(favorite.getUid() == "test1");
        assertTrue(favorite.getLike() == 20);
    }

    @Test
    public void restaurantTest(){
        Restaurant restaurant = new Restaurant("le resto", "pas loin", "photo ref", "http://lesite.com",
                "1234", "1", new LatLng(1,2) , "20", new ArrayList<String>(),
                true);
        assertTrue(restaurant.getName() == "le resto");
        assertTrue(restaurant.getAddress() == "pas loin");
        assertTrue(restaurant.getRestaurantPhotoReference() == "photo ref");
        assertTrue(restaurant.getWebsite() == "http://lesite.com");
        assertTrue(restaurant.getPhoneNumbers() == "1234");
        assertTrue(restaurant.getID() == "1");
        assertTrue(restaurant.getLatLng() instanceof LatLng);
        assertTrue(restaurant.getDistance() == "20");
    }

    @Test
    public void workmateTest(){
        Workmate workmate = new Workmate("1", "photo", "lenom", "lenom@le.com", new Restaurant());
        assertTrue(workmate.getUid() == "1");
        assertTrue(workmate.getWorkmatePicture() == "photo");
        assertTrue(workmate.getWorkmateName() == "lenom");
        assertTrue(workmate.getWorkmateEmail() == "lenom@le.com");
        assertTrue(workmate.getYourLunch() instanceof Restaurant);
    }

}
