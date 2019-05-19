package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class NotificationRequest {
    @SerializedName("request_id")
    private int requestId;

    @SerializedName("message")
    private String message;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
