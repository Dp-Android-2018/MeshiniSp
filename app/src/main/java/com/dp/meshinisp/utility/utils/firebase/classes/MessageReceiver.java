package com.dp.meshinisp.utility.utils.firebase.classes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import com.dp.meshinisp.R;
import com.dp.meshinisp.service.model.global.LoginResponseModel;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.view.ui.activity.MainActivity;
import com.dp.meshinisp.view.ui.activity.RequestDetailsActivity;
import com.dp.meshinisp.view.ui.activity.SplashScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class MessageReceiver extends FirebaseMessagingService {
    private static String TAG = "Receiver :";
    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_ID = "exampleChannel";

    public static String TOKEN = null;
    private Intent intent;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("Notification Log Title:" + remoteMessage.getNotification().getTitle());
        System.out.println("Notification Log Body:" + remoteMessage.getNotification().getBody());
        System.out.println("Notification Log Data :" + remoteMessage.getData().get("title"));
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().get("title") != null) {
                System.out.println("title is : " + remoteMessage.getData().get("title"));
                intent = new Intent();
                switch (remoteMessage.getData().get("title")) {
                    case "account-approved":
                        makeAccountApprovedIntent();
                        break;
                    case "offer-accepted":
                        makeIntentOfferRequest(remoteMessage.getData());
                        break;
                    case "profile-update-approved":
                        makeIntentProfileUpdated(remoteMessage.getData());
                        break;
                    case "account-disabled":
                        makeAccountApprovedIntent();
                        break;
                    case "account-enabled":
                        makeAccountApprovedIntent();
                        break;
                }
                Notify(intent, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }
    }

    private void makeAccountApprovedIntent() {
        intent = new Intent(getApplicationContext(), MainActivity.class);
    }

    private void makeIntentProfileUpdated(Map<String, String> data) {
        LoginResponseModel loginResponseModel = customUtilsLazy.getValue().getSavedMemberData();
        loginResponseModel.setProfilePictureUrl(data.get("profile_picture"));
        loginResponseModel.setFirstName(data.get("first_name"));
        loginResponseModel.setLastName(data.get("last_name"));
        loginResponseModel.setEmail(data.get("email"));
        loginResponseModel.setPhone(data.get("phone"));
        customUtilsLazy.getValue().saveMemberDataToPrefs(loginResponseModel);
        intent = new Intent(getApplicationContext(), MainActivity.class);
    }

    private void makeIntentOfferRequest(Map<String, String> data) {
        intent = new Intent(getApplicationContext(), RequestDetailsActivity.class);
        intent.putExtra(ConfigurationFile.Constants.REQUEST_Type, ConfigurationFile.Constants.TRIPS_TYPE_UPCOMING);
        intent.putExtra(ConfigurationFile.Constants.REQUEST_ID, Integer.parseInt(data.get("request_id")));
        intent.putExtra(ConfigurationFile.Constants.OFFER_PRICE, data.get("offer_price"));
    }

    public void Notify(Intent intent, String messageTitle, String notificationBody) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {500, 500, 500, 500, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "androidChannel")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(messageTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        System.out.println("Refreshed token: " + token);
    }


}