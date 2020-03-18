package com.fieldforce.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SopModel {

    public static final int SOP_TYPE = 1;

    @SerializedName("sub_sop_id")
    public String sopId;

    @SerializedName("sub_sop")
    public String sop;

    @SerializedName("is_complete")
    public String isComplete;

    @SerializedName("sub_sop_flag")
    public String subFlag;

    public SopModel() {}


    public String getSopId() {
        return sopId;
    }

    public String getSop() {
        return sop;
    }

    public Integer getSopType(){
        return SOP_TYPE;
    }
}
