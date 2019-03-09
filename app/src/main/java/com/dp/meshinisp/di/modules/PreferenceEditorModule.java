package com.dp.meshinisp.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.dp.meshinisp.R;
import com.dp.meshinisp.di.scope.SharedPreferenceScope;

import dagger.Module;
import dagger.Provides;


@Module
public class PreferenceEditorModule {


    @Provides
    @SharedPreferenceScope
    SharedPreferences.Editor getSharedPreferenceEditor(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.action_close)
                , Context.MODE_PRIVATE).edit();
    }


    @Provides
    @SharedPreferenceScope
    SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.action_close)
                , Context.MODE_PRIVATE);
    }
}
