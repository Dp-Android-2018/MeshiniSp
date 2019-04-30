package com.dp.meshinisp.view.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityChangePasswordBinding;
import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.ChangePasswordViewModel;
import com.google.android.material.snackbar.Snackbar;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class ChangePasswordActivity extends BaseActivity {

    private String email;
    private String activationToken;
    ActivityChangePasswordBinding binding;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    Lazy<ChangePasswordViewModel> changePasswordViewModelLazy = inject(ChangePasswordViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        email = getIntent().getStringExtra(ConfigurationFile.Constants.MAIL_NAME);
        activationToken = getIntent().getStringExtra(ConfigurationFile.Constants.TOKN_VALUE);

    }

    public void changePasswordClicked(View view) {
        if (!binding.etNewPassword.getText().toString().isEmpty()
                && binding.etNewPassword.getText().toString().length() >= 8
                && !binding.etConfirmPassword.getText().toString().isEmpty()
                && binding.etConfirmPassword.getText().toString().length() >= 8
                && binding.etNewPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())
        ) {
            makeResetPassword();
        } else {
            showErrors();
        }
    }

    private void makeResetPassword() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            changePasswordViewModelLazy.getValue().resetPassword(getRequest(), activationToken).observe(this, new Observer<Response<ActivationResponse>>() {
                @Override
                public void onChanged(Response<ActivationResponse> activationResponseResponse) {
                    if (activationResponseResponse != null) {
                        SharedUtils.getInstance().cancelDialog();
                        if (activationResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                                && ConfigurationFile.Constants.SUCCESS_CODE_TO > activationResponseResponse.code()) {
                            openNextActivity();
                        }else if (activationResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE){
                            logout();
                        }
                        else {
                            showSnackBar("error code :" + activationResponseResponse.code());
                        }
                    }
                }
            });
        }else {
            showSnackBar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void openNextActivity() {
        Intent intent=new Intent(ChangePasswordActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private ActivationRequest getRequest() {
        ActivationRequest activationRequest=new ActivationRequest();
        activationRequest.setMail(email);
        activationRequest.setPassword(binding.etConfirmPassword.getText().toString());
        return activationRequest;
    }

    private void showErrors() {
        if (binding.etNewPassword.getText().toString().isEmpty()) {
            showSnackBar(getString(R.string.enter_password_error_message));
            return;
        }

        if (binding.etNewPassword.getText().toString().length() < 8) {
            showSnackBar(getString(R.string.password_length_message_error));
            return;
        }

        if (binding.etConfirmPassword.getText().toString().isEmpty()) {
            showSnackBar(getString(R.string.enter_confirm_password_error_message));
            return;
        }

        if (binding.etConfirmPassword.getText().toString().length() < 8) {
            showSnackBar(getString(R.string.password_length_message_error));
            return;
        }

        if (! binding.etNewPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())) {
            showSnackBar(getString(R.string.password_not_match_message_error));
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}
