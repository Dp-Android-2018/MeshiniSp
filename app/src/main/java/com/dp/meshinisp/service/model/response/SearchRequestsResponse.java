package com.dp.meshinisp.service.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dp.meshinisp.service.model.global.LinksModel;
import com.dp.meshinisp.service.model.global.MetaDataModel;
import com.dp.meshinisp.service.model.global.RequestsResponseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchRequestsResponse implements Parcelable {

    @SerializedName("data")
    private ArrayList<RequestsResponseModel>  data;

    @SerializedName("links")
    private LinksModel pageLinks;

    @SerializedName("meta")
    private MetaDataModel metaData;

    protected SearchRequestsResponse(Parcel in) {
        data = in.createTypedArrayList(RequestsResponseModel.CREATOR);
    }

    public static final Creator<SearchRequestsResponse> CREATOR = new Creator<SearchRequestsResponse>() {
        @Override
        public SearchRequestsResponse createFromParcel(Parcel in) {
            return new SearchRequestsResponse(in);
        }

        @Override
        public SearchRequestsResponse[] newArray(int size) {
            return new SearchRequestsResponse[size];
        }
    };

    public ArrayList<RequestsResponseModel> getData() {
        return data;
    }

    public void setData(ArrayList<RequestsResponseModel>  data) {
        this.data = data;
    }

    public LinksModel getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(LinksModel pageLinks) {
        this.pageLinks = pageLinks;
    }

    public MetaDataModel getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataModel metaData) {
        this.metaData = metaData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }
}
