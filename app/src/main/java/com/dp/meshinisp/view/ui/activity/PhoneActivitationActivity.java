package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityPhoneActivitationBinding;

public class PhoneActivitationActivity extends AppCompatActivity {

    ActivityPhoneActivitationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_phone_activitation);

        binding.btResend.setOnClickListener(v -> {
            Intent intent=new Intent(PhoneActivitationActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }
}
