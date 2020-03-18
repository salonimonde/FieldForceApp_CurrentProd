package com.fieldforce.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fieldforce.db.tables.AddProofTable;
import com.fieldforce.db.tables.AllJobCardTable;
import com.fieldforce.db.tables.AreaTable;
import com.fieldforce.db.tables.AssetJobCardTable;
import com.fieldforce.db.tables.BankTable;
import com.fieldforce.db.tables.BreakDownJobCardTable;
import com.fieldforce.db.tables.CategoryTable;
import com.fieldforce.db.tables.CommissionJobCardTable;
import com.fieldforce.db.tables.ComplaintJobCardTable;
import com.fieldforce.db.tables.ConsumerEnquiryTable;
import com.fieldforce.db.tables.ConversionJobCardTable;
import com.fieldforce.db.tables.DecommissionJobCardTable;
import com.fieldforce.db.tables.IdProofTable;
import com.fieldforce.db.tables.LandmarkTable;
import com.fieldforce.db.tables.LocationTable;
import com.fieldforce.db.tables.LoginTable;
import com.fieldforce.db.tables.MeterInstalltionJobCardTable;
import com.fieldforce.db.tables.NotificationTable;
import com.fieldforce.db.tables.PaymentTable;
import com.fieldforce.db.tables.Pincode;
import com.fieldforce.db.tables.PreventiveJobCardTable;
import com.fieldforce.db.tables.RegistrationTable;
import com.fieldforce.db.tables.RejectedJobCardTable;
import com.fieldforce.db.tables.ServiceJobCardTable;
import com.fieldforce.db.tables.SiteVerificationJobCardTable;
import com.fieldforce.db.tables.SubCategoryTable;
import com.fieldforce.db.tables.WardTable;

import java.text.MessageFormat;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String KEY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS {0} ({1})";
    public static final String KEY_DROP_TABLE = "DROP TABLE IF EXISTS {0}";
    public final static String SQL = "SELECT COUNT(*) FROM sqlite_master WHERE name=?";
    public static final String TAG = "DatabaseHelper";
    private static final int CURRENT_DB_VERSION = 6;
    private static final String DB_NAME = "FieldForceDB.db";
    private static final String DROP_RECORD_TRIGGER = "drop_records";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLoginTable(db);
        createNotificationTable(db);
        createCommissionJobCardTable(db);
        createDecommissionJobCardTable(db);
        createPreventiveJobCardTable(db);
        createBreakdownJobCardTable(db);
        createSiteVerificationJobCardTable(db);
        createMeterInstallCardTable(db);
        createNSCJobCardTable(db);
        createServiceJobCardTable(db);
        createComplaintJobCardTable(db);
        createConsumerEnquiryTable(db);
        createMeterConversionAssignmentTable(db);
        createRegistrationTable(db);
        createRejectedJobCardTable(db);
        createAllJobCardTable(db);
        createAreaTable(db);
        createBankTable(db);
        createSchemeTable(db);
        createDocumentTable(db);
        createAddDocumentTable(db);
        createWardTable(db);
        createCategoryTable(db);
        createSubCategoryTable(db);
        createPincodeTable(db);
        createLocationTable(db);
        createLandmarkTable(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//      dropTable(sqLiteDatabase, LoginTable.TABLE_NAME);
    }

    private void createLoginTable(SQLiteDatabase db) {
        String loginTableFields = LoginTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LoginTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                LoginTable.Cols.USER_ID + " VARCHAR, " +
                LoginTable.Cols.USER_NAME + " VARCHAR, " +
                LoginTable.Cols.USER_CITY + " VARCHAR, " +
                LoginTable.Cols.USER_CONTACT_NO + " VARCHAR, " +
                LoginTable.Cols.USER_EMAIL + " VARCHAR, " +
                LoginTable.Cols.EMP_TYPE + " VARCHAR, " +
                LoginTable.Cols.USER_ADDRESS + " VARCHAR, " +
                LoginTable.Cols.LOGIN_DATE + " VARCHAR, " +
                LoginTable.Cols.USER_STATE + " VARCHAR, " +
                LoginTable.Cols.STATE_ID + " VARCHAR, " +
                LoginTable.Cols.CITY_ID + " VARCHAR, " +
                LoginTable.Cols.USER_TYPE + " VARCHAR, " +
                LoginTable.Cols.LOGIN_LAT + " VARCHAR, " +
                LoginTable.Cols.USER_DISTRICT + " VARCHAR, " +
                LoginTable.Cols.DISTRICT_ID + " VARCHAR, " +
                LoginTable.Cols.LOGIN_LNG + " VARCHAR";


        createTable(db, LoginTable.TABLE_NAME, loginTableFields);
    }

    private void createNotificationTable(SQLiteDatabase db) {
        String notificationTableFields = NotificationTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NotificationTable.Cols.TITLE + " VARCHAR, " +
                NotificationTable.Cols.MESSAGE + " VARCHAR, " +
                NotificationTable.Cols.DATE + " VARCHAR, " +
                NotificationTable.Cols.IS_READ + " VARCHAR, " +
                NotificationTable.Cols.USER_LOGIN_ID + " VARCHAR ";
        createTable(db, NotificationTable.TABLE_NAME, notificationTableFields);
    }

    private void createCommissionJobCardTable(SQLiteDatabase db) {
        String CommissionjobCardTableFields = CommissionJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommissionJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_ID + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_NAME + " VARCHAR, " +
                CommissionJobCardTable.Cols.COMMISSION_ID + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_CATEGORY + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_SUBCATEGORY + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_AREA + " VARCHAR, " +
                CommissionJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_LOCATION + " VARCHAR, " +
                CommissionJobCardTable.Cols.MAKE_NUMBER + " VARCHAR, " +
                CommissionJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                CommissionJobCardTable.Cols.JOB_TYPE + " VARCHAR, " +
                CommissionJobCardTable.Cols.MATERIAL_RECEIVED + " VARCHAR, " +
                CommissionJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR";
        createTable(db, CommissionJobCardTable.TABLE_NAME, CommissionjobCardTableFields);
    }

    private void createDecommissionJobCardTable(SQLiteDatabase db) {
        String DISCjobCardTableFields = DecommissionJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DecommissionJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                DecommissionJobCardTable.Cols.JOB_ID + " VARCHAR, " +
                DecommissionJobCardTable.Cols.JOB_NAME + " VARCHAR, " +
                DecommissionJobCardTable.Cols.DECOMMISSION_ID + " VARCHAR, " +
                DecommissionJobCardTable.Cols.JOB_CATEGORY + " VARCHAR, " +
                DecommissionJobCardTable.Cols.JOB_SUBCATEGORY + " VARCHAR, " +
                DecommissionJobCardTable.Cols.JOB_AREA + " VARCHAR, " +
                DecommissionJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                DecommissionJobCardTable.Cols.JOB_LOCATION + " VARCHAR, " +
                DecommissionJobCardTable.Cols.MAKE_NUMBER + " VARCHAR, " +
                DecommissionJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                DecommissionJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR";

        createTable(db, DecommissionJobCardTable.TABLE_NAME, DISCjobCardTableFields);
    }

    private void createPreventiveJobCardTable(SQLiteDatabase db) {
        String PrevJobCardTableFields = PreventiveJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PreventiveJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                PreventiveJobCardTable.Cols.JOB_ID + " VARCHAR, " +
                PreventiveJobCardTable.Cols.JOB_NAME + " VARCHAR, " +
                PreventiveJobCardTable.Cols.PREVENTIVE_ID + " VARCHAR, " +
                PreventiveJobCardTable.Cols.JOB_CATEGORY + " VARCHAR, " +
                PreventiveJobCardTable.Cols.JOB_SUBCATEGORY + " VARCHAR, " +
                PreventiveJobCardTable.Cols.JOB_AREA + " VARCHAR, " +
                PreventiveJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                PreventiveJobCardTable.Cols.DATE + " VARCHAR, " +
                PreventiveJobCardTable.Cols.MAKE_NUMBER + " VARCHAR, " +
                PreventiveJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                PreventiveJobCardTable.Cols.JOB_LOCATION + " VARCHAR";
        createTable(db, PreventiveJobCardTable.TABLE_NAME, PrevJobCardTableFields);
    }

    private void createBreakdownJobCardTable(SQLiteDatabase db) {
        String BreakdownJobCardTableFields = BreakDownJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BreakDownJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                BreakDownJobCardTable.Cols.JOB_ID + " VARCHAR, " +
                BreakDownJobCardTable.Cols.JOB_NAME + " VARCHAR, " +
                BreakDownJobCardTable.Cols.BREAKDOWN_ID + " VARCHAR, " +
                BreakDownJobCardTable.Cols.JOB_CATEGORY + " VARCHAR, " +
                BreakDownJobCardTable.Cols.JOB_SUBCATEGORY + " VARCHAR, " +
                BreakDownJobCardTable.Cols.JOB_AREA + " VARCHAR, " +
                BreakDownJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                BreakDownJobCardTable.Cols.DATE + " VARCHAR, " +
                BreakDownJobCardTable.Cols.MAKE_NUMBER + " VARCHAR, " +
                BreakDownJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                BreakDownJobCardTable.Cols.JOB_LOCATION + " VARCHAR";
        createTable(db, BreakDownJobCardTable.TABLE_NAME, BreakdownJobCardTableFields);
    }

    private void createMeterInstallCardTable(SQLiteDatabase db) {
        String MeterInstallCardTable = MeterInstalltionJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.METER_ID + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.CONSUMER_NO + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.MOBILE_NO + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.METER_CATEGORY + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.METER_SUBCATEGORY + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.AREA + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.REQUEST_ID + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.REJECTION_REMARK + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.SCREEN + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.AREA_NAME + " VARCHAR, " +
                MeterInstalltionJobCardTable.Cols.DUE_DATE + " VARCHAR";
        createTable(db, MeterInstalltionJobCardTable.TABLE_NAME, MeterInstallCardTable);
    }


    private void createSiteVerificationJobCardTable(SQLiteDatabase db) {
        String SiteVerificationFields = SiteVerificationJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.ADDRESS + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.MOBILE_NO + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.CATEGORY + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.TYPE_OF_PERIMSE + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.SERVICE_REQUESTED + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.REQUEST_ID + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.DUE_DATE + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.REJECTION_REMARK + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.LATITUDE + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.LONGITUDE + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.SCREEN + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.AREA + " VARCHAR, " +
                SiteVerificationJobCardTable.Cols.SUB_CATEGORY + " VARCHAR";
        createTable(db, SiteVerificationJobCardTable.TABLE_NAME, SiteVerificationFields);
    }


    private void createServiceJobCardTable(SQLiteDatabase db) {
        String SiteVerificationFields = ServiceJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ServiceJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                ServiceJobCardTable.Cols.ADDRESS + " VARCHAR, " +
                ServiceJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR, " +
                ServiceJobCardTable.Cols.SERVICE_ID + " VARCHAR, " +
                ServiceJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                ServiceJobCardTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                ServiceJobCardTable.Cols.CONSUMER_NO + " VARCHAR, " +
                ServiceJobCardTable.Cols.SERVICE_TYPE + " VARCHAR, " +
                ServiceJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                ServiceJobCardTable.Cols.MOBILE_NO + " VARCHAR, " +
                ServiceJobCardTable.Cols.SERVICE_NO + " VARCHAR, " +
                ServiceJobCardTable.Cols.DUE_DATE + " VARCHAR, " +
                ServiceJobCardTable.Cols.SCREEN + " VARCHAR, " +
                ServiceJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                ServiceJobCardTable.Cols.REQUEST_ID + " VARCHAR";
        createTable(db, ServiceJobCardTable.TABLE_NAME, SiteVerificationFields);
    }

    private void createNSCJobCardTable(SQLiteDatabase db) {
        String NSCJobCardTableFields = AssetJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AssetJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_CARD_ID + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_NAME + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_CATEGORY + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_MAKE + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_MAKE_NO + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_LOCATION + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_CARD_STATUS + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE + " VARCHAR, " +
                AssetJobCardTable.Cols.ASSET_SUBMITTED_DATE + " VARCHAR";
        createTable(db, AssetJobCardTable.TABLE_NAME, NSCJobCardTableFields);
    }

    private void createComplaintJobCardTable(SQLiteDatabase db) {
        String ComplaintJobCardTableFields = ComplaintJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ComplaintJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                ComplaintJobCardTable.Cols.COMPLAINT_ID + " VARCHAR, " +
                ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID + " VARCHAR, " +
                ComplaintJobCardTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                ComplaintJobCardTable.Cols.CONSUMER_NO + " VARCHAR, " +
                ComplaintJobCardTable.Cols.MOBILE_NO + " VARCHAR, " +
                ComplaintJobCardTable.Cols.COMPLAINT_TYPE + " VARCHAR, " +
                ComplaintJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                ComplaintJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                ComplaintJobCardTable.Cols.ADDRESS + " VARCHAR, " +
                ComplaintJobCardTable.Cols.DUE_DATE + " VARCHAR, " +
                ComplaintJobCardTable.Cols.SCREEN + " VARCHAR, " +
                ComplaintJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                ComplaintJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR";
        createTable(db, ComplaintJobCardTable.TABLE_NAME, ComplaintJobCardTableFields);
    }
    private void createConsumerEnquiryTable(SQLiteDatabase db) {
        String ConsumerEnquiryTableFields = ConsumerEnquiryTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ComplaintJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.MOBILE_NO + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.ENQUIRY_ID + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.ENQUIRY_NO + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.ASSIGN_DATE + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.DUE_DATE + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.CARD_STATUS + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.SCREEN + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.STATE + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.CITY + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.STATE_ID + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.CITY_ID + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.NSC_ID + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.COMPLETED_ON + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.REGISTRATION_ID + " VARCHAR, " +
                ConsumerEnquiryTable.Cols.JOB_AREA + " VARCHAR";
        createTable(db, ConsumerEnquiryTable.TABLE_NAME, ConsumerEnquiryTableFields);
    }

    private void createMeterConversionAssignmentTable(SQLiteDatabase db) {
        String MeterConversionAssignmentTableFields = ConversionJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConversionJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                ConversionJobCardTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                ConversionJobCardTable.Cols.MOBILE_NO + " VARCHAR, " +
                ConversionJobCardTable.Cols.CONVERSION_ID + " VARCHAR, " +
                ConversionJobCardTable.Cols.JOB_ID + " VARCHAR, " +
                ConversionJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR, " +
                ConversionJobCardTable.Cols.DUE_DATE + " VARCHAR, " +
                ConversionJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                ConversionJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                ConversionJobCardTable.Cols.REGULATOR_NO + " VARCHAR, " +
                ConversionJobCardTable.Cols.METER_NO + " VARCHAR, " +
                ConversionJobCardTable.Cols.INSTALLED_ON + " VARCHAR, " +
                ConversionJobCardTable.Cols.RFC_VERIFIED_ON + " VARCHAR, " +
                ConversionJobCardTable.Cols.REJECTION_REMARK + " VARCHAR, " +
                ConversionJobCardTable.Cols.SCREEN + " VARCHAR, " +
                ConversionJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                ConversionJobCardTable.Cols.AREA_NAME + " VARCHAR, " +
                ConversionJobCardTable.Cols.JOB_AREA + " VARCHAR";
        createTable(db, ConversionJobCardTable.TABLE_NAME, MeterConversionAssignmentTableFields);
    }

    private void createAllJobCardTable(SQLiteDatabase db){
        String AllJobCardTableFields = AllJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AllJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                AllJobCardTable.Cols.CONSUMER_NAME + " VARCHAR, " +
                AllJobCardTable.Cols.MOBILE_NO + " VARCHAR, " +
                AllJobCardTable.Cols.STATUS + " VARCHAR, " +
                AllJobCardTable.Cols.ENQUIRY_ID + " VARCHAR, " +
                AllJobCardTable.Cols.ASSIGNED_DATE + " VARCHAR, " +
                AllJobCardTable.Cols.DUE_DATE + " VARCHAR, " +
                AllJobCardTable.Cols.SCREEN + " VARCHAR, " +
                AllJobCardTable.Cols.STATE_ID + " VARCHAR, " +
                AllJobCardTable.Cols.CITY_ID + " VARCHAR, " +
                AllJobCardTable.Cols.STATE + " VARCHAR, " +
                AllJobCardTable.Cols.CITY + " VARCHAR, " +
                AllJobCardTable.Cols.NSC_ID + " VARCHAR, " +
                AllJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                AllJobCardTable.Cols.REGISTRATION_ID + " VARCHAR, " +
                AllJobCardTable.Cols.JOB_AREA + " VARCHAR, " +
                AllJobCardTable.Cols.ENQUIRY_NO + " VARCHAR, " +
                AllJobCardTable.Cols.ADDRESS + " VARCHAR, " +
                AllJobCardTable.Cols.CATEGORY + " VARCHAR, " +
                AllJobCardTable.Cols.SUB_CATEGORY + " VARCHAR, " +
                AllJobCardTable.Cols.ACCEPT_STATUS + " VARCHAR, " +
                AllJobCardTable.Cols.TYPE_OF_PREMISE + " VARCHAR, " +
                AllJobCardTable.Cols.SERVICE_REQUESTED + " VARCHAR, " +
                AllJobCardTable.Cols.REJECTION_REMARK + " VARCHAR, " +
                AllJobCardTable.Cols.LAT + " VARCHAR, " +
                AllJobCardTable.Cols.LONG + " VARCHAR, " +
                AllJobCardTable.Cols.REQUEST_ID + " VARCHAR, " +
                AllJobCardTable.Cols.SITE_VERIFICATION_ID + " VARCHAR, " +
                AllJobCardTable.Cols.METER_ID + " VARCHAR, " +
                AllJobCardTable.Cols.AREA + " VARCHAR, " +
                AllJobCardTable.Cols.CONSUMER_NO + " VARCHAR, " +
                AllJobCardTable.Cols.METER_CATEGORY + " VARCHAR, " +
                AllJobCardTable.Cols.METER_SUBCATEGORY + " VARCHAR, " +
                AllJobCardTable.Cols.CONVERSION_ID + " VARCHAR, " +
                AllJobCardTable.Cols.JOB_ID + " VARCHAR, " +
                AllJobCardTable.Cols.METER_INSTALL_ID + " VARCHAR, " +
                AllJobCardTable.Cols.REGULATOR_NO + " VARCHAR, " +
                AllJobCardTable.Cols.METER_NO + " VARCHAR, " +
                AllJobCardTable.Cols.INSTALLED_ON + " VARCHAR, " +
                AllJobCardTable.Cols.RFC_VERIFIED_ON + " VARCHAR, " +
                AllJobCardTable.Cols.SERVICE_ID + " VARCHAR, " +
                AllJobCardTable.Cols.SERVICE_NO + " VARCHAR, " +
                AllJobCardTable.Cols.SERVICE_TYPE + " VARCHAR, " +
                AllJobCardTable.Cols.COMPLAINT_ID + " VARCHAR, " +
                AllJobCardTable.Cols.COMPLAINT_REQUEST_ID + " VARCHAR, " +
                AllJobCardTable.Cols.COMPLAINT_TYPE + " VARCHAR";
        createTable(db, AllJobCardTable.TABLE_NAME, AllJobCardTableFields);
    }


    private void createRegistrationTable(SQLiteDatabase db){
        String RegistrationTableFields = RegistrationTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RegistrationTable.Cols.ENQUIRY_ID + " VARCHAR, " +
                RegistrationTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                RegistrationTable.Cols.CONSUMER_CATEGORY + " VARCHAR, " +
                RegistrationTable.Cols.CONSUMER_SUB_CATEGORY + " VARCHAR, " +
                RegistrationTable.Cols.STATE + " VARCHAR, " +
                RegistrationTable.Cols.NAME + " VARCHAR, " +
                RegistrationTable.Cols.ADHAAR + " VARCHAR, " +
                RegistrationTable.Cols.EMAIL_ID + " VARCHAR, " +
                RegistrationTable.Cols.FLAT_NO + " VARCHAR, " +
                RegistrationTable.Cols.BUILDING_NAME + " VARCHAR, " +
                RegistrationTable.Cols.LOCATION + " VARCHAR, " +
                RegistrationTable.Cols.AREA + " VARCHAR, " +
                RegistrationTable.Cols.CITY + " VARCHAR, " +
                RegistrationTable.Cols.PINCODE + " VARCHAR, " +
                RegistrationTable.Cols.MOBILE + " VARCHAR, " +
                RegistrationTable.Cols.PREMISE + " VARCHAR, " +
                RegistrationTable.Cols.ENQUIRY_NO + " VARCHAR, " +
                RegistrationTable.Cols.CONSUMER_NO + " VARCHAR, " +
                RegistrationTable.Cols.SAVE_TYPE + " VARCHAR, " +
                RegistrationTable.Cols.SCHEME_NAME + " VARCHAR, " +
                RegistrationTable.Cols.SCHEME_AMOUNT + " VARCHAR, " +
                RegistrationTable.Cols.PAYMENT_METHOD + " VARCHAR, " +
                RegistrationTable.Cols.CHEQUE_NO + " VARCHAR, " +
                RegistrationTable.Cols.CHEQUE_BRANCH + " VARCHAR, " +
                RegistrationTable.Cols.CHEQUE_DATE + " VARCHAR, " +
                RegistrationTable.Cols.CHEQUE_BANK + " VARCHAR, " +
                RegistrationTable.Cols.CHEQUE_MICR + " VARCHAR, " +
                RegistrationTable.Cols.DD_NO + " VARCHAR, " +
                RegistrationTable.Cols.BANK_NAME + " VARCHAR, " +
                RegistrationTable.Cols.DD_DATE + " VARCHAR, " +
                RegistrationTable.Cols.DD_MICR + " VARCHAR, " +
                RegistrationTable.Cols.DD_BRANCH + " VARCHAR, " +
                RegistrationTable.Cols.LATITUDE + " VARCHAR, " +
                RegistrationTable.Cols.LONGITUDE + " VARCHAR, " +
                RegistrationTable.Cols.CONNECTIVITY + " VARCHAR, " +
                RegistrationTable.Cols.IS_NSC_NEW + " VARCHAR, " +
                RegistrationTable.Cols.FILE_0 + " BLOB, " +
                RegistrationTable.Cols.FILE_1 + " BLOB, " +
                RegistrationTable.Cols.FILE_ADD_PROOF_0 + " BLOB, " +
                RegistrationTable.Cols.FILE_ADD_PROOF_1 + " BLOB, " +
                RegistrationTable.Cols.FILE_NOC_PROOF + " BLOB, " +
                RegistrationTable.Cols.FIELD_CHEQUE_DD + " BLOB, " +
                RegistrationTable.Cols.FILE_SIGN + " BLOB, " +
                RegistrationTable.Cols.IMAGE_COUNT + " VARCHAR, " +
                RegistrationTable.Cols.IMAGE_COUNT_ADD + " VARCHAR, " +
                RegistrationTable.Cols.VENDOR_ID + " VARCHAR, " +
                RegistrationTable.Cols.FIELD_FORCE_ID + " VARCHAR, " +
                RegistrationTable.Cols.DOCUMENTS + " VARCHAR, " +
                RegistrationTable.Cols.DOCUMENTS_ADD + " VARCHAR, " +
                RegistrationTable.Cols.FLOOR_NO + " VARCHAR, " +
                RegistrationTable.Cols.PLOT_NO + " VARCHAR, " +
                RegistrationTable.Cols.WING + " VARCHAR, " +
                RegistrationTable.Cols.ROAD_NO + " VARCHAR, " +
                RegistrationTable.Cols.LANDMARK + " VARCHAR, " +
                RegistrationTable.Cols.DISTRICT + " VARCHAR, " +
                RegistrationTable.Cols.SOCIETY_NAME + " VARCHAR, " +
                RegistrationTable.Cols.REGISTRATION_NO + " VARCHAR, " +
                RegistrationTable.Cols.BANK + " VARCHAR, " +
                RegistrationTable.Cols.BRANCH_NAME + " VARCHAR, " +
                RegistrationTable.Cols.MICR_CODE + " VARCHAR, " +
                RegistrationTable.Cols.CHEQUE_DD_NUMBER + " VARCHAR, " +
                RegistrationTable.Cols.DATE_OF_PAYMENT + " VARCHAR, " +
                RegistrationTable.Cols.CARD_STATUS + " VARCHAR, " +
                RegistrationTable.Cols.IS_REJECTED + " VARCHAR, " +
                RegistrationTable.Cols.NSC_ID + " VARCHAR, " +
                RegistrationTable.Cols.FILE_CONSUMER_PHOTO + " BLOB, " +
                RegistrationTable.Cols.AREA_NAME + " VARCHAR";
                createTable(db, RegistrationTable.TABLE_NAME, RegistrationTableFields);
    }

    private void createRejectedJobCardTable(SQLiteDatabase db){
        String RejectedJobCardTableFields = RejectedJobCardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RejectedJobCardTable.Cols.NAME + " VARCHAR, " +
                RejectedJobCardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                RejectedJobCardTable.Cols.AREA_NAME + " VARCHAR, " +
                RejectedJobCardTable.Cols.CARD_STATUS + " VARCHAR, " +
                RejectedJobCardTable.Cols.COMPLETED_ON + " VARCHAR, " +
                RejectedJobCardTable.Cols.REGISTRATION_NO + " VARCHAR";
        createTable(db, RejectedJobCardTable.TABLE_NAME, RejectedJobCardTableFields);
    }

    private void createAreaTable(SQLiteDatabase db){
        String AreaTableFields = AreaTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AreaTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                AreaTable.Cols.AREA_ID + " VARCHAR, " +
                AreaTable.Cols.AREA_NAME + " VARCHAR";
        createTable(db, AreaTable.TABLE_NAME, AreaTableFields);
    }

    private void createBankTable(SQLiteDatabase db){
        String BankTableFields = BankTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BankTable.Cols.BANK_NAME + " VARCHAR";
        createTable(db, BankTable.TABLE_NAME, BankTableFields);
    }

    private void createSchemeTable(SQLiteDatabase db){
        String SchemeTableFields = PaymentTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PaymentTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                PaymentTable.Cols.SCHEME_ID + " VARCHAR, " +
                PaymentTable.Cols.SCHEME_NAME + " VARCHAR, " +
                PaymentTable.Cols.SCHEME_AMOUNT + " VARCHAR";
        createTable(db, PaymentTable.TABLE_NAME, SchemeTableFields);
    }

    private void createDocumentTable(SQLiteDatabase db){
        String DocumentTableFields = IdProofTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IdProofTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                IdProofTable.Cols.IDPROOF_ID + " VARCHAR, " +
                IdProofTable.Cols.DOCUMENT_NAME + " VARCHAR, "+
                IdProofTable.Cols.DOCUMENT_TYPE + " VARCHAR";
        createTable(db, IdProofTable.TABLE_NAME, DocumentTableFields);
    }
    private void createAddDocumentTable(SQLiteDatabase db){
        String AddDocumentTableFields = AddProofTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AddProofTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                AddProofTable.Cols.ADDPROOF_ID + " VARCHAR, " +
                AddProofTable.Cols.DOCUMENT_NAME + " VARCHAR, "+
                AddProofTable.Cols.DOCUMENT_TYPE + " VARCHAR";
        createTable(db, AddProofTable.TABLE_NAME, AddDocumentTableFields);
    }
    private void createWardTable(SQLiteDatabase db){
        String WardTableFields = WardTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WardTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                WardTable.Cols.WARD_ID + " VARCHAR, " +
                WardTable.Cols.WARD_NAME + " VARCHAR, " +
                WardTable.Cols.AREA_ID + " VARCHAR";
        createTable(db, WardTable.TABLE_NAME, WardTableFields);
    }
    private void createCategoryTable(SQLiteDatabase db){
        String CategoryTableFields = CategoryTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoryTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                CategoryTable.Cols.CATEGORY_ID + " VARCHAR, " +
                CategoryTable.Cols.CATEGORY_NAME + " VARCHAR";
        createTable(db, CategoryTable.TABLE_NAME, CategoryTableFields);
    }
    private void createSubCategoryTable(SQLiteDatabase db){
        String SubCategoryTableFields = SubCategoryTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SubCategoryTable.Cols.SUBCATEGORY_ID + " VARCHAR, " +
                SubCategoryTable.Cols.SUBCATEGORY_NAME + " VARCHAR";
        createTable(db, SubCategoryTable.TABLE_NAME, SubCategoryTableFields);
    }
    private void createPincodeTable(SQLiteDatabase db){
        String PincodeTableFields = Pincode.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Pincode.Cols.USER_LOGIN_ID + " VARCHAR, " +
                Pincode.Cols.AREA_ID + " VARCHAR, " +
                Pincode.Cols.PINCODE_ID + " VARCHAR, " +
                Pincode.Cols.PINCODE + " VARCHAR";
        createTable(db, Pincode.TABLE_NAME, PincodeTableFields);
    }
    private void createLocationTable(SQLiteDatabase db){
        String LocationTableFields = LocationTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LocationTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                LocationTable.Cols.AREA_ID + " VARCHAR, " +
                LocationTable.Cols.LOCATION_ID + " VARCHAR, " +
                LocationTable.Cols.LOCATION_NAME + " VARCHAR";
        createTable(db, LocationTable.TABLE_NAME, LocationTableFields);
    }

    private void createLandmarkTable(SQLiteDatabase db){
        String LandmarkTableFields = LandmarkTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LandmarkTable.Cols.USER_LOGIN_ID + " VARCHAR, " +
                LandmarkTable.Cols.AREA_ID + " VARCHAR, " +
                LandmarkTable.Cols.LANDMARK_ID + " VARCHAR, " +
                LandmarkTable.Cols.LANDMARK_NAME + " VARCHAR";
        createTable(db, LandmarkTable.TABLE_NAME, LandmarkTableFields);
    }

    public void dropTable(SQLiteDatabase db, String name) {
        String query = MessageFormat.format(DatabaseHelper.KEY_DROP_TABLE, name);
        db.execSQL(query);
    }

    public static boolean exists(SQLiteDatabase db, String name) {
        Cursor cur = db.rawQuery(SQL, new String[]{name});
        cur.moveToFirst();
        int tables = cur.getInt(0);
        if (tables > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void createTable(SQLiteDatabase db, String name, String fields) {
        String query = MessageFormat.format(DatabaseHelper.KEY_CREATE_TABLE,
                name, fields);
        db.execSQL(query);
    }
}