package com.dp.meshinisp.view.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
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
import com.dp.meshinisp.RequestDetailsActivity;
import com.dp.meshinisp.databinding.ActivityMainBinding;
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
       /* mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.loadUrl("https://xd.adobe.com/view/2607fb8f-de60-46a9-7c40-fee07a90fd9d-5036/?fullscreen");*/
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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RequestDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void pickDateAndTime(EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker.getInstance().showDatePickerDialog(binding.getRoot().getContext(), new OnDateTimeSelected() {
                    @Override
                    public void onDateTimeReady(String date, String time) {
                        String dateTimeVal = date + " " + time;
                        editText.setText(dateTimeVal);
                    }
                });
            }
        });
    }

    /*@Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }*/
}
