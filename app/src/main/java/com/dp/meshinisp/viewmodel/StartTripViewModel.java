package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.response.OffersResponse;
import com.dp.meshinisp.service.repository.remotes.OffersRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class StartTripViewModel extends AndroidViewModel {
    private LiveData<Response<OffersResponse>> data;

    private Lazy<OffersRepository> offersRepositoryLazy = inject(OffersRepository.class);

    public StartTripViewModel(@NonNull Application application) {
        super(application);
    }

    public  void getAllOffers(int pageNumber) {
        data= offersRepositoryLazy.getValue().getAllOffers(pageNumber);
    }

    public LiveData<Response<OffersResponse>> getData() {
        return data;
    }

}
