package com.dp.meshinisp.view.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityOffersBinding;

public class OffersActivity extends AppCompatActivity {

    ActivityOffersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_offers);
        setupToolbar();
    }

    private void setupToolbar() {
        binding.offersToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.offersToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
