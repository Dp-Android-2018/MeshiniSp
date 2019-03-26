package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.LoginResponseModel;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends DefaultResponse{
    @SerializedName("data")
    private LoginResponseModel data;

    public LoginResponseModel getData() {
        return data;
    }

    public void setData(LoginResponseModel data) {
        this.data = data;
    }
}
