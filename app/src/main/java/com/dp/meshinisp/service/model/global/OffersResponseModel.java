package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class OffersResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_picture")
    private String profilePictureUrl;

    @SerializedName("rating")
    private float rating;

    @SerializedName("no_of_trips")
    private int tripsCount;

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

    public int getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(int tripsCount) {
        this.tripsCount = tripsCount;
    }
}
