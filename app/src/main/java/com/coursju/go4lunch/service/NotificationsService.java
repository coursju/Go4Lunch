package com.coursju.go4lunch.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.api.ExpectedHelper;
import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.modele.Expected;
import com.coursju.go4lunch.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class NotificationsService extends FirebaseMessagingService {

    private static final String TAG = "NotificationsService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();
            Log.i(TAG, message);
            if (Constants.CURRENT_WORKMATE.getYourLunch() != null) {
                CollectionReference ref = ExpectedHelper.getExpectedRestaurant(Constants.CURRENT_WORKMATE.getYourLunch().getName());
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
    }

    private void sendVisualNotification(String messageBody) {
        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch_256)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        int NOTIFICATION_ID = 007;
        String NOTIFICATION_TAG = "GO4LUNCH";
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}