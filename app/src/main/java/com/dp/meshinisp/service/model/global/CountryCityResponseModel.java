package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryCityResponseModel {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("id")
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
