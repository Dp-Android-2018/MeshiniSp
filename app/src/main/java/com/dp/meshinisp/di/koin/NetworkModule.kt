package com.dp.meshinisp.di.koin

import android.content.Context
import com.dp.meshinisp.di.modules.NetworkModule
import com.dp.meshinisp.service.repository.remotes.ApiInterfaces
import com.dp.meshinisp.utility.utils.ConfigurationFile
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

val NetworkModule = module {

    single { provideOkHttpClient() }
    single { provideRetrofit(androidApplication(), get()) }
    single { getRetrofitApiInterfaces(get()) }
}

@Provides
@Singleton
internal fun provideOkHttpClient(): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
    okHttpClient.addInterceptor { chain ->
        val original = chain.request()
        // Request customization: add request headers
        val requestBuilder = original.newBuilder()
                .header("Content-Type", ConfigurationFile.Constants.CONTENT_TYPE)
                .header("Accept", ConfigurationFile.Constants.ACCEPT)
                .header("x-api-key", ConfigurationFile.Constants.API_KEY)
                .header("Accept-Language", ConfigurationFile.Constants.ACCEPT_LANGUAGE)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    return okHttpClient.build()
}

@Provides
@Singleton
internal fun provideRetrofit(context: Context, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(ConfigurationFile.UrlConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
}

@Provides
@Singleton
internal fun getRetrofitApiInterfaces(retrofit: Retrofit): ApiInterfaces {
    return retrofit.create(ApiInterfaces::class.java)
}