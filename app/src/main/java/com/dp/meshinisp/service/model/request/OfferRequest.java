package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class OfferRequest {
    @SerializedName("price")
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
