package com.dp.meshinisp.service.model.global;

import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.google.gson.annotations.SerializedName;

public class FinancialResponseData {
    @SerializedName("name")
    private String name;

    @SerializedName("completed_trips")
    private int completedTripsNum;

    @SerializedName("rating")
    private float ratingNum;

    @SerializedName("revenue_data")
    private RevenueDataModel revenueData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompletedTripsNum() {
        return completedTripsNum;
    }

    public void setCompletedTripsNum(int completedTripsNum) {
        this.completedTripsNum = completedTripsNum;
    }

    public String getRatingNum() {
        return ConfigurationFile.Constants.BRACKET_BEFORE + ratingNum + ConfigurationFile.Constants.BRACKET_AFTER;
    }

    public void setRatingNum(float ratingNum) {
        this.ratingNum = ratingNum;
    }

    public RevenueDataModel getRevenueData() {
        return revenueData;
    }

    public void setRevenueData(RevenueDataModel revenueData) {
        this.revenueData = revenueData;
    }
}
