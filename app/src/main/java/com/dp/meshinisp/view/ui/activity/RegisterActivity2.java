package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRegister2Binding;

public class RegisterActivity2 extends AppCompatActivity {

    ActivityRegister2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register2);

        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity2.this, RegisterActivity3.class);
                startActivity(intent);
            }
        });
    }
}
