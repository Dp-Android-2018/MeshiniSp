package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

public class RevenueDataModel {
    @SerializedName("trips_count")
    private int tripsCount;

    @SerializedName("raw_total")
    private String totalRevenue;

    @SerializedName("fees")
    private String fees;

    @SerializedName("net_profit")
    private String totalProfit;

    public int getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(int tripsCount) {
        this.tripsCount = tripsCount;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }
}
