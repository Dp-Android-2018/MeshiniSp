package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StartTripResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<StartTripResponseModel> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<StartTripResponseModel> getData() {
        return data;
    }

    public void setData(ArrayList<StartTripResponseModel> data) {
        this.data = data;
    }
}
