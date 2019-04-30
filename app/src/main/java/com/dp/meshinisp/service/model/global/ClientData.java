package com.dp.meshinisp.service.model.global;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientData {

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("city")
    private CityModel city;

    @SerializedName("languages")
    private List<CityModel> languages;

    @SerializedName("phone")
    private String phone;

    @SerializedName("activated")
    private boolean activated;

    @SerializedName("status")
    private boolean status;

    @SerializedName("profile_picture")
    private String profilePictureUrl;

    @SerializedName("vehicle_type")
    private String vehicleType;

    @SerializedName("vehicle_type_string")
    private String vehicleTypeString;

    @SerializedName("vehicle_license")
    private String vehicleLicenseUrl;

    @SerializedName("driving_license")
    private String drivingLicenseUrl;

    @SerializedName("device_token")
    private String deviceToken;


    @SerializedName("api_token")
    private String apiToken;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public List<CityModel> getLanguages() {
        return languages;
    }

    public void setLanguages(List<CityModel> languages) {
        this.languages = languages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleTypeString() {
        return vehicleTypeString;
    }

    public void setVehicleTypeString(String vehicleTypeString) {
        this.vehicleTypeString = vehicleTypeString;
    }

    public String getVehicleLicenseUrl() {
        return vehicleLicenseUrl;
    }

    public void setVehicleLicenseUrl(String vehicleLicenseUrl) {
        this.vehicleLicenseUrl = vehicleLicenseUrl;
    }

    public String getDrivingLicenseUrl() {
        return drivingLicenseUrl;
    }

    public void setDrivingLicenseUrl(String drivingLicenseUrl) {
        this.drivingLicenseUrl = drivingLicenseUrl;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
