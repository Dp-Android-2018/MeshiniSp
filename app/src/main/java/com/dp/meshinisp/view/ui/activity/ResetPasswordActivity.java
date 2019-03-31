package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityResetPasswordBinding;
import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.ResetPasswordViewModel;
import com.google.android.material.snackbar.Snackbar;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class ResetPasswordActivity extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    Lazy<ResetPasswordViewModel> resetPasswordViewModelLazy = inject(ResetPasswordViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etMail.getText().toString().isEmpty()
                        && ValidationUtils.validateTexts(binding.etMail.getText().toString(), ValidationUtils.TYPE_EMAIL)) {
                    sendActivationMail();
                } else {
                    showErrors();
                }
            }
        });


    }

    private void sendActivationMail() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            resetPasswordViewModelLazy.getValue().sendActivationCode(getActivationRequest()).observe(this, new Observer<Response<ActivationResponse>>() {
                @Override
                public void onChanged(Response<ActivationResponse> activationResponseResponse) {
                    if (activationResponseResponse != null) {
                        SharedUtils.getInstance().cancelDialog();
                        if (activationResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE) {
                            showSnackBar("Success");
                            openNextActivity();
                        } else {
                            showSnackBar("error code :" + activationResponseResponse.code());
                        }
                    }

                }
            });
        } else {
            showSnackBar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void openNextActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, PhoneActivitationActivity.class);
        intent.putExtra(ConfigurationFile.Constants.MAIL_NAME, binding.etMail.getText().toString());
        startActivity(intent);
    }

    private ActivationRequest getActivationRequest() {
        ActivationRequest activationRequest = new ActivationRequest();
        activationRequest.setMail(binding.etMail.getText().toString());
        return activationRequest;
    }

    private void showErrors() {
        if (binding.etMail.getText().toString().isEmpty()) {
            showSnackBar(getString(R.string.enter_mail_error_message));
            return;
        }

        if (!ValidationUtils.validateTexts(binding.etMail.getText().toString(), ValidationUtils.TYPE_EMAIL)) {
            showSnackBar(getString(R.string.invalid_mail_error_message));
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}
