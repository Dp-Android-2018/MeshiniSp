package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.response.CountryCityResponse;
import com.dp.meshinisp.service.model.response.SearchRequestsResponse;

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

public class RequestsRepository {
    private Lazy<ApiInterfaces> endPointsLazy = inject(ApiInterfaces.class);

    public LiveData<Response<SearchRequestsResponse>> searchForRequests(int pageId,int countryId,String startDate,String endDate) {
        MutableLiveData<Response<SearchRequestsResponse>> data = new MutableLiveData<>();
        endPointsLazy.getValue().searchForRequests(pageId,countryId,startDate,endDate)
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
}
