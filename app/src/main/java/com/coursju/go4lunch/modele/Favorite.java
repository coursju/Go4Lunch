package com.coursju.go4lunch.modele;

public class Favorite {

    private String uid;
    private Integer like = 0;

    public Favorite() {
    }

    public Favorite(String uid, Integer like) {
        this.uid = uid;
        this.like = like;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }
}
