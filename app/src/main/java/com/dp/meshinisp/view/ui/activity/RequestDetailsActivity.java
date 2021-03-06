package com.dp.meshinisp.view.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRequestDetailsBinding;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.dp.meshinisp.service.model.global.ServiceProviderData;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.service.model.request.OfferRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.model.response.StartTripResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.utility.utils.firebase.classes.FirebaseToken;
import com.dp.meshinisp.viewmodel.RequestDetailsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import kotlin.Lazy;
import ng.max.slideview.SlideView;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestDetailsActivity extends BaseActivity {
    ActivityRequestDetailsBinding binding;
    private int requestId;
    private String requestType;
    private String offerPrice;
    private RequestDetailsModel data;
    Lazy<RequestDetailsViewModel> requestDetailsViewModelLazy = inject(RequestDetailsViewModel.class);
    private final int CALL_REQUEST = 100;
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private DatabaseReference reference;
    private String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_details);
        requestType = getIntent().getStringExtra(ConfigurationFile.Constants.REQUEST_Type);
        requestId = getIntent().getIntExtra(ConfigurationFile.Constants.REQUEST_ID, 0);
        ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        FirebaseToken.getInstance().getFirebaseToken().observe(this, s -> deviceToken = s);
        checkRequestType(requestType);
        setupToolbar();
        initializeViewModel();
        makeActionOnBtnSendOffer();
        makeActionOnBtnOk();
        makeActionOnBtnStartChat();
        makeActionOnBtnCall();
        makeActionOnBtnStartTrip();
    }

    private void checkRequestType(String requestType) {
        switch (requestType) {
            case ConfigurationFile.Constants.FROM_REQUESTS_TYPE:
                binding.btSendOffer.setVisibility(View.VISIBLE);
                binding.connectLinearLayout.setVisibility(View.GONE);
                binding.btSlideToStartTrip.setVisibility(View.GONE);
                binding.btSlideToStartTripLayout.setVisibility(View.GONE);
                break;

            case ConfigurationFile.Constants.TRIPS_TYPE_PAST:
                binding.btSendOffer.setVisibility(View.GONE);
                binding.connectLinearLayout.setVisibility(View.GONE);
                binding.btSlideToStartTrip.setVisibility(View.GONE);
                binding.btSlideToStartTripLayout.setVisibility(View.GONE);
                break;

            case ConfigurationFile.Constants.TRIPS_TYPE_UPCOMING:
                initializeUpcomingScreen();
                break;

            case ConfigurationFile.Constants.OFFERS_TYPE_ACTIVITY:
                initializeOffersScreen();
                break;
        }
    }

    private void initializeOffersScreen() {
        offerPrice = getIntent().getStringExtra(ConfigurationFile.Constants.OFFER_PRICE);
        binding.etOfferAmount.setText(offerPrice);
        binding.connectLinearLayout.setVisibility(View.GONE);
        binding.btSlideToStartTrip.setVisibility(View.GONE);
        binding.btSlideToStartTripLayout.setVisibility(View.GONE);
        binding.btSendOffer.setVisibility(View.GONE);
        binding.etOfferAmount.setVisibility(View.VISIBLE);
        binding.btOk.setVisibility(View.VISIBLE);
        doneSendingOffer();
        offerSent();
        binding.btSendOffer.setText(getResources().getString(R.string.offer_sent));
    }

    private void offerSent() {
        binding.btSendOffer.setFocusable(false);
        binding.btSendOffer.setEnabled(false);
        binding.btSendOffer.setVisibility(View.VISIBLE);
        binding.btSendOffer.setBackground(getResources().getDrawable(R.drawable.btn_background_black));
        binding.btSendOffer.setTextColor(getResources().getColor(R.color.white_90));
        binding.btOk.setVisibility(View.INVISIBLE);
        binding.etOfferAmount.setVisibility(View.INVISIBLE);
        binding.btSendOffer.setText(getResources().getString(R.string.offer_accepted));
    }

    private void initializeUpcomingScreen() {
        offerPrice = getIntent().getStringExtra(ConfigurationFile.Constants.OFFER_PRICE);
        binding.etOfferAmount.setText(offerPrice);
        binding.connectLinearLayout.setVisibility(View.VISIBLE);
        binding.btSlideToStartTrip.setVisibility(View.VISIBLE);
        binding.btSlideToStartTripLayout.setVisibility(View.VISIBLE);
        binding.btSendOffer.setVisibility(View.GONE);
        binding.etOfferAmount.setVisibility(View.VISIBLE);
        binding.btOk.setVisibility(View.VISIBLE);
        doneSendingOffer();
        offerSent();
    }

    private void initializeViewModel() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            requestDetailsViewModelLazy.getValue().searchForRequests(requestId);
            observeViewModel();
        } else {
            showSnackbar(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void observeViewModel() {
        requestDetailsViewModelLazy.getValue().getData().observe(this, requestDetailsResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (requestDetailsResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > requestDetailsResponseResponse.code()) {
                if (requestDetailsResponseResponse.body() != null)
                    initializeUiWithData(requestDetailsResponseResponse.body().getData());
            } else if (requestDetailsResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                logout();
            } else {
                showErrorMessage(requestDetailsResponseResponse.errorBody());
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void initializeUiWithData(RequestDetailsModel data) {
        this.data = data;
        binding.tvRate.setText(String.valueOf(data.getClient().getRating()));
        binding.ratingBar.setRating(data.getClient().getRating());
        binding.tvCountry.setText(data.getCountry());
        binding.tvPickupLocation.setText(data.getPickupAddress());
        String dateAndTime = data.getPickupDate() + " " + data.getPickupTime();
        binding.tvDateAndTime.setText(dateAndTime);
        binding.tvVehicle.setText(data.getVehicleType());
        binding.tvTripsNo.setText(String.valueOf(data.getClient().getTripsCount()));
        binding.tvClientName.setText(String.valueOf(data.getClient().getClientName()));
        String tripSchedule = "";
        for (String string : data.getPlaces()) {
            tripSchedule += string;
            tripSchedule += "\n";
        }
        binding.tvTripSchedule.setText(tripSchedule);

        ImageView ivFeedPhoto = binding.circleImageView2;
        Picasso.get().load(data.getClient().getProfilePictureUrl()).into(ivFeedPhoto);

    }

    private void setupToolbar() {
        binding.collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        binding.collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER);
        binding.requestsToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        binding.requestsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void makeActionOnBtnSendOffer() {
        binding.btSendOffer.setOnClickListener(v -> {
            binding.btSendOffer.setVisibility(View.GONE);
            binding.etOfferAmount.setVisibility(View.VISIBLE);
            binding.btOk.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right_anim));
            binding.btOk.setVisibility(View.VISIBLE);
        });
    }

    private void makeActionOnBtnOk() {
        binding.btOk.setOnClickListener(v -> {
            if (!binding.etOfferAmount.getText().toString().isEmpty()) {
                if (ValidationUtils.isConnectingToInternet(this)) {
                    SharedUtils.getInstance().showProgressDialog(this);
                    requestDetailsViewModelLazy.getValue().sendOffer(requestId, getOfferRequest())
                            .observe(this, offerResponseResponse -> {
                                SharedUtils.getInstance().cancelDialog();
                                if (offerResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                                        && ConfigurationFile.Constants.SUCCESS_CODE_TO > offerResponseResponse.code()) {
                                    showSnackbar(getString(R.string.offer_sent_successfully));
                                    doneSendingOffer();
                                    offerSent();
                                    binding.btSendOffer.setText(getResources().getString(R.string.offer_sent));
                                } else if (offerResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                                    logout();
                                } else {
                                    showErrorMessage(offerResponseResponse.errorBody());
                                }
                            });
                } else {
                    showSnackbar(getString(R.string.there_is_no_internet_connection));
                }
            } else {
                showSnackbar(getString(R.string.please_enter_offer_amount));
            }

        });
    }

    private OfferRequest getOfferRequest() {
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setPrice(Integer.parseInt(binding.etOfferAmount.getText().toString()));
        return offerRequest;
    }

    private void showErrorMessage(ResponseBody offerResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(offerResponseResponse.string(), ErrorResponse.class);
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

    private void doneSendingOffer() {
        binding.btOk.setBackground(getResources().getDrawable(R.drawable.btn_background_black));
        binding.btOk.setTextColor(getResources().getColor(R.color.white_90));
        binding.etOfferAmount.setBackground(getResources().getDrawable(R.drawable.edittext_background_black));
        binding.etOfferAmount.setFocusable(false);
        binding.etOfferAmount.setEnabled(false);
        binding.btOk.setFocusable(false);
        binding.btOk.setEnabled(false);
    }

    private void makeActionOnBtnStartChat() {
        binding.btStartChat.setOnClickListener(v -> {
            addServiceProviderToFirebase();
        });
    }

    public void addServiceProviderToFirebase() {
        reference = FirebaseDatabase.getInstance().getReference("serviceProviders/"
                + customUtilsLazy.getValue().getSavedMemberData().getUserId() + "/conversations");
        String chatPath = "Meshini" + customUtilsLazy.getValue().getSavedMemberData().getUserId() + "-" + data.getClient().getId() + "-" + data.getId();
        final ServiceProviderData serviceProviderData = new ServiceProviderData(chatPath, customUtilsLazy.getValue().getSavedMemberData().getFirstName(), deviceToken);

        reference.child("user-" + data.getClient().getId()).setValue(serviceProviderData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                openChatActivity();
            } else {
                showSnackbar("Firebase error !!");
            }
        });
    }

    private void openChatActivity() {
        Intent intent = new Intent(RequestDetailsActivity.this, ChatActivity.class);
        intent.putExtra(ConfigurationFile.Constants.USER_DATA, new Gson().toJson(data.getClient()));
        intent.putExtra(ConfigurationFile.Constants.TRIP_ID, data.getId());
        startActivity(intent);
    }

    private void makeActionOnBtnCall() {
        binding.btCall.setOnClickListener(v -> callPhoneNumber());
    }

    public void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(RequestDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + data.getClient().getPhone().trim()));
            startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == CALL_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            } else {
                Toast.makeText(RequestDetailsActivity.this, getResources().getString(R.string.call_permission_denied_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makeActionOnBtnStartTrip() {
        SlideView slideView = binding.btSlideToStartTrip;
        slideView.setOnSlideCompleteListener(slideView1 -> {
            // vibrate the device
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            if (ValidationUtils.isConnectingToInternet(this)) {
                makeStartTripRequest();
            } else {
                showSnackbar(getString(R.string.there_is_no_internet_connection));
            }
        });

    }

    private void makeStartTripRequest() {
        SharedUtils.getInstance().showProgressDialog(this);
        requestDetailsViewModelLazy.getValue().startTrip(requestId).observe(this, startTripResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (startTripResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > startTripResponseResponse.code()) {
                if (startTripResponseResponse.body() != null) {
                    showSnackbar(startTripResponseResponse.body().getMessage());
                    openStartTripActivity(startTripResponseResponse.body().getData());
                }
            } else if (startTripResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                logout();
            } else {
                showStartTripErrorMessage(startTripResponseResponse);
            }
        });
    }

    private void openStartTripActivity(ArrayList<StartTripResponseModel> startTripResponseModels) {
        Intent intent = new Intent(RequestDetailsActivity.this, MainActivity.class);
        intent.putParcelableArrayListExtra(ConfigurationFile.Constants.TRIPS_DATA, startTripResponseModels);
        intent.putExtra(ConfigurationFile.Constants.REQUEST_DATA, data);
        intent.putExtra(ConfigurationFile.Constants.REQUEST_ID, requestId);
        intent.putExtra(ConfigurationFile.Constants.START_TRIP_TYPE, ConfigurationFile.Constants.FROM_REQUEST_DETAILS);
        startActivity(intent);
        finish();
    }

    private void showStartTripErrorMessage(Response<StartTripResponse> startTripResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(startTripResponseResponse.errorBody().string(), ErrorResponse.class);
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
}
