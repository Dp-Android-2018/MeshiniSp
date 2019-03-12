package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityTripsBinding;


public class TripsActivity extends AppCompatActivity {

    ActivityTripsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_trips);
        setupToolbar();
    }

    private void setupToolbar() {
        binding.tripsToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.tripsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
