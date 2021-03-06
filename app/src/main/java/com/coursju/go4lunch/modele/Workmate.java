package com.coursju.go4lunch.modele;

public class Workmate {

    private String uid = "";
    private String mWorkmatePicture = "";
    private String mWorkmateName = "";
    private String mWorkmateEmail = "";
    private Restaurant mYourLunch = new Restaurant();

    public Workmate() {}

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
