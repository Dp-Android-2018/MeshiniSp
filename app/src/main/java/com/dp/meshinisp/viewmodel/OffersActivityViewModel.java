package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.model.response.OffersResponse;
import com.dp.meshinisp.service.repository.remotes.OffersRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class OffersActivityViewModel extends AndroidViewModel {
    private LiveData<Response<OffersResponse>> data;
    private LiveData<Response<MessageResponse>> deletedOfferResponse;

    private Lazy<OffersRepository> offersRepositoryLazy = inject(OffersRepository.class);

    public OffersActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public  void getAllOffers(int pageNumber) {
        data= offersRepositoryLazy.getValue().getAllOffers(pageNumber);
    }

    public  void deleteSpecificOffer(int offerId) {
        deletedOfferResponse= offersRepositoryLazy.getValue().deleteSpecificOffer(offerId);
    }

    public LiveData<Response<OffersResponse>> getData() {
        return data;
    }

    public LiveData<Response<MessageResponse>> getDeletedOfferResponse() {
        return deletedOfferResponse;
    }
}
