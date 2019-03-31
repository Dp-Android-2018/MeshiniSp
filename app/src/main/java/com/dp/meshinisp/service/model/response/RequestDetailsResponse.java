package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.RequestDetailsModel;
import com.google.gson.annotations.SerializedName;

public class RequestDetailsResponse {
    @SerializedName("data")
    private RequestDetailsModel data;

    public RequestDetailsModel getData() {
        return data;
    }

    public void setData(RequestDetailsModel data) {
        this.data = data;
    }
}
