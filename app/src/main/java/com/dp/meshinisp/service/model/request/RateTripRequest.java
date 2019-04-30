package com.dp.meshinisp.service.model.request;

import com.google.gson.annotations.SerializedName;

public class RateTripRequest {
    @SerializedName("rating")
    private float ratingValue;

    @SerializedName("review")
    private String reviewText;

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
