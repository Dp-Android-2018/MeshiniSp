package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.response.TripsResponse;
import com.dp.meshinisp.service.repository.remotes.TripsRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class TripsActivityViewModel extends AndroidViewModel {
    private LiveData<Response<TripsResponse>> data;

    private Lazy<TripsRepository> tripsRepositoryLazy = inject(TripsRepository.class);

    public TripsActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void listPastRequests(int pageNumber) {
        data = tripsRepositoryLazy.getValue().listPastRequests(pageNumber);
    }

    public void listUpcomingRequests(int pageNumber) {
        data = tripsRepositoryLazy.getValue().listUpcomingRequests(pageNumber);
    }

    public LiveData<Response<TripsResponse>> getData() {
        return data;
    }
}
