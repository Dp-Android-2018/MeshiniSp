package com.dp.meshinisp.view.ui.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityMainBinding;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.DateTimePicker;
import com.dp.meshinisp.view.ui.callback.OnDateTimeSelected;

import im.delight.android.webview.AdvancedWebView;

//implements AdvancedWebView.Listener

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    private AdvancedWebView mWebView;
    public static DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeDrawerandNavigationView();
        setupToolbar();

       binding.navigationView.tvNavItem1.setOnClickListener(v -> {
           Intent intent=new Intent(MainActivity.this,TripsActivity.class);
           startActivity(intent);
       });

       binding.navigationView.tvNavItem2.setOnClickListener(v -> {
           Intent intent=new Intent(MainActivity.this, OffersActivity.class);
           startActivity(intent);
       });

//       binding.navigationView.tvNavItem5.setOnClickListener(v -> openPlayStoreToRateApp());

        binding.navigationView.navigationViewHeaderLayout.findViewById(R.id.v_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openPlayStoreToRateApp() {
        Uri uri = Uri.parse(ConfigurationFile.Constants.MARKET_URL + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(ConfigurationFile.Constants.PLAYSTORE_URL + this.getPackageName())));
        }
    }

    private void setupToolbar() {
        binding.mainToolbar.setNavigationIcon(R.drawable.ic_menu);
        binding.mainToolbar.setNavigationOnClickListener(v -> MainActivity.drawer.openDrawer(GravityCompat.START));
    }

    private void initializeDrawerandNavigationView() {
        drawer = binding.drawerLayout;
        ConstraintLayout content = binding.contentLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.Open, R.string.Close) {
            private float scaleFactor = 6f;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                /*if (customUtils.getSavedLanguageType().equals(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC)) {
                    content.setTranslationX(-slideX);
                } else {
                    content.setTranslationX(slideX);
                }*/
                content.setTranslationX(slideX);
                content.setScaleX(1 - (slideOffset / scaleFactor));
                content.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.setDrawerElevation(0f);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void showDialog(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View v = View.inflate(this, R.layout.request_guide_dialog, null);
        builder.setView(v);
        builder.setCancelable(true);
        Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        makeActionOnLayoutComponents(v);
        dialog.show();
    }

    private void makeActionOnLayoutComponents(View v) {
        EditText fromEditText = v.findViewById(R.id.et_dialog_from);
        EditText toEditText = v.findViewById(R.id.et_dialog_to);
        EditText countryEditText = v.findViewById(R.id.et_dialog_country);
        Button btnSearch = v.findViewById(R.id.bt_search_for_requests);
        pickDateAndTime(fromEditText);
        pickDateAndTime(toEditText);
        makeSearch(btnSearch);
    }

    private void makeSearch(Button btnSearch) {
        btnSearch.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, RequestDetailsActivity.class);
            startActivity(intent);
        });
    }

    private void pickDateAndTime(EditText editText) {
        editText.setOnClickListener(v -> DateTimePicker.getInstance().showDatePickerDialog(binding.getRoot().getContext(), new OnDateTimeSelected() {
            @Override
            public void onDateTimeReady(String date, String time) {
                String dateTimeVal = date + " " + time;
                editText.setText(dateTimeVal);
            }
        }));
    }
}
