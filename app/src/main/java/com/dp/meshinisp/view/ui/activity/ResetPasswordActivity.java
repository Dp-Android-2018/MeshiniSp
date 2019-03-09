package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityResetPasswordBinding;


public class ResetPasswordActivity extends AppCompatActivity {

    ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_reset_password);

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResetPasswordActivity.this,PhoneActivitationActivity.class);
                startActivity(intent);
            }
        });


    }
}
