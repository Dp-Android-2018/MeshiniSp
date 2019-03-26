package com.dp.meshinisp.service.model.response;

import com.google.gson.annotations.SerializedName;

public class ActivationResponse extends DefaultResponse{

    @SerializedName("token")
    private String activationToken;

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }
}
