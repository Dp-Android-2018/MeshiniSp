package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dp.meshinisp.application.MyApp;
import com.dp.meshinisp.utility.utils.ConnectionReceiver;

public class BaseActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.getInstance().setConnectionListener(this);
    }

    public void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected();
        System.out.println("Internet Connection Status :" + isConnected);
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
