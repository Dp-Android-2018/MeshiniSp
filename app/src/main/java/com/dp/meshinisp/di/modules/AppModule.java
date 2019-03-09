package com.dp.meshinisp.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Bahaa Gabal on 24,November,2018
 */

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context){
        this.mContext = context;
    }

    @Provides
    @Singleton
    Context getContext(){
        return mContext;
    }
}
