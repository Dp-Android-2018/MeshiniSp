package com.dp.meshinisp.utility.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dp.meshinisp.R;
import com.google.gson.Gson;

public class SharedPreferenceHandler {

    private Context context;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public SharedPreferenceHandler(Context context) {
        this.context=context;
        prefs=context.getSharedPreferences(context.getResources().getString(R.string.action_close)
                , Context.MODE_PRIVATE);

        editor=context.getSharedPreferences(context.getResources().getString(R.string.action_close), Context.MODE_PRIVATE).edit();
    }

    public void addStringToSharedPreferences(String title, String value) {
        editor.putString(title, value);
        editor.commit();
    }

    public String getStringFromSharedPreferences(String title) {
        String value = "";
        value = prefs.getString(title, "");
        return value;
    }

    public void addIntegerToSharedPreferences(String title, int value) {
        editor.putInt(title, value);
        editor.commit();
    }

    public int getIntegerFromSharedPreferences(String title) {
        int value = 0;
        value = prefs.getInt(title, 0);
        return value;
    }

    public void addBooleanToSharedPreferences(String title, Boolean value) {
        editor.putBoolean(title, value);
        editor.commit();
    }

    public Boolean getBooleanFromSharedPreferences(String title) {
        Boolean value = false;
        value = prefs.getBoolean(title, false);
        return value;
    }

    public void saveObjectToSharedPreferences(String savedObjectName, Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.remove(savedObjectName);
        editor.putString(savedObjectName, json);
        editor.commit();
    }

    public Object getSavedObject(String savedObjectName, Class objectClass) {
        Gson gson = new Gson();
        String json = prefs.getString(savedObjectName, "");
        return gson.fromJson(json, objectClass);
    }

    public void saveMemberTypeSharedPreferences(String savedObjectName, boolean memberType) {
        editor.putBoolean(savedObjectName, memberType);
        editor.commit();
    }

    public boolean getSavedActivationType() {
        return prefs.getBoolean(ConfigurationFile.Constants.ACTIVATION_TYPE,false);
    }

    public void saveLanguageTypeSharedPreferences(String savedObjectName, String langType) {
        editor.putString(savedObjectName, langType);
        editor.commit();
    }

    public String getSavedLanguageType() {
        return prefs.getString(ConfigurationFile.Constants.LANGUAGE_TYPE,"0");
    }

    public void clearToken() {
        editor.clear();
        editor.apply();
    }
}