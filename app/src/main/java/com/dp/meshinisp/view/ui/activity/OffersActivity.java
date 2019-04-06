package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityOffersBinding;
import com.dp.meshinisp.service.model.global.OffersResponseModel;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.model.response.OffersResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.OffersRecyclerViewAdapter;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.viewmodel.OffersActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class OffersActivity extends AppCompatActivity {

    ActivityOffersBinding binding;
    private int countryId;
    private String startDate, endDate;
    LinearLayoutManager linearLayoutManager;
    private OffersRecyclerViewAdapter offersRecyclerViewAdapter;
    private int pageId = ConfigurationFile.Constants.DEFAULT_PAGE_ID;
    private String next_page = ConfigurationFile.Constants.DEFAULT_STRING_VALUE;
    private ArrayList<OffersResponseModel> loadedData;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int position = 0;
    private Lazy<OffersActivityViewModel> offersActivityViewModelLazy = inject(OffersActivityViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers);
        loadedData = new ArrayList<>();
        setupToolbar();
        initializeViewModel();
        initializeRecyclerViewAdapter();
    }

    private void initializeViewModel() {
        if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(this))) {
            SharedUtils.getInstance().showProgressDialog(this);
            offersActivityViewModelLazy.getValue().getAllOffers(pageId);
            observeViewmodel();
        } else {
            Snackbar.make(binding.getRoot(), R.string.there_is_no_internet_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initializeRecyclerViewAdapter() {
        offersRecyclerViewAdapter = new OffersRecyclerViewAdapter(loadedData);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvOffers.setLayoutManager(linearLayoutManager);
        binding.rvOffers.setAdapter(offersRecyclerViewAdapter);
        makeActionOnClickOnRecyclerViewItem();
        makeOnScrollOnRecyclerView();
    }

    private void makeActionOnClickOnRecyclerViewItem() {
        offersRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                opnDetailsActivity(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void removeItem(int position) {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            offersActivityViewModelLazy.getValue().deleteSpecificOffer(loadedData.get(position).getId());
            observeDeleteSpecificOfferData();
        } else {
            showSnackbr(getString(R.string.there_is_no_internet_connection));
        }
    }

    private void observeDeleteSpecificOfferData() {
        offersActivityViewModelLazy.getValue().getDeletedOfferResponse().observe(this, messageResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (messageResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > messageResponseResponse.code()) {
                loadedData.remove(position);
                offersRecyclerViewAdapter.notifyItemRemoved(position);
                if (messageResponseResponse.body() != null) {
                    showSnackbr(messageResponseResponse.body().getMessage());
                }
            } else {
                showErrors(messageResponseResponse);
            }
        });
    }

    private void showErrors(Response<MessageResponse> messageResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(messageResponseResponse.errorBody().string(), ErrorResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String error = "";
        for (String string : errorResponse.getErrors()) {
            error += string;
            error += "\n";
        }
        showSnackbr(error);
    }

    public void opnDetailsActivity(int position, String text) {
        Intent intent = new Intent(OffersActivity.this, RequestDetailsActivity.class);
        intent.putExtra(ConfigurationFile.Constants.REQUEST_ID, loadedData.get(position).getRequestId());
        intent.putExtra(ConfigurationFile.Constants.REQUEST_Type, ConfigurationFile.Constants.OFFERS_TYPE_ACTIVITY);
        intent.putExtra(ConfigurationFile.Constants.OFFER_PRICE, String.valueOf(loadedData.get(position).getOffer()));
        startActivity(intent);
    }

    private void makeOnScrollOnRecyclerView() {
        binding.rvOffers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > ConfigurationFile.Constants.DEFAULT_INTEGER_VALUE) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (loading && (next_page != null)) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loadMoreData();
                        }
                    }
                }
            }
        });
    }

    private void loadMoreData() {
        loading = false;
        position = totalItemCount;
        pageId++;
        SharedUtils.getInstance().showProgressDialog(this);
        offersActivityViewModelLazy.getValue().getAllOffers(pageId);
        observeViewmodel();
    }

    private void observeViewmodel() {
        offersActivityViewModelLazy.getValue().getData().observe(this, offersResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (offersResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > offersResponseResponse.code()) {
                if (offersResponseResponse.body() != null) {
                    if (!offersResponseResponse.body().getData().isEmpty()) {
                        addDataToLoadedData(offersResponseResponse.body());
                    }
                }
            } else {
                Gson gson = new GsonBuilder().create();
                ErrorResponse errorResponse = new ErrorResponse();

                try {
                    errorResponse = gson.fromJson(offersResponseResponse.errorBody().string(), ErrorResponse.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String error = "";
                for (String string : errorResponse.getErrors()) {
                    error += string;
                    error += "\n";
                }
                showSnackbr(error);
            }

        });
    }

    private void addDataToLoadedData(OffersResponse body) {
        for (int i = 0; i < body.getData().size(); i++) {
            loadedData.add(body.getData().get(i));
        }
        loading = true;
        if (body.getPageLinks().getNextPageLink() != null) {
            next_page = body.getPageLinks().getNextPageLink();
        } else {
            next_page = null;
        }
        if (loadedData.isEmpty() && (next_page != null)) {
            offersActivityViewModelLazy.getValue().getAllOffers(pageId);
            observeViewmodel();
        }
        offersRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupToolbar() {
        binding.offersToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.offersToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showSnackbr(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}
