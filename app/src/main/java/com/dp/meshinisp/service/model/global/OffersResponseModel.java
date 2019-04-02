package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class OffersResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    @SerializedName("date")
    private String date;

    @SerializedName("offer")
    private float offer;

    @SerializedName("request_id")
    private int requestId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getOffer() {
        return offer;
    }

    public void setOffer(float offer) {
        this.offer = offer;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
