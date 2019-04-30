package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.ActiveTripResponseModel;
import com.google.gson.annotations.SerializedName;

public class ActiveTripResponse {
    @SerializedName("data")
    private ActiveTripResponseModel data;

    public ActiveTripResponseModel getData() {
        return data;
    }

    public void setData(ActiveTripResponseModel data) {
        this.data = data;
    }
}
