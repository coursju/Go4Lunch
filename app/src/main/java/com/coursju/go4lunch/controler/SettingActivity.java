package com.coursju.go4lunch.controler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseActivity;
import com.coursju.go4lunch.utils.SignOutOrDeleteUser;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.delete_user_button)
    Button deleteUserButton;

    private Activity mActivity = this;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureDeleteButton();
    }

    private void configureDeleteButton(){
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SignOutOrDeleteUser(getApplicationContext(),mActivity).deleteUserFromFirebase();
            }
        });
    }
}
