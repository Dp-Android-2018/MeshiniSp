package com.dp.meshinisp.service.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    @SerializedName("errors")
    private  ArrayList<String>  errors;


    public void setErrors( ArrayList<String>  errors) {
        this.errors = errors;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
