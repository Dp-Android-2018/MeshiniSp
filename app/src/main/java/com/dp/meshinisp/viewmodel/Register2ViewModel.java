package com.dp.meshinisp.viewmodel;

import android.app.Application;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.repository.remotes.Register2Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class Register2ViewModel extends AndroidViewModel {

    private Lazy<Register2Repository> registerRepositoryLazy=inject(Register2Repository.class);

    public Register2ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<CountryCityResponseModel>> getLanguages(){
        return registerRepositoryLazy.getValue().getLanguages();
    }



}
