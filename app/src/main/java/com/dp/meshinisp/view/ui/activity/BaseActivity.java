package com.dp.meshinisp.view.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.dp.meshinisp.application.MyApplication;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.ConnectionReceiver;
import com.dp.meshinisp.utility.utils.ContextWrapper;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;
import kotlin.Lazy;
import okhttp3.ResponseBody;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class BaseActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    Lazy<CustomUtils> customUtils = inject(CustomUtils.class);
    Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        MyApplication.Companion.getInstance().setConnectionListener(this);
//        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.Companion.getInstance().setConnectionListener(this);
    }

    public void logout() {
        SharedUtils.getInstance().showProgressDialog(this);
        mainActivityViewModelLazy.getValue().logout().observe(this, voidResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                goToLoginPage();
            } else if (voidResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                goToLoginPage();
            } else {
                if (voidResponse.errorBody() != null) {
                    showStartTripErrorMessage(voidResponse.errorBody());
                }
            }
        });
    }

    private void goToLoginPage() {
        String languageType = customUtils.getValue().getSavedLanguageType();
        customUtils.getValue().clearSharedPref();
        customUtils.getValue().saveLanguageTypeToPrefs(languageType);
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showStartTripErrorMessage(ResponseBody errorResponseBody) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(errorResponseBody.string(), ErrorResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String error = "";
        for (String string : errorResponse.getErrors()) {
            error += string;
            error += "\n";
        }
        showSnackbar(error);
    }

    private void showSnackbar(String error) {
        Snackbar.make(Objects.requireNonNull(getCurrentFocus()), error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Intent I = new Intent(BaseActivity.this, NoInternetConnectionActivity.class);
            startActivity(I);
            finish();
            finishAffinity();

        } else {
            Intent I = new Intent(BaseActivity.this, SplashScreenActivity.class);
            startActivity(I);
            finish();
            finishAffinity();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale newLocale = new Locale(getAppLang());

        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

    public String getAppLang() {
        if (customUtils.getValue().getSavedLanguageType().equals(ConfigurationFile.Constants.DEFAULT_LANGUAGE_STRING)) {
            if (Locale.getDefault().getDisplayLanguage().equals(ConfigurationFile.Constants.ARABIC_LANGUAGE_STRING)) {
                ConfigurationFile.Constants.ACCEPT_LANGUAGE = ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC;
                customUtils.getValue().saveLanguageTypeToPrefs(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC);
            } else if (Locale.getDefault().getDisplayLanguage().equals(ConfigurationFile.Constants.FRENCH_LANGUAGE_STRING)) {
                ConfigurationFile.Constants.ACCEPT_LANGUAGE = ConfigurationFile.Constants.ACCEPT_LANGUAGE_FRENCH;
                customUtils.getValue().saveLanguageTypeToPrefs(ConfigurationFile.Constants.ACCEPT_LANGUAGE_FRENCH);
            } else {
                ConfigurationFile.Constants.ACCEPT_LANGUAGE = ConfigurationFile.Constants.ACCEPT_LANGUAGE_ENGLISH;
                customUtils.getValue().saveLanguageTypeToPrefs(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ENGLISH);
            }
        } else {
            ConfigurationFile.Constants.ACCEPT_LANGUAGE = customUtils.getValue().getSavedLanguageType();
        }
        return customUtils.getValue().getSavedLanguageType();
    }
}
