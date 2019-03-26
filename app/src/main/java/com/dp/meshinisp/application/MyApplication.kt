package com.dp.meshinisp.application

import android.app.Application
import com.dp.meshinisp.koin.*
import com.dp.meshinisp.utility.utils.ConnectionReceiver
import com.dp.meshinisp.utility.utils.ConnectionReceiver.connectionReceiverListener
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(DependencyModule,
                NetworkModule, ViewModelModule, CustomModules, SharedPreferenceModule))
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    fun setConnectionListener(listener: ConnectionReceiver.ConnectionReceiverListener) {
        connectionReceiverListener = listener
    }
}