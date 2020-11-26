package com.coursju.go4lunch.modele;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;

import java.net.URL;
import java.util.List;

public class Restaurant {

    private String mName = null;
    private String mAddress = null;
    private String mKindOfFood = null;
    private PhotoMetadata mRestaurantPhotoMetadata = null;
    private List<Workmate> expectedWorkmates = null;
    private Uri mWebsite = null;
    private String mPhoneNumbers = null;
    private Integer mLikeRate = null;
    private String mID = null;
    private LatLng mLatLng = null;
    private OpeningHours mOpeningHours = null;
    private Boolean mIsOpen = false;
    private String mDistance = null;

    public Restaurant(String name, String address, PhotoMetadata restaurantPicture, Uri website,
                      String phoneNumbers, String ID, LatLng latLng, OpeningHours openingHours,
                      Boolean isOpen) {
        mName = name;
        mAddress = address;
        mRestaurantPhotoMetadata = restaurantPicture;
        mWebsite = website;
        mPhoneNumbers = phoneNumbers;
        mID = ID;
        mLatLng = latLng;
        mOpeningHours = openingHours;
        mIsOpen = isOpen;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getKindOfFood() {
        return mKindOfFood;
    }

    public void setKindOfFood(String kindOfFood) {
        mKindOfFood = kindOfFood;
    }

    public PhotoMetadata getRestaurantPicture() {
        return mRestaurantPhotoMetadata;
    }

    public void setRestaurantPicture(PhotoMetadata restaurantPicture) {
        mRestaurantPhotoMetadata = restaurantPicture;
    }

    public List<Workmate> getExpectedWorkmates() {
        return expectedWorkmates;
    }

    public void setExpectedWorkmates(List<Workmate> expectedWorkmates) {
        this.expectedWorkmates = expectedWorkmates;
    }

    public Uri getWebsite() {
        return mWebsite;
    }

    public void setWebsite(Uri website) {
        mWebsite = website;
    }

    public String getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    public Integer getLikeRate() {
        return mLikeRate;
    }

    public void setLikeRate(Integer likeRate) {
        mLikeRate = likeRate;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLating(LatLng latLng) {
        mLatLng = latLng;
    }

    public OpeningHours getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        mOpeningHours = openingHours;
    }

    public Boolean getOpen() {
        return mIsOpen;
    }

    public void setOpen(Boolean open) {
        mIsOpen = open;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "mName='" + mName + '\'' +
                '}';
    }
}
