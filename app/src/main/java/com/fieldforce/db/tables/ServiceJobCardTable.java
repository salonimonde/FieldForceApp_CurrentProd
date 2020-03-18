package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


public class ServiceJobCardTable {
    public static final String TABLE_NAME = "ServiceJobCardTable";
    public static final String PATH = "SERVICE_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 37;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */
    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String SERVICE_ID = "service_id";
        public static final String SERVICE_NO = "service_no";
        public static final String SERVICE_TYPE = "service_type";
        public static final String CONSUMER_NAME = "consumer_name";
        public static final String CONSUMER_NO = "consumer_no";
        public static final String ADDRESS = "address";
        public static final String MOBILE_NO = "mobile_no";
        public static final String REQUEST_ID = "request_id";
        public static final String ASSIGNED_DATE = "assigned_date";
        public static final String ACCEPT_STATUS = "accept_status";
        public static final String CARD_STATUS = "service_card_status";
        public static final String DUE_DATE = "due_date";

        public static final String SCREEN = "screen";
        public static final String COMPLETED_ON  = "completed_on";

    }
}