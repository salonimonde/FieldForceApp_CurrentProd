package com.fieldforce.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfileModel implements Serializable {

    @SerializedName("city")
    public String city;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("email_id")
    public String email_id;

    @SerializedName("profile_image")
    public String profile_image;

    @SerializedName("emp_type")
    public String emp_type;

    @SerializedName("contact_no")
    public String contact_no;

    @SerializedName("address")
    public String address;

    @SerializedName("employee_id")
    public String emp_id;

    @SerializedName("user_name")
    public String user_name;

    @SerializedName("token")
    public String token;

    @SerializedName("name")
    public String name;

    @SerializedName("state")
    public String state;

    @SerializedName("state_id")
    public String stateId;

    @SerializedName("city_id")
    public String cityId;

    @SerializedName("vendor_id")
    public String vendorId;

    @SerializedName("field_force_id")
    public String fieldForceId;

    @SerializedName("user_type")
    public String userType;


    @SerializedName("district")
    public String district;

    @SerializedName("district_id")
    public String districtId;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String screen;

    public String getScreen() { return screen;}

    public String getCity() {
        return city;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getAddress() {
        return address;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getCityId() {
        return cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getFieldForceId() {
        return fieldForceId;
    }

    public String getUserType() {
        return userType;
    }






}


