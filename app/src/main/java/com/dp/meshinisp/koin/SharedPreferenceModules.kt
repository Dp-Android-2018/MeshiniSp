package com.dp.meshinisp.koin

import com.dp.meshinisp.utility.utils.SharedPreferenceHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

@JvmField
val SharedPreferenceModule = module {
    single { SharedPreferenceHandler(androidContext()) }
}