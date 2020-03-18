package com.fieldforce.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TodayModel implements Serializable, Comparable<TodayModel>{

    public TodayModel() {
    }

    @SerializedName("registration_id")
    public String registrationId;

    @SerializedName("id")
    public String id;

    @SerializedName("job_name")
    public String job_name;

    @SerializedName("category")
    public String category;

    @SerializedName("subcategory")
    public String subcategory;

    @SerializedName("area")
    public String area;

    @SerializedName("location")
    public String location;

    @SerializedName("job_id")
    public String job_id;

    @SerializedName("commission_id")
    public String commission_id;

    @SerializedName("decommission_id")
    public String decommission_id;

    @SerializedName("parameter_name")
    public String parameter_name;

    @SerializedName("parameter_value")
    public String parameter_value;

    @SerializedName("parameter_unit")
    public String parameter_unit;

    @SerializedName("check_list")
    public ArrayList<String> check_list;

    @SerializedName("deassign_request_list")
    public ArrayList<String> deassign_request_list;

    @SerializedName("readingparameter_list")
    public ArrayList<String> readingparameter_list;

    @SerializedName("assetmake_no")
    public String assetmakeNo;

    @SerializedName("consumer_name")
    public String consumerName;

    @SerializedName("consumer_no")
    public String consumerNo;

    @SerializedName("meter_id")
    public String meterId;

    @SerializedName("meter_installation_id")
    public String meterInstallationId;

    @SerializedName("supply_type")
    public String supplyType;

    @SerializedName("preventive_id")
    public String preventive_id;

    @SerializedName("breakdown_id")
    public String breakdown_id;

    @SerializedName("assigned_date")
    public String assignedDate;

    @SerializedName("site_verification_id")
    public String site_verification_id;

    @SerializedName("pick_up_address")
    public String pick_up_address;

    @SerializedName("material_count")
    public String material_count;

    @SerializedName("store_contact_no")
    public String store_contact_no;

    @SerializedName("request_id")
    public String requestId;

    @SerializedName("material_name")
    public String material_name;

    @SerializedName("job_type")
    public String job_type;

    @SerializedName("material_list")
    public ArrayList<String> material_list;

    @SerializedName("requested_load")
    public String requested_load;

    @SerializedName("type_of_premises")
    public String type_of_premises;

    @SerializedName("service_requested")
    public String service_requested;

    @SerializedName("max_demand")
    public String max_demand;

    @SerializedName("branch_id")
    public String branch_id;

    @SerializedName("branch")
    public String branch;

    @SerializedName("route_id")
    public String route_id;

    @SerializedName("route")
    public String route;

    @SerializedName("service_id")
    public String service_id;

    @SerializedName("service_no")
    public String serviceNumber;

    @SerializedName("service_type")
    public String serviceType;

    @SerializedName("billcycle")
    public String billcycle;

    @SerializedName("jc_id")
    public String assetCardId;

    @SerializedName("asset")
    public String assetName;

    @SerializedName("make")
    public String assetMake;

    @SerializedName("make_no")
    public String assetMakeNo;

    @SerializedName("complaint_id")
    public String complaintId;

    @SerializedName("complaint_no")
    public String complaintNumber;

    @SerializedName("complaint_type")
    public String complaintType;

    @SerializedName("address")
    public String address;

    @SerializedName("mobile_no")
    public String mobileNumber;

    @SerializedName("enquiry_no")
    public String enquiryNumber;

    @SerializedName("conversion_id")
    public String conversionId;

    @SerializedName("due_date")
    public String dueDate;

    @SerializedName("meter_no")
    public String meterNo;

    @SerializedName("meter_digits")
    public String meterDigits;

    @SerializedName("meter_make")
    public String meterMake;

    @SerializedName("meter_type")
    public String meterType;

    @SerializedName("regulator_no")
    public String regulatorNo;

    @SerializedName("pipe_length")
    public String pipeLength;

    @SerializedName("free_pipe_length")
    public String freeLength;

    @SerializedName("extra_charges")
    public String extraCharges;

    @SerializedName("tentative_conversion_date")
    public String conversionDate;

    @SerializedName("rfc_verified")
    public String rfcVerified;

    @SerializedName("po_number")
    public String poNumber;

    @SerializedName("remark")
    public String remark;

    @SerializedName("rfc_verified_on")
    public String rfcVerifiedOn;

    @SerializedName("installed_on")
    public String installedOn;

    @SerializedName("mobile_rejection_remark")
    public String siteRejectRemark;

    @SerializedName("installation_rejection_remark")
    public String installRejectRemark;

    @SerializedName("conversion_rejection_remark")
    public String convertRejectRemark;

    @SerializedName("reportDataCount")
    public String reportDataCount;

    @SerializedName("registration_no")
    public String registrationNo;

    @SerializedName("flat_no")
    public String flatNo;

    @SerializedName("building_name")
    public String buildingName;

    @SerializedName("city")
    public String city;

    @SerializedName("phone")
    public String phone;

    @SerializedName("installation_date")
    public String installationDate;

    @SerializedName("test_date")
    public String testDate;

    @SerializedName("pressure_gauge")
    public String pressureGuage;

    @SerializedName("test_duration")
    public String testDuration;

    @SerializedName("email")
    public String email;

    @SerializedName("pincode")
    public String pinCode;

    @SerializedName("tpi_sign")
    public String imgTpi;

    @SerializedName("nitrogen_purging")
    public String nitogenPurging;


    @SerializedName("lat")
    public String latitude;

    @SerializedName("long")
    public String longitude;

    public String screen;

    public String completedOn;


    public Date newDueDate;

    @SerializedName("state_id")
    public String stateId;

    @SerializedName("city_id")
    public String cityId;

    @SerializedName("state")
    public String state;

    @SerializedName("nsc_id")
    public String nscId;

    @SerializedName("area_name")
    public String areaName;

    @SerializedName("floor_no")
    public String floorNo;

    @SerializedName("plot_no")
    public String plotNo;

    @SerializedName("wing")
    public String wing;

    @SerializedName("road_no")
    public String roadNo;

    @SerializedName("landmark")
    public String landmark;

    @SerializedName("district")
    public String district;




























    public String cardStatus;
    public String submittedDate;
    public String acceptstatus;
    public String materialReceived;

    @SerializedName("check_SOP_list")
    public ArrayList<CheckListModel> checkList;

    @SerializedName("remark_dict")
    public ArrayList<CheckListModel> remarkList;


    public String getJob_name() {
        return job_name;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getArea() {
        return area;
    }

    public String getLocation() {
        return location;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getCommission_id() {
        return commission_id;
    }

    public String getDecommission_id() {
        return decommission_id;
    }

    public String getParameter_name() {
        return parameter_name;
    }

    public String getParameter_value() {
        return parameter_value;
    }

    public String getParameter_unit() {
        return parameter_unit;
    }

    public ArrayList<String> getCheck_list() {
        return check_list;
    }

    public ArrayList<String> getDeassign_request_list() {
        return deassign_request_list;
    }

    public ArrayList<String> getReadingparameter_list() {
        return readingparameter_list;
    }

    public String getAssetmakeNo() {
        return assetmakeNo;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public String getMeterId() {
        return meterId;
    }

    public String getMeterInstallationId() {
        return meterInstallationId;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public String getPreventive_id() {
        return preventive_id;
    }

    public String getBreakdown_id() {
        return breakdown_id;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public String getSite_verification_id() {
        return site_verification_id;
    }

    public String getPick_up_address() {
        return pick_up_address;
    }

    public String getMaterial_count() {
        return material_count;
    }

    public String getStore_contact_no() {
        return store_contact_no;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public String getJob_type() {
        return job_type;
    }

    public ArrayList<String> getMaterial_list() {
        return material_list;
    }

    public String getRequested_load() {
        return requested_load;
    }

    public String getType_of_premises() {
        return type_of_premises;
    }

    public String getService_requested() {
        return service_requested;
    }

    public String getMax_demand() {
        return max_demand;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public String getBranch() {
        return branch;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getRoute() {
        return route;
    }

    public String getService_id() {
        return service_id;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getBillcycle() {
        return billcycle;
    }

    public String getAssetCardId() {
        return assetCardId;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getAssetMake() {
        return assetMake;
    }

    public String getAssetMakeNo() {
        return assetMakeNo;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public String getAcceptstatus() {
        return acceptstatus;
    }

    public String getMaterialReceived() {
        return materialReceived;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public String getAddress() {
        return address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEnquiryNumber() {
        return enquiryNumber;
    }

    public String getConversionId() {
        return conversionId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public String getMeterDigits() {
        return meterDigits;
    }

    public String getMeterMake() {
        return meterMake;
    }

    public String getMeterType() {
        return meterType;
    }

    public String getRegulatorNo() {
        return regulatorNo;
    }

    public String getPipeLength() {
        return pipeLength;
    }

    public String getFreeLength() {
        return freeLength;
    }

    public String getCharges() {
        return extraCharges;
    }

    public String getConversionDate() {
        return conversionDate;
    }

    public String getRfcVerified() {
        return rfcVerified;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public String getRemark() {
        return remark;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getScreen() {
        return screen;
    }

    public Date getNewDueDate(){return newDueDate; }

    public String getStateId(){return stateId; }

    public String getcityId(){return cityId; }

    public String getState(){return state; }

    public String getFloorNo(){return floorNo; }

    public String getPlotNo(){return plotNo ; }

    public String getWing(){return wing ; }

    public String getRoadNo(){return roadNo ; }

    public String getLandmark(){return landmark ; }

    public String getDistrict(){return district ; }





    @Override
    public int compareTo(@NonNull TodayModel o) {
        return o.newDueDate.compareTo(this.newDueDate);
    }
}