package com.dp.meshinisp.service.model.response;

import com.google.gson.annotations.SerializedName;

public class OfferResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
