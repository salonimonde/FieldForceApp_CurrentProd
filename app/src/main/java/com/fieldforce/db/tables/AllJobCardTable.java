package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class AllJobCardTable {
    public static final String TABLE_NAME = "AllJobCardTable";
    public static final String PATH = "ALL_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 22;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of All Job Card Table
     *
     * @author Bynry01
     */

    public static class Cols {

        /* Enquiry */
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String CONSUMER_NAME = "consumer_name";
        public static final String MOBILE_NO = "mobile_no";
        public static final String STATUS = "status";
        public static final String ENQUIRY_ID = "enquiry_id";
        public static final String ASSIGNED_DATE = "assigned_date";
        public static final String DUE_DATE = "due_date";
        public static final String SCREEN = "screen";
        public static final String JOB_AREA = "job_area";
        public static final String ENQUIRY_NO = "enquiry_no";
        public static final String STATE_ID = "state_id";
        public static final String CITY_ID = "city_id";
        public static final String STATE = "state";
        public static final String CITY = "city";
        public static final String NSC_ID = "nsc_id";
        public static final String COMPLETED_ON = "completed_on";

        public static final String REGISTRATION_ID  = "registration_id";


        /* Site Verification */
        public static final String ADDRESS = "address";
        public static final String CATEGORY= "category";
        public static final String SUB_CATEGORY = "sub_category";
        public static final String ACCEPT_STATUS = "accept_status";
        public static final String TYPE_OF_PREMISE = "premise_type";
        public static final String SERVICE_REQUESTED = "service_requested";
        public static final String REJECTION_REMARK = "rejection_remark";
        public static final String LAT = "lat";
        public static final String LONG = "long";
        public static final String REQUEST_ID = "request_id";
        public static final String SITE_VERIFICATION_ID = "site_verification_id";

        /* Meter Installation */
        public static final String METER_ID = "meter_id";
        public static final String METER_INSTALL_ID = "meter_install_id";
        public static final String AREA = "area";
        public static final String CONSUMER_NO = "consumer_no";
        public static final String METER_CATEGORY = "meter_category";
        public static final String METER_SUBCATEGORY = "meter_sub_category";

        /* Conversion */
        public static final String CONVERSION_ID = "conversion_id";
        public static final String JOB_ID = "job_id";
        public static final String REGULATOR_NO = "regulator_no";
        public static final String METER_NO = "meter_no";
        public static final String INSTALLED_ON = "installed_on";
        public static final String RFC_VERIFIED_ON = "rfc_verified_on";

        /* Services */

        public static final String SERVICE_ID = "service_id";
        public static final String SERVICE_NO= "service_no";
        public static final String SERVICE_TYPE = "service_type";

        /* Complaints */

        public static final String COMPLAINT_ID = "complaint_id";
        public static final String COMPLAINT_REQUEST_ID = "complaint_request_id";
        public static final String COMPLAINT_TYPE = "complaint_type";





    }
}