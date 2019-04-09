package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.model.response.CountryCityResponse;
import com.dp.meshinisp.service.model.response.EmptyResponse;
import com.dp.meshinisp.service.model.response.LoginRegisterResponse;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class Register1Repository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);

    public LiveData<List<CountryCityResponseModel>> getCities(int countryId) {
        MutableLiveData<List<CountryCityResponseModel>> cityResponse = new MutableLiveData<>();
        endPointsLazy.getValue().getCities(countryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CountryCityResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<CountryCityResponse> countryCityResponseResponse) {
                        if (countryCityResponseResponse.body() != null) {
                            cityResponse.setValue(countryCityResponseResponse.body().getCountryCityList());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return cityResponse;
    }

    public LiveData<List<CountryCityResponseModel>> getCountries() {
        MutableLiveData<List<CountryCityResponseModel>> countryResponse = new MutableLiveData<>();
        endPointsLazy.getValue().getCountries()
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return countryResponse;
    }


    public LiveData<Response<LoginRegisterResponse>> register(RegisterRequest registerRequest) {
        MutableLiveData<Response<LoginRegisterResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().register(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoginRegisterResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<LoginRegisterResponse> loginRegisterResponseResponse) {
                        data.setValue(loginRegisterResponseResponse);
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

    public LiveData<Response<Void>> checkMailAndPhone(String mail,String phone) {
        MutableLiveData<Response<Void>> data = new MutableLiveData<>();
        endPointsLazy.getValue().checkMailAndPhone(mail,phone)
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
