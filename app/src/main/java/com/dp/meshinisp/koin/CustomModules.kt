package com.dp.meshinisp.koin

import com.dp.meshinisp.utility.utils.ConnectionReceiver
import com.dp.meshinisp.utility.utils.CustomUtils
import com.dp.meshinisp.utility.utils.SharedPreferenceHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

@JvmField
val CustomModules= module {

    single { ConnectionReceiver() }
    single { CustomUtils() }
}