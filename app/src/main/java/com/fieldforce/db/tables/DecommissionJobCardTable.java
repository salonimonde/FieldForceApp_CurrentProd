package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the CommissionJobCardTable of device database
 *
 * @author Bynry01
 */
public class DecommissionJobCardTable {

    public static final String TABLE_NAME = "DecommissionJobCardTable";
    public static final String PATH = "DISC_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 10;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */
    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String DECOMMISSION_ID = "dsc_commission_id";
        public static final String JOB_ID = "dsc_asset_id";
        public static final String JOB_NAME = "dsc_asset_name";
        public static final String JOB_AREA = "dsc_asset_area";
        public static final String JOB_LOCATION = "dsc_asset_location";
        public static final String JOB_CATEGORY = "dsc_asset_category";
        public static final String JOB_SUBCATEGORY = "dsc_asset_subcategory";
        public static final String CARD_STATUS = "dsc_card_status";
        public static final String MAKE_NUMBER = "make_no";
        public static final String ASSIGNED_DATE = "assigned_date";
        public static final String ACCEPT_STATUS = "accept_status";
    }
}