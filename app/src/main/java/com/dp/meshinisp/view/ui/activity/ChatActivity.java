package com.dp.meshinisp.view.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityChatBinding;

public class ChatActivity extends BaseActivity {

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat);

        setupToolbar();

    }

    private void setupToolbar() {
        binding.chatToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.chatToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
