package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class RequestClientModel {
    @SerializedName("trips_count")
    private int tripsCount;

    @SerializedName("profile_picture")
    private String profilePictureUrl;

    @SerializedName("rating")
    private float rating;

    @SerializedName("phone")
    private String phone;

    @SerializedName("device_token")
    private String deviceToken;

    public int getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(int tripsCount) {
        this.tripsCount = tripsCount;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
