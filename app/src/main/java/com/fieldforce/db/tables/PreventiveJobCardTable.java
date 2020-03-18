package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

/**
 * Created by Prachi on 09-02-2018.
 * Bynry
 */
public class PreventiveJobCardTable
{
    public static final String TABLE_NAME = "PreventiveJobCardTable";
    public static final String PATH = "PREVENTIVE_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 34;
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
        public static final String PREVENTIVE_ID = "preventive_id";
        public static final String JOB_ID = "preventive_job_id";
        public static final String JOB_NAME = "preventive_job_name";
        public static final String JOB_AREA = "preventive_job_area";
        public static final String JOB_LOCATION = "preventive_job_location";
        public static final String JOB_CATEGORY = "preventive_job_category";
        public static final String JOB_SUBCATEGORY = "preventive_job_subcategory";
        public static final String CARD_STATUS = "preventive_card_status";
        public static final String MAKE_NUMBER = "make_no";
        public static final String DATE = "preventive_card_date";
        public static final String ACCEPT_STATUS = "accept_status";

    }
}