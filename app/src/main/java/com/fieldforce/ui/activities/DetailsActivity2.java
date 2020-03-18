package com.fieldforce.ui.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.adapters.AssetParametersAdapter;
import com.fieldforce.ui.adapters.AssetReadingParametersAdapter;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.LocationManagerReceiver;
import com.fieldforce.utility.MultipartUtility;
import com.fieldforce.utility.SignatureView;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class DetailsActivity2 extends ParentActivity implements View.OnClickListener, ApiServiceCaller {

    private Context mContext;
    private String ASSET_IMAGE_DIRECTORY_NAME = ".FF-Images", assetImageName = "images", imgURL = "";
    private Typeface mFontBold, mFontMedium, mFontRegular;
    private NestedScrollView nsvAcceptance, nsvWork, nsvFS;
    private RecyclerView recyclerViewSOP, recyclerViewReadingParameters;
    private TextView txtWorkName, txtExpectedDate, txtName, txtNumber, txtAddress, txtJobDesc,
            txtMore, txtAccepted, txtWorkOrder, txtFollowSteps, txtInstructions, txtTitle, txtSubTitle,
            txtUploadImage, txtOtherParameter, txtAddSignature;
    private Button btnAccept, btnReject, btnNext, btnComplete, btnBack, btnClear, btnUpload;
    private ImageView imgAcceptance, imgWorkInactive, imgFS, imgBack, imgModule, imgTakePhoto, imgPreview;
    private EditText edtRemark, edtDate, edtActualFault;
    private View viewDate;
    private RelativeLayout relativeDate;
    private String userId, jobId, screenName, sopTimeStamp, token;
    private View viewBottomAcceptance, viewBottomWork, viewBottomFS;
    private LinearLayout linearJobDescription, linearMaterialList, linearInstructions,
            linearAcceptance, linearWork, linearFS, linearAcceptRejectButtons, linearReadingParameter,
            linearLayoutSignView, viewSignature;
    private TodayModel todayModel, newTodayModel;
    private Date startTime, endTime;

    private LocationManagerReceiver mLocationManagerReceiver;
    private Location mLocation;

    private File filePhoto = null, fileSign = null;
    private SignatureView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_2);

        mContext = this;
        token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
        newTodayModel = new TodayModel();

        startTime = CommonUtility.getCurrentTime();
        mFontBold = App.getMontserratBoldFont();
        mFontMedium = App.getMontserratMediumFont();
        mFontRegular = App.getMontserratRegularFont();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nsvAcceptance = findViewById(R.id.nsv_acceptance);
        nsvWork = findViewById(R.id.nsv_work);
        nsvFS = findViewById(R.id.nsv_fs);

        linearJobDescription = findViewById(R.id.linear_job_description);
        linearMaterialList = findViewById(R.id.linear_material_list);
        linearInstructions = findViewById(R.id.linear_instructions);
        linearAcceptRejectButtons = findViewById(R.id.linear_buttons);
        linearReadingParameter = findViewById(R.id.linear_reading_parameter);
        linearLayoutSignView = findViewById(R.id.linear_signature_view);
        viewSignature = findViewById(R.id.view_signature);
        signatureView = new SignatureView(mContext);
        viewSignature.addView(signatureView);

        linearAcceptance = findViewById(R.id.linear_acceptance);
        linearAcceptance.setOnClickListener(this);
        linearWork = findViewById(R.id.linear_work);
        linearWork.setOnClickListener(this);
        linearFS = findViewById(R.id.linear_fs);
        linearFS.setOnClickListener(this);

        txtWorkName = findViewById(R.id.txt_work_name);
        txtExpectedDate = findViewById(R.id.txt_expected_date);
        txtName = findViewById(R.id.txt_name);
        txtNumber = findViewById(R.id.txt_number);
        txtAddress = findViewById(R.id.txt_address);
        txtJobDesc = findViewById(R.id.txt_job_desc);
        txtMore = findViewById(R.id.txt_more);
        txtMore.setOnClickListener(this);
        txtAccepted = findViewById(R.id.txt_accepted);
        txtWorkOrder = findViewById(R.id.txt_work_order);
        txtFollowSteps = findViewById(R.id.txt_follow_steps);
        txtInstructions = findViewById(R.id.txt_instructions);
        txtTitle = findViewById(R.id.txt_title);
        txtSubTitle = findViewById(R.id.txt_sub_title);
        txtUploadImage = findViewById(R.id.txt_upload_image);
        txtOtherParameter = findViewById(R.id.txt_other_parameters);
        txtAddSignature = findViewById(R.id.txt_add_sign);

        txtWorkName.setTypeface(mFontBold);
        txtExpectedDate.setTypeface(mFontRegular);
        txtName.setTypeface(mFontMedium);
        txtNumber.setTypeface(mFontRegular);
        txtAddress.setTypeface(mFontRegular);
        txtJobDesc.setTypeface(mFontRegular);
        txtMore.setTypeface(mFontRegular);
        txtAccepted.setTypeface(mFontMedium);
        txtWorkOrder.setTypeface(mFontBold);
        txtFollowSteps.setTypeface(mFontMedium);
        txtInstructions.setTypeface(mFontBold);
        txtTitle.setTypeface(mFontBold);
        txtSubTitle.setTypeface(mFontMedium);
        txtUploadImage.setTypeface(mFontMedium);
        txtOtherParameter.setTypeface(mFontMedium);
        txtAddSignature.setTypeface(mFontMedium);

        btnAccept = findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(this);
        btnReject = findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(this);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(this);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
        btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(this);

        btnAccept.setTypeface(mFontMedium);
        btnReject.setTypeface(mFontMedium);
        btnNext.setTypeface(mFontMedium);
        btnComplete.setTypeface(mFontMedium);
        btnClear.setTypeface(mFontMedium);
        btnUpload.setTypeface(mFontMedium);


        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgModule = findViewById(R.id.img_module);
        imgAcceptance = findViewById(R.id.img_acceptance);
        imgWorkInactive = findViewById(R.id.img_work_inactive);
        imgFS = findViewById(R.id.img_fs);
        imgTakePhoto = findViewById(R.id.img_capture);
        imgTakePhoto.setOnClickListener(this);
        imgPreview = findViewById(R.id.img_preview);

        edtRemark = findViewById(R.id.edt_remark);
        edtRemark.setTypeface(mFontRegular);
        edtDate = findViewById(R.id.edt_date);
        edtDate.setTypeface(mFontRegular);
        edtDate.setEnabled(false);
        viewDate = findViewById(R.id.view_date);
        viewDate.setOnClickListener(this);
        relativeDate = findViewById(R.id.relative_date);
        relativeDate.setVisibility(View.GONE);
        edtActualFault = findViewById(R.id.edt_actual_fault);

        edtActualFault.setTypeface(mFontRegular);


        viewBottomAcceptance = findViewById(R.id.bottom_view_acceptance);
        viewBottomWork = findViewById(R.id.bottom_view_work);
        viewBottomFS = findViewById(R.id.bottom_view_fs);

        recyclerViewSOP = findViewById(R.id.recycler_view_parameters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewSOP.setLayoutManager(layoutManager);
        recyclerViewReadingParameters = findViewById(R.id.recycler_view_reading_parameters);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewReadingParameters.setLayoutManager(layoutManager2);

        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);

        mLocationManagerReceiver = new LocationManagerReceiver(mContext);
        mLocation = mLocationManagerReceiver.getLocation();

        Intent intent = getIntent();
        if (intent != null) {
            screenName = intent.getStringExtra(AppConstants.COMING_FROM);
            jobId = intent.getStringExtra(AppConstants.JOB_ID);
            todayModel = (TodayModel) intent.getSerializableExtra(AppConstants.MODEL);
        }

        setValues();
    }

    private void setValues() {
        if (screenName.equals(getString(R.string.services))) {
            imgModule.setImageResource(R.drawable.ic_action_site_verification);
            txtTitle.setText(getString(R.string.id) + " #" + todayModel.getServiceNumber());
            txtSubTitle.setVisibility(View.GONE);
            txtWorkName.setText(getString(R.string.services));
            txtExpectedDate.setText(getString(R.string.type) + ": " + todayModel.getServiceType() + "\n\n" +
                    getString(R.string.expected_delivery_date) + todayModel.getDueDate());
            txtName.setText(todayModel.getConsumerName());
            txtNumber.setText(todayModel.getMobileNumber());
            txtAddress.setText(todayModel.getAddress());

            linearJobDescription.setVisibility(View.GONE);
            linearMaterialList.setVisibility(View.GONE);
            linearInstructions.setVisibility(View.GONE);

            isJobAccepted(todayModel.getAcceptstatus());

        } else if (screenName.equals(getString(R.string.complaint))) {
            imgModule.setImageResource(R.drawable.ic_action_meter_installation);
            txtTitle.setText(getString(R.string.id) + " #" + todayModel.getComplaintNumber());
            txtSubTitle.setVisibility(View.GONE);
            txtWorkName.setText(getString(R.string.complaint));
            txtExpectedDate.setText(getString(R.string.type) + ": " + todayModel.getComplaintType() + "\n\n" +
                    getString(R.string.expected_delivery_date) + todayModel.getDueDate());
            txtName.setText(todayModel.getConsumerName());
            txtNumber.setText(todayModel.getMobileNumber());
            txtAddress.setText(todayModel.getAddress());
            edtActualFault.setVisibility(View.VISIBLE);


            linearJobDescription.setVisibility(View.GONE);
            linearMaterialList.setVisibility(View.GONE);
            linearInstructions.setVisibility(View.GONE);

            isJobAccepted(todayModel.getAcceptstatus());
        }
    }

    private void isJobAccepted(String isJobAccepted) {
        if (isJobAccepted.equals(AppConstants.CARD_STATUS_ACCEPTED)) {
            linearAcceptRejectButtons.setVisibility(View.GONE);
            txtAccepted.setText(getString(R.string.accepted));
            imgAcceptance.setImageResource(R.drawable.ic_action_accepted);
            imgWorkInactive.setImageResource(R.drawable.ic_action_work);
            imgFS.setImageResource(R.drawable.ic_action_fs);

            nsvAcceptance.setVisibility(View.GONE);
            nsvWork.setVisibility(View.VISIBLE);
            nsvFS.setVisibility(View.GONE);

            viewBottomAcceptance.setVisibility(View.GONE);
            viewBottomWork.setVisibility(View.VISIBLE);
            viewBottomFS.setVisibility(View.GONE);

            linearWork.setClickable(true);
            linearFS.setClickable(true);

            if (screenName.equals(getString(R.string.complaint)))
                setUIComponents();
            else
                getDetails();
        } else {
            nsvAcceptance.setVisibility(View.VISIBLE);
            nsvWork.setVisibility(View.GONE);
            nsvFS.setVisibility(View.GONE);

            linearAcceptRejectButtons.setVisibility(View.VISIBLE);
            linearWork.setClickable(false);
            linearFS.setClickable(false);
        }
    }

    private void nextNavigation(String status) {
        if (status.equalsIgnoreCase(AppConstants.CARD_STATUS_ACCEPTED)) {
            getDetails();
            setUIComponents();
        } else {
            finish();
        }
    }

    private void setUIComponents() {
        if (screenName.equals(getString(R.string.complaint))) {
            nsvAcceptance.setVisibility(View.GONE);
            nsvWork.setVisibility(View.GONE);
            nsvFS.setVisibility(View.VISIBLE);
            txtAccepted.setText(getString(R.string.accepted));
            imgAcceptance.setImageResource(R.drawable.ic_action_accepted);
            imgWorkInactive.setImageResource(R.drawable.ic_action_work_inactive);
            imgFS.setImageResource(R.drawable.ic_action_fs);

            linearAcceptRejectButtons.setVisibility(View.GONE);
            linearWork.setClickable(false);
            linearFS.setClickable(true);

            viewBottomAcceptance.setVisibility(View.GONE);
            viewBottomWork.setVisibility(View.GONE);
            viewBottomFS.setVisibility(View.VISIBLE);
        } else {
            nsvAcceptance.setVisibility(View.GONE);
            nsvWork.setVisibility(View.VISIBLE);
            nsvFS.setVisibility(View.GONE);
            txtAccepted.setText(getString(R.string.accepted));
            imgAcceptance.setImageResource(R.drawable.ic_action_accepted);
            imgWorkInactive.setImageResource(R.drawable.ic_action_work);

            linearAcceptRejectButtons.setVisibility(View.GONE);
            linearWork.setClickable(true);
            linearFS.setClickable(true);

            viewBottomAcceptance.setVisibility(View.GONE);
            viewBottomWork.setVisibility(View.VISIBLE);
            viewBottomFS.setVisibility(View.GONE);
        }
    }

    private boolean onNext() {
        boolean returnValue = false;
        if (AssetParametersAdapter.hashMap.containsValue("true")) {
            if (linearReadingParameter.getVisibility() == View.VISIBLE) {
                if (AssetReadingParametersAdapter.hashMap.size() > 0) {
                    sopTimeStamp = CommonUtility.getCurrentDateTime();
                    returnValue = true;
                } else {
                    Toast.makeText(mContext, getString(R.string.error_reading_parameters), Toast.LENGTH_SHORT).show();
                }
            } else {
                returnValue = true;
            }
        } else {
            Toast.makeText(mContext, getString(R.string.select_work_order_details_followed), Toast.LENGTH_SHORT).show();
        }
        return returnValue;
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
        } else if (v == linearAcceptance) {
            nsvAcceptance.setVisibility(View.VISIBLE);
            nsvWork.setVisibility(View.GONE);
            nsvFS.setVisibility(View.GONE);

            viewBottomAcceptance.setVisibility(View.VISIBLE);
            viewBottomWork.setVisibility(View.GONE);
            viewBottomFS.setVisibility(View.GONE);
        } else if (v == linearWork) {
            nsvAcceptance.setVisibility(View.GONE);
            nsvWork.setVisibility(View.VISIBLE);
            nsvFS.setVisibility(View.GONE);

            viewBottomAcceptance.setVisibility(View.GONE);
            viewBottomWork.setVisibility(View.VISIBLE);
            viewBottomFS.setVisibility(View.GONE);
        } else if (v == linearFS) {
            if (screenName.equals(getString(R.string.complaint))) {
                afterOnNext();
            } else {
                if (onNext())
                    afterOnNext();
            }
        } else if (v == btnAccept) {
            apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_ACCEPTED);
        } else if (v == btnReject) {
            apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_REJECTED);
        } else if (v == btnNext) {
            if (screenName.equals(getString(R.string.complaint))) {
                afterOnNext();
            } else {
                if (onNext())
                    afterOnNext();
            }
        } else if (v == btnComplete) {
            onComplete();
        } else if (v == imgTakePhoto) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri file = getOutputMediaFileUri(ASSET_IMAGE_DIRECTORY_NAME, assetImageName);
            List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;

                mContext.grantUriPermission(packageName, file, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intent, AppConstants.CAMERA_RESULT_CODE);
        } else if (v == viewDate) {
            // Get Current Date
            Calendar calendar = Calendar.getInstance();
            int mYear, mMonth, mDay;
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -90);
            Date newDate = c.getTime();

            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, 365);
            Date maxDate = c1.getTime();

            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    edtDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(newDate.getTime());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
            datePickerDialog.show();
        } else if (v == btnBack) {
            linearLayoutSignView.setVisibility(View.GONE);
        } else if (v == btnClear) {
            signatureView.clearSignature();
        } else if (v == btnUpload) {
            if (signatureView.getSignature() != null) {
                Uri tempUri = getImageUri(mContext, signatureView.getSignature());
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                fileSign = new File(getRealPathFromURI(tempUri));

                if (fileSign != null)
                    uploadData();
            } else {
                Toast.makeText(mContext, getString(R.string.add_signature), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void afterOnNext() {
        nsvAcceptance.setVisibility(View.GONE);
        nsvWork.setVisibility(View.GONE);
        nsvFS.setVisibility(View.VISIBLE);
        imgFS.setImageResource(R.drawable.ic_action_fs);

        viewBottomAcceptance.setVisibility(View.GONE);
        viewBottomWork.setVisibility(View.GONE);
        viewBottomFS.setVisibility(View.VISIBLE);
    }

    private void onComplete() {

        if (filePhoto != null) {
//            setSignatureView();
            if (screenName.equals(getText(R.string.complaint))) {
                if (!edtActualFault.getText().toString().trim().isEmpty()) {
                    uploadData();
                } else {
                    Toast.makeText(mContext, R.string.please_enter_actual_fault, Toast.LENGTH_SHORT).show();
                }
            } else {
                uploadData();
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_you_have_not_picked_image), Toast.LENGTH_SHORT).show();
        }
    }

    private void apiAcceptReject(String jobId, String screenName, String status) {
        showLoadingDialog();

        String time = CommonUtility.getCurrentDateTime();
        JSONObject jsonObject;
        Map<String, String> postParam = new HashMap<>();
        postParam.put("status", status);
        postParam.put("accept_time_stamp", time);
        postParam.put("userId", userId);

        if (screenName.equals(getString(R.string.services))) {
            postParam.put("service_id", jobId);
            jsonObject = new JSONObject(postParam);
            JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                    ApiConstants.SENT_SERVICE_ACCEPTANCE, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.SENT_SERVICE_ACCEPTANCE);
        } else if (screenName.equals(getString(R.string.complaint))) {
            postParam.put("complaint_id", jobId);
            jsonObject = new JSONObject(postParam);
            JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                    ApiConstants.SENT_COMPLAINT_ACCEPTANCE, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.SENT_COMPLAINT_ACCEPTANCE);
        }
    }

    private void getDetails() {
        if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            try {
                showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service_id", jobId);
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SERVICE_DETAILS, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_SERVICE_DETAILS);

            } catch (Exception e) {
                dismissLoadingDialog();
            }
        } else if (screenName.equals(getString(R.string.complaint))) {
            try {
                /*showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("meter_installation_id", jobId);
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_INSTALLATION_DETAILS,
                        ApiConstants.GET_METER_INSTALL_DETAIL, this, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_METER_INSTALL_DETAIL);*/

            } catch (Exception e) {
                dismissLoadingDialog();
            }
        }
    }

    private void uploadData() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            endTime = CommonUtility.getCurrentTime();
            if (screenName.equals(getString(R.string.services))) {
                showLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("service_id", jobId);
                    jsonObject.put("userId", userId);
                    jsonObject.put("sop_time_stamp", sopTimeStamp);
                    jsonObject.put("time_difference", calculateTime());
                    jsonObject.put("pole_no", "0");

                    JSONObject jsonCheckingParameter = new JSONObject();
                    HashMap<String, CharSequence> hashMapCheckingParameter = AssetParametersAdapter.hashMap;
                    List<String> checkKeyList = new ArrayList<>(hashMapCheckingParameter.keySet());
                    List<CharSequence> checkValueList = new ArrayList<>(hashMapCheckingParameter.values());

                    for (int i = 0; i < hashMapCheckingParameter.size(); i++)
                        jsonCheckingParameter.put(checkKeyList.get(i), checkValueList.get(i));
                    jsonObject.put("check_parameters", jsonCheckingParameter);

                    JSONObject jsonReadingParameter = new JSONObject();
                    HashMap<String, CharSequence> hashMap = AssetReadingParametersAdapter.hashMap;
                    List<String> keyList = new ArrayList<>(hashMap.keySet());
                    List<CharSequence> valueList = new ArrayList<>(hashMap.values());

                    for (int i = 0; i < hashMap.size(); i++)
                        jsonReadingParameter.put(keyList.get(i), valueList.get(i));
                    jsonObject.put("reading_parameters", jsonReadingParameter);

                    jsonObject.put("remark", edtRemark.getText().toString());
                    jsonObject.put("latitude", mLocation != null ? mLocation.getLatitude() : "0");
                    jsonObject.put("longitude", mLocation != null ? mLocation.getLongitude() : "0");

                    imgURL = ApiConstants.UPLOAD_IMAGE_SERVICES;

                    JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                            ApiConstants.UPLOAD_SERVICE_DETAILS, this, token);
                    App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_SERVICE_DETAILS);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (screenName.equals(getString(R.string.complaint))) {
                new UploadComplaint().execute();
            }
        }
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        if (jsonResponse != null)
            if (jsonResponse.status != null && !jsonResponse.status.isEmpty()) {

                if (jsonResponse.status.equalsIgnoreCase(AppConstants.CARD_STATUS_ACCEPTED)) {
                    newTodayModel.completedOn = CommonUtility.getCompletionDate();
                    if (screenName.equals(getString(R.string.services))) {
                        DatabaseManager.updateServiceCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllServiceCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                    } else if (screenName.equals(getString(R.string.complaint))) {
                        DatabaseManager.updateComplaintCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllComplaintCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                    }
                } else {
                    if (screenName.equals(getString(R.string.services))) {
                        DatabaseManager.deleteJobCardService(mContext, jobId, userId);
                        DatabaseManager.deleteJobCardAll(mContext, jobId, userId,String.valueOf(screenName));
                    } else if (screenName.equals(getString(R.string.complaint))) {
                        DatabaseManager.deleteJobCardComplaint(mContext, jobId, userId);
                        DatabaseManager.deleteJobCardAll(mContext, jobId, userId,String.valueOf(screenName));
                    }
                }
                nextNavigation(jsonResponse.status);
            }
        switch (label) {
            case ApiConstants.GET_SERVICE_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.service_detail.size() > 0) {
                            try {
                                dismissLoadingDialog();
                                AssetParametersAdapter assetParametersAdapter = new AssetParametersAdapter(mContext, jsonResponse.service_detail.get(0).check_list, screenName);
                                recyclerViewSOP.setAdapter(assetParametersAdapter);
                                if (jsonResponse.service_detail.get(0).readingparameter_list.size() == 0) {
                                    linearReadingParameter.setVisibility(View.GONE);
                                }
                                AssetReadingParametersAdapter readingParametersAdapter = new AssetReadingParametersAdapter(mContext, jsonResponse.service_detail.get(0).readingparameter_list);
                                recyclerViewReadingParameters.setAdapter(readingParametersAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
            break;
            case ApiConstants.UPLOAD_SERVICE_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        dismissLoadingDialog();
                        DatabaseManager.updateServiceCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllServiceCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                        Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        AssetParametersAdapter.hashMap.clear();
                        AssetReadingParametersAdapter.hashMap.clear();
                        new UploadImage().execute();
                        finish();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {

    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_call:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + todayModel.getMobileNumber()));
                startActivity(intent);
                return true;
            case R.id.action_video:

                return true;
            default:
                break;
        }
        return false;
    }

    public void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        super.onActivityResult(pRequestCode, pResultCode, pData);
        if (pRequestCode == AppConstants.CAMERA_RESULT_CODE && pResultCode == AppConstants.RESULT_OK) {
            try {
                Bitmap bitmap = getBitmapScaled(ASSET_IMAGE_DIRECTORY_NAME, assetImageName);
                imgPreview.setImageBitmap(bitmap);
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(mContext, bitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                filePhoto = new File(getRealPathFromURI(tempUri));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String calculateTime() {
        long diffMs = endTime.getTime() - startTime.getTime();
        long diffSec = diffMs / 1000;
        long min = diffSec / 60;
        long sec = diffSec % 60;

        return min + "Min" + sec + "Sec";
    }

    public Uri getOutputMediaFileUri(String dirname, String filename) {
        File file = getFilePath(dirname, filename);
        return FileProvider.getUriForFile(mContext, "com.fieldforce.file", file);
    }

    public File getFilePath(String dirname, String filename) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), dirname);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File createdFile = new File(mediaStorageDir.getPath() + File.separator + filename + ".jpg");
        return createdFile;
    }

    private Bitmap getBitmapScaled(String dirname, String filename) {
        Bitmap compressedImage = null;
        try {
            File file = getFilePath(dirname, filename);
            compressedImage = new Compressor(mContext)
                    .setMaxWidth(AppConstants.IMAGE_WIDTH)
                    .setMaxHeight(AppConstants.IMAGE_HEIGHT)
                    .setQuality(AppConstants.ONE)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToBitmap(file);
            compressedImage = Bitmap.createScaledBitmap(compressedImage, AppConstants.IMAGE_WIDTH, AppConstants.IMAGE_HEIGHT, false);
            if (compressedImage != null)
                compressedImage = CommonUtility.addWaterMarkDate(compressedImage, CommonUtility.getCurrentDateTime());
        } catch (Exception e) {
        }

        return compressedImage;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void deleteImage(File file) {
        File fileDelete = file;
        if (fileDelete.exists()) {
            if (fileDelete.delete()) {
            } else {
            }
        }
    }

    private void deleteImageFolder() {
        File folder1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + ASSET_IMAGE_DIRECTORY_NAME);
        try {
            if (folder1.exists())
                CommonUtility.deleteDir(folder1);
        } catch (Exception e) {
        }
    }

    private class UploadImage extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);

                MultipartUtility multipartUtility = new MultipartUtility(imgURL, "UTF-8");

                multipartUtility.addFormField("image_id", jobId);
                multipartUtility.addFilePart("file", filePhoto);
                if (fileSign != null)
                    multipartUtility.addFilePart(getString(R.string.file_sign), fileSign);
                multipartUtility.addFormField("Authorization", token);
                String response = multipartUtility.finish(); // response from server.


                return response;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)) {
                try {
                    JSONObject response = new JSONObject(s);

                    if (response.getString(getString(R.string.result)).equalsIgnoreCase(getString(R.string.success))) {
//                        deleteImage(filePhoto);
                        if (filePhoto != null)
                            filePhoto.delete();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UploadComplaint extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            showLoadingDialog();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {
                MultipartUtility multipartUtility = new MultipartUtility(ApiConstants.UPLOAD_COMPLAINT_DETAILS, "UTF-8");
                multipartUtility.addFormField("Authorization", token);
                multipartUtility.addFormField("userId", userId);
                multipartUtility.addFormField("ff_remark", edtRemark.getText().toString());
                multipartUtility.addFormField("actual_fault", edtActualFault.getText().toString());
                multipartUtility.addFormField("id", jobId);
                multipartUtility.addFormField("time_difference", calculateTime());
                multipartUtility.addFormField("latitude", String.valueOf(mLocation != null ? mLocation.getLatitude() : "0"));
                multipartUtility.addFormField("longitude", String.valueOf(mLocation != null ? mLocation.getLongitude() : "0"));
                multipartUtility.addFilePart("file", filePhoto);

                String response = multipartUtility.finish();

                return response;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    dismissLoadingDialog();
                    JSONObject response = new JSONObject(result);
                    if (response.getString(getString(R.string.result)).equalsIgnoreCase(getString(R.string.success))) {
                        if (response.getString("message") != null && response.getString("message").length() > 0) {

                            DatabaseManager.updateComplaintCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                            DatabaseManager.updateAllComplaintCardStatus(newTodayModel.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteImageFolder();
    }

}