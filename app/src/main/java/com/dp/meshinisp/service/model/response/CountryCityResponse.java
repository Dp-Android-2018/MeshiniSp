package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryCityResponse extends DefaultResponse{

    @SerializedName("data")
    private List<CountryCityResponseModel> countryCityList;

    public List<CountryCityResponseModel> getCountryCityList() {
        return countryCityList;
    }

    public void setCountryCityList(List<CountryCityResponseModel> countryCityList) {
        this.countryCityList = countryCityList;
    }
}
