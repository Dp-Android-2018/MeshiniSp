package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.request.LoginRequest;
import com.dp.meshinisp.service.model.response.LoginResponse;
import com.dp.meshinisp.service.repository.remotes.LoginRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class LoginViewModel extends AndroidViewModel {

    private Lazy<LoginRepository> loginRepositoryLazy = inject(LoginRepository.class);

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<LoginResponse>> login(LoginRequest loginRequest) {
        return loginRepositoryLazy.getValue().login(loginRequest);
    }

}
