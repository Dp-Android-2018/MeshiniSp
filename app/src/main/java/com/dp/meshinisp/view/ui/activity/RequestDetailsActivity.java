package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRequestDetailsBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class RequestDetailsActivity extends AppCompatActivity {
    ActivityRequestDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_details);

        setupToolbar();

        binding.btSlideToStartChat.setOnStateChangeListener(active -> Snackbar.make(binding.getRoot(), "state : " + active, Snackbar.LENGTH_SHORT));

        binding.btSendOffer.setOnClickListener(v -> {
            binding.btSendOffer.setVisibility(View.GONE);
            binding.etOfferAmount.setVisibility(View.VISIBLE);
            binding.btOk.setVisibility(View.VISIBLE);
        });

        binding.btOk.setOnClickListener(v -> {
            binding.btOk.setBackground(getResources().getDrawable(R.drawable.btn_background_black));
            binding.btOk.setTextColor(getResources().getColor(R.color.white_90));
            binding.etOfferAmount.setBackground(getResources().getDrawable(R.drawable.edittext_background_black));
            binding.etOfferAmount.setFocusable(false);
            binding.etOfferAmount.setEnabled(false);
        });

        binding.btStartChat.setOnClickListener(v -> {
            Intent intent=new Intent(RequestDetailsActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    private void setupToolbar() {
        binding.collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent));
//        binding.collapsingToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
