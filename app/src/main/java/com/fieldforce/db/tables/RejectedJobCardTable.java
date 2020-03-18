package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class RejectedJobCardTable {
    public static final String TABLE_NAME = "RejectedJobCardTable";
    public static final String PATH = "REJECTED_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 43;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */

    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String NAME = "name";
        public static final String AREA_NAME = "area_name";
        public static final String CARD_STATUS = "status";
        public static final String REGISTRATION_NO = "registration_no";
        public static final String COMPLETED_ON = "completed_on";

    }
}