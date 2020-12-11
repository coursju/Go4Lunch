package com.coursju.go4lunch.api;

import com.coursju.go4lunch.modele.Expected;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExpectedHelper {

    private static final String COLLECTION_NAME = "expected";

    public static CollectionReference getExpectedsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createExpected(String uid, String restaurant, String workmateName, String workmatePicture) {
        Expected expectedToCreate = new Expected(uid, workmateName, workmatePicture);
        return ExpectedHelper.getExpectedsCollection().document(restaurant).collection("coming").document(uid).set(expectedToCreate);
    }

    public static Task<Void> deleteExpected(String uid, String restaurant) {
        return ExpectedHelper.getExpectedsCollection().document(restaurant).collection("coming").document(uid).delete();
    }

    public static CollectionReference getExpectedRestaurant(String restaurantName){
        return ExpectedHelper.getExpectedsCollection().document(restaurantName).collection("coming");
    }
}
