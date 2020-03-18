package com.fieldforce.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.models.Consumer;
import com.fieldforce.models.UserProfileModel;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller {

    private Context mContext;
    private EditText edtID, edtPassword;
    private Button btnLogin;
    private String userId, userPass;
    private static int z = 0;
    private ImageView imageClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        edtID = findViewById(R.id.edt_id);
        edtID.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        imageClick = findViewById(R.id.imageClick);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CommonUtility.askForPermissions(mContext, App.getInstance().permissions);
        }

        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCounts();
            }
        });

    }

    private void checkCounts() {
        z++;
        if (z == 10) {
            z = 0;
            showDialogForAndroidID(mContext);
        }
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
//            Log.d("xxxxxxxxxxxxxx", " " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            isValidate();
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public void doLogin() {

        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            showLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", userId);
                jsonObject.put("password", userPass);
                Log.d("2222222222",""+Build.VERSION.SDK_INT);


                /*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    @SuppressLint("MissingPermission") String imeiNumber = telephonyManager.getDeviceId();
                    jsonObject.put("imei_no", imeiNumber);
                } else {
                    String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    jsonObject.put("imei_no", androidId);
                }*/
                jsonObject.put("imei_no","866121043007035");
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.LOGIN_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.LOGIN_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_LONG).show();
        }
    }

    public void isValidate() {
        userId = edtID.getText().toString().trim();
        userPass = edtPassword.getText().toString();
        if (!TextUtils.isEmpty(userId)) {
            if (!TextUtils.isEmpty(userPass)) {
                if (userPass.length() >= 6)
                    doLogin();
                else
                    edtPassword.setError(getString(R.string.password_should_have_at_least_characters));
            } else
                edtPassword.setError(getString(R.string.please_enter_password));
        } else {
            edtID.setError(getString(R.string.please_enter_user_id));
        }
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.LOGIN_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.responsedata != null) {
                            try {
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.user_id = jsonResponse.responsedata.getUser_id();
                                userProfileModel.emp_id = jsonResponse.responsedata.getEmp_id();
                                userProfileModel.user_name = jsonResponse.responsedata.getUser_name();
                                userProfileModel.city = jsonResponse.responsedata.getCity();
                                userProfileModel.contact_no = jsonResponse.responsedata.getContact_no();
                                userProfileModel.address = jsonResponse.responsedata.getAddress();
//                                userProfileModel.emp_type = jsonResponse.responsedata.getEmp_type();
                                userProfileModel.email_id = jsonResponse.responsedata.getEmail_id();
                                userProfileModel.name = jsonResponse.responsedata.getName();
                                userProfileModel.stateId = jsonResponse.responsedata.getStateId();
                                userProfileModel.state = jsonResponse.responsedata.getState();
                                userProfileModel.cityId = jsonResponse.responsedata.getCityId();
                                userProfileModel.vendorId = jsonResponse.responsedata.getVendorId();
                                userProfileModel.fieldForceId = jsonResponse.responsedata.getFieldForceId();
                                userProfileModel.userType = jsonResponse.responsedata.getUserType();
                                userProfileModel.districtId = jsonResponse.responsedata.getDistrictId();
                                userProfileModel.district = jsonResponse.responsedata.getDistrict();


                                DatabaseManager.saveLoginDetails(mContext, userProfileModel);

                                AppPreferences.getInstance(mContext).putString(AppConstants.AUTH_TOKEN, jsonResponse.responsedata.getToken());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_NAME, jsonResponse.responsedata.getUser_name());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_CITY, jsonResponse.responsedata.getCity());
                                AppPreferences.getInstance(mContext).putString(AppConstants.EMP_ID, jsonResponse.responsedata.getEmp_id());
                                AppPreferences.getInstance(mContext).putString(AppConstants.MOBILE_NO, jsonResponse.responsedata.getContact_no());
                                AppPreferences.getInstance(mContext).putString(AppConstants.NAME, jsonResponse.responsedata.getName());
                                AppPreferences.getInstance(mContext).putString(AppConstants.STATE, jsonResponse.responsedata.getState());
                                AppPreferences.getInstance(mContext).putString(AppConstants.CITY, jsonResponse.responsedata.getCity());
                                AppPreferences.getInstance(mContext).putString(AppConstants.STATE_ID, jsonResponse.responsedata.getStateId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.CITY_ID, jsonResponse.responsedata.getCityId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.VENDOR_ID, jsonResponse.responsedata.getVendorId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.FIELD_FORCE_ID, jsonResponse.responsedata.getFieldForceId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_TYPE, jsonResponse.responsedata.getUserType());
                                AppPreferences.getInstance(mContext).putString(AppConstants.DISTRICT_ID, jsonResponse.responsedata.getDistrictId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_DISTRICT, jsonResponse.responsedata.getDistrict());

//                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);
//                                AppPreferences.getInstance(mContext).putString(AppConstants.PROFILE_IMAGE_URL, jsonResponse.responsedata.getProfile_image());


                                AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.all));
                                AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "0");

                                getCategory();
                                getSubCategory();
                                getBankNames();
                                getArea();
                                //getWardList();
                                getPinCode();
                                getDocumentList();
                                getPaymentScheme();
                                //getLocation();
                                //getLandmark();

                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_CONSUMER_CATEGORY_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.categoary_list != null) {
                        ArrayList<Consumer> categoryList = new ArrayList<>();
                        categoryList.addAll(jsonResponse.categoary_list);
                        DatabaseManager.saveCategory(mContext, categoryList);
                        //getSubCategory();

                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                                dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_CONSUMER_SUB_CATEGORY_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.consumer_subcategoary_List != null) {
                        ArrayList<Consumer> subCategoryList = new ArrayList<>();
                        subCategoryList.addAll(jsonResponse.consumer_subcategoary_List);
                        DatabaseManager.saveSubCategory(mContext, subCategoryList);
                        //getArea();

                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                                dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_AREA_SP: {
                if (jsonResponse != null) {
                    if (jsonResponse.area_list != null) {
                        ArrayList<Consumer> consumer = new ArrayList<>();
                        consumer.addAll(jsonResponse.area_list);
                        DatabaseManager.saveArea(mContext, consumer);
                        //getWardList();

                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_WARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.ward_list != null) {
                        ArrayList<Consumer> wardlist = new ArrayList<>();
                        wardlist.addAll(jsonResponse.ward_list);
                        DatabaseManager.saveWard(mContext, wardlist, "");
                        //getPinCode();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                                dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_PIN_CODE: {
                if (jsonResponse != null) {
                    if (jsonResponse.pincode_list != null) {
                        ArrayList<Consumer> pincode = new ArrayList<>();
                        pincode.addAll(jsonResponse.pincode_list);
                        DatabaseManager.savePincode(mContext, pincode, "");
                        //getDocumentList();

                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                                dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_DOCUMENT_LIST: {
                if (jsonResponse != null) {
                    if (jsonResponse.document_list != null || jsonResponse.document_address_list != null) {
                        ArrayList<Consumer> documnetList = new ArrayList<>();
                        ArrayList<Consumer> adddocumnetList = new ArrayList<>();
                        documnetList.addAll(jsonResponse.document_list);
                        adddocumnetList.addAll(jsonResponse.document_address_list);
                        DatabaseManager.saveIDProof(mContext, documnetList, "type");
                        DatabaseManager.saveAddProof(mContext, adddocumnetList);
                        //getPaymentScheme();
                        //dismissLoadingDialog();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                                dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_PAYMENT_SCHEMES: {
                if (jsonResponse != null) {
                    if (jsonResponse.scheme_list != null) {
                        ArrayList<Consumer> paymentList = new ArrayList<>();
                        paymentList.addAll(jsonResponse.scheme_list);
                        DatabaseManager.savePaymentScheme(mContext, paymentList);
                        //getBankNames();
                        dismissLoadingDialog();


                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                                dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_BANK_NAME_URL: {
                if (jsonResponse.result != null) {
                    if (jsonResponse.banklist != null) {
                        ArrayList<Consumer> bank = new ArrayList<>();
                        bank.addAll(jsonResponse.banklist);
                        DatabaseManager.saveBankNames(mContext, bank);
                        //dismissLoadingDialog();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_LOCATION_REGISTRATION: {
                if (jsonResponse.result != null) {
                    if (jsonResponse.location_list != null) {
                        ArrayList<Consumer> locationArrayList = new ArrayList<>();
                        locationArrayList.addAll(jsonResponse.location_list);
                        DatabaseManager.saveLocation(mContext, locationArrayList, "");
                        //dismissLoadingDialog();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_LANDMARK: {
                if (jsonResponse.result != null) {
                    if (jsonResponse.landmark_list != null) {
                        ArrayList<Consumer> landmarkArrayList = new ArrayList<>();
                        landmarkArrayList.addAll(jsonResponse.landmark_list);
                        DatabaseManager.saveLandmark(mContext, landmarkArrayList, "");
                        //dismissLoadingDialog();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
//                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;

        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        switch (label) {
            case ApiConstants.LOGIN_URL: {
                dismissLoadingDialog();
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;

        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        switch (label) {
            case ApiConstants.LOGIN_URL: {
                dismissLoadingDialog();
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();

            }
            break;
        }
    }

    public void onBackPressed() {
        CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.do_you_want_to_close_exit_app_now),
                getString(R.string.main_activity), false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
        customDialog.setCancelable(false);
    }


    private void getArea() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            //showLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("city_id", AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, ""));
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_AREA_SP, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_AREA_SP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    public void getBankNames() {

        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            //showLoadingDialog();
            try {
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_BANK_NAME_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_BANK_NAME_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getPaymentScheme() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            //showLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_PAYMENT_SCHEMES, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_PAYMENT_SCHEMES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getDocumentList() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            //showLoadingDialog();
            try {
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_DOCUMENT_LIST, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_DOCUMENT_LIST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }


    private void getWardList() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                //showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("city_id", AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, ""));
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_WARD, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_WARD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getCategory() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            //showLoadingDialog();
            try {
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_CONSUMER_CATEGORY_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CONSUMER_CATEGORY_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getSubCategory() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {

                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_CONSUMER_SUB_CATEGORY_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CONSUMER_SUB_CATEGORY_URL);
                //showLoadingDialog();
                //JSONObject jsonObject = new JSONObject();
                //jsonObject.put("city_id",  AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, ""));
                /*JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_CONSUMER_SUB_CATEGORY_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CONSUMER_SUB_CATEGORY_URL);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getPinCode() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                //showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("city_id", AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, ""));
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_PIN_CODE, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_PIN_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getLocation() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                //showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("city_id", AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, ""));
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_LOCATION_REGISTRATION, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_LOCATION_REGISTRATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getLandmark() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                //showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("city_id", AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, ""));
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_LANDMARK, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_LANDMARK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }


    public void showDialogForAndroidID(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dailog_android_id);
        final TextView txtAndroidId;
        txtAndroidId = dialog.findViewById(R.id.android_id);
        Button ok = dialog.findViewById(R.id.btn_ok);
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setGravity(/*Gravity.TOP | */Gravity.CENTER);
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("1111111111",""+Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            dialog.show();
            txtAndroidId.setText(androidId);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);

    }
}
