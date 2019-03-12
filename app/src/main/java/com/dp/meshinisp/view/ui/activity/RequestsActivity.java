package com.dp.meshinisp.view.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRequestsBinding;

public class RequestsActivity extends AppCompatActivity {

    ActivityRequestsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_requests);
        setupToolbar();
    }

    private void setupToolbar() {
        binding.requestsToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.requestsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
