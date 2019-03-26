package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class ActivationRequest {
    @SerializedName("login")
    private String mail;

    @SerializedName("code")
    private String activationCode;

    @SerializedName("password")
    private String password;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
