package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.request.RateTripRequest;
import com.dp.meshinisp.service.model.request.StartDestinationRequest;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.repository.remotes.StartTripRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class StartTripViewModel extends AndroidViewModel {

    private Lazy<StartTripRepository> offersRepositoryLazy = inject(StartTripRepository.class);

    public StartTripViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<Void>> setNextDestination(int requestId, StartDestinationRequest startDestinationRequest) {
        return offersRepositoryLazy.getValue().setNextDestination(requestId, startDestinationRequest);
    }

    public LiveData<Response<Void>> setDoneDestination(int requestId, StartDestinationRequest startDestinationRequest) {
        return offersRepositoryLazy.getValue().setDoneDestination(requestId, startDestinationRequest);
    }

    public LiveData<Response<MessageResponse>> finishTrip(int requestId) {
        return offersRepositoryLazy.getValue().finishTrip(requestId);
    }

    public LiveData<Response<MessageResponse>> rateTrip(int requestId, RateTripRequest rateTripRequest) {
        return offersRepositoryLazy.getValue().rateTrip(requestId, rateTripRequest);
    }


}
