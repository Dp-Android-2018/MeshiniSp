package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityTripsBinding;
import com.dp.meshinisp.service.model.global.TripsResponseModel;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.TripsResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.TripsRecyclerViewAdapter;
import com.dp.meshinisp.viewmodel.TripsActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class TripsActivity extends BaseActivity {

    ActivityTripsBinding binding;
    private boolean requestType;
    LinearLayoutManager linearLayoutManager;
    private TripsRecyclerViewAdapter offersRecyclerViewAdapter;
    private int pageId = ConfigurationFile.Constants.DEFAULT_PAGE_ID;
    private String next_page = ConfigurationFile.Constants.DEFAULT_STRING_VALUE;
    private ArrayList<TripsResponseModel> loadedData;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int position = 0;
    private Lazy<TripsActivityViewModel> tripsActivityViewModelLazy = inject(TripsActivityViewModel.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trips);
        loadedData = new ArrayList<>();
        setupToolbar();
        listPastRequests();
        listUpcomingRequests();
        getAndSetPastRequests();
        initializeRecyclerViewAdapter();
    }

    public void listPastRequests() {
        binding.btPast.setOnClickListener(v -> {
            binding.vPast.setVisibility(View.VISIBLE);
            binding.vUpcoming.setVisibility(View.INVISIBLE);
            requestType = false;
            getAndSetPastRequests();
            initializeRecyclerViewAdapter();
        });
    }

    private void getAndSetPastRequests() {
        binding.rvTrips.removeAllViews();
        loadedData.clear();
        if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(this))) {
            SharedUtils.getInstance().showProgressDialog(this);
            tripsActivityViewModelLazy.getValue().listPastRequests(pageId);
            observeViewmodel();
        } else {
            Snackbar.make(binding.getRoot(), R.string.there_is_no_internet_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void listUpcomingRequests() {
        binding.btUpcoming.setOnClickListener(v -> {
            binding.vPast.setVisibility(View.INVISIBLE);
            binding.vUpcoming.setVisibility(View.VISIBLE);
            requestType = true;
            getAndSetUpcomingRequests();
            initializeRecyclerViewAdapter();
        });
    }

    private void getAndSetUpcomingRequests() {
        binding.rvTrips.removeAllViews();
        loadedData.clear();
        if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(this))) {
            SharedUtils.getInstance().showProgressDialog(this);
            tripsActivityViewModelLazy.getValue().listUpcomingRequests(pageId);
            observeViewmodel();
        } else {
            Snackbar.make(binding.getRoot(), R.string.there_is_no_internet_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initializeRecyclerViewAdapter() {
        if (requestType) {
            offersRecyclerViewAdapter = new TripsRecyclerViewAdapter(loadedData, ConfigurationFile.Constants.TRIPS_TYPE_UPCOMING);
        } else {
            offersRecyclerViewAdapter = new TripsRecyclerViewAdapter(loadedData, ConfigurationFile.Constants.TRIPS_TYPE_PAST);
        }

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvTrips.setLayoutManager(linearLayoutManager);
        binding.rvTrips.setAdapter(offersRecyclerViewAdapter);
        makeOnScrollOnRecyclerView();
    }

    private void makeOnScrollOnRecyclerView() {
        binding.rvTrips.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        if (requestType) {
            tripsActivityViewModelLazy.getValue().listUpcomingRequests(pageId);
            observeViewmodel();
        } else {
            tripsActivityViewModelLazy.getValue().listPastRequests(pageId);
            observeViewmodel();
        }
    }

    private void observeViewmodel() {
        tripsActivityViewModelLazy.getValue().getData().observe(this, tripsResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (tripsResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                    && ConfigurationFile.Constants.SUCCESS_CODE_TO > tripsResponseResponse.code()) {
                if (!tripsResponseResponse.body().getData().isEmpty()) {
                    addDataToLoadedData(tripsResponseResponse.body());
                }
            } else if (tripsResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                logout();
            } else {
                showErrorMessage(tripsResponseResponse);
            }
        });
    }

    private void showErrorMessage(Response<TripsResponse> tripsResponseResponse) {
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = new ErrorResponse();

        try {
            errorResponse = gson.fromJson(tripsResponseResponse.errorBody().string(), ErrorResponse.class);
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

    private void addDataToLoadedData(TripsResponse body) {
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
            if (requestType) {
                tripsActivityViewModelLazy.getValue().listUpcomingRequests(pageId);
                observeViewmodel();
            } else {
                tripsActivityViewModelLazy.getValue().listPastRequests(pageId);
                observeViewmodel();
            }
        }
        offersRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupToolbar() {
        binding.tripsToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.tripsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
