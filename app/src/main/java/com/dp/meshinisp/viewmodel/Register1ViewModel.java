package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.model.response.LoginRegisterResponse;
import com.dp.meshinisp.service.repository.remotes.Register1Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class Register1ViewModel extends AndroidViewModel {

    private Lazy<Register1Repository> registerRepositoryLazy = inject(Register1Repository.class);

    public Register1ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<LoginRegisterResponse>> register(RegisterRequest registerRequest) {
        return registerRepositoryLazy.getValue().register(registerRequest);
    }

    public LiveData<Response<Void>> checkMailAndPhone(String mail, String phone) {
        return registerRepositoryLazy.getValue().checkMailAndPhone(mail,phone);
    }

    public LiveData<List<CountryCityResponseModel>> getCountries() {
        return registerRepositoryLazy.getValue().getCountries();
    }

    public LiveData<List<CountryCityResponseModel>> getCities(int countryId) {
        return registerRepositoryLazy.getValue().getCities(countryId);
    }

}
