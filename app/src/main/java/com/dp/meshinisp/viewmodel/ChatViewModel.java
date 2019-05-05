package com.dp.meshinisp.viewmodel;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.service.autofill.UserData;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.dp.meshinisp.application.MyApplication;
import com.dp.meshinisp.service.model.global.ConversationHistory;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.firebase.classes.FirebaseToken;
import com.dp.meshinisp.view.ui.callback.BaseInterface;
import com.dp.meshinisp.view.ui.callback.CallAnotherActivityNavigator;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.sql.Timestamp;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;

import static android.app.Activity.RESULT_OK;
import static org.koin.java.standalone.KoinJavaComponent.inject;

/**
 * Created by DELL on 13/05/2018.
 */

public class ChatViewModel extends BaseObservable {

    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private DatabaseReference reference;
    public ObservableList<Message> messages;
    public ObservableField<String> message;
    private ConversationHistory history;
    StorageReference storageReference;
    private BaseInterface callback;
    private Activity activity;
    private int checker;
    Uri selectedImageUri = null;
    private String deviceToken = null;
    private CallAnotherActivityNavigator navigator;
    //    RxPermissions rxPermissions;
//    MessageNotification notification;

    public ChatViewModel(Activity activity, BaseInterface callback, CallAnotherActivityNavigator navigator, ConversationHistory history) {
        this.callback = callback;
        this.activity = activity;
        this.navigator = navigator;
        messages = new ObservableArrayList<>();
        message = new ObservableField<String>("");
        this.history = history;

//        rxPermissions = new RxPermissions(activity);
//        deviceToken = activity.getIntent().getStringExtra(ConfigurationFile.IntentConstants.DEVICE_TOKEN);

//        System.out.println("Device Token Data :" + deviceToken);

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        /*notification = new MessageNotification();
        notification.setDeviceToken(deviceToken);
        notification.setKey(5);
        MessageData messageData = new MessageData();
        messageData.setNotificationTitle("new_message");
        notification.setData(messageData);*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        storageReference = FirebaseStorage.getInstance().getReference().child("app_photos");
//        ((MyApplication) (MyApplication.Companion.getInstance())).setSecondUserName(history.getConversations().sender_name);
//        getConversationDetails();
    }



   /* public void sendNotification() {

        if (NetWorkConnection.isConnectingToInternet(activity)) {
            CustomUtils.getInstance().showProgressDialog(activity);
            MsgRequest msgRequest = new MsgRequest();
            msgRequest.setNotification(notification);
            System.out.println("Mobile Code  Notification Request :" + new Gson().toJson(msgRequest));
            ApiClient.getClient().create(EndPoints.class).sendNotification(ConfigurationFile.Constants.API_KEY, CustomUtils.getInstance().getAppLanguage(activity), ConfigurationFile.Constants.Content_Type, ConfigurationFile.Constants.Content_Type, msgRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(defaultResponseResponse -> {
                        CustomUtils.getInstance().cancelDialog();
                        System.out.println("Mobile Code  Notification:" + defaultResponseResponse.code());
                        System.out.println("Mobile Code  Notification Msg :" + defaultResponseResponse.body().getMessage());
                        if (defaultResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE) {

                        } else if (defaultResponseResponse.code() == ConfigurationFile.Constants.CANT_COMPLETE_REQUEST_CODE) {

                        } else if (defaultResponseResponse.code() == ConfigurationFile.Constants.UNAUTHENTICATED_CODE ||
                                defaultResponseResponse.code() == ConfigurationFile.Constants.UNSUBSCRIBE_CODE) {
                            CustomUtils.getInstance().endSession(activity);
                        }

                    }, throwable -> {
                        CustomUtils.getInstance().cancelDialog();
                        //  binding.progressBar.setVisibility(View.GONE);
                        System.out.println("Ex :" + throwable.getMessage());
                    });


        } else {
            callback.updateUi(ConfigurationFile.Constants.NO_INTERNET_CONNECTION_CODE);
        }
    }*/



    //////////////////////Dialog To Choose Image From Camera Or Gallery /////////////////////////////
    /*public void displayDialog(View view) {
        callback.updateUi(ConfigurationFile.Constants.SHOW_DIALOG_CODE);
    }*/

    ///////////////////////////////////////////////////////////////////////////////////
   /* public void shareLocation(View view) {
        navigator.callActivity();
    }*/

    //////////////////////////////////////////////////////////////////////////

    ///////////////////Ask For Permission if Sdk > lollipop //////////////////////////////////////////////
   /* public void askForPermission(int checker) {
        CustomUtils.getInstance().requirePermission(rxPermissions, checker, callback);
    }*/

   /* ///////////////////////////////////Set Image To Image View and Convert it to Base 54 ////////////////////////////////
    public void onActivityResult(int requestCode, int resultCode, Intent data, Image image) {
        if (requestCode == ConfigurationFile.Constants.CAMERA_REQUEST || requestCode == ConfigurationFile.Constants.GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImageUri = Uri.fromFile(new File(image.getPath()));
                uploadFireBasePic();
            }
        } else if (requestCode == ConfigurationFile.Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, activity);
                LatLng lng = place.getLatLng();
                addMessage(lng.latitude + "," + lng.longitude, "location");
            }
        }
    }*/

   /* public void uploadFireBasePic() {
        CustomUtils.getInstance().uploadFireBasePic(storageReference, selectedImageUri, photoUrl -> addMessage(photoUrl, "image"));
    }*/


}
