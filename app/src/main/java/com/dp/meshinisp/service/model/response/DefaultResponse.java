package com.dp.meshinisp.service.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DefaultResponse {

    @SerializedName("errors")
    private  ArrayList<String>  errors;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    public void setErrors( ArrayList<String>  errors) {
        this.errors = errors;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
