package com.dp.meshinisp.di.modules;

import android.content.Context;
import androidx.annotation.NonNull;

import com.dp.meshinisp.service.repository.remotes.ApiInterfaces;
import com.dp.meshinisp.utility.utils.ConfigurationFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Bahaa Gabal on 24,November,2018
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Content-Type", ConfigurationFile.Constants.CONTENT_TYPE)
                        .header("Accept", ConfigurationFile.Constants.ACCEPT)
                        .header("x-api-key", ConfigurationFile.Constants.API_KEY)
                        .header("Accept-Language", ConfigurationFile.Constants.ACCEPT_LANGUAGE);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return okHttpClient.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Context context, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ConfigurationFile.UrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiInterfaces getRetrofitApiInterfaces(Retrofit retrofit) {
        return retrofit.create(ApiInterfaces.class);
    }
}
