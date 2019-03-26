package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.ClientData;
import com.google.gson.annotations.SerializedName;

public class LoginRegisterResponse extends DefaultResponse {

    @SerializedName("data")
    private ClientData clientData;

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }
}
