package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRequestDetailsBinding;
import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.dp.meshinisp.service.model.request.OfferRequest;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.OfferResponse;
import com.dp.meshinisp.service.model.response.RequestDetailsResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.RequestDetailsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestDetailsActivity extends AppCompatActivity {
    ActivityRequestDetailsBinding binding;
    private int requestId;
    Lazy<RequestDetailsViewModel> requestDetailsViewModelLazy = inject(RequestDetailsViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_details);
        requestId = getIntent().getIntExtra(ConfigurationFile.Constants.REQUEST_ID, 0);
        setupToolbar();
        initializeViewModel();
        makeActionOnBtnSendOffer();
        makeActionOnBtnOk();
        makeActionOnBtnStartChat();
        makeActionOnBtnCall();
        makeActionOnBtnStartTrip();
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
        requestDetailsViewModelLazy.getValue().getData().observe(this, new Observer<Response<RequestDetailsResponse>>() {
            @Override
            public void onChanged(Response<RequestDetailsResponse> requestDetailsResponseResponse) {
                SharedUtils.getInstance().cancelDialog();
                if (requestDetailsResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE
                        || requestDetailsResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE_SECOND) {
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
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void initializeUiWithData(RequestDetailsModel data) {
        binding.tvRate.setText(String.valueOf(data.getClient().getRating()));
        binding.ratingBar.setRating(data.getClient().getRating());
        binding.tvCountry.setText(data.getCountry());
        binding.tvPickupLocation.setText(data.getPickupAddress());
        String dateAndTime=data.getPickupDate()+" "+data.getPickupTime();
        binding.tvDateAndTime.setText(dateAndTime);
        binding.tvVehicle.setText(data.getVehicleType());
        binding.tvTripsNo.setText(String.valueOf(data.getClient().getTripsCount()));
        String tripSchedule = "";
        for (String string : data.getPlaces()) {
            tripSchedule += string;
            tripSchedule += "\n";
        }
        binding.tvTripSchedule.setText(tripSchedule);

    }

    private void setupToolbar() {
        binding.collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        binding.collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER);
//        binding.collapsingToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
//        binding.collapsingToolbar.setNavigationOnClickListener(v -> onBackPressed());

//        binding.collapsingToolbar.setNavigationOnClickListener(v -> onBackPressed());
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
                            .observe(this, new Observer<Response<OfferResponse>>() {
                                @Override
                                public void onChanged(Response<OfferResponse> offerResponseResponse) {
                                    SharedUtils.getInstance().cancelDialog();
                                    if (offerResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE
                                            || offerResponseResponse.code() == ConfigurationFile.Constants.SUCCESS_CODE_SECOND) {
                                        doneSendingOffer();
                                    } else {
                                        showErrorMessage(offerResponseResponse);
                                    }
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
        OfferRequest offerRequest=new OfferRequest();
        offerRequest.setPrice(Integer.parseInt(binding.etOfferAmount.getText().toString()));
        return offerRequest;
    }

    private void showErrorMessage(Response<OfferResponse> offerResponseResponse) {
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
        showSnackbar(getString(R.string.offer_sent_successfully));
        binding.btOk.setBackground(getResources().getDrawable(R.drawable.btn_background_black));
        binding.btOk.setTextColor(getResources().getColor(R.color.white_90));
        binding.etOfferAmount.setBackground(getResources().getDrawable(R.drawable.edittext_background_black));
        binding.etOfferAmount.setFocusable(false);
        binding.etOfferAmount.setEnabled(false);
    }

    private void makeActionOnBtnStartChat() {
        binding.btStartChat.setOnClickListener(v -> {
            Intent intent = new Intent(RequestDetailsActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    private void makeActionOnBtnCall() {
        binding.btCall.setOnClickListener(v -> {
            Intent intent = new Intent(RequestDetailsActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }

    private void makeActionOnBtnStartTrip() {
        binding.btSlideToStartTrip.setOnStateChangeListener(active -> Snackbar.make(binding.getRoot(), "state : " + active, Snackbar.LENGTH_SHORT));

    }
}
