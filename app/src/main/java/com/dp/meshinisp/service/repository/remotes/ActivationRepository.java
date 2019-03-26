package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class ActivationRepository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);

    public LiveData<Response<ActivationResponse>> sendActivationCode(ActivationRequest activationRequest) {
        MutableLiveData<Response<ActivationResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().sendActivationCode(activationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ActivationResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ActivationResponse> activationResponseResponse) {
                        data.setValue(activationResponseResponse);
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

    public LiveData<Response<ActivationResponse>> getActivationToken(ActivationRequest activationRequest) {
        MutableLiveData<Response<ActivationResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().getActivationToken(activationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ActivationResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ActivationResponse> activationResponseResponse) {
                        data.setValue(activationResponseResponse);
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

 public LiveData<Response<ActivationResponse>> resetPassword(ActivationRequest activationRequest,String token) {
        MutableLiveData<Response<ActivationResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().resetPassword(activationRequest,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ActivationResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ActivationResponse> activationResponseResponse) {
                        data.setValue(activationResponseResponse);
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
