package com.coursju.go4lunch.modele;

import java.util.ArrayList;
import java.util.List;

public class Workmate {

    private String uid = "";
    private String mWorkmatePicture = "";
    private String mWorkmateName = "";
    private String mWorkmateFirstName = "";
    private String mWorkmateEmail = "";
    private Restaurant mYourLunch = null;
    private List<Restaurant> mLikedRestaurants = new ArrayList<>();//String
    //preferences, settings = notifications, distances...

    public Workmate (String uid, String workmateName, String workmatePicture){
        this.uid = uid;
        this.mWorkmateName = workmateName;
        this.mWorkmatePicture = workmatePicture;
    }
}
