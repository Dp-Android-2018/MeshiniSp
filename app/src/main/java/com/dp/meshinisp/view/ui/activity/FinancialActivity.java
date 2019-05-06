package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityFinancialBinding;
import com.dp.meshinisp.service.model.global.FinancialResponseData;
import com.dp.meshinisp.service.model.response.ErrorResponse;
import com.dp.meshinisp.service.model.response.FinancialResponse;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.SharedUtils;
import com.dp.meshinisp.utility.utils.ValidationUtils;
import com.dp.meshinisp.viewmodel.FinancialViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Lazy;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class FinancialActivity extends BaseActivity {

    ActivityFinancialBinding binding;
    private Lazy<FinancialViewModel> financialViewModelLazy = inject(FinancialViewModel.class);
    private String startDate;
    private String endDate;
    private Date c;
    private Calendar cal;
    private SimpleDateFormat df;
    private boolean isFirstClick;
    private boolean isForwardArrowClick;
    private boolean isBackArrowClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cal = Calendar.getInstance();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_financial);
        getCurrentDate();
        setupToolbar();
        getThisMonthData();
        makeActionOnClickOnArrows();
    }

    private void makeActionOnClickOnArrows() {
        binding.ivForwardArrow.setOnClickListener(v -> {
            if (!isFirstClick) {
                cal.add(Calendar.MONTH, -1);
                isFirstClick = true;
            } else if (isBackArrowClick) {
                cal.add(Calendar.MONTH, -1);
                isBackArrowClick = false;
            }
            cal.add(Calendar.MONTH, -1);
            c = cal.getTime();
            endDate = startDate;
            startDate = df.format(c);
            getThisMonthData();
            isForwardArrowClick = true;
        });

        binding.ivBackArrow.setOnClickListener(v -> {
            if (isForwardArrowClick) {
                cal.add(Calendar.MONTH, 1);
                isForwardArrowClick = false;
            }
            cal.add(Calendar.MONTH, 1);
            c = cal.getTime();
            startDate = endDate;
            endDate = df.format(c);
            getThisMonthData();
            isBackArrowClick = true;
        });
    }

    private void getCurrentDate() {
        c = cal.getTime();
        startDate = df.format(c);
        cal.add(Calendar.MONTH, 1);
        c = cal.getTime();
        endDate = df.format(c);

    }

    private void getThisMonthData() {
        if (ValidationUtils.isConnectingToInternet(this)) {
            SharedUtils.getInstance().showProgressDialog(this);
            financialViewModelLazy.getValue().getFinancialData(startDate, endDate).observe(this, new Observer<Response<FinancialResponse>>() {
                @Override
                public void onChanged(Response<FinancialResponse> financialResponseResponse) {
                    SharedUtils.getInstance().cancelDialog();
                    if (financialResponseResponse.code() >= ConfigurationFile.Constants.SUCCESS_CODE_FROM
                            && ConfigurationFile.Constants.SUCCESS_CODE_TO > financialResponseResponse.code()) {
                        if (financialResponseResponse.body() != null) {
                            updateUiWithData(financialResponseResponse.body().getData());
                        }
                    } else if (financialResponseResponse.code() == ConfigurationFile.Constants.LOGGED_IN_BEFORE_CODE) {
                        FinancialActivity.this.logout();
                    } else {
                        if (financialResponseResponse.errorBody() != null) {
                            showFinancialErrorMessage(financialResponseResponse.errorBody());
                        }
                    }
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.there_is_no_internet_connection));
        }
    }

    public void showFinancialErrorMessage(ResponseBody errorResponseBody) {
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
        showSnackBar(error);
    }

    private void updateUiWithData(FinancialResponseData financialResponseData) {
        binding.tvStartDate.setText(startDate);
        binding.tvEndDate.setText(endDate);
        binding.tvName.setText(financialResponseData.getName());
        binding.tvTripsCompletedNo.setText(String.valueOf(financialResponseData.getCompletedTripsNum()));
        binding.tvRateNumber.setText(financialResponseData.getRatingNum());
        binding.tvFeesAmount.setText(financialResponseData.getRevenueData().getFees());
        binding.tvTotalRevenueAmount.setText(financialResponseData.getRevenueData().getTotalRevenue());
        binding.tvProfit.setText(financialResponseData.getRevenueData().getTotalProfit());
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        binding.financialToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.financialToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
