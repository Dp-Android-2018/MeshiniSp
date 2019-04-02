package com.dp.meshinisp.view.ui.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityMainBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.DateTimePicker;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.SpinnerAdapter;
import com.dp.meshinisp.view.ui.callback.OnDateTimeSelected;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

//implements AdvancedWebView.Listener

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    public static DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);
    private EditText fromEditText;
    private EditText toEditText;
    private AppCompatSpinner countrySpinner;
    private Dialog dialog;
    private SpinnerAdapter countrySpinnerAdapter;
    private CountryCityResponseModel selectedCountry;
    private int countryId;
    private View v;
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeDrawerandNavigationView();
        initializeUiData();
        setupToolbar();
        makeActionOnClickOnMenuItems();
        initializeShowStateDialog();

    }

    private void initializeShowStateDialog() {
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        makeActionOnLayoutComponents(layoutBottomSheet.getRootView());
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        View v = layoutBottomSheet.getRootView();
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void initializeUiData() {
        TextView tvName = binding.navigationView.navigationViewHeaderLayout.vAccount.findViewById(R.id.tv_user_name);
        TextView tvrate = binding.navigationView.navigationViewHeaderLayout.vAccount.findViewById(R.id.tv_user_rate);
        ImageView userImageView = binding.navigationView.navigationViewHeaderLayout.vAccount.findViewById(R.id.im_user_image);

        tvName.setText(customUtilsLazy.getValue().getSavedMemberData().getFirstName());
        tvrate.setText(customUtilsLazy.getValue().getSavedMemberData().getEmail());

        Picasso.get().load(customUtilsLazy.getValue().getSavedMemberData().getProfilePictureUrl()).into(userImageView);
    }

    private void makeActionOnClickOnMenuItems() {
        binding.navigationView.tvNavItem1.setOnClickListener(v -> openActivity(TripsActivity.class));

        binding.navigationView.tvNavItem2.setOnClickListener(v -> openActivity(OffersActivity.class));

        binding.navigationView.tvNavItem6.setOnClickListener(v -> logout());

        binding.navigationView.tvNavItem5.setOnClickListener(v -> openPlayStoreToRateApp());

        binding.navigationView.navigationViewHeaderLayout.vAccount.setOnClickListener(v -> openActivity(AccountActivity.class));
    }

    private void logout() {
        customUtilsLazy.getValue().clearSharedPref();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openActivity(Class activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
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
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            btnBottomSheet.setText("Expand sheet");
        }

        /*final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        View v = View.inflate(this, R.layout.request_guide_dialog, null);
        builder.setView(v);
        builder.setCancelable(true);
        dialog = builder.create();
        Window window = dialog.getWindow();
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        makeActionOnLayoutComponents(v);
        dialog.show();*/
    }

    private void makeActionOnLayoutComponents(View v) {
        this.v = v;
        fromEditText = v.findViewById(R.id.et_dialog_from);
        toEditText = v.findViewById(R.id.et_dialog_to);
        countrySpinner = v.findViewById(R.id.sp_country);
        Button btnSearch = v.findViewById(R.id.bt_search_for_requests);
        pickDateAndTime(fromEditText);
        pickDateAndTime(toEditText);
        setCountrySpinner();
        makeSearch(btnSearch);
    }

    public void setCountrySpinner() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            mainActivityViewModelLazy.getValue().getCountries().observe(this, (List<CountryCityResponseModel> countryCityPojos) -> {
                countrySpinnerAdapter = new SpinnerAdapter(MainActivity.this, countryCityPojos);
                countrySpinner.setAdapter(countrySpinnerAdapter);
                countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedCountry = (CountryCityResponseModel) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        showSnackbarOnDialogView(getString(R.string.please_select_country));
                    }
                });
            });
        } else {
            showSnackbarOnDialogView(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void makeSearch(Button btnSearch) {
        btnSearch.setOnClickListener(v -> {
            if (!fromEditText.getText().toString().isEmpty()
                    && !toEditText.getText().toString().isEmpty()) {
                if (ValidationUtils.isConnectingToInternet(this)) {
                    SharedUtils.getInstance().showProgressDialog(this);
                    makeSearchRequest();
                } else {
                    showSnackbarOnDialogView(getString(R.string.there_is_no_internet_connection));
                }
            } else {
                showErrors();
            }
        });
    }

    private void makeSearchRequest() {
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        countryId = selectedCountry.getId();
        mainActivityViewModelLazy.getValue().searchForRequests(1, countryId, fromEditText.getText().toString(), toEditText.getText().toString())
                .observe(this, searchRequestsResponseResponse -> {
                    SharedUtils.getInstance().cancelDialog();
                    if (searchRequestsResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > searchRequestsResponseResponse.code()) {
                        if (searchRequestsResponseResponse.body() != null) {
                            if (!searchRequestsResponseResponse.body().getData().isEmpty()) {
                                openActivityRequests();
                            } else {
                                showSnackbarOnDialogView(getString(R.string.there_is_no_requests_available));
                            }
                        }
                    } else {
                        Gson gson = new GsonBuilder().create();
                        ErrorResponse errorResponse = new ErrorResponse();

                        try {
                            errorResponse = gson.fromJson(searchRequestsResponseResponse.errorBody().string(), ErrorResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String error = "";
                        for (String string : errorResponse.getErrors()) {
                            error += string;
                            error += "\n";
                        }
                        showSnackbarOnDialogView(error);
                    }
                });
    }

    public void showSnackbarOnDialogView(String message) {
        Snackbar.make(v.getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void openActivityRequests() {
//        dialog.dismiss();
        Intent intent = new Intent(this, RequestsActivity.class);
        intent.putExtra(ConfigurationFile.Constants.COUNTRY_ID, countryId);
        intent.putExtra(ConfigurationFile.Constants.DATE_FROM, fromEditText.getText().toString());
        intent.putExtra(ConfigurationFile.Constants.DATE_TO, toEditText.getText().toString());
        startActivity(intent);
        clearAllFields();
    }

    private void clearAllFields() {
        fromEditText.setText("");
        toEditText.setText("");
    }

    private void showErrors() {
        if (fromEditText.getText().toString().isEmpty()) {
            showSnackbarOnDialogView(getString(R.string.please_select_start_date));
            return;
        }

        if (toEditText.getText().toString().isEmpty()) {
            showSnackbarOnDialogView(getString(R.string.please_select_end_date));
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void pickDateAndTime(EditText editText) {
        editText.setOnClickListener(v -> DateTimePicker.getInstance().showDatePickerDialog(binding.getRoot().getContext(), new OnDateTimeSelected() {
            @Override
            public void onDateReady(String date) {
                editText.setText(date);
            }
        }));
    }
}
