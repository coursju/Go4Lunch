package com.coursju.go4lunch.controler;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.base.BaseActivity;
import com.coursju.go4lunch.utils.Constants;
import com.coursju.go4lunch.utils.SignOutOrDeleteUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.delete_user_button) Button deleteUserButton;
    @BindView(R.id.change_radius_btn) ImageButton changeRadiusButton;
    @BindView(R.id.change_email_btn) ImageButton changeEmailButton;
    @BindView(R.id.change_name_btn) ImageButton changeNameButton;
    @BindView(R.id.change_password_btn) ImageButton changePasswordButton;
    @BindView(R.id.change_name_txt) EditText changeNameText;
    @BindView(R.id.change_email_txt) EditText changeEmailText;
    @BindView(R.id.change_password_txt) EditText changePasswordText;
    @BindView(R.id.change_radius_txt) EditText changeRadiusText;

    private Activity mActivity = this;
    private FirebaseUser userToModify;
    private UserProfileChangeRequest mUserProfileChangeRequest;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userToModify = FirebaseAuth.getInstance().getCurrentUser();
        setTitle(getResources().getString(R.string.title));
        configureDeleteButton();
        configureRadiusButton();
    }

    private void configureDeleteButton(){
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignOutOrDeleteUser(getApplicationContext(),mActivity).deleteUserFromFirebase();
            }
        });
    }

    private void configureRadiusButton(){
        changeRadiusButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Constants.RADIUS = changeRadiusText.getText().toString();
                changeRadiusButton.setForegroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
            }
        });
    }
}
