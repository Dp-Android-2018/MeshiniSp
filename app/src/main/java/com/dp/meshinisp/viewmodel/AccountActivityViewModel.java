package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.request.ProfileInfoRequest;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.repository.remotes.AccountRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class AccountActivityViewModel extends AndroidViewModel {

    private Lazy<AccountRepository> accountRepositoryLazy = inject(AccountRepository.class);

    public AccountActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<MessageResponse>> updateProfileInfo(ProfileInfoRequest profileInfoRequest) {
        return accountRepositoryLazy.getValue().updateProfileInfo(profileInfoRequest);
    }

}
