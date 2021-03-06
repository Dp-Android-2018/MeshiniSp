package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityPhoneActivitationBinding;
import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.PhoneActivationViewModel;
import com.goodiebag.pinview.Pinview;
import com.google.android.material.snackbar.Snackbar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class PhoneActivitationActivity extends BaseActivity {

    ActivityPhoneActivitationBinding binding;
    String email;
    Lazy<PhoneActivationViewModel> phoneActivationViewModelLazy = inject(PhoneActivationViewModel.class);
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_activitation);
        email = getIntent().getStringExtra(ConfigurationFile.Constants.MAIL_NAME);

        binding.pinView.setPinViewEventListener((pinview, fromUser) -> {
            if (!pinview.getValue().isEmpty()) {
                getActivationToken();
                Toast.makeText(PhoneActivitationActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btResend.setOnClickListener(v -> {
            sendActivationMail();
        });
    }

    private void getActivationToken() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            phoneActivationViewModelLazy.getValue().getActivationToken(getCodeActivationRequest()).observe(this, activationResponseResponse -> {
                if (activationResponseResponse != null) {
                    SharedUtils.getInstance().cancelDialog();
                    if (activationResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > activationResponseResponse.code()) {
                        openNextActivityWithToken(activationResponseResponse.body().getActivationToken());
                    } else if (activationResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                        logout();
                    } else {
                        showSnackBar("error code :" + activationResponseResponse.code());
                    }
                }

            });
        } else {
            showSnackBar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void sendActivationMail() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            phoneActivationViewModelLazy.getValue().sendActivationCode(getMailActivationRequest()).observe(this, activationResponseResponse -> {
                if (activationResponseResponse != null) {
                    SharedUtils.getInstance().cancelDialog();
                    if (activationResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > activationResponseResponse.code()) {
                        showSnackBar("Code Sent Successfully.");
                    } else if (activationResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                        logout();
                    } else {
                        showSnackBar("error code :" + activationResponseResponse.code());
                    }
                }

            });
        } else {
            showSnackBar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void openNextActivityWithToken(String activationToken) {
        Intent intent = new Intent(PhoneActivitationActivity.this, ChangePasswordActivity.class);
        intent.putExtra(ConfigurationFile.Constants.MAIL_NAME, email);
        intent.putExtra(ConfigurationFile.Constants.TOKN_VALUE, activationToken);
        startActivity(intent);
        finish();
    }

    private ActivationRequest getMailActivationRequest() {
        ActivationRequest activationRequest = new ActivationRequest();
        activationRequest.setMail(email);
        return activationRequest;
    }

    private ActivationRequest getCodeActivationRequest() {
        ActivationRequest activationRequest = new ActivationRequest();
        activationRequest.setMail(email);
        activationRequest.setActivationCode(binding.pinView.getValue());
        return activationRequest;
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}
