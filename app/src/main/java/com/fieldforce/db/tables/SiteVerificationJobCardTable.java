package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


public class SiteVerificationJobCardTable {
    public static final String TABLE_NAME = "SiteVerificationJobCardTable";
    public static final String PATH = "SITE_VERIFICATION_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 36;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */
    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String SITE_VERIFICATION_ID = "site_verification_id";
        public static final String CONSUMER_NAME = "consumer_name";
        public static final String MOBILE_NO = "mobile_no";
        public static final String ADDRESS = "address";
        public static final String CATEGORY = "category";
        public static final String SUB_CATEGORY = "sub_category";
        public static final String CARD_STATUS = "site_verification_card_status";
        public static final String ASSIGNED_DATE = "assigned_date";
        public static final String ACCEPT_STATUS = "accept_status";
        public static final String TYPE_OF_PERIMSE = "type_of_perimse";
        public static final String SERVICE_REQUESTED = "service_requested";
        public static final String REQUEST_ID = "request_id";
        public static final String DUE_DATE = "due_date";


        public static final String REJECTION_REMARK = "rejection_remark";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "long";

        public static final String SCREEN = "screen";
        public static final String COMPLETED_ON  = "completed_on";
        public static final String AREA  = "area";

    }
}