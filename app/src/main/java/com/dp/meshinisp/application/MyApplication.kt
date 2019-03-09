package com.dp.meshinisp.application

import android.app.Application
import com.dp.meshinisp.di.koin.NetworkModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(NetworkModule))
    }
}