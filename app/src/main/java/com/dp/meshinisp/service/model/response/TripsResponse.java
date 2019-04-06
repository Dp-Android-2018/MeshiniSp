package com.dp.meshinisp.service.model.response;

import com.dp.meshinisp.service.model.global.LinksModel;
import com.dp.meshinisp.service.model.global.MetaDataModel;
import com.dp.meshinisp.service.model.global.TripsResponseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TripsResponse {
    @SerializedName("data")
    private ArrayList<TripsResponseModel> data;

    @SerializedName("links")
    private LinksModel pageLinks;

    @SerializedName("meta")
    private MetaDataModel metaData;

    public ArrayList<TripsResponseModel> getData() {
        return data;
    }

    public void setData(ArrayList<TripsResponseModel> data) {
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
}