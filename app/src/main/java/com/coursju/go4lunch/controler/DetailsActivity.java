package com.coursju.go4lunch.controler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.FavoritesHelper;
import com.coursju.go4lunch.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends BaseActivity {

    private final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(findViewById(R.id.activity_details_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_details_frame, new DetailsFragment()).commit();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_details;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

