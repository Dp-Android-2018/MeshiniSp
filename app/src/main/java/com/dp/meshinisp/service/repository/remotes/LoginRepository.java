package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.request.LoginRequest;
import com.dp.meshinisp.service.model.response.LoginResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class LoginRepository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);


    public LiveData<Response<LoginResponse>> login(LoginRequest loginRequest) {
        MutableLiveData<Response<LoginResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoginResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<LoginResponse> loginResponseResponse) {
                            data.setValue(loginResponseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }


}
