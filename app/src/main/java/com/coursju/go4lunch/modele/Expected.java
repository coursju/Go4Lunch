package com.coursju.go4lunch.modele;

public class Expected {

    private String uid;
    private String workmateName;
    private String workmatePicture;

    public Expected() {
    }

    public Expected(String uid, String workmateName, String workmatePicture) {
        this.workmatePicture = workmatePicture;
        this.workmateName = workmateName;
        this.uid = uid;
    }

    public String getWorkmatePicture() {
        return workmatePicture;
    }

    public void setWorkmatePicture(String workmatePicture) {
        this.workmatePicture = workmatePicture;
    }

    public String getWorkmateName() {
        return workmateName;
    }

    public void setWorkmateName(String workmateName) {
        this.workmateName = workmateName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
