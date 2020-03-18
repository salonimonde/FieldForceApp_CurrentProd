package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;
/**
 * Created by Saloni on 23-09-2019.
 */

public class RegistrationTable {
    public static final String TABLE_NAME = "RegistrationTable";
    public static final String PATH = "REGISTRATION_TABLE";
    public static final int PATH_TOKEN = 42;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String ENQUIRY_ID = "enquiry_id";
        public static final String CONSUMER_CATEGORY = "consumer_category";
        public static final String CONSUMER_SUB_CATEGORY = "consumer_subcategory";
        public static final String STATE = "state";
        public static final String NAME = "name";
        public static final String ADHAAR = "addhaar";
        public static final String EMAIL_ID = "email_id";
        public static final String FLAT_NO = "flat_no";
        public static final String BUILDING_NAME = "building_name";
        public static final String LOCATION = "location";
        public static final String AREA = "area";
        public static final String CITY = "city";
        public static final String PINCODE = "pincode";
        public static final String MOBILE = "mobile";
        public static final String PREMISE = "premise";
        public static final String ENQUIRY_NO  = "enquiry_no";
        public static final String CONSUMER_NO  = "consumer_no";
        public static final String SAVE_TYPE  = "save_type";
        public static final String SCHEME_NAME  = "scheme_name";
        public static final String SCHEME_AMOUNT = "scheme_amount";
        public static final String PAYMENT_METHOD = "payment_method";
        public static final String CHEQUE_NO = "cheque_no";
        public static final String CHEQUE_BRANCH = "cheque_branch";
        public static final String CHEQUE_DATE = "cheque_date";
        public static final String CHEQUE_BANK = "cheque_bank";
        public static final String CHEQUE_MICR = "cheque_micr";
        public static final String DD_NO = "dd_no";
        public static final String BANK_NAME = "bank_name";
        public static final String DD_DATE = "dd_date";
        public static final String DD_MICR = "dd_micr";
        public static final String DD_BRANCH = "dd_branch";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String CONNECTIVITY = "connectivity";
        public static final String IS_NSC_NEW = "is_nsc_new";
        public static final String FILE_0 = "File0";
        public static final String FILE_1 = "File1";
        public static final String FILE_ADD_PROOF_0 = "File_add_proof0";
        public static final String FILE_ADD_PROOF_1 = "File_add_proof1";
        public static final String FILE_NOC_PROOF = "File_noc_proof";
        public static final String IMAGE_COUNT = "image_count";
        public static final String IMAGE_COUNT_ADD = "image_count_add";
        public static final String VENDOR_ID = "vendor_id";
        public static final String FIELD_FORCE_ID = "field_force_id";
        public static final String DOCUMENTS = "documents";
        public static final String DOCUMENTS_ADD = "documents_add";
        public static final String AREA_NAME = "area_name";
        public static final String FLOOR_NO = "floor_no";
        public static final String PLOT_NO = "plot_no";
        public static final String WING = "wing";
        public static final String ROAD_NO = "road_no";
        public static final String LANDMARK = "landmark";
        public static final String DISTRICT = "district";
        public static final String SOCIETY_NAME = "society_name";
        public static final String NSC_ID = "nsc_id";
        public static final String REGISTRATION_NO = "registration_no";
        public static final String BANK = "bank";
        public static final String BRANCH_NAME = "branch_name";
        public static final String MICR_CODE = "micr_code";
        public static final String CHEQUE_DD_NUMBER = "cheque_dd_number";
        public static final String IS_REJECTED = "is_document_rejected";
        public static final String DATE_OF_PAYMENT = "date_of_payment";
        public static final String CARD_STATUS = "status";
        public static final String FIELD_CHEQUE_DD = "File_cheque_dd";
        public static final String FILE_SIGN = "File_sign";
        public static final String FILE_CONSUMER_PHOTO = "File_consumerphoto";
    }
}
