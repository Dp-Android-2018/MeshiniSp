package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class FinancialRequest {
    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
