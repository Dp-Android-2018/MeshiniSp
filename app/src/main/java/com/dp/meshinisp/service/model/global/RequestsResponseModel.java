package com.dp.meshinisp.service.model.global;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RequestsResponseModel implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("has_offer")
    private boolean hasOffer;

    @SerializedName("client")
    private ClientModel client;

    protected RequestsResponseModel(Parcel in) {
        id = in.readInt();
        hasOffer = in.readByte() != 0;
    }

    public static final Creator<RequestsResponseModel> CREATOR = new Creator<RequestsResponseModel>() {
        @Override
        public RequestsResponseModel createFromParcel(Parcel in) {
            return new RequestsResponseModel(in);
        }

        @Override
        public RequestsResponseModel[] newArray(int size) {
            return new RequestsResponseModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(boolean hasOffer) {
        this.hasOffer = hasOffer;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (hasOffer ? 1 : 0));
    }
}
