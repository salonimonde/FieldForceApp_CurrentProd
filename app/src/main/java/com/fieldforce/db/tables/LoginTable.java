package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the LoginTable Table of device database
 *
 * @author Bynry01
 */
public class LoginTable
{

    public static final String TABLE_NAME = "LoginTable";
    public static final String PATH = "LOGIN_TABLE";
    public static final int PATH_TOKEN = 11;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();
    /**
     * This class contains Constants to describe name of Columns of LoginTable
     * @author Bynry01
     */
    public static class Cols
    {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String USER_ID = "user_id";
        public static final String USER_CITY = "user_city";
        public static final String USER_CONTACT_NO = "user_contact_no";
        public static final String USER_NAME = "user_name";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_ADDRESS = "user_address";
        public static final String EMP_TYPE = "emp_type";
        public static final String LOGIN_DATE = "login_date";
        public static final String LOGIN_LAT= "login_lat";
        public static final String LOGIN_LNG= "login_lng";

        public static final String USER_STATE = "user_state";
        public static final String STATE_ID = "state_id";
        public static final String CITY_ID = "city_id";
        public static final String USER_TYPE = "user_type";
        public static final String USER_DISTRICT = "user_district";
        public static final String DISTRICT_ID = "district_id";
    }
}