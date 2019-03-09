package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister3Binding;

public class RegisterActivity3 extends AppCompatActivity {

    ActivityRegister3Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_register3);

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity3.this, RegisterActivity4.class);
                startActivity(intent);
            }
        });
    }
}
