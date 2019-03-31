package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.response.SearchRequestsResponse;
import com.dp.meshinisp.service.repository.remotes.MainRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestsActivityViewModel extends AndroidViewModel {
    private LiveData<Response<SearchRequestsResponse>> data;

    private Lazy<MainRepository> mainRepositoryLazy = inject(MainRepository.class);

    public RequestsActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public  void searchForRequests(int pageId,int countryId,String startDate,String endDate) {
        data= mainRepositoryLazy.getValue().searchForRequests(pageId,countryId,startDate,endDate);
    }

    public LiveData<Response<SearchRequestsResponse>> getData() {
        return data;
    }
}
