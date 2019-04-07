package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class StartTripResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("lat")
    private float locationLat;

    @SerializedName("long")
    private float locationLong;

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

    public float getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(float locationLat) {
        this.locationLat = locationLat;
    }

    public float getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(float locationLong) {
        this.locationLong = locationLong;
    }
}
