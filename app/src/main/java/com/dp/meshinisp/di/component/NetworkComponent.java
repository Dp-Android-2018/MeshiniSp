package com.dp.meshinisp.di.component;

import com.dp.meshinisp.di.modules.AppModule;
import com.dp.meshinisp.di.modules.NetworkModule;
import com.dp.meshinisp.di.modules.PreferenceEditorModule;
import com.dp.meshinisp.di.modules.SharedPreferenceModule;
import com.dp.meshinisp.service.repository.remotes.ApiInterfaces;
import com.dp.meshinisp.utility.utils.SharedPreferenceHandler;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, SharedPreferenceModule.class})
public interface NetworkComponent {

    ApiInterfaces getRetrofitApiInterfaces();

    SharedPreferenceHandler getSharedPreferenceInstance();

    SharedPreferencesSubComponent getSubComponentSharedPreference(PreferenceEditorModule preferenceEditorModule);
}
