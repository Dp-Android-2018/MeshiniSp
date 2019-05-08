package com.dp.meshinisp.utility.utils;

import com.dp.meshinisp.service.model.global.LoginResponseModel;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class CustomUtils {
    private Lazy<SharedPreferenceHandler> prefrenceUtils = inject(SharedPreferenceHandler.class);

    public LoginResponseModel getSavedMemberData() {
        return (LoginResponseModel) prefrenceUtils.getValue().getSavedObject(ConfigurationFile.SharedPrefConstants.SHARED_PREF_NAME, LoginResponseModel.class);
    }

    public void saveMemberDataToPrefs(LoginResponseModel data) {
        prefrenceUtils.getValue().saveObjectToSharedPreferences(ConfigurationFile.SharedPrefConstants.SHARED_PREF_NAME, data);
    }

    public void saveActivationTypeToPrefs(String objName, boolean objValue){
        prefrenceUtils.getValue().saveMemberTypeSharedPreferences(objName,objValue);
    }

    public boolean getSavedActivationType(){
        return prefrenceUtils.getValue().getSavedActivationType();
    }


    public void saveLanguageTypeToPrefs(String langValue){
        prefrenceUtils.getValue().saveLanguageTypeSharedPreferences(ConfigurationFile.Constants.LANGUAGE_TYPE,langValue);
    }

    public String getSavedLanguageType(){
        return prefrenceUtils.getValue().getSavedLanguageType();
    }


    public void clearSharedPref() {
        prefrenceUtils.getValue().clearToken();
    }
}
