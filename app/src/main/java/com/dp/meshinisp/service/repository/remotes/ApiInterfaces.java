package com.dp.meshinisp.service.repository.remotes;

import com.dp.meshinisp.service.model.request.ActivationRequest;
import com.dp.meshinisp.service.model.request.ChangePasswordRequest;
import com.dp.meshinisp.service.model.request.LoginRequest;
import com.dp.meshinisp.service.model.request.OfferRequest;
import com.dp.meshinisp.service.model.request.ProfileInfoRequest;
import com.dp.meshinisp.service.model.request.RegisterRequest;
import com.dp.meshinisp.service.model.response.ActivationResponse;
import com.dp.meshinisp.service.model.response.CountryCityResponse;
import com.dp.meshinisp.service.model.response.LoginRegisterResponse;
import com.dp.meshinisp.service.model.response.LoginResponse;
import com.dp.meshinisp.service.model.response.MessageResponse;
import com.dp.meshinisp.service.model.response.OffersResponse;
import com.dp.meshinisp.service.model.response.RequestDetailsResponse;
import com.dp.meshinisp.service.model.response.SearchRequestsResponse;
import com.dp.meshinisp.service.model.response.TripsResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Observable<Response<ActivationResponse>> resetPassword(@Body ActivationRequest activationRequest, @Path("token") String token);

    //Search requests (service provider)
    @GET("/api/service-provider/request/search")
    Observable<Response<SearchRequestsResponse>> searchForRequests(@Query("page") int pageNumber, @Query("country_id") int countryId, @Query("start_date") String startDate, @Query("end_date") String endDate);

    //Get countries & cities (tourist)
    @GET("/api/utilities/countries")
    Observable<Response<CountryCityResponse>> getTouristCountries();


    //Get request data by id (service provider)
    @GET("/api/service-provider/request/{request}")
    Observable<Response<RequestDetailsResponse>> getRequestDetails(@Path("request") int requestId);

    //Get request data by id (service provider)
    @POST("/api/service-provider/request/{request}/send-offer")
    Observable<Response<MessageResponse>> sendOffer(@Path("request") int requestId, @Body OfferRequest offerRequest);

    //Update profile info
    @PUT("/api/service-provider/profile")
    Observable<Response<MessageResponse>> updateProfileInfo(@Body ProfileInfoRequest profileInfoRequest);

    //Change password (service provider)
    @POST("/api/service-provider/change-password")
    Observable<Response<MessageResponse>> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    //Get all offers for a specific service provider
    @GET("/api/service-provider/offers")
    Observable<Response<OffersResponse>> getAllOffers(@Query("page") int pageNumber);

    //List past requests (service provider)
    @GET("/api/service-provider/request/past")
    Observable<Response<TripsResponse>> listPastRequests(@Query("page") int pageNumber);

    //List upcoming requests (service provider)
    @GET("/api/service-provider/request/upcoming")
    Observable<Response<TripsResponse>> listUpcomingRequests(@Query("page") int pageNumber);

    //Delete a specific offer
    @DELETE("/api/service-provider/offers/{offer}")
    Observable<Response<MessageResponse>> deleteSpecificOffer(@Path("offer") int offerId);

}
