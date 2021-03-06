package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.request.OfferRequest;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.model.response.RequestDetailsResponse;
import com.dp.meshinisp.service.model.response.StartTripResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class RequestDetailsRepository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);

    public LiveData<Response<RequestDetailsResponse>> getRequestDetails(int requestId) {
        MutableLiveData<Response<RequestDetailsResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().getRequestDetails(requestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<RequestDetailsResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<RequestDetailsResponse> requestDetailsResponseResponse) {
                        data.setValue(requestDetailsResponseResponse);
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

    public LiveData<Response<MessageResponse>> sendOffer(int requestId, OfferRequest offerRequest) {
        MutableLiveData<Response<MessageResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().sendOffer(requestId, offerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MessageResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MessageResponse> offerResponseResponse) {
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

    public LiveData<Response<StartTripResponse>> startTrip(int requestId) {
        MutableLiveData<Response<StartTripResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().startTrip(requestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<StartTripResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<StartTripResponse> startTripResponseResponse) {
                        data.setValue(startTripResponseResponse);
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
