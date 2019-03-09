package com.dp.meshinisp.application;

import android.app.Application;

import com.dp.meshinisp.di.component.DaggerNetworkComponent;
import com.dp.meshinisp.di.component.NetworkComponent;
import com.dp.meshinisp.di.modules.AppModule;
import com.dp.meshinisp.utility.utils.ConnectionReceiver;

public class MyApp extends Application {

    private NetworkComponent daggerNetworkComponent;
    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initializeDaggerNetworkComponent();
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public void initializeDaggerNetworkComponent() {
        daggerNetworkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public NetworkComponent getDaggerNetworkComponent() {
        return daggerNetworkComponent;
    }

    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }

}
