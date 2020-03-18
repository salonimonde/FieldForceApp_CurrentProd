package com.fieldforce.webservices;

@SuppressWarnings("ALL")
public class ApiConstants {

    /*public static final String DOMAIN_URL = "http://192.168.1.42:8008"; // Arpita Local
    public static final String DOMAIN_URL_CIS = "http://192.168.1.42:8008";*/

    /*public static final String DOMAIN_URL = "http://192.168.1.51:7000"; //
    public static final String DOMAIN_URL_CIS = "http://192.168.1.51:7000";*/

    /*public static final String DOMAIN_URL = "http://192.168.43.214:8000"; //
    public static final String DOMAIN_URL_CIS = "http://192.168.43.214:8000";*/

    /*public static final String DOMAIN_URL = "http://139.59.82.191:9000"; // Test Environment
    public static final String DOMAIN_URL_CIS = "http://139.59.82.191:9000"; //*/

    public static final String DOMAIN_URL = "http://54.187.162.209"; //  PROD
    public static final String DOMAIN_URL_CIS = "http://68.183.84.255"; //  PROD

    public static final String BASE_URL = DOMAIN_URL + "/api/";
    public static final String BASE_URL_CIS = DOMAIN_URL_CIS + "/api/";

    public static final int LOG_STATUS = 0; // TODO Please Make Sure that While creating signed APK make this value to "1"

    public static final String LOGIN_URL = BASE_URL_CIS + "user_login";
    public static final String FCM_DEVICE_TOKEN_URL = BASE_URL + "fcm_token/";

    // Api for Enquiry
    public static final String GET_ENQUIRY_JOB_CARD = BASE_URL_CIS + "get_enquiry_jobcard";
    // this api gets called in the end for DMA_VENDOR
    public static final String GET_ENQUIRY_DE_ASSIGN_JOB_CARD_URL = BASE_URL_CIS + "reassigned_deassigned_enquiry_jobcard";
    public static final String GET_CONSUMER_CATEGORY_URL = BASE_URL_CIS + "get_categoary/";
    public static final String GET_CONSUMER_SUB_CATEGORY_URL = BASE_URL_CIS + "get_sub_categoary/";
    public static final String GET_STATE = BASE_URL_CIS +  "get_state/";
    public static final String GET_AREA_SP = BASE_URL_CIS +  "get_area/";
    public static final String GET_CITY = BASE_URL_CIS + "get_city/";
    public static final String GET_PIN_CODE = BASE_URL_CIS + "get_pincode/";
    public static final String GET_DOCUMENT_LIST = BASE_URL_CIS + "get_document_list/";
    public static final String UPLOAD_NSC_FORM = BASE_URL_CIS + "new-connection/";
    public static final String GET_PAYMENT_SCHEMES = BASE_URL_CIS + "get_consumer_scheme";
    public static final String CHECK_AADHAR_NO = BASE_URL_CIS + "check_aadhar_no";
    public static final String GET_WARD = BASE_URL_CIS + "get_ward/";
    public static final String GET_LOCATION_REGISTRATION = BASE_URL_CIS + "get_location/";
    public static final String GET_LANDMARK = BASE_URL_CIS + "get_landmark/";


    //Api for Site Verification
    public static final String GET_SITE_VERIFICATION_JOB_CARDS = BASE_URL_CIS + "get_site_verification_card/";
    public static final String GET_SITE_VERIFICATION_DE_ASSIGN_CARDS = BASE_URL_CIS + "reassigned_deassigned_site_verification_card/";
    public static final String SENT_SITE_VERIFICATION_ACCEPTANCE = BASE_URL_CIS + "accept_site_verification_card/";
    public static final String GET_SITE_VERIFICATION_DETAILS = BASE_URL_CIS + "detail_site_verification_card/";
    public static final String UPLOAD_SITE_VERIFICATION_DETAILS = BASE_URL_CIS + "upload_site_verification_card/";
    public static final String UPLOAD_IMAGE_SITE_VERIFICATION = BASE_URL_CIS + "image_upload_site_verification_card/";

    //Api for Installation
    public static final String GET_INSTALLATION_JOB_CARDS = BASE_URL_CIS + "get_meter_install_card/";
    public static final String GET_INSTALLATION_DE_ASSIGN_CARDS = BASE_URL_CIS + "reassigned_deassigned_meter_install_card/";
    public static final String SENT_INSTALLATION_ACCEPTANCE = BASE_URL_CIS + "accept_meter_install_card/";
    public static final String GET_INSTALLATION_DETAILS = BASE_URL_CIS + "detail_meter_install_card/";
    public static final String UPLOAD_INSTALLATION_DETAILS = BASE_URL_CIS + "upload_meter_install_card/";
    public static final String UPLOAD_IMAGE_INSTALLATION = BASE_URL_CIS + "image_upload_meter_install_card/";

    //Api for Conversion
    public static final String GET_CONVERSION_JOB_CARDS = BASE_URL_CIS + "get_conversion_jobcard";
    public static final String GET_CONVERSION_DE_ASSIGN_JOB_CARDS = BASE_URL_CIS + "reassigned_deassigned_conversion_jobcard";
    public static final String SENT_CONVERSION_ACCEPTANCE = BASE_URL_CIS + "accept_conversion_jobcard";
    public static final String GET_CONVERSION_DETAILS = BASE_URL_CIS + "get_detail_conversion_card";
    public static final String UPLOAD_CONVERSION_DETAILS = BASE_URL_CIS + "upload_conversion_card/";
    public static final String UPLOAD_CONVERSION_IMAGE = BASE_URL_CIS + "image_upload_conversion_card/";
    public static final String GET_METER_MAKE = BASE_URL_CIS + "get_meter_make/";

    //Api for Services
    public static final String GET_SERVICE_JOB_CARDS = BASE_URL_CIS + "get_service_card/";
    public static final String GET_DE_ASSIGN_SERVICE_JOB_CARDS = BASE_URL_CIS + "reassigned_deassigned_service_card/";
    public static final String SENT_SERVICE_ACCEPTANCE = BASE_URL_CIS + "accept_service_card/";
    public static final String GET_SERVICE_DETAILS = BASE_URL_CIS + "detail_service_card/";
    public static final String UPLOAD_SERVICE_DETAILS = BASE_URL_CIS + "upload_service_card/";
    public static final String UPLOAD_IMAGE_SERVICES = BASE_URL_CIS + "image_upload_service_card/";

    //Api for Complaints
    public static final String GET_COMPLAINT_JOB_CARDS = BASE_URL_CIS + "get_complaint_card/";
    // This api gets called in the end for PROJECT_VENDOR
    public static final String GET_DE_ASSIGN_COMPLAINT_JOB_CARDS = BASE_URL_CIS + "reassigned_deassigned_complaint_card/";
    public static final String SENT_COMPLAINT_ACCEPTANCE = BASE_URL_CIS + "accept_complaint_card/";
    public static final String UPLOAD_COMPLAINT_DETAILS = BASE_URL_CIS + "upload_complaint_card/";


    public static final String GET_COMM_CARDS_URL = BASE_URL + "get_commissioning_card/";
    public static final String GET_DECOM_CARDS_URL = BASE_URL + "get_decommissioning_card/";
    public static final String COMMISSION_ASSET_DETAILS_URL = BASE_URL + "commission_detail/";
    public static final String DECOMMISSION_ASSET_DETAILS_URL = BASE_URL + "decommission_detail/";
    public static final String UPLOAD_COMMISSION_ASSET_DETAILS_URL = BASE_URL + "upload_commissioning_asset_check/";
    public static final String UPLOAD_DECOMMISSION_ASSET_DETAILS_URL = BASE_URL + "upload_decommissioning_asset_check/";
    public static final String STD_PARAMETERS_COMMISSION_URL = BASE_URL + "standard_commission_detail/";
    public static final String STD_PARAMETERS_DECOMMISSION_URL = BASE_URL + "standard_decommission_detail/";
    public static final String GET_MATERIAL_LIST_COMMISSION_URL = BASE_URL + "get_commissioning_material_list/";
    public static final String STD_PREVENTIVE_PARAMETERS_URL = BASE_URL + "preventive_maintenance_detail_standard/";
    public static final String STD_BREAKDOWN_PARAMETERS_URL = BASE_URL + "breakdown_maintenance_detail_standard/";
    public static final String REASSIGN_DE_ASSIGN_COMMISSION_CARD_URL = BASE_URL + "reassigned_deassigned_commissioning_card/";
    public static final String REASSIGN_DE_ASSIGN_PREVENTIVE_CARD_URL = BASE_URL + "reassigned_deassigned_preventive_maintenance_card/";
    public static final String REASSIGN_DE_ASSIGN_BREAKDOWN_CARD_URL = BASE_URL + "reassigned_deassigned_breakdown_maintenance_card/";
    public static final String GET_PREVENTIVE_JOB_CARDS_URL = BASE_URL + "get_asset_preventive_maintenance_card/";

    public static final String GET_BREAKDOWN_JOB_CARDS_URL = BASE_URL + "get_asset_breakdown_maintenance_card/";
    public static final String GET_PREVENTIVE_DETAILS_URL = BASE_URL + "preventive_maintenance_detail/";
    public static final String GET_BREAKDOWN_DETAILS_URL = BASE_URL + "breakdown_maintenance_detail/";
    public static final String GET_BREAKDOWN_ACCEPT_URL = BASE_URL + "breakdown_maintenance_card_accept/";
    public static final String GET_PREVENTIVE_ACCEPT_URL = BASE_URL + "preventive_maintenance_card_accept/";
    public static final String GET_COMMISSION_ACCEPT_URL = BASE_URL + "commissioning_card_accept/";
    public static final String GET_DECOMMISSION_ACCEPT_URL = BASE_URL + "decommissioning_card_accept/";
    public static final String REASSIGN_DE_ASSIGN_DECOMMISSION_CARD_URL = BASE_URL + "reassigned_deassigned_decommissioning_card/";
    public static final String UPLOAD_PREVENTIVE_DETAILS_URL = BASE_URL + "upload_preventive_maintenance_asset_check/";
    public static final String UPLOAD_BREAKDOWN_DETAILS_URL = BASE_URL + "upload_breakdown_mainetenance_asset_check/";
    public static final String UPLOAD_COMMISSION_ASSET_IMAGE_URL = BASE_URL + "save-asset-commissioning-image/";
    public static final String UPLOAD_DECOMMISSION_ASSET_IMAGE_URL = BASE_URL + "save-asset-decommissioning-image/";
    public static final String UPLOAD_PREVENTIVE_ASSET_IMAGE_URL = BASE_URL + "save-asset-preventive-maintenance-image/";
    public static final String UPLOAD_BREAKDOWN__ASSET_IMAGE_URL = BASE_URL + "save-asset-breakdown-maintenance-image/";
    public static final String GET_ASSIGNED_ASSET_CARD_URL = BASE_URL + "get-asset-card/";
    public static final String GET_DE_ASSIGNED_ASSET_CARD_URL = BASE_URL + "reassigned-deassigned-asset-card/";

    public static final String GET_ASSET_DETAILS_URL = BASE_URL + "get-asset-details/";
    public static final String GET_SUB_DIVISION_URL = BASE_URL + "get-sub-division/";
    public static final String GET_AREA_URL = BASE_URL + "get-area/";
    public static final String GET_LOCATION_URL = BASE_URL + "get-location/";
    public static final String UPLOAD_ASSET_DETAILS_URL = BASE_URL + "upload-asset-details/";
    public static final String UPLOAD_ASSET_IMAGE_URL = BASE_URL + "save-asset-image/";

    public static final String GET_CYCLE_URL = BASE_URL_CIS + "get_billcycle/";
    public static final String GET_SUBDIVISION_URL = BASE_URL_CIS + "get_subdivision/";
    public static final String GET_BINDER_URL = BASE_URL_CIS + "get_route/";

    public static final String GET_RECOVERY_JOB_CARD_URL = BASE_URL_CIS + "";
    public static final String GET_DE_ASSIGN_RECOVERY_JOB_CARD_URL = BASE_URL_CIS + "";



    public static final String GET_BANK_NAME_URL = BASE_URL_CIS + "get-bank-name/";

    public static final String SIGNATURE_IMAGE_UPLOAD = BASE_URL_CIS + "signature_image_upload_meter_conversion/";

    public static final String UPLOAD_MJC_CHECK_LIST = BASE_URL_CIS + "mjc_check_list/";


    //For Volley
    public static final String LOGOUT = "2";
    public static final String FCM_DEVICE_TOKEN = "3";
    public static final String GET_COMM_CARDS = "4";
    public static final String DECOMM_TODAY_CARDS = "5";
    public static final String COMMISSION_ASSET_DETAILS = "6";
    public static final String STD_COMMISSION_PARAMETERS = "7";
    public static final String UPLOAD_COMMISSION_ASSET_DETAILS = "8";
    public static final String DECOMMISSION_ASSET_DETAILS = "9";
    public static final String UPLOAD_DECOMMISSION_ASSET_DETAILS = "10";
    public static final String STD_DECOMMISSION_PARAMETERS = "11";
    public static final String STD_MONITORING_PARAMETERS = "13";
    public static final String REASSIGN_DEASSIGN_COMMISSION_CARD = "14";
    public static final String GET_PREVENTIVE_JOB_CARDS = "15";
    public static final String GET_BREAKDOWN_JOB_CARDS = "16";
    public static final String GET_PREVENTIVE_DETAILS = "17";
    public static final String GET_BREAKDOWN_DETAILS = "18";
    public static final String UPLOAD_PREVENTIVE_DETAILS = "19";
    public static final String UPLOAD_BREAKDOWN_DETAILS = "20";
    public static final String REASSIGN_DEASSIGN_DECOMMISSION_CARD = "22";
    public static final String REASSIGN_DEASSIGN_PREVENTIVE_CARD = "23";
    public static final String REASSIGN_DEASSIGN_BREAKDOWN_CARD = "24";
    public static final String STD_PREVENTIVE_PARAMETERS = "26";
    public static final String STD_BREAKDOWN_PARAMETERS = "27";
    public static final String GET_BREAKDOWN_ACCEPT = "28";
    public static final String GET_PREVENTIVE_ACCEPT = "29";
    public static final String GET_COMMISSION_ACCEPT = "30";
    public static final String GET_DECOMMISSION_ACCEPT = "31";
    public static final String GET_COMMISSION_MATERIALLIST = "32";
    public static final String GET_COMPLAINT_ACCEPT = "42";
    public static final String GET_ASSIGNED_ASSET_CARD = "50";
    public static final String GET_DE_ASSIGNED_ASSET_CARD = "51";
    public static final String GET_ASSET_DETAILS = "52";
    public static final String GET_SUB_DIVISION = "53";
    public static final String GET_AREA = "54";
    public static final String GET_LOCATION = "55";
    public static final String UPLOAD_ASSET_DETAILS = "56";
    public static final String UPLOAD_ASSET_IMAGE = "57";
    public static final String GET_CYCLE = "58";
    public static final String GET_SUBDIVISION = "59";
    public static final String GET_BINDER = "60";

    public static final String GET_RECOVERY_JOB_CARD = "63";
    public static final String GET_DE_ASSIGN_RECOVERY_JOB_CARD = "64";


    public static final int UPLOAD_COUNT = 1;


    // Api for rejected registrations
    public static final String GET_REJECTED_REGISTRATIONS  = BASE_URL_CIS + "get_rejected_registration";

    public static final String UPLOAD_REJECTED_FORM = BASE_URL_CIS + "rejected-connection/";

}
