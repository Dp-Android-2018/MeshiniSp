package com.dp.meshinisp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dp.meshinisp.service.model.request.FinancialRequest;
import com.dp.meshinisp.service.model.response.FinancialResponse;
import com.dp.meshinisp.service.repository.remotes.FinancialRepository;

import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class FinancialViewModel extends AndroidViewModel {

    private Lazy<FinancialRepository> financialRepositoryLazy = inject(FinancialRepository.class);

    public FinancialViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<FinancialResponse>> getFinancialData(String startDate, String endDate) {
        return financialRepositoryLazy.getValue().getFinancialData(startDate, endDate);
    }


}
