package com.fieldforce.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.LocaleData;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.fieldforce.models.CheckListModel;
import com.fieldforce.models.SopModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.adapters.AssetParametersAdapter;
import com.fieldforce.ui.adapters.AssetReadingParametersAdapter;
import com.fieldforce.ui.adapters.SopAdapter;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;
import com.fieldforce.utility.DecimalInputFilter;
import com.fieldforce.utility.DialogCreator;
import com.fieldforce.utility.LocationManagerReceiver;
import com.fieldforce.utility.MultipartUtility;
import com.fieldforce.utility.SignatureView;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;

import org.json.JSONArray;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

public class DetailsActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller {

    private Context mContext;
    private String ASSET_IMAGE_DIRECTORY_NAME = ".FF-Images", assetImageName = "images", imgURL = "";
    private String SETUP_IMAGE_DIRECTORY_NAME = ".ST-Images", setupImageName = "setupimages", imgSetupURL = "";
    private Typeface mFontBold, mFontMedium, mFontRegular;
    private NestedScrollView nsvAcceptance, nsvWork, nsvFS;
    private RecyclerView recyclerViewSOP, recyclerViewReadingParameters, recyclerViewConfig;
    private TextView txtWorkName, txtExpectedDate, txtName, txtNumber, txtAddress, txtJobDesc,
            txtMore, txtAccepted, txtWorkOrder, txtFollowSteps, txtInstructions, txtTitle, txtSubTitle,
            txtRadioName, txtUploadImage, txtOtherParameter, txtAddSignature, txtPreviousRejection, txtSetupImage;
    private Button btnAccept, btnReject, btnNext, btnComplete, btnClear, btnUpload, btnTpiClear,
            btnTpiNext, btnViewMore;
    private ImageView imgAcceptance, imgWorkInactive, imgFS, imgBack, imgModule, imgTakePhoto, imgPreview,
            imgSetupPreview, imgTakeSetupPhoto, imgViewMore;
    private EditText edtRemark, edtMeterNumber, edtDate, edtRegulatorNumber, edtPipeLength,
            edtReading, edtPONumber, edtFreeLength, edtExtraCharges, edtOtherMeterMake, edtGuage, edtActualFault;
    private View viewDate;
    private RelativeLayout relativeDate;
    private String userId, jobId, screenName, sopTimeStamp, isVerified = "false", selectedMakeId, token, isPurged = "false";
    private View viewBottomAcceptance, viewBottomWork, viewBottomFS;
    private LinearLayout linearSpinnerParameters, linearJobDescription, linearMaterialList, linearInstructions,
            linearAcceptance, linearWork, linearFS, linearAcceptRejectButtons, linearReadingParameter,
            linearInstallationParameters, linearRadio, linearLayoutSignView, viewSignature,
            linearSetupPhoto, linearLayoutTpiSignView, viewTpiSignature, linearRadioNitrogen;
    private RadioGroup radioGroup, radioGroupNitrogen;
    private RadioButton radioYes, radioNo, radioNitrogenYes, radioNitrogenNo;
    private TodayModel todayModel, todayModelDetail;

    private ImageView btnBack, btnTpiBack;

    private LinearLayout linearView;

    private CardView cardFS;

    private JsonResponse response;
    private Date startTime, endTime;
    private AlertDialog alertDialog, alert;
    private Boolean isPartial = false, isInstallation = false;


    private Spinner spinnerMeterDigits, spinnerMeterMake, spinnerMeterType, spinnerRejectionReason;

    private LocationManagerReceiver mLocationManagerReceiver;
    private Location mLocation;

    private File filePhoto = null, fileSign = null, fileSetupPhoto = null, fileTpiSign = null;
    private File consumerSign = null, contractorSign = null, engineerSign = null;
    private SignatureView signatureView, tpiSignatureView;

    private String rejectionReason = "", timeRequired = "", nextDate = "", rejectionRemark = "";

    private Boolean isData = false;
    private static String conversion_date = "", regulator_no = "", pipe_length = "", free_length = "", po_no = "",
            extra_charges = "", rem = "";


    Timer timer;
    long timerInterval = 1000; //1 second
    long timerDelay = 1000; //1 second
    int Count = 0;

    private String testDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mContext = this;
        startTimer();
        token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
        todayModelDetail = new TodayModel();

        startTime = CommonUtility.getCurrentTime();
        mFontBold = App.getMontserratBoldFont();
        mFontMedium = App.getMontserratMediumFont();
        mFontRegular = App.getMontserratRegularFont();

        alertDialog = new AlertDialog.Builder(mContext).create();
        alert = new AlertDialog.Builder(mContext).create();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nsvAcceptance = findViewById(R.id.nsv_acceptance);
        nsvWork = findViewById(R.id.nsv_work);
        nsvFS = findViewById(R.id.nsv_fs);

        linearSpinnerParameters = findViewById(R.id.linear_select_parameters);
        linearSpinnerParameters.setVisibility(View.GONE);
        linearJobDescription = findViewById(R.id.linear_job_description);
        linearMaterialList = findViewById(R.id.linear_material_list);
        linearInstructions = findViewById(R.id.linear_instructions);
        linearAcceptRejectButtons = findViewById(R.id.linear_buttons);
        linearReadingParameter = findViewById(R.id.linear_reading_parameter);
        linearInstallationParameters = findViewById(R.id.linear_installation_parameters);
        linearInstallationParameters.setVisibility(View.GONE);
        linearRadio = findViewById(R.id.linear_radio);
        linearLayoutSignView = findViewById(R.id.linear_signature_view);
        viewSignature = findViewById(R.id.view_signature);
        signatureView = new SignatureView(mContext);
        viewSignature.addView(signatureView);
        linearRadioNitrogen = findViewById(R.id.linear_radio_nitrogen);

        linearLayoutTpiSignView = findViewById(R.id.linear_tpi_signature_view);
        viewTpiSignature = findViewById(R.id.view_tpi_signature);
        tpiSignatureView = new SignatureView(mContext);
        viewTpiSignature.addView(tpiSignatureView);

        linearLayoutSignView.setOnClickListener(null);
        linearLayoutTpiSignView.setOnClickListener(null);

        linearAcceptance = findViewById(R.id.linear_acceptance);
        linearAcceptance.setOnClickListener(this);
        linearWork = findViewById(R.id.linear_work);
        linearWork.setOnClickListener(this);
        linearFS = findViewById(R.id.linear_fs);
        linearFS.setOnClickListener(this);

        linearSetupPhoto = findViewById(R.id.linear_setup_photo);

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
        txtRadioName = findViewById(R.id.txt_radio_name);
        txtUploadImage = findViewById(R.id.txt_upload_image);
        txtOtherParameter = findViewById(R.id.txt_other_parameters);
        txtAddSignature = findViewById(R.id.txt_add_sign);
        txtPreviousRejection = findViewById(R.id.txt_prv_rejection_reason);
        txtSetupImage = findViewById(R.id.txt_setup_image);

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
        txtRadioName.setTypeface(mFontRegular);
        txtUploadImage.setTypeface(mFontMedium);
        txtOtherParameter.setTypeface(mFontMedium);
        txtAddSignature.setTypeface(mFontMedium);
        txtPreviousRejection.setTypeface(mFontRegular);
        txtSetupImage.setTypeface(mFontMedium);


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
        btnTpiBack = findViewById(R.id.btn_tpi_back);
        btnTpiBack.setOnClickListener(this);
        btnTpiClear = findViewById(R.id.btn_tpi_clear);
        btnTpiClear.setOnClickListener(this);
        btnTpiNext = findViewById(R.id.btn_tpi_next);
        btnTpiNext.setOnClickListener(this);


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
        imgSetupPreview = findViewById(R.id.img_setup_preview);
        imgTakeSetupPhoto = findViewById(R.id.img_setup_capture);
        imgTakeSetupPhoto.setOnClickListener(this);
        imgViewMore = findViewById(R.id.img_view_more);
        imgViewMore.setOnClickListener(this);

        edtRemark = findViewById(R.id.edt_remark);
        edtMeterNumber = findViewById(R.id.edt_meter_number);
        edtRegulatorNumber = findViewById(R.id.edt_regulator_number);
        edtPipeLength = findViewById(R.id.edt_pipe_length);
        edtDate = findViewById(R.id.edt_date);
        edtDate.setEnabled(false);
        viewDate = findViewById(R.id.view_date);
        viewDate.setOnClickListener(this);
        relativeDate = findViewById(R.id.relative_date);
        edtReading = findViewById(R.id.edt_reading);
        edtPONumber = findViewById(R.id.edt_po_number);
        edtFreeLength = findViewById(R.id.edt_free_length);
        edtExtraCharges = findViewById(R.id.edt_extra_charges);
        edtOtherMeterMake = findViewById(R.id.edt_other_meter_make);
        edtGuage = findViewById(R.id.edt_guage);


        edtRemark.setTypeface(mFontRegular);
        edtMeterNumber.setTypeface(mFontRegular);
        edtRegulatorNumber.setTypeface(mFontRegular);
        edtPipeLength.setTypeface(mFontRegular);
        edtDate.setTypeface(mFontRegular);
        edtReading.setTypeface(mFontRegular);
        edtPONumber.setTypeface(mFontRegular);
        edtFreeLength.setTypeface(mFontRegular);
        edtExtraCharges.setTypeface(mFontRegular);
        edtOtherMeterMake.setTypeface(mFontRegular);
        edtGuage.setTypeface(mFontRegular);

        cardFS = findViewById(R.id.card_fs);
        linearView = findViewById(R.id.linear_view);

        viewBottomAcceptance = findViewById(R.id.bottom_view_acceptance);
        viewBottomWork = findViewById(R.id.bottom_view_work);
        viewBottomFS = findViewById(R.id.bottom_view_fs);
        radioGroup = findViewById(R.id.radio_group_rfc);
        radioYes = findViewById(R.id.radio_yes);
        radioNo = findViewById(R.id.radio_no);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                        isVerified = "true";
                        edtGuage.setVisibility(View.VISIBLE);
//                        edtGuage.setFilters(new InputFilter[]{new DecimalInputFilter(3, 2)});
//                        edtGuage.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
                        break;
                    case R.id.radio_no:
                        isVerified = "false";
                        edtGuage.setVisibility(View.GONE);
                        edtGuage.setText("");
                        break;
                }
            }
        });
        radioGroupNitrogen = findViewById(R.id.radio_group_nitrogen);
        radioNitrogenYes = findViewById(R.id.radio_nitrogen_yes);
        radioNitrogenNo = findViewById(R.id.radio_nitrogen_no);
        radioGroupNitrogen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_nitrogen_yes:
                        isPurged = "true";
                        linearRadio.setVisibility(View.VISIBLE);
                        if (radioYes.isChecked()) {
                            edtGuage.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.radio_nitrogen_no:
                        isPurged = "false";
                        radioNo.setChecked(true);
                        linearRadio.setVisibility(View.GONE);
                        edtGuage.setVisibility(View.GONE);
                }
            }
        });

        recyclerViewSOP = findViewById(R.id.recycler_view_parameters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewSOP.setLayoutManager(layoutManager);
        recyclerViewReadingParameters = findViewById(R.id.recycler_view_reading_parameters);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewReadingParameters.setLayoutManager(layoutManager2);
        recyclerViewConfig = findViewById(R.id.recycler_view_config);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewConfig.setLayoutManager(layoutManager3);


        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);

        mLocationManagerReceiver = new LocationManagerReceiver(mContext);
        mLocation = mLocationManagerReceiver.getLocation();

        spinnerMeterDigits = findViewById(R.id.spinner_meter_digits);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
                mContext.getResources().getStringArray(R.array.meter_digits)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mFontRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mFontRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeterDigits.setAdapter(dataAdapter);

        spinnerMeterType = findViewById(R.id.spinner_meter_type);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
                mContext.getResources().getStringArray(R.array.meter_type)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mFontRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mFontRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeterType.setAdapter(dataAdapter1);
        spinnerMeterMake = findViewById(R.id.spinner_meter_make);

        Intent intent = getIntent();
        if (intent != null) {
            screenName = intent.getStringExtra(AppConstants.COMING_FROM);
            jobId = intent.getStringExtra(AppConstants.JOB_ID);
            todayModel = (TodayModel) intent.getSerializableExtra(AppConstants.MODEL);
        }

        setValues();
        getMeterMake();
    }

    private void setValues() {
        if (screenName.equals(getString(R.string.site_verification))) {
            imgModule.setImageResource(R.drawable.ic_action_site_verification);
            txtTitle.setText(getString(R.string.id) + " #" + todayModel.getRequestId());
            txtSubTitle.setVisibility(View.GONE);
            txtWorkName.setText(getString(R.string.site_verification_heading));
            txtExpectedDate.setText(getString(R.string.expected_delivery_date) + todayModel.getDueDate());
            txtName.setText(todayModel.getConsumerName());
            txtNumber.setText(todayModel.getMobileNumber());
            txtAddress.setText(todayModel.getAddress());

            txtAddSignature.setText("Consumer Signature");
            txtUploadImage.setText(getString(R.string.site_photo));

            linearJobDescription.setVisibility(View.GONE);
            linearMaterialList.setVisibility(View.GONE);
            linearInstructions.setVisibility(View.GONE);

            linearRadioNitrogen.setVisibility(View.GONE);
            linearRadio.setVisibility(View.GONE);

            edtDate.setHint(R.string.installation_tentative_date);

            txtPreviousRejection.setText(getString(R.string.previous_rejection_remark) + todayModel.siteRejectRemark);

            isJobAccepted(todayModel.getAcceptstatus());

        } else if (screenName.equals(getString(R.string.installation))) {
            linearSpinnerParameters.setVisibility(View.VISIBLE);
            imgModule.setImageResource(R.drawable.ic_action_meter_installation);
            txtTitle.setText(getString(R.string.id) + " #" + todayModel.getRequestId());
            txtSubTitle.setVisibility(View.GONE);
            txtWorkName.setText(getString(R.string.meter_install_heading));
            txtExpectedDate.setText(getString(R.string.expected_delivery_date) + todayModel.getDueDate());
            txtName.setText(todayModel.getConsumerName());
            txtNumber.setText(todayModel.getMobileNumber());
            txtAddress.setText(todayModel.getAddress());
            txtAddSignature.setText("Consumer Signature");

            linearJobDescription.setVisibility(View.GONE);
            linearMaterialList.setVisibility(View.GONE);
            linearInstructions.setVisibility(View.GONE);
            linearInstallationParameters.setVisibility(View.VISIBLE);

            txtUploadImage.setText(getString(R.string.installation_photo));

            edtDate.setHint(R.string.conversion_tentative_date);
            txtRadioName.setText(getString(R.string.rfc_verified));

//          TODO: Uncomment for  conversion rejection remark
            txtPreviousRejection.setText(getString(R.string.previous_rejection_remark) + todayModel.installRejectRemark);
            txtPreviousRejection.setVisibility(View.GONE);

            isJobAccepted(todayModel.getAcceptstatus());
        } else if (screenName.equals(getString(R.string.convert))) {
            imgModule.setImageResource(R.drawable.ic_action_convert);
            txtTitle.setText(getString(R.string.id) + " #" + todayModel.getJob_id());
            txtSubTitle.setVisibility(View.GONE);
            txtWorkName.setText(getString(R.string.conversion_heading));
            txtExpectedDate.setText(getString(R.string.expected_delivery_date) + todayModel.getDueDate());
            txtName.setText(todayModel.getConsumerName());
            txtNumber.setText(todayModel.getMobileNumber());
            txtAddress.setText(todayModel.getArea());

            edtReading.setVisibility(View.VISIBLE);

            txtUploadImage.setText(getString(R.string.meter_photo));

            linearJobDescription.setVisibility(View.GONE);
            linearMaterialList.setVisibility(View.GONE);
            linearInstructions.setVisibility(View.GONE);
            linearSetupPhoto.setVisibility(View.VISIBLE);

            imgViewMore.setVisibility(View.VISIBLE);

            relativeDate.setVisibility(View.GONE);
//            txtRadioName.setText(getString(R.string.gas_started));

//            TODO: Uncomment for  conversion rejection remark

            txtPreviousRejection.setText(getString(R.string.previous_rejection_remark) + todayModel.siteRejectRemark);


            linearRadio.setVisibility(View.GONE);
            linearRadioNitrogen.setVisibility(View.GONE);

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

            getDetails();
//            setFields();
            /*if (screenName.equals(getString(R.string.installation))) {
                setData();
            }*/

        } else {
            nsvAcceptance.setVisibility(View.VISIBLE);
            nsvWork.setVisibility(View.GONE);
            nsvFS.setVisibility(View.GONE);

            linearAcceptRejectButtons.setVisibility(View.VISIBLE);
            linearWork.setClickable(false);
            linearFS.setClickable(false);
        }
    }

    private void getDetails() {
        if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            try {
                showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("site_verification_id", jobId);
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SITE_VERIFICATION_DETAILS, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_SITE_VERIFICATION_DETAILS);

            } catch (Exception e) {
                dismissLoadingDialog();
            }
        } else if (screenName.equals(getString(R.string.installation))) {
            try {
                showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("meter_installation_id", jobId);
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_INSTALLATION_DETAILS, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_INSTALLATION_DETAILS);

            } catch (Exception e) {
                dismissLoadingDialog();
            }
        } else if (screenName.equals(getString(R.string.convert))) {
            try {
                showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("conversion_id", jobId);
                jsonObject.put("userId", userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_CONVERSION_DETAILS, this, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CONVERSION_DETAILS);

            } catch (Exception e) {
                dismissLoadingDialog();
            }
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

        if (screenName.equals(getString(R.string.site_verification))) {
            postParam.put("site_verification_id", jobId);
            if (!status.equalsIgnoreCase(AppConstants.CARD_STATUS_ACCEPTED))
                postParam.put("status", "Refused");
            postParam.put("rejection_remark", rejectionReason);
            postParam.put("next_date", nextDate);
            postParam.put("time_required", timeRequired);
            jsonObject = new JSONObject(postParam);
            JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                    ApiConstants.SENT_SITE_VERIFICATION_ACCEPTANCE, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.SENT_SITE_VERIFICATION_ACCEPTANCE);
        } else if (screenName.equals(getString(R.string.installation))) {
            postParam.put("meter_installation_id", jobId);
            if (!status.equalsIgnoreCase(AppConstants.CARD_STATUS_ACCEPTED))
                postParam.put("status", "Refused");
            postParam.put("rejection_remark", rejectionRemark);
            jsonObject = new JSONObject(postParam);
            JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                    ApiConstants.SENT_INSTALLATION_ACCEPTANCE, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.SENT_INSTALLATION_ACCEPTANCE);
        } else if (screenName.equals(getString(R.string.convert))) {
            postParam.put("conversion_id", jobId);
            if (!status.equalsIgnoreCase(AppConstants.CARD_STATUS_ACCEPTED))
                postParam.put("status", "Refused");
            postParam.put("rejection_remark", rejectionRemark);
            jsonObject = new JSONObject(postParam);
            JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                    ApiConstants.SENT_CONVERSION_ACCEPTANCE, this, token);
            App.getInstance().addToRequestQueue(request, ApiConstants.SENT_CONVERSION_ACCEPTANCE);
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

    private void getMeterMake() {
        try {
            JSONObject jsonObject = new JSONObject();
            JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                    ApiConstants.GET_METER_MAKE, this, "");
            App.getInstance().addToRequestQueue(request, ApiConstants.GET_METER_MAKE);

        } catch (Exception e) {
            dismissLoadingDialog();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            stopTimer();
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

            if (screenName.equals(getString(R.string.installation))) {

                if (onInstallationNext()) {

                    if (isPartial) {
                        nsvAcceptance.setVisibility(View.GONE);
                        nsvWork.setVisibility(View.GONE);
                        nsvFS.setVisibility(View.VISIBLE);
                        imgFS.setImageResource(R.drawable.ic_action_fs);

                        viewBottomAcceptance.setVisibility(View.GONE);
                        viewBottomWork.setVisibility(View.GONE);
                        viewBottomFS.setVisibility(View.VISIBLE);
                        /*linearRadio.setVisibility(View.GONE);
                        edtGuage.setVisibility(View.GONE);

                        if (radioYes.isChecked()) {
                            edtGuage.setVisibility(View.GONE);
                        }*/
                        linearRadioNitrogen.setVisibility(View.GONE);

                    } else {
                        if (installationParameterCheck()) {
                            nsvAcceptance.setVisibility(View.GONE);
                            nsvWork.setVisibility(View.GONE);
                            nsvFS.setVisibility(View.VISIBLE);
                            imgFS.setImageResource(R.drawable.ic_action_fs);
                            viewBottomAcceptance.setVisibility(View.GONE);
                            viewBottomWork.setVisibility(View.GONE);
                            viewBottomFS.setVisibility(View.VISIBLE);

                            /*linearRadio.setVisibility(View.VISIBLE);

                            if (radioYes.isChecked()) {
                                edtGuage.setVisibility(View.VISIBLE);
                            }*/
                            linearRadioNitrogen.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (onNext()) {
                        nsvAcceptance.setVisibility(View.GONE);
                        nsvWork.setVisibility(View.GONE);
                        nsvFS.setVisibility(View.VISIBLE);
                        imgFS.setImageResource(R.drawable.ic_action_fs);

                        viewBottomAcceptance.setVisibility(View.GONE);
                        viewBottomWork.setVisibility(View.GONE);
                        viewBottomFS.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (v == btnAccept) {
            apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_ACCEPTED);
        } else if (v == btnReject) {
            if (screenName.equals(getString(R.string.site_verification))) {
                showDialogForRejection(this);
            } else {
                showDialogForRejectingJobCard(this);
            }
        } else if (v == btnNext) {
            if (screenName.equals(getString(R.string.installation))) {

                if (onInstallationNext()) {
                    if (isPartial) {
                        nsvAcceptance.setVisibility(View.GONE);
                        nsvWork.setVisibility(View.GONE);
                        nsvFS.setVisibility(View.VISIBLE);
                        imgFS.setImageResource(R.drawable.ic_action_fs);

                        viewBottomAcceptance.setVisibility(View.GONE);
                        viewBottomWork.setVisibility(View.GONE);
                        viewBottomFS.setVisibility(View.VISIBLE);
                        /*linearRadio.setVisibility(View.GONE);
                        edtGuage.setVisibility(View.GONE);

                        if (radioYes.isChecked()) {
                            edtGuage.setVisibility(View.GONE);
                        }*/
                        linearRadioNitrogen.setVisibility(View.GONE);

                    } else {
                        if (installationParameterCheck()) {
                            nsvAcceptance.setVisibility(View.GONE);
                            nsvWork.setVisibility(View.GONE);
                            nsvFS.setVisibility(View.VISIBLE);
                            imgFS.setImageResource(R.drawable.ic_action_fs);

                            viewBottomAcceptance.setVisibility(View.GONE);
                            viewBottomWork.setVisibility(View.GONE);
                            viewBottomFS.setVisibility(View.VISIBLE);

                            /*linearRadio.setVisibility(View.VISIBLE);
                            if (radioYes.isChecked()) {
                                edtGuage.setVisibility(View.VISIBLE);
                            }*/
                            linearRadioNitrogen.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else {
                if (onNext()) {
                    nsvAcceptance.setVisibility(View.GONE);
                    nsvWork.setVisibility(View.GONE);
                    nsvFS.setVisibility(View.VISIBLE);
                    imgFS.setImageResource(R.drawable.ic_action_fs);

                    viewBottomAcceptance.setVisibility(View.GONE);
                    viewBottomWork.setVisibility(View.GONE);
                    viewBottomFS.setVisibility(View.VISIBLE);
                }
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
        } else if (v == imgTakeSetupPhoto) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri file1 = getOutputMediaFileUri(SETUP_IMAGE_DIRECTORY_NAME, setupImageName);
            List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;
                mContext.grantUriPermission(packageName, file1, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file1);
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
                if (fileSign != null) {
                    if (screenName.equals(getString(R.string.site_verification))){
                        linearLayoutSignView.setVisibility(View.GONE);
                        showDialogForInstallCompletion();
                    } else {
                        uploadData();
                    }
                }
            } else {
                Toast.makeText(mContext, getString(R.string.add_signature), Toast.LENGTH_SHORT).show();
            }
        } else if (v == btnTpiBack) {
            linearLayoutTpiSignView.setVisibility(View.GONE);
        } else if (v == btnTpiClear) {
            tpiSignatureView.clearSignature();
        } else if (v == btnTpiNext) {
            if (tpiSignatureView.getSignature() != null) {
                Uri tempTpiUri = getImageUri(mContext, tpiSignatureView.getSignature());
                fileTpiSign = new File(getRealPathFromURI(tempTpiUri));
                linearLayoutSignView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(mContext, getString(R.string.add_tpi_signature), Toast.LENGTH_SHORT).show();
            }
        } else if (v == imgViewMore) {
            DialogCreator.showInstallationDetailsDialog(mContext, todayModel.regulatorNo, todayModel.meterNo, todayModel.installedOn, todayModel.rfcVerifiedOn);
        }
    }

    private boolean installationParameterCheck() {
        boolean checkValue = false;
        if (edtMeterNumber.getText().toString().length() > 0) {
            if (!spinnerMeterDigits.getSelectedItem().equals(getString(R.string.meter_digits))) {
                if (spinnerMeterMake.getSelectedItem().equals(getString(R.string.meter_make))) {
                    Toast.makeText(mContext, getString(R.string.select_meter_make), Toast.LENGTH_SHORT).show();
                } else if (spinnerMeterMake.getSelectedItem().toString().equals(getString(R.string.others))) {
                    if (!edtOtherMeterMake.getText().toString().trim().isEmpty()) {
                        if (!spinnerMeterType.getSelectedItem().equals(getString(R.string.meter_type))) {
                            checkValue = true;
                        } else
                            Toast.makeText(mContext, getString(R.string.select_meter_type), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, R.string.enter_other_meter_make, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!spinnerMeterType.getSelectedItem().equals(getString(R.string.meter_type))) {
                        checkValue = true;
                    } else
                        Toast.makeText(mContext, getString(R.string.select_meter_type), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(mContext, getString(R.string.select_meter_digits), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(mContext, getString(R.string.enter_meter_number), Toast.LENGTH_SHORT).show();

        return checkValue;
    }

    private boolean installationOtherParameterCheck() {
        boolean checkValue = false;
        if (edtRegulatorNumber.getText().toString().length() > 0) {
            if (edtPONumber.getText().toString().length() > 0) {
                if (edtPipeLength.getText().toString().length() > 0) {
                    if (edtFreeLength.getText().toString().length() > 0) {
                        if (Integer.parseInt(edtFreeLength.getText().toString().trim()) <=
                                Integer.parseInt(edtPipeLength.getText().toString().trim())) {
                            if (edtExtraCharges.getText().toString().length() > 0) {
                                if (isVerified.equals("true")) {
                                    if (edtGuage.getText().toString().trim().length() > 0) {
                                        if (edtRemark.getText().toString().trim().length() > 0) {
                                            checkValue = true;
                                        } else {
                                            Toast.makeText(mContext, R.string.enter_remark, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(mContext, getString(R.string.enter_pressure_guage), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    checkValue = true;
                                }
                            } else
                                Toast.makeText(mContext, getString(R.string.enter_extra_charges), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, R.string.enter_proper_free_pipe_length, Toast.LENGTH_SHORT).show();
                        }

                    } else
                        Toast.makeText(mContext, getString(R.string.free_pipe_length), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, getString(R.string.enter_pipe_length), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, R.string.enter_po_number, Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(mContext, getString(R.string.enter_regulator_number), Toast.LENGTH_SHORT).show();

        return checkValue;
    }

    private boolean onNext() {
        boolean returnValue = false;

        if (screenName.equals(getString(R.string.convert))) {
            if (!AssetParametersAdapter.hashMap.containsValue("false")) {
                if (linearReadingParameter.getVisibility() == View.VISIBLE) {
                    if (AssetReadingParametersAdapter.hashMap.size() > 0) {
                        sopTimeStamp = CommonUtility.getCurrentDateTime();
                        returnValue = true;
                    } else {
                        Toast.makeText(mContext, getString(R.string.error_reading_parameters), Toast.LENGTH_SHORT).show();
                        returnValue = false;
                    }
                } else {
                    sopTimeStamp = CommonUtility.getCurrentDateTime();
                    returnValue = true;
                }
                return returnValue;
            } else {
                Toast.makeText(mContext, getString(R.string.select_work_order_details_followed), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (AssetParametersAdapter.hashMap.containsValue("true")) {
                if (linearReadingParameter.getVisibility() == View.VISIBLE) {
                    if (AssetReadingParametersAdapter.hashMap.size() > 0) {
                        sopTimeStamp = CommonUtility.getCurrentDateTime();
                        returnValue = true;
                    } else {
                        Toast.makeText(mContext, getString(R.string.error_reading_parameters), Toast.LENGTH_SHORT).show();
                        returnValue = false;
                    }
                } else {
                    sopTimeStamp = CommonUtility.getCurrentDateTime();
                    returnValue = true;
                }
                return returnValue;
            } else {
                Toast.makeText(mContext, getString(R.string.select_work_order_details_followed), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public boolean onInstallationNext() {
        boolean returnValue = false;
        if (!SopAdapter.hashMapSubSop.isEmpty()) {

            if (!SopAdapter.sopRemark.containsValue(getString(R.string.select_remark))) {

                if (SopAdapter.hashMapSubSopValue.containsValue("false")) {
                    isPartial = true;
                } else {
                    isPartial = false;
                }

                sopTimeStamp = CommonUtility.getCurrentDateTime();
                if (linearReadingParameter.getVisibility() == View.VISIBLE) {
                    if (AssetReadingParametersAdapter.hashMap.size() > 0) {
                        returnValue = true;
                    } else {
                        Toast.makeText(mContext, getString(R.string.error_reading_parameters), Toast.LENGTH_SHORT).show();
                        returnValue = false;
                    }
                } else {
                    sopTimeStamp = CommonUtility.getCurrentDateTime();
                    returnValue = true;
                }
                return returnValue;

            } else {
                Toast.makeText(mContext, getString(R.string.please_select_all_remarks), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(mContext, getString(R.string.select_work_order_details_followed), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void onComplete() {
        if (filePhoto != null) {
            if (!screenName.equals(getString(R.string.convert))) {
                if (screenName.equals(getString(R.string.installation))) {
                    if (isPartial) {
                        linearLayoutTpiSignView.setVisibility(View.GONE);
                        uploadData();
                    } else {
                        if (edtDate.getText().toString().length() > 0) {
                            if (installationOtherParameterCheck()) {
//                            setSignatureView();
                                setTpiSignatureView();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.select_tentative_date), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (edtDate.getText().toString().length() > 0) {
                        if (edtRemark.getText().toString().length() > 0) {
                            setSignatureView();
                        } else {
                            Toast.makeText(mContext, R.string.please_enter_remark, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.select_tentative_date), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (fileSetupPhoto != null) {
                    setSignatureView();
                } else {
                    Toast.makeText(mContext, R.string.error_you_have_not_picked_setup_image, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (filePhoto == null) {
            if (screenName.equals(getString(R.string.installation))) {
                if (isPartial) {
                    linearLayoutTpiSignView.setVisibility(View.GONE);
                    uploadData();
                } else {
                    Toast.makeText(mContext, R.string.error_you_have_not_picked_setup_image, Toast.LENGTH_SHORT).show();
                }
            } else if (screenName.equals(getString(R.string.convert)) || screenName.equals(getString(R.string.site_verification))) {
                Toast.makeText(mContext, getString(R.string.error_you_have_not_picked_image), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_you_have_not_picked_image), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSignatureView() {
        if (screenName.equals(getString(R.string.site_verification))) {
            linearLayoutSignView.setVisibility(View.VISIBLE);
        } else if (screenName.equals(getString(R.string.convert))) {
            if (isVerified.equals("true")) {
                linearLayoutSignView.setVisibility(View.VISIBLE);
            } else {
                linearLayoutSignView.setVisibility(View.GONE);
                if (!edtReading.getText().toString().trim().isEmpty()) {
                    uploadData();
                } else {
                    Toast.makeText(mContext, R.string.please_enter_initial_reading, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (isVerified.equals("true")) {
                linearLayoutSignView.setVisibility(View.VISIBLE);
            } else {
                linearLayoutSignView.setVisibility(View.GONE);
                uploadData();
            }
        }
    }

    private void setTpiSignatureView() {
        if (isVerified.equals("true")) {
            linearLayoutTpiSignView.setVisibility(View.VISIBLE);
        } else {
            linearLayoutTpiSignView.setVisibility(View.GONE);
            uploadData();
        }
    }

    private void uploadData() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            endTime = CommonUtility.getCurrentTime();
            if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
                showLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("site_verification_id", jobId);
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

                    jsonObject.put("installation_date", edtDate.getText().toString());
                    jsonObject.put("remark", edtRemark.getText().toString());
                    jsonObject.put("latitude", mLocation != null ? mLocation.getLatitude() : "0");
                    jsonObject.put("longitude", mLocation != null ? mLocation.getLongitude() : "0");

                    jsonObject.put("installation_flag", String.valueOf(isInstallation));




                    imgURL = ApiConstants.UPLOAD_IMAGE_SITE_VERIFICATION;

                    JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                            ApiConstants.UPLOAD_SITE_VERIFICATION_DETAILS, this, token);
                    App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_SITE_VERIFICATION_DETAILS);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
                try {
                    testDuration = stopTimer();
                    showLoadingDialog();
                    String time = CommonUtility.getCurrentDateTime();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("meter_installation_id", jobId);
                    jsonObject.put("userId", userId);
                    jsonObject.put("sop_time_stamp", sopTimeStamp);
                    jsonObject.put("time_difference", calculateTime());
                    jsonObject.put("pole_no", "0");

                    jsonObject.put("meter_no", edtMeterNumber.getText().toString());

                    if (spinnerMeterDigits.getSelectedItem().toString().equals(getString(R.string.meter_digits))) {
                        jsonObject.put("meter_digits", 0);

                    } else {
                        jsonObject.put("meter_digits", spinnerMeterDigits.getSelectedItem().toString());

                    }

                    if (spinnerMeterMake.getSelectedItem().toString().equals(getString(R.string.meter_make))) {
                        jsonObject.put("meter_make", 0);
                    } else if (spinnerMeterMake.getSelectedItem().toString().equals(getString(R.string.others))) {
                        jsonObject.put("meter_make", "others");
                    } else {
                        jsonObject.put("meter_make", selectedMakeId);
                    }

                    if (!edtOtherMeterMake.getText().toString().trim().isEmpty()) {
                        jsonObject.put("other_meter_make", edtOtherMeterMake.getText().toString());
                    }

                    if (spinnerMeterType.getSelectedItem().toString().equals(getString(R.string.meter_type))) {
                        jsonObject.put("meter_type", 0);

                    } else {
                        jsonObject.put("meter_type", spinnerMeterType.getSelectedItem().toString());
                    }


                    JSONArray jsonCheckingParameter = new JSONArray();
                    HashMap<String, String> hashMapCheckingParameter = SopAdapter.hashMap;
                    List<String> checkKeyList = new ArrayList<>(hashMapCheckingParameter.keySet());

                    for (int i = 0; i < hashMapCheckingParameter.size(); i++) {
                        jsonCheckingParameter.put(checkKeyList.get(i));
                    }

                    JSONArray jsonCheckingParameter1 = new JSONArray();
                    HashMap<String, String> hashMapCheckingParameter1 = SopAdapter.hashMapSubSop;
                    List<String> checkKeyList1 = new ArrayList<>(hashMapCheckingParameter1.keySet());

                    for (int i = 0; i < hashMapCheckingParameter1.size(); i++) {
                        jsonCheckingParameter1.put(checkKeyList1.get(i));
                    }

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("main_configuration_id", jsonCheckingParameter);
                    jsonObject1.put("sub_sop_id", jsonCheckingParameter1);


                    jsonObject.put("check_parameters", jsonObject1);


                    JSONObject jsonReadingParameter = new JSONObject();
                    HashMap<String, CharSequence> hashMap = AssetReadingParametersAdapter.hashMap;
                    List<String> keyList = new ArrayList<>(hashMap.keySet());
                    List<CharSequence> valueList = new ArrayList<>(hashMap.values());

                    for (int i = 0; i < hashMap.size(); i++)
                        jsonReadingParameter.put(keyList.get(i), valueList.get(i));
                    jsonObject.put("reading_parameters", jsonReadingParameter);


                    JSONArray jsonCheckingParameter2 = new JSONArray();
                    HashMap<String, String> hashMapCheckingParameter2 = SopAdapter.hashMapSopRemark;
                    List<String> checkKeyList2 = new ArrayList<>(hashMapCheckingParameter1.keySet());

                    for (int i = 0; i < hashMapCheckingParameter1.size(); i++) {
                        jsonCheckingParameter1.put(checkKeyList1.get(i));
                    }

                    jsonObject.put("regulator_no", edtRegulatorNumber.getText().toString());
                    jsonObject.put("pipe_length", edtPipeLength.getText().toString());
                    jsonObject.put("free_pipe_length", edtFreeLength.getText().toString());
                    jsonObject.put("extra_charges", edtExtraCharges.getText().toString());
                    jsonObject.put("conversion_date", edtDate.getText().toString());

                    jsonObject.put("po_number", edtPONumber.getText().toString());
                    jsonObject.put("nitrogen_purging", isPurged);


                    if (isPurged.equals("true")) {
                        jsonObject.put("rfc_verified", isVerified);
                        if (!edtGuage.getText().toString().trim().isEmpty()) {
                            jsonObject.put("pressure_guage", edtGuage.getText().toString());
                            jsonObject.put("rfc_date", time.trim());
                            jsonObject.put("test_duration", testDuration);
                        }
                    } else {
                        jsonObject.put("rfc_verified", "false");

                    }


                    jsonObject.put("latitude", mLocation != null ? mLocation.getLatitude() : "0");
                    jsonObject.put("longitude", mLocation != null ? mLocation.getLongitude() : "0");
                    jsonObject.put("remark", edtRemark.getText().toString());


                    if (!isPartial && radioNitrogenNo.isChecked()) {
                        isPartial = true;
                    }
                    jsonObject.put("is_partial", isPartial.toString().trim());


                    imgURL = ApiConstants.UPLOAD_IMAGE_INSTALLATION;

                    JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                            ApiConstants.UPLOAD_INSTALLATION_DETAILS, this, token);
                    App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_INSTALLATION_DETAILS);

                } catch (Exception e) {
                    dismissLoadingDialog();
                    e.printStackTrace();
                }
            } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
                try {
                    showLoadingDialog();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("conversion_id", jobId);
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

                    jsonObject.put("latitude", mLocation != null ? mLocation.getLatitude() : "0");
                    jsonObject.put("longitude", mLocation != null ? mLocation.getLongitude() : "0");
                    jsonObject.put("remark", edtRemark.getText().toString());
                    jsonObject.put("initial_reading", edtReading.getText().toString());

//                    jsonObject.put("gas_started", isVerified);

                    imgURL = ApiConstants.UPLOAD_CONVERSION_IMAGE;

                    JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                            ApiConstants.UPLOAD_CONVERSION_DETAILS, this, token);
                    App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_CONVERSION_DETAILS);

                } catch (Exception e) {
                    dismissLoadingDialog();
                    e.printStackTrace();
                }
            }
        }
    }

    public void onAsyncSuccess(final JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        if (jsonResponse != null)
            if (jsonResponse.status != null && !jsonResponse.status.isEmpty()) {
                if (jsonResponse.status.equalsIgnoreCase(AppConstants.CARD_STATUS_ACCEPTED)) {
                    todayModelDetail.completedOn = CommonUtility.getCompletionDate();
                    if (screenName.equals(getString(R.string.site_verification))) {
                        DatabaseManager.updateSiteVerificationCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllSiteVerificationCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                    } else if (screenName.equals(getString(R.string.installation))) {
                        DatabaseManager.updateMeterInstallCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllMeterInstallCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                    } else if (screenName.equals(getString(R.string.convert))) {
                        DatabaseManager.updateConversionCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllConversionCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_OPEN, AppConstants.CARD_STATUS_ACCEPTED);
                    }
                } else {
                    if (screenName.equals(getString(R.string.site_verification))) {
                        DatabaseManager.deleteJobCardSiteVerification(mContext, jobId, userId);
                        DatabaseManager.deleteJobCardAll(mContext, jobId, userId, String.valueOf(screenName));
                    } else if (screenName.equals(getString(R.string.installation))) {
                        DatabaseManager.deleteJobCardMeterInstall(mContext, jobId, userId);
                        DatabaseManager.deleteJobCardAll(mContext, jobId, userId, String.valueOf(screenName));
                    } else if (screenName.equals(getString(R.string.convert))) {
                        DatabaseManager.deleteJobCardConversion(mContext, jobId, userId);
                        DatabaseManager.deleteJobCardAll(mContext, jobId, userId, String.valueOf(screenName));
                    }
                }
                nextNavigation(jsonResponse.status);
            }
        switch (label) {
            case ApiConstants.GET_SITE_VERIFICATION_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.site_verification_detail != null && !jsonResponse.site_verification_detail.isEmpty()) {
                            try {
                                dismissLoadingDialog();
                                AssetParametersAdapter assetParametersAdapter = new AssetParametersAdapter(mContext,
                                        jsonResponse.site_verification_detail.get(0).check_list, screenName);
                                recyclerViewSOP.setAdapter(assetParametersAdapter);
                                if (jsonResponse.site_verification_detail.get(0).readingparameter_list.size() == 0) {
                                    linearReadingParameter.setVisibility(View.GONE);
                                }
                                AssetReadingParametersAdapter readingParametersAdapter = new AssetReadingParametersAdapter(mContext,
                                        jsonResponse.site_verification_detail.get(0).readingparameter_list);
                                recyclerViewReadingParameters.setAdapter(readingParametersAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
            break;
            case ApiConstants.UPLOAD_SITE_VERIFICATION_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        dismissLoadingDialog();
                        todayModelDetail.completedOn = CommonUtility.getCompletionDate();
                        DatabaseManager.updateSiteVerificationCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                        DatabaseManager.updateAllSiteVerificationCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                        Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        AssetParametersAdapter.hashMap.clear();
                        AssetReadingParametersAdapter.hashMap.clear();
                        new UploadImage().execute();
                        finish();
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_INSTALLATION_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.meter_install_detail != null && !jsonResponse.meter_install_detail.isEmpty()) {
                            try {
                                dismissLoadingDialog();
                                if (jsonResponse.meter_install_detail.get(0).reportDataCount.equals("1")) {
                                    isData = true;
                                } else {
                                }
                                todayModelDetail.meterNo = jsonResponse.meter_install_detail.get(0).meterNo;
                                todayModelDetail.meterDigits = jsonResponse.meter_install_detail.get(0).meterDigits;
                                todayModelDetail.meterMake = jsonResponse.meter_install_detail.get(0).meterMake;
                                todayModelDetail.meterType = jsonResponse.meter_install_detail.get(0).meterType;
                                todayModelDetail.regulatorNo = jsonResponse.meter_install_detail.get(0).regulatorNo;
                                todayModelDetail.pipeLength = jsonResponse.meter_install_detail.get(0).pipeLength;
                                todayModelDetail.freeLength = jsonResponse.meter_install_detail.get(0).freeLength;
                                todayModelDetail.extraCharges = jsonResponse.meter_install_detail.get(0).extraCharges;
                                todayModelDetail.conversionDate = jsonResponse.meter_install_detail.get(0).conversionDate;
                                todayModelDetail.rfcVerified = jsonResponse.meter_install_detail.get(0).rfcVerified;
                                todayModelDetail.poNumber = jsonResponse.meter_install_detail.get(0).poNumber;
                                todayModelDetail.remark = jsonResponse.meter_install_detail.get(0).remark;
                                todayModelDetail.nitogenPurging = jsonResponse.meter_install_detail.get(0).nitogenPurging;

                                setFields();

                                ArrayList<CheckListModel> configList = new ArrayList<>();
                                configList.addAll(jsonResponse.meter_install_detail.get(0).checkList);

                                /*ArrayList<CheckListModel> remarkList = new ArrayList<>();
                                remarkList.addAll(jsonResponse.meter_install_detail.get(0).remarkList);*/

                                SopAdapter sopAdapter = new SopAdapter(mContext, configList, null);
                                recyclerViewSOP.setAdapter(sopAdapter);
                                sopAdapter.notifyDataSetChanged();

                                if (jsonResponse.meter_install_detail.get(0).readingparameter_list.size() == 0) {
                                    linearReadingParameter.setVisibility(View.GONE);
                                }
                                AssetReadingParametersAdapter readingParametersAdapter = new AssetReadingParametersAdapter(mContext, jsonResponse.meter_install_detail.get(0).readingparameter_list);
                                recyclerViewReadingParameters.setAdapter(readingParametersAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.UPLOAD_INSTALLATION_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        dismissLoadingDialog();
                        todayModelDetail.completedOn = CommonUtility.getCompletionDate();
                        if (jsonResponse.message.equalsIgnoreCase(CommonUtility.getString(mContext, R.string.error_download_data))) {
                            CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.error_download_data),
                                    getString(R.string.main_activity), false);
                            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            customDialog.show();
                            customDialog.setCancelable(false);
                        } else {
                            DatabaseManager.updateMeterInstallCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                            DatabaseManager.updateAllMeterInstallCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            AssetParametersAdapter.hashMap.clear();
                            AssetReadingParametersAdapter.hashMap.clear();
                            if (filePhoto != null) {
                                new UploadImage().execute();
                            }
                            finish();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_CONVERSION_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.conversion_detail != null && !jsonResponse.conversion_detail.isEmpty()) {
                            try {
                                dismissLoadingDialog();
                                todayModel.pinCode = jsonResponse.conversion_detail.get(0).pinCode;
                                todayModel.registrationNo = jsonResponse.conversion_detail.get(0).registrationNo;
                                todayModel.flatNo = jsonResponse.conversion_detail.get(0).flatNo;
                                todayModel.buildingName = jsonResponse.conversion_detail.get(0).buildingName;
                                todayModel.location = jsonResponse.conversion_detail.get(0).location;
                                todayModel.area = jsonResponse.conversion_detail.get(0).area;
                                todayModel.city = jsonResponse.conversion_detail.get(0).city;
                                todayModel.phone = jsonResponse.conversion_detail.get(0).phone;
                                todayModel.email = jsonResponse.conversion_detail.get(0).email;
                                todayModel.installationDate = jsonResponse.conversion_detail.get(0).installationDate;
                                todayModel.meterNo = jsonResponse.conversion_detail.get(0).meterNo;
                                todayModel.testDate = jsonResponse.conversion_detail.get(0).testDate;
                                todayModel.pressureGuage = jsonResponse.conversion_detail.get(0).pressureGuage;
                                todayModel.testDuration = jsonResponse.conversion_detail.get(0).testDuration;
                                todayModel.imgTpi = jsonResponse.conversion_detail.get(0).imgTpi;
                                AssetParametersAdapter assetParametersAdapter = new AssetParametersAdapter(mContext,
                                        jsonResponse.conversion_detail.get(0).check_list, screenName);
                                recyclerViewSOP.setAdapter(assetParametersAdapter);
                                if (jsonResponse.conversion_detail.get(0).readingparameter_list.size() == 0) {
                                    linearReadingParameter.setVisibility(View.GONE);
                                }
                                AssetReadingParametersAdapter readingParametersAdapter = new AssetReadingParametersAdapter(mContext,
                                        jsonResponse.conversion_detail.get(0).readingparameter_list);
                                recyclerViewReadingParameters.setAdapter(readingParametersAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            break;
            case ApiConstants.UPLOAD_CONVERSION_DETAILS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        dismissLoadingDialog();
                        todayModelDetail.completedOn = CommonUtility.getCompletionDate();
                        if (jsonResponse.message.equalsIgnoreCase(CommonUtility.getString(mContext, R.string.error_download_data))) {
                            CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.error_download_data),
                                    getString(R.string.main_activity), false);
                            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            customDialog.show();
                            customDialog.setCancelable(false);
                        } else {
                            DatabaseManager.updateConversionCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                            DatabaseManager.updateAllConversionCardStatus(todayModelDetail.completedOn, jobId, AppConstants.CARD_STATUS_CLOSED, AppConstants.CARD_STATUS_ACCEPTED);
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            AssetParametersAdapter.hashMap.clear();
                            AssetReadingParametersAdapter.hashMap.clear();
                            new UploadImage().execute();
                            Intent intent = new Intent(mContext, MJCFormActivity.class);
                            intent.putExtra(AppConstants.MODEL, todayModel);
                            intent.putExtra("initial_reading", edtReading.getText().toString());
                            intent.putExtra("conversion_date", sopTimeStamp);
                            intent.putExtra("image_sign_id", jobId);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_METER_MAKE: {
                if (jsonResponse != null) {
                    if (jsonResponse.meter_make_list != null) {
                        dismissLoadingDialog();
                        ArrayList<String> list = new ArrayList<>();
                        final ArrayList<String> listId = new ArrayList<>();
                        list.add(0, getString(R.string.meter_make));
                        listId.add(0, "0");
                        for (int i = 0; i < jsonResponse.meter_make_list.size(); i++) {
                            list.add(jsonResponse.meter_make_list.get(i).meterMakeName);
                            listId.add(jsonResponse.meter_make_list.get(i).meterMakeId);
                        }
                        int count = jsonResponse.meter_make_list.size();
                        int newCount = count + 1;
                        list.add(newCount, getString(R.string.others));
                        listId.add(count + 1, "" + newCount);

                        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list) {
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View v = super.getView(position, convertView, parent);
                                ((TextView) v).setTypeface(mFontRegular);
                                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                                return v;
                            }

                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View v = super.getDropDownView(position, convertView, parent);
                                ((TextView) v).setTypeface(mFontRegular);
                                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                                return v;
                            }
                        };
                        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMeterMake.setAdapter(dataAdapter1);
                        spinnerMeterMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                selectedMakeId = listId.get(position);
                                if (spinnerMeterMake.getSelectedItem().toString().trim().equals(getString(R.string.others))) {
                                    edtOtherMeterMake.setVisibility(View.VISIBLE);
                                } else {
                                    edtOtherMeterMake.setVisibility(View.GONE);
                                    selectedMakeId = listId.get(position);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        dismissLoadingDialog();
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
        dismissLoadingDialog();
        Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        dismissLoadingDialog();
        Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
    }


    private void setFields() {

        if (!todayModelDetail.meterNo.isEmpty()) {
            if (todayModelDetail.meterNo.equals("None")) {
                edtMeterNumber.setText("");
            } else
                edtMeterNumber.setText(todayModelDetail.meterNo);
        }
        if (!todayModelDetail.meterDigits.isEmpty()) {
            if (todayModelDetail.meterDigits.equals("0")) {
                spinnerMeterDigits.setSelection(0);
            }
            if (todayModelDetail.meterDigits.equals("5")) {
                spinnerMeterDigits.setSelection(1);
            }
            if (todayModelDetail.meterDigits.equals("6")) {
                spinnerMeterDigits.setSelection(2);
            }
            if (todayModelDetail.meterDigits.equals("7")) {
                spinnerMeterDigits.setSelection(3);
            }
            if (todayModelDetail.meterDigits.equals("8")) {
                spinnerMeterDigits.setSelection(4);
            }
            if (todayModelDetail.meterDigits.equals("9")) {
                spinnerMeterDigits.setSelection(5);
            }
            if (todayModelDetail.meterDigits.equals("10")) {
                spinnerMeterDigits.setSelection(6);
            }
        }
        if (todayModelDetail.meterMake != null) {
            String value = todayModelDetail.meterMake;
            if (value.equals("None")) {
                spinnerMeterMake.setSelection(0);
            } else {
                spinnerMeterMake.setSelection(Integer.parseInt(todayModelDetail.meterMake));
            }

        }
        if (todayModelDetail.meterType != null) {
            if (todayModelDetail.meterType.equals("0")) {
                spinnerMeterType.setSelection(0);
            } else if (todayModelDetail.meterType.equals("Smart")) {
                spinnerMeterType.setSelection(1);
            } else if (todayModelDetail.meterType.equals("Mechanical")) {
                spinnerMeterType.setSelection(2);
            }
        }

        if (isData) {

            if (todayModelDetail.nitogenPurging.equals("true")) {
                radioNitrogenYes.setChecked(true);
                linearRadio.setVisibility(View.VISIBLE);
            }
            if (todayModelDetail.remark != null || todayModelDetail.remark != "") {
                if (todayModelDetail.remark.equals("None")) {
                    edtRemark.setText("");
                } else {
                    edtRemark.setText(todayModelDetail.remark);
                }
            } else {
                edtRemark.setText("");
            }

            if (todayModelDetail.regulatorNo != null || todayModelDetail.regulatorNo != "") {
                edtRegulatorNumber.setText(todayModelDetail.regulatorNo);
            } else
                edtRegulatorNumber.setText("");

            if (todayModelDetail.pipeLength != null || todayModelDetail.pipeLength != "")
                edtPipeLength.setText(todayModelDetail.pipeLength);
            else
                edtPipeLength.setText("");

            if (todayModelDetail.conversionDate != null) {
                edtDate.setText(todayModelDetail.conversionDate);

            } else
                edtDate.setText("");
        }

        if (todayModelDetail.poNumber != null)
            edtPONumber.setText(todayModelDetail.poNumber);

        if (todayModelDetail.freeLength != null) {
            edtFreeLength.setText(todayModelDetail.freeLength);
        }
        if (todayModelDetail.extraCharges != null) {
            edtExtraCharges.setText(todayModelDetail.extraCharges);
        }


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
                Bitmap bitmap1 = getBitmapScaled(SETUP_IMAGE_DIRECTORY_NAME, setupImageName);
                if (bitmap != null) {
                    imgPreview.setImageBitmap(bitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(mContext, bitmap);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    filePhoto = new File(getRealPathFromURI(tempUri));
                }
                if (bitmap1 != null) {

                    imgSetupPreview.setImageBitmap(bitmap1);
                    Uri setupUri = getImageUri(mContext, bitmap1);
                    fileSetupPhoto = new File(getRealPathFromURI(setupUri));
                }
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
        File createdFile;

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        createdFile = new File(mediaStorageDir.getPath() + File.separator + filename + ".jpg");
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

        File fileDelete = new File(file.getAbsolutePath());
        if (fileDelete.exists()) {
            if (fileDelete.delete()) {
            } else {
            }
        }
    }

    private void deleteImageFolder() {
        File folder1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + ASSET_IMAGE_DIRECTORY_NAME);
        File folder2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + SETUP_IMAGE_DIRECTORY_NAME);
        try {
            if (folder1.exists())
                CommonUtility.deleteDir(folder1);
            if (folder2.exists())
                CommonUtility.deleteDir(folder2);
        } catch (Exception e) {
        }
    }

    private class UploadImage extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);

                MultipartUtility multipartUtility = new MultipartUtility(imgURL, "UTF-8");

                if (screenName.equals(getString(R.string.convert)))
                    multipartUtility.addFilePart("imgSetupURL", fileSetupPhoto);

                multipartUtility.addFormField("image_id", jobId);

                if (filePhoto != null)
                    multipartUtility.addFilePart("file", filePhoto);

             /*   if (screenName.equals(getString(R.string.convert)))
                    multipartUtility.addFilePart("setup_file", fileSetupPhoto);*/

                if (fileSign != null)
                    multipartUtility.addFilePart(getString(R.string.file_sign), fileSign);

                if (fileTpiSign != null)
                    multipartUtility.addFilePart("tpi_sign", fileTpiSign);

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

                        if (filePhoto != null)
                            filePhoto.delete();
                        if (fileSetupPhoto != null)
                            fileSetupPhoto.delete();
                        if (fileSign != null)
                            fileSign.delete();
                        if (fileTpiSign != null)
                            fileTpiSign.delete();

                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showDialogForRejectingJobCard(final Context context) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_for_rejecting_jobcard, null);

        final EditText edtRemark;
        Button btnOK;

        edtRemark = promptView.findViewById(R.id.edt_rejection_remark);
        btnOK = promptView.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtRemark.getText().toString().trim().isEmpty()) {
                    rejectionRemark = edtRemark.getText().toString();
                    apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_REJECTED);
                    alert.dismiss();
                } else {
                    Toast.makeText(mContext, R.string.please_enter_remark_for_rejection, Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setView(promptView);
        alert.setCancelable(false);
        alert.show();
    }


    public void showDialogForRejection(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_for_rejection, null);

        final EditText edtNextDate, edtTimeRequired;
        final Spinner spinnerRejectionReason = promptView.findViewById(R.id.spinner_rejection_reason);
        edtNextDate = promptView.findViewById(R.id.edt_date);
        edtTimeRequired = promptView.findViewById(R.id.edt_time_required);
        Button btnYes;
        btnYes = promptView.findViewById(R.id.btn_yes);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.rejection_reason)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(CommonUtility.getColor(getContext(), R.color.colorProfileEditText));
                ((TextView) v).setTextSize(14f);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTextColor(CommonUtility.getColor(getContext(), R.color.colorProfileEditText));
                ((TextView) v).setTextSize(14f);
                return v;
            }
        };
        spinnerRejectionReason.setAdapter(adapter);
        spinnerRejectionReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    edtTimeRequired.setVisibility(View.VISIBLE);
                    edtNextDate.setVisibility(View.GONE);
                } else if (position == 7) {
                    edtNextDate.setVisibility(View.VISIBLE);
                    edtTimeRequired.setVisibility(View.GONE);

                    edtNextDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                                    edtNextDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                                    nextDate = edtNextDate.getText().toString();
                                }
                            }, mYear, mMonth, mDay);
                            datePickerDialog.getDatePicker().setMinDate(newDate.getTime());
                            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                            datePickerDialog.show();
                        }
                    });
                } else {
                    edtNextDate.setVisibility(View.GONE);
                    edtTimeRequired.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerRejectionReason.getSelectedItem().equals(getString(R.string.please_select_reason_for_rejection))) {
                    Toast.makeText(mContext, R.string.please_select_reason_for_rejection, Toast.LENGTH_SHORT).show();
                } else if (spinnerRejectionReason.getSelectedItem().equals(getString(R.string.network_unavailable))) {

                    if (edtTimeRequired.getText().toString().trim().isEmpty()) {
                        timeRequired = "";
                    } else {
                        timeRequired = edtTimeRequired.getText().toString();
                    }
                    rejectionReason = spinnerRejectionReason.getSelectedItem().toString().trim();
                    nextDate = "";
                    apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_REJECTED);
                    alertDialog.dismiss();

                } else if (spinnerRejectionReason.getSelectedItem().equals(getString(R.string.society_not_allowing))) {
                    if (!edtNextDate.getText().toString().trim().isEmpty()) {
                        nextDate = edtNextDate.getText().toString();
                    } else {
                        nextDate = "";
                    }
                    rejectionReason = spinnerRejectionReason.getSelectedItem().toString().trim();
                    timeRequired = "";

                    apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_REJECTED);
                    alertDialog.dismiss();
                } else {
                    rejectionReason = spinnerRejectionReason.getSelectedItem().toString().trim();
                    nextDate = "";
                    timeRequired = "";
                    apiAcceptReject(jobId, screenName, AppConstants.CARD_STATUS_REJECTED);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.setView(promptView);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AssetParametersAdapter.hashMap.clear();
        AssetReadingParametersAdapter.hashMap.clear();
        deleteImageFolder();
    }


    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                Log.i("startTimer", Count + " s");
                Count++;
            }
        }, timerDelay, timerInterval);
    }

    private String stopTimer() {
//        Log.i("stopTimer", "Stopped after " + Count + " s");
        int hours = (int) Count / 3600;
        int remainder = (int) Count - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
//        Log.i("stopTimer", "Stopped time  " + hours + "  hr " + mins + " min " + secs + " sec ");

        Count = 0;
        if (timer != null) {
            timer.cancel();
        }
        if (hours != 0)
            return hours + " hr " + mins + " min " + secs + " sec";
        else
            return mins + " min " + secs + " sec";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopTimer();
    }


    public void showDialogForInstallCompletion() {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.dialog_cancel, null);
        Button btnYes, btnNo;
        TextView txtTitle;

        btnYes = promptView.findViewById(R.id.btn_yes);
        btnNo = promptView.findViewById(R.id.btn_no);
        txtTitle = promptView.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.do_you_want_to_assign_this_to_installation));
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInstallation = true;
                uploadData();
                alert.dismiss();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInstallation = false;
                uploadData();
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.setCancelable(false);
        alert.show();
    }
}
