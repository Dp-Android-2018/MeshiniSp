package com.dp.meshinisp.service.model.global;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestDetailsModel implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("country")
    private String country;

    @SerializedName("pickup_address")
    private String pickupAddress;

    @SerializedName("pickup_lat")
    private float pickupLat;

    @SerializedName("pickup_long")
    private float pickupLong;

    @SerializedName("vehicle_type_string")
    private String vehicleType;

    @SerializedName("places")
    private ArrayList<String> places;

    @SerializedName("client")
    private RequestClientModel client;

    @SerializedName("date")
    private String pickupDate;

    @SerializedName("pickup_time")
    private String pickupTime;

    protected RequestDetailsModel(Parcel in) {
        id = in.readInt();
        country = in.readString();
        pickupAddress = in.readString();
        pickupLat = in.readFloat();
        pickupLong = in.readFloat();
        vehicleType = in.readString();
        places = in.createStringArrayList();
        pickupDate = in.readString();
        pickupTime = in.readString();
    }

    public static final Creator<RequestDetailsModel> CREATOR = new Creator<RequestDetailsModel>() {
        @Override
        public RequestDetailsModel createFromParcel(Parcel in) {
            return new RequestDetailsModel(in);
        }

        @Override
        public RequestDetailsModel[] newArray(int size) {
            return new RequestDetailsModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public float getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(float pickupLat) {
        this.pickupLat = pickupLat;
    }

    public float getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(float pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public ArrayList<String> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    public RequestClientModel getClient() {
        return client;
    }

    public void setClient(RequestClientModel client) {
        this.client = client;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(country);
        dest.writeString(pickupAddress);
        dest.writeFloat(pickupLat);
        dest.writeFloat(pickupLong);
        dest.writeString(vehicleType);
        dest.writeStringList(places);
        dest.writeString(pickupDate);
        dest.writeString(pickupTime);
    }
}
