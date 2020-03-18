package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class AddProofTable {
    public static final String TABLE_NAME = "AddProofTable";
    public static final String PATH = "ADDPROOF_TABLE";
    public static final int PATH_TOKEN = 6;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of All Job Card Table
     *
     * @author Bynry01
     */

    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String ADDPROOF_ID = "addproof_id";
        public static final String DOCUMENT_NAME = "document_name";
        public static final String DOCUMENT_TYPE = "document_type";
    }

}
