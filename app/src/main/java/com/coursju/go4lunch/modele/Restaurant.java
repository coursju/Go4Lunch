package com.coursju.go4lunch.modele;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Restaurant {

    private String mName = null;
    private String mAddress = null;
    private String mKindOfFood = null;
    private String mRestaurantPhotoReference = null;
    private String mWebsite = null;
    private String mPhoneNumbers = null;
    private Integer mLikeRate = null;
    private String mID = null;
    @Exclude
    private LatLng mLatLng = null;
    @Exclude
    private List<String> mOpeningHours = null;
    private Boolean mIsOpen = null;
    private String mDistance = null;
    @Exclude
    private Bitmap mBitmap = null;

    public Restaurant() {
    }

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

    public void setName(String name) {
        mName = name;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setKindOfFood(String kindOfFood) {
        mKindOfFood = kindOfFood;
    }

    public void setRestaurantPhotoReference(String restaurantPhotoReference) {
        mRestaurantPhotoReference = restaurantPhotoReference;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    public void setLikeRate(Integer likeRate) {
        mLikeRate = likeRate;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public void setOpeningHours(List<String> openingHours) {
        mOpeningHours = openingHours;
    }

    public void setOpen(Boolean open) {
        mIsOpen = open;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    @Override
    public String toString(){
        return mName;
    }
}
