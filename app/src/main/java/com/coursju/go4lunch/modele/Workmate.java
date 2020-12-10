package com.coursju.go4lunch.modele;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Workmate {

    private String uid = "";
    private String mWorkmatePicture = "";
    private String mWorkmateName = "";
//    private String mWorkmateFirstName = "";
    private String mWorkmateEmail = "";
    private Restaurant mYourLunch = new Restaurant();
//    private List<Restaurant> mLikedRestaurants = new ArrayList<>();//String
    //preferences, settings = notifications, distances...


    public Workmate() {
    }

    public Workmate(String uid, String workmatePicture, String workmateName, String workmateEmail, Restaurant yourLunch) {
        this.uid = uid;
        mWorkmatePicture = workmatePicture;
        mWorkmateName = workmateName;
        mWorkmateEmail = workmateEmail;
        mYourLunch = yourLunch;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWorkmatePicture() {
        return mWorkmatePicture;
    }

    public void setWorkmatePicture(String workmatePicture) {
        mWorkmatePicture = workmatePicture;
    }

    public String getWorkmateName() {
        return mWorkmateName;
    }

    public void setWorkmateName(String workmateName) {
        mWorkmateName = workmateName;
    }

    public String getWorkmateEmail() {
        return mWorkmateEmail;
    }

    public void setWorkmateEmail(String workmateEmail) {
        mWorkmateEmail = workmateEmail;
    }

    public Restaurant getYourLunch() {
        return mYourLunch;
    }

    public void setYourLunch(Restaurant yourLunch) {
        mYourLunch = yourLunch;
    }
}
