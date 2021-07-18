package com.coursju.go4lunch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.coursju.go4lunch.api.ExpectedHelper;
import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.authentification.AuthentificationActivity;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Workmate;
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

    public void deleteUserFromFirebase(){
            AuthUI.getInstance()
                    .delete(mContext)
                    .addOnSuccessListener(mActivity, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
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
                        Workmate w = Constants.CURRENT_WORKMATE;
                        WorkmateHelper.deleteWorkmate(w.getUid());
                        if (Constants.CURRENT_WORKMATE.getYourLunch().getName() != null){
                            ExpectedHelper.deleteExpected(w.getUid(), w.getYourLunch().getName());
                        }
                        Intent intent = new Intent(mContext, AuthentificationActivity.class);
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
