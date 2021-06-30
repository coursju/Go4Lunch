package com.coursju.go4lunch.base;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.authentification.AuthentificationActivity;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.viewmodel.Go4LunchViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


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
        getLocationPermission();
    }

    public abstract int getFragmentLayout();

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    public Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

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

    protected void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    1234);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1234: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startActivity(new Intent(this, MainActivity.class));
//                }
//            }
//        }
//    }
}