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
import android.widget.ImageView;
import android.widget.Toast;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRequestDetailsBinding;
import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.dp.meshinisp.service.model.request.OfferRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.RequestDetailsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import kotlin.Lazy;
import ng.max.slideview.SlideView;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestDetailsActivity extends AppCompatActivity {
    ActivityRequestDetailsBinding binding;
    private int requestId;
    private String requestType;
    private String offerPrice;
    private RequestDetailsModel data;
    Lazy<RequestDetailsViewModel> requestDetailsViewModelLazy = inject(RequestDetailsViewModel.class);
    private final int CALL_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_details);
        requestType = getIntent().getStringExtra(ConfigurationFile.Constants.REQUEST_Type);
        requestId = getIntent().getIntExtra(ConfigurationFile.Constants.REQUEST_ID, 0);
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
                binding.connectLinearLayout.setVisibility(View.INVISIBLE);
                binding.btSlideToStartTrip.setVisibility(View.INVISIBLE);
                break;

            case ConfigurationFile.Constants.TRIPS_TYPE_PAST:
                binding.btSendOffer.setVisibility(View.INVISIBLE);
                binding.connectLinearLayout.setVisibility(View.INVISIBLE);
                binding.btSlideToStartTrip.setVisibility(View.INVISIBLE);
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
        binding.connectLinearLayout.setVisibility(View.INVISIBLE);
        binding.btSlideToStartTrip.setVisibility(View.INVISIBLE);
        binding.btSendOffer.setVisibility(View.GONE);
        binding.etOfferAmount.setVisibility(View.VISIBLE);
        binding.btOk.setVisibility(View.VISIBLE);
        doneSendingOffer();
    }

    private void initializeUpcomingScreen() {
        offerPrice = getIntent().getStringExtra(ConfigurationFile.Constants.OFFER_PRICE);
        binding.etOfferAmount.setText(offerPrice);
        binding.connectLinearLayout.setVisibility(View.VISIBLE);
        binding.btSlideToStartTrip.setVisibility(View.VISIBLE);
        binding.btSendOffer.setVisibility(View.GONE);
        binding.etOfferAmount.setVisibility(View.VISIBLE);
        binding.btOk.setVisibility(View.VISIBLE);
        doneSendingOffer();
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
            } else {
                Gson gson = new GsonBuilder().create();
                ErrorResponse errorResponse = new ErrorResponse();

                try {
                    errorResponse = gson.fromJson(requestDetailsResponseResponse.errorBody().string(), ErrorResponse.class);
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
//        binding.collapsingToolbar.setBackgroundColor(getResources().getColor(R.color.transparent)); ?attr/colorPrimary
        binding.requestsToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        binding.requestsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void makeActionOnBtnSendOffer() {
        binding.btSendOffer.setOnClickListener(v -> {
            binding.btSendOffer.setVisibility(View.GONE);
            binding.etOfferAmount.setVisibility(View.VISIBLE);
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
                                } else {
                                    showErrorMessage(offerResponseResponse);
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

    private void showErrorMessage(Response<MessageResponse> offerResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(offerResponseResponse.errorBody().string(), ErrorResponse.class);
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
            Intent intent = new Intent(RequestDetailsActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    private void makeActionOnBtnCall() {
        binding.btCall.setOnClickListener(v -> {
            callPhoneNumber();
            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+Integer.parseInt(data.getClient().getPhone())));
            startActivity(callIntent);*/
        });
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
       /* binding.btSlideToStartTrip.setOnStateChangeListener(active ->
                Snackbar.make(binding.getRoot(), "state : " + active, Snackbar.LENGTH_SHORT)
        );*/

        SlideView slideView = binding.btSlideToStartTrip;
        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                // vibrate the device
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);

                // go to a new activity
                startActivity(new Intent(RequestDetailsActivity.this, OffersActivity.class));
            }
        });

    }
}
