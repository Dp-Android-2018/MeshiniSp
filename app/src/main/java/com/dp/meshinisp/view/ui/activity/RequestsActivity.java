package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityRequestsBinding;
import com.dp.meshinisp.service.model.global.RequestsResponseModel;
import com.dp.meshinisp.service.model.response.SearchRequestsResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.RequestsRecyclerViewAdapter;
import com.dp.meshinisp.viewmodel.RequestsActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestsActivity extends AppCompatActivity {

    ActivityRequestsBinding binding;
    private int countryId;
    private String startDate, endDate;
    LinearLayoutManager linearLayoutManager;
    private RequestsRecyclerViewAdapter requestsRecyclerViewAdapter;
    private int pageId = ConfigurationFile.Constants.DEFAULT_PAGE_ID;
    private String next_page = ConfigurationFile.Constants.DEFAULT_STRING_VALUE;
    private ArrayList<RequestsResponseModel> loadedData;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int position = 0;
    private Lazy<RequestsActivityViewModel> requestsActivityViewModelLazy = inject(RequestsActivityViewModel.class);
    private Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_requests);
        countryId = getIntent().getIntExtra(ConfigurationFile.Constants.COUNTRY_ID, 0);
        startDate = getIntent().getStringExtra(ConfigurationFile.Constants.DATE_FROM);
        endDate = getIntent().getStringExtra(ConfigurationFile.Constants.DATE_TO);
        ConfigurationFile.Constants.AUTHORIZATION=customUtilsLazy.getValue().getSavedMemberData().getApiToken();
        loadedData = new ArrayList<>();
        setupToolbar();
        initializeViewModel();
        initializeRecyclerViewAdapter();
    }

    private void initializeViewModel() {
        if (ValidationUtils.isConnectingToInternet(Objects.requireNonNull(this))) {
            SharedUtils.getInstance().showProgressDialog(this);
            requestsActivityViewModelLazy.getValue().searchForRequests(pageId, countryId, startDate, endDate);
            observeViewmodel();
        } else {
            Snackbar.make(binding.getRoot(), R.string.there_is_no_internet_connection, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initializeRecyclerViewAdapter() {
        requestsRecyclerViewAdapter = new RequestsRecyclerViewAdapter(loadedData);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvRequests.setLayoutManager(linearLayoutManager);
        binding.rvRequests.setAdapter(requestsRecyclerViewAdapter);
        makeOnScrollOnRecyclerView();
    }

    private void makeOnScrollOnRecyclerView() {
        binding.rvRequests.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > ConfigurationFile.Constants.DEFAULT_INTEGER_VALUE)
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
//        pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
        pageId++;
        SharedUtils.getInstance().showProgressDialog(this);
        requestsActivityViewModelLazy.getValue().searchForRequests(pageId, countryId, startDate, endDate);
        observeViewmodel();
    }

    private void observeViewmodel() {
        requestsActivityViewModelLazy.getValue().getData().observe(this, searchRequestsResponseResponse -> {
            SharedUtils.getInstance().cancelDialog();
            if (!searchRequestsResponseResponse.body().getData().isEmpty()) {
                addDataToLoadedData(searchRequestsResponseResponse.body());
            }
        });
    }

    private void addDataToLoadedData(SearchRequestsResponse body) {
        for (int i = 0; i < body.getData().size(); i++) {
            loadedData.add(body.getData().get(i));
        }
        loading = true;
        if (body.getPageLinks().getNextPageLink() != null) {
            next_page = body.getPageLinks().getNextPageLink();
//            pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
        } else {
            next_page = null;
        }
        if (loadedData.isEmpty() && (next_page != null)) {
            requestsActivityViewModelLazy.getValue().searchForRequests(pageId, countryId, startDate, endDate);
            observeViewmodel();
        }
        requestsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupToolbar() {
        binding.requestsToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.requestsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
