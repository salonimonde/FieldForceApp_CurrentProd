package com.fieldforce.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Consumer implements Serializable {

    public Consumer() {
    }



    @SerializedName("consumerAddress")
    public String address;

    @SerializedName("consumer_no")
    public String consumer_no;

    @SerializedName("acctype")
    public String acctype;

    @SerializedName("consumer_name")
    public String consumer_name;

    @SerializedName("city")
    public String city;

    @SerializedName("city_id")
    public String city_id;

    @SerializedName("state")
    public String state;

    @SerializedName("state_id")
    public String state_id;

    @SerializedName("remark")
    public String remark;

    @SerializedName("image")
    public String image;

    @SerializedName("contact_no")
    public String contact_no;

    @SerializedName("is_primary")
    public String is_primary;

    @SerializedName("otp")
    public String otp;

    @SerializedName("connection_type")
    public String connection_type;

    @SerializedName("serviceType")
    public String service_type;

    @SerializedName("service_id")
    public String service_id;

    @SerializedName("serviceNumber")
    public String service_no;

    @SerializedName("service_request_id")
    public String service_request_id;

    @SerializedName("service_status")
    public String service_status;

    @SerializedName("request_date")
    public String request_date;

    @SerializedName("emailid")
    public String emailid;

    @SerializedName("alternet_email_id")
    public String alternet_email_id;

    @SerializedName("alternet_contact_no")
    public String alternet_contact_no;

    @SerializedName("profile_img")
    public String profile_img;

    @SerializedName("register_mobile_no")
    public String register_mobile_no;

    @SerializedName("complaint_type_id")
    public String complaint_id;

    @SerializedName("complaintType")
    public String complaint_type;

    @SerializedName("complaintNumber")
    public String complaint_no;

    @SerializedName("raised_date")
    public String raised_date;

    @SerializedName("resolved_date")
    public String resolved_date;

    @SerializedName("complaint_status")
    public String complaint_status;

    @SerializedName("consumer_categoary_id")
    public String consumerCategoryId;

    @SerializedName("consumer_category")
    public String consumerCategory;

    @SerializedName("consumer_subcategoary_id")
    public String consumer_subcategory_id;

    @SerializedName("consumer_subcategoary")
    public String consumer_subcategory;

    @SerializedName("pincode")
    public String pincode;

    @SerializedName("pincode_id")
    public String pincode_id;

    @SerializedName("document")
    public String document;

    @SerializedName("checked")
    public String checked;

    @SerializedName("document_id")
    public String document_id;

    @SerializedName("document_type")
    public String document_type;

    @SerializedName("transaction_mode")
    public String transaction_mode;

    @SerializedName("transaction_id")
    public String transaction_id;

    @SerializedName("tariff")
    public String tariff;

    @SerializedName("tariff_code")
    public String tariffcode;

    @SerializedName("meterreading_id")
    public String meterreading_id;

    @SerializedName("current_amt")
    public String current_amt;

    @SerializedName("prompt_amt")
    public String prompt_amt;

    @SerializedName("after_due_amt")
    public String after_due_amt;

    @SerializedName("prompt_date")
    public String prompt_date;

    @SerializedName("due_date")
    public String due_date;

    @SerializedName("arrer")
    public String arrier;

    @SerializedName("month")
    public String month;

    @SerializedName("consumption")
    public String consumption_unit;

    @SerializedName("bill_amount")
    public String bill_amount;

    @SerializedName("bill_url")
    public String bill_url;

    @SerializedName("amount")
    public String amount;

    @SerializedName("bill_month")
    public String bill_month;

    @SerializedName("status")
    public String status;

    @SerializedName("category")
    public String category;
    @SerializedName("category_id")
    public String categoryId;

    @SerializedName("name")
    public String name;

    @SerializedName("complaint_subtype")
    public String complaint_subtype;

    @SerializedName("complaint_subtype_id")
    public String complaint_subtype_id;

    @SerializedName("nsc_date")
    public String nsc_date;

    @SerializedName("nsc_id")
    public String nsc_id;

    @SerializedName("nsc_no")
    public String nsc_no;

    @SerializedName("sub_category")
    public String sub_category;

    @SerializedName("paid_date")
    public String paidDate;

    @SerializedName("total_amount")
    public String totalAmount;

    @SerializedName("mail_flag")
    public String isMailSent;

    @SerializedName("mobile")
    public String mobile;

    @SerializedName("other")
    public String other;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("image_id")
    public String imageId;

    @SerializedName("supply_type")
    public String supplyType;

    @SerializedName("occupation")
    public String occupation;

    @SerializedName("flat_no")
    public String flat_no;

    @SerializedName("aadhar_no")
    public String aadhar_no;

    @SerializedName("service_requested")
    public String serviceRequested;

    @SerializedName("premise")
    public String premises;

    @SerializedName("scheme_id")
    public String schemeId;

    @SerializedName("scheme_name")
    public String schemeName;

    @SerializedName("scheme_amount")
    public String schemeAmount;

    @SerializedName("meter_make_id")
    public String meterMakeId;

    @SerializedName("meter_make_name")
    public String meterMakeName;

    @SerializedName("bank_name")
    public String bank_name;

    @SerializedName("area_id")
    public String areaID;

    @SerializedName("area")
    public String area;

    @SerializedName("ward_id")
    public String wardID;

    @SerializedName("ward")
    public String ward;

    @SerializedName("location_id")
    public String locationID;

    @SerializedName("location")
    public String location;

    @SerializedName("landmark_id")
    public String landmarkID;

    @SerializedName("landmark")
    public String landmark;

    public String getLandmarkID() {
        return landmarkID;
    }

    public void setLandmarkID(String landmarkID) {
        this.landmarkID = landmarkID;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public ArrayList<Consumer> document_list;
    public ArrayList<Consumer> document_address_list;

    public  ArrayList<String> banklist;

    public String getAddress() {
        return address;
    }

    public String getConsumer_no() {
        return consumer_no;
    }

    public String getAcctype() {
        return acctype;
    }

    public String getConsumer_name() {
        return consumer_name;
    }

    public String getCity() {
        return city;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getState() {
        return state;
    }

    public String getState_id() {
        return state_id;
    }

    public String getRemark() {
        return remark;
    }

    public String getImage() {
        return image;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getIs_primary() {
        return is_primary;
    }

    public String getOtp() {
        return otp;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public String getService_type() {
        return service_type;
    }

    public String getService_id() {
        return service_id;
    }

    public String getService_no() {
        return service_no;
    }

    public String getService_request_id() {
        return service_request_id;
    }

    public String getService_status() {
        return service_status;
    }

    public String getRequest_date() {
        return request_date;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getAlternet_email_id() {
        return alternet_email_id;
    }

    public String getAlternet_contact_no() {
        return alternet_contact_no;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public String getRegister_mobile_no() {
        return register_mobile_no;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public String getComplaint_type() {
        return complaint_type;
    }

    public String getComplaint_no() {
        return complaint_no;
    }

    public String getRaised_date() {
        return raised_date;
    }

    public String getResolved_date() {
        return resolved_date;
    }

    public String getComplaint_status() {
        return complaint_status;
    }

    public String getConsumerCategoryId() {
        return consumerCategoryId;
    }

    public String getConsumerCategory() {
        return consumerCategory;
    }

    public String getConsumer_subcategory_id() {
        return consumer_subcategory_id;
    }

    public String getConsumer_subcategory() {
        return consumer_subcategory;
    }

    public String getPincode() {
        return pincode;
    }

    public String getPincode_id() {
        return pincode_id;
    }

    public String getArea() {
        return area;
    }

    public String getAreaID() {
        return areaID;
    }

    public String getDocument() {
        return document;
    }

    public String getChecked() {
        return checked;
    }

    public String getDocument_id() {
        return document_id;
    }

    public String getTransaction_mode() {
        return transaction_mode;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getTariff() {
        return tariff;
    }

    public String getTariffcode() {
        return tariffcode;
    }

    public String getMeterreading_id() {
        return meterreading_id;
    }

    public String getCurrent_amt() {
        return current_amt;
    }

    public String getPrompt_amt() {
        return prompt_amt;
    }

    public String getAfter_due_amt() {
        return after_due_amt;
    }

    public String getPrompt_date() {
        return prompt_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getArrier() {
        return arrier;
    }

    public String getMonth() {
        return month;
    }

    public String getConsumption_unit() {
        return consumption_unit;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public String getBill_url() {
        return bill_url;
    }

    public String getAmount() {
        return amount;
    }

    public String getBill_month() {
        return bill_month;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getComplaint_subtype() {
        return complaint_subtype;
    }

    public String getComplaint_subtype_id() {
        return complaint_subtype_id;
    }

    public String getNsc_date() {
        return nsc_date;
    }

    public String getNsc_id() {
        return nsc_id;
    }

    public String getNsc_no() {
        return nsc_no;
    }

    public String getSub_category() {
        return sub_category;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getIsMailSent() {
        return isMailSent;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOther() {
        return other;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public String getServiceRequested() {
        return serviceRequested;
    }

    public String getPremises() {
        return premises;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public String getSchemeName() {
        return schemeName;
    }
    public String getBankName(){
        return bank_name;
    }

    public ArrayList<Consumer> getDocument_list() {
        return document_list;
    }

    public ArrayList<Consumer> getDocument_address_list() {
        return document_address_list;
    }

    public String getSchemeAmount() {
        return schemeAmount;
    }

    public String getMeterMakeId() {
        return meterMakeId;
    }

    public String getMeterMakeName() {
        return meterMakeName;
    }
}
