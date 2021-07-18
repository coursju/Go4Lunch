package com.coursju.go4lunch.controler;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseActivity;

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

