package com.fieldforce.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.fieldforce.R;
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
import com.fieldforce.db.tables.SubCategoryTable;
import com.fieldforce.db.tables.WardTable;
import com.fieldforce.models.Consumer;
import com.fieldforce.models.ImageModel;
import com.fieldforce.models.NotificationCard;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.db.tables.SiteVerificationJobCardTable;
import com.fieldforce.models.HistoryModel;
import com.fieldforce.models.UserProfileModel;
import com.fieldforce.ui.activities.RegistrationFormActivity;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class acts as an interface between database and UI. It contains all the
 * methods to interact with device database.
 *
 * @author Bynry01
 */
public class DatabaseManager {
    /**
     * Save User to Login table
     *
     * @param context Context
     */
    public static int cnt;


    public static String getDbPath(Context context) {
        return context.getDatabasePath("P2P.db").getAbsolutePath();
    }

    // LoginTable insertion
    public static void saveLoginDetails(Context loginActivity, UserProfileModel user_info) {
        DatabaseManager.saveUserInfo(loginActivity, user_info);
    }

    private static void saveUserInfo(Context context, UserProfileModel userProfilesModel) {
        if (userProfilesModel != null) {
            ContentValues values = getContentValuesUserInfoTable(userProfilesModel);
            saveValues(context, LoginTable.CONTENT_URI, values, null);
        }
    }

    private static void saveValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private static ContentValues getContentValuesUserInfoTable(UserProfileModel userProfileModel) {
        ContentValues values = new ContentValues();
        try {
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(new Date());
            values.put(LoginTable.Cols.USER_ID, userProfileModel.user_id != null ? userProfileModel.user_id : "");
            values.put(LoginTable.Cols.USER_NAME, userProfileModel.user_name != null ? userProfileModel.user_name : "");
            values.put(LoginTable.Cols.USER_LOGIN_ID, userProfileModel.emp_id != null ? userProfileModel.emp_id : "");
            values.put(LoginTable.Cols.USER_CITY, userProfileModel.city != null ? userProfileModel.city : "");
            values.put(LoginTable.Cols.USER_EMAIL, userProfileModel.email_id != null ? userProfileModel.email_id : "");
            values.put(LoginTable.Cols.USER_CONTACT_NO, userProfileModel.contact_no != null ? userProfileModel.contact_no : "");
            values.put(LoginTable.Cols.USER_ADDRESS, userProfileModel.address != null ? userProfileModel.address : "");
            values.put(LoginTable.Cols.EMP_TYPE, userProfileModel.emp_type != null ? userProfileModel.emp_type : "");
            values.put(LoginTable.Cols.LOGIN_DATE, date);
            values.put(LoginTable.Cols.LOGIN_LAT, "");
            values.put(LoginTable.Cols.LOGIN_LNG, "");


            values.put(LoginTable.Cols.USER_STATE, userProfileModel.state != null ? userProfileModel.state : "");
            values.put(LoginTable.Cols.STATE_ID, userProfileModel.stateId != null ? userProfileModel.stateId : "");
            values.put(LoginTable.Cols.CITY_ID, userProfileModel.cityId != null ? userProfileModel.cityId : "");
            values.put(LoginTable.Cols.USER_TYPE, userProfileModel.userType != null ? userProfileModel.userType : "");


            values.put(LoginTable.Cols.USER_DISTRICT, userProfileModel.district != null ? userProfileModel.district : "");
            values.put(LoginTable.Cols.DISTRICT_ID, userProfileModel.districtId != null ? userProfileModel.districtId : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }


    // Database functions related to Area created be Saloni 14-01-2019

    public static void saveArea(Context loginActivity, ArrayList<Consumer> consumer) {
        for (Consumer consumerModel : consumer){
            DatabaseManager.saveAreaInfo(loginActivity, consumerModel);

        }
    }

    private static void saveAreaInfo(Context context, Consumer consumer) {
        if (consumer != null) {
            ContentValues values = getContentValuesAreaInfoTable(context, consumer);
            String condition = AreaTable.Cols.AREA_ID + "='" + consumer.areaID + "'";
            saveAreaValues(context, AreaTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesAreaInfoTable(Context context, Consumer consumer) {
        ContentValues values = new ContentValues();
        try {
            values.put(AreaTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(AreaTable.Cols.AREA_ID, consumer.areaID != null ? consumer.areaID : "");
            values.put(AreaTable.Cols.AREA_NAME, consumer.area != null ? consumer.area : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveAreaValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getAreas(Context context, String userId) {
        String condition = AreaTable.Cols.USER_LOGIN_ID + "='" + userId + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(AreaTable.CONTENT_URI, null,
                condition, null, AreaTable.Cols.AREA_ID + " ASC ");
        ArrayList<Consumer> areas = getAreaFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return areas;
    }

    private static ArrayList<Consumer> getAreaFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getAreasFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getAreasFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.areaID = cursor.getString(cursor.getColumnIndex(AreaTable.Cols.AREA_ID)) != null ? cursor.getString(cursor.getColumnIndex(AreaTable.Cols.AREA_ID)) : "";
        consumerModel.area = cursor.getString(cursor.getColumnIndex(AreaTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(AreaTable.Cols.AREA_NAME)) : "";
        return consumerModel;
    }

    // Area Related Functions end here

    // Database functions related to payment created be Jayshree 21-01-2020
    public static void savePaymentScheme(Context loginActivity, ArrayList<Consumer> scheme) {
        for (Consumer schemeName : scheme){
            DatabaseManager.savePaymentInfo(loginActivity, schemeName);

        }
    }

    private static void savePaymentInfo(Context context, Consumer schemeName) {
        if (schemeName != null) {
            ContentValues values = getContentValuesPaymentInfoTable(context, schemeName);
            String condition = PaymentTable.Cols.SCHEME_ID + "='" + schemeName.schemeId + "'";
            saveSchemeValues(context, PaymentTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesPaymentInfoTable(Context context, Consumer consumer) {
        ContentValues values = new ContentValues();
        try {
            values.put(PaymentTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(PaymentTable.Cols.SCHEME_ID, consumer.schemeId != null ? consumer.schemeId : "");
            values.put(PaymentTable.Cols.SCHEME_NAME, consumer.schemeName != null ? consumer.schemeName : "");
            values.put(PaymentTable.Cols.SCHEME_AMOUNT, consumer.schemeAmount != null ? consumer.schemeAmount : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveSchemeValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getPayment(Context context, String userId) {
        String condition = PaymentTable.Cols.USER_LOGIN_ID + "='" + userId + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(PaymentTable.CONTENT_URI, null,
                condition, null, PaymentTable.Cols.ID + " ASC ");
        ArrayList<Consumer> payment = getSchemeFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return payment;
    }

    private static ArrayList<Consumer> getSchemeFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getPaymentFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getPaymentFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.schemeName = cursor.getString(cursor.getColumnIndex(PaymentTable.Cols.SCHEME_NAME)) != null ? cursor.getString(cursor.getColumnIndex(PaymentTable.Cols.SCHEME_NAME)) : "";
        consumerModel.schemeAmount = cursor.getString(cursor.getColumnIndex(PaymentTable.Cols.SCHEME_AMOUNT)) != null ? cursor.getString(cursor.getColumnIndex(PaymentTable.Cols.SCHEME_AMOUNT)) : "";
        return consumerModel;
    }
    // payment Related Functions end here

    // Database functions related to ID created be Jayshree 21-01-2020

    public static void saveIDProof(Context loginActivity, ArrayList<Consumer> idProof, String type) {
        for (Consumer consumerIdProof : idProof){
            DatabaseManager.saveIdProofInfo(loginActivity, consumerIdProof, type);

        }
    }

    private static void saveIdProofInfo(Context context, Consumer consumer, String type) {
        if (consumer != null) {
            ContentValues values = getContentValueIdProofInfoTable(context, consumer, type);
            String condition = IdProofTable.Cols.IDPROOF_ID + "='" + consumer.document_id + "'";
            saveIdProofValues(context, IdProofTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueIdProofInfoTable(Context context, Consumer consumer, String type) {
        ContentValues values = new ContentValues();
        try {
            values.put(IdProofTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(IdProofTable.Cols.IDPROOF_ID, consumer.document_id != null ? consumer.document_id : "");
            values.put(IdProofTable.Cols.DOCUMENT_NAME, consumer.document != null ? consumer.document : "");
            values.put(IdProofTable.Cols.DOCUMENT_TYPE, consumer.document_type != null ? consumer.document_type : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveIdProofValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getIdProof(Context context, String userId) {
        String condition = IdProofTable.Cols.USER_LOGIN_ID + "='" + userId + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(IdProofTable.CONTENT_URI, null,
                condition, null, IdProofTable.Cols.IDPROOF_ID + " ASC ");
        ArrayList<Consumer> idproof = getIdProofFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return idproof;
    }

    private static ArrayList<Consumer> getIdProofFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getIdProofsFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getIdProofsFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.document_id = cursor.getString(cursor.getColumnIndex(IdProofTable.Cols.IDPROOF_ID)) != null ? cursor.getString(cursor.getColumnIndex(IdProofTable.Cols.IDPROOF_ID)) : "";
        consumerModel.document = cursor.getString(cursor.getColumnIndex(IdProofTable.Cols.DOCUMENT_NAME)) != null ? cursor.getString(cursor.getColumnIndex(IdProofTable.Cols.DOCUMENT_NAME)) : "";
        return consumerModel;
    }

    // IdProof Related Functions end here

    // Database functions related to AddressProof created be Jayshree 22-01-2020

    public static void saveAddProof(Context loginActivity, ArrayList<Consumer> addProof) {
        for (Consumer consumerAddProof : addProof){
            DatabaseManager.saveAddProofInfo(loginActivity, consumerAddProof);

        }
    }

    private static void saveAddProofInfo(Context context, Consumer consumer) {
        if (consumer != null) {
            ContentValues values = getContentValueAddProofInfoTable(context, consumer);
            String condition = AddProofTable.Cols.ADDPROOF_ID + "='" + consumer.document_id + "'";
            saveAddProofValues(context, AddProofTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueAddProofInfoTable(Context context, Consumer consumer) {
        ContentValues values = new ContentValues();
        try {
            values.put(AddProofTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(AddProofTable.Cols.ADDPROOF_ID, consumer.document_id != null ? consumer.document_id : "");
            values.put(AddProofTable.Cols.DOCUMENT_NAME, consumer.document != null ? consumer.document : "");
            values.put(AddProofTable.Cols.DOCUMENT_TYPE, consumer.document_type != null ? consumer.document_type : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveAddProofValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getAddProof(Context context, String userId) {
        String condition = AddProofTable.Cols.USER_LOGIN_ID + "='" + userId + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(AddProofTable.CONTENT_URI, null,
                condition, null, AddProofTable.Cols.ADDPROOF_ID + " ASC ");
        ArrayList<Consumer> addproof = getAddProofFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return addproof;
    }

    private static ArrayList<Consumer> getAddProofFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getAddProofsFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getAddProofsFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.document_id = cursor.getString(cursor.getColumnIndex(AddProofTable.Cols.ADDPROOF_ID)) != null ? cursor.getString(cursor.getColumnIndex(AddProofTable.Cols.ADDPROOF_ID)) : "";
        consumerModel.document = cursor.getString(cursor.getColumnIndex(AddProofTable.Cols.DOCUMENT_NAME)) != null ? cursor.getString(cursor.getColumnIndex(AddProofTable.Cols.DOCUMENT_NAME)) : "";
        return consumerModel;
    }

    // AddresssDocument Related Functions end here

    // Database functions related to Category created be Jayshree 22-01-2020

    public static void saveCategory(Context loginActivity, ArrayList<Consumer> category) {
        for (Consumer consumerCategory : category){
            DatabaseManager.saveCategoryInfo(loginActivity, consumerCategory);

        }
    }

    private static void saveCategoryInfo(Context context, Consumer consumer) {
        if (consumer != null) {
            ContentValues values = getContentValueCategoryInfoTable(context, consumer);
            String condition = CategoryTable.Cols.CATEGORY_ID + "='" + consumer.consumerCategoryId + "'";
            saveCategoryValues(context, CategoryTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueCategoryInfoTable(Context context, Consumer consumer) {
        ContentValues values = new ContentValues();
        try {
            values.put(CategoryTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(CategoryTable.Cols.CATEGORY_ID, consumer.consumerCategoryId != null ? consumer.consumerCategoryId : "");
            values.put(CategoryTable.Cols.CATEGORY_NAME, consumer.consumerCategory != null ? consumer.consumerCategory : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveCategoryValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getCategory(Context context, String userId) {
        String condition = CategoryTable.Cols.USER_LOGIN_ID + "='" + userId + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CategoryTable.CONTENT_URI, null,
                condition, null, CategoryTable.Cols.CATEGORY_ID + " ASC ");
        ArrayList<Consumer> category = getCategoryFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return category;
    }

    private static ArrayList<Consumer> getCategoryFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getCategorysFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getCategorysFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.consumerCategoryId = cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORY_ID)) != null ? cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORY_ID)) : "";
        consumerModel.consumerCategory = cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORY_NAME)) != null ? cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORY_NAME)) : "";
        return consumerModel;
    }

    // Category Related Functions end here

    // Database functions related to SubCategory created be Jayshree 25-01-2020

    public static void saveSubCategory(Context loginActivity, ArrayList<Consumer> SubCategory) {
        for (Consumer consumerSubCategory : SubCategory){
            DatabaseManager.saveSubCategoryInfo(loginActivity, consumerSubCategory);

        }
    }

    private static void saveSubCategoryInfo(Context context, Consumer consumer) {
        if (consumer != null) {
            ContentValues values = getContentValueSubCategoryInfoTable(context, consumer);
            String condition = SubCategoryTable.Cols.SUBCATEGORY_ID + "='" + consumer.consumer_subcategory_id + "'";
            saveSubCategoryValues(context, SubCategoryTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueSubCategoryInfoTable(Context context, Consumer consumer) {
        ContentValues values = new ContentValues();
        try {
            values.put(SubCategoryTable.Cols.SUBCATEGORY_ID, consumer.consumer_subcategory_id != null ? consumer.consumer_subcategory_id : "");
            values.put(SubCategoryTable.Cols.SUBCATEGORY_NAME, consumer.consumer_subcategory != null ? consumer.consumer_subcategory : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveSubCategoryValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getSubCategory(Context context, String userId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(SubCategoryTable.CONTENT_URI, null,
                null, null, SubCategoryTable.Cols.SUBCATEGORY_ID + " ASC ");
        ArrayList<Consumer> subCategory = getSubCategoryFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return subCategory;
    }

    private static ArrayList<Consumer> getSubCategoryFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getSubCategorysFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getSubCategorysFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.consumer_subcategory_id = cursor.getString(cursor.getColumnIndex(SubCategoryTable.Cols.SUBCATEGORY_ID)) != null ? cursor.getString(cursor.getColumnIndex(SubCategoryTable.Cols.SUBCATEGORY_ID)) : "";
        consumerModel.consumer_subcategory = cursor.getString(cursor.getColumnIndex(SubCategoryTable.Cols.SUBCATEGORY_NAME)) != null ? cursor.getString(cursor.getColumnIndex(SubCategoryTable.Cols.SUBCATEGORY_NAME)) : "";
        return consumerModel;
    }

    // SubCategory Related Functions end here

    // Database functions related to Ward created be Jayshree 23-01-2020

    public static void saveWard(Context loginActivity, ArrayList<Consumer> ward, String areaId) {
        for (Consumer consumerWard : ward){
            DatabaseManager.saveWardInfo(loginActivity, consumerWard, areaId);

        }
    }

    private static void saveWardInfo(Context context, Consumer consumer, String areaId) {
        if (consumer != null) {
            ContentValues values = getContentValueWardInfoTable(context, consumer, areaId);
            String condition = WardTable.Cols.WARD_ID + "='" + consumer.wardID + "'";
            saveWardValues(context, WardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueWardInfoTable(Context context, Consumer consumer, String areaId) {
        ContentValues values = new ContentValues();
        try {
            values.put(WardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(WardTable.Cols.WARD_ID, consumer.wardID != null ? consumer.wardID : "");
            values.put(WardTable.Cols.WARD_NAME, consumer.ward != null ? consumer.ward : "");
            values.put(WardTable.Cols.AREA_ID, consumer.areaID != null ? consumer.areaID : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveWardValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getWard(Context context, String userId, String areaId) {
        String condition = WardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " + WardTable.Cols.AREA_ID + "='" + areaId +"'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(WardTable.CONTENT_URI, null,
                condition, null, WardTable.Cols.WARD_ID + " ASC ");
        ArrayList<Consumer> ward = getWardFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return ward;
    }

    private static ArrayList<Consumer> getWardFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getWardsFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getWardsFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.wardID = cursor.getString(cursor.getColumnIndex(WardTable.Cols.WARD_ID)) != null ? cursor.getString(cursor.getColumnIndex(WardTable.Cols.WARD_ID)) : "";
        consumerModel.ward = cursor.getString(cursor.getColumnIndex(WardTable.Cols.WARD_NAME)) != null ? cursor.getString(cursor.getColumnIndex(WardTable.Cols.WARD_NAME)) : "";
        consumerModel.areaID = cursor.getString(cursor.getColumnIndex(WardTable.Cols.AREA_ID)) != null ? cursor.getString(cursor.getColumnIndex(WardTable.Cols.AREA_ID)) : "";

        return consumerModel;
    }

    // Ward Related Functions end here




    // Bank Related Functions start here
    public static void saveBankNames(Context loginActivity, ArrayList<Consumer> banks) {
        for (Consumer bankName : banks){
            DatabaseManager.saveBankInfo(loginActivity, bankName);
        }
    }

    private static void saveBankInfo(Context context, Consumer bankName) {
        if (bankName != null) {
            ContentValues values = getContentValuesBankInfoTable(context, bankName);
            String condition = BankTable.Cols.BANK_NAME + "='" + bankName + "'";
            saveBankValues(context, BankTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesBankInfoTable(Context context, Consumer bankName) {
        ContentValues values = new ContentValues();
        try {
            values.put(BankTable.Cols.BANK_NAME, bankName.bank_name != null ? bankName.bank_name : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveBankValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }


    public static ArrayList<Consumer> getBanks(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(BankTable.CONTENT_URI, null,
                null, null, BankTable.Cols.ID + " ASC ");
        ArrayList<Consumer> banks = getBankFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return banks;
    }

    private static ArrayList<Consumer> getBankFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getBanksFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getBanksFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.bank_name = cursor.getString(cursor.getColumnIndex(BankTable.Cols.BANK_NAME)) != null ? cursor.getString(cursor.getColumnIndex(BankTable.Cols.BANK_NAME)) : "";
        return consumerModel;
    }
    // Bank Related Functions end here

    // Database functions related to Pincode created be Jayshree 25-01-2020

    public static void savePincode(Context loginActivity, ArrayList<Consumer> pincode, String areaId) {
        for (Consumer consumerPincode : pincode){
            DatabaseManager.savePincodeInfo(loginActivity, consumerPincode, areaId);

        }
    }

    private static void savePincodeInfo(Context context, Consumer consumer, String areaId) {
        if (consumer != null) {
            ContentValues values = getContentValuePincodeInfoTable(context, consumer, areaId);
            String condition = Pincode.Cols.PINCODE_ID + "='" + consumer.pincode_id + "'";
            savePincodeValues(context, Pincode.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuePincodeInfoTable(Context context, Consumer consumer, String areaId) {
        ContentValues values = new ContentValues();
        try {
            values.put(Pincode.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(Pincode.Cols.PINCODE_ID, consumer.pincode_id != null ? consumer.pincode_id : "");
            values.put(Pincode.Cols.PINCODE, consumer.pincode != null ? consumer.pincode : "");
            values.put(Pincode.Cols.AREA_ID, consumer.areaID != null ? consumer.areaID : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void savePincodeValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getPincode(Context context, String userId, String areaId) {
        String condition = Pincode.Cols.USER_LOGIN_ID + "='" + userId + "' AND " + Pincode.Cols.AREA_ID + "='" + areaId +"'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Pincode.CONTENT_URI, null,
                condition, null, Pincode.Cols.PINCODE_ID + " ASC ");
        ArrayList<Consumer> pincode = getPincodeFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return pincode;
    }

    private static ArrayList<Consumer> getPincodeFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getPincodesFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getPincodesFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.pincode_id = cursor.getString(cursor.getColumnIndex(Pincode.Cols.PINCODE_ID)) != null ? cursor.getString(cursor.getColumnIndex(Pincode.Cols.PINCODE_ID)) : "";
        consumerModel.pincode = cursor.getString(cursor.getColumnIndex(Pincode.Cols.PINCODE)) != null ? cursor.getString(cursor.getColumnIndex(Pincode.Cols.PINCODE)) : "";
        consumerModel.areaID = cursor.getString(cursor.getColumnIndex(Pincode.Cols.AREA_ID)) != null ? cursor.getString(cursor.getColumnIndex(Pincode.Cols.AREA_ID)) : "";

        return consumerModel;
    }


    // Pincode Related Functions end here

    // Database functions related to Location created be Jayshree 28-01-2020

    public static void saveLocation(Context loginActivity, ArrayList<Consumer> locationArrayList, String areaId) {
        for (Consumer consumerLocation : locationArrayList){
            DatabaseManager.saveLocationInfo(loginActivity, consumerLocation, areaId);

        }
    }

    private static void saveLocationInfo(Context context, Consumer consumer, String areaId) {
        if (consumer != null) {
            ContentValues values = getContentValueLocationInfoTable(context, consumer, areaId);
            String condition = LocationTable.Cols.LOCATION_ID + "='" + consumer.locationID + "'";
            saveLocationValues(context, LocationTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueLocationInfoTable(Context context, Consumer consumer, String areaId) {
        ContentValues values = new ContentValues();
        try {
            values.put(LocationTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(LocationTable.Cols.LOCATION_ID, consumer.locationID != null ? consumer.locationID : "");
            values.put(LocationTable.Cols.LOCATION_NAME, consumer.location != null ? consumer.location : "");
            values.put(LocationTable.Cols.AREA_ID, consumer.areaID != null ? consumer.areaID : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveLocationValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getLocation(Context context, String userId, String areaId) {
        String condition = LocationTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " + LocationTable.Cols.AREA_ID + "='" + areaId +"'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(LocationTable.CONTENT_URI, null,
                condition, null, LocationTable.Cols.LOCATION_ID + " ASC ");
        ArrayList<Consumer> location = getLocationFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return location;
    }

    private static ArrayList<Consumer> getLocationFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getLocationsFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getLocationsFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.locationID = cursor.getString(cursor.getColumnIndex(LocationTable.Cols.LOCATION_ID)) != null ? cursor.getString(cursor.getColumnIndex(LocationTable.Cols.LOCATION_ID)) : "";
        consumerModel.location = cursor.getString(cursor.getColumnIndex(LocationTable.Cols.LOCATION_NAME)) != null ? cursor.getString(cursor.getColumnIndex(LocationTable.Cols.LOCATION_NAME)) : "";
        consumerModel.areaID = cursor.getString(cursor.getColumnIndex(LocationTable.Cols.AREA_ID)) != null ? cursor.getString(cursor.getColumnIndex(LocationTable.Cols.AREA_ID)) : "";

        return consumerModel;
    }


    // Location Related Functions end here



    // Database functions related to Landmark created be Jayshree 28-01-2020

    public static void saveLandmark(Context loginActivity, ArrayList<Consumer> landmarkArrayList, String areaId) {
        for (Consumer consumerLandmark : landmarkArrayList){
            DatabaseManager.saveLandmarkInfo(loginActivity, consumerLandmark, areaId);

        }
    }

    private static void saveLandmarkInfo(Context context, Consumer consumer, String areaId) {
        if (consumer != null) {
            ContentValues values = getContentValueLandmarkInfoTable(context, consumer, areaId);
            String condition = LandmarkTable.Cols.LANDMARK_ID + "='" + consumer.landmarkID + "'";
            saveLandmarkValues(context, LandmarkTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValueLandmarkInfoTable(Context context, Consumer consumer, String areaId) {
        ContentValues values = new ContentValues();
        try {
            values.put(LandmarkTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(LandmarkTable.Cols.LANDMARK_ID, consumer.landmarkID != null ? consumer.landmarkID : "");
            values.put(LandmarkTable.Cols.LANDMARK_NAME, consumer.landmark != null ? consumer.landmark : "");
            values.put(LandmarkTable.Cols.AREA_ID, consumer.areaID != null ? consumer.areaID : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    private static void saveLandmarkValues(Context context, Uri table, ContentValues values, String condition) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(table, null,
                condition, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(table, values, condition, null);
        } else {
            resolver.insert(table, values);
        }
        if (cursor != null) {
            cursor.close();
        }
    }



    public static ArrayList<Consumer> getLandmark(Context context, String userId, String areaId) {
        String condition = LandmarkTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " + LandmarkTable.Cols.AREA_ID + "='" + areaId +"'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(LandmarkTable.CONTENT_URI, null,
                condition, null, LandmarkTable.Cols.LANDMARK_ID + " ASC ");
        ArrayList<Consumer> landmark = getLandmarkFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return landmark;
    }

    private static ArrayList<Consumer> getLandmarkFromCursor(Context context, Cursor cursor) {
        ArrayList<Consumer> consumerModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                Consumer consumerModel;
                consumerModels = new ArrayList<Consumer>();
                while (!cursor.isAfterLast()) {
                    consumerModel = getLandmarksFromCursor(context, cursor);
                    consumerModels.add(consumerModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return consumerModels;
    }


    private static Consumer getLandmarksFromCursor(Context context, Cursor cursor) {
        Consumer consumerModel = new Consumer();
        consumerModel.landmarkID = cursor.getString(cursor.getColumnIndex(LandmarkTable.Cols.LANDMARK_ID)) != null ? cursor.getString(cursor.getColumnIndex(LandmarkTable.Cols.LANDMARK_ID)) : "";
        consumerModel.landmark = cursor.getString(cursor.getColumnIndex(LandmarkTable.Cols.LANDMARK_NAME)) != null ? cursor.getString(cursor.getColumnIndex(LandmarkTable.Cols.LANDMARK_NAME)) : "";
        consumerModel.areaID = cursor.getString(cursor.getColumnIndex(LandmarkTable.Cols.AREA_ID)) != null ? cursor.getString(cursor.getColumnIndex(LandmarkTable.Cols.AREA_ID)) : "";

        return consumerModel;
    }


    // Landmark Related Functions end here


    //Notification Table related methods

    public static void saveNotification(Context context, NotificationCard noti) {
        if (noti != null) {
            ContentValues values = new ContentValues();
            try {
                values.put(NotificationTable.Cols.TITLE, noti.title);
                values.put(NotificationTable.Cols.MESSAGE, noti.message);
                values.put(NotificationTable.Cols.DATE, noti.date);
                values.put(NotificationTable.Cols.IS_READ, "false");
                values.put(NotificationTable.Cols.USER_LOGIN_ID, noti.isRead);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(NotificationTable.TABLE_NAME, null, values);
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public static void setReadNotification(Context context, String title) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotificationTable.Cols.IS_READ, "true");
        String[] args = new String[]{title};
        db.update(NotificationTable.TABLE_NAME, values, "title=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static int getCount(Context context, String flag, String userId) {
        int i = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + NotificationTable.TABLE_NAME + " where  "
                + NotificationTable.Cols.IS_READ + " = '" + flag + "' AND "
                + NotificationTable.Cols.USER_LOGIN_ID + " = '" + userId + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<NotificationCard> noti = new ArrayList<NotificationCard>();

        while (c.moveToNext()) {
            i++;
            NotificationCard notiCard = new NotificationCard();
            notiCard.title = c.getString(c.getColumnIndex("title"));
            notiCard.message = c.getString(c.getColumnIndex("message"));
            notiCard.date = c.getString(c.getColumnIndex("date"));
            notiCard.isRead = c.getString(c.getColumnIndex("is_read"));
            notiCard.userId = c.getString(c.getColumnIndex("user_login_id"));
            noti.add(notiCard);
        }
        db.close();
        return i;
    }

    public static ArrayList<NotificationCard> getAllNotification(Context context, String userId) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + NotificationTable.TABLE_NAME + " WHERE "
                + NotificationTable.Cols.USER_LOGIN_ID + " = '" + userId + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<NotificationCard> noti = new ArrayList<NotificationCard>();

        while (c.moveToNext()) {
            NotificationCard notiCard = new NotificationCard();
            notiCard.title = c.getString(c.getColumnIndex("title"));
            notiCard.message = c.getString(c.getColumnIndex("message"));
            notiCard.date = c.getString(c.getColumnIndex("date"));
            notiCard.isRead = c.getString(c.getColumnIndex("is_read"));
            notiCard.userId = c.getString(c.getColumnIndex("user_login_id"));
            noti.add(notiCard);
        }
        db.close();
        return noti;
    }

    public static void deleteAccount(Context context, String messageBody) {
        try {
            String condition = NotificationTable.Cols.MESSAGE + "='" + messageBody + "'";
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(NotificationTable.TABLE_NAME, condition, null);
            if (db.isOpen()) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // NSC job card insertion
    public static void saveCommissionJobCards(Context mContext, ArrayList<TodayModel> nsc_today) {
        for (TodayModel todayModel : nsc_today) {
            DatabaseManager.saveCommissionJobCardsInfo(mContext, todayModel);
        }
    }

    private static void saveCommissionJobCardsInfo(Context context, TodayModel nsc_today) {
        if (nsc_today != null) {
            ContentValues values = getContentValuesCommissionJobCardTable(context, nsc_today);
            String condition = CommissionJobCardTable.Cols.COMMISSION_ID + "='" + nsc_today.commission_id + "'";
            saveValues(context, CommissionJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesCommissionJobCardTable(Context context, TodayModel nsc_today) {
        ContentValues values = new ContentValues();
        try {
            values.put(CommissionJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(CommissionJobCardTable.Cols.JOB_ID, nsc_today.job_id != null ? nsc_today.job_id : "");
            values.put(CommissionJobCardTable.Cols.JOB_NAME, nsc_today.job_name != null ? nsc_today.job_name : "");
            values.put(CommissionJobCardTable.Cols.COMMISSION_ID, nsc_today.commission_id != null ? nsc_today.commission_id : "");
            values.put(CommissionJobCardTable.Cols.JOB_AREA, nsc_today.area != null ? nsc_today.area : "");
            values.put(CommissionJobCardTable.Cols.JOB_LOCATION, nsc_today.location != null ? nsc_today.location : "");
            values.put(CommissionJobCardTable.Cols.JOB_CATEGORY, nsc_today.category != null ? nsc_today.category : "");
            values.put(CommissionJobCardTable.Cols.JOB_SUBCATEGORY, nsc_today.subcategory != null ? nsc_today.subcategory : "");
            values.put(CommissionJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(CommissionJobCardTable.Cols.MAKE_NUMBER, nsc_today.assetmakeNo != null ? nsc_today.assetmakeNo : "");
            values.put(CommissionJobCardTable.Cols.ASSIGNED_DATE, nsc_today.assignedDate != null ? nsc_today.assignedDate : "");
            values.put(CommissionJobCardTable.Cols.JOB_TYPE, nsc_today.job_type != null ? nsc_today.job_type : "");
            values.put(CommissionJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
            values.put(CommissionJobCardTable.Cols.MATERIAL_RECEIVED, AppConstants.CARD_STATUS_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getCommissionJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + CommissionJobCardTable.TABLE_NAME + " where "
                + CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + CommissionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getJobCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getJobCardFromCursor(Cursor cursor) {
        TodayModel commissionTodayCards = new TodayModel();
        commissionTodayCards.job_id = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_ID)) : "";
        commissionTodayCards.commission_id = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.COMMISSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.COMMISSION_ID)) : "";
        commissionTodayCards.job_name = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_NAME)) : "";
        commissionTodayCards.area = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_AREA)) : "";
        commissionTodayCards.location = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_LOCATION)) : "";
        commissionTodayCards.category = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_CATEGORY)) : "";
        commissionTodayCards.subcategory = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_SUBCATEGORY)) : "";
        commissionTodayCards.assignedDate = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        commissionTodayCards.assetmakeNo = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MAKE_NUMBER)) : "";
        commissionTodayCards.acceptstatus = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ACCEPT_STATUS)) : "";
        commissionTodayCards.job_type = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_TYPE)) : "";
        commissionTodayCards.materialReceived = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MATERIAL_RECEIVED)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MATERIAL_RECEIVED)) : "";

        return commissionTodayCards;
    }

    //  get total count of NSCToday
    public static int getCommissionTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + CommissionJobCardTable.TABLE_NAME + " where "
                + CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + CommissionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //  get total count of Complaint
    public static int getComplaintTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //  get total count of NSCToday
    public static int getCommissionTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + CommissionJobCardTable.TABLE_NAME + " where "
                + CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + CommissionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + CommissionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //get count of Preventive

    public static int getPreventiveTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PreventiveJobCardTable.TABLE_NAME + " where "
                + PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + PreventiveJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + PreventiveJobCardTable.Cols.DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //get count of Breakdown

    public static int getBreakdownTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + BreakDownJobCardTable.TABLE_NAME + " where "
                + BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + BreakDownJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    //get count of Site Verification

    public static int getSiteVerificationTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + SiteVerificationJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //get count of Service
    public static int getServiceTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //  get total count of Complaint
    public static int getComplaintTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + ComplaintJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //  get total count of Service
    public static int getServiceTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + ServiceJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // Update NscTable

  /*  public static void updateCommissionCardStatus(String commissionid,String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Update " + CommissionJobCardTable.TABLE_NAME + " set " + CommissionJobCardTable.Cols.CARD_STATUS + "=' " + jobCardStatus +  "' where " + CommissionJobCardTable.Cols.COMMISSION_ID + "='" + commissionid + "'", null);
        cursor.close();
    }*/

    public static void updateCommissionCardStatus(String commissionid, String jobCardStatus, String status, String materialstatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(CommissionJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(CommissionJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(CommissionJobCardTable.Cols.MATERIAL_RECEIVED, materialstatus);
        String[] args = new String[]{commissionid};
        db.update(CommissionJobCardTable.TABLE_NAME, values, "commission_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Update Dsc Card
    public static void updateDSCCardStatus(String decommissionid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DecommissionJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(DecommissionJobCardTable.Cols.ACCEPT_STATUS, status);
        String[] args = new String[]{decommissionid};
        db.update(DecommissionJobCardTable.TABLE_NAME, values, "dsc_commission_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Update MeterInstall Card
    public static void updateMeterInstallCardStatus(String completedOn, String meterInstallId, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(MeterInstalltionJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(MeterInstalltionJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{meterInstallId};
        db.update(MeterInstalltionJobCardTable.TABLE_NAME, values, "meterinstall_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllMeterInstallCardStatus(String completedOn, String meterInstallId, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{meterInstallId};
        db.update(AllJobCardTable.TABLE_NAME, values, "meter_install_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Update preventive Card
    public static void updatePreventiveCardStatus(String preventiveid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PreventiveJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(PreventiveJobCardTable.Cols.ACCEPT_STATUS, status);
        String[] args = new String[]{preventiveid};
        db.update(PreventiveJobCardTable.TABLE_NAME, values, "preventive_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Update breakdown Card
    public static void updateBreakDownCardStatus(String breakdownid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BreakDownJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(BreakDownJobCardTable.Cols.ACCEPT_STATUS, status);
        String[] args = new String[]{breakdownid};
        db.update(BreakDownJobCardTable.TABLE_NAME, values, "breakdown_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    //Update SiteVerification Card
    public static void updateSiteVerificationCardStatus(String completedOn, String SiteVerificationid, String jobCardStatus, String status) {

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(SiteVerificationJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(SiteVerificationJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(SiteVerificationJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{SiteVerificationid};
        db.update(SiteVerificationJobCardTable.TABLE_NAME, values, "site_verification_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllSiteVerificationCardStatus(String completedOn, String SiteVerificationid, String jobCardStatus, String status) {

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{SiteVerificationid};
        db.update(AllJobCardTable.TABLE_NAME, values, "site_verification_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }


    //Update Service Card
    public static void updateServiceCardStatus(String completedOn, String Serviceid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ServiceJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(ServiceJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(ServiceJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{Serviceid};
        int srt = db.update(ServiceJobCardTable.TABLE_NAME, values, "service_id=?", args);
//        Log.i("uuuuuuuuuuuuu", "pppppppp   ::::"  + srt);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllServiceCardStatus(String completedOn, String Serviceid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{Serviceid};
        int srt = db.update(AllJobCardTable.TABLE_NAME, values, "service_id=?", args);
//        Log.i("uuuuuuuuuuuuu", "pppppppp   ::::"  + srt);
        if (db.isOpen()) {
            db.close();
        }
    }

    // Nsc Deassign
    public static void handleReassignDeassignNSC(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardNSC(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardNSC(Context context, String card_id, String user_id) {
        try {
            String condition = CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND " + CommissionJobCardTable.Cols.COMMISSION_ID + "='" + card_id + "'";
            context.getContentResolver().delete(CommissionJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //DSC Deassign
    public static void handleReassignDeassignDISC(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardDISC(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardDISC(Context context, String card_id, String user_id) {
        try {
            String condition = DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND " + DecommissionJobCardTable.Cols.DECOMMISSION_ID + "='" + card_id + "'";
            context.getContentResolver().delete(DecommissionJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Meterinstall Deassign
    public static void handleReassignDeassignMeterInstall(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardMeterInstall(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardMeterInstall(Context context, String card_id, String user_id) {
        try {
            String condition = MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                    + MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID + "='" + card_id + "'";
            context.getContentResolver().delete(MeterInstalltionJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Preventive Deassign
    public static void handleReassignDeassignPreventive(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardPreventive(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardPreventive(Context context, String card_id, String user_id) {
        try {
            String condition = PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND " + PreventiveJobCardTable.Cols.PREVENTIVE_ID + "='" + card_id + "'";
            context.getContentResolver().delete(PreventiveJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Breakdown Deassign
    public static void handleReassignDeassignBreakdown(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardBreakdown(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardBreakdown(Context context, String card_id, String user_id) {
        try {
            String condition = BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND " + BreakDownJobCardTable.Cols.BREAKDOWN_ID + "='" + card_id + "'";
            context.getContentResolver().delete(BreakDownJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // siteverification Deassign
    public static void handleReassignDeassignSiteVerification(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardSiteVerification(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardSiteVerification(Context context, String card_id, String user_id) {
        try {
            String condition = SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                    + SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID + "='" + card_id + "'";
            context.getContentResolver().delete(SiteVerificationJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Service Deassign
    public static void handleReassignDeassignService(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardService(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardService(Context context, String card_id, String user_id) {
        try {
            String condition = ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                    + ServiceJobCardTable.Cols.SERVICE_ID + "='" + card_id + "'";
            context.getContentResolver().delete(ServiceJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Complaint Deassign
    public static void handleReassignDeassignComplaint(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardComplaint(mContext, card_id, user_id);
        }
    }
    //Delete Complaint JobCard

    public static void deleteJobCardComplaint(Context context, String card_id, String user_id) {
        try {
            String condition = ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                    + ComplaintJobCardTable.Cols.COMPLAINT_ID + "='" + card_id + "'";
            context.getContentResolver().delete(ComplaintJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DSC Job card Insertion
    public static void saveDecommJobCards(Context mContext, ArrayList<TodayModel> nsc_today) {
        for (TodayModel todayModel : nsc_today)
            DatabaseManager.savDSCJobCardsInfo(mContext, todayModel);
    }

    private static void savDSCJobCardsInfo(Context context, TodayModel nsc_today) {
        if (nsc_today != null) {
            ContentValues values = getContentValuesDSCJobCardTable(context, nsc_today);
            String condition = DecommissionJobCardTable.Cols.DECOMMISSION_ID + "='" + nsc_today.decommission_id + "'";
            saveValues(context, DecommissionJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesDSCJobCardTable(Context context, TodayModel nsc_today) {
        ContentValues values = new ContentValues();
        try {
            values.put(DecommissionJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(DecommissionJobCardTable.Cols.JOB_ID, nsc_today.job_id != null ? nsc_today.job_id : "");
            values.put(DecommissionJobCardTable.Cols.JOB_NAME, nsc_today.job_name != null ? nsc_today.job_name : "");
            values.put(DecommissionJobCardTable.Cols.DECOMMISSION_ID, nsc_today.decommission_id != null ? nsc_today.decommission_id : "");
            values.put(DecommissionJobCardTable.Cols.JOB_AREA, nsc_today.area != null ? nsc_today.area : "");
            values.put(DecommissionJobCardTable.Cols.JOB_LOCATION, nsc_today.location != null ? nsc_today.location : "");
            values.put(DecommissionJobCardTable.Cols.JOB_CATEGORY, nsc_today.category != null ? nsc_today.category : "");
            values.put(DecommissionJobCardTable.Cols.JOB_SUBCATEGORY, nsc_today.subcategory != null ? nsc_today.subcategory : "");
            values.put(DecommissionJobCardTable.Cols.MAKE_NUMBER, nsc_today.assetmakeNo != null ? nsc_today.assetmakeNo : "");
            values.put(DecommissionJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(DecommissionJobCardTable.Cols.ASSIGNED_DATE, nsc_today.assignedDate != null ? nsc_today.assignedDate : "");
            values.put(DecommissionJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getDISCJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DecommissionJobCardTable.TABLE_NAME + " where "
                + DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + DecommissionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getDSCJobCardListFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getDSCJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getDSCJobCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getDSCJobCardFromCursor(Cursor cursor) {
        TodayModel dscTodayCards = new TodayModel();
        dscTodayCards.job_id = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_ID)) : "";
        dscTodayCards.decommission_id = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.DECOMMISSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.DECOMMISSION_ID)) : "";
        dscTodayCards.job_name = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_NAME)) : "";
        dscTodayCards.area = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_AREA)) : "";
        dscTodayCards.location = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_LOCATION)) : "";
        dscTodayCards.category = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_CATEGORY)) : "";
        dscTodayCards.subcategory = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_SUBCATEGORY)) : "";
        dscTodayCards.assignedDate = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        dscTodayCards.assetmakeNo = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.MAKE_NUMBER)) : "";
        dscTodayCards.acceptstatus = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ACCEPT_STATUS)) : "";

        return dscTodayCards;
    }

    //  get total count of DISCToday
    public static int getDISCTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DecommissionJobCardTable.TABLE_NAME + " where " + DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + DecommissionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static int getDISCTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DecommissionJobCardTable.TABLE_NAME + " where "
                + DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + DecommissionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + DecommissionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    // Preventive Card Insertion

    public static void savePreventiveJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels)
            DatabaseManager.savePreventiveJobCardsInfo(mContext, todayModel);
    }

    private static void savePreventiveJobCardsInfo(Context context, TodayModel preventive_today) {
        if (preventive_today != null) {
            ContentValues values = getContentValuesPreventiveJobCardTable(context, preventive_today);
            String condition = PreventiveJobCardTable.Cols.PREVENTIVE_ID + "='" + preventive_today.preventive_id + "'";
            saveValues(context, PreventiveJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesPreventiveJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();

        try {
            // for(int i = 0; i < todayModels.size(); i++) {
            values.put(PreventiveJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(PreventiveJobCardTable.Cols.JOB_ID, todayModels.job_id != null ? todayModels.job_id : "");
            values.put(PreventiveJobCardTable.Cols.JOB_NAME, todayModels.job_name != null ? todayModels.job_name : "");
            values.put(PreventiveJobCardTable.Cols.PREVENTIVE_ID, todayModels.preventive_id != null ? todayModels.preventive_id : "");
            values.put(PreventiveJobCardTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(PreventiveJobCardTable.Cols.JOB_LOCATION, todayModels.location != null ? todayModels.location : "");
            values.put(PreventiveJobCardTable.Cols.JOB_CATEGORY, todayModels.category != null ? todayModels.category : "");
            values.put(PreventiveJobCardTable.Cols.JOB_SUBCATEGORY, todayModels.subcategory != null ? todayModels.subcategory : "");
            values.put(PreventiveJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(PreventiveJobCardTable.Cols.DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(PreventiveJobCardTable.Cols.MAKE_NUMBER, todayModels.assetmakeNo != null ? todayModels.assetmakeNo : "");
            values.put(PreventiveJobCardTable.Cols.ACCEPT_STATUS, "");
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getPreventiveJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PreventiveJobCardTable.TABLE_NAME + " where "
                + PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + PreventiveJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getPreventiveJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getPreventiveJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel preventive_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                preventive_today_cards = getPreventiveJobCardFromCursor(cursor);
                jobCards.add(preventive_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getPreventiveJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();
        todayModel.job_id = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_ID)) : "";
        todayModel.preventive_id = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.PREVENTIVE_ID)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.PREVENTIVE_ID)) : "";
        todayModel.job_name = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_NAME)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_AREA)) : "";
        todayModel.location = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_LOCATION)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_SUBCATEGORY)) : "";
        todayModel.assetmakeNo = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.MAKE_NUMBER)) : "";
        todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ACCEPT_STATUS)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.DATE)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.DATE)) : "";

        return todayModel;
    }


    //  get total count of Preventive  Today
    public static int getPreventiveTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PreventiveJobCardTable.TABLE_NAME + " where "
                + PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + PreventiveJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

//BreakDown Insertion

    public static void saveBreakdownJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels) {
            DatabaseManager.saveBreakdownJobCardsInfo(mContext, todayModel);
        }
    }

    private static void saveBreakdownJobCardsInfo(Context context, TodayModel breakdown_today) {
        if (breakdown_today != null) {
            ContentValues values = getContentValuesBreakdownJobCardTable(context, breakdown_today);

            String condition = BreakDownJobCardTable.Cols.BREAKDOWN_ID + "='" + breakdown_today.breakdown_id + "'";
            saveValues(context, BreakDownJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesBreakdownJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();

        try {

            values.put(BreakDownJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(BreakDownJobCardTable.Cols.JOB_ID, todayModels.job_id != null ? todayModels.job_id : "");
            values.put(BreakDownJobCardTable.Cols.JOB_NAME, todayModels.job_name != null ? todayModels.job_name : "");
            values.put(BreakDownJobCardTable.Cols.BREAKDOWN_ID, todayModels.breakdown_id != null ? todayModels.breakdown_id : "");
            values.put(BreakDownJobCardTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(BreakDownJobCardTable.Cols.JOB_LOCATION, todayModels.location != null ? todayModels.location : "");
            values.put(BreakDownJobCardTable.Cols.JOB_CATEGORY, todayModels.category != null ? todayModels.category : "");
            values.put(BreakDownJobCardTable.Cols.JOB_SUBCATEGORY, todayModels.subcategory != null ? todayModels.subcategory : "");
            values.put(BreakDownJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(BreakDownJobCardTable.Cols.DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(BreakDownJobCardTable.Cols.MAKE_NUMBER, todayModels.assetmakeNo != null ? todayModels.assetmakeNo : "");
            values.put(BreakDownJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getBreakdownJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + BreakDownJobCardTable.TABLE_NAME + " where "
                + BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + BreakDownJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getBreakdownJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getBreakdownJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel breakdown_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                breakdown_today_cards = getBreakdownJobCardFromCursor(cursor);
                jobCards.add(breakdown_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getBreakdownJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();
        todayModel.job_id = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_ID)) : "";
        todayModel.breakdown_id = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.BREAKDOWN_ID)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.BREAKDOWN_ID)) : "";
        todayModel.job_name = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_NAME)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_AREA)) : "";
        todayModel.location = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_LOCATION)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_SUBCATEGORY)) : "";
        todayModel.assetmakeNo = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.MAKE_NUMBER)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.DATE)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.DATE)) : "";
        todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ACCEPT_STATUS)) : "";

        return todayModel;
    }


    //  get total count of Breakdown  Today
    public static int getBreakDownTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + BreakDownJobCardTable.TABLE_NAME + " where "
                + BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + BreakDownJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //SiteVerification Insertion

    public static void saveSiteVerificationJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels) {
            DatabaseManager.saveSiteVerificationJobCardsInfo(mContext, todayModel);
        }

    }

    private static void saveSiteVerificationJobCardsInfo(Context context, TodayModel breakdown_today) {
        if (breakdown_today != null) {
            ContentValues values = getContentValuesSiteVerificationJobCardTable(context, breakdown_today);

            String condition = SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID + "='" + breakdown_today.site_verification_id + "'";
            saveValues(context, SiteVerificationJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesSiteVerificationJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();

        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                date = baseFormat.parse(todayModels.dueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }

            values.put(SiteVerificationJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(SiteVerificationJobCardTable.Cols.SUB_CATEGORY, todayModels.subcategory != null ? todayModels.subcategory : "");
            values.put(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID, todayModels.site_verification_id != null ? todayModels.site_verification_id : "");
            values.put(SiteVerificationJobCardTable.Cols.CATEGORY, todayModels.category != null ? todayModels.category : "");
            values.put(SiteVerificationJobCardTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(SiteVerificationJobCardTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(SiteVerificationJobCardTable.Cols.ASSIGNED_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(SiteVerificationJobCardTable.Cols.ADDRESS, todayModels.address != null ? todayModels.address : "");
            values.put(SiteVerificationJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(SiteVerificationJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
            values.put(SiteVerificationJobCardTable.Cols.TYPE_OF_PERIMSE, todayModels.type_of_premises != null ? todayModels.type_of_premises : "");
            values.put(SiteVerificationJobCardTable.Cols.SERVICE_REQUESTED, todayModels.service_requested != null ? todayModels.service_requested : "");
            values.put(SiteVerificationJobCardTable.Cols.REQUEST_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(SiteVerificationJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");


            values.put(SiteVerificationJobCardTable.Cols.REJECTION_REMARK, todayModels.siteRejectRemark != null ? todayModels.siteRejectRemark : "");

            values.put(SiteVerificationJobCardTable.Cols.LATITUDE, todayModels.getLatitude() != null ? todayModels.getLatitude() : "");
            values.put(SiteVerificationJobCardTable.Cols.LONGITUDE, todayModels.getLongitude() != null ? todayModels.getLongitude() : "");


            values.put(SiteVerificationJobCardTable.Cols.SCREEN, todayModels.getScreen() != null ? todayModels.getScreen() : "");
            values.put(SiteVerificationJobCardTable.Cols.AREA, todayModels.getArea() != null ? todayModels.getArea() : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getSiteVerificationJobCard(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + SiteVerificationJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getSiteVerificationJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getSiteVerificationJobCardToday(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + SiteVerificationJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getSiteVerificationJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getSiteVerificationJobCardWeek(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                        + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + SiteVerificationJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + SiteVerificationJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + SiteVerificationJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getSiteVerificationJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getSiteVerificationJobCardMonth(String userId, String jobCardStatus) {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                        + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "=? AND "
                        + SiteVerificationJobCardTable.Cols.CARD_STATUS + "=? AND ("
                        + SiteVerificationJobCardTable.Cols.DUE_DATE + " BETWEEN ? And ?) ORDER BY "
                        + SiteVerificationJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, jobCardStatus, beginDate, endDate}, null);

        ArrayList<TodayModel> jobCards = getSiteVerificationJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }


    private static ArrayList<TodayModel> getSiteVerificationJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel breakdown_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                breakdown_today_cards = getSiteVerificationJobCardFromCursor(cursor);
                jobCards.add(breakdown_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getSiteVerificationJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.DUE_DATE)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        todayModel.address = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ADDRESS)) : "";
        todayModel.site_verification_id = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SUB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SUB_CATEGORY)) : "";
        todayModel.consumerName = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) : "";
        todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.MOBILE_NO)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ASSIGNED_DATE)) : "";
        todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ACCEPT_STATUS)) : "";
        todayModel.type_of_premises = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.TYPE_OF_PERIMSE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.TYPE_OF_PERIMSE)) : "";
        todayModel.service_requested = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SERVICE_REQUESTED)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SERVICE_REQUESTED)) : "";
        todayModel.requestId = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) : "";
        todayModel.dueDate = formattedDate != null ? formattedDate : "";
        todayModel.siteRejectRemark = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REJECTION_REMARK)) : "";

        todayModel.latitude = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LATITUDE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LATITUDE)) : "";
        todayModel.longitude = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LONGITUDE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LONGITUDE)) : "";


        todayModel.screen = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) : "";


        return todayModel;
    }


    //Service Insertion


    // To get Latitude, longitude from site verification for location

    public static ArrayList<TodayModel> getSiteVerificationLatLong(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select "
                + SiteVerificationJobCardTable.Cols.LATITUDE + "," + SiteVerificationJobCardTable.Cols.LONGITUDE + ","
                + SiteVerificationJobCardTable.Cols.REQUEST_ID + "," + SiteVerificationJobCardTable.Cols.CONSUMER_NAME + ","
                + SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID
                + " from " + SiteVerificationJobCardTable.TABLE_NAME
                + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getSiteVerificationLatLongListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getSiteVerificationLatLongToday(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "
                + SiteVerificationJobCardTable.Cols.LATITUDE + "," + SiteVerificationJobCardTable.Cols.LONGITUDE + ","
                + SiteVerificationJobCardTable.Cols.REQUEST_ID + "," + SiteVerificationJobCardTable.Cols.CONSUMER_NAME + ","
                + SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID
                + " from " + SiteVerificationJobCardTable.TABLE_NAME
                + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getSiteVerificationLatLongListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getSiteVerificationLatLongWeek(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "
                        + SiteVerificationJobCardTable.Cols.LATITUDE + "," + SiteVerificationJobCardTable.Cols.LONGITUDE + ","
                        + SiteVerificationJobCardTable.Cols.REQUEST_ID + "," + SiteVerificationJobCardTable.Cols.CONSUMER_NAME + ","
                        + SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID
                        + " from " + SiteVerificationJobCardTable.TABLE_NAME
                        + " where "
                        + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + SiteVerificationJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + SiteVerificationJobCardTable.Cols.CARD_STATUS + "=?",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getSiteVerificationLatLongListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getSiteVerificationLatLongMonth(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "
                        + SiteVerificationJobCardTable.Cols.LATITUDE + "," + SiteVerificationJobCardTable.Cols.LONGITUDE + ","
                        + SiteVerificationJobCardTable.Cols.REQUEST_ID + "," + SiteVerificationJobCardTable.Cols.CONSUMER_NAME + ","
                        + SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID
                        + " from " + SiteVerificationJobCardTable.TABLE_NAME
                        + " where "
                        + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + SiteVerificationJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + SiteVerificationJobCardTable.Cols.CARD_STATUS + "=?",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getSiteVerificationLatLongListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getSiteVerificationLatLongListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel breakdown_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                breakdown_today_cards = getSiteVerificationLatLongFromCursor(cursor);
                jobCards.add(breakdown_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getSiteVerificationLatLongFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();

        todayModel.latitude = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LATITUDE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LATITUDE)) : "";
        todayModel.longitude = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LONGITUDE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LONGITUDE)) : "";
        todayModel.consumerName = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) : "";
        todayModel.requestId = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) : "";
        todayModel.site_verification_id = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID)) : "";

        return todayModel;
    }

    public static void saveServiceJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels) {
            DatabaseManager.saveServiceJobCardsInfo(mContext, todayModel);
        }

    }

    private static void saveServiceJobCardsInfo(Context context, TodayModel breakdown_today) {
        if (breakdown_today != null) {
            ContentValues values = getContentValuesServiceJobCardTable(context, breakdown_today);

            String condition = ServiceJobCardTable.Cols.SERVICE_ID + "='" + breakdown_today.service_id + "'";
            saveValues(context, ServiceJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesServiceJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();

        SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = baseFormat.parse(todayModels.dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }

        try {

            values.put(ServiceJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(ServiceJobCardTable.Cols.SERVICE_ID, todayModels.service_id != null ? todayModels.service_id : "");
            values.put(ServiceJobCardTable.Cols.SERVICE_TYPE, todayModels.serviceType != null ? todayModels.serviceType : "");
            values.put(ServiceJobCardTable.Cols.ASSIGNED_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(ServiceJobCardTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(ServiceJobCardTable.Cols.CONSUMER_NO, todayModels.consumerNo != null ? todayModels.consumerNo : "");
            values.put(ServiceJobCardTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(ServiceJobCardTable.Cols.ADDRESS, todayModels.address != null ? todayModels.address : "");
            values.put(ServiceJobCardTable.Cols.SERVICE_NO, todayModels.serviceNumber != null ? todayModels.serviceNumber : "");
            values.put(ServiceJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(ServiceJobCardTable.Cols.REQUEST_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(ServiceJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
            values.put(ServiceJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");


            values.put(ServiceJobCardTable.Cols.SCREEN, todayModels.screen != null ? todayModels.screen : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }


    //  get total count of Breakdown  Today
    public static int getSiteVerificationTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


// Get NSC HISTORY CARDS

    public static ArrayList<HistoryModel> getNSCHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + CommissionJobCardTable.TABLE_NAME + " where "
                + CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + CommissionJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + CommissionJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel nsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_history_cards = getHistoryJobCardListCursor(cursor);
                jobCards.add(nsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return historyModel;
    }

// Search NSC Cards

    public static ArrayList<TodayModel> getNSCSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + CommissionJobCardTable.TABLE_NAME + " where " +
                CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and " +
                CommissionJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( " +
                CommissionJobCardTable.Cols.JOB_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todaymodel = new TodayModel();
                todaymodel.job_name = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_NAME)) : "";
                todaymodel.category = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_CATEGORY)) : "";
                todaymodel.area = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_AREA)) : "";
                todaymodel.location = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_LOCATION)) : "";
                todaymodel.commission_id = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.COMMISSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.COMMISSION_ID)) : "";
                todaymodel.assetmakeNo = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MAKE_NUMBER)) : "";
                todaymodel.acceptstatus = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.ACCEPT_STATUS)) : "";
                todaymodel.materialReceived = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MATERIAL_RECEIVED)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.MATERIAL_RECEIVED)) : "";
                todaymodel.job_type = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_TYPE)) : "";
                todaymodel.job_id = cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(CommissionJobCardTable.Cols.JOB_ID)) : "";

                searchlist.add(todaymodel);
            }
        }
        db.close();
        return searchlist;
    }


// Search DSC Cards

    public static ArrayList<TodayModel> getDISCSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DecommissionJobCardTable.TABLE_NAME + " where " +
                DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and " +
                DecommissionJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( " +
                DecommissionJobCardTable.Cols.JOB_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todaymodel = new TodayModel();
                todaymodel.job_name = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_NAME)) : "";
                todaymodel.category = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_CATEGORY)) : "";
                todaymodel.area = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_AREA)) : "";
                todaymodel.location = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_LOCATION)) : "";
                todaymodel.decommission_id = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.DECOMMISSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.DECOMMISSION_ID)) : "";
                todaymodel.assetmakeNo = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.MAKE_NUMBER)) : "";
                todaymodel.acceptstatus = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.MAKE_NUMBER)) : "";
                todaymodel.job_id = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.JOB_ID)) : "";
                searchlist.add(todaymodel);
            }
        }
        db.close();
        return searchlist;
    }

    // Search Preventive Cards

    public static ArrayList<TodayModel> getPreventiveSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PreventiveJobCardTable.TABLE_NAME + " where " +
                PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and " +
                PreventiveJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( " +
                PreventiveJobCardTable.Cols.JOB_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todaymodel = new TodayModel();
                todaymodel.job_name = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_NAME)) : "";
                todaymodel.category = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_CATEGORY)) : "";
                todaymodel.area = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_AREA)) : "";
                todaymodel.location = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_LOCATION)) : "";
                todaymodel.preventive_id = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.PREVENTIVE_ID)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.PREVENTIVE_ID)) : "";
                todaymodel.assetmakeNo = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.MAKE_NUMBER)) : "";
                todaymodel.acceptstatus = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.ACCEPT_STATUS)) : "";
                todaymodel.job_id = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.JOB_ID)) : "";
                searchlist.add(todaymodel);
            }
        }
        db.close();
        return searchlist;
    }

    // Search Breakdown Cards

    public static ArrayList<TodayModel> getBreakdownSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + BreakDownJobCardTable.TABLE_NAME + " where " +
                BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and " +
                BreakDownJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( " +
                BreakDownJobCardTable.Cols.JOB_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todaymodel = new TodayModel();
                todaymodel.job_name = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_NAME)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_NAME)) : "";
                todaymodel.category = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_CATEGORY)) : "";
                todaymodel.area = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_AREA)) : "";
                todaymodel.location = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_LOCATION)) : "";
                todaymodel.breakdown_id = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.BREAKDOWN_ID)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.BREAKDOWN_ID)) : "";
                todaymodel.assetmakeNo = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.MAKE_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.MAKE_NUMBER)) : "";
                todaymodel.acceptstatus = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.ACCEPT_STATUS)) : "";
                todaymodel.job_id = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.JOB_ID)) : "";
                searchlist.add(todaymodel);
            }
        }
        db.close();
        return searchlist;
    }


    // Get DSC HISTORY CARDS

    public static ArrayList<HistoryModel> getDSCHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DecommissionJobCardTable.TABLE_NAME + " where "
                + DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + DecommissionJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + DecommissionJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getDSCHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getDSCHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getDSCHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getDSCHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(DecommissionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return historyModel;
    }

    // Get METERInstall HISTORY CARDS

    public static ArrayList<HistoryModel> getMeterInstallHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        /*Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + MeterInstalltionJobCardTable.Cols.ID + " DESC", null);*/
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED + "' ORDER BY "
                + MeterInstalltionJobCardTable.Cols.COMPLETED_ON + " DESC", null);
        ArrayList<HistoryModel> historyCards = getMeterInstallHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getMeterInstallHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getMeterInstallHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getMeterInstallHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) : "";
        historyModel.screen = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) != null
                ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) : "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) != null
                ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CARD_STATUS)) : "";
        return historyModel;
    }


    // Search Site verification Cards

    public static ArrayList<TodayModel> getSiteVerificationSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where " +
                SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and " +
                SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( " +
                SiteVerificationJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todayModel = new TodayModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.DUE_DATE)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.DUE_DATE)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                todayModel.address = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ADDRESS)) : "";
                todayModel.site_verification_id = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SITE_VERIFICATION_ID)) : "";
                todayModel.category = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CATEGORY)) : "";
                todayModel.subcategory = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SUB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SUB_CATEGORY)) : "";
                todayModel.consumerName = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) : "";
                todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.MOBILE_NO)) : "";
                todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ASSIGNED_DATE)) : "";
                todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.ACCEPT_STATUS)) : "";
                todayModel.type_of_premises = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.TYPE_OF_PERIMSE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.TYPE_OF_PERIMSE)) : "";
                todayModel.requestId = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) : "";
                todayModel.service_requested = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SERVICE_REQUESTED)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SERVICE_REQUESTED)) : "";
                todayModel.dueDate = formattedDate != null ? formattedDate : "";


                todayModel.siteRejectRemark = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REJECTION_REMARK)) : "";

                todayModel.latitude = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LATITUDE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LATITUDE)) : "";
                todayModel.longitude = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LONGITUDE)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.LONGITUDE)) : "";


                todayModel.screen = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) : "";
                todayModel.area = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) : "";

                searchlist.add(todayModel);
            }
        }
        db.close();
        return searchlist;
    }

    public static ArrayList<HistoryModel> getSiteVerificationHistorySearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where " +
                SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and " +
                SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( " +
                SiteVerificationJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<HistoryModel> searchlist = new ArrayList<HistoryModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) != null
                        ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) != null
                        ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) : "";

                searchlist.add(historyModel);
            }
        }
        db.close();
        return searchlist;
    }


    // Get Preventive HISTORY CARDS

    public static ArrayList<HistoryModel> getPreventiveHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PreventiveJobCardTable.TABLE_NAME + " where "
                + PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + PreventiveJobCardTable.Cols.DATE + " ORDER BY "
                + PreventiveJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getPreventiveHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getPreventiveHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getPreventiveHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getPreventiveHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.DATE)) != null ? cursor.getString(cursor.getColumnIndex(PreventiveJobCardTable.Cols.DATE)) : "";
        return historyModel;
    }
    // Get Breakdown HISTORY CARDS

    // Get Asset Indexing HISTORY CARDS

    public static ArrayList<HistoryModel> getAssetIndexingHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AssetJobCardTable.TABLE_NAME + " where "
                + AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE + " ORDER BY "
                + AssetJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getAssetIndexingJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getAssetIndexingJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getAssetIndexingJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getAssetIndexingJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE)) : "";
        return historyModel;
    }

    // Get Breakdown HISTORY CARDS

    public static ArrayList<HistoryModel> getBreakdownHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + BreakDownJobCardTable.TABLE_NAME + " where "
                + BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + BreakDownJobCardTable.Cols.DATE + " ORDER BY "
                + BreakDownJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getBreakdownHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getBreakdownHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getBreakdownHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getBreakdownHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.DATE)) != null ? cursor.getString(cursor.getColumnIndex(BreakDownJobCardTable.Cols.DATE)) : "";
        return historyModel;
    }


    // Get SiteVerfication HISTORY CARDS

    public static ArrayList<HistoryModel> getSiteVerificationHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        /*Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + SiteVerificationJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + SiteVerificationJobCardTable.Cols.ID + " DESC", null);*/
        Cursor cursor = db.rawQuery("Select * from " + SiteVerificationJobCardTable.TABLE_NAME + " where "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED + "' ORDER BY "
                + SiteVerificationJobCardTable.Cols.COMPLETED_ON + " DESC", null);
        ArrayList<HistoryModel> historyCards = getSiteVerificationHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getSiteVerificationHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getSiteVerficationHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getSiteVerficationHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CONSUMER_NAME)) : "";
        historyModel.screen = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) != null
                ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.SCREEN)) : "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) != null
                ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.REQUEST_ID)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) != null
                ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.AREA)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(SiteVerificationJobCardTable.Cols.CARD_STATUS)) : "";
        return historyModel;
    }

    // Get Conversion HISTORY CARDS

    public static ArrayList<HistoryModel> getConvertHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + ConversionJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + ConversionJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getConvertHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getConvertHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getConvertHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getConvertHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        historyModel.date = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        return historyModel;
    }


    public static void deleteDataNSC(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + CommissionJobCardTable.TABLE_NAME + " WHERE "
                + CommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + CommissionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + CommissionJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataDSC(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + DecommissionJobCardTable.TABLE_NAME + " WHERE "
                + DecommissionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + DecommissionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + DecommissionJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataMeterInstall(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + MeterInstalltionJobCardTable.TABLE_NAME + " WHERE "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataSiteVerification(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + SiteVerificationJobCardTable.TABLE_NAME + " WHERE "
                + SiteVerificationJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + SiteVerificationJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + SiteVerificationJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataServices(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + ServiceJobCardTable.TABLE_NAME + " WHERE "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + ServiceJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataComplaints(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + ComplaintJobCardTable.TABLE_NAME + " WHERE "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + ComplaintJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataPreventive(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + PreventiveJobCardTable.TABLE_NAME + " WHERE "
                + PreventiveJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + PreventiveJobCardTable.Cols.DATE + "='" + date + "'  AND "
                + PreventiveJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataBreakdown(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + BreakDownJobCardTable.TABLE_NAME + " WHERE "
                + BreakDownJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + BreakDownJobCardTable.Cols.DATE + "='" + date + "'  AND "
                + BreakDownJobCardTable.Cols.CARD_STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataAll(Context context, String user_id, String jobcardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + AllJobCardTable.TABLE_NAME + " WHERE "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + AllJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + AllJobCardTable.Cols.STATUS + "='" + jobcardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }


    // All Table Job Card Insertion
    public static void saveAllJobCards(Context mContext, ArrayList<TodayModel> all_today) {
        for (TodayModel todayModel : all_today)
            DatabaseManager.saveAllJobCardsInfo(mContext, todayModel);
    }

    private static void saveAllJobCardsInfo(Context context, TodayModel all_today) {

        if (all_today != null) {

            if (all_today.screen.equals(CommonUtility.getString(context, R.string.enquiry))) {
                ContentValues values = getContentValuesEnquiryAllJobCardTable(context, all_today);
                String condition = AllJobCardTable.Cols.ENQUIRY_ID + "='"
                        + all_today.requestId + "'";
                saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
            } else if (all_today.screen.equals(CommonUtility.getString(context, R.string.site_verification))) {
                ContentValues values = getContentValuesEnquiryAllJobCardTable(context, all_today);
                String condition = AllJobCardTable.Cols.SITE_VERIFICATION_ID + "='" + all_today.site_verification_id + "'";
                saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
            } else if (all_today.screen.equals(CommonUtility.getString(context, R.string.installation))) {
                ContentValues values = getContentValuesEnquiryAllJobCardTable(context, all_today);
                String condition = AllJobCardTable.Cols.METER_INSTALL_ID + "='" + all_today.meterInstallationId + "'";
                saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
            } else if (all_today.screen.equals(CommonUtility.getString(context, R.string.convert))) {
                ContentValues values = getContentValuesEnquiryAllJobCardTable(context, all_today);
                String condition = AllJobCardTable.Cols.CONVERSION_ID + "='" + all_today.job_id + "'";
                saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
            } else if (all_today.screen.equals(CommonUtility.getString(context, R.string.services))) {
                ContentValues values = getContentValuesEnquiryAllJobCardTable(context, all_today);
                String condition = AllJobCardTable.Cols.SERVICE_ID + "='" + all_today.service_id + "'";
                saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
            } else if (all_today.screen.equals(CommonUtility.getString(context, R.string.complaint))) {
                ContentValues values = getContentValuesEnquiryAllJobCardTable(context, all_today);
                String condition = AllJobCardTable.Cols.COMPLAINT_ID + "='" + all_today.complaintId + "'";
                saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
            }
        }
    }

    private static ContentValues getContentValuesEnquiryAllJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();

        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                if (todayModels.dueDate != null) {
                    date = baseFormat.parse(todayModels.dueDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }

            /* Enquiry */
            values.put(AllJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(AllJobCardTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(AllJobCardTable.Cols.ASSIGNED_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(AllJobCardTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(AllJobCardTable.Cols.STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(AllJobCardTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(AllJobCardTable.Cols.ENQUIRY_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(AllJobCardTable.Cols.ENQUIRY_NO, todayModels.enquiryNumber != null ? todayModels.enquiryNumber : "");
            values.put(AllJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");
            values.put(AllJobCardTable.Cols.SCREEN, todayModels.screen != null ? todayModels.screen : "");
            values.put(AllJobCardTable.Cols.STATE_ID, todayModels.stateId != null ? todayModels.stateId : "");
            values.put(AllJobCardTable.Cols.CITY_ID, todayModels.cityId != null ? todayModels.cityId : "");
            values.put(AllJobCardTable.Cols.STATE, todayModels.state != null ? todayModels.state : "");
            values.put(AllJobCardTable.Cols.CITY, todayModels.city != null ? todayModels.city : "");
            /* Site Verification */
            values.put(AllJobCardTable.Cols.SUB_CATEGORY, todayModels.subcategory != null ? todayModels.subcategory : "");
            values.put(AllJobCardTable.Cols.SITE_VERIFICATION_ID, todayModels.site_verification_id != null ? todayModels.site_verification_id : "");
            values.put(AllJobCardTable.Cols.CATEGORY, todayModels.category != null ? todayModels.category : "");
            values.put(AllJobCardTable.Cols.ADDRESS, todayModels.address != null ? todayModels.address : "");
            values.put(AllJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
            values.put(AllJobCardTable.Cols.TYPE_OF_PREMISE, todayModels.type_of_premises != null ? todayModels.type_of_premises : "");
            values.put(AllJobCardTable.Cols.SERVICE_REQUESTED, todayModels.service_requested != null ? todayModels.service_requested : "");
            values.put(AllJobCardTable.Cols.REQUEST_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(AllJobCardTable.Cols.LAT, todayModels.getLatitude() != null ? todayModels.getLatitude() : "");
            values.put(AllJobCardTable.Cols.LONG, todayModels.getLongitude() != null ? todayModels.getLongitude() : "");
            /* Installation */
            values.put(AllJobCardTable.Cols.METER_ID, todayModels.meterId != null ? todayModels.meterId : "");
            values.put(AllJobCardTable.Cols.METER_INSTALL_ID, todayModels.meterInstallationId != null ? todayModels.meterInstallationId : "");

            values.put(AllJobCardTable.Cols.CONSUMER_NO, todayModels.consumerNo != null ? todayModels.consumerNo : "");
            values.put(AllJobCardTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(AllJobCardTable.Cols.METER_CATEGORY, todayModels.category != null ? todayModels.category : "");
            values.put(AllJobCardTable.Cols.METER_SUBCATEGORY, todayModels.subcategory != null ? todayModels.subcategory : "");

            /* Conversion */
            values.put(AllJobCardTable.Cols.CONVERSION_ID, todayModels.conversionId != null ? todayModels.conversionId : "");
            values.put(AllJobCardTable.Cols.JOB_ID, todayModels.job_id != null ? todayModels.job_id : "");
            values.put(AllJobCardTable.Cols.REGULATOR_NO, todayModels.regulatorNo != null ? todayModels.regulatorNo : "");
            values.put(AllJobCardTable.Cols.METER_NO, todayModels.meterNo != null ? todayModels.meterNo : "");
            values.put(AllJobCardTable.Cols.INSTALLED_ON, todayModels.installedOn != null ? todayModels.installedOn : "");
            values.put(AllJobCardTable.Cols.RFC_VERIFIED_ON, todayModels.rfcVerifiedOn != null ? todayModels.rfcVerifiedOn : "");

            /* Services */
            values.put(AllJobCardTable.Cols.SERVICE_ID, todayModels.service_id != null ? todayModels.service_id : "");
            values.put(AllJobCardTable.Cols.SERVICE_TYPE, todayModels.serviceType != null ? todayModels.serviceType : "");
            values.put(AllJobCardTable.Cols.ADDRESS, todayModels.address != null ? todayModels.address : "");
            values.put(AllJobCardTable.Cols.SERVICE_NO, todayModels.serviceNumber != null ? todayModels.serviceNumber : "");

            /* Complaints */
            values.put(AllJobCardTable.Cols.COMPLAINT_ID, todayModels.complaintId != null ? todayModels.complaintId : "");
            values.put(AllJobCardTable.Cols.COMPLAINT_TYPE, todayModels.complaintType != null ? todayModels.complaintType : "");
            values.put(AllJobCardTable.Cols.COMPLAINT_REQUEST_ID, todayModels.complaintNumber != null ? todayModels.complaintNumber : "");

            if (todayModels.screen.equals(CommonUtility.getString(context, R.string.convert))) {
                values.put(AllJobCardTable.Cols.REJECTION_REMARK, todayModels.siteRejectRemark != null ? todayModels.siteRejectRemark : "");
            } else if (todayModels.screen.equals(CommonUtility.getString(context, R.string.installation))) {
                values.put(AllJobCardTable.Cols.REJECTION_REMARK, todayModels.installRejectRemark != null ? todayModels.installRejectRemark : "");
            } else {
                values.put(AllJobCardTable.Cols.REJECTION_REMARK, todayModels.siteRejectRemark != null ? todayModels.siteRejectRemark : "");
            }


            if (todayModels.screen.equals(CommonUtility.getString(context, R.string.convert))){
                values.put(AllJobCardTable.Cols.AREA, todayModels.areaName != null ? todayModels.areaName : "");

            } else {
                values.put(AllJobCardTable.Cols.AREA, todayModels.area != null ? todayModels.area : "");

            }

            values.put(AllJobCardTable.Cols.ADDRESS, todayModels.address != null ? todayModels.address : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }


    // MeterInstall Job card Insertion
    public static void saveMeterInstallJobCards(Context mContext, ArrayList<TodayModel> meterinstall_today) {
        for (TodayModel todayModel : meterinstall_today)
            DatabaseManager.savMeterInstallJobCardsInfo(mContext, todayModel);
    }

    private static void savMeterInstallJobCardsInfo(Context context, TodayModel meterinstall_today) {
        if (meterinstall_today != null) {
            ContentValues values = getContentValuesMeterInstallJobCardTable(context, meterinstall_today);
            String condition = MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID + "='" + meterinstall_today.meterInstallationId + "'";
            saveValues(context, MeterInstalltionJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesMeterInstallJobCardTable(Context context, TodayModel todayModel) {
        ContentValues values = new ContentValues();
        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                if (todayModel.dueDate != null) {
                    date = baseFormat.parse(todayModel.dueDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }
            values.put(MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING));
            values.put(MeterInstalltionJobCardTable.Cols.METER_ID, todayModel.meterId != null ? todayModel.meterId : "");
            values.put(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME, todayModel.consumerName != null ? todayModel.consumerName : "");
            values.put(MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID, todayModel.meterInstallationId != null ? todayModel.meterInstallationId : "");
            values.put(MeterInstalltionJobCardTable.Cols.AREA, todayModel.address != null ? todayModel.address : "");
            values.put(MeterInstalltionJobCardTable.Cols.CONSUMER_NO, todayModel.consumerNo != null ? todayModel.consumerNo : "");
            values.put(MeterInstalltionJobCardTable.Cols.MOBILE_NO, todayModel.mobileNumber != null ? todayModel.mobileNumber : "");
            values.put(MeterInstalltionJobCardTable.Cols.METER_CATEGORY, todayModel.category != null ? todayModel.category : "");
            values.put(MeterInstalltionJobCardTable.Cols.METER_SUBCATEGORY, todayModel.subcategory != null ? todayModel.subcategory : "");
            values.put(MeterInstalltionJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE, todayModel.assignedDate != null ? todayModel.assignedDate : "");
            values.put(MeterInstalltionJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");
            values.put(MeterInstalltionJobCardTable.Cols.REQUEST_ID, todayModel.requestId != null ? todayModel.requestId : "");
            values.put(MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
            values.put(MeterInstalltionJobCardTable.Cols.REQUEST_ID, todayModel.requestId != null ? todayModel.requestId : "");


            values.put(MeterInstalltionJobCardTable.Cols.REJECTION_REMARK, todayModel.installRejectRemark != null ? todayModel.installRejectRemark : "");

            values.put(MeterInstalltionJobCardTable.Cols.SCREEN, todayModel.screen != null ? todayModel.screen : "");
            values.put(MeterInstalltionJobCardTable.Cols.AREA_NAME, todayModel.areaName != null ? todayModel.areaName : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getMeterInstallJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + MeterInstalltionJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getMeterInstallCardListFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getMeterInstallJobCardsToday(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MeterInstalltionJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + MeterInstalltionJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getMeterInstallCardListFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getMeterInstallJobCardsWeek(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                        + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + MeterInstalltionJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + MeterInstalltionJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getMeterInstallCardListFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getMeterInstallJobCardsMonth(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                        + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + MeterInstalltionJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + MeterInstalltionJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getMeterInstallCardListFromCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }


    private static ArrayList<TodayModel> getMeterInstallCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getMeterInstallCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getMeterInstallCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (!cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.DUE_DATE)).isEmpty()) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        todayModel.meterId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_ID)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_ID)) : "";
        todayModel.meterInstallationId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID)) : "";
        todayModel.consumerName = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) : "";
        todayModel.address = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA)) : "";
        todayModel.consumerNo = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NO)) : "";
        todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.MOBILE_NO)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_CATEGORY)) : "";
        todayModel.subcategory = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_SUBCATEGORY)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS)) : "";
        todayModel.requestId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) : "";
        todayModel.dueDate = formattedDate != null ? formattedDate : "";


        todayModel.installRejectRemark = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REJECTION_REMARK)) : "";


        todayModel.screen = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) : "";

        return todayModel;
    }

    //  get total count of meterinstallToday
    public static int getMeterInstallTodayCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static int getMeterInstallTodayCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    // Search Meter Install Cards

    public static ArrayList<TodayModel> getMeterInstallSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + MeterInstalltionJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.DUE_DATE)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.DUE_DATE)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                TodayModel todayModel = new TodayModel();
                todayModel.meterId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_ID)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_ID)) : "";
                todayModel.meterInstallationId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_INSTALL_ID)) : "";
                todayModel.consumerName = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) : "";
                todayModel.address = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA)) : "";
                todayModel.consumerNo = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NO)) : "";
                todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.MOBILE_NO)) : "";
                todayModel.category = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_CATEGORY)) : "";
                todayModel.subcategory = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.METER_SUBCATEGORY)) : "";
                todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ASSIGNED_DATE)) : "";
                todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.ACCEPT_STATUS)) : "";
                todayModel.requestId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) : "";
                todayModel.dueDate = formattedDate != null ? formattedDate : "";

                todayModel.installRejectRemark = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REJECTION_REMARK)) : "";


                todayModel.screen = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) : "";
                todayModel.area = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) : "";

                searchlist.add(todayModel);
            }
        }
        db.close();
        return searchlist;
    }



    public static ArrayList<HistoryModel> getMeterInstallHistorySearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + MeterInstalltionJobCardTable.TABLE_NAME + " where "
                + MeterInstalltionJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + MeterInstalltionJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + MeterInstalltionJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<HistoryModel> searchlist = new ArrayList<HistoryModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) != null
                        ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.REQUEST_ID)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(MeterInstalltionJobCardTable.Cols.AREA_NAME)) : "";
                searchlist.add(historyModel);
            }
        }
        db.close();
        return searchlist;
    }

    //     AssetTable Related Methods Starts
    public static void saveAssetJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels) {
            DatabaseManager.saveAssetJobCardsInfo(mContext, todayModel);
        }
    }

    private static void saveAssetJobCardsInfo(Context context, TodayModel todayModel) {
        if (todayModel != null) {
            ContentValues values = getContentValuesAssetJobCardTable(context, todayModel);
            String condition = AssetJobCardTable.Cols.ASSET_CARD_ID + "='" + todayModel.assetCardId + "'";
            saveValues(context, AssetJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesAssetJobCardTable(Context context, TodayModel todayModel) {
        ContentValues values = new ContentValues();
        try {
            values.put(AssetJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(AssetJobCardTable.Cols.ASSET_CARD_ID, todayModel.assetCardId != null ? todayModel.assetCardId : "");
            values.put(AssetJobCardTable.Cols.ASSET_NAME, todayModel.assetName != null ? todayModel.assetName : "");
            values.put(AssetJobCardTable.Cols.ASSET_CATEGORY, todayModel.category != null ? todayModel.category : "");
            values.put(AssetJobCardTable.Cols.ASSET_MAKE, todayModel.assetMake != null ? todayModel.assetMake : "");
            values.put(AssetJobCardTable.Cols.ASSET_MAKE_NO, todayModel.assetMakeNo != null ? todayModel.assetMakeNo : "");
            values.put(AssetJobCardTable.Cols.ASSET_LOCATION, todayModel.location != null ? todayModel.location : "");
            values.put(AssetJobCardTable.Cols.ASSET_CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE, CommonUtility.getCurrentDate());
            values.put(AssetJobCardTable.Cols.ASSET_SUBMITTED_DATE, todayModel.assignedDate != null ? todayModel.assignedDate : "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public static ArrayList<TodayModel> getAssetJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AssetJobCardTable.TABLE_NAME + " where "
                + AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getAssetJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getAssetJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel assetJobCardModel;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                assetJobCardModel = getAssetJobCardFromCursor(cursor);
                jobCards.add(assetJobCardModel);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getAssetJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();
        todayModel.assetCardId = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_ID)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_ID)) : "";
        todayModel.assetName = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_NAME)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_NAME)) : "";
        todayModel.category = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CATEGORY)) : "";
        todayModel.assetMake = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE)) : "";
        todayModel.assetMakeNo = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE_NO)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_MAKE_NO)) : "";
        todayModel.location = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_LOCATION)) : "";
        todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_CARD_STATUS)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_ASSIGNED_DATE)) : "";
        todayModel.submittedDate = cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_SUBMITTED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(AssetJobCardTable.Cols.ASSET_SUBMITTED_DATE)) : "";
        return todayModel;
    }

    public static int getAssetJobCardCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AssetJobCardTable.TABLE_NAME + " where " +
                AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " +
                AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static void updateAssetJobCardStatus(String assetCardId, String jobCardStatus, String submittedDate) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AssetJobCardTable.Cols.ASSET_CARD_STATUS, jobCardStatus);
        values.put(AssetJobCardTable.Cols.ASSET_SUBMITTED_DATE, submittedDate);
        db.update(AssetJobCardTable.TABLE_NAME, values, "asset_card_id=" + assetCardId, null);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void handleDeAssignAssetJobCard(Context mContext, ArrayList<String> deAssignedJobCards, String userId) {
        for (String card_id : deAssignedJobCards) {
            deleteAssetJobCard(mContext, card_id, userId);
        }
    }

    public static void deleteAssetJobCard(Context context, String assetCardId, String userId) {
        try {
            String condition = AssetJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND " +
                    AssetJobCardTable.Cols.ASSET_CARD_ID + "='" + assetCardId + "' AND " +
                    AssetJobCardTable.Cols.ASSET_CARD_STATUS + "='" + AppConstants.CARD_STATUS_OPEN + "'";
            context.getContentResolver().delete(AssetJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //     AssetTable Related Methods Ends

    // Complaint job card insertion
    public static void saveComplaintJobCards(Context mContext, ArrayList<TodayModel> today_complist) {
        for (TodayModel todayModel : today_complist) {
            DatabaseManager.saveComplaintJobCardsInfo(mContext, todayModel);
        }

    }

    private static void saveComplaintJobCardsInfo(Context context, TodayModel today_complist) {
        if (today_complist != null) {
            ContentValues values = getContentValuesComplaintJobCardTable(context, today_complist);
            String condition = ComplaintJobCardTable.Cols.COMPLAINT_ID + "='" + today_complist.complaintId + "'";
            saveValues(context, ComplaintJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesComplaintJobCardTable(Context context, TodayModel today_complist) {
        ContentValues values = new ContentValues();
        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                if (today_complist.dueDate != null) {
                    date = baseFormat.parse(today_complist.dueDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }

            values.put(ComplaintJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(ComplaintJobCardTable.Cols.COMPLAINT_ID, today_complist.complaintId != null ? today_complist.complaintId : "");
            values.put(ComplaintJobCardTable.Cols.CONSUMER_NAME, today_complist.consumerName != null ? today_complist.consumerName : "");
            values.put(ComplaintJobCardTable.Cols.ASSIGNED_DATE, today_complist.assignedDate != null ? today_complist.assignedDate : "");
            values.put(ComplaintJobCardTable.Cols.ADDRESS, today_complist.address != null ? today_complist.address : "");
            values.put(ComplaintJobCardTable.Cols.CONSUMER_NO, today_complist.consumerNo != null ? today_complist.consumerNo : "");
            values.put(ComplaintJobCardTable.Cols.COMPLAINT_TYPE, today_complist.complaintType != null ? today_complist.complaintType : "");
            values.put(ComplaintJobCardTable.Cols.MOBILE_NO, today_complist.mobileNumber != null ? today_complist.mobileNumber : "");
            values.put(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID, today_complist.complaintNumber != null ? today_complist.complaintNumber : "");
            values.put(ComplaintJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");
            values.put(ComplaintJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);

            values.put(ComplaintJobCardTable.Cols.SCREEN, today_complist.screen != null ? today_complist.screen : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getComplaintJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getComplaintCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getComplaintJobCardsToday(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ComplaintJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ComplaintJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getComplaintCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getComplaintJobCardsWeek(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                        + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ComplaintJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ComplaintJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ComplaintJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getComplaintCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getComplaintJobCardsMonth(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                        + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ComplaintJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ComplaintJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ComplaintJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getComplaintCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getComplaintCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getComplaintCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getComplaintCardFromCursor(Cursor cursor) {
        TodayModel complaintTodayCards = new TodayModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.DUE_DATE)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }

        complaintTodayCards.complaintId = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_ID)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_ID)) : "";
        complaintTodayCards.consumerName = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) : "";
        complaintTodayCards.address = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) : "";
        complaintTodayCards.consumerNo = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NO)) : "";
        complaintTodayCards.complaintNumber = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) : "";
        complaintTodayCards.complaintType = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_TYPE)) : "";
        complaintTodayCards.mobileNumber = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.MOBILE_NO)) : "";
        complaintTodayCards.assignedDate = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ASSIGNED_DATE)) : "";
        complaintTodayCards.dueDate = formattedDate != null ? formattedDate : "";
        complaintTodayCards.acceptstatus = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ACCEPT_STATUS)) : "";

        complaintTodayCards.screen = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) : "";

        return complaintTodayCards;
    }


    public static ArrayList<TodayModel> getServiceJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ServiceJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getServiceCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getServiceJobCardsToday(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ServiceJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ServiceJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getServiceCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getServiceJobCardsWeek(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                        + ServiceJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ServiceJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ServiceJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ServiceJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getServiceCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getServiceJobCardsMonth(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                        + ServiceJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ServiceJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ServiceJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ServiceJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getServiceCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getServiceCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel nsc_today_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                nsc_today_cards = getServiceCardFromCursor(cursor);
                jobCards.add(nsc_today_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getServiceCardFromCursor(Cursor cursor) {
        TodayModel complaintTodayCards = new TodayModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.DUE_DATE)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }

        complaintTodayCards.consumerNo = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NO)) : "";
        complaintTodayCards.serviceType = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_TYPE)) : "";
        complaintTodayCards.consumerName = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) : "";
        complaintTodayCards.address = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) : "";
        complaintTodayCards.serviceNumber = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) : "";
        complaintTodayCards.service_id = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_ID)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_ID)) : "";
        complaintTodayCards.mobileNumber = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.MOBILE_NO)) : "";
        complaintTodayCards.assignedDate = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ASSIGNED_DATE)) : "";
        complaintTodayCards.acceptstatus = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ACCEPT_STATUS)) : "";
        complaintTodayCards.dueDate = formattedDate != null ? formattedDate : "";

        complaintTodayCards.acceptstatus = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ACCEPT_STATUS)) : "";


        complaintTodayCards.screen = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) : "";

        return complaintTodayCards;
    }


    //Update Complaint Card
    public static void updateComplaintCardStatus(String completedOn, String Complaintid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ComplaintJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(ComplaintJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(ComplaintJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{Complaintid};
        int str = db.update(ComplaintJobCardTable.TABLE_NAME, values, "complaint_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllComplaintCardStatus(String completedOn, String Complaintid, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{Complaintid};
        int str = db.update(AllJobCardTable.TABLE_NAME, values, "complaint_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    //get Complaint Detail

    public static ArrayList<TodayModel> getComplaintDetailCard(String jobCardStatus, String complaint_id) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.COMPLAINT_ID + "='" + complaint_id + "' AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getComplaintCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    //get Service Detail

    public static ArrayList<TodayModel> getServiceDetailCard(String jobCardStatus, String complaint_id) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.SERVICE_ID + "='" + complaint_id + "' AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<TodayModel> jobCards = getServiceCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }


    // Get Complaint History Card

    public static ArrayList<HistoryModel> getComplaintHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        /*Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + userId
                + "' GROUP BY "
                + ComplaintJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + ComplaintJobCardTable.Cols.ID + " DESC", null);*/
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED + "' ORDER BY "
                + ComplaintJobCardTable.Cols.COMPLETED_ON + " DESC", null);
        ArrayList<HistoryModel> historyCards = getComplaintHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getComplaintHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel complaint_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                complaint_history_cards = getComplaintHistoryJobCardListCursor(cursor);
                jobCards.add(complaint_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getComplaintHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) : "";
        historyModel.screen = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) != null
                ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) : "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) != null
                ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) != null
                ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CARD_STATUS)) : "";
        return historyModel;
    }


    // Get Service History Card

    public static ArrayList<HistoryModel> getServiceHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        /*Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' GROUP BY "
                + ServiceJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + ServiceJobCardTable.Cols.ID + " DESC", null);*/
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED + "' ORDER BY "
                + ServiceJobCardTable.Cols.COMPLETED_ON + " DESC", null);
        ArrayList<HistoryModel> historyCards = getServiceHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getServiceHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel complaint_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                complaint_history_cards = getServiceHistoryJobCardListCursor(cursor);
                jobCards.add(complaint_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getServiceHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) : "";
        historyModel.screen = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) != null
                ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) : "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) != null
                ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) != null
                ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CARD_STATUS)) : "";
        return historyModel;
    }

    // Search Complaints Cards
    public static ArrayList<TodayModel> getComplaintsSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + ComplaintJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todayModel = new TodayModel();

                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.DUE_DATE)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.DUE_DATE)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }

                todayModel.complaintId = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_ID)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_ID)) : "";
                todayModel.consumerName = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) : "";
                todayModel.address = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) : "";
                todayModel.consumerNo = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NO)) : "";
                todayModel.complaintNumber = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) : "";
                todayModel.complaintType = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_TYPE)) : "";
                todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.MOBILE_NO)) : "";
                todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ASSIGNED_DATE)) : "";
                todayModel.dueDate = formattedDate != null ? formattedDate : "";
                todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ACCEPT_STATUS)) : "";

                todayModel.screen = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) : "";

                searchlist.add(todayModel);
            }
        }
        db.close();
        return searchlist;
    }

    public static ArrayList<HistoryModel> getComplaintsHistorySearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ComplaintJobCardTable.TABLE_NAME + " where "
                + ComplaintJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + ComplaintJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + ComplaintJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<HistoryModel> searchlist = new ArrayList<HistoryModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) != null
                        ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.COMPLAINT_REQUEST_ID)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) != null
                        ? cursor.getString(cursor.getColumnIndex(ComplaintJobCardTable.Cols.ADDRESS)) : "";
                searchlist.add(historyModel);
            }
        }
        db.close();
        return searchlist;
    }

    //get rejected registration searched cards

    public static ArrayList<RegistrationModel> getRejectedSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + RegistrationTable.TABLE_NAME + " where "
                + RegistrationTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + RegistrationTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + RegistrationTable.Cols.NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<RegistrationModel> searchlist = new ArrayList<RegistrationModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RegistrationModel registrationModel = new RegistrationModel();

                registrationModel.registrationNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.REGISTRATION_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.REGISTRATION_NO)) : "";
                registrationModel.nscId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NSC_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NSC_ID)) : "";
                registrationModel.name = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NAME)) : "";
                registrationModel.mobile = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MOBILE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MOBILE)) : "";
                registrationModel.addhaar = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ADHAAR)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ADHAAR)) : "";
                registrationModel.emailId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.EMAIL_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.EMAIL_ID)) : "";
                registrationModel.consumerCategory = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_CATEGORY)) : "";
                registrationModel.consumerSubCategory = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY)) : "";
                registrationModel.flatNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLAT_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLAT_NO)) : "";
                registrationModel.buildingName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BUILDING_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BUILDING_NAME)) : "";
                registrationModel.location = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LOCATION)) : "";
                registrationModel.state = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.STATE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.STATE)) : "";
                registrationModel.city = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CITY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CITY)) : "";
                registrationModel.area = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA)) : "";
                registrationModel.pincode = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PINCODE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PINCODE)) : "";
                registrationModel.premise = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PREMISE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PREMISE)) : "";
                registrationModel.connectivity = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONNECTIVITY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONNECTIVITY)) : "";
                registrationModel.schemeName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_NAME)) : "";
                registrationModel.schemeAmount = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_AMOUNT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_AMOUNT)) : "";
                registrationModel.paymentMethod = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PAYMENT_METHOD)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PAYMENT_METHOD)) : "";
                registrationModel.chequeDDNumber = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_DD_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_DD_NUMBER)) : "";
                registrationModel.micrCode = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MICR_CODE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MICR_CODE)) : "";
                registrationModel.bank = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BANK)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BANK)) : "";
                registrationModel.branchName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BRANCH_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BRANCH_NAME)) : "";
                registrationModel.dateOfPayment = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DATE_OF_PAYMENT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DATE_OF_PAYMENT)) : "";
                registrationModel.areaName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA_NAME)) : "";
                registrationModel.floorNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLOOR_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLOOR_NO)) : "";
                registrationModel.plotNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PLOT_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PLOT_NO)) : "";
                registrationModel.wing = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.WING)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.WING)) : "";
                registrationModel.roadNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ROAD_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ROAD_NO)) : "";
                registrationModel.landmark = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LANDMARK)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LANDMARK)) : "";
                registrationModel.district = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DISTRICT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DISTRICT)) : "";
                registrationModel.societyName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SOCIETY_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SOCIETY_NAME)) : "";

                searchlist.add(registrationModel);
            }
        }
        db.close();
        return searchlist;
    }

    public static ArrayList<HistoryModel> getRejectedHistorySearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + RejectedJobCardTable.TABLE_NAME + " where "
                + RejectedJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + RejectedJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + RejectedJobCardTable.Cols.NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<HistoryModel> searchlist = new ArrayList<HistoryModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.NAME)) : "";
                historyModel.screen = "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.REGISTRATION_NO)) != null
                        ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.REGISTRATION_NO)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.AREA_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.AREA_NAME)) : "";
                searchlist.add(historyModel);
            }
        }
        db.close();
        return searchlist;
    }



    // Search Service Cards
    public static ArrayList<TodayModel> getServiceSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + ServiceJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel serviceCards = new TodayModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.DUE_DATE)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.DUE_DATE)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                serviceCards.consumerNo = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NO)) : "";
                serviceCards.serviceType = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_TYPE)) : "";
                serviceCards.consumerName = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) : "";
                serviceCards.address = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) : "";
                serviceCards.serviceNumber = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) : "";
                serviceCards.service_id = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_ID)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_ID)) : "";
                serviceCards.mobileNumber = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.MOBILE_NO)) : "";
                serviceCards.assignedDate = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ASSIGNED_DATE)) : "";
                serviceCards.acceptstatus = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ACCEPT_STATUS)) : "";
                serviceCards.dueDate = formattedDate != null ? formattedDate : "";

                serviceCards.screen = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) : "";

                searchlist.add(serviceCards);
            }
        }
        db.close();
        return searchlist;
    }


    public static ArrayList<HistoryModel> getServiceHistorySearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ServiceJobCardTable.TABLE_NAME + " where "
                + ServiceJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + ServiceJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + ServiceJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<HistoryModel> searchlist = new ArrayList<HistoryModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) != null
                        ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.SERVICE_NO)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) != null
                        ? cursor.getString(cursor.getColumnIndex(ServiceJobCardTable.Cols.ADDRESS)) : "";
                searchlist.add(historyModel);
            }
        }
        db.close();
        return searchlist;
    }

    //Rejected Registration related methods
    public static void saveRejectedJobCards(Context mContext, ArrayList<RegistrationModel> registrationModels){
        for (RegistrationModel registrationModel : registrationModels){
            DatabaseManager.saveRejectedJobCardsInfo(mContext, registrationModel);
        }
    }
    private static void saveRejectedJobCardsInfo(Context context, RegistrationModel registrationModel) {
        if (registrationModel != null) {
            ContentValues values = getContentValuesRejectedJobCardTable(context, registrationModel);
            String condition = RejectedJobCardTable.Cols.REGISTRATION_NO+ "='"
                    + registrationModel.registrationNo + "'";
            saveValues(context, RejectedJobCardTable.CONTENT_URI, values, condition);
        }
    }
    private static ContentValues getContentValuesRejectedJobCardTable(Context context, RegistrationModel registrationModel) {
        ContentValues values = new ContentValues();
        try {
            values.put(RejectedJobCardTable.Cols.USER_LOGIN_ID , AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(RejectedJobCardTable.Cols.NAME , registrationModel.applicantName != null ? registrationModel.applicantName : "");
            values.put(RejectedJobCardTable.Cols.REGISTRATION_NO , registrationModel.registrationNo != null ? registrationModel.registrationNo : "");
            values.put(RejectedJobCardTable.Cols.AREA_NAME , registrationModel.areaName != null ? registrationModel.areaName : "");
            values.put(RejectedJobCardTable.Cols.CARD_STATUS , AppConstants.CARD_STATUS_OPEN);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
    public static void saveRejectedRegistrationJobCards(Context mContext, ArrayList<RegistrationModel> registrationModels, String cardStatus){
        for (RegistrationModel registrationModel : registrationModels){
            DatabaseManager.saveRejectedRegistrationJobCardsInfo(mContext, registrationModel, cardStatus);
        }
    }
    private static void saveRejectedRegistrationJobCardsInfo(Context context, RegistrationModel registrationModel, String cardStatus) {
        if (registrationModel != null) {
            ContentValues values = getContentValuesRejectedRegistrationJobCardTable(context, registrationModel, cardStatus);
            String condition = RegistrationTable.Cols.NSC_ID+ "='"
                    + registrationModel.nscId + "'";
            saveValues(context, RegistrationTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesRejectedRegistrationJobCardTable(Context context, RegistrationModel registrationModel, String cardStatus) {
        ContentValues values = new ContentValues();
        try {
            values.put(RegistrationTable.Cols.USER_LOGIN_ID , AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(RegistrationTable.Cols.NSC_ID , registrationModel.nscId != null ? registrationModel.nscId : "");
            values.put(RegistrationTable.Cols.REGISTRATION_NO , registrationModel.registrationNo != null ? registrationModel.registrationNo : "");
            values.put(RegistrationTable.Cols.NAME , registrationModel.applicantName != null ? registrationModel.applicantName : "");
            values.put(RegistrationTable.Cols.ADHAAR , registrationModel.addhaar != null ? registrationModel.addhaar : "");
            values.put(RegistrationTable.Cols.CONSUMER_NO , registrationModel.consumerNo != null ? registrationModel.consumerNo : "");
            values.put(RegistrationTable.Cols.CONSUMER_CATEGORY , registrationModel.consumerCategory != null ? registrationModel.consumerCategory : "");
            values.put(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY , registrationModel.consumerSubCategory != null ? registrationModel.consumerSubCategory : "");
            values.put(RegistrationTable.Cols.AREA , registrationModel.area != null ? registrationModel.area : "");
            values.put(RegistrationTable.Cols.AREA_NAME , registrationModel.areaName != null ? registrationModel.areaName : "");
            values.put(RegistrationTable.Cols.BANK , registrationModel.bank != null ? registrationModel.bank : "");
            values.put(RegistrationTable.Cols.BRANCH_NAME , registrationModel.branchName != null ? registrationModel.branchName : "");
            values.put(RegistrationTable.Cols.MICR_CODE , registrationModel.micrCode != null ? registrationModel.micrCode : "");
            values.put(RegistrationTable.Cols.CHEQUE_DD_NUMBER , registrationModel.chequeDDNumber != null ? registrationModel.chequeDDNumber : "");
            values.put(RegistrationTable.Cols.PAYMENT_METHOD , registrationModel.paymentMethod != null ? registrationModel.paymentMethod : "");
            values.put(RegistrationTable.Cols.SCHEME_AMOUNT , registrationModel.schemeAmount != null ? registrationModel.schemeAmount : "");
            values.put(RegistrationTable.Cols.SCHEME_NAME , registrationModel.schemeName != null ? registrationModel.schemeName : "");
            values.put(RegistrationTable.Cols.DATE_OF_PAYMENT , registrationModel.dateOfPayment != null ? registrationModel.dateOfPayment : "");
            values.put(RegistrationTable.Cols.STATE , registrationModel.state != null ? registrationModel.state : "");
            values.put(RegistrationTable.Cols.CITY , registrationModel.city != null ? registrationModel.city : "");
            values.put(RegistrationTable.Cols.PINCODE , registrationModel.pincode != null ? registrationModel.pincode : "");
            values.put(RegistrationTable.Cols.EMAIL_ID , registrationModel.emailId != null ? registrationModel.emailId : "");
            values.put(RegistrationTable.Cols.MOBILE , registrationModel.mobile != null ? registrationModel.mobile : "");
            values.put(RegistrationTable.Cols.FLAT_NO , registrationModel.flatNo != null ? registrationModel.flatNo : "");
            values.put(RegistrationTable.Cols.BUILDING_NAME , registrationModel.buildingName != null ? registrationModel.buildingName: "");
            values.put(RegistrationTable.Cols.LOCATION , registrationModel.billingLocation != null ? registrationModel.billingLocation : "");
            values.put(RegistrationTable.Cols.PREMISE , registrationModel.typeOfPremises != null ? registrationModel.typeOfPremises : "");
            values.put(RegistrationTable.Cols.IS_REJECTED , registrationModel.isRejected != null ? registrationModel.isRejected : "");
            values.put(RegistrationTable.Cols.CARD_STATUS , cardStatus);

            values.put(RegistrationTable.Cols.FLOOR_NO , registrationModel.floorNo != null ? registrationModel.floorNo : "");
            values.put(RegistrationTable.Cols.PLOT_NO , registrationModel.plotNo != null ? registrationModel.plotNo : "");
            values.put(RegistrationTable.Cols.WING , registrationModel.wing != null ? registrationModel.wing : "");
            values.put(RegistrationTable.Cols.ROAD_NO , registrationModel.roadNo!= null ? registrationModel.roadNo : "");
            values.put(RegistrationTable.Cols.LANDMARK , registrationModel.landmark != null ? registrationModel.landmark: "");
            values.put(RegistrationTable.Cols.DISTRICT , registrationModel.district != null ? registrationModel.district : "");
            values.put(RegistrationTable.Cols.SOCIETY_NAME , registrationModel.societyName != null ? registrationModel.societyName : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    // Enquiry Related Methods

    public static void saveEnquiryJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels) {
            DatabaseManager.saveEnquiryJobCardsInfo(mContext, todayModel);
        }
    }


    private static void saveEnquiryJobCardsInfo(Context context, TodayModel todayModel) {
        if (todayModel != null) {
            ContentValues values = getContentValuesEnquiryJobCardTable(context, todayModel);
            String condition = ConsumerEnquiryTable.Cols.ENQUIRY_ID + "='"
                    + todayModel.requestId + "'";
            saveValues(context, ConsumerEnquiryTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesEnquiryJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();
        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                if (todayModels.dueDate != null) {
                    date = baseFormat.parse(todayModels.dueDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }


            values.put(ConsumerEnquiryTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(ConsumerEnquiryTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(ConsumerEnquiryTable.Cols.ASSIGN_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(ConsumerEnquiryTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(ConsumerEnquiryTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(ConsumerEnquiryTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(ConsumerEnquiryTable.Cols.ENQUIRY_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(ConsumerEnquiryTable.Cols.ENQUIRY_NO, todayModels.enquiryNumber != null ? todayModels.enquiryNumber : "");
            values.put(ConsumerEnquiryTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");


            values.put(ConsumerEnquiryTable.Cols.SCREEN, todayModels.screen != null ? todayModels.screen : "");
            values.put(ConsumerEnquiryTable.Cols.STATE_ID, todayModels.screen != null ? todayModels.stateId : "");
            values.put(ConsumerEnquiryTable.Cols.CITY_ID, todayModels.screen != null ? todayModels.cityId : "");
            values.put(ConsumerEnquiryTable.Cols.STATE, todayModels.screen != null ? todayModels.state : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }


    //  Save new Enquiry Card in table

    public static void saveNewEnquiryJobCards(Context mContext, TodayModel todayModels, String nscId, String screen, String status) {
        DatabaseManager.saveNewEnquiryJobCardsInfo(mContext, todayModels, nscId, screen, status);
    }

    private static void saveNewEnquiryJobCardsInfo(Context context, TodayModel todayModel, String nscId, String screen, String status) {
        if (todayModel != null) {
            ContentValues values = getContentValuesNewEnquiryJobCardTable(context, todayModel, nscId, screen, status);
            String condition = ConsumerEnquiryTable.Cols.ENQUIRY_ID + "='"
                    + todayModel.requestId + "'";
            saveValues(context, ConsumerEnquiryTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesNewEnquiryJobCardTable(Context context, TodayModel todayModels, String nscId, String screen,
                                                                        String status) {
        ContentValues values = new ContentValues();
        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                if (todayModels.dueDate != null) {
                    date = baseFormat.parse(todayModels.dueDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }


            values.put(ConsumerEnquiryTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(ConsumerEnquiryTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(ConsumerEnquiryTable.Cols.ASSIGN_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(ConsumerEnquiryTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(ConsumerEnquiryTable.Cols.CARD_STATUS, status);
            values.put(ConsumerEnquiryTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(ConsumerEnquiryTable.Cols.ENQUIRY_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(ConsumerEnquiryTable.Cols.ENQUIRY_NO, todayModels.enquiryNumber != null ? todayModels.enquiryNumber : "");
            values.put(ConsumerEnquiryTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");

            values.put(ConsumerEnquiryTable.Cols.SCREEN, screen != null ? screen : "");
            values.put(ConsumerEnquiryTable.Cols.STATE_ID, todayModels.screen != null ? todayModels.stateId : "");
            values.put(ConsumerEnquiryTable.Cols.CITY_ID, todayModels.screen != null ? todayModels.cityId : "");
            values.put(ConsumerEnquiryTable.Cols.STATE, todayModels.screen != null ? todayModels.state : "");
            values.put(ConsumerEnquiryTable.Cols.NSC_ID, nscId != null ? nscId : "");
            values.put(ConsumerEnquiryTable.Cols.COMPLETED_ON, todayModels.completedOn != null ? todayModels.completedOn : "");


            values.put(ConsumerEnquiryTable.Cols.REGISTRATION_ID, todayModels.registrationId != null ? todayModels.registrationId : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }




    public static void saveAllNewEnquiryJobCards(Context mContext, TodayModel todayModels, String nscId, String screen,
                                                 String status) {
        DatabaseManager.saveAllNewEnquiryJobCardsInfo(mContext, todayModels, nscId, screen, status);
    }

    private static void saveAllNewEnquiryJobCardsInfo(Context context, TodayModel todayModel, String nscId, String screen,
                                                      String status) {
        if (todayModel != null) {
            ContentValues values = getContentValuesAllNewEnquiryJobCardTable(context, todayModel, nscId, screen, status);
            String condition = AllJobCardTable.Cols.ENQUIRY_ID + "='"
                    + todayModel.requestId + "'";
            saveValues(context, AllJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesAllNewEnquiryJobCardTable(Context context, TodayModel todayModels, String nscId,
                                                                           String screen, String status) {
        ContentValues values = new ContentValues();
        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                if (todayModels.dueDate != null) {
                    date = baseFormat.parse(todayModels.dueDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }


            values.put(AllJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(AllJobCardTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(AllJobCardTable.Cols.ASSIGNED_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(AllJobCardTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(AllJobCardTable.Cols.STATUS, status);
            values.put(AllJobCardTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(AllJobCardTable.Cols.ENQUIRY_ID, todayModels.requestId != null ? todayModels.requestId : "");
            values.put(AllJobCardTable.Cols.ENQUIRY_NO, todayModels.enquiryNumber != null ? todayModels.enquiryNumber : "");
            values.put(AllJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");

            values.put(AllJobCardTable.Cols.SCREEN, screen != null ? screen : "");
            values.put(AllJobCardTable.Cols.STATE_ID, todayModels.screen != null ? todayModels.stateId : "");
            values.put(AllJobCardTable.Cols.CITY_ID, todayModels.screen != null ? todayModels.cityId : "");
            values.put(AllJobCardTable.Cols.STATE, todayModels.screen != null ? todayModels.state : "");
            values.put(AllJobCardTable.Cols.NSC_ID, nscId != null ? nscId : "");
            values.put(AllJobCardTable.Cols.COMPLETED_ON, todayModels.completedOn != null ? todayModels.completedOn : "");



            values.put(AllJobCardTable.Cols.REGISTRATION_ID, todayModels.registrationId != null ? todayModels.registrationId : "");



        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<RegistrationModel> getRejectedJobCards(String userId, String jobCardStatus){
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + RegistrationTable.TABLE_NAME + " where "
                + RegistrationTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + RegistrationTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        ArrayList<RegistrationModel> jobCards = getRejectedCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<RegistrationModel> getRejectedCardListFromCursor(Cursor cursor) {
        ArrayList<RegistrationModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            RegistrationModel assetJobCardModel;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                assetJobCardModel = getRejectedJobCardFromCursor(cursor);
                jobCards.add(assetJobCardModel);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static RegistrationModel getRejectedJobCardFromCursor(Cursor cursor) {
        RegistrationModel registrationModel = new RegistrationModel();

        registrationModel.registrationNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.REGISTRATION_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.REGISTRATION_NO)) : "";
        registrationModel.nscId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NSC_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NSC_ID)) : "";
        registrationModel.name = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NAME)) : "";
        registrationModel.mobile = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MOBILE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MOBILE)) : "";
        registrationModel.addhaar = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ADHAAR)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ADHAAR)) : "";
        registrationModel.emailId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.EMAIL_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.EMAIL_ID)) : "";
        registrationModel.consumerCategory = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_CATEGORY)) : "";
        registrationModel.consumerSubCategory = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY)) : "";
        registrationModel.flatNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLAT_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLAT_NO)) : "";
        registrationModel.buildingName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BUILDING_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BUILDING_NAME)) : "";
        registrationModel.location = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LOCATION)) : "";
        registrationModel.state = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.STATE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.STATE)) : "";
        registrationModel.city = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CITY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CITY)) : "";
        registrationModel.area = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA)) : "";
        registrationModel.pincode = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PINCODE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PINCODE)) : "";
        registrationModel.premise = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PREMISE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PREMISE)) : "";
        registrationModel.connectivity = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONNECTIVITY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONNECTIVITY)) : "";
        registrationModel.schemeName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_NAME)) : "";
        registrationModel.schemeAmount = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_AMOUNT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_AMOUNT)) : "";
        registrationModel.paymentMethod = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PAYMENT_METHOD)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PAYMENT_METHOD)) : "";
        registrationModel.chequeDDNumber = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_DD_NUMBER)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_DD_NUMBER)) : "";
        registrationModel.micrCode = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MICR_CODE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MICR_CODE)) : "";
        registrationModel.bank = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BANK)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BANK)) : "";
        registrationModel.branchName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BRANCH_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BRANCH_NAME)) : "";
        registrationModel.dateOfPayment = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DATE_OF_PAYMENT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DATE_OF_PAYMENT)) : "";
        registrationModel.areaName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA_NAME)) : "";
        registrationModel.floorNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLOOR_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLOOR_NO)) : "";
        registrationModel.plotNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PLOT_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PLOT_NO)) : "";
        registrationModel.wing = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.WING)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.WING)) : "";
        registrationModel.roadNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ROAD_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ROAD_NO)) : "";
        registrationModel.landmark = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LANDMARK)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LANDMARK)) : "";
        registrationModel.district = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DISTRICT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DISTRICT)) : "";
        registrationModel.societyName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SOCIETY_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SOCIETY_NAME)) : "";

        return registrationModel;
    }

    public static ArrayList<TodayModel> getEnquiryJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ConsumerEnquiryTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getEnquiryCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }


    public static ArrayList<TodayModel> getEnquiryJobCardsToday(String userId, String jobCardStatus) {
//        String date = CommonUtility.getCurrentDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConsumerEnquiryTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ConsumerEnquiryTable.Cols.DUE_DATE + " ASC", null);

        ArrayList<TodayModel> jobCards = getEnquiryCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getEnquiryJobCardsWeek(String userId, String jobCardStatus) {
//        String date = CommonUtility.getCurrentDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                        + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ConsumerEnquiryTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ConsumerEnquiryTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ConsumerEnquiryTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getEnquiryCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getEnquiryJobCardsMonth(String userId, String jobCardStatus) {
//        String date = CommonUtility.getCurrentDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                        + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ConsumerEnquiryTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ConsumerEnquiryTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ConsumerEnquiryTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);

        ArrayList<TodayModel> jobCards = getEnquiryCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getEnquiryCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel assetJobCardModel;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                assetJobCardModel = getEnquiryJobCardFromCursor(cursor);
                jobCards.add(assetJobCardModel);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getEnquiryJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.DUE_DATE)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        todayModel.id = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ID)) : "";
        todayModel.consumerName = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) : "";
        todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.MOBILE_NO)) : "";
        todayModel.job_id = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) : "";
        todayModel.enquiryNumber = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_NO)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ASSIGN_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ASSIGN_DATE)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) : "";
        todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CARD_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CARD_STATUS)) : "";
        todayModel.dueDate = formattedDate != null ? formattedDate : "";
        todayModel.requestId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) : "";

        todayModel.screen = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) : "";
        todayModel.stateId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE_ID)) : "";
        todayModel.cityId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY_ID)) : "";
        todayModel.state = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE)) : "";
        todayModel.city = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY)) : "";

        return todayModel;
    }

    public static ArrayList<HistoryModel> getEnquiryHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        /*Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' GROUP BY "
                + ConsumerEnquiryTable.Cols.ASSIGN_DATE + " ORDER BY "
                + ConsumerEnquiryTable.Cols.ID + " DESC", null);*/
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED
                + "' OR " + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_COMPLETED
                + "' ORDER BY "
                + ConsumerEnquiryTable.Cols.COMPLETED_ON + " DESC,"
                + ConsumerEnquiryTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getEnquiryHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return historyCards;
    }

    private static ArrayList<HistoryModel> getEnquiryHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getEnquiryHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static HistoryModel getEnquiryHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) : "";
        historyModel.screen = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) != null
                ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) : "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.NSC_ID)) != null
                ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.NSC_ID)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) != null
                ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CARD_STATUS)) : "";

        return historyModel;
    }

    public static void deleteDataRejected(String user_id, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + RejectedJobCardTable.TABLE_NAME + " WHERE "
                + RejectedJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + RejectedJobCardTable.Cols.COMPLETED_ON + "='" + date + "' AND "
                + RejectedJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void deleteDataEnquiry(String user_id, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + ConsumerEnquiryTable.TABLE_NAME + " WHERE "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + ConsumerEnquiryTable.Cols.ASSIGN_DATE + "='" + date + "' AND "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void handleDeAssignEnquiry(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardEnquiry(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardEnquiry(Context context, String card_id, String user_id) {
        try {
            String condition = ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                    + ConsumerEnquiryTable.Cols.ENQUIRY_ID + "='" + card_id + "'";
            context.getContentResolver().delete(ConsumerEnquiryTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getEnquiryJobCardCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static int getEnquiryJobCardCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + ConsumerEnquiryTable.Cols.ASSIGN_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static void updateRejectedCardStatus(String completedOn, String id, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(RejectedJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(RejectedJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{id};
        db.update(RejectedJobCardTable.TABLE_NAME, values, "registration_no=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }


    //Update Enquiry Card
    public static void updateEnquiryCardStatusUpload(String completedOn, String id, String jobCardStatus,
                                                     String nscId, String registrationId, String area) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConsumerEnquiryTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(ConsumerEnquiryTable.Cols.NSC_ID, nscId);
        values.put(ConsumerEnquiryTable.Cols.COMPLETED_ON, completedOn);
        values.put(ConsumerEnquiryTable.Cols.REGISTRATION_ID, registrationId);
        values.put(ConsumerEnquiryTable.Cols.JOB_AREA, area);
        String[] args = new String[]{id};
        db.update(ConsumerEnquiryTable.TABLE_NAME, values, "enquiry_no=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateEnquiryCardStatus(String completedOn, String id, String jobCardStatus, String nscId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConsumerEnquiryTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(ConsumerEnquiryTable.Cols.NSC_ID, nscId);
        values.put(ConsumerEnquiryTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{id};
        db.update(ConsumerEnquiryTable.TABLE_NAME, values, "enquiry_no=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateNewEnquiryCardStatus(String completedOn, String id, String jobCardStatus, String nscId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConsumerEnquiryTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(ConsumerEnquiryTable.Cols.NSC_ID, nscId);
        values.put(ConsumerEnquiryTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{id};
        db.update(ConsumerEnquiryTable.TABLE_NAME, values, "registration_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllEnquiryCardStatusUpload(String completedOn, String id, String jobCardStatus, String nscId, String registrationId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.NSC_ID, nscId);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        values.put(AllJobCardTable.Cols.REGISTRATION_ID, registrationId);
        String[] args = new String[]{id};
        db.update(AllJobCardTable.TABLE_NAME, values, "enquiry_no=? ", args);
        if (db.isOpen()) {
            db.close();
        }
    }
    public static void updateAllEnquiryCardStatus(String completedOn, String id, String jobCardStatus, String nscId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.NSC_ID, nscId);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{id};
        db.update(AllJobCardTable.TABLE_NAME, values, "enquiry_no=? ", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllNewEnquiryCardStatus(String completedOn, String id, String jobCardStatus, String nscId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.NSC_ID, nscId);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{id};
        db.update(AllJobCardTable.TABLE_NAME, values, "registration_id=? ", args);
        if (db.isOpen()) {
            db.close();
        }
    }



    public static ArrayList<TodayModel> getEnquirySearchedCards(String userId, String jobCardStatus, String searchText) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' and "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND ( "
                + ConsumerEnquiryTable.Cols.CONSUMER_NAME + " LIKE '%" + searchText + "%' )", null);

        ArrayList<TodayModel> searchList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todayModel = new TodayModel();

                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.DUE_DATE)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.DUE_DATE)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                todayModel.consumerName = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) : "";
                todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.MOBILE_NO)) : "";
                todayModel.job_id = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) : "";
                todayModel.enquiryNumber = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) : "";
                todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ASSIGN_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ASSIGN_DATE)) : "";
                todayModel.area = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) : "";
                todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CARD_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CARD_STATUS)) : "";
                todayModel.requestId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.ENQUIRY_ID)) : "";
                todayModel.dueDate = formattedDate != null ? formattedDate : "";

                todayModel.screen = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) : "";
                todayModel.stateId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE_ID)) : "";
                todayModel.cityId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY_ID)) : "";
                todayModel.state = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.STATE)) : "";
                todayModel.city = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY)) != null ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CITY)) : "";

                searchList.add(todayModel);
            }
        }
        db.close();
        return searchList;
    }


    public static ArrayList<HistoryModel> getEnquiryHistorySearchedCards(String userId, String jobCardStatus, String searchText) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConsumerEnquiryTable.TABLE_NAME + " where "
                + ConsumerEnquiryTable.Cols.USER_LOGIN_ID + "='" + userId + "' and "
                + ConsumerEnquiryTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND ( "
                + ConsumerEnquiryTable.Cols.CONSUMER_NAME + " LIKE '%" + searchText + "%' )", null);

        ArrayList<HistoryModel> searchList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();

                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }

                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.NSC_ID)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.NSC_ID)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConsumerEnquiryTable.Cols.JOB_AREA)) : "";

                searchList.add(historyModel);
            }
        }
        db.close();
        return searchList;
    }

    // All Searched Cards
    public static ArrayList<HistoryModel> getAllHistorySearchedCards(String userId, String jobCardStatus, String searchText) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' and "
                + AllJobCardTable.Cols.STATUS + "='" + jobCardStatus
                + "' OR " + AllJobCardTable.Cols.STATUS + "='" + AppConstants.CARD_STATUS_COMPLETED
                + "' AND ( "
                + AllJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchText + "%' )", null);

        ArrayList<HistoryModel> searchList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();

                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }

                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.NSC_ID)) != null
                        ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.NSC_ID)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) != null
                        ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) : "";

                searchList.add(historyModel);
            }
        }
        db.close();
        return searchList;
    }

    // Conversion Related Methods

    public static void saveConversionJobCards(Context mContext, ArrayList<TodayModel> todayModels) {
        for (TodayModel todayModel : todayModels) {
            DatabaseManager.saveConversionJobCardsInfo(mContext, todayModel);
        }
    }

    private static void saveConversionJobCardsInfo(Context context, TodayModel todayModel) {
        if (todayModel != null) {
            ContentValues values = getContentValuesConversionJobCardTable(context, todayModel);

            String condition = ConversionJobCardTable.Cols.CONVERSION_ID + "='"
                    + todayModel.job_id + "'";
            saveValues(context, ConversionJobCardTable.CONTENT_URI, values, condition);
        }
    }

    private static ContentValues getContentValuesConversionJobCardTable(Context context, TodayModel todayModels) {
        ContentValues values = new ContentValues();
        try {

            SimpleDateFormat baseFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            try {
                date = baseFormat.parse(todayModels.dueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = targetFormat.format(date);
            }
            values.put(ConversionJobCardTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(ConversionJobCardTable.Cols.CONSUMER_NAME, todayModels.consumerName != null ? todayModels.consumerName : "");
            values.put(ConversionJobCardTable.Cols.ASSIGNED_DATE, todayModels.assignedDate != null ? todayModels.assignedDate : "");
            values.put(ConversionJobCardTable.Cols.JOB_AREA, todayModels.area != null ? todayModels.area : "");
            values.put(ConversionJobCardTable.Cols.CARD_STATUS, AppConstants.CARD_STATUS_OPEN);
            values.put(ConversionJobCardTable.Cols.ACCEPT_STATUS, AppConstants.CARD_STATUS_DEFAULT);
            values.put(ConversionJobCardTable.Cols.MOBILE_NO, todayModels.mobileNumber != null ? todayModels.mobileNumber : "");
            values.put(ConversionJobCardTable.Cols.CONVERSION_ID, todayModels.conversionId != null ? todayModels.conversionId : "");
            values.put(ConversionJobCardTable.Cols.JOB_ID, todayModels.job_id != null ? todayModels.job_id : "");
            values.put(ConversionJobCardTable.Cols.DUE_DATE, formattedDate != null ? formattedDate : "");


            values.put(ConversionJobCardTable.Cols.REGULATOR_NO, todayModels.regulatorNo != null ? todayModels.regulatorNo : "");
            values.put(ConversionJobCardTable.Cols.METER_NO, todayModels.meterNo != null ? todayModels.meterNo : "");
            values.put(ConversionJobCardTable.Cols.INSTALLED_ON, todayModels.installedOn != null ? todayModels.installedOn : "");
            values.put(ConversionJobCardTable.Cols.RFC_VERIFIED_ON, todayModels.rfcVerifiedOn != null ? todayModels.rfcVerifiedOn : "");

            values.put(ConversionJobCardTable.Cols.REJECTION_REMARK, todayModels.siteRejectRemark != null ? todayModels.siteRejectRemark : "");

            values.put(ConversionJobCardTable.Cols.SCREEN, todayModels.screen != null ? todayModels.screen : "");
            values.put(ConversionJobCardTable.Cols.AREA_NAME, todayModels.areaName != null ? todayModels.areaName : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static ArrayList<TodayModel> getConversionJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ConversionJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getConversionCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getConversionJobCardsToday(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConversionJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' ORDER BY "
                + ConversionJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getConversionCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getConversionJobCardsWeek(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                        + ConversionJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ConversionJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ConversionJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ConversionJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getConversionCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getConversionJobCardsMonth(String userId, String jobCardStatus) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                        + ConversionJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + ConversionJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + ConversionJobCardTable.Cols.CARD_STATUS + "=? ORDER BY "
                        + ConversionJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getConversionCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getConversionCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel assetJobCardModel;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                assetJobCardModel = getConversionJobCardFromCursor(cursor);
                jobCards.add(assetJobCardModel);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getConversionJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.DUE_DATE)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }

        todayModel.consumerName = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) : "";
        todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.MOBILE_NO)) : "";
        todayModel.job_id = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) : "";
        todayModel.conversionId = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONVERSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONVERSION_ID)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ASSIGNED_DATE)) : "";
        todayModel.area = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_AREA)) : "";
        todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CARD_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CARD_STATUS)) : "";
        todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ACCEPT_STATUS)) : "";
        todayModel.dueDate = formattedDate != null ? formattedDate : "";

        todayModel.regulatorNo = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REGULATOR_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REGULATOR_NO)) : "";
        todayModel.meterNo = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.METER_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.METER_NO)) : "";
        todayModel.installedOn = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.INSTALLED_ON)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.INSTALLED_ON)) : "";
        todayModel.rfcVerifiedOn = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.RFC_VERIFIED_ON)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.RFC_VERIFIED_ON)) : "";

        todayModel.siteRejectRemark = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REJECTION_REMARK)) : "";

        todayModel.screen = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) : "";
        todayModel.areaName = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) : "";

        return todayModel;
    }

    public static ArrayList<HistoryModel> getConversionHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();


        /*Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' GROUP BY "
                + ConversionJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + ConversionJobCardTable.Cols.ID + " DESC ", null);*/
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED + "' ORDER BY "
                + ConversionJobCardTable.Cols.COMPLETED_ON + " DESC", null);
        ArrayList<HistoryModel> historyCards = getConversionHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();

        return historyCards;
    }

    private static ArrayList<HistoryModel> getConversionHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getConversionHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return jobCards;
    }

    private static HistoryModel getConversionHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) : "";
        historyModel.screen = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) != null
                ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) : "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) != null
                ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CARD_STATUS)) : "";

        return historyModel;
    }

    public static void deleteDataConversion(String user_id, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getWritableDatabase();
        String date = CommonUtility.getPreviousDate(15);
        db.execSQL("DELETE FROM " + ConversionJobCardTable.TABLE_NAME + " WHERE "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                + ConversionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'  AND "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'");
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void handleDeAssignConversion(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id) {
        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardConversion(mContext, card_id, user_id);
        }
    }

    public static void deleteJobCardConversion(Context context, String card_id, String user_id) {
        try {
            String condition = ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                    + ConversionJobCardTable.Cols.CONVERSION_ID + "='" + card_id + "'";
            context.getContentResolver().delete(ConversionJobCardTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateConversionCardStatus(String completedOn, String meterInstallId, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConversionJobCardTable.Cols.CARD_STATUS, jobCardStatus);
        values.put(ConversionJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(ConversionJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{meterInstallId};
        db.update(ConversionJobCardTable.TABLE_NAME, values, "conversion_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static void updateAllConversionCardStatus(String completedOn, String meterInstallId, String jobCardStatus, String status) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(AllJobCardTable.Cols.STATUS, jobCardStatus);
        values.put(AllJobCardTable.Cols.ACCEPT_STATUS, status);
        values.put(AllJobCardTable.Cols.COMPLETED_ON, completedOn);
        String[] args = new String[]{meterInstallId};
        db.update(AllJobCardTable.TABLE_NAME, values, "conversion_id=?", args);
        if (db.isOpen()) {
            db.close();
        }
    }

    public static int getConversionJobCardCount(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static int getConversionJobCardCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobCardStatus + "' AND "
                + ConversionJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static int getAllJobCardCount(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AllJobCardTable.Cols.STATUS + "='" + jobCardStatus + "' AND "
                + AllJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static int getAllJobCardScreen(String userId, String jobCardStatus, String date) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + AllJobCardTable.Cols.SCREEN + " from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AllJobCardTable.Cols.STATUS + "='" + jobCardStatus + "' AND "
                + AllJobCardTable.Cols.ASSIGNED_DATE + "='" + date + "'", null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public static ArrayList<TodayModel> getConversionSearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + ConversionJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<TodayModel> searchlist = new ArrayList<TodayModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TodayModel todayModel = new TodayModel();

                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.DUE_DATE)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.DUE_DATE)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }

                todayModel.consumerName = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) : "";
                todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.MOBILE_NO)) : "";
                todayModel.job_id = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) : "";
                todayModel.conversionId = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONVERSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONVERSION_ID)) : "";
                todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ASSIGNED_DATE)) : "";
                todayModel.area = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_AREA)) : "";
                todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CARD_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CARD_STATUS)) : "";
                todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.ACCEPT_STATUS)) : "";
                todayModel.dueDate = formattedDate != null ? formattedDate : "";

                todayModel.regulatorNo = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REGULATOR_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REGULATOR_NO)) : "";
                todayModel.meterNo = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.METER_NO)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.METER_NO)) : "";
                todayModel.installedOn = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.INSTALLED_ON)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.INSTALLED_ON)) : "";
                todayModel.rfcVerifiedOn = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.RFC_VERIFIED_ON)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.RFC_VERIFIED_ON)) : "";

                todayModel.siteRejectRemark = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.REJECTION_REMARK)) : "";


                todayModel.screen = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) : "";
                todayModel.areaName = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) : "";

                searchlist.add(todayModel);
            }
        }
        db.close();
        return searchlist;
    }


    public static ArrayList<HistoryModel> getConversionHistorySearchedCards(String Userid, String jobcardstatus, String searchtext) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + ConversionJobCardTable.TABLE_NAME + " where "
                + ConversionJobCardTable.Cols.USER_LOGIN_ID + "='" + Userid + "' and "
                + ConversionJobCardTable.Cols.CARD_STATUS + "='" + jobcardstatus + "' AND ( "
                + ConversionJobCardTable.Cols.CONSUMER_NAME + " LIKE '%" + searchtext + "%' )", null);

        ArrayList<HistoryModel> searchlist = new ArrayList<HistoryModel>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HistoryModel historyModel = new HistoryModel();
                SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    if (cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.COMPLETED_ON)) != null) {
                        date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.COMPLETED_ON)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = "";
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
                historyModel.date = formattedDate != null ? formattedDate : "";
                historyModel.name = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.CONSUMER_NAME)) : "";
                historyModel.screen = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.SCREEN)) : "";
                historyModel.nscId = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.JOB_ID)) : "";
                historyModel.area = cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) != null
                        ? cursor.getString(cursor.getColumnIndex(ConversionJobCardTable.Cols.AREA_NAME)) : "";
                searchlist.add(historyModel);
            }
        }
        db.close();
        return searchlist;
    }



    public static ArrayList<TodayModel> getAllJobCardsToday(String userId, String jobCardStatus) {
//        String date = CommonUtility.getCurrentDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(CommonUtility.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AllJobCardTable.Cols.DUE_DATE + "='" + formattedDate + "' AND "
                + AllJobCardTable.Cols.STATUS + "='" + jobCardStatus + "' ORDER BY "
                + AllJobCardTable.Cols.DUE_DATE + " ASC", null);

        ArrayList<TodayModel> jobCards = getAllJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getAllJobCardsWeek(String userId, String jobCardStatus) {
//        String date = CommonUtility.getCurrentDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");


        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 8);
            resultdate = new Date(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                        + AllJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + AllJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + AllJobCardTable.Cols.STATUS + "=? ORDER BY "
                        + AllJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);
        ArrayList<TodayModel> jobCards = getAllJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getAllJobCardsMonth(String userId, String jobCardStatus) {
//        String date = CommonUtility.getCurrentDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        Date startDate = null;
        Date resultdate = null;
        try {
            startDate = originalFormat.parse(CommonUtility.getCurrentDate());
            c.setTime(originalFormat.parse(CommonUtility.getCurrentDate()));
            c.add(Calendar.DATE, 31);
            resultdate = new Date(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginDate = targetFormat.format(startDate);
        String endDate = targetFormat.format(resultdate);

        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                        + AllJobCardTable.Cols.USER_LOGIN_ID + "=? AND ( "
                        + AllJobCardTable.Cols.DUE_DATE + " BETWEEN ? AND ?) AND "
                        + AllJobCardTable.Cols.STATUS + "=? ORDER BY "
                        + AllJobCardTable.Cols.DUE_DATE + " ASC",
                new String[]{userId, beginDate, endDate, jobCardStatus}, null);

        ArrayList<TodayModel> jobCards = getAllJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    public static ArrayList<TodayModel> getAllJobCards(String userId, String jobCardStatus) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AllJobCardTable.Cols.STATUS + "='" + jobCardStatus + "' ORDER BY "
                + AllJobCardTable.Cols.DUE_DATE + " ASC", null);
        ArrayList<TodayModel> jobCards = getAllJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();
        return jobCards;
    }

    private static ArrayList<TodayModel> getAllJobCardListFromCursor(Cursor cursor) {
        ArrayList<TodayModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            TodayModel assetJobCardModel;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                assetJobCardModel = getAllJobCardFromCursor(cursor);
                jobCards.add(assetJobCardModel);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return jobCards;
    }

    private static TodayModel getAllJobCardFromCursor(Cursor cursor) {
        TodayModel todayModel = new TodayModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.DUE_DATE)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.DUE_DATE)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        /* Enquiry */
        todayModel.id = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ID)) : "";
        todayModel.consumerName = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NAME)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NAME)) : "";
        todayModel.mobileNumber = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.MOBILE_NO)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.MOBILE_NO)) : "";
        todayModel.enquiryNumber = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ENQUIRY_NO)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ENQUIRY_NO)) : "";
        todayModel.assignedDate = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ASSIGNED_DATE)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ASSIGNED_DATE)) : "";
        todayModel.cardStatus = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATUS)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATUS)) : "";
        todayModel.dueDate = formattedDate != null ? formattedDate : "";
        todayModel.requestId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ENQUIRY_ID)) : "";
        todayModel.screen = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SCREEN)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SCREEN)) : "";
        todayModel.stateId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATE_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATE_ID)) : "";
        todayModel.cityId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CITY_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CITY_ID)) : "";
        todayModel.state = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATE)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATE)) : "";
        todayModel.city = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CITY)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CITY)) : "";

        /* Site Verification */
        todayModel.site_verification_id = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SITE_VERIFICATION_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SITE_VERIFICATION_ID)) : "";
        todayModel.acceptstatus = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ACCEPT_STATUS)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ACCEPT_STATUS)) : "";
        todayModel.type_of_premises = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.TYPE_OF_PREMISE)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.TYPE_OF_PREMISE)) : "";
        todayModel.service_requested = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_REQUESTED)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_REQUESTED)) : "";
        todayModel.requestId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REQUEST_ID)) : "";
        todayModel.latitude = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.LAT)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.LAT)) : "";
        todayModel.longitude = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.LONG)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.LONG)) : "";

        /* Meter Installation */
        todayModel.meterId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_ID)) : "";
        todayModel.meterInstallationId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_INSTALL_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_INSTALL_ID)) : "";
        todayModel.consumerNo = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NO)) : "";

        /* Conversion */
        todayModel.conversionId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONVERSION_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONVERSION_ID)) : "";
        todayModel.regulatorNo = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REGULATOR_NO)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REGULATOR_NO)) : "";
        todayModel.meterNo = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_NO)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_NO)) : "";
        todayModel.installedOn = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.INSTALLED_ON)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.INSTALLED_ON)) : "";
        todayModel.rfcVerifiedOn = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.RFC_VERIFIED_ON)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.RFC_VERIFIED_ON)) : "";

        /* Services */
        todayModel.serviceType = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_TYPE)) : "";
        todayModel.serviceNumber = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_NO)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_NO)) : "";
        todayModel.service_id = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_ID)) : "";

        /* Complaints */
        todayModel.complaintId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_ID)) : "";
        todayModel.complaintNumber = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_REQUEST_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_REQUEST_ID)) : "";
        todayModel.complaintType = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_TYPE)) : "";


        todayModel.installRejectRemark = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REJECTION_REMARK)) : "";
        todayModel.siteRejectRemark = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REJECTION_REMARK)) : "";
        todayModel.convertRejectRemark = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REJECTION_REMARK)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REJECTION_REMARK)) : "";
        todayModel.address = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ADDRESS)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ADDRESS)) : "";

//        con
        if (todayModel.screen.equals("Convert")) {
            todayModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) : "";
            todayModel.job_id = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_ID)) : "";
        } else {
            //enq

            todayModel.job_id = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ENQUIRY_ID)) : "";
            todayModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) : "";
        }

//        sv
        if (todayModel.screen.equals("Site\\nVerification") ||
                todayModel.screen.equals("Services") ||
                todayModel.screen.equals("Complaints")) {
            todayModel.category = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CATEGORY)) : "";
            todayModel.subcategory = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SUB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SUB_CATEGORY)) : "";
        } else {
            //        mi
            todayModel.category = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_CATEGORY)) : "";
            todayModel.subcategory = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_SUBCATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.METER_SUBCATEGORY)) : "";
        }


        todayModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) : "";

        return todayModel;
    }


    public static void handleDeAssignAll(Context mContext, ArrayList<String> re_de_assigned_jobcards, String user_id, String screen) {

        for (String card_id : re_de_assigned_jobcards) {
            deleteJobCardAll(mContext, card_id, user_id, screen);
        }
    }

    public static void deleteJobCardAll(Context context, String card_id, String user_id, String screen) {
        try {

            if (screen.equals("Enquiry")) {
                String condition = AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                        + AllJobCardTable.Cols.ENQUIRY_ID + "='" + card_id + "' AND "
                        + AllJobCardTable.Cols.SCREEN + "='" + screen + "'";
                context.getContentResolver().delete(AllJobCardTable.CONTENT_URI, condition, null);
            } else if (screen.equals("Convert")) {
                String condition = AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                        + AllJobCardTable.Cols.CONVERSION_ID + "='" + card_id + "' AND "
                        + AllJobCardTable.Cols.SCREEN + "='" + screen + "'";
                context.getContentResolver().delete(AllJobCardTable.CONTENT_URI, condition, null);
            } else if (screen.equals("Site\nVerification")) {
                String condition = AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                        + AllJobCardTable.Cols.SITE_VERIFICATION_ID + "='" + card_id + "' AND "
                        + AllJobCardTable.Cols.SCREEN + "='" + screen + "'";
                context.getContentResolver().delete(AllJobCardTable.CONTENT_URI, condition, null);
            } else if (screen.equals("Complaints")) {
                String condition = AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                        + AllJobCardTable.Cols.COMPLAINT_ID + "='" + card_id + "' AND "
                        + AllJobCardTable.Cols.SCREEN + "='" + screen + "'";
                context.getContentResolver().delete(AllJobCardTable.CONTENT_URI, condition, null);
            } else if (screen.equals("Services")) {
                String condition = AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                        + AllJobCardTable.Cols.SERVICE_ID + "='" + card_id + "' AND "
                        + AllJobCardTable.Cols.SCREEN + "='" + screen + "'";
                context.getContentResolver().delete(AllJobCardTable.CONTENT_URI, condition, null);
            } else if (screen.equals("Installation")) {
                String condition = AllJobCardTable.Cols.USER_LOGIN_ID + "='" + user_id + "' AND "
                        + AllJobCardTable.Cols.METER_INSTALL_ID + "='" + card_id + "' AND "
                        + AllJobCardTable.Cols.SCREEN + "='" + screen + "'";
                context.getContentResolver().delete(AllJobCardTable.CONTENT_URI, condition, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HistoryModel> getHistoryRejectedJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();


        /*Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' GROUP BY "
                + AllJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + AllJobCardTable.Cols.ID + " DESC ", null);*/
        Cursor cursor = db.rawQuery("Select * from " + RejectedJobCardTable.TABLE_NAME + " where "
                + RejectedJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + RejectedJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_CLOSED
                + "' OR " + RejectedJobCardTable.Cols.CARD_STATUS + "='" + AppConstants.CARD_STATUS_COMPLETED
                + "' ORDER BY "
                + RejectedJobCardTable.Cols.COMPLETED_ON + "," + RejectedJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getRejectedHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();

        return historyCards;
    }
    private static ArrayList<HistoryModel> getRejectedHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getRejectedHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return jobCards;
    }
    private static HistoryModel getRejectedHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();
        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.NAME)) : "";
        historyModel.screen = "";
        historyModel.nscId = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.REGISTRATION_NO)) != null
                ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.REGISTRATION_NO)) : "";
        historyModel.area = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.AREA_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.AREA_NAME)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.CARD_STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(RejectedJobCardTable.Cols.CARD_STATUS)) : "";

        return historyModel;
    }

    public static ArrayList<HistoryModel> getAllHistoryJobCard(String userId) {
        SQLiteDatabase db = DatabaseProvider.dbHelper.getReadableDatabase();


        /*Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' GROUP BY "
                + AllJobCardTable.Cols.ASSIGNED_DATE + " ORDER BY "
                + AllJobCardTable.Cols.ID + " DESC ", null);*/
        Cursor cursor = db.rawQuery("Select * from " + AllJobCardTable.TABLE_NAME + " where "
                + AllJobCardTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + AllJobCardTable.Cols.STATUS + "='" + AppConstants.CARD_STATUS_CLOSED
                + "' OR " + AllJobCardTable.Cols.STATUS + "='" + AppConstants.CARD_STATUS_COMPLETED
                + "' ORDER BY "
                + AllJobCardTable.Cols.COMPLETED_ON + "," + AllJobCardTable.Cols.ID + " DESC", null);
        ArrayList<HistoryModel> historyCards = getAllHistoryJobCardListFromCursor(cursor);
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        if (db.isOpen())
            db.close();

        return historyCards;
    }

    private static ArrayList<HistoryModel> getAllHistoryJobCardListFromCursor(Cursor cursor) {
        ArrayList<HistoryModel> jobCards = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            HistoryModel dsc_history_cards;
            jobCards = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                dsc_history_cards = getAllHistoryJobCardListCursor(cursor);
                jobCards.add(dsc_history_cards);
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return jobCards;
    }

    private static HistoryModel getAllHistoryJobCardListCursor(Cursor cursor) {
        HistoryModel historyModel = new HistoryModel();

        SimpleDateFormat baseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            if (cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLETED_ON)) != null) {
                date = baseFormat.parse(cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLETED_ON)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = targetFormat.format(date);
        }
        historyModel.screen = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SCREEN)) != null ?
                cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SCREEN)) : "";
        historyModel.date = formattedDate != null ? formattedDate : "";
        historyModel.name = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NAME)) != null
                ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.CONSUMER_NAME)) : "";
        historyModel.status = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATUS)) != null
                ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.STATUS)) : "";
        if (historyModel.screen.equals("Enquiry")) {
            historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.NSC_ID)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.NSC_ID)) : "";
            historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_AREA)) : "";
        }
        if (historyModel.screen.equals("Site\nVerification")) {
            historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REQUEST_ID)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REQUEST_ID)) : "";
            historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) : "";
        }
        if (historyModel.screen.equals("Installation")) {
            historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REQUEST_ID)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.REQUEST_ID)) : "";
            historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) : "";
        }
        if (historyModel.screen.equals("Convert")) {
            historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_ID)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.JOB_ID)) : "";
            historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.AREA)) : "";
        }
        if (historyModel.screen.equals("Services")) {
            historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_NO)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.SERVICE_NO)) : "";
            historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ADDRESS)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ADDRESS)) : "";
        }
        if (historyModel.screen.equals("Complaints")) {
            historyModel.nscId = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_REQUEST_ID)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.COMPLAINT_REQUEST_ID)) : "";
            historyModel.area = cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ADDRESS)) != null
                    ? cursor.getString(cursor.getColumnIndex(AllJobCardTable.Cols.ADDRESS)) : "";
        }

        return historyModel;
    }


    public static long saveRegistrationNSC(Context context, RegistrationModel registrationModel, Activity activity, String cardStatus) {
        if (registrationModel != null) {
            ContentValues values = getContentValuesRegistrationTable(context, registrationModel, cardStatus);
            return saveRegistrations(context, RegistrationTable.CONTENT_URI, values, null, activity);
        } else return 0;
    }

    private static ContentValues getContentValuesRegistrationTable(Context context, RegistrationModel registrationModel,
                                                                   String cardStatus) {
        ContentValues values = new ContentValues();
        try {
            values.put(RegistrationTable.Cols.USER_LOGIN_ID, AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, ""));
            values.put(RegistrationTable.Cols.ENQUIRY_ID, registrationModel.enquiryId != null ? registrationModel.enquiryId : "");
            values.put(RegistrationTable.Cols.CONSUMER_CATEGORY, registrationModel.consumerCategory != null ? registrationModel.consumerCategory : "");
            values.put(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY, registrationModel.consumerSubCategory != null ? registrationModel.consumerSubCategory : "");


            values.put(RegistrationTable.Cols.STATE, registrationModel.state != null ? registrationModel.state : "");
            values.put(RegistrationTable.Cols.NAME, registrationModel.name != null ? registrationModel.name : "");
            values.put(RegistrationTable.Cols.ADHAAR, registrationModel.addhaar != null ? registrationModel.addhaar : "");
            values.put(RegistrationTable.Cols.EMAIL_ID, registrationModel.emailId != null ? registrationModel.emailId : "");
            values.put(RegistrationTable.Cols.FLAT_NO, registrationModel.flatNo != null ? registrationModel.flatNo : "");
            values.put(RegistrationTable.Cols.BUILDING_NAME, registrationModel.buildingName != null ? registrationModel.buildingName : "");
            values.put(RegistrationTable.Cols.LOCATION, registrationModel.location != null ? registrationModel.location : "");
            values.put(RegistrationTable.Cols.AREA, registrationModel.area != null ? registrationModel.area : "");
            values.put(RegistrationTable.Cols.CITY, registrationModel.city != null ? registrationModel.city : "");
            values.put(RegistrationTable.Cols.PINCODE, registrationModel.pincode != null ? registrationModel.pincode : "");
            values.put(RegistrationTable.Cols.MOBILE, registrationModel.mobile != null ? registrationModel.mobile : "");
            values.put(RegistrationTable.Cols.PREMISE, registrationModel.premise != null ? registrationModel.premise : "");
            values.put(RegistrationTable.Cols.ENQUIRY_NO, registrationModel.enquiryNo != null ? registrationModel.enquiryNo : "");
            values.put(RegistrationTable.Cols.CONSUMER_NO, registrationModel.consumerNo != null ? registrationModel.consumerNo : "");
            values.put(RegistrationTable.Cols.SAVE_TYPE, registrationModel.saveType != null ? registrationModel.saveType : "");
            values.put(RegistrationTable.Cols.SCHEME_NAME, registrationModel.schemeName != null ? registrationModel.schemeName : "");
            values.put(RegistrationTable.Cols.SCHEME_AMOUNT, registrationModel.schemeAmount != null ? registrationModel.schemeAmount : "");
            values.put(RegistrationTable.Cols.PAYMENT_METHOD, registrationModel.paymentMethod != null ? registrationModel.paymentMethod : "");
            values.put(RegistrationTable.Cols.CHEQUE_NO, registrationModel.chequeNo != null ? registrationModel.chequeNo : "");
            values.put(RegistrationTable.Cols.CHEQUE_BRANCH, registrationModel.chequeBranch != null ? registrationModel.chequeBranch : "");
            values.put(RegistrationTable.Cols.CHEQUE_DATE, registrationModel.chequeDate != null ? registrationModel.chequeDate : "");
            values.put(RegistrationTable.Cols.CHEQUE_BANK, registrationModel.chequeBank != null ? registrationModel.chequeBank : "");
            values.put(RegistrationTable.Cols.CHEQUE_MICR, registrationModel.chequeMicr != null ? registrationModel.chequeMicr : "");
            values.put(RegistrationTable.Cols.DD_NO, registrationModel.ddNo != null ? registrationModel.ddNo : "");
            values.put(RegistrationTable.Cols.BANK_NAME, registrationModel.bankName != null ? registrationModel.bankName : "");
            values.put(RegistrationTable.Cols.DD_DATE, registrationModel.ddDate != null ? registrationModel.ddDate : "");
            values.put(RegistrationTable.Cols.DD_MICR, registrationModel.ddMicr != null ? registrationModel.ddMicr : "");
            values.put(RegistrationTable.Cols.DD_BRANCH, registrationModel.ddBranch != null ? registrationModel.ddBranch : "");
            values.put(RegistrationTable.Cols.LATITUDE, registrationModel.latitude != null ? registrationModel.latitude : "");
            values.put(RegistrationTable.Cols.LONGITUDE, registrationModel.longitude != null ? registrationModel.longitude : "");
            values.put(RegistrationTable.Cols.CONNECTIVITY, registrationModel.connectivity != null ? registrationModel.connectivity : "");
            values.put(RegistrationTable.Cols.IS_NSC_NEW, registrationModel.isNscNew != null ? registrationModel.isNscNew : "");
            values.put(RegistrationTable.Cols.IMAGE_COUNT, registrationModel.imageCount != null ? registrationModel.imageCount : "");
            values.put(RegistrationTable.Cols.IMAGE_COUNT_ADD, registrationModel.imageCountAdd != null ? registrationModel.imageCountAdd : "");
            values.put(RegistrationTable.Cols.VENDOR_ID, registrationModel.vendorId != null ? registrationModel.vendorId : "");
            values.put(RegistrationTable.Cols.FIELD_FORCE_ID, registrationModel.fieldForceId != null ? registrationModel.fieldForceId : "");
            values.put(RegistrationTable.Cols.DOCUMENTS, registrationModel.documents != null ? registrationModel.documents : "");
            values.put(RegistrationTable.Cols.DOCUMENTS_ADD, registrationModel.documentsAdd != null ? registrationModel.documentsAdd : "");
            values.put(RegistrationTable.Cols.FILE_0, registrationModel.File0 != null ? registrationModel.File0.image : "");
            values.put(RegistrationTable.Cols.FILE_1, registrationModel.File1 != null ? registrationModel.File1.image : "");
            values.put(RegistrationTable.Cols.FILE_ADD_PROOF_0, registrationModel.FileAddProof0 != null ? registrationModel.FileAddProof0.image : "");
            values.put(RegistrationTable.Cols.FILE_ADD_PROOF_1, registrationModel.FileAddProof1 != null ? registrationModel.FileAddProof1.image : "");
            values.put(RegistrationTable.Cols.FILE_NOC_PROOF, registrationModel.FileNocProof != null ? registrationModel.FileNocProof.image : "");
            values.put(RegistrationTable.Cols.AREA_NAME, registrationModel.areaName != null ? registrationModel.areaName : "");
            values.put(RegistrationTable.Cols.FLOOR_NO, registrationModel.floorNo != null ? registrationModel.floorNo : "");
            values.put(RegistrationTable.Cols.PLOT_NO, registrationModel.plotNo != null ? registrationModel.plotNo : "");
            values.put(RegistrationTable.Cols.WING, registrationModel.wing != null ? registrationModel.wing : "");
            values.put(RegistrationTable.Cols.ROAD_NO, registrationModel.roadNo != null ? registrationModel.roadNo : "");
            values.put(RegistrationTable.Cols.LANDMARK, registrationModel.landmark != null ? registrationModel.landmark : "");
            values.put(RegistrationTable.Cols.DISTRICT, registrationModel.district != null ? registrationModel.district : "");
            values.put(RegistrationTable.Cols.SOCIETY_NAME, registrationModel.societyName != null ? registrationModel.societyName : "");
            values.put(RegistrationTable.Cols.CARD_STATUS, cardStatus);
            values.put(RegistrationTable.Cols.FIELD_CHEQUE_DD, registrationModel.FileChequeDD != null ? registrationModel.FileChequeDD.image : "");
            values.put(RegistrationTable.Cols.FILE_SIGN, registrationModel.FileSign != null ? registrationModel.FileSign.image : "");
            values.put(RegistrationTable.Cols.FILE_CONSUMER_PHOTO, registrationModel.FileConsumerPhoto != null ? registrationModel.FileConsumerPhoto.image : "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    private static long saveRegistrations(Context context, Uri table, ContentValues values, String condition, Activity activity) {
        ContentResolver resolver = context.getContentResolver();
        try {
            Uri url = resolver.insert(table, values);
            String name = String.valueOf(url);
            String[] items1 = name.split("\\/");
            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show();
            return Integer.parseInt(items1[4]);
        } catch (Exception e) {
            Toast.makeText(context, "Failed to save reading", Toast.LENGTH_LONG).show();
        }
        return 0;
    }



    public static ArrayList<RegistrationModel> getRegistration(Context context, String userId, int limit, String cardStatus) {
        String condition = RegistrationTable.Cols.USER_LOGIN_ID + "='" + userId + "' AND "
                + RegistrationTable.Cols.CARD_STATUS + "='" +cardStatus+"'";

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(RegistrationTable.CONTENT_URI, null,
                condition, null, RegistrationTable.Cols.ID + " DESC " + " LIMIT " + limit);
        ArrayList<RegistrationModel> meterReadings = getRegistrationsFromCursor(context, cursor);
        if (cursor != null) {
            cursor.close();
        }
        return meterReadings;
    }

    private static ArrayList<RegistrationModel> getRegistrationsFromCursor(Context context, Cursor cursor) {
        ArrayList<RegistrationModel> registrationModels = null;
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                RegistrationModel registrationModel;
                registrationModels = new ArrayList<RegistrationModel>();
                while (!cursor.isAfterLast()) {
                    registrationModel = getRegistrationFromCursor(context, cursor);
                    registrationModels.add(registrationModel);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return registrationModels;
    }

    private static RegistrationModel getRegistrationFromCursor(Context context, Cursor cursor) {

        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Date date = new Date();
        String dateValue = dateFormat.format(date);
        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.id = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) : "";
        registrationModel.enquiryId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ENQUIRY_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ENQUIRY_ID)) : "";
        registrationModel.consumerCategory = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_CATEGORY)) : "";
        registrationModel.consumerSubCategory = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_SUB_CATEGORY)) : "";
        registrationModel.state = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.STATE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.STATE)) : "";
        registrationModel.name = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.NAME)) : "";
        registrationModel.addhaar = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ADHAAR)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ADHAAR)) : "";
        registrationModel.emailId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.EMAIL_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.EMAIL_ID)) : "";
        registrationModel.flatNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLAT_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLAT_NO)) : "";
        registrationModel.buildingName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BUILDING_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BUILDING_NAME)) : "";
        registrationModel.location = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LOCATION)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LOCATION)) : "";
        registrationModel.area = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA)) : "";
        registrationModel.city = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CITY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CITY)) : "";
        registrationModel.pincode = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PINCODE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PINCODE)) : "";
        registrationModel.mobile = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MOBILE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.MOBILE)) : "";
        registrationModel.premise = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PREMISE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PREMISE)) : "";
        registrationModel.enquiryNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ENQUIRY_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ENQUIRY_NO)) : "";
        registrationModel.consumerNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONSUMER_NO)) : "";
        registrationModel.saveType = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SAVE_TYPE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SAVE_TYPE)) : "";
        registrationModel.schemeName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_NAME)) : "";
        registrationModel.schemeAmount = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_AMOUNT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SCHEME_AMOUNT)) : "";
        registrationModel.paymentMethod = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PAYMENT_METHOD)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PAYMENT_METHOD)) : "";
        registrationModel.chequeNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_NO)) : "";
        registrationModel.chequeBranch = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_BRANCH)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_BRANCH)) : "";
        registrationModel.chequeDate = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_DATE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_DATE)) : "";
        registrationModel.chequeBank = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_BANK)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_BANK)) : "";
        registrationModel.chequeMicr = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_MICR)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CHEQUE_MICR)) : "";
        registrationModel.ddNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_NO)) : "";
        registrationModel.bankName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BANK_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.BANK_NAME)) : "";
        registrationModel.ddDate = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_DATE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_DATE)) : "";
        registrationModel.ddMicr = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_MICR)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_MICR)) : "";
        registrationModel.ddBranch = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_BRANCH)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DD_BRANCH)) : "";
        registrationModel.latitude = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LATITUDE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LATITUDE)) : "";
        registrationModel.longitude = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LONGITUDE)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LONGITUDE)) : "";
        registrationModel.connectivity = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONNECTIVITY)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.CONNECTIVITY)) : "";
        registrationModel.isNscNew = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.IS_NSC_NEW)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.IS_NSC_NEW)) : "";
        registrationModel.imageCount = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.IMAGE_COUNT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.IMAGE_COUNT)) : "";
        registrationModel.imageCountAdd = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.IMAGE_COUNT_ADD)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.IMAGE_COUNT_ADD)) : "";
        registrationModel.vendorId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.VENDOR_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.VENDOR_ID)) : "";
        registrationModel.fieldForceId = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FIELD_FORCE_ID)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FIELD_FORCE_ID)) : "";
        registrationModel.documents = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DOCUMENTS)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DOCUMENTS)) : "";
        registrationModel.documentsAdd = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DOCUMENTS_ADD)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DOCUMENTS_ADD)) : "";
        registrationModel.areaName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.AREA_NAME)) : "";
        registrationModel.registrationNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.REGISTRATION_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.REGISTRATION_NO)) : "";
        registrationModel.floorNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLOOR_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FLOOR_NO)) : "";
        registrationModel.plotNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PLOT_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.PLOT_NO)) : "";
        registrationModel.wing = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.WING)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.WING)) : "";
        registrationModel.roadNo = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ROAD_NO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ROAD_NO)) : "";
        registrationModel.landmark = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LANDMARK)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.LANDMARK)) : "";
        registrationModel.district = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DISTRICT)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.DISTRICT)) : "";
        registrationModel.societyName = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SOCIETY_NAME)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.SOCIETY_NAME)) : "";





        ImageModel id01 = new ImageModel();
        id01.name = "nsc_id01_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        id01.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_0)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_0)) : "";
        id01.content_type = "image/jpeg";
        registrationModel.File0 = id01;

        ImageModel id02 = new ImageModel();
        id02.name = "nsc_id02_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        id02.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_1)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_1)) : "";
        id02.content_type = "image/jpeg";
        registrationModel.File1 = id02;

        ImageModel add01 = new ImageModel();
        add01.name = "nsc_add01_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        add01.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_ADD_PROOF_0)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_ADD_PROOF_0)) : "";
        add01.content_type = "image/jpeg";
        registrationModel.FileAddProof0 = add01;

        ImageModel add02 = new ImageModel();
        add02.name = "nsc_add02_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        add02.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_ADD_PROOF_1)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_ADD_PROOF_1)) : "";
        add02.content_type = "image/jpeg";
        registrationModel.FileAddProof1 = add02;

        ImageModel chequedd = new ImageModel();
        chequedd.name = "nsc_chequedd_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        chequedd.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FIELD_CHEQUE_DD)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FIELD_CHEQUE_DD)) : "";
        chequedd.content_type = "image/jpeg";
        registrationModel.FileChequeDD = chequedd;

        ImageModel sign = new ImageModel();
        sign.name = "nsc_sign_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        sign.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_SIGN)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_SIGN)) : "";
        sign.content_type = "image/jpeg";
        registrationModel.FileSign = sign;


        ImageModel noc = new ImageModel();
        noc.name = "nsc_noc_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        noc.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_NOC_PROOF)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_NOC_PROOF)) : "";
        noc.content_type = "image/jpeg";
        registrationModel.FileNocProof = noc;



        ImageModel consumerPhoto = new ImageModel();
        consumerPhoto.name = "nsc_consumerphoto_" + AppPreferences.getInstance(context).getString(AppConstants.EMP_ID, "")
                + "_" + dateValue + "_" + cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.ID)) + ".JPEG";
        consumerPhoto.image = cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_CONSUMER_PHOTO)) != null ? cursor.getString(cursor.getColumnIndex(RegistrationTable.Cols.FILE_CONSUMER_PHOTO)) : "";
        consumerPhoto.content_type = "image/jpeg";
        registrationModel.FileConsumerPhoto = consumerPhoto;

        return registrationModel;
    }




    public static void deleteRegistration(Context context, String id, String user_login_id) {
        try {
            String condition = RegistrationTable.Cols.USER_LOGIN_ID + "='" + user_login_id + "' AND " +
                    RegistrationTable.Cols.ID + "='" + id + "'";
            context.getContentResolver().delete(RegistrationTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteRejectedRegistration(Context context, String registration_no, String user_login_id) {
        try {
            String condition = RegistrationTable.Cols.USER_LOGIN_ID + "='" + user_login_id + "' AND " +
                    RegistrationTable.Cols.REGISTRATION_NO + "='" + registration_no + "'";
            context.getContentResolver().delete(RegistrationTable.CONTENT_URI, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}