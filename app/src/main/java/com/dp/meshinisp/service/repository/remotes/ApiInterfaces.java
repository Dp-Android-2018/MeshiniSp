package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.request.LoginRequest;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;
import com.dp.meshinisp.service.model.response.CountryCityResponse;
import com.dp.meshinisp.service.model.response.DefaultResponse;
import com.dp.meshinisp.service.model.response.LoginRegisterResponse;
import com.dp.meshinisp.service.model.response.LoginResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaces {

    //get all countries
    @GET("/api/utilities/world-countries")
    Observable<Response<CountryCityResponse>> getCountries();

    //get all cities
    @GET("/api/utilities/world-cities")
    Observable<Response<CountryCityResponse>> getCities(@Query("world_country_id") int id);

    //register
    @POST("/api/service-provider/register")
    Observable<Response<LoginRegisterResponse>> register(@Body RegisterRequest request);

    //Get languages
    @GET("/api/utilities/languages")
    Observable<Response<CountryCityResponse>> getLanguages();

    //Login (service provider)
    @POST("/api/service-provider/login")
    Observable<Response<LoginResponse>> login(@Body LoginRequest loginRequest);

    //Send phone Activation Code
    @POST("/api/service-provider/phone/send")
    Observable<Response<ActivationResponse>> sendActivationCode(@Body ActivationRequest activationRequest);

    //Forget password (service provider)
    @POST("/api/service-provider/forget")
    Observable<Response<ActivationResponse>> getActivationToken(@Body ActivationRequest activationRequest);

    //Reset password (service provider)
    @POST("/api/service-provider/forget/reset/{token}")
    Observable<Response<ActivationResponse>> resetPassword(@Body ActivationRequest activationRequest ,@Path("token") String token);
}
