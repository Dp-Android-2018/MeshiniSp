package com.dp.meshinisp.service.model.global;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActiveTripResponseModel implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("client")
    private ActiveTripClientModel activeTripClientModel;

    @SerializedName("vehicle_type")
    private String vehicleType;

    @SerializedName("pickup_address")
    private String pickupAddress;

    @SerializedName("pickup_lat")
    private float pickupLat;

    @SerializedName("pickup_long")
    private float pickupLong;

    @SerializedName("places")
    private ArrayList<StartTripResponseModel> places;

    protected ActiveTripResponseModel(Parcel in) {
        id = in.readInt();
        vehicleType = in.readString();
        pickupAddress = in.readString();
        pickupLat = in.readFloat();
        pickupLong = in.readFloat();
        places = in.createTypedArrayList(StartTripResponseModel.CREATOR);
    }

    public static final Creator<ActiveTripResponseModel> CREATOR = new Creator<ActiveTripResponseModel>() {
        @Override
        public ActiveTripResponseModel createFromParcel(Parcel in) {
            return new ActiveTripResponseModel(in);
        }

        @Override
        public ActiveTripResponseModel[] newArray(int size) {
            return new ActiveTripResponseModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ActiveTripClientModel getActiveTripClientModel() {
        return activeTripClientModel;
    }

    public void setActiveTripClientModel(ActiveTripClientModel activeTripClientModel) {
        this.activeTripClientModel = activeTripClientModel;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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

    public ArrayList<StartTripResponseModel> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<StartTripResponseModel> places) {
        this.places = places;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(vehicleType);
        dest.writeString(pickupAddress);
        dest.writeFloat(pickupLat);
        dest.writeFloat(pickupLong);
        dest.writeTypedList(places);
    }
}
