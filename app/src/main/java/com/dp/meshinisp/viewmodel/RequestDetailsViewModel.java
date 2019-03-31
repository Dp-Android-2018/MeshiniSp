package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.request.OfferRequest;
import com.dp.meshinisp.service.model.response.OfferResponse;
import com.dp.meshinisp.service.model.response.RequestDetailsResponse;
import com.dp.meshinisp.service.repository.remotes.RequestDetailsRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestDetailsViewModel extends AndroidViewModel {
    private LiveData<Response<RequestDetailsResponse>> data;

    private Lazy<RequestDetailsRepository> requestDetailsRepositoryLazy = inject(RequestDetailsRepository.class);

    public RequestDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void searchForRequests(int requestId) {
        data = requestDetailsRepositoryLazy.getValue().getRequestDetails(requestId);
    }

    public LiveData<Response<OfferResponse>> sendOffer(int requestId, OfferRequest offerRequest) {
        return requestDetailsRepositoryLazy.getValue().sendOffer(requestId, offerRequest);
    }


    public LiveData<Response<RequestDetailsResponse>> getData() {
        return data;
    }
}
