package com.fieldforce.webservices;

import com.fieldforce.models.Consumer;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.models.UserProfileModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonResponse {
//    Message and Success

    public String SUCCESS = "success";
    public String message;
    public String result;
    public String status;
    public String nsc_id;
    public static String FAILURE = "failure";


//    ArrayList's Models

    public UserProfileModel responsedata;
    public ArrayList<TodayModel> meter_install_cards;
    public ArrayList<TodayModel> site_verification_cards;
    public ArrayList<TodayModel> commissioning_cards;
    public ArrayList<TodayModel> decommissioning_cards;
    public ArrayList<TodayModel> commissioning_detail;
    public ArrayList<TodayModel> decommissioning_detail;
    public ArrayList<TodayModel> meter_install_detail;
    public ArrayList<TodayModel> conversion_detail;

    public ArrayList<TodayModel> std_list;
    public ArrayList<String> deassign_request_list;
    public ArrayList<TodayModel> preventive_maintenance_cards;
    public ArrayList<TodayModel> breakdown_maintenance_cards;
    public ArrayList<TodayModel> preventive_maintenance_detail_cards;
    public ArrayList<TodayModel> breakdown_maintenance_detail_cards;
    public ArrayList<TodayModel> site_verification_detail;
    public ArrayList<TodayModel> material_lists;

    public ArrayList<TodayModel> enquiry_cards;
    public ArrayList<TodayModel> conversion_cards;
    public ArrayList<TodayModel> service_detail;

    //for Complaint
    public ArrayList<TodayModel> complaint_cards;


//    Asset Indexing Related Models

    public ArrayList<TodayModel> asset_cards;
    public ArrayList<TodayModel> service_jobs;
    public ArrayList<String> deassign_complaint_list;
    public ArrayList<String> deassign_service_list;
    public ArrayList<Consumer> state_list;
    public ArrayList<Consumer> city_list;
    public ArrayList<Consumer> pincode_list;
    public ArrayList<Consumer> document_list;
    public ArrayList<Consumer> scheme_list;
    public ArrayList<Consumer> document_address_list;
    @SerializedName("meter_make_list")
    public ArrayList<Consumer> meter_make_list;
    public ArrayList<Consumer> categoary_list;
    public ArrayList<Consumer> consumer_subcategoary_List;

    public ArrayList<Consumer> banklist;
    public ArrayList<Consumer> area_list;
    public ArrayList<Consumer> location_list;
    public ArrayList<Consumer> ward_list;
    public ArrayList<Consumer> landmark_list;



    public ArrayList<RegistrationModel> data;

}
