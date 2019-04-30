package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class ChangeLanguageRequest {
    @SerializedName("language")
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
