package com.fieldforce.models;

import com.google.gson.annotations.SerializedName;

public class MjcModel {

    @SerializedName("city")
    public String mjcCity;

    @SerializedName("nsc_no")
    public String nscNo;

    @SerializedName("flat")
    public String flat;

    @SerializedName("building")
    public String building;

    @SerializedName("location")
    public String location;

    @SerializedName("area")
    public String area;

    @SerializedName("pin_code")
    public String pinCode;

    @SerializedName("mobile")
    public String mobile;

    @SerializedName("phone")
    public String phone;

    @SerializedName("email")
    public String email;

    @SerializedName("installation_date")
    public String installationDate;

    @SerializedName("meter_no")
    public String meterNo;

    @SerializedName("conversion_date")
    public String conversionDate;

    @SerializedName("test_date")
    public String testDate;

    @SerializedName("test_time")
    public String testTime;

    @SerializedName("pressure_guage")
    public String pressureGuage;

    @SerializedName("initial_reading")
    public String initialReading;

    @SerializedName("tpi_sign")
    public String imgTpiSign;

    public MjcModel(){

    }
}
