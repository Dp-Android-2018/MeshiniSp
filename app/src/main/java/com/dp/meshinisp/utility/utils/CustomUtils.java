package com.dp.meshinisp.utility.utils;

import android.app.Application;


public class CustomUtils {
   /* private SharedPreferenceHandler prefrenceUtils;

    public CustomUtils(Application application) {
        this.prefrenceUtils = GetSharedPreference.getInstance().getSharedPreference(application);
    }

    public LoginResponseModel getSavedMemberData() {
        return (LoginResponseModel) prefrenceUtils.getSavedObject(ConfigurationFile.SharedPrefConstants.SHARED_PREF_NAME, LoginResponseModel.class);
    }

    public void saveMemberDataToPrefs(LoginResponseModel data) {
        prefrenceUtils.saveObjectToSharedPreferences(ConfigurationFile.SharedPrefConstants.SHARED_PREF_NAME, data);
    }

    public void saveMobileUserDataToPrefs(SignUpRequest data) {
        prefrenceUtils.saveObjectToSharedPreferences(ConfigurationFile.SharedPrefConstants.NewDataToSave, data);
    }

    public SignUpRequest getSaveMobileUserDataToPrefs() {
        return (SignUpRequest) prefrenceUtils.getSavedObject(ConfigurationFile.SharedPrefConstants.NewDataToSave, SignUpRequest.class);
    }

    public void saveActivationTypeToPrefs(String objName, boolean objValue){
        prefrenceUtils.saveMemberTypeSharedPreferences(objName,objValue);
    }

    public boolean getSavedActivationType(){
        return prefrenceUtils.getSavedActivationType();
    }


    public void saveLanguageTypeToPrefs(String langValue){
        prefrenceUtils.saveLanguageTypeSharedPreferences(ConfigurationFile.Constants.LANGUAGE_TYPE,langValue);
    }

    public String getSavedLanguageType(){
        return prefrenceUtils.getSavedLanguageType();
    }


    public void clearSharedPref() {
        prefrenceUtils.clearToken();
    }*/
}
