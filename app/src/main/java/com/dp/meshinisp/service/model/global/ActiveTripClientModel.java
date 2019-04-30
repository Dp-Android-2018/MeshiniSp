package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class ActiveTripClientModel {
    @SerializedName("name")
    private String name;

    @SerializedName("profile_picture")
    private String profilePictureUrl;

    @SerializedName("phone")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
