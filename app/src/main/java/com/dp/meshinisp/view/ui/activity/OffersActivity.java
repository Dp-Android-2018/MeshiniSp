package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityOffersBinding;
import com.dp.meshinisp.service.model.global.OffersResponseModel;
import com.dp.meshinisp.service.model.response.OffersResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.view.ui.adapter.OffersRecyclerViewAdapter;
import com.dp.meshinisp.viewmodel.OffersActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
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
    private int pageId = ConfigurationFile.Constants.PAGE_ID;
    private String next_page = null;
    private ArrayList<OffersResponseModel> loadedData;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private int position = 0;
    private Lazy<OffersActivityViewModel> offersActivityViewModelLazy = inject(OffersActivityViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_offers);
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
        makeOnScrollOnRecyclerView();
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
        pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
        SharedUtils.getInstance().showProgressDialog(this);
        offersActivityViewModelLazy.getValue().getAllOffers(pageId);
        observeViewmodel();
    }

    private void observeViewmodel() {
        offersActivityViewModelLazy.getValue().getData().observe(this, new Observer<Response<OffersResponse>>() {
            @Override
            public void onChanged(Response<OffersResponse> offersResponseResponse) {
                SharedUtils.getInstance().cancelDialog();
                if (!offersResponseResponse.body().getData().isEmpty()) {
                    addDataToLoadedData(offersResponseResponse.body());
                }
            }
        });
    }

    private void addDataToLoadedData(OffersResponse body) {
        for (int i = 0; i < body.getData().size(); i++) {
            loadedData.add(body.getData().get(i));
        }
       /* loading = true;
        if (body.getPageLinks().getNextPageLink() != null) {
            next_page = body.getPageLinks().getNextPageLink();
            pageId = Integer.parseInt(next_page.substring(next_page.length() - 1));
        } else {
            next_page = null;
        }
        if (loadedData.isEmpty() && (next_page != null)) {
            offersActivityViewModelLazy.getValue().getAllOffers(pageId);
            observeViewmodel();
        }*/
        offersRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setupToolbar() {
        binding.offersToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.offersToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}
