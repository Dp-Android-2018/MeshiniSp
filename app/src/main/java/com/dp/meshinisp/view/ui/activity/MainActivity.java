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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityMainBinding;
import com.dp.meshinisp.service.model.global.ActiveTripResponseModel;
import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.service.model.request.ChangeLanguageRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.RequestBottomSheetDialog;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.utility.utils.firebase.classes.ActiveTripFirebase;
import com.dp.meshinisp.utility.utils.firebase.classes.FirebaseDataBase;
import com.dp.meshinisp.view.ui.callback.ActiveTripDataCallback;
import com.dp.meshinisp.view.ui.fragment.MainFragment;
import com.dp.meshinisp.view.ui.fragment.StartTripFragment;
import com.dp.meshinisp.viewmodel.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.Lazy;
import okhttp3.ResponseBody;

import static org.koin.java.standalone.KoinJavaComponent.inject;
//implements AdvancedWebView.Listener

public class MainActivity extends BaseActivity implements RequestBottomSheetDialog.BottomSheetListener {

    ActivityMainBinding binding;
    private Lazy<MainActivityViewModel> mainActivityViewModelLazy = inject(MainActivityViewModel.class);
    public static DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private int requestId;
    private String startTripFrom;
    private RequestDetailsModel data;
    private ArrayList<StartTripResponseModel> startTripResponseModels;
    private Dialog dialog;
    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_ID = "exampleChannel";
    private View changeLanguageView;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        startTripFrom = getIntent().getStringExtra(ConfigurationFile.Constants.START_TRIP_TYPE);
        requestId = getIntent().getIntExtra(ConfigurationFile.Constants.REQUEST_ID, 0);
        data = getIntent().getParcelableExtra(ConfigurationFile.Constants.REQUEST_DATA);
        startTripResponseModels = getIntent().getParcelableArrayListExtra(ConfigurationFile.Constants.TRIPS_DATA);
        notificationManager = NotificationManagerCompat.from(this);
        openSelectedFragmentWithData();
        initializeDrawerandNavigationView();
        initializeUiData();
        makeActionOnClickOnMenuItems();
        checkForActiveTrip();
    }

    private void checkForActiveTrip() {
        FirebaseDataBase firebaseDataBase = new FirebaseDataBase();
        firebaseDataBase.setUserId(customUtilsLazy.getValue().getSavedMemberData().getUserId(), activeTrip -> {
            if (activeTrip) {
                firebaseDataBase.setActiveTripDataCallback(new ActiveTripDataCallback() {
                    @Override
                    public void ActiveTripData(ActiveTripFirebase activeTripFirebase) {
                        if (activeTripFirebase != null) {
                            binding.navigationView.tvNavItem7.setVisibility(View.VISIBLE);
                            binding.navigationView.tvNavItem7.setOnClickListener(v -> MainActivity.this.openActiveTrip(activeTripFirebase));
                        }
                    }
                });
            }
        });
    }

    private void openActiveTrip(ActiveTripFirebase activeTripFirebase) {
        MainActivity.drawer.closeDrawer(GravityCompat.START);
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            mainActivityViewModelLazy.getValue().getActiveTripData()
                    .observe(this, activeTripResponseResponse -> {
                        SharedUtils.getInstance().cancelDialog();
                        if (activeTripResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                                && ConfigurationFile.Constants.SUCCESS_CODE_TO > activeTripResponseResponse.code()) {
                            if (activeTripResponseResponse.body() != null) {
                                openFragmentOfActiveTrip(activeTripFirebase, activeTripResponseResponse.body().getData());
                            }
                        } else if (activeTripResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                            logout();
                        }
                    });
        }
    }

    private void openFragmentOfActiveTrip(ActiveTripFirebase activeTripFirebase, ActiveTripResponseModel activeTripResponseModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ConfigurationFile.Constants.TRIPS_DATA, activeTripResponseModel.getPlaces());
        bundle.putInt(ConfigurationFile.Constants.REQUEST_ID, activeTripFirebase.getRequest_id());
        bundle.putParcelable(ConfigurationFile.Constants.ACTIVE_REQUEST_DATA, activeTripResponseModel);
        bundle.putString(ConfigurationFile.Constants.FIREBASE_REQUEST_DATA, new Gson().toJson(activeTripFirebase));
        StartTripFragment newsDetailsFragment = new StartTripFragment();
        newsDetailsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_container, newsDetailsFragment);
        transaction.commit();
    }

    private void openSelectedFragmentWithData() {
        if (startTripFrom != null) {
            if (startTripFrom.equals(ConfigurationFile.Constants.FROM_REQUEST_DETAILS)) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ConfigurationFile.Constants.TRIPS_DATA, startTripResponseModels);
                bundle.putParcelable(ConfigurationFile.Constants.REQUEST_DATA, data);
                bundle.putInt(ConfigurationFile.Constants.REQUEST_ID, requestId);
                StartTripFragment newsDetailsFragment = new StartTripFragment();
                newsDetailsFragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout_container, newsDetailsFragment);
                transaction.commit();
            }
        } else {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout_container, new MainFragment());
            transaction.commit();
        }
    }

    private void openSelectedFragment(Fragment fragment) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_container, fragment);
        transaction.commit();
    }

    private void initializeUiData() {
        TextView tvName = binding.navigationView.navigationViewHeaderLayout.vAccount.findViewById(R.id.tv_user_name);
        TextView tvrate = binding.navigationView.navigationViewHeaderLayout.vAccount.findViewById(R.id.tv_user_rate);
        ImageView userImageView = binding.navigationView.navigationViewHeaderLayout.vAccount.findViewById(R.id.im_user_image);

        tvName.setText(customUtilsLazy.getValue().getSavedMemberData().getFirstName());
        tvrate.setText(ConfigurationFile.Constants.BRACKET_BEFORE + customUtilsLazy.getValue().getSavedMemberData().getUserRating() + ConfigurationFile.Constants.BRACKET_AFTER);

        Picasso.get().load(customUtilsLazy.getValue().getSavedMemberData().getProfilePictureUrl()).into(userImageView);
    }

    private void makeActionOnClickOnMenuItems() {
        binding.navigationView.tvNavItem1.setOnClickListener(v -> {
            MainActivity.drawer.closeDrawer(GravityCompat.START);
            openActivity(TripsActivity.class);
        });

        binding.navigationView.tvNavItem2.setOnClickListener(v -> openActivity(OffersActivity.class));

        binding.navigationView.tvNavItem4.setOnClickListener(v -> showDialogToChangeLanguage());
        binding.navigationView.tvNavItem5.setOnClickListener(v -> openPlayStoreToRateApp());
        binding.navigationView.tvNavItem6.setOnClickListener(v -> logout());
        binding.navigationView.tvNavItem8.setOnClickListener(v -> openActivity(FinancialActivity.class));

        binding.navigationView.navigationViewHeaderLayout.vAccount.setOnClickListener(v -> openActivity(AccountActivity.class));
        binding.navigationView.navigationViewHeaderLayout.ivLogo.setOnClickListener(v -> openAppMakerDialog());
    }

    private void openAppMakerDialog() {
        count++;
        if (count == 10) {
            Snackbar.make(binding.getRoot(), getResources().getString(R.string.app_maker_name), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.view_linkein_profile), view -> {
                        Intent browserIntent = new
                                Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.linkedin.com/in/mohamedsaidabdelaty"));
                        startActivity(browserIntent);
                    }).show();
            count = 0;
        }
    }

    private void showDialogToChangeLanguage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.change_language_dialog, null);
        builder.setView(v);
        builder.setCancelable(true);
        dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        makeActionOnChangeLanguageDialog(v);
        dialog.show();
    }

    private void makeActionOnChangeLanguageDialog(View view) {
        changeLanguageView = view;
        TextView arabicTextView = (TextView) view.findViewById(R.id.tv_arabic);
        TextView englishTextView = (TextView) view.findViewById(R.id.tv_english);
        TextView frenchTextView = (TextView) view.findViewById(R.id.tv_francais);

        arabicTextView.setOnClickListener(v -> {
            customUtilsLazy.getValue().saveLanguageTypeToPrefs(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC);
            openAppAgain(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC);
        });
        englishTextView.setOnClickListener(v -> {
            customUtilsLazy.getValue().saveLanguageTypeToPrefs(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ENGLISH);
            openAppAgain(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ENGLISH);
        });
        frenchTextView.setOnClickListener(v -> {
            customUtilsLazy.getValue().saveLanguageTypeToPrefs(ConfigurationFile.Constants.ACCEPT_LANGUAGE_FRENCH);
            openAppAgain(ConfigurationFile.Constants.ACCEPT_LANGUAGE_FRENCH);
        });
    }

    private ChangeLanguageRequest getChangeLanguageRequest(String language) {
        ChangeLanguageRequest changeLanguageRequest = new ChangeLanguageRequest();
        changeLanguageRequest.setLanguage(language);
        return changeLanguageRequest;
    }

    private void openAppAgain(String language) {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            mainActivityViewModelLazy.getValue().changeLanguage(getChangeLanguageRequest(language)).observe(this, voidResponse -> {
                SharedUtils.getInstance().cancelDialog();
                if (voidResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                        && ConfigurationFile.Constants.SUCCESS_CODE_TO > voidResponse.code()) {
                    Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else if (voidResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                    logout();
                } else {
                    if (voidResponse.errorBody() != null) {
                        showMainErrorMessage(voidResponse.errorBody());
                    }
                }
            });
        } else {
            Snackbar.make(changeLanguageView, getResources().getString(R.string.there_is_no_internet_connection), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showMainErrorMessage(ResponseBody errorResponseBody) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(errorResponseBody.string(), ErrorResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String error = "";
        for (String string : errorResponse.getErrors()) {
            error += string;
            error += "\n";
        }
        showSnackbar(error);
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }


    private void openActivity(Class activityClass) {
        MainActivity.drawer.closeDrawer(GravityCompat.START);
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


    private void initializeDrawerandNavigationView() {
        drawer = binding.drawerLayout;
        ConstraintLayout content = binding.contentLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.Open, R.string.Close) {
            private float scaleFactor = 6f;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                if (customUtilsLazy.getValue().getSavedLanguageType().equals(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC)) {
                    content.setTranslationX(-slideX);
                } else {
                    content.setTranslationX(slideX);
                }
                content.setScaleX(1 - (slideOffset / scaleFactor));
                content.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.setDrawerElevation(0f);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
