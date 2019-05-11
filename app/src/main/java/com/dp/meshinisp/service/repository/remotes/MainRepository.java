package com.dp.meshinisp.service.repository.remotes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.ChangeLanguageRequest;
import com.dp.meshinisp.service.model.response.ActiveTripResponse;
import com.dp.meshinisp.service.model.response.CountryCityResponse;
import com.dp.meshinisp.service.model.response.SearchRequestsResponse;
import com.dp.meshinisp.view.ui.callback.OnConnectionTimeoutListener;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class MainRepository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);
    private OnConnectionTimeoutListener connectionTimeoutListener;

    public void setConnectionTimeoutListener(OnConnectionTimeoutListener connectionTimeoutListener) {
        this.connectionTimeoutListener = connectionTimeoutListener;
    }

    public LiveData<Response<SearchRequestsResponse>> searchForRequests(int pageId, int countryId, String startDate, String endDate, String paymentMethod) {
        MutableLiveData<Response<SearchRequestsResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().searchForRequests(pageId, countryId, startDate, endDate, paymentMethod)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SearchRequestsResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<SearchRequestsResponse> searchRequestsResponseResponse) {
                        data.setValue(searchRequestsResponseResponse);
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

    public LiveData<List<CountryCityResponseModel>> getCountries() {
        MutableLiveData<List<CountryCityResponseModel>> countryResponse = new MutableLiveData<>();
        endPointsLazy.getValue().getTouristCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CountryCityResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<CountryCityResponse> countryCityResponseResponse) {
                        if (countryCityResponseResponse.body() != null) {
                            countryResponse.setValue(countryCityResponseResponse.body().getCountryCityList());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        connectionTimeoutListener.onConnectionTimeout(true, e.getMessage());
                        e.printStackTrace();

//                        TimeoutException timeoutException = new TimeoutException();
                        /*if (e.equals(timeoutException)) {
                            connectionTimeoutListener.onConnectionTimeout(true,e.getMessage());
                        } else {
                            e.printStackTrace();
                        }*/
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return countryResponse;
    }

    public LiveData<Response<ActiveTripResponse>> getActiveTripData() {
        MutableLiveData<Response<ActiveTripResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().getActiveTripData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ActiveTripResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ActiveTripResponse> activeTripResponseResponse) {
                        data.setValue(activeTripResponseResponse);
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

    public LiveData<Response<Void>> changeLanguage(ChangeLanguageRequest changeLanguageRequest) {
        MutableLiveData<Response<Void>> data = new MutableLiveData<>();
        endPointsLazy.getValue().changeLanguage(changeLanguageRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        data.setValue(voidResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }

    public LiveData<Response<Void>> logout() {
        MutableLiveData<Response<Void>> data = new MutableLiveData<>();
        endPointsLazy.getValue().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        data.setValue(voidResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }


}
