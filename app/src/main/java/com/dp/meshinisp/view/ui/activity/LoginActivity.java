package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.WindowManager;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        makeActionOnClickOntvNewUserSignUp();
        makeActionOnClickOntvForgetPassword();
        makeActionOnClickOnbtLogin();
    }

    private void makeActionOnClickOnbtLogin() {
        binding.btLogin.setOnClickListener(v -> openIntent(MainActivity.class));
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
