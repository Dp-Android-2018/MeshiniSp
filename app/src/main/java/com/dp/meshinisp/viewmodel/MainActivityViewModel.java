package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.response.SearchRequestsResponse;
import com.dp.meshinisp.service.repository.remotes.MainRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class MainActivityViewModel extends AndroidViewModel {

    private Lazy<MainRepository> mainRepositoryLazy = inject(MainRepository.class);

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<SearchRequestsResponse>> searchForRequests(int pageId,int countryId,String startDate,String endDate) {
        return mainRepositoryLazy.getValue().searchForRequests(pageId,countryId,startDate,endDate,"COD");
    }

    public LiveData<List<CountryCityResponseModel>> getCountries() {
        return mainRepositoryLazy.getValue().getCountries();
    }

}
