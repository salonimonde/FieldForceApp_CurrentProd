package com.fieldforce.db.tables;
import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the CommissionJobCardTable of device database
 *
 * @author Bynry01
 */
public class ComplaintJobCardTable
{
    public static final String TABLE_NAME = "ComplaintJobCardTable";
    public static final String PATH = "COMPLAINT_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 27;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();
    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */
    public static class Cols
    {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String COMPLAINT_ID = "complaint_id";
        public static final String COMPLAINT_REQUEST_ID = "complaint_request_id";
        public static final String CONSUMER_NAME = "consumer_name";
        public static final String CONSUMER_NO = "consumer_no";
        public static final String MOBILE_NO = "mobile_no";
        public static final String COMPLAINT_TYPE = "complaint_type";
        public static final String ADDRESS = "address";
        public static final String CARD_STATUS = "card_status";
        public static final String ASSIGNED_DATE = "assigned_date";
        public static final String ACCEPT_STATUS = "accept_status";
        public static final String DUE_DATE = "due_date";

        public static final String SCREEN = "screen";
        public static final String COMPLETED_ON  = "completed_on";

    }
}