package com.coursju.go4lunch.base;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.ExpectedHelper;
import com.coursju.go4lunch.api.FavoritesHelper;
import com.coursju.go4lunch.authentification.AuthentificationActivity;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Expected;
import com.coursju.go4lunch.utils.Constants;
import com.coursju.go4lunch.viewmodel.Go4LunchViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.List;
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

    public void on(){
            if (Constants.CURRENT_WORKMATE.getYourLunch() != null) {
                CollectionReference ref = ExpectedHelper.getExpectedRestaurant(Constants.DETAILS_RESTAURANT.getName());
                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Expected> expectedList = queryDocumentSnapshots.toObjects(Expected.class);
                        String restoAddress = Constants.CURRENT_WORKMATE.getYourLunch().getName()+"\n"
                                +Constants.CURRENT_WORKMATE.getYourLunch().getAddress()+"\n";
                        for (Expected expected : expectedList) {
                            if (!expected.getUid().equals(Constants.CURRENT_WORKMATE.getUid())) {
                                restoAddress += expected.getWorkmateName()+"\n";
                            }
                        }
                        sendVisualNotification(restoAddress);
                        Log.i(TAG, restoAddress);
                    }
                });
            }else{
                Log.i(TAG, "no restaurant selected");
            }

    }

    private void sendVisualNotification(String messageBody) {
        final int NOTIFICATION_ID = 007;
        final String NOTIFICATION_TAG = "GO4LUNCH";

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(messageBody);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch_256)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}