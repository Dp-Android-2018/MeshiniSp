package com.dp.meshinisp.application

import android.app.Application
import com.dp.meshinisp.koin.*
import com.dp.meshinisp.utility.utils.ConnectionReceiver
import com.dp.meshinisp.utility.utils.ConnectionReceiver.connectionReceiverListener
import org.koin.android.ext.android.startKoin
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build



class MyApplication : Application() {

    val CHANNEL_ID = "exampleChannel"

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(DependencyModule,
                NetworkModule, ViewModelModule, CustomModules, SharedPreferenceModule))
        createNotificationChannel()
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    fun setConnectionListener(listener: ConnectionReceiver.ConnectionReceiverListener) {
        connectionReceiverListener = listener
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}