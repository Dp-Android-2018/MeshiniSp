package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class TripsResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("country")
    private String country;

    @SerializedName("date")
    private String date;

    @SerializedName("vehicle_type")
    private String vehicleType;

    @SerializedName("vehicle_type_string")
    private String vehicleTypeString;

    @SerializedName("pickup_address")
    private String pickupAddress;

    @SerializedName("service_provider")
    private String serviceProvider;

    @SerializedName("offer_price")
    private String offerPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleTypeString() {
        return vehicleTypeString;
    }

    public void setVehicleTypeString(String vehicleTypeString) {
        this.vehicleTypeString = vehicleTypeString;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }
}
