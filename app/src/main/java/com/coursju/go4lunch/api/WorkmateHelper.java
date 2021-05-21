package com.coursju.go4lunch.api;

import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WorkmateHelper {
    private static final String COLLECTION_NAME = "workmates";

    public static CollectionReference getWorkmatesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createWorkmate(String uid, String workmatePicture, String workmateName, String workmateEmail, Restaurant mResto) {
        Workmate workmateToCreate = new Workmate(uid, workmatePicture, workmateName, workmateEmail, mResto );
        return WorkmateHelper.getWorkmatesCollection().document(uid).set(workmateToCreate);
    }

    public static Task<DocumentSnapshot> getWorkmate(String uid){
        return WorkmateHelper.getWorkmatesCollection().document(uid).get();
    }

    public static Task<Void> updateWorkmateFromSetting() {
        return WorkmateHelper.getWorkmatesCollection()
                .document(Constants.CURRENT_WORKMATE.getUid())
                .set(Constants.CURRENT_WORKMATE);
    }

    public static Task<Void> updateRestaurant(Restaurant restaurant) {

        Constants.CURRENT_WORKMATE.setYourLunch(restaurant);
        Workmate workmate = Constants.CURRENT_WORKMATE;

        workmate.getYourLunch().setBitmap(null);
        workmate.getYourLunch().setOpeningHours(null);
        workmate.getYourLunch().setLatLng(null);

        return WorkmateHelper.getWorkmatesCollection().document(Constants.CURRENT_USER.getUid()).set(workmate);
    }

    public static Task<Void> deleteWorkmate(String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).delete();
    }

}
