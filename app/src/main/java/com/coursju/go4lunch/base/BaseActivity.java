package com.coursju.go4lunch.base;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.FavoritesHelper;
import com.coursju.go4lunch.authentification.AuthentificationActivity;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.viewmodel.Go4LunchViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";
    protected Go4LunchViewModel go4LunchViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        go4LunchViewModel = new ViewModelProvider(this).get(Go4LunchViewModel.class);
        this.setContentView(this.getFragmentLayout());
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate");
    }

    public abstract int getFragmentLayout();

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    protected void launchAuthentification(){
        Intent intent = new Intent(this, AuthentificationActivity.class);
        startActivity(intent);
    }
}