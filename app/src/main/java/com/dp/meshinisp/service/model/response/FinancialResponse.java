package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.FinancialResponseData;
import com.google.gson.annotations.SerializedName;

public class FinancialResponse {
    @SerializedName("data")
    private FinancialResponseData data;

    public FinancialResponseData getData() {
        return data;
    }

    public void setData(FinancialResponseData data) {
        this.data = data;
    }
}
