package com.coursju.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FavoritesHelper {

    private static final String COLLECTION_NAME = "favorites";

    public static CollectionReference getFavoritesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createFavorite(String uid, String restaurantName, Boolean like) {
        Map<String, Boolean> map = new HashMap<>();
        map.put(uid, like);
        return FavoritesHelper
                .getFavoritesCollection()
                .document(restaurantName)
                .set(map, SetOptions.merge());
    }
}
