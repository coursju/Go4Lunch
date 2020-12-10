package com.coursju.go4lunch.api;

import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class WorkmateHelper {
    private static final String COLLECTION_NAME = "workmates";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String workmatePicture, String workmateName, String workmateEmail, Restaurant mResto) {
        Workmate userToCreate = new Workmate(uid, workmatePicture, workmateName, workmateEmail, mResto );
        return WorkmateHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return WorkmateHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return WorkmateHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateRestaurant(Restaurant restaurant) {

        Constants.CURRENT_WORKMATE.setYourLunch(restaurant);
        Workmate workmate = Constants.CURRENT_WORKMATE;

        workmate.getYourLunch().setBitmap(null);
        workmate.getYourLunch().setOpeningHours(null);
        workmate.getYourLunch().setLatLng(null);

        return WorkmateHelper.getUsersCollection().document(Constants.CURRENT_USER.getUid()).set(workmate);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return WorkmateHelper.getUsersCollection().document(uid).delete();
    }

}
