package com.dp.meshinisp.di.component;

import com.dp.meshinisp.di.modules.PreferenceEditorModule;
import com.dp.meshinisp.di.scope.SharedPreferenceScope;
import com.dp.meshinisp.utility.utils.SharedPreferenceHandler;

import dagger.Subcomponent;


@SharedPreferenceScope
@Subcomponent(modules = {PreferenceEditorModule.class})
public interface SharedPreferencesSubComponent {

    void inject(SharedPreferenceHandler sharedPreferenceHandler);
}
