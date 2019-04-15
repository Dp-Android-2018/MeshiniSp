package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class StartDestinationRequest {
    @SerializedName("destination_id")
    private int destinationId;

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}
