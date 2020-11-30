package com.coursju.go4lunch.modele;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private String mName = null;
    private String mAddress = null;
    private String mKindOfFood = null;
    private String mRestaurantPhotoReference = null;
    private List<Workmate> expectedWorkmates = null;
    private String mWebsite = null;
    private String mPhoneNumbers = null;
    private Integer mLikeRate = null;
    private String mID = null;
    private LatLng mLatLng = null;
    private List<String> mOpeningHours = null;
    private Boolean mIsOpen = null;
    private String mDistance = null;
    private Bitmap mBitmap = null;

    public Restaurant(String name, String address, String restaurantPhotoReference, String website,
                      String phoneNumbers, String ID, LatLng latLng, String distance, List<String> openingHours,
                      Boolean isOpen){
        mName = name;
        mAddress = address;
        mRestaurantPhotoReference = restaurantPhotoReference;
        mWebsite = website;
        mPhoneNumbers = phoneNumbers;
        mID = ID;
        mLatLng = latLng;
        mDistance = distance;
        mOpeningHours = openingHours;
        mIsOpen = isOpen;
        expectedWorkmates = new ArrayList<>();
    }

    public String getRestaurantPhotoReference() {
        return mRestaurantPhotoReference;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getName() {
        return mName;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getKindOfFood() {
        return mKindOfFood;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public Integer getLikeRate() {
        return mLikeRate;
    }

    public String getID() {
        return mID;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public List<String> getOpeningHours() {
        return mOpeningHours;
    }

    public Boolean getOpen() {
        return mIsOpen;
    }

    public String getDistance() {
        return mDistance;
    }

    public List<Workmate> getExpectedWorkmates(){return expectedWorkmates;}

    @Override
    public String toString(){
        return mName;//+" "+mOpeningHours.toString();//mAddress+" "+mDistance+" "+mWebsite+mPhoneNumbers+" "+mLatLng.toString();
    }
}
