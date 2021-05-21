package com.coursju.go4lunch.authentification;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.WorkmateHelper;
import com.coursju.go4lunch.base.BaseActivity;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Restaurant;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;


public class AuthentificationActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;

    @BindView(R.id.email_login_button) Button buttonEmailLogin;
    @BindView(R.id.google_login_button) Button buttonGoogleLogin;
    @BindView(R.id.facebook_login_button) Button buttonFacebookLogin;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_authentification;
    }

    @OnClick(R.id.email_login_button)
    public void onClickLoginEmailButton() {
        this.startEmailSignInActivity();
    }

    @OnClick(R.id.google_login_button)
    public void onClickLoginGoogleButton() {
        this.startGoogleSignInActivity();
    }

    @OnClick(R.id.facebook_login_button)
    public void onClickLoginFacebookButton() {
        this.startFacebookSignInActivity();
    }

    private void startGoogleSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_lunch)
                        .build(),
                RC_SIGN_IN);
    }

    private void startFacebookSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_lunch)
                        .build(),
                RC_SIGN_IN);
    }

    private void startEmailSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_lunch)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d("authentification: ",getString(R.string.connection_succeed));
                this.createUserInFirestore();//!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!! put in first IF !!!!
                onceLogged();
            } else {
                if (response == null) {
                    Log.d("authentification",getString(R.string.error_authentication_canceled));
                    Toast.makeText(getApplicationContext(),getString(R.string.error_authentication_canceled),Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d("authentification",getString(R.string.error_no_internet));
                    Toast.makeText(getApplicationContext(),getString(R.string.error_no_internet),Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.d("authentification",getString(R.string.error_unknown_error));
                    Toast.makeText(getApplicationContext(),getString(R.string.error_unknown_error),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onceLogged();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String userName = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            String userEmail = this.getCurrentUser().getEmail();

            WorkmateHelper.createWorkmate(uid, urlPicture, userName, userEmail, new Restaurant()).addOnFailureListener(this.onFailureListener());
        }
    }

    private void onceLogged(){
        if (this.isCurrentUserLogged()){
            this.startMainActivity();
        }
    }
}
