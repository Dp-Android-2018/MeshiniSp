package com.dp.meshinisp.application

import android.app.Application
import com.dp.meshinisp.di.component.DaggerNetworkComponent
import com.dp.meshinisp.di.component.NetworkComponent
import com.dp.meshinisp.di.koin.NetworkModule
import com.dp.meshinisp.di.modules.AppModule
import com.dp.meshinisp.utility.utils.ConnectionReceiver
import com.dp.meshinisp.utility.utils.ConnectionReceiver.connectionReceiverListener
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(NetworkModule))
    }

    fun setConnectionListener(listener: ConnectionReceiver.ConnectionReceiverListener) {
        connectionReceiverListener = listener
    }
}