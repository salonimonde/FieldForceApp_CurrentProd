package com.fieldforce.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckListModel {

    public static final int CONFIG_TYPE = 0;

    @SerializedName("main_configuration_id")
    public String configurationId;

    @SerializedName("main_configuration")
    public String configuration;

    @SerializedName("main_sop_flag")
    public String mainFlag;

    @SerializedName("sub_configuration")
    public ArrayList<SopModel> subConfiguration;

    @SerializedName("remark")
    public String remark;

    @SerializedName("remark_id")
    public String remarkId;

    public Integer getConfigType(){
        return CONFIG_TYPE;
    }
    public CheckListModel(){

    }
}
