package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityChangePasswordProfileBinding;
import com.dp.meshinisp.service.model.request.ChangePasswordRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.ChangePasswordProfileViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class ChangePasswordProfileActivity extends BaseActivity {

    ActivityChangePasswordProfileBinding binding;
    Lazy<ChangePasswordProfileViewModel> changePasswordProfileViewModelLazy = inject(ChangePasswordProfileViewModel.class);
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password_profile);
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        setupToolbar();
    }

    private void setupToolbar() {
        binding.accountToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.accountToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void saveChanges(View view) {
        if (!binding.etPassword.getText().toString().isEmpty()
                && !binding.etNewPassword.getText().toString().isEmpty()
                && !binding.etPasswordConfirmation.getText().toString().isEmpty()
                && (binding.etPassword.getText().toString().length() >= 8)
                && (binding.etNewPassword.getText().toString().length() >= 8)
                && (binding.etPasswordConfirmation.getText().toString().length() >= 8)
                && binding.etNewPassword.getText().toString().equals(binding.etPasswordConfirmation.getText().toString())
        ) {
            if (ValidationUtils.isConnectingToInternet(this)) {
                SharedUtils.getInstance().showProgressDialog(this);
                makeChangePasswordRequest();
            } else {
                showSnackbar(getString(R.string.there_is_no_internet_connection));
            }
        } else {
            showErrors();
        }
    }

    private void makeChangePasswordRequest() {
        changePasswordProfileViewModelLazy.getValue().changePassword(getChangedPasswordRequest()).observe(this, new Observer<Response<MessageResponse>>() {
            @Override
            public void onChanged(Response<MessageResponse> offerResponseResponse) {
                SharedUtils.getInstance().cancelDialog();
                if (offerResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                        && ConfigurationFile.Constants.SUCCESS_CODE_TO > offerResponseResponse.code()) {
                    if (offerResponseResponse.body() != null) {
                        showSnackbar(offerResponseResponse.body().getMessage());
                    }
                    new Handler().postDelayed(() -> onBackPressed(), ConfigurationFile.Constants.WAIT_VALUE);

                } else if (offerResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                    logout();
                } else {
                    showErrorMessage(offerResponseResponse);
                }
            }
        });
    }

    private void showErrorMessage(Response<MessageResponse> offerResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(offerResponseResponse.errorBody().string(), ErrorResponse.class);
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

    private ChangePasswordRequest getChangedPasswordRequest() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword(binding.etPassword.getText().toString());
        changePasswordRequest.setNewPassword(binding.etNewPassword.getText().toString());
        return changePasswordRequest;
    }

    private void showErrors() {
        if (binding.etPassword.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_password_error_message));
            return;
        }
        if (binding.etNewPassword.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_new_password_error_message));
            return;
        }
        if (binding.etPasswordConfirmation.getText().toString().isEmpty()) {
            showSnackbar(getString(R.string.enter_new_password_confirmation_error_message));
            return;
        }
        if (binding.etPassword.getText().toString().length() < 8) {
            showSnackbar(getString(R.string.password_length_message_error));
            return;
        }
        if (binding.etNewPassword.getText().toString().length() < 8) {
            showSnackbar(getString(R.string.new_password_length_message_error));
            return;
        }
        if (binding.etPasswordConfirmation.getText().toString().length() < 8) {
            showSnackbar(getString(R.string.new_password_confirmation_length_message_error));
            return;
        }

        if (!binding.etNewPassword.getText().toString().equals(binding.etPasswordConfirmation.getText().toString())) {
            showSnackbar(getString(R.string.passwords_not_match_error));
        }


    }

    public void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }
}
