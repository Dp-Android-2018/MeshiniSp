package com.dp.meshinisp.service.model.global;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StartTripResponseModel implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("lat")
    private float locationLat;

    @SerializedName("long")
    private float locationLong;

    protected StartTripResponseModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        locationLat = in.readFloat();
        locationLong = in.readFloat();
    }

    public static final Creator<StartTripResponseModel> CREATOR = new Creator<StartTripResponseModel>() {
        @Override
        public StartTripResponseModel createFromParcel(Parcel in) {
            return new StartTripResponseModel(in);
        }

        @Override
        public StartTripResponseModel[] newArray(int size) {
            return new StartTripResponseModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(float locationLat) {
        this.locationLat = locationLat;
    }

    public float getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(float locationLong) {
        this.locationLong = locationLong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeFloat(locationLat);
        dest.writeFloat(locationLong);
    }
}
