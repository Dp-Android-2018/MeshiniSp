package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.dp.meshinisp.application.MyApplication;
import com.dp.meshinisp.utility.utils.ConnectionReceiver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        MyApplication.Companion.getInstance().setConnectionListener(this);
        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.Companion.getInstance().setConnectionListener(this);
    }

    public void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected();
        if (!isConnected) {
            //show a No Internet Alert or Dialog
            Intent I = new Intent(BaseActivity.this, NoInternetConnectionActivity.class);
            startActivity(I);
            finish();
            finishAffinity();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {

            //show a No Internet Alert or Dialog
            Intent I = new Intent(BaseActivity.this, NoInternetConnectionActivity.class);
            startActivity(I);
            finish();
            finishAffinity();

        } else {

            Intent I = new Intent(BaseActivity.this, HomeActivity.class);
            startActivity(I);
            finish();
            finishAffinity();
            // dismiss the dialog or refresh the activity
        }
    }
}
