package com.coursju.go4lunch.utils;

import android.app.Activity;
import android.content.Context;

import com.coursju.go4lunch.controler.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class SignOutOrDeleteUser {

    private Context mContext;
    private Activity mActivity;
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    public SignOutOrDeleteUser(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    public void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(mContext)
                .addOnSuccessListener(mActivity, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    public void deleteUserFromFirebase(){ // doesn't work actually
//        if (this.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .delete(mContext)
                    .addOnSuccessListener(mActivity, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
        //}
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case SIGN_OUT_TASK:
                        mActivity.finish();
                        break;
                    case DELETE_USER_TASK:
                        mActivity.finish();// doesn't work actually
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
