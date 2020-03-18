package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class LocationTable {
    public static final String TABLE_NAME = "LocationTable";
    public static final String PATH = "LOCATION_TABLE";
    public static final int PATH_TOKEN = 17;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of All Job Card Table
     *
     * @author Bynry01
     */

    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String LOCATION_ID = "location_id";
        public static final String LOCATION_NAME = "location_name";
        public static final String AREA_ID = "area_id";
    }

}
