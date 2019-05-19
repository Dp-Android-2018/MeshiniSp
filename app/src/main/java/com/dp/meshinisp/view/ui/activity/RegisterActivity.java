package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegisterBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.viewmodel.Register1ViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class RegisterActivity extends BaseActivity {

    ActivityRegisterBinding binding;
    Lazy<Register1ViewModel> registerViewModelLazy = inject(Register1ViewModel.class);
    Lazy<RegisterRequest> registerRequestLazy = inject(RegisterRequest.class);
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    SpinnerAdapter countrySpinnerAdapter;
    SpinnerAdapter citySpinnerAdapter;
    RegisterRequest registerRequest;
    RegisterRequest registerRequest2;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerRequest = registerRequestLazy.getValue();
        registerRequest2 = registerRequestLazy.getValue();
        setCountrySpinner();
    }

    public void setCountrySpinner() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            registerViewModelLazy.getValue().getCountries().observe(this, (List<CountryCityResponseModel> countryCityPojos) -> {
                SharedUtils.getInstance().cancelDialog();
                countrySpinnerAdapter = new SpinnerAdapter(RegisterActivity.this, countryCityPojos);
                binding.spCountry.setAdapter(countrySpinnerAdapter);
                binding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CountryCityResponseModel selectedCountry = (CountryCityResponseModel) parent.getItemAtPosition(position);
                        setCitySpinner(selectedCountry.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        showSnackbar("please select country");
                    }
                });
            });
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    public void setCitySpinner(int countryId) {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            registerViewModelLazy.getValue().getCities(countryId).observe(this, countryCityPojos -> {
                SharedUtils.getInstance().cancelDialog();
                citySpinnerAdapter = new SpinnerAdapter(RegisterActivity.this, countryCityPojos);
                binding.spCity.setAdapter(citySpinnerAdapter);
                binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CountryCityResponseModel selectedCity = (CountryCityResponseModel) parent.getItemAtPosition(position);
                        registerRequest.setCityId(selectedCity.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        showSnackbar("please select city");
                    }
                });
            });
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    public void getDataFromView(View view) {
        firstName = binding.etFirstName.getText().toString();
        lastName = binding.etLastName.getText().toString();
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();
        phone = binding.etPhone.getText().toString();

        if (registerRequest.getCityId() == 0) {
            showSnackbar(getString(R.string.select_city_error_message));
        }
        if (!ValidationUtils.isEmpty(firstName) && !ValidationUtils.isEmpty(lastName)
                && !ValidationUtils.isEmpty(password) && (password.length() >= 8) && !ValidationUtils.isEmpty(email)
                && !ValidationUtils.isEmpty(phone) && ValidationUtils.validateTexts(phone, ValidationUtils.TYPE_PHONE) && ValidationUtils.isMail(email)) {
            checkPhoneAndMail();
        } else {
            showAllErrors();

        }
    }

    private void checkPhoneAndMail() {
        SharedUtils.getInstance().showProgressDialog(this);
        registerViewModelLazy.getValue().checkMailAndPhone(email, phone).observe(this, loginRegisterResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (loginRegisterResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE
                    || loginRegisterResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE_SECOND) {
                addDataToRegisterRequest();
                openNextActivity();
            } else if (loginRegisterResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                logout();
            } else {
                showErrors(loginRegisterResponseResponse);
            }
        });
    }

    private void showErrors(Response<Void> loginRegisterResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(loginRegisterResponseResponse.errorBody().string(), ErrorResponse.class);
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

    private RegisterRequest getCheckRequest() {
        registerRequest2.setPhone(phone);
        registerRequest2.setEmail(email);
        return registerRequest2;
    }

    private void openNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
        intent.putExtra(ConfigurationFile.Constants.REGISTER1DATA, registerRequest);
        startActivity(intent);
        finish();
    }

    private void addDataToRegisterRequest() {
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setPhone(phone);
    }

    private void showAllErrors() {
        if (ValidationUtils.isEmpty(firstName)) {
            showSnackbar(getString(R.string.enter_first_name_error_message));
            return;
        }
        if (ValidationUtils.isEmpty(lastName)) {
            showSnackbar(getString(R.string.enter_last_name_error_message));
            return;
        }
        if (ValidationUtils.isEmpty(email)) {
            showSnackbar(getString(R.string.enter_mail_error_message));
            return;
        }
        if (ValidationUtils.isEmpty(password)) {
            showSnackbar(getString(R.string.enter_password_error_message));
            return;
        }
        if (ValidationUtils.isEmpty(phone)) {
            showSnackbar(getString(R.string.enter_phone_error_message));
            return;
        }

        if (!ValidationUtils.validateTexts(phone, ValidationUtils.TYPE_PHONE)) {
            showSnackbar(getString(R.string.invalid_phone_error_message));
            return;
        }

        if (!ValidationUtils.isMail(email)) {
            showSnackbar(getString(R.string.invalid_mail_error_message));
            return;
        }
        if (password.length() < 8) {
            showSnackbar(getString(R.string.password_length_message_error));
        }
    }

    public void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }
}
