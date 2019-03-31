package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.request.ChangePasswordRequest;
import com.dp.meshinisp.service.model.request.ProfileInfoRequest;
import com.dp.meshinisp.service.model.response.OfferResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class AccountRepository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);


    public LiveData<Response<OfferResponse>> updateProfileInfo(ProfileInfoRequest profileInfoRequest) {
        MutableLiveData<Response<OfferResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().updateProfileInfo(profileInfoRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<OfferResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<OfferResponse> offerResponseResponse) {
                        data.setValue(offerResponseResponse);
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

     public LiveData<Response<OfferResponse>> changePassword(ChangePasswordRequest changePasswordRequest) {
        MutableLiveData<Response<OfferResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().changePassword(changePasswordRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<OfferResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<OfferResponse> offerResponseResponse) {
                        data.setValue(offerResponseResponse);
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
