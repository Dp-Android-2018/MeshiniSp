package com.dp.meshinisp.di.modules;

import android.content.Context;

import com.dp.meshinisp.utility.utils.SharedPreferenceHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferenceModule {

    @Singleton
    @Provides
    SharedPreferenceHandler getSharedPreferenceUtils(Context context) {
        return new SharedPreferenceHandler(context);
    }
}
