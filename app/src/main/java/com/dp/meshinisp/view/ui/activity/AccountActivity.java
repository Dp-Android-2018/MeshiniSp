package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityAccountBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class AccountActivity extends AppCompatActivity {

    ActivityAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_account);
        setupToolbar();
    }

    private void setupToolbar() {
        binding.accountToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.accountToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
