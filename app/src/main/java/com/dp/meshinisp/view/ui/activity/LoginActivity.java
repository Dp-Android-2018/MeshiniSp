package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityLoginBinding;
import com.dp.meshinisp.service.model.global.LoginResponseModel;
import com.dp.meshinisp.service.model.request.LoginRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.LoginResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.utility.utils.firebase.classes.FirebaseToken;
import com.dp.meshinisp.viewmodel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import androidx.databinding.DataBindingUtil;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    Lazy<LoginViewModel> loginViewModelLazy = inject(LoginViewModel.class);
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getAndSetDeviceToken();
        makeActionOnClickOntvNewUserSignUp();
        makeActionOnClickOntvForgetPassword();
        makeActionOnClickOnbtLogin();
    }

    private void getAndSetDeviceToken() {
        FirebaseToken.getInstance().getFirebaseToken().observe(this, s -> deviceToken = s);
    }

    private void makeActionOnClickOnbtLogin() {
        binding.btLogin.setOnClickListener(v -> {
            if (!binding.etMail.getText().toString().isEmpty()
                    && !binding.etPassword.getText().toString().isEmpty()
                    && (binding.etPassword.getText().toString().length() >= 8)
            ) {
                if (ValidationUtils.isConnectingToInternet(this)) {
                    makeLoginRequest();
                } else {
                    showSnackbar(getString(R.string.there_is_no_internet_connection));
                }
            } else {
                showErrors();
            }
        });
    }

    private void makeLoginRequest() {
        SharedUtils.getInstance().showProgressDialog(this);
        loginViewModelLazy.getValue().login(getLoginRequest()).observe(this, loginResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (loginResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > loginResponseResponse.code()) {
                if (loginResponseResponse.body() != null) {
                    saveDataToSharedPreferences(loginResponseResponse.body().getData());
                    openMainActivity();
                }
                if (loginResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                    logout();
                } else {
                    Snackbar.make(binding.getRoot(), "no data", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                makeActionOnCode(loginResponseResponse);
            }
        });
    }

    private void makeActionOnCode(Response<LoginResponse> loginResponseResponse) {
        if (loginResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE) {
            if (loginResponseResponse.body() != null) {
                saveDataToSharedPreferences(loginResponseResponse.body().getData());
                openMainActivity();
            }
        } else {
            Gson gson = new GsonBuilder().create();
            ErrorResponse errorResponse = new ErrorResponse();

            try {
                errorResponse = gson.fromJson(loginResponseResponse.errorBody().string(), ErrorResponse.class);
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
    }

    private void saveDataToSharedPreferences(LoginResponseModel data) {
        customUtilsLazy.getValue().saveMemberDataToPrefs(data);
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private LoginRequest getLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(binding.etMail.getText().toString());
        loginRequest.setPassword(binding.etPassword.getText().toString());
        loginRequest.setDeviceToken(deviceToken);
        return loginRequest;
    }

    private void showErrors() {
        if (binding.etMail.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_mail_or_phone_error_message));
            return;
        }
        if (binding.etPassword.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_password_error_message));
            return;
        }
        if (binding.etPassword.getText().toString().length() < 8) {
            showSnackbar(getString(R.string.password_length_message_error));
        }
    }

    public void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void makeActionOnClickOntvNewUserSignUp() {
        binding.tvNewUserSignUp.setOnClickListener(v -> openIntent(RegisterActivity.class));
    }

    private void makeActionOnClickOntvForgetPassword() {
        binding.tvForgetPassword.setOnClickListener(v -> openIntent(ResetPasswordActivity.class));
    }

    private void openIntent(Class activityClass) {
        Intent intent = new Intent(LoginActivity.this, activityClass);
        startActivity(intent);
    }
}
