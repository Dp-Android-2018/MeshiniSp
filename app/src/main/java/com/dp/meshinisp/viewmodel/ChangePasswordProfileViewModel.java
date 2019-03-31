package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.request.ChangePasswordRequest;
import com.dp.meshinisp.service.model.response.OfferResponse;
import com.dp.meshinisp.service.repository.remotes.AccountRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class ChangePasswordProfileViewModel extends AndroidViewModel {

    private Lazy<AccountRepository> accountRepositoryLazy = inject(AccountRepository.class);

    public ChangePasswordProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<OfferResponse>> changePassword(ChangePasswordRequest changePasswordRequest) {
        return accountRepositoryLazy.getValue().changePassword(changePasswordRequest);
    }


}
