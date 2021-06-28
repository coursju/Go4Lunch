package com.coursju.go4lunch.controler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.ExpectedHelper;
import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.authentification.AuthentificationActivity;
import com.coursju.go4lunch.base.BaseActivity;
import com.coursju.go4lunch.modele.Expected;
import com.coursju.go4lunch.modele.Workmate;
import com.coursju.go4lunch.utils.Constants;
import com.coursju.go4lunch.utils.SignOutOrDeleteUser;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";
    private final static int TAKE_PICTURE = 1;

    @BindView(R.id.change_picture_btn) Button changePictureButton;
    @BindView(R.id.setting_progressBar) ProgressBar settingProgressBar;
    @BindView(R.id.setting_picture_thumbnail) ImageView settingPictureThumbnail;
    @BindView(R.id.delete_user_button) Button deleteUserButton;
    @BindView(R.id.change_radius_btn) ImageButton changeRadiusButton;
    @BindView(R.id.change_email_btn) ImageButton changeEmailButton;
    @BindView(R.id.change_name_btn) ImageButton changeNameButton;
    @BindView(R.id.change_password_btn) ImageButton changePasswordButton;
    @BindView(R.id.change_name_txt) EditText changeNameText;
    @BindView(R.id.change_email_txt) EditText changeEmailText;
    @BindView(R.id.change_password_txt) EditText changePasswordText;
    @BindView(R.id.change_radius_txt) EditText changeRadiusText;
    @BindView(R.id.change_picture_txt) EditText changePictureText;

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
        setTitle(getResources().getString(R.string.setting_title));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    public void updateView(){
        userToModify = FirebaseAuth.getInstance().getCurrentUser();
        if (userToModify.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(settingPictureThumbnail);
        }
        changeNameText.setText(userToModify.getDisplayName());
        changeEmailText.setText(userToModify.getEmail());
        changeRadiusText.setText(Constants.RADIUS);
    }

    public void pictureButtonClick(View view){
        if (!changePictureText.getText().toString().equals("")){
            changePictureButton.setVisibility(View.GONE);
            settingProgressBar.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(changePictureText.getText().toString()))
                    .build();
            userToModify.updateProfile(profileUpdates).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(mActivity, getString(R.string.setting_success), Toast.LENGTH_SHORT).show();
                    changePictureButton.setVisibility(View.VISIBLE);
                    settingProgressBar.setVisibility(View.GONE);
                    updateView();
                    Constants.CURRENT_WORKMATE.setWorkmatePicture(userToModify.getPhotoUrl().toString());
                    updateWorkmateAndExpected();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mActivity, getString(R.string.setting_failure), Toast.LENGTH_SHORT).show();
                    changePictureButton.setVisibility(View.VISIBLE);
                    settingProgressBar.setVisibility(View.GONE);            }
            });
        }else{
            Toast.makeText(mActivity, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        }
        }

    public void nameTextClick(View v) {
        changeNameButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
        Toast.makeText(mActivity, changeNameText.getText(), Toast.LENGTH_SHORT).show();
    }

    public void nameButtonClick(View v) {
        if (!changeNameText.getText().toString().equals("")){
            changeNameButton.setBackground(getDrawable(R.drawable.ic_cloud_upload_48));
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(changeNameText.getText().toString())
                    .build();
            userToModify.updateProfile(profileUpdates).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(mActivity, getString(R.string.setting_success), Toast.LENGTH_SHORT).show();
                    changeNameButton.setBackground(getDrawable(R.drawable.ic_check_circle_48dp));
                    Constants.CURRENT_WORKMATE.setWorkmateName(changeNameText.getText().toString());
                    updateWorkmateAndExpected();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mActivity, getString(R.string.setting_failure), Toast.LENGTH_SHORT).show();
                    changeNameButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
                }
            });
        }else{
            Toast.makeText(mActivity, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        }
    }

    public void emailTextClick(View v) {
        changeEmailButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
    }

    public void emailButtonClick(View v) {
        if (!changeEmailText.getText().toString().equals("")){
            changeEmailButton.setBackground(getDrawable(R.drawable.ic_cloud_upload_48));
            userToModify.updateEmail(changeEmailText.getText().toString()).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(mActivity, getString(R.string.setting_success), Toast.LENGTH_SHORT).show();
                    changeEmailButton.setBackground(getDrawable(R.drawable.ic_check_circle_48dp));
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mActivity, getString(R.string.setting_failure), Toast.LENGTH_SHORT).show();
                    changeEmailButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
                }
            });
        }else{
            Toast.makeText(mActivity, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        }
    }

    public void passwordTextClick(View v) {
        changePasswordButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
    }

    public void passwordButtonClick(View v) {
        if (!changePasswordText.getText().toString().equals("")){
            changePasswordButton.setBackground(getDrawable(R.drawable.ic_cloud_upload_48));
            userToModify.updatePassword(changePasswordText.getText().toString()).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(mActivity, getString(R.string.setting_success), Toast.LENGTH_SHORT).show();
                    changePasswordButton.setBackground(getDrawable(R.drawable.ic_check_circle_48dp));
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mActivity, getString(R.string.setting_failure), Toast.LENGTH_SHORT).show();
                    changeRadiusButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
                }
            });
        }else{
            Toast.makeText(mActivity, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        }
    }

    public void radiusTextClick(View v) {
        changeRadiusButton.setBackground(getDrawable(R.drawable.ic_uncheck_circle_48dp));
    }

    public void radiusButtonClick(View v) {
        if (!changeRadiusText.getText().toString().equals("")){
            Constants.RADIUS = changeRadiusText.getText().toString();
        changeRadiusButton.setBackground(getDrawable(R.drawable.ic_check_circle_48dp));
        }else{
            Toast.makeText(mActivity, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteButtonClick(View v) {
        AuthUI.getInstance()
                .delete(getApplicationContext())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(mActivity, "Delete success", Toast.LENGTH_SHORT).show();
                        Workmate w = Constants.CURRENT_WORKMATE;
                        WorkmateHelper.deleteWorkmate(w.getUid());
                        if (Constants.CURRENT_WORKMATE.getYourLunch().getName() != null){
                            ExpectedHelper.deleteExpected(w.getUid(), w.getYourLunch().getName());
                        }
                        Intent intent = new Intent(getApplicationContext(), AuthentificationActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void updateWorkmateAndExpected(){
        WorkmateHelper.updateWorkmateFromSetting();
        if (Constants.CURRENT_WORKMATE.getYourLunch().getName() != null){
            Workmate w = Constants.CURRENT_WORKMATE;
            ExpectedHelper.createExpected(w.getUid(), w.getYourLunch().getName(), w.getWorkmateName(), w.getWorkmatePicture());
        }
    }
}
