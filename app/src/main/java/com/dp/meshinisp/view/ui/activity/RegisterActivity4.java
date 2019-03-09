package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister4Binding;

public class RegisterActivity4 extends AppCompatActivity {

    ActivityRegister4Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,(R.layout.activity_register4));

        binding.btNext.setOnClickListener(v -> {
            Intent intent=new Intent(RegisterActivity4.this, RegisterActivity5.class);
            startActivity(intent);
        });
    }
}
