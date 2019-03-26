package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegisterBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.viewmodel.Register1ViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    Lazy<Register1ViewModel> registerViewModelLazy=inject(Register1ViewModel.class);
    Lazy<RegisterRequest> registerRequestLazy=inject(RegisterRequest.class);
    SpinnerAdapter countrySpinnerAdapter;
    SpinnerAdapter citySpinnerAdapter;
    RegisterRequest registerRequest;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register);
        registerRequest=registerRequestLazy.getValue();
        setCountrySpinner();
    }

    public void setCountrySpinner(){
        if (ValidationUtils.isConnectingToInternet(this)) {
            registerViewModelLazy.getValue().getCountries().observe(this, (List<CountryCityResponseModel> countryCityPojos) -> {
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
        }else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    public void setCitySpinner(int countryId){
        if (ValidationUtils.isConnectingToInternet(this)) {
            registerViewModelLazy.getValue().getCities(countryId).observe(this, countryCityPojos -> {
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
        }else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    public void getDataFromView(View view){
         firstName=binding.etFirstName.getText().toString();
         lastName=binding.etLastName.getText().toString();
         email=binding.etEmail.getText().toString();
         password=binding.etPassword.getText().toString();
         phone=binding.etPhone.getText().toString();

        if(registerRequest.getCityId()==0){
            showSnackbar(getString(R.string.select_city_error_message));
        }
        if (!ValidationUtils.isEmpty(firstName) && !ValidationUtils.isEmpty(lastName)
        &&!ValidationUtils.isEmpty(password) && (password.length() >= 8) &&!ValidationUtils.isEmpty(email)
                &&!ValidationUtils.isEmpty(phone)){
            addDataToRegisterRequest();
            openNextActivity();
        }else {
            showAllErrors();

        }
    }

    private void openNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,RegisterActivity2.class);
        intent.putExtra(ConfigurationFile.Constants.REGISTER1DATA,registerRequest);
        startActivity(intent);
    }

    private void addDataToRegisterRequest() {
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setPhone(phone);
    }

    private void showAllErrors() {
        if(ValidationUtils.isEmpty(firstName)){
            showSnackbar(getString(R.string.enter_first_name_error_message));
            return;
        }
        if (ValidationUtils.isEmpty(lastName)){
            showSnackbar(getString(R.string.enter_last_name_error_message));
            return;
        }
        if(ValidationUtils.isEmpty(email)){
            showSnackbar(getString(R.string.enter_mail_error_message));
            return;
        }
        if(ValidationUtils.isEmpty(password)){
            showSnackbar(getString(R.string.enter_password_error_message));
            return;
        }
        if (ValidationUtils.isEmpty(phone)){
            showSnackbar(getString(R.string.enter_phone_error_message));
            return;
        }
        if(!ValidationUtils.isMail(email)){
            showSnackbar(getString(R.string.invalid_mail_error_message));
            return;
        }
        if(password.length()<8){
            showSnackbar(getString(R.string.password_length_message_error));
        }
    }

    public void showSnackbar(String message){
        Snackbar.make(binding.getRoot(),message,Snackbar.LENGTH_LONG).show();
    }
}
