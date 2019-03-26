package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;
import com.dp.meshinisp.service.repository.remotes.ActivationRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class ChangePasswordViewModel extends AndroidViewModel {

    private Lazy<ActivationRepository> activationRepositoryLazy = inject(ActivationRepository.class);

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<ActivationResponse>> resetPassword(ActivationRequest activationRequest,String token) {
        return activationRepositoryLazy.getValue().resetPassword(activationRequest,token);
    }
}
