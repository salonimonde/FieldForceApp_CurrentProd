package com.fieldforce.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.fieldforce.models.Consumer;
import com.fieldforce.models.ImageModel;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.adapters.DocumentIdAdapter;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;
import com.fieldforce.utility.DialogCreator;
import com.fieldforce.utility.MultipartUtility;
import com.fieldforce.utility.SignatureView;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import android.telephony.SmsManager;
import android.widget.Toolbar;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import id.zelory.compressor.Compressor;

import static com.fieldforce.utility.AppConstants.GALLERY;
import static com.fieldforce.utility.CommonUtility.isNetworkAvailable;

public class RegistrationFormActivity extends ParentActivity implements View.OnClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ApiServiceCaller,
        ActivityCompat.OnRequestPermissionsResultCallback, VerificationListener {

    private static final String TAG = Verification.class.getSimpleName();
    public static Activity formActivity;
    private Verification mVerification;


    // for OTP verification
    public static final String INTENT_PHONENUMBER = "phonenumber";

    private String PROFILE_IMAGE_DIRECTORY_NAME = ".profile-Images", ProfileImageName = "images";
    private Context mContext;
    private TextView txtSMSVerification, txtVerify, txtResendOTP;
    private Button btnNext1, btnNext2, btnNext3, btnNext4, btnSubmit, btnVerifyOTP, btnSMSVerification;
    private EditText edtConsumerName, edtMobile, edtAadharNumber, edtEmailId, edtOther, edtFlatNumber, edtSocietyBuildingName,
            edtLocation, edtArea, edtAmountPayable, edtChequeNo, edtChequeBranch, edtChequeDate,
            edtDDNo, edtOtherBank, edtIFSCCheque, edtIFSCDD, edtOtherBankCheque, edtDDDate, edtDDBranch, edtState,
            edtCity, edtEnterOTP, edtFloor, edtPlotNo, edtWing, edtSocietyName, edtRoadNo, edtLandmark,
            edtDistrict, edtWard;
    private RelativeLayout relativeViewGeneralInfo, relativeViewAddressInfo, relativeViewUploadDoc, relativeViewPayment,
            relativeIdProof, relativeAddressProof, relativeImgId1, relativeImgId2, relativeImgAdd1, relativeImgAdd2,
            relBankName, relNocProof, relBankNameCheque, relativeSignature;
    private NestedScrollView nestedIdProof, nestedAddressProof, nestedSignature;
    private RecyclerView recyclerIDDoc, recyclerViewAddDoc;
    private LinearLayoutManager layoutManager;
    private LinearLayout linearChequeDetails, linearDDDetails, linearNocDetails, linearOTP;
    private ImageView imgBack, imgIdProofArrow, imgAddressProofArrow, imgNocProofArrow, toolImage;
    private Spinner spinnerOccupation, spinnerPremisesType, spinnerState, spinnerCity, spinnerPinCode,
            spinnerPaymentScheme, spinnerPaymentMethod, spinnerConsumerCategory, spinnerConsumerSubCategory,
            spinnerBankName, spinnerArea, spinnerBankNameCheque, spinnerWard, spinnerLocation, spinnerLandmark;
    private List<String> cityList, pinCodeList, paymentSchemeList, categoryList, subCategoryList,
            bankList, areaList, keyList, wardList, areaListPincode, areaListLocation, locationList, landmarkList, areaListLandmark;
    private ArrayList<String> cityArray, pinCodeArray, stateArray, schemeArray, categoryArray, subCategoryArray,
            bankArray, areaArray, bankArrayCheque, wardArray, locationArray, landmarkArray;
    private HashMap hashMapCity = new HashMap<String, String>(), hashMapPinCode = new HashMap<String, String>(),
            hashMapState = new HashMap<String, String>(),
            hashMapCategory = new HashMap<String, String>(), hashMapSubCategory = new HashMap<String, String>(),
            hashMapBankName = new HashMap<String, String>(), haspMapArea = new HashMap<String, String>(),
            hashMapBankChequeArray = new HashMap<String, String>(), hashMapWard = new HashMap<String, String>(), hashMapLocation = new HashMap<String, String>(), hashMapLandmark = new HashMap<String, String>();
    private LinkedHashMap hashMapPaymentScheme = new LinkedHashMap<String, String>();
    private String selectedState, selectedCity, selectedPinCode, selectedScheme, selectedCategory, selectSubCategory,
            selectedBank, selectedArea, selectedBankCheque, message, selectedWard, selectedAreaPincode, selectedAreaLocation, selectedLocation, selectedLandmark, selectedAreaLandmark;
    private String selectedBankName, selectedOtherBankName, selectedDDNumber, selectedChequeNumber, selectedChequeBranch,
            selectedChequeDate, selectedBankCheckName;
    private ArrayList<Bitmap> imgArrayBitmap = new ArrayList<>();
    private Typeface mRegularBold, mRegular;
    private ArrayList<String> checkValueList = new ArrayList<>();
    private int clickCount = 0;

    public boolean istoUpload = false;

    private TextView txtRadioName;

    private View viewCheque, viewDD;

    private int Count = 0;
    private ImageView imgTakeID1, imgTakeID2, imgCancelID1, imgCancelID2, imgTakeADD1,
            imgTakeADD2, imgCancelADD1, imgCancelADD2, imgTakeNoc, imgCancelNoc, imgCheque, imgDD,
            imgCancelCheque, imgCancelDD, imgConsumerPhoto;
    private File File1 = null, File2 = null, File3 = null, File4 = null, File5 = null,
            fileCheque = null, fileDD = null, fileConsumerSign = null, fileConsumerPhoto = null;
    private String imgId1, imgId2 = "", imgId3 = "", imgId4 = "", imgId5 = "",
            imgIdCheque = "", imgIdDD = "", imgPhoto = "";

    private String enquiryId = "";

    private boolean idProof = false, addressProof = true, nocProof = true;
    private Button btnPreviousOne, btnPreviousTwo, btnPreviousThree, btnPreviousFour, btnClearConsumer;
    private int imageCount = 0, imageCountAdd = 0;
    private ArrayList<String> imgRemoveList = new ArrayList<>();

    private TodayModel todayModel, newTodayModel;
    private RegistrationModel registrationModel, newRegistrationModel;

    private String latitude, longitude;
    private String premise = "", isConnected = "NO";


    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private RadioGroup radioGroup;
    private RadioButton radioYes, radioNo;

    private RelativeLayout relPaymentMethod;

    private Integer schemeAmount = 0;

    //
    private String comingFrom = "New_NSC", stateId = "", districtId = "", cityId = "", vendorId = "", nscId = "", fieldForceId = "";

    private Boolean isNscNew = false, isSchemePresent = false, isRejected = false;

    public static boolean isAadharPresent = false;

    private AlertDialog alert, alert1;

    private Bitmap idBitmap1 = null, idBitmap2 = null, addBitmap1 = null, addBitmap2 = null, nocBitmap = null,
            bitmapChequeDD = null, bitmapSign = null, bitmapConsumerPhoto = null;

    private LinearLayout viewConsumerSignature;
    private SignatureView signatureViewConsumer;


    private ArrayList<Consumer> consumerArea, bankNames, schemeName, documentList, addDocumnetList, wardName, category, subCategory, pincode, location, landmark;

    private String mStrOTP;
    private ArrayList<RegistrationModel> registrationToUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        mContext = this;
        newTodayModel = new TodayModel();
        registrationModel = new RegistrationModel();
        formActivity = getParent();
        Intent intent = getIntent();
        if (intent != null) {
            comingFrom = (String) intent.getSerializableExtra(AppConstants.COMING_FROM);
            if (comingFrom.equals(getString(R.string.rejected_nsc))) {
                newRegistrationModel = (RegistrationModel) intent.getSerializableExtra(AppConstants.MODEL);
                isRejected = true;
            } else {
                todayModel = (TodayModel) intent.getSerializableExtra(AppConstants.MODEL);
            }

        }
        stateId = AppPreferences.getInstance(mContext).getString(AppConstants.STATE_ID, "");
        cityId = AppPreferences.getInstance(mContext).getString(AppConstants.CITY_ID, "");
        districtId = AppPreferences.getInstance(mContext).getString(AppConstants.DISTRICT_ID, "");

        vendorId = AppPreferences.getInstance(mContext).getString(AppConstants.VENDOR_ID, "");
        fieldForceId = AppPreferences.getInstance(mContext).getString(AppConstants.FIELD_FORCE_ID, "");
        if (comingFrom.equals(getString(R.string.new_nsc))) {
            isNscNew = true;
        }

        alert = new AlertDialog.Builder(mContext).create();
        alert1 = new AlertDialog.Builder(mContext).create();
        mRegularBold = App.getMontserratMediumFont();
        mRegular = App.getMontserratRegularFont();

        relPaymentMethod = findViewById(R.id.rel_spinner_payment_method);

        TextView txtTitle = findViewById(R.id.txt_title);
        txtTitle.setTypeface(mRegularBold);
        txtTitle.setText(getString(R.string.registration_form));
        TextView txtSubTitle = findViewById(R.id.txt_sub_title);
        txtSubTitle.setVisibility(View.GONE);
        //ImageView image = findViewById(R.id.img_consumer);
        //image.setVisibility(View.GONE);

        txtRadioName = findViewById(R.id.txt_radio_name);
        txtRadioName.setTypeface(mRegular);

        viewCheque = findViewById(R.id.view_date_cheque);
        viewDD = findViewById(R.id.view_date_dd);

        relativeViewGeneralInfo = findViewById(R.id.layout_step_one);
        relativeViewAddressInfo = findViewById(R.id.layout_step_two);
        relativeViewUploadDoc = findViewById(R.id.layout_step_three);
        relativeViewPayment = findViewById(R.id.layout_step_four);
        relativeImgId1 = findViewById(R.id.rel_img_id1);
        relativeImgId2 = findViewById(R.id.rel_img_id2);
        relativeImgAdd1 = findViewById(R.id.rel_img_add1);
        relativeImgAdd2 = findViewById(R.id.rel_add_images2);
        relBankName = findViewById(R.id.rel_bank_name);
        relNocProof = findViewById(R.id.relative_noc_proof);
        relBankNameCheque = findViewById(R.id.rel_bank_name_cheque);
        relativeSignature = findViewById(R.id.relative_layout_step_five);


        btnPreviousOne = findViewById(R.id.btn_previous_1);
        btnPreviousTwo = findViewById(R.id.btn_previous_2);
        btnPreviousThree = findViewById(R.id.btn_previous_3);
        btnPreviousFour = findViewById(R.id.btn_previous_4);

        btnNext1 = findViewById(R.id.btn_next1);
        btnNext2 = findViewById(R.id.btn_next2);
        btnNext3 = findViewById(R.id.btn_next3);
        btnNext4 = findViewById(R.id.btn_next_4);
        btnSubmit = findViewById(R.id.btn_submit);
//        btnVerifyOTP = findViewById(R.id.btn_verify_otp);
//        btnSMSVerification = findViewById(R.id.btn_sms_verification);
        txtSMSVerification = findViewById(R.id.txt_sms_verification);
        txtSMSVerification.setText(Html.fromHtml("<u>SMS Verification</u>"));
        txtVerify = findViewById(R.id.txt_verify_otp);
        txtVerify.setText(Html.fromHtml("<u>Verify OTP</u>"));
        txtResendOTP = findViewById(R.id.txt_resend_otp);
        txtResendOTP.setText(Html.fromHtml("<u>Resend OTP</u>"));

        imgTakeADD1 = findViewById(R.id.img_take_add1);
        imgTakeADD2 = findViewById(R.id.img_take_add2);
        imgTakeID1 = findViewById(R.id.img_take_id1);
        imgTakeID2 = findViewById(R.id.img_take_id2);
        imgTakeNoc = findViewById(R.id.img_take_noc);
        imgCheque = findViewById(R.id.img_take_cheque);
        imgDD = findViewById(R.id.img_take_dd);
        //imgConsumerPhoto=findViewById(R.id.image_view_consumer);
        //defaultConsumerPhoto();
        //imgConsumerPhoto.setImageResource(R.drawable.ic_action_default_user_icon);


        imgCancelADD1 = findViewById(R.id.img_cancel_add1);
        imgCancelADD2 = findViewById(R.id.img_cancel_add2);
        imgCancelID1 = findViewById(R.id.img_cancel_id1);
        imgCancelID2 = findViewById(R.id.img_cancel_id2);
        imgCancelNoc = findViewById(R.id.img_cancel_id_noc);
        imgCancelCheque = findViewById(R.id.img_cancel_cheque);
        imgCancelDD = findViewById(R.id.img_cancel_dd);

        imgTakeADD1.setOnClickListener(this);
        imgTakeADD2.setOnClickListener(this);
        imgTakeID1.setOnClickListener(this);
        imgTakeID2.setOnClickListener(this);
        imgTakeNoc.setOnClickListener(this);
        imgCheque.setOnClickListener(this);
        imgDD.setOnClickListener(this);

        //imgConsumerPhoto.setOnClickListener(this); TODO

        btnPreviousOne.setOnClickListener(this);
        btnPreviousTwo.setOnClickListener(this);
        btnPreviousThree.setOnClickListener(this);
        btnPreviousFour.setOnClickListener(this);

        imgCancelADD1.setOnClickListener(this);
        imgCancelADD2.setOnClickListener(this);
        imgCancelID1.setOnClickListener(this);
        imgCancelID2.setOnClickListener(this);
        imgCancelNoc.setOnClickListener(this);
        imgCancelCheque.setOnClickListener(this);
        imgCancelDD.setOnClickListener(this);

        btnNext1.setOnClickListener(this);
        btnNext2.setOnClickListener(this);
        btnNext3.setOnClickListener(this);
        btnNext4.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        btnClearConsumer = findViewById(R.id.btn_clear_consumer);
        btnClearConsumer.setOnClickListener(this);

//        btnVerifyOTP.setOnClickListener(this);
//        btnSMSVerification.setOnClickListener(this);
        txtSMSVerification.setOnClickListener(this);
        txtResendOTP.setOnClickListener(this);

        InputFilter filter = new InputFilter.AllCaps();

        edtConsumerName = findViewById(R.id.edt_applicant_company_name);
        edtMobile = findViewById(R.id.edt_mobile_number);
        edtAadharNumber = findViewById(R.id.edt_aadhar_number);
        edtEmailId = findViewById(R.id.edt_email_id);
        edtFlatNumber = findViewById(R.id.edt_flat_no);
        edtSocietyBuildingName = findViewById(R.id.edt_building_name);
        edtLocation = findViewById(R.id.edt_location);
//        edtArea = findViewById(R.id.edt_area);
        edtOther = findViewById(R.id.edt_other);
        edtAmountPayable = findViewById(R.id.edt_amount_payable);
        edtAmountPayable.setEnabled(false);
        edtChequeNo = findViewById(R.id.edt_cheque_no);
        edtChequeBranch = findViewById(R.id.edt_cheque_branch);
        edtChequeDate = findViewById(R.id.edt_cheque_date);
        edtDDNo = findViewById(R.id.edt_dd_no);
        edtOtherBank = findViewById(R.id.edt_other_bank);
        edtIFSCCheque = findViewById(R.id.edt_cheque_ifsc);
//        edtIFSCCheque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtIFSCCheque.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11), filter});
        edtIFSCDD = findViewById(R.id.edt_dd_ifsc);
//        edtIFSCDD.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtIFSCDD.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11), filter});
        edtOtherBankCheque = findViewById(R.id.edt_other_bank_cheque);
        edtDDDate = findViewById(R.id.edt_dd_date);
        edtDDBranch = findViewById(R.id.edt_dd_branch);
        edtState = findViewById(R.id.edt_state);
        edtCity = findViewById(R.id.edt_city);
        edtEnterOTP = findViewById(R.id.edt_enter_otp);
        edtFloor = findViewById(R.id.edt_floor);
        edtPlotNo = findViewById(R.id.edt_plot_no);
        edtWing = findViewById(R.id.edt_wing);
        edtSocietyName = findViewById(R.id.edt_society_name);
        edtRoadNo = findViewById(R.id.edt_road_name);
        edtLandmark = findViewById(R.id.edt_landmark);
        edtDistrict = findViewById(R.id.edt_district);
        //edtWard = findViewById(R.id.edt_ward);

        recyclerIDDoc = findViewById(R.id.rc_document);
        layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerIDDoc.setLayoutManager(layoutManager);

        recyclerViewAddDoc = findViewById(R.id.rc_document_address);
        layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewAddDoc.setLayoutManager(layoutManager);

        spinnerState = findViewById(R.id.spinner_state);
        spinnerCity = findViewById(R.id.sp_city);
        spinnerPinCode = findViewById(R.id.sp_pincode);
        spinnerPaymentScheme = findViewById(R.id.spinner_scheme);
        spinnerConsumerCategory = findViewById(R.id.spinner_consumer_category);
        spinnerConsumerSubCategory = findViewById(R.id.spinner_consumer_sub_category);
        spinnerBankName = findViewById(R.id.spinner_bank_name);
        spinnerArea = findViewById(R.id.sp_area);
        spinnerBankNameCheque = findViewById(R.id.spinner_bank_name_cheque);
        //spinnerWard = findViewById(R.id.sp_ward);TODO


        //spinnerLocation = findViewById(R.id.sp_location);TODO
        //spinnerLandmark = findViewById(R.id.sp_landmark);TODO


        linearChequeDetails = findViewById(R.id.linear_cheque_details);
        linearDDDetails = findViewById(R.id.linear_dd_details);
        linearNocDetails = findViewById(R.id.linear_noc_details);
        linearOTP = findViewById(R.id.linear_otp);


        viewConsumerSignature = findViewById(R.id.view_signature_consumer);
        signatureViewConsumer = new SignatureView(mContext);
        viewConsumerSignature.addView(signatureViewConsumer);


        radioGroup = findViewById(R.id.radio_group_connectivity);
        radioYes = findViewById(R.id.radio_yes);
        radioNo = findViewById(R.id.radio_no);

        // Toolbar toolbar =findViewById(R.id.toolbar);

        //toolImage= toolbar.findViewById(R.id.img_consumer);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                        isConnected = "YES";
                        break;
                    case R.id.radio_no:
                        isConnected = "NO";
                        break;
                }
            }
        });

        //jayshree changes
        edtFlatNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String prefix = getString(R.string.house_number) + " ";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    edtFlatNumber.setText(prefix + cleanString);
                    edtFlatNumber.setSelection(edtFlatNumber.getText().length());
                }

            }
        });

        edtFloor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String prefix = getString(R.string.floor_no_new) + " ";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    edtFloor.setText(prefix + cleanString);
                    edtFloor.setSelection(edtFloor.getText().length());
                }


            }
        });

        edtPlotNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String prefix = getString(R.string.plot_no_new) + " ";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    edtPlotNo.setText(prefix + cleanString);
                    edtPlotNo.setSelection(edtPlotNo.getText().length());
                }

            }
        });

        edtWing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String prefix = getString(R.string.wing_new) + " ";
                if (!editable.toString().startsWith(prefix)) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (editable.toString().startsWith(deletedPrefix)) {
                        cleanString = editable.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = editable.toString().replaceAll(prefix, "");
                    }
                    edtWing.setText(prefix + cleanString);
                    edtWing.setSelection(edtWing.getText().length());
                }
            }
        });

        //****************************************


       /* spinnerOccupation = findViewById(R.id.spinner_occupation);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mContext.getResources().getStringArray(R.array.occupation)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOccupation.setAdapter(dataAdapter2);
        spinnerOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 3) {
                    edtOther.setVisibility(View.VISIBLE);
                } else {
                    edtOther.setVisibility(View.GONE);
                    edtOther.setText(AppConstants.BLANK_STRING);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });*/
        spinnerPremisesType = findViewById(R.id.spinner_premises_type);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mContext.getResources().getStringArray(R.array.premises)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPremisesType.setAdapter(dataAdapter3);
        if (isRejected) {
            if (newRegistrationModel.premise.equals("OWNED")) {
                spinnerPremisesType.setSelection(1);
            } else {
                spinnerPremisesType.setSelection(2);
            }
        }
        spinnerPremisesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    relNocProof.setVisibility(View.VISIBLE);
                    linearNocDetails.setVisibility(View.VISIBLE);

                } else {
                    relNocProof.setVisibility(View.GONE);
                    linearNocDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPaymentMethod = findViewById(R.id.spinner_payment_type);
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
                mContext.getResources().getStringArray(R.array.payment_type)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(dataAdapter4);
        if (isRejected) {
            if (newRegistrationModel.paymentMethod.equals(getString(R.string.cheque))) {
                spinnerPaymentMethod.setSelection(1);
            } else {
                spinnerPaymentMethod.setSelection(2);
            }
        }
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                /*
                if (position == 1) {
                    edtChequeNo.setVisibility(View.VISIBLE);
                } else {
                    edtChequeNo.setVisibility(View.GONE);
                }*/

                if (position == 1) {
                    linearChequeDetails.setVisibility(View.VISIBLE);
                    linearDDDetails.setVisibility(View.GONE);
                    viewCheque.setOnClickListener(new View.OnClickListener() {
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
                                    edtChequeDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);
                                    selectedChequeDate = edtChequeDate.getText().toString().trim();
                                }
                            }, mYear, mMonth, mDay);
                            datePickerDialog.getDatePicker().setMinDate(newDate.getTime());
                            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                            datePickerDialog.show();
                        }
                    });
                } else if (position == 2) {
                    linearDDDetails.setVisibility(View.VISIBLE);
                    linearChequeDetails.setVisibility(View.GONE);

                    viewDD.setOnClickListener(new View.OnClickListener() {
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
                                    edtDDDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year);

                                }
                            }, mYear, mMonth, mDay);
                            datePickerDialog.getDatePicker().setMinDate(newDate.getTime());
                            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                            datePickerDialog.show();
                        }
                    });
                } else {
                    linearDDDetails.setVisibility(View.GONE);
                    linearChequeDetails.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeViewAddressInfo.getVisibility() == View.VISIBLE) {
                    relativeViewGeneralInfo.setVisibility(View.VISIBLE);
                    relativeViewAddressInfo.setVisibility(View.GONE);
                    relativeViewUploadDoc.setVisibility(View.GONE);
                    relativeViewPayment.setVisibility(View.GONE);
                } else if (relativeViewUploadDoc.getVisibility() == View.VISIBLE) {
                    relativeViewUploadDoc.setVisibility(View.GONE);
                    relativeViewAddressInfo.setVisibility(View.VISIBLE);
                    relativeViewUploadDoc.setVisibility(View.GONE);
                    relativeViewPayment.setVisibility(View.GONE);
                } else if (relativeViewPayment.getVisibility() == View.VISIBLE) {
                    relativeViewUploadDoc.setVisibility(View.GONE);
                    relativeViewAddressInfo.setVisibility(View.GONE);
                    relativeViewUploadDoc.setVisibility(View.VISIBLE);
                    relativeViewPayment.setVisibility(View.GONE);
                } else
                    onBackPressed();
            }
        });

        edtConsumerName.setTypeface(mRegular);
        edtAadharNumber.setTypeface(mRegular);
        edtEmailId.setTypeface(mRegular);
        edtFlatNumber.setTypeface(mRegular);
        edtSocietyBuildingName.setTypeface(mRegular);
        edtLocation.setTypeface(mRegular);
//        edtArea.setTypeface(mRegular);
        edtMobile.setTypeface(mRegular);
        edtOther.setTypeface(mRegular);
        /*edtIFSCDD.setTypeface(mRegular);
        edtIFSCCheque.setTypeface(mRegular);*/
        edtDDNo.setTypeface(mRegular);
        edtChequeDate.setTypeface(mRegular);
        edtChequeBranch.setTypeface(mRegular);
        edtChequeNo.setTypeface(mRegular);
        edtOtherBank.setTypeface(mRegular);
        edtOther.setTypeface(mRegular);
        edtOtherBankCheque.setTypeface(mRegular);
        edtDDDate.setTypeface(mRegular);
        edtState.setTypeface(mRegular);
        edtCity.setTypeface(mRegular);
        edtEnterOTP.setTypeface(mRegular);
        edtFloor.setTypeface(mRegular);
        edtPlotNo.setTypeface(mRegular);
        edtWing.setTypeface(mRegular);
        edtSocietyName.setTypeface(mRegular);
        edtRoadNo.setTypeface(mRegular);
        edtLandmark.setTypeface(mRegular);
        edtDistrict.setTypeface(mRegular);

        btnNext1.setTypeface(mRegular);
        btnNext2.setTypeface(mRegular);
        btnNext3.setTypeface(mRegular);
        btnSubmit.setTypeface(mRegular);
//        btnVerifyOTP.setTypeface(mRegular);
//        btnSMSVerification.setTypeface(mRegular);
        txtSMSVerification.setTypeface(mRegular);
        txtResendOTP.setTypeface(mRegular);

        relativeIdProof = findViewById(R.id.relative_id_proof);
        relativeIdProof.setOnClickListener(this);
        relativeAddressProof = findViewById(R.id.relative_address_proof);
        relativeAddressProof.setOnClickListener(this);

        imgIdProofArrow = findViewById(R.id.img_id_proof_arrow);
        imgIdProofArrow.setRotation(180);
        imgAddressProofArrow = findViewById(R.id.img_address_proof_arrow);
        imgNocProofArrow = findViewById(R.id.img_noc_proof_arrow);

        nestedIdProof = findViewById(R.id.nested_id_proof);
        nestedIdProof.setVisibility(View.VISIBLE);
        nestedAddressProof = findViewById(R.id.nested_address_proof);
        nestedAddressProof.setVisibility(View.GONE);
        nestedSignature = findViewById(R.id.nested_view_six);

        if (comingFrom.equals(getString(R.string.new_nsc))) {
            edtConsumerName.setText("");
            edtMobile.setText("");
            enquiryId = "";
        } else if (comingFrom.equals(getString(R.string.edit_nsc))) {
            edtConsumerName.setText(todayModel.getConsumerName());
            edtMobile.setText(todayModel.getMobileNumber());
            edtMobile.setText(todayModel.getMobileNumber());
            enquiryId = todayModel.requestId;
        }

        edtState.setText(AppPreferences.getInstance(mContext).getString(AppConstants.STATE, ""));
        edtCity.setText(AppPreferences.getInstance(mContext).getString(AppConstants.CITY, ""));
        edtDistrict.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_DISTRICT, ""));


        edtState.setClickable(false);
        edtState.setEnabled(false);
        edtCity.setClickable(false);
        edtCity.setEnabled(false);
        edtDistrict.setClickable(false);
        edtDistrict.setEnabled(false);

        edtAadharNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!edtAadharNumber.getText().toString().trim().isEmpty()) {
                    if (edtAadharNumber.getText().toString().trim().length() == 12) {
                        checkAadharNumber();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (checkPlayServices()) {
            buildGoogleApiClient();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CommonUtility.askForPermissions(mContext, App.getInstance().permissions);
                }
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        getBankNames();
        //getPaymentScheme();
        // initiateVerification();

        getCategory();
        if (comingFrom.equals(getString(R.string.rejected_nsc))) {
            setValues();
        }
    }


    public void getBankNames() {

        bankNames = DatabaseManager.getBanks(mContext);

        hashMapBankName.clear();
        hashMapBankName.put("0", getString(R.string.select_bank));
        hashMapBankName.put("1", getString(R.string.others));

        bankArray = new ArrayList<>();
        bankArray.add(0, getString(R.string.select_bank));
        bankArray.add(1, getString(R.string.others));
        for (Consumer con : bankNames) {
            bankArray.add(con.getBankName());

        }


        hashMapBankChequeArray.clear();
        hashMapBankChequeArray.put("0", getString(R.string.select_bank));
        hashMapBankChequeArray.put("1", getString(R.string.others));

        bankArrayCheque = new ArrayList<>();
        bankArrayCheque.add(0, getString(R.string.select_bank));
        bankArrayCheque.add(1, getString(R.string.others));
        for (Consumer con : bankNames) {
            bankArrayCheque.add(con.getBankName());
        }


        if (bankArrayCheque.size() > 2) {
            setBankSpinnerCheque();
        }
        if (bankArray.size() > 2) {
            setBankSpinner();

        }

/*
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
//            showLoadingDialog();
            try {

                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_BANK_NAME_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_BANK_NAME_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onClick(View view) {
        if (view == btnNext1) {
            // TODO uncomment for getting OTP.
            /*if (clickCount == 0) {
                Toast.makeText(mContext, "Please, validate your mobile number", Toast.LENGTH_SHORT).show();
            } else */
            if (edtAadharNumber.getText().toString().trim().length() != 0 &&
                    edtAadharNumber.getText().toString().trim().length() == 12) {
                checkAadharNumber();
            }
//            else
            validateGeneralInfo();

        } else if (view == btnNext2) {

            validateAddressInfo();
        } else if (view == txtSMSVerification) {
            if (edtMobile.getText().length() < 10) {
                Toast.makeText(this, "Please enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            } else {
                getOTP();
            }
            clickCount = clickCount + 1;
            enableInputField(true);
            /*if (edtEnterOTP.getText().toString().equals(mStrOTP)) {

                Toast.makeText(RegistrationFormActivity.this, "Verified Mobile Number",
                        Toast.LENGTH_LONG).show();

                *//*enableInputField(false);
                CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.already_verified),
                        getString(R.string.otp_already_verifed), false);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
                customDialog.setCancelable(false);*//*
//                Toast.makeText(getApplicationContext(), getString(R.string.otp_already_verifed), Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();*/
        } else if (view == txtResendOTP) {
            if (!edtEnterOTP.getText().toString().equals(mStrOTP)) {
                txtResendOTP.setClickable(true);
                getOTP();
            }
        } else if (view == btnNext3) {
            imageCount = 0;
            imageCountAdd = 0;
            if (validateIdProof()) {
                relativeViewUploadDoc.setVisibility(View.GONE);
                relativeViewPayment.setVisibility(View.VISIBLE);
                getPaymentScheme();
            }

        } else if (view == btnNext4) {

            checkPaymentValidations();
        } else if (view == btnClearConsumer) {
            signatureViewConsumer.clearSignature();
        } else if (view.equals(btnSubmit)) {

            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            PackageManager pm = mContext.getPackageManager();
            boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

            if (isGPSEnabled && hasGps) {
                captureLocation();

                if (signatureViewConsumer.getSignature() != null) {
                    Bitmap bitmap = null, galleryBitmap = null;


                    Uri tempUriConsumer = getImageUri(mContext, signatureViewConsumer.getSignature());
                    fileConsumerSign = new File(getRealPathFromURI(tempUriConsumer));

                    try {
                        galleryBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUriConsumer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap = Bitmap.createScaledBitmap(
                            galleryBitmap, AppConstants.IMAGE_WIDTH, AppConstants.IMAGE_HEIGHT, false);
                    bitmapSign = bitmap;

                    if (isRejected) {
                        new UploadData().execute();
                    } else {
                        showDialogForUploadOption(mContext);
//                        doSubmitOps();
                    }

                } else {
                    Toast.makeText(mContext, getString(R.string.please_add_consumer_signature), Toast.LENGTH_SHORT).show();
                }
//                checkPaymentValidations();
            } else {
                showLocationEnableDialog();
            }

        } else if (view.equals(imgTakeID1)) {
            Count = 1;
            File1 = null;
            relativeImgId1.setVisibility(View.VISIBLE);
            showPictureDialog();
        } else if (view.equals(imgTakeID2)) {
            Count = 2;
            File2 = null;
            relativeImgId2.setVisibility(View.VISIBLE);
            showPictureDialog();
        } else if (view.equals(imgTakeADD1)) {
            Count = 3;
            File3 = null;
            relativeImgAdd1.setVisibility(View.VISIBLE);
            showPictureDialog();
        } else if (view.equals(imgTakeADD2)) {
            Count = 4;
            File4 = null;
            relativeImgAdd2.setVisibility(View.VISIBLE);
            showPictureDialog();
        } else if (view.equals(imgTakeNoc)) {
            Count = 5;
            File5 = null;
            linearNocDetails.setVisibility(View.VISIBLE);
            showPictureDialog();

        } else if (view.equals(imgCheque)) {
            Count = 6;
            fileCheque = null;
            callCamera();
        } else if (view.equals(imgDD)) {
            Count = 7;
            fileDD = null;
            callCamera();
        } else if (view.equals(imgConsumerPhoto)) {
            Count = 8;
            fileConsumerPhoto = null;
            showPictureDialog();
        } else if (view == relativeIdProof) {
            if (idProof) {
                nestedIdProof.setVisibility(View.VISIBLE);
                imgIdProofArrow.setRotation(180);

                nestedAddressProof.setVisibility(View.GONE);
                imgAddressProofArrow.setRotation(0);

                idProof = false;
            } else {
                nestedIdProof.setVisibility(View.GONE);
                imgIdProofArrow.setRotation(0);
                idProof = true;
            }
        } else if (view == relativeAddressProof) {
            if (addressProof) {
                nestedAddressProof.setVisibility(View.VISIBLE);
                imgAddressProofArrow.setRotation(180);

                nestedIdProof.setVisibility(View.GONE);
                imgIdProofArrow.setRotation(0);

                addressProof = false;
            } else {
                nestedAddressProof.setVisibility(View.GONE);
                imgAddressProofArrow.setRotation(0);
                addressProof = true;
            }
        } else if (view == relNocProof) {
            if (premise.equals(getString(R.string.rented))) {
                if (nocProof) {
                    linearNocDetails.setVisibility(View.VISIBLE);
                    imgNocProofArrow.setRotation(180);

                    nestedAddressProof.setVisibility(View.GONE);
                    nestedIdProof.setVisibility(View.GONE);

                    imgIdProofArrow.setRotation(0);
                    imgAddressProofArrow.setRotation(0);

                    nocProof = false;
                } else {
                    relNocProof.setVisibility(View.GONE);
                    imgNocProofArrow.setRotation(0);
                    nocProof = true;
                }
            } else {
                relNocProof.setVisibility(View.GONE);
                imgNocProofArrow.setRotation(0);
                nocProof = true;
            }

        } else if (view == btnPreviousOne) {
            relativeViewGeneralInfo.setVisibility(View.VISIBLE);
            relativeViewAddressInfo.setVisibility(View.GONE);
            relativeViewUploadDoc.setVisibility(View.GONE);
            relativeViewPayment.setVisibility(View.GONE);
        } else if (view == btnPreviousTwo) {
            relativeViewGeneralInfo.setVisibility(View.GONE);
            relativeViewAddressInfo.setVisibility(View.VISIBLE);
            relativeViewUploadDoc.setVisibility(View.GONE);
            relativeViewPayment.setVisibility(View.GONE);
        } else if (view == btnPreviousThree) {
            relativeViewGeneralInfo.setVisibility(View.GONE);
            relativeViewAddressInfo.setVisibility(View.GONE);
            relativeViewUploadDoc.setVisibility(View.VISIBLE);
            relativeViewPayment.setVisibility(View.GONE);
        } else if (view == btnPreviousFour) {
            relativeViewGeneralInfo.setVisibility(View.GONE);
            relativeViewAddressInfo.setVisibility(View.GONE);
            relativeViewUploadDoc.setVisibility(View.GONE);
            relativeViewPayment.setVisibility(View.VISIBLE);
        } else if (view == imgCancelID1) {
            File1 = null;
            if (imageCount > 0) {
                imageCount--;
            }
            imgTakeID1.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgId1))
                imgRemoveList.add(imgId1);
        } else if (view == imgCancelID2) {
            File2 = null;
            if (imageCount > 0) {
                imageCount--;
            }
            imgRemoveList.add(imgId2);
            imgTakeID2.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgId2))
                imgRemoveList.add(imgId2);
        } else if (view == imgCancelADD1) {
            File3 = null;
            if (imageCountAdd > 0) {
                imageCountAdd--;
            }
            imgRemoveList.add(imgId3);
            imgTakeADD1.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgId3))
                imgRemoveList.add(imgId3);
        } else if (view == imgCancelADD2) {
            imgRemoveList.add(imgId4);
            File4 = null;
            if (imageCountAdd > 0) {
//                imageCountAdd--;
                imageCountAdd--;
            }
            imgTakeADD2.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgId4))
                imgRemoveList.add(imgId4);
        } else if (view == imgCancelNoc) {
            File5 = null;
            imgRemoveList.add(imgId5);
            imgTakeNoc.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgId5))
                imgRemoveList.add(imgId5);
        } else if (view == imgCancelCheque) {
            fileCheque = null;
            imgRemoveList.add(imgIdCheque);
            imgCheque.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgIdCheque))
                imgRemoveList.add(imgIdCheque);
        } else if (view == imgCancelDD) {
            fileDD = null;
            imgRemoveList.add(imgIdDD);
            imgDD.setImageResource(R.drawable.ic_action_camera1);
            if (!imgRemoveList.contains(imgIdDD))
                imgRemoveList.add(imgIdDD);
        }

    }

    private void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri file = getOutputMediaFileUri(PROFILE_IMAGE_DIRECTORY_NAME, ProfileImageName);
        List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            mContext.grantUriPermission(packageName, file, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, AppConstants.CAMERA_RESULT_CODE);
    }

    private void getCategory() {
        category = DatabaseManager.getCategory(mContext, AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
        hashMapCategory.clear();
        hashMapCategory.put("0", getString(R.string.select_consumer_category));
        for (Consumer con : category)
            hashMapCategory.put(con.getConsumerCategoryId(), con.getConsumerCategory());
        setCategorySpinner();

    }

    private void getSubCategory() {
        subCategory = DatabaseManager.getSubCategory(mContext, AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
        hashMapSubCategory.clear();
        hashMapSubCategory.put("0", getString(R.string.select_consumer_sub_category));
        for (Consumer con : subCategory)
            hashMapSubCategory.put(con.getConsumer_subcategory_id(), con.getConsumer_subcategory());
        setSubCategorySpinner();

    }

    private void getState() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            showLoadingDialog();
            try {
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.GET, null,
                        ApiConstants.GET_STATE, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_STATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getCity() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("state_id", selectedState);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_CITY, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CITY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    private void getPinCode(String areaId) {
        pincode = DatabaseManager.getPincode(mContext, AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""), areaId);
        hashMapPinCode.clear();
        hashMapPinCode.put("0", getString(R.string.select_pin_code));
        for (Consumer con : pincode)
            hashMapPinCode.put(con.getPincode_id(), con.getPincode());
        setPinCodeSpinner();
    }

    private void getArea() {
        consumerArea = DatabaseManager.getAreas(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));

        haspMapArea.clear();
        haspMapArea.put("0", getString(R.string.select_area));
        for (Consumer con : consumerArea)
            haspMapArea.put(con.getAreaID(), con.getArea());

        setAreaSpinner();

        signatureViewConsumer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disable the scroll view to intercept the touch event
                        nestedSignature.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        // Allow scroll View to interceot the touch event
                        nestedSignature.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        nestedSignature.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

    }

    private void getWard(String areaId) {
        wardName = DatabaseManager.getWard(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""), areaId);
        Log.e("WARD", "nxxxxxxxxxx" + wardName);
        hashMapWard.clear();
        hashMapWard.put("0", getString(R.string.select_ward));
        for (Consumer con : wardName)
            hashMapWard.put(con.getWardID(), con.getWard());

        setWardSpinner();
    }

    private void getLocation(String areaId) {
        location = DatabaseManager.getLocation(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""), areaId);
        Log.e("Location", "nxxxxxxxxxx" + location);

        hashMapLocation.clear();
        hashMapLocation.put("0", getString(R.string.select_location));
        for (Consumer con : location)
            hashMapLocation.put(con.getLocationID(), con.getLocation());

        setLocationSpinner();
    }

    private void getLandmark(String areaId) {
        landmark = DatabaseManager.getLandmark(mContext, AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""), areaId);
        Log.e("Landmark", "nxxxxxxxxxx" + landmark);
        hashMapLandmark.clear();
        hashMapLandmark.put("0", getString(R.string.select_landmark));
        for (Consumer con : landmark)
            hashMapLandmark.put(con.getLandmarkID(), con.getLandmark());

        setLandMarkSpinner();
    }

    private void getDocumentListID() {
        documentList = DatabaseManager.getIdProof(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
        DocumentIdAdapter.checkParamsListId.clear();


        DocumentIdAdapter Adapter = new DocumentIdAdapter(mContext, documentList, getString(R.string.edit_id_proof));
        recyclerIDDoc.setAdapter(Adapter);
    }

    private void getAddDocumentList() {
        addDocumnetList = DatabaseManager.getAddProof(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));

        DocumentIdAdapter.checkParamsListAddress.clear();


        DocumentIdAdapter Adapter2 = new DocumentIdAdapter(mContext, addDocumnetList, getString(R.string.edit_add_proof));
        recyclerViewAddDoc.setAdapter(Adapter2);
    }

    private void getPaymentScheme() {

        schemeName = DatabaseManager.getPayment(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));

        hashMapPaymentScheme.clear();
        hashMapPaymentScheme.put(getString(R.string.select_payment_scheme_mandatory), "0");
        for (Consumer con : schemeName)
            hashMapPaymentScheme.put(con.getSchemeName(), con.getSchemeAmount());

        setSchemeSpinner();
    }

    private void setStateSpinner() {
        Collection<String> valueSet = null;
        stateArray = new ArrayList<>();
        if (hashMapState != null && hashMapState.size() > 0) {
            for (int i = 0; i < hashMapState.size(); i++) {
                valueSet = sortByKey(hashMapState).values();
                stateArray = new ArrayList<>(valueSet);
            }
        } else stateArray.add(getString(R.string.select_state));

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, stateArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(dataAdapter);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapState != null && hashMapState.size() > 0) {
                    List<String> stateList = getKeysFromValue(hashMapState, spinnerState.getSelectedItem().toString());
                    selectedState = stateList.get(0);

                    if (selectedState.equals("0")) {
                        hashMapCity.clear();
                        haspMapArea.clear();
                        hashMapPinCode.clear();
                        setCitySpinner();
                        setAreaSpinner();
                        //setPinCodeSpinner();
                    } else
                        getCity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setCitySpinner() {
        Collection<String> valueSet = null;
        cityArray = new ArrayList<>();
        if (hashMapCity != null && hashMapCity.size() > 0) {
            for (int i = 0; i < hashMapCity.size(); i++) {
                valueSet = sortByKey(hashMapCity).values();
                cityArray = new ArrayList<>(valueSet);
            }
        } else cityArray.add(getString(R.string.select_city));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, cityArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(dataAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapCity != null && hashMapCity.size() > 0) {
                    cityList = getKeysFromValue(hashMapCity, spinnerCity.getSelectedItem().toString());
                    selectedCity = cityList.get(0);
                    if (selectedCity.equals("0")) {
                        haspMapArea.clear();
                        setAreaSpinner();
                    } else
                        getArea();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setPinCodeSpinner() {
        Collection<String> valueSet = null;
        pinCodeArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapPinCode != null && hashMapPinCode.size() > 0) {
            for (int i = 0; i < hashMapPinCode.size(); i++) {
                valueSet = sortByKey(hashMapPinCode).values();
                pinCodeArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapPinCode).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        } else pinCodeArray.add(getString(R.string.select_pin_code));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, pinCodeArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPinCode.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.pincode)) {
                    spinnerPinCode.setSelection(a);
                }
            }
        }
        spinnerPinCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapPinCode != null && hashMapPinCode.size() > 0) {
                    pinCodeList = getKeysFromValue(hashMapPinCode, spinnerPinCode.getSelectedItem().toString());
                    selectedPinCode = pinCodeList.get(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setAreaSpinner() {
        Collection<String> valueSet = null;
        areaArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();

        if (haspMapArea != null && haspMapArea.size() > 0) {
            for (int i = 0; i < haspMapArea.size(); i++) {
                valueSet = sortByKey(haspMapArea).values();
                areaArray = new ArrayList<>(valueSet);
                keySet = sortByKey(haspMapArea).keySet();
                keySetArray = new ArrayList<>(keySet);

            }
        } else areaArray.add(getString(R.string.select_area));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, areaArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.area)) {
                    spinnerArea.setSelection(a);
                }
            }
        }
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (haspMapArea != null && haspMapArea.size() > 0) {
                    areaList = getKeysFromValue(haspMapArea, spinnerArea.getSelectedItem().toString());
                    areaListPincode = getKeysFromValue(haspMapArea, spinnerArea.getSelectedItem().toString());
                    areaListLocation = getKeysFromValue(haspMapArea, spinnerArea.getSelectedItem().toString());
                    areaListLandmark = getKeysFromValue(haspMapArea, spinnerArea.getSelectedItem().toString());

                    selectedArea = areaList.get(0);
                    selectedAreaPincode = areaListPincode.get(0);
                    selectedAreaLocation = areaListLocation.get(0);
                    selectedAreaLandmark = areaListLandmark.get(0);
                    if (selectedArea.equals("0")) {
                        hashMapPinCode.clear();
                        setPinCodeSpinner();
                    } else
                        getPinCode(selectedArea);
                    //TODO
                    /*if(selectedAreaPincode.equals("0")){
                        hashMapPinCode.clear();
                        setPinCodeSpinner();
                    }
                    else
                        getPinCode(selectedAreaPincode);
                    if(selectedAreaLocation.equals("0")){
                        hashMapLocation.clear();
                        setLocationSpinner();
                    }
                    else
                        getLocation(selectedAreaLocation);
                    if(selectedAreaLandmark.equals("0")){
                        hashMapLandmark.clear();
                        setLandMarkSpinner();
                    }
                    else
                        getLandmark(selectedAreaLandmark);
*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setWardSpinner() {
        Collection<String> valueSet = null;
        wardArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        //hashMapWard.put("0", getString(R.string.select_ward));
        if (hashMapWard != null && hashMapWard.size() > 0) {
            for (int i = 0; i < hashMapWard.size(); i++) {
                valueSet = sortByKey(hashMapWard).values();
                wardArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapWard).keySet();
                keySetArray = new ArrayList<>(keySet);
            }

            //else wardArray.add(getString(R.string.select_ward));
        } else wardArray.add(getString(R.string.select_ward));
        //TODO
       /* if(hashMapWard.size() == 2) {
            for (int i = 0; i < hashMapWard.size(); i++) {
                valueSet = sortByKey(hashMapWard).values();
                wardArray = new ArrayList<>(valueSet);
                wardArray.remove(0);
                keySet = sortByKey(hashMapWard).keySet();
                keySetArray = new ArrayList<>(keySet);

            }
         }*/

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, wardArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWard.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.wardName)) {
                    spinnerWard.setSelection(a);

                }
            }
        }
        /*if(keySetArray.size()==1){
            //spinnerWard.setVisibility(View.GONE);
            //edtWard.setVisibility(View.VISIBLE);
            String ward=wardArray.get(1);
            spinnerWard.setSelection(Integer.parseInt(ward));
        }*/
        spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapWard != null && hashMapWard.size() > 0) {
                    wardList = getKeysFromValue(hashMapWard, spinnerWard.getSelectedItem().toString());
                    selectedWard = wardList.get(0);
                    if (selectedWard.equals("0")) {
                        hashMapPinCode.clear();
                        //setPinCodeSpinner();
                    } /*else
                        getPinCode(selectedWard);*/

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


    }

    private void setLocationSpinner() {
        Collection<String> valueSet = null;
        locationArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapLocation != null && hashMapLocation.size() > 1) {
            for (int i = 0; i < hashMapLocation.size(); i++) {
                valueSet = sortByKey(hashMapLocation).values();
                locationArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapLocation).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        } else locationArray.add(getString(R.string.select_location));

        if (hashMapLocation != null && hashMapLocation.size() == 1) {
            locationArray.remove(getString(R.string.select_location));
            for (int i = 0; i < hashMapLocation.size(); i++) {
                valueSet = sortByKey(hashMapLocation).values();
                locationArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapLocation).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        }
        //else  wardArray.remove(getString(R.string.select_ward));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, locationArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.location)) {
                    spinnerLocation.setSelection(a);

                }
            }
        }
        /*if(keySetArray.size()==1){
            edtWard.setVisibility(View.VISIBLE);
            nnnnnnnnnnnnnnnn
        }*/
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapLocation != null && hashMapLocation.size() > 0) {
                    locationList = getKeysFromValue(hashMapLocation, spinnerLocation.getSelectedItem().toString());
                    selectedLocation = locationList.get(0);
                    if (selectedLocation.equals("0")) {
                        hashMapLocation.clear();
                        //setPinCodeSpinner();
                    } /*else
                        getPinCode(selectedWard);*/

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setLandMarkSpinner() {
        Collection<String> valueSet = null;
        landmarkArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapLandmark != null && hashMapLandmark.size() > 1) {
            for (int i = 0; i < hashMapLandmark.size(); i++) {
                valueSet = sortByKey(hashMapLandmark).values();
                landmarkArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapLandmark).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        } else landmarkArray.add(getString(R.string.select_landmark));

        if (hashMapLandmark != null && hashMapLandmark.size() == 1) {
            landmarkArray.remove(getString(R.string.select_landmark));
            for (int i = 0; i < hashMapLandmark.size(); i++) {
                valueSet = sortByKey(hashMapLandmark).values();
                landmarkArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapLandmark).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        }
        //else  wardArray.remove(getString(R.string.select_ward));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, landmarkArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandmark.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.landmark)) {
                    spinnerLandmark.setSelection(a);

                }
            }
        }
        /*if(keySetArray.size()==1){
            edtWard.setVisibility(View.VISIBLE);
            nnnnnnnnnnnnnnnn
        }*/
        spinnerLandmark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapLandmark != null && hashMapLandmark.size() > 0) {
                    landmarkList = getKeysFromValue(hashMapLandmark, spinnerLandmark.getSelectedItem().toString());
                    selectedLandmark = landmarkList.get(0);
                    if (selectedLandmark.equals("0")) {
                        hashMapLandmark.clear();
                        //setPinCodeSpinner();
                    } /*else
                        getPinCode(selectedWard);*/

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setSchemeSpinner() {
        Collection<String> valueSet;
        schemeArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapPaymentScheme != null && hashMapPaymentScheme.size() > 0) {
            for (int i = 0; i < hashMapPaymentScheme.size(); i++) {
                valueSet = hashMapPaymentScheme.keySet();
                schemeArray = new ArrayList<>(valueSet);
            }
        } else schemeArray.add(getString(R.string.select_payment_scheme_mandatory));


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, schemeArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentScheme.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < schemeArray.size(); a++) {
                if (schemeArray.get(a).equals(newRegistrationModel.schemeName)) {
                    spinnerPaymentScheme.setSelection(a);
                }
            }
        }
        spinnerPaymentScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapPaymentScheme != null && hashMapPaymentScheme.size() > 0) {
//                    paymentSchemeList = getValuesFromKey(hashMapPaymentScheme, spinnerPaymentScheme.getSelectedItem().toString());

                    selectedScheme = spinnerPaymentScheme.getSelectedItem().toString();

//                    edtAmountPayable.setText(getString(R.string.amount_payable) + " : " + paymentSchemeList.get(0) + getString(R.string.rs));
                    edtAmountPayable.setText(getString(R.string.amount_payable) + " : " + hashMapPaymentScheme.get(selectedScheme) + getString(R.string.rs));
                    schemeAmount = Integer.parseInt(String.valueOf(hashMapPaymentScheme.get(selectedScheme)));

                    if (edtAmountPayable.getText().toString().equals((getString(R.string.amount_payable) + " : 0" + getString(R.string.rs)))) {
                        relPaymentMethod.setVisibility(View.GONE);
                        linearChequeDetails.setVisibility(View.GONE);
                        linearDDDetails.setVisibility(View.GONE);
                        spinnerPaymentMethod.setSelection(0);
                    } else {
                        relPaymentMethod.setVisibility(View.VISIBLE);
//                        spinnerPaymentMethod.setSelection(0);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setBankSpinner() {
      /*  Collection<String> valueSet;

        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapBankName != null && hashMapBankName.size() > 0) {
            for (int i = 0; i < hashMapBankName.size(); i++) {
                valueSet = sortByKey(hashMapBankName).values();
                bankArray = new ArrayList<>(valueSet);
            }
        } else bankArray.add(getString(R.string.select_bank));
*/

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, bankArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBankName.setAdapter(dataAdapter);
        if (isRejected) {
            if (newRegistrationModel.paymentMethod.equals(getString(R.string.dd)))
                for (int a = 0; a < bankArray.size(); a++) {
                    if (bankArray.get(a).equals(newRegistrationModel.bank)) {
                        spinnerBankName.setSelection(a);
                    }
                }
        }

        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapBankName != null && hashMapBankName.size() > 0) {
                    bankList = getKeysFromValue(hashMapBankName, spinnerBankName.getSelectedItem().toString());
                    selectedBank = spinnerBankName.getSelectedItem().toString();

//                    getValuesForPaymentMethod();
                    if (position == 0) {
                        edtOtherBank.setVisibility(View.GONE);
                    } else if (position == 1) {
                        edtOtherBank.setVisibility(View.VISIBLE);
                    } else {
                        edtOtherBank.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setBankSpinnerCheque() {
       /* Collection<String> valueSet;
        bankArrayCheque = new ArrayList<>();
        if (hashMapBankChequeArray != null && hashMapBankChequeArray.size() > 0) {
            for (int i = 0; i < hashMapBankChequeArray.size(); i++) {
                valueSet = sortByKey(hashMapBankChequeArray).values();
                bankArrayCheque = new ArrayList<>(valueSet);
            }
        } else bankArrayCheque.add(getString(R.string.select_bank));

*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, bankArrayCheque) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBankNameCheque.setAdapter(dataAdapter);

        if (isRejected) {
            if (newRegistrationModel.paymentMethod.equals(getString(R.string.cheque))) {
                for (int a = 0; a < bankArrayCheque.size(); a++) {
                    if (bankArrayCheque.get(a).equals(newRegistrationModel.bank)) {
                        spinnerBankNameCheque.setSelection(a);
                    }
                }
            }
        }
        spinnerBankNameCheque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapBankChequeArray != null && hashMapBankChequeArray.size() > 0) {
                    bankList = getKeysFromValue(hashMapBankChequeArray, spinnerBankNameCheque.getSelectedItem().toString());
                    selectedBankCheque = spinnerBankNameCheque.getSelectedItem().toString();

//                    getValuesForPaymentMethod();
                    if (position == 0) {
                        edtOtherBankCheque.setVisibility(View.GONE);
                    } else if (spinnerBankNameCheque.getSelectedItem().toString().equals(getString(R.string.others))) {
                        edtOtherBankCheque.setVisibility(View.VISIBLE);
                    } else {
                        edtOtherBankCheque.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private void setCategorySpinner() {
        Collection<String> valueSet;
        categoryArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapCategory != null && hashMapCategory.size() > 0) {
            for (int i = 0; i < hashMapCategory.size(); i++) {
                valueSet = sortByKey(hashMapCategory).values();
                categoryArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapCategory).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        } else categoryArray.add(getString(R.string.select_consumer_category));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categoryArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConsumerCategory.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.consumerCategory)) {
                    spinnerConsumerCategory.setSelection(a);
                }
            }
        }
        spinnerConsumerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapCategory != null && hashMapCategory.size() > 0) {
                    categoryList = getKeysFromValue(hashMapCategory, spinnerConsumerCategory.getSelectedItem().toString());
                    selectedCategory = categoryList.get(0);

                    if (selectedCategory.equals("0")) {
                        hashMapSubCategory.clear();
                        setSubCategorySpinner();
                    } else
                        getSubCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void setSubCategorySpinner() {
        Collection<String> valueSet;
        subCategoryArray = new ArrayList<>();
        Collection<String> keySet;
        ArrayList<String> keySetArray = new ArrayList<>();
        if (hashMapSubCategory != null && hashMapSubCategory.size() > 0) {
            for (int i = 0; i < hashMapSubCategory.size(); i++) {
                valueSet = sortByKey(hashMapSubCategory).values();
                subCategoryArray = new ArrayList<>(valueSet);
                keySet = sortByKey(hashMapSubCategory).keySet();
                keySetArray = new ArrayList<>(keySet);
            }
        } else subCategoryArray.add(getString(R.string.select_consumer_sub_category));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, subCategoryArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorProfileEditText));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, R.color.colorBlack));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConsumerSubCategory.setAdapter(dataAdapter);
        if (isRejected) {
            for (int a = 0; a < keySetArray.size(); a++) {
                if (keySetArray.get(a).equals(newRegistrationModel.consumerSubCategory)) {
                    spinnerConsumerSubCategory.setSelection(a);
                }
            }
        }
        spinnerConsumerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (hashMapSubCategory != null && hashMapSubCategory.size() > 0) {
                    subCategoryList = getKeysFromValue(hashMapSubCategory, spinnerConsumerSubCategory.getSelectedItem().toString());
                    selectSubCategory = subCategoryList.get(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private List getKeysFromValue(Map hm, Object value) {
        Set ref = hm.keySet();
        Iterator it = ref.iterator();
        List list = new ArrayList();

        while (it.hasNext()) {
            Object o = it.next();
            if (hm.get(o).equals(value)) {
                list.add(o);
            }
        }
        return list;
    }

    private void validateGeneralInfo() {
//        if (clickCount == 0) {
        if (edtConsumerName.getText().toString().trim().length() > 0) {
            if (edtMobile.getText().toString().trim().matches("^[0-9][0-9]{9}$")) {
//                if (edtEmailId.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") ||
                if (edtEmailId.getText().toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ||
                        edtEmailId.getText().toString().length() == 0) {
                    if (spinnerConsumerCategory.getSelectedItemPosition() != 0) {
                        if (spinnerConsumerSubCategory.getSelectedItemPosition() != 0) {
                            if (edtAadharNumber.getText().toString().trim().length() == 12 ||
                                    edtAadharNumber.getText().toString().length() == 0) {
                                if (edtAadharNumber.getText().toString().length() != 0) {
                                    if (!isAadharPresent) {
                                        relativeViewGeneralInfo.setVisibility(View.GONE);
                                        relativeViewAddressInfo.setVisibility(View.VISIBLE);
                                        newTodayModel.consumerName = edtConsumerName.getText().toString().trim();
                                        newTodayModel.mobileNumber = edtMobile.getText().toString().trim();
                                        getArea();
                                    } else {
                                        newTodayModel.consumerName = edtConsumerName.getText().toString().trim();
                                        newTodayModel.mobileNumber = edtMobile.getText().toString().trim();
                                        showDialogForAadhar();
                                    }
                                } else {
                                    relativeViewGeneralInfo.setVisibility(View.GONE);
                                    relativeViewAddressInfo.setVisibility(View.VISIBLE);
                                    newTodayModel.consumerName = edtConsumerName.getText().toString().trim();
                                    newTodayModel.mobileNumber = edtMobile.getText().toString().trim();
                                    getArea();

                                }
                            } else {
                                Toast.makeText(mContext, getString(R.string.error_valid_aadhaar_no), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.error_valid_consumer_sub_category), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, getString(R.string.error_valid_consumer_category), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.error_valid_email_id), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, getString(R.string.error_valid_mobile_no), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_valid_consumer_name), Toast.LENGTH_SHORT).show();
        }
    } /*else {
            Toast.makeText(mContext, "Please validate your mobile number ", Toast.LENGTH_SHORT).show();
        }*//*
    }*/

    private void validateAddressInfo() {
        if (edtFlatNumber.getText().toString().trim().length() > 0 && edtFlatNumber.getText().toString().length() > 10)
            if (edtSocietyBuildingName.getText().toString().trim().length() > 0)
                if (edtRoadNo.getText().toString().trim().length() > 0)
                    if (edtLandmark.getText().toString().trim().length() > 0)
                        if (edtLocation.getText().toString().trim().length() > 0)
                            if (spinnerState.getSelectedItemPosition() != 0)
                                if (spinnerCity.getSelectedItemPosition() != 0)
                                    if (spinnerArea.getSelectedItemPosition() != 0)
                                        if (spinnerPinCode.getSelectedItemPosition() != 0)
                                            if (spinnerPremisesType.getSelectedItemPosition() != 0) {
                                                relativeViewAddressInfo.setVisibility(View.GONE);
                                                relativeViewUploadDoc.setVisibility(View.VISIBLE);
                                                premise = spinnerPremisesType.getSelectedItem().toString().trim();
                                                newTodayModel.stateId = stateId;
                                                newTodayModel.cityId = cityId;
                                                newTodayModel.state = edtState.getText().toString();
                                                newTodayModel.stateId = stateId;
                                                newTodayModel.area = spinnerArea.getSelectedItem().toString();
                                                getDocumentListID();
                                                getAddDocumentList();
                                            } else
                                                Toast.makeText(mContext, getString(R.string.error_select_premises), Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(mContext, getString(R.string.error_select_pincode), Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(mContext, getString(R.string.select_area), Toast.LENGTH_SHORT).show();

                                else
                                    Toast.makeText(mContext, getString(R.string.error_select_city), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, getString(R.string.error_select_state), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, getString(R.string.error_valid_location), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, getString(R.string.error_valid_landmark), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, getString(R.string.error_valid_road_no_street_name), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext, getString(R.string.error_valid_society_building_name), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, getString(R.string.error_valid_building_no), Toast.LENGTH_SHORT).show();
    }

    private boolean validateIdProof() {
        boolean checkValue = false;
        if (DocumentIdAdapter.checkParamsListId.size() > 0 && DocumentIdAdapter.checkParamsListId.size() <= 2) {
            if (File1 != null || File2 != null) {
                if (File1 != null) {
                    imageCount++;
                }
                if (File2 != null) {
                    imageCount++;
                }
                if (imageCount == DocumentIdAdapter.checkParamsListId.size()) {
                    for (int i = 0; i < DocumentIdAdapter.checkParamsListId.size(); i++) {
                        String sameValue = DocumentIdAdapter.checkParamsListId.get(i);
                        if (!checkValueList.contains(sameValue)) {
                            checkValueList.add(i, sameValue);
                        }
                    }

                    // DocumentIdAdapter.checkParamsListId.clear();
                    imgArrayBitmap.clear();
                    if (validateAddressProof())

                        checkValue = true;
                } else {
                    Toast.makeText(mContext, getString(R.string.error_selected_id), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, getString(R.string.error_id_nsc_image), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_id_nsc), Toast.LENGTH_SHORT).show();
        }

        return checkValue;
    }

    private boolean validateAddressProof() {
        boolean checkValue = false;
        if (DocumentIdAdapter.checkParamsListAddress.size() > 0 && DocumentIdAdapter.checkParamsListAddress.size() <= 2) {
            if (File3 != null || File4 != null) {

                if (File3 != null) {
                    imageCountAdd++;
                }
                if (File4 != null) {
                    imageCountAdd++;
                }
                if (imageCountAdd == DocumentIdAdapter.checkParamsListAddress.size()) {
                    if (premise.equals(getString(R.string.rented))) {
                        if (File5 == null) {
                            Toast.makeText(mContext, R.string.please_capture_image_of_noc_certificate, Toast.LENGTH_SHORT).show();
                        } else {
                            checkValue = true;
                        }
                    } else {
                        checkValue = true;
                    }
                } else
                    Toast.makeText(mContext, getString(R.string.error_selected_add), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(mContext, getString(R.string.error_add_nsc_image), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(mContext, getString(R.string.error_add_nsc), Toast.LENGTH_SHORT).show();

        return checkValue;
    }

    private void validateNocProof() {
        if (File5 == null) {
            Toast.makeText(mContext, R.string.please_capture_image_of_noc_certificate, Toast.LENGTH_SHORT).show();
            return;
        } else
            return;
    }

    private void checkPaymentValidations() {

        if (!selectedScheme.equals(getString(R.string.select_payment_scheme_mandatory))) {
            if (relPaymentMethod.getVisibility() == View.VISIBLE) {
                if (spinnerPaymentMethod.getSelectedItem().equals(getString(R.string.select_payment_type))) {
                    Toast.makeText(mContext, getString(R.string.select_payment_method), Toast.LENGTH_SHORT).show();
                } else if (spinnerPaymentMethod.getSelectedItem().equals(getString(R.string.dd))) {
                    if (fileDD != null) {
                        if (edtDDNo.getText().toString().trim().length() == 6
                                || edtDDNo.getText().toString().length() == 0) {
                            if (edtIFSCDD.getText().toString().trim().matches("^[A-Z]{4}[0][A-Z0-9]{6}$")
                                    || edtIFSCDD.getText().toString().length() == 0) {

                                if (selectedBank.equals(getString(R.string.select_bank))) {
                                    selectedBankName = "";
                                    edtChequeNo.setText("");
                                    edtChequeBranch.setText("");
                                    edtChequeDate.setText("");
                                    selectedBankCheckName = "";
                                    edtIFSCCheque.setText("");
                                    /*if (isRejected) {
                                        new UploadData().execute();
                                    } else {
                                        showDialogForUploadOption(mContext);
                                    }*/
                                    relativeSignature.setVisibility(View.VISIBLE);
                                    relativeViewPayment.setVisibility(View.GONE);
                                } else if (selectedBank.equals(getString(R.string.others))) {
                                    edtOtherBank.setVisibility(View.VISIBLE);
                                    if (!edtOtherBank.getText().toString().trim().isEmpty()) {
                                        selectedBankName = edtOtherBank.getText().toString().trim();
                                        edtChequeNo.setText("");
                                        edtChequeBranch.setText("");
                                        edtChequeDate.setText("");
                                        /*if (isRejected) {
                                            new UploadData().execute();
                                        } else {
                                            showDialogForUploadOption(mContext);
                                        }*/
                                        relativeSignature.setVisibility(View.VISIBLE);
                                        relativeViewPayment.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(mContext, R.string.please_specify_other_bank_name, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    selectedBankName = selectedBank;
                                    edtChequeNo.setText("");
                                    edtChequeBranch.setText("");
                                    edtChequeDate.setText("");
                                    selectedBankCheckName = "";
                                    edtIFSCCheque.setText("");
                                    relativeSignature.setVisibility(View.VISIBLE);
                                    relativeViewPayment.setVisibility(View.GONE);
                                    /*if (isRejected) {
                                        new UploadData().execute();
                                    } else {
                                        showDialogForUploadOption(mContext);
                                    }*/
                                }
                            } else {
                                Toast.makeText(mContext, R.string.enter_valid_ifsc_code, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, R.string.enter_valid_dd_no, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(mContext, R.string.capture_dd_image, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (fileCheque != null) {
                        if (edtChequeNo.getText().toString().trim().length() == 6
                                || edtChequeNo.getText().toString().trim().length() == 0) {
                            if (selectedBankCheque.equals(getString(R.string.select_bank))) {
                                if (edtIFSCCheque.getText().toString().trim().matches("^[A-Z]{4}[0][A-Z0-9]{6}$")
                                        || edtIFSCCheque.getText().toString().trim().length() == 0) {
                                    selectedChequeDate = edtChequeDate.getText().toString().trim();
                                    selectedBankCheckName = "";
                                    edtDDNo.setText("");
                                    selectedBankName = "";
                                    edtDDDate.setText("");
                                    edtDDBranch.setText("");
                                    edtIFSCDD.setText("");
                                    relativeSignature.setVisibility(View.VISIBLE);
                                    relativeViewPayment.setVisibility(View.GONE);
                                    /*if (isRejected) {
                                        new UploadData().execute();
                                    } else {
                                        showDialogForUploadOption(mContext);
                                    }*/
                                } else {
                                    Toast.makeText(mContext, R.string.ifsc_code, Toast.LENGTH_SHORT).show();
                                }
                            } else if (selectedBankCheque.equals(getString(R.string.others))) {
                                edtOtherBankCheque.setVisibility(View.VISIBLE);
                                if (!edtOtherBankCheque.getText().toString().trim().isEmpty()) {
                                    if (edtIFSCCheque.getText().toString().trim().matches("^[A-Z]{4}[0][A-Z0-9]{6}$")
                                            || edtIFSCCheque.getText().toString().trim().length() == 0) {
                                        selectedChequeDate = edtChequeDate.getText().toString().trim();
                                        selectedBankCheckName = edtOtherBankCheque.getText().toString().trim();
                                        edtDDNo.setText("");
                                        selectedBankName = "";
                                        edtDDDate.setText("");
                                        edtDDBranch.setText("");
                                        edtIFSCDD.setText("");
                                        relativeSignature.setVisibility(View.VISIBLE);
                                        relativeViewPayment.setVisibility(View.GONE);
                                        /*if (isRejected) {
                                            new UploadData().execute();
                                        } else {
                                            showDialogForUploadOption(mContext);
                                        }*/

                                    } else {
                                        Toast.makeText(mContext, R.string.ifsc_code, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, getString(R.string.please_specify_other_bank_name), Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                if (edtIFSCCheque.getText().toString().trim().matches("^[A-Z]{4}[0][A-Z0-9]{6}$")
                                        || edtIFSCCheque.getText().toString().trim().length() == 0) {
                                    selectedChequeDate = edtChequeDate.getText().toString().trim();
                                    selectedBankCheckName = selectedBankCheque;
                                    edtDDNo.setText("");
                                    selectedBankName = "";
                                    edtDDDate.setText("");
                                    edtDDBranch.setText("");
                                    edtIFSCDD.setText("");
                                    relativeSignature.setVisibility(View.VISIBLE);
                                    relativeViewPayment.setVisibility(View.GONE);
                                    /*if (isRejected) {
                                        new UploadData().execute();
                                    } else {
                                        showDialogForUploadOption(mContext);
                                    }*/
                                } else {
                                    Toast.makeText(mContext, R.string.enter_valid_ifsc_code, Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Toast.makeText(mContext, R.string.enter_valid_cheque_no, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, R.string.capture_cheque_image, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                selectedBankCheckName = "";
                edtDDNo.setText("");
                selectedBankName = "";
                edtDDDate.setText("");
                edtDDBranch.setText("");
                edtIFSCDD.setText("");
                selectedBankCheckName = "";
                edtDDNo.setText("");
                selectedBankName = "";
                edtDDDate.setText("");
                edtDDBranch.setText("");
                edtIFSCDD.setText("");
                relativeSignature.setVisibility(View.VISIBLE);
                relativeViewPayment.setVisibility(View.GONE);
                /*if (isRejected) {
                    new UploadData().execute();
                } else {
                    showDialogForUploadOption(mContext);
                }*/

            }
        } else
            Toast.makeText(mContext, getString(R.string.select_payment_scheme), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onAsyncSuccess(final JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.GET_CONSUMER_CATEGORY_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.categoary_list != null) {
                        hashMapCategory.clear();
                        hashMapCategory.put("0", getString(R.string.select_consumer_category));
                        for (Consumer con : jsonResponse.categoary_list)
                            hashMapCategory.put(con.getConsumerCategoryId(), con.getConsumerCategory());
                        setCategorySpinner();
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
            case ApiConstants.GET_CONSUMER_SUB_CATEGORY_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.consumer_subcategoary_List != null) {
                        hashMapSubCategory.clear();
                        hashMapSubCategory.put("0", getString(R.string.select_consumer_sub_category));
                        for (Consumer con : jsonResponse.consumer_subcategoary_List)
                            hashMapSubCategory.put(con.getConsumer_subcategory_id(), con.getConsumer_subcategory());
                        setSubCategorySpinner();
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
            case ApiConstants.GET_STATE: {
                if (jsonResponse != null) {
                    if (jsonResponse.state_list != null) {
                        hashMapState.clear();
                        hashMapState.put("0", getString(R.string.select_state));
                        for (Consumer con : jsonResponse.state_list)
                            hashMapState.put(con.getState_id(), con.getState());
                        setStateSpinner();
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
            case ApiConstants.GET_CITY: {
                if (jsonResponse != null) {
                    if (jsonResponse.city_list != null) {
                        hashMapCity.clear();
                        hashMapCity.put("0", getString(R.string.select_city));
                        for (Consumer con : jsonResponse.city_list)
                            hashMapCity.put(con.getCity_id(), con.getCity());
                        setCitySpinner();
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
            case ApiConstants.GET_PIN_CODE: {
                if (jsonResponse != null) {
                    if (jsonResponse.pincode_list != null) {
                        hashMapPinCode.clear();
                        hashMapPinCode.put("0", getString(R.string.select_pin_code));
                        for (Consumer con : jsonResponse.pincode_list)
                            hashMapPinCode.put(con.getPincode_id(), con.getPincode());
                        setPinCodeSpinner();
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
            case ApiConstants.GET_DOCUMENT_LIST: {
                if (jsonResponse != null) {
                    if (jsonResponse.document_list != null) {
                        if (jsonResponse.document_list.size() > 0) {

                            DocumentIdAdapter.checkParamsListId.clear();
                            DocumentIdAdapter.checkParamsListAddress.clear();

                            DocumentIdAdapter Adapter = new DocumentIdAdapter(mContext, jsonResponse.document_list, getString(R.string.edit_id_proof));
                            recyclerIDDoc.setAdapter(Adapter);

                            DocumentIdAdapter Adapter2 = new DocumentIdAdapter(mContext, jsonResponse.document_address_list, getString(R.string.edit_add_proof));
                            recyclerViewAddDoc.setAdapter(Adapter2);
                        }
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
            case ApiConstants.GET_PAYMENT_SCHEMES: {
                if (jsonResponse != null) {
                    if (jsonResponse.scheme_list != null) {
                        hashMapPaymentScheme.clear();
                        hashMapPaymentScheme.put(getString(R.string.select_payment_scheme_mandatory), "0");
                        for (Consumer con : jsonResponse.scheme_list)
                            hashMapPaymentScheme.put(con.getSchemeName(), con.getSchemeAmount());

                        setSchemeSpinner();
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

                        hashMapBankName.clear();
                        hashMapBankName.put("0", getString(R.string.select_bank));
                        hashMapBankName.put("1", getString(R.string.others));

                        bankArray = new ArrayList<>();
                        bankArray.add(0, getString(R.string.select_bank));
                        bankArray.add(1, getString(R.string.others));
                        for (Consumer con : jsonResponse.banklist) {
                            bankArray.add(con.getBankName());

                        }


                        hashMapBankChequeArray.clear();
                        hashMapBankChequeArray.put("0", getString(R.string.select_bank));
                        hashMapBankChequeArray.put("1", getString(R.string.others));

                        bankArrayCheque = new ArrayList<>();
                        bankArrayCheque.add(0, getString(R.string.select_bank));
                        bankArrayCheque.add(1, getString(R.string.others));
                        for (Consumer con : jsonResponse.banklist) {
                            bankArrayCheque.add(con.getBankName());
                        }


                        if (bankArrayCheque.size() > 2) {
                            setBankSpinnerCheque();
                        }
                        if (bankArray.size() > 2) {
                            setBankSpinner();

                        }

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
            case ApiConstants.GET_AREA_SP: {
                if (jsonResponse != null) {
                    if (jsonResponse.area_list != null) {
                        haspMapArea.clear();
                        haspMapArea.put("0", getString(R.string.select_area));
                        for (Consumer con : jsonResponse.area_list)
                            haspMapArea.put(con.getAreaID(), con.getArea());
                        setAreaSpinner();
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
            case ApiConstants.GET_WARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.ward_list != null) {
                        hashMapWard.clear();
                        hashMapWard.put("0", getString(R.string.select_ward));
                        for (Consumer con : jsonResponse.ward_list)
                            hashMapWard.put(con.getWardID(), con.getWard());
                        setWardSpinner();
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
            case ApiConstants.CHECK_AADHAR_NO: {
                if (jsonResponse != null) {
                    if (jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        isAadharPresent = false;
                    } else {
                        isAadharPresent = true;

                    }
                }
            }
            break;
            case ApiConstants.UPLOAD_NSC_FORM: {
                try {
                    dismissLoadingDialog();
                    if (jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        String nscId = jsonResponse.nsc_id;
                        DocumentIdAdapter.checkParamsListId.clear();
                        DocumentIdAdapter.checkParamsListAddress.clear();

                        if (registrationToUpload.get(0).isNscNew.equals("false")) {
                            todayModel.completedOn = CommonUtility.getCompletionDate();
                            DatabaseManager.updateEnquiryCardStatus(CommonUtility.getCompletionDate(), registrationToUpload.get(0).enquiryNo, AppConstants.CARD_STATUS_CLOSED, nscId);
                            DatabaseManager.updateAllEnquiryCardStatus(CommonUtility.getCompletionDate(), registrationToUpload.get(0).enquiryNo, AppConstants.CARD_STATUS_CLOSED, nscId);
                        } else if (registrationToUpload.get(0).isNscNew.equals("true")) {
                            newTodayModel.nscId = nscId;
                            newTodayModel.screen = getString(R.string.enquiry);
                            newTodayModel.completedOn = CommonUtility.getCompletionDate();
                            newTodayModel.consumerName = registrationToUpload.get(0).name;
                            newTodayModel.mobileNumber = registrationToUpload.get(0).mobile;
                            newTodayModel.stateId = registrationToUpload.get(0).state;
                            newTodayModel.cityId = registrationToUpload.get(0).city;
                            newTodayModel.area = registrationToUpload.get(0).areaName;
                            DatabaseManager.updateNewEnquiryCardStatus(CommonUtility.getCompletionDate(), registrationToUpload.get(0).id, AppConstants.CARD_STATUS_CLOSED, nscId);
                            DatabaseManager.updateAllNewEnquiryCardStatus(CommonUtility.getCompletionDate(), registrationToUpload.get(0).id, AppConstants.CARD_STATUS_CLOSED, nscId);
                        } else {
                            DatabaseManager.updateRejectedCardStatus(CommonUtility.getCompletionDate(), registrationToUpload.get(0).registrationNo,
                                    AppConstants.CARD_STATUS_CLOSED);
                            DatabaseManager.deleteRejectedRegistration(mContext, registrationToUpload.get(0).registrationNo,
                                    AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
                        }

                        DatabaseManager.deleteRegistration(mContext, registrationToUpload.get(0).id,
                                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));


                        CustomDialog customDialog = new CustomDialog((Activity) mContext,
                                getString(R.string.nsc_service_added_successfully_with_us) + nscId,
                                getString(R.string.nsc), false);
                        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        customDialog.show();
                        customDialog.setCancelable(false);

                        /*if (File1 != null)
                            File1.delete();
                        if (File2 != null)
                            File2.delete();
                        if (File3 != null)
                            File3.delete();
                        if (File4 != null)
                            File4.delete();
                        if (File5 != null)
                            File5.delete();*/



                        /*if (fileCheque != null)
                            fileCheque.delete();
                        if (fileDD != null)
                            fileDD.delete();*/

                    } else {
                        Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    public void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        super.onActivityResult(pRequestCode, pResultCode, pData);
        if ((pRequestCode == AppConstants.CAMERA_RESULT_CODE && pResultCode == AppConstants.RESULT_OK)
                || (pRequestCode == GALLERY && pResultCode == AppConstants.RESULT_OK)) {
            Bitmap bitmap = null, galleryBitmap = null;
            if (pRequestCode == AppConstants.CAMERA_RESULT_CODE) {
                bitmap = getBitmapScaled(PROFILE_IMAGE_DIRECTORY_NAME, ProfileImageName);
            } else {
                Uri contentURI = pData.getData();
                try {
                    galleryBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    bitmap = Bitmap.createScaledBitmap(
                            galleryBitmap, AppConstants.IMAGE_WIDTH, AppConstants.IMAGE_HEIGHT, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (Count == 1) {
                    Count = 0;
                    if (File1 != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgId1)) {
                                imgRemoveList.remove(imgId1);
                            } else
                                imgRemoveList.add(imgId1);
                        }
                    }
                    imgTakeID1.setImageBitmap(bitmap);
//                    imgTakeID1.setImageBitmap(bitmap);
                    idBitmap1 = bitmap;
                    Uri tempUri = getImageUri(mContext, bitmap);

                    File1 = new File(getRealPathFromURI(tempUri));
//                    Picasso.get().load(File1).into(imgTakeID1);
                } else if (Count == 2) {
                    Count = 0;
                    if (File2 != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgId2)) {
                                imgRemoveList.remove(imgId2);
                            } else
                                imgRemoveList.add(imgId2);
                        }
                    }
                    imgTakeID2.setImageBitmap(bitmap);
//                    imgTakeID2.setImageBitmap(scaled);
                    idBitmap2 = bitmap;

                    Uri tempUri = getImageUri(mContext, bitmap);
                    File2 = new File(getRealPathFromURI(tempUri));
//                    Picasso.get().load(File2).into(imgTakeID2);

                    if (File1 == null) {
                        File1 = File2;
                        File2 = null;
                        idBitmap1 = idBitmap2;
                        idBitmap2 = null;
                    }
                } else if (Count == 3) {
                    Count = 0;

                    if (File3 != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgId3)) {
                                imgRemoveList.remove(imgId3);
                            } else
                                imgRemoveList.add(imgId3);
                        }
                    }
                    imgTakeADD1.setImageBitmap(bitmap);
//                    imgTakeADD1.setImageBitmap(scaled);
                    addBitmap1 = bitmap;

                    Uri tempUri = getImageUri(mContext, bitmap);
                    File3 = new File(getRealPathFromURI(tempUri));
//                    Picasso.get().load(File3).into(imgTakeADD1);

                } else if (Count == 4) {
                    Count = 0;

                    if (File4 != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgId4)) {
                                imgRemoveList.remove(imgId4);
                            } else
                                imgRemoveList.add(imgId4);
                        }
                    }
                    imgTakeADD2.setImageBitmap(bitmap);
//                    imgTakeADD2.setImageBitmap(scaled);
                    addBitmap2 = bitmap;

                    Uri tempUri = getImageUri(mContext, bitmap);
                    File4 = new File(getRealPathFromURI(tempUri));
//                    Picasso.get().load(File4).into(imgTakeADD2);

                    if (File3 == null) {
                        File3 = File4;
                        File4 = null;
                        addBitmap1 = addBitmap2;
                        addBitmap2 = null;
                    }
                } else if (Count == 5) {
                    Count = 0;

                    if (File5 != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgId5)) {
                                imgRemoveList.remove(imgId5);
                            } else {
                                imgRemoveList.add(imgId5);
                            }
                        }
                    }
                    imgTakeNoc.setImageBitmap(bitmap);
//                    imgTakeNoc.setImageBitmap(scaled);
                    nocBitmap = bitmap;

                    Uri tempUri = getImageUri(mContext, bitmap);
                    File5 = new File(getRealPathFromURI(tempUri));
//                    Picasso.get().load(File5).into(imgTakeNoc);

                } else if (Count == 6) {
                    Count = 0;
                    if (fileCheque != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgIdCheque)) {
                                imgRemoveList.remove(imgIdCheque);
                            } else {
                                imgRemoveList.add(imgIdCheque);
                            }
                        }
                    }
                    imgCheque.setImageBitmap(bitmap);
                    bitmapChequeDD = bitmap;
                    Uri tempUri = getImageUri(mContext, bitmap);
                    fileCheque = new File(getRealPathFromURI(tempUri));
                } else if (Count == 7) {
                    Count = 0;
                    if (fileDD != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgIdDD)) {
                                imgRemoveList.remove(imgIdDD);
                            } else {
                                imgRemoveList.add(imgIdDD);
                            }
                        }
                    }
                    imgDD.setImageBitmap(bitmap);
                    bitmapChequeDD = bitmap;
                    Uri tempUri = getImageUri(mContext, bitmap);
                    fileDD = new File(getRealPathFromURI(tempUri));
                } else if (Count == 8) {
                    Count = 0;
                    if (fileConsumerPhoto != null) {
                        if (imgRemoveList.size() > 0) {
                            if (imgRemoveList.contains(imgPhoto)) {
                                imgRemoveList.remove(imgPhoto);
                            } else {
                                imgRemoveList.add(imgPhoto);
                            }
                        }
                    }
                    imgConsumerPhoto.setImageBitmap(bitmap);
                    bitmapConsumerPhoto = bitmap;
                    Uri tempUri = getImageUri(mContext, bitmap);
                    fileConsumerPhoto = new File(getRealPathFromURI(tempUri));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compressedImage;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public File getFilePath(String dirname, String filename) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), dirname);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File createdFile = new File(mediaStorageDir.getPath() + File.separator + filename + ".jpg");
        return createdFile;
    }

    public Uri getOutputMediaFileUri(String dirname, String filename) {
        File file = getFilePath(dirname, filename);
        return FileProvider.getUriForFile(mContext, "com.fieldforce.file", file);
    }

    private TreeMap<String, String> sortByKey(HashMap<String, String> map) {
        // TreeMap to store values of HashMap
        TreeMap<String, String> sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);
        return sorted;
    }

    private void captureLocation() {
        createLocationRequest();
        startLocationUpdates();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CommonUtility.askForPermissions(mContext, App.getInstance().permissions);
            }
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(AppConstants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(AppConstants.FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(AppConstants.DISPLACEMENT);
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CommonUtility.askForPermissions(mContext, App.getInstance().permissions);
            }
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(mContext, getString(R.string.this_device_is_not_supported), Toast.LENGTH_LONG).show();
            }
            return false;
        }

        return true;
    }

    private void showLocationEnableDialog() {
        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        //to show google location turn on alert
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult((Activity) mContext, AppConstants.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        DialogCreator.showMessageDialog(mContext, getString(R.string.location_settings_unavailable_please_change_manually));
                        break;
                }
            }
        });
    }

    // OTP related methods are given belows by Kanchan 03-07-2019

    @Override
    public void onInitiated(String response) {
        Log.d(TAG, "Initialized!" + response);
    }

    @Override
    public void onInitiationFailed(Exception paramException) {
        Log.e(TAG, "Verification initialization failed: " + paramException.getMessage());
//        hideProgressBarAndShowMessage(R.string.failed);
    }

    @Override
    public void onVerified(String response) {
        enableInputField(false);
        Log.d(TAG, "Verified!\n" + response);
        CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.verified),
                getString(R.string.verify_otp), false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
        customDialog.setCancelable(false);
        dismissLoadingDialog();
//        edtEnterOTP.setText("");
        edtMobile.setFocusable(false);
        edtMobile.setClickable(true);
        hideKeypad();
    }

    @Override
    public void onVerificationFailed(Exception paramException) {
        Log.e(TAG, "Verification failed: " + paramException.getMessage());
        JSONObject jresponse = null;
        try {
            jresponse = new JSONObject(paramException.getMessage());
            message = jresponse.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (message.equals("otp_expired")) {
            CustomDialog customDialog = new CustomDialog((Activity) mContext, "OTP Expired",
                    getString(R.string.verify_otp_fail), false);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();
            customDialog.setCancelable(false);
        } else if (message.equals("otp_not_verified")) {
            CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.verification_failed),
                    getString(R.string.verify_otp_fail), false);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();
            customDialog.setCancelable(false);
        }
        dismissLoadingDialog();
        edtEnterOTP.setText("");
//        hideKeypad();
        linearOTP.setVisibility(View.VISIBLE);
    }

    void initiateVerification() {
        initiateVerification(false);
    }

    void initiateVerificationAndSuppressPermissionCheck() {
        initiateVerification(true);
    }

    private void hideKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void initiateVerification(boolean skipPermissionCheck) {
        String phoneNumber = edtMobile.getText().toString().trim();
        createVerification(phoneNumber, skipPermissionCheck);
    }

    void createVerification(String phoneNumber, boolean skipPermissionCheck) {
        if (!skipPermissionCheck && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);
            // hideProgressBar();
        } else {
            mVerification = SendOtpVerification.createSmsVerification
                    (SendOtpVerification
                            .config(phoneNumber)
                            .context(mContext)
                            .autoVerification(false)
                            .httpsConnection(false)//connection to be use in network calls
                            .expiry("5")//value in minutes
                            .senderId("SMSBGL") //where XXXX is any string
//                            .otp("1234")// Default Otp code if want to add yours
                            .otplength("4") //length of your otp
                            .message("##OTP## is Your verification code. Code valid for 5 minutes only, one time use. Please, Do not share your OTP with anyone.")
                            .build(), this);
            mVerification.initiate();
        }
    }

    public void onSubmitClicked(View view) {
        if (edtEnterOTP.getText().toString().equals(mStrOTP)) {
            Toast.makeText(RegistrationFormActivity.this, "Verified Mobile Number",
                    Toast.LENGTH_LONG).show();
            edtEnterOTP.setFocusable(false);
            edtEnterOTP.setFocusableInTouchMode(false);
            txtResendOTP.setClickable(false);
            txtSMSVerification.setClickable(false);

                /*enableInputField(false);
                CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.already_verified),
                        getString(R.string.otp_already_verifed), false);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
                customDialog.setCancelable(false);*/
//                Toast.makeText(getApplicationContext(), getString(R.string.otp_already_verifed), Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        /*String code = ((EditText) findViewById(R.id.edt_enter_otp)).getText().toString();
        if (code.equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_enter_OTP), Toast.LENGTH_LONG).show();
        } else if (!code.isEmpty()) {
            hideKeypad();
            if (mVerification != null) {
                mVerification.verify(code);

                showLoadingDialog(getString(R.string.verification_in_progress));
                enableInputField(false);
            }
        }*/
    }

    private void tryAndPrefillPhoneNumber() {
        if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            edtEnterOTP.setText(manager.getLine1Number());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tryAndPrefillPhoneNumber();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your phone number to automatically "
                        + "pre-fill it", Toast.LENGTH_LONG).show();
            }
        }
        initiateVerificationAndSuppressPermissionCheck();
    }

    public void onButtonClicked(View view) {
        getE164Number();
    }

    private String getE164Number() {
        return edtEnterOTP.getText().toString().replaceAll("\\D", "").trim();
    }

    void enableInputField(boolean enable) {
        View container = findViewById(R.id.linear_otp);
        if (edtMobile.getText().toString().trim().length() == 0 || (edtMobile.getText().toString().trim().length() != 10)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_valid_mobile_no), Toast.LENGTH_LONG).show();
        } else if (enable) {
            container.setVisibility(View.VISIBLE);
            initiateVerification();
        } else {
            container.setVisibility(View.GONE);
        }
    }

    /* Function to save values in model and in database by Saloni*/
    public void doSubmitOps() {

        registrationModel.enquiryId = !enquiryId.isEmpty() ? enquiryId : "";
        registrationModel.consumerCategory = selectedCategory;
        registrationModel.consumerSubCategory = selectSubCategory;
        registrationModel.state = stateId;
        registrationModel.name = edtConsumerName.getText().toString();
        registrationModel.addhaar = edtAadharNumber.getText().toString();
        registrationModel.emailId = edtEmailId.getText().toString();
        registrationModel.flatNo = edtFlatNumber.getText().toString();


        if (edtFloor.getText().toString().length() > 8) {
            registrationModel.floorNo = edtFloor.getText().toString();
        }
        if (edtPlotNo.getText().toString().length() > 10) {
            registrationModel.plotNo = edtPlotNo.getText().toString();
        }

        if (edtWing.getText().toString().length() > 6) {
            registrationModel.wing = edtWing.getText().toString();
        }
//        registrationModel.floorNo = edtFloor.getText().toString();
//        registrationModel.plotNo = edtPlotNo.getText().toString();
//        registrationModel.wing = edtWing.getText().toString();
        registrationModel.roadNo = edtRoadNo.getText().toString();
        registrationModel.landmark = edtLandmark.getText().toString();


        registrationModel.district = edtDistrict.getText().toString();
        registrationModel.buildingName = edtSocietyBuildingName.getText().toString();
        registrationModel.societyName = edtSocietyName.getText().toString();
        registrationModel.location = edtLocation.getText().toString();
        registrationModel.area = selectedArea;
        registrationModel.areaName = spinnerArea.getSelectedItem().toString();
        registrationModel.city = cityId;
        //registrationModel.ward = selectedWard;
        //registrationModel.wardName = spinnerWard.getSelectedItem().toString();
        //registrationModel.location = selectedLocation;
        //registrationModel.landmark = selectedLandmark;


        //registrationModel.district = districtId;
        registrationModel.pincode = selectedPinCode;
        registrationModel.mobile = edtMobile.getText().toString();
        registrationModel.premise = spinnerPremisesType.getSelectedItem().toString();
        registrationModel.chequeNo = edtChequeNo.getText().toString();
        registrationModel.chequeBranch = edtChequeBranch.getText().toString();
        //registrationModel.chequeDate = selectedChequeDate;
        registrationModel.chequeDate = edtChequeDate.getText().toString();
        registrationModel.chequeBank = selectedBankCheckName;
        registrationModel.chequeMicr = edtIFSCCheque.getText().toString().trim();
        registrationModel.ddNo = edtDDNo.getText().toString();
        registrationModel.bankName = selectedBankName;
        registrationModel.ddDate = edtDDDate.getText().toString().trim();
        registrationModel.ddMicr = edtIFSCDD.getText().toString().trim();
        registrationModel.ddBranch = edtDDBranch.getText().toString().trim();
        registrationModel.latitude = latitude;
        registrationModel.longitude = longitude;
        registrationModel.connectivity = isConnected;
        if (isRejected) {
            registrationModel.isNscNew = "";

        } else {
            registrationModel.isNscNew = String.valueOf(isNscNew);

        }
        registrationModel.imageCount = String.valueOf(imageCount);
        registrationModel.imageCountAdd = String.valueOf(imageCountAdd);
        registrationModel.vendorId = vendorId;
        registrationModel.fieldForceId = fieldForceId;
        if (comingFrom.equals(getString(R.string.edit_nsc))) {
            registrationModel.enquiryNo = todayModel.getEnquiryNumber();
            registrationModel.consumerNo = todayModel.getConsumerNo();
        } else {
            registrationModel.enquiryNo = "";
            registrationModel.consumerNo = "";
        }
        registrationModel.saveType = "FF Mobile";
        registrationModel.schemeName = selectedScheme;
        registrationModel.schemeAmount = String.valueOf(schemeAmount);

        if (schemeAmount != 0) {
            registrationModel.paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
        } else {
            registrationModel.paymentMethod = "";
        }


        ArrayList<String> checklist11 = new ArrayList<>();
        for (int i = 0; i < checkValueList.size(); i++) {
            checklist11.add(i, checkValueList.get(i));
        }
        ArrayList<String> documentOne = new ArrayList<>();


        for (int i = 0; i < checklist11.size(); i++) {
            documentOne.add(checklist11.get(i) + "");
        }
        registrationModel.documents = documentOne.toString();


        ArrayList<String> documentTwo = new ArrayList<>();

        ArrayList<String> checklist12 = new ArrayList<>();
        for (int i = 0; i < DocumentIdAdapter.checkParamsListAddress.size(); i++) {
            checklist12.add(i, DocumentIdAdapter.checkParamsListAddress.get(i));
        }

        for (int i = 0; i < checklist12.size(); i++) {
            documentTwo.add(checklist12.get(i) + "");
        }

        registrationModel.documentsAdd = documentTwo.toString();

        ImageModel imageFile0 = new ImageModel();
        ImageModel imageFile1 = new ImageModel();
        ImageModel imageAddFile0 = new ImageModel();
        ImageModel imageAddFile1 = new ImageModel();
        ImageModel imageNocFile0 = new ImageModel();
        ImageModel imageSignFile0 = new ImageModel();
        ImageModel imageChequeDDFile0 = new ImageModel();
        ImageModel imageConsumerPhoto = new ImageModel();


        imageFile0.image = CommonUtility.getBitmapEncodedString(idBitmap1);
        imageFile1.image = CommonUtility.getBitmapEncodedString(idBitmap2);
        imageAddFile0.image = CommonUtility.getBitmapEncodedString(addBitmap1);
        imageAddFile1.image = CommonUtility.getBitmapEncodedString(addBitmap2);
        imageNocFile0.image = CommonUtility.getBitmapEncodedString(nocBitmap);
        imageSignFile0.image = CommonUtility.getBitmapEncodedString(bitmapSign);
        imageChequeDDFile0.image = CommonUtility.getBitmapEncodedString(bitmapChequeDD);
        imageConsumerPhoto.image = CommonUtility.getBitmapEncodedString(bitmapConsumerPhoto);

        registrationModel.File0 = imageFile0;
        registrationModel.File1 = imageFile1;
        registrationModel.FileAddProof0 = imageAddFile0;
        registrationModel.FileAddProof1 = imageAddFile1;
        registrationModel.FileNocProof = imageNocFile0;
        registrationModel.FileSign = imageSignFile0;
        registrationModel.FileChequeDD = imageChequeDDFile0;
        registrationModel.FileConsumerPhoto = imageConsumerPhoto;


        registrationModel.isRejected = String.valueOf(isRejected);


        long id = DatabaseManager.saveRegistrationNSC(mContext, registrationModel, RegistrationFormActivity.formActivity, AppConstants.CARD_STATUS_COMPLETED);


        if (comingFrom.equals(getString(R.string.edit_nsc))) {
            todayModel.completedOn = CommonUtility.getCompletionDate();

            DatabaseManager.updateEnquiryCardStatusUpload(todayModel.completedOn, todayModel.getEnquiryNumber(), AppConstants.CARD_STATUS_COMPLETED, nscId, String.valueOf(id), registrationModel.areaName);
            DatabaseManager.updateAllEnquiryCardStatusUpload(todayModel.completedOn, todayModel.getEnquiryNumber(), AppConstants.CARD_STATUS_COMPLETED, nscId, String.valueOf(id));
        } else if (comingFrom.equals(getString(R.string.new_nsc))) {
            newTodayModel.nscId = nscId;
            newTodayModel.screen = getString(R.string.enquiry);
            newTodayModel.completedOn = CommonUtility.getCompletionDate();
            newTodayModel.registrationId = String.valueOf(id);
            DatabaseManager.saveNewEnquiryJobCards(mContext, newTodayModel, nscId, getString(R.string.enquiry), AppConstants.CARD_STATUS_COMPLETED);
            DatabaseManager.saveAllNewEnquiryJobCards(mContext, newTodayModel, nscId, getString(R.string.enquiry), AppConstants.CARD_STATUS_COMPLETED);
        }

        DocumentIdAdapter.checkParamsListId.clear();
        DocumentIdAdapter.checkParamsListAddress.clear();

        if (istoUpload) {
            uploadRegistration();
        } else {
            finish();
        }

    }

    public class UploadData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {
                MultipartUtility multipartUtility;
                if (isRejected) {
                    multipartUtility = new MultipartUtility(ApiConstants.UPLOAD_REJECTED_FORM, "UTF-8");
                } else {
                    multipartUtility = new MultipartUtility(ApiConstants.UPLOAD_NSC_FORM, "UTF-8");
                }
                if (!enquiryId.isEmpty()) {
                    multipartUtility.addFormField("enquiry_id", enquiryId);
                }
                multipartUtility.addFormField("consumer_category", selectedCategory);
                multipartUtility.addFormField("consumer_subcategory", selectSubCategory);
                multipartUtility.addFormField("state", stateId);
                multipartUtility.addFormField("name", edtConsumerName.getText().toString());
                multipartUtility.addFormField("addhaar", edtAadharNumber.getText().toString());
                multipartUtility.addFormField("email_id", edtEmailId.getText().toString());
//                multipartUtility.addFormField("occupation", spinnerOccupation.getSelectedItem().toString());
//                multipartUtility.addFormField("other", edtOther.getText().toString());
                multipartUtility.addFormField("flat_no", edtFlatNumber.getText().toString());
                multipartUtility.addFormField("building_name", edtSocietyBuildingName.getText().toString());
                multipartUtility.addFormField("location", edtLocation.getText().toString());
                multipartUtility.addFormField("area", selectedArea);
                multipartUtility.addFormField("ward", selectedWard);
                multipartUtility.addFormField("location", selectedLocation);
                multipartUtility.addFormField("landmark", selectedLandmark);
                multipartUtility.addFormField("city", cityId);
                multipartUtility.addFormField("pincode", selectedPinCode);
                multipartUtility.addFormField("mobile", edtMobile.getText().toString());
                multipartUtility.addFormField("premise", spinnerPremisesType.getSelectedItem().toString());
                multipartUtility.addFormField("req_load", "");
                multipartUtility.addFormField("cont_load", "");

                if (comingFrom.equals(getString(R.string.edit_nsc))) {
                    multipartUtility.addFormField("enquiry_no", todayModel.getEnquiryNumber());
                    multipartUtility.addFormField("consumer_no", todayModel.getConsumerNo());
                }

                multipartUtility.addFormField("save_type", "FF Mobile");
                multipartUtility.addFormField("scheme_name", selectedScheme);
                multipartUtility.addFormField("scheme_amount", String.valueOf(schemeAmount));

                if (schemeAmount != 0) {
                    multipartUtility.addFormField("payment_method", spinnerPaymentMethod.getSelectedItem().toString());
                } else {
                    multipartUtility.addFormField("payment_method", "");
                }

                multipartUtility.addFormField("cheque_no", edtChequeNo.getText().toString());
                multipartUtility.addFormField("cheque_branch", edtChequeBranch.getText().toString());
                multipartUtility.addFormField("cheque_date", edtChequeDate.getText().toString());
                multipartUtility.addFormField("cheque_bank", selectedBankCheckName);
                multipartUtility.addFormField("cheque_micr", edtIFSCCheque.getText().toString().trim());
                multipartUtility.addFormField("dd_no", edtDDNo.getText().toString());
                multipartUtility.addFormField("bank_name", selectedBankName);
                multipartUtility.addFormField("dd_date", edtDDDate.getText().toString().trim());
                multipartUtility.addFormField("dd_micr", edtIFSCDD.getText().toString().trim());
                multipartUtility.addFormField("dd_branch", edtDDBranch.getText().toString().trim());
                multipartUtility.addFormField("latitude", latitude);
                multipartUtility.addFormField("longitude", longitude);
                multipartUtility.addFormField("connectivity", isConnected);

                multipartUtility.addFormField("is_nsc_new", String.valueOf(isNscNew));

                ArrayList<String> checklist11 = new ArrayList<>();
                for (int i = 0; i < checkValueList.size(); i++) {
                    checklist11.add(i, checkValueList.get(i));
                }


                for (int i = 0; i < checklist11.size(); i++) {
                    multipartUtility.addFormField("documents", checklist11.get(i));

                }

                ArrayList<String> checklist12 = new ArrayList<>();
                for (int i = 0; i < DocumentIdAdapter.checkParamsListAddress.size(); i++) {
                    checklist12.add(i, DocumentIdAdapter.checkParamsListAddress.get(i));
                }


                for (int i = 0; i < checklist12.size(); i++) {
                    multipartUtility.addFormField("documents_add", checklist12.get(i));

                }
                if (File1 != null) {
                    multipartUtility.addFilePart("File0", File1);
                }
                if (File2 != null) {
                    multipartUtility.addFilePart("File1", File2);
                }
                if (File3 != null) {
                    multipartUtility.addFilePart("File_add_proof0", File3);
                }
                if (File4 != null) {
                    multipartUtility.addFilePart("File_add_proof1", File4);
                }
                if (File5 != null) {
                    multipartUtility.addFilePart("File_noc_proof", File5);
                }
                if (fileConsumerPhoto != null) {
                    multipartUtility.addFilePart("File_consumer_photo", fileConsumerPhoto);
                }

                if (spinnerPaymentMethod.getSelectedItem().toString().trim().equals(getString(R.string.cheque))) {
                    multipartUtility.addFilePart("File_cheque_dd", fileCheque);
                }
                if (spinnerPaymentMethod.getSelectedItem().toString().trim().equals(getString(R.string.dd))) {
                    multipartUtility.addFilePart("File_cheque_dd", fileDD);
                }


                multipartUtility.addFormField("image_count", String.valueOf(imageCount));
                multipartUtility.addFormField("image_count_add", String.valueOf(imageCountAdd));


                multipartUtility.addFormField("vendor_id", vendorId);
                multipartUtility.addFormField("field_force_id", fieldForceId);

                if (isRejected) {
                    multipartUtility.addFormField("is_document_rejected", "YES");
                    multipartUtility.addFormField("nsc_id", newRegistrationModel.nscId);
                } else {
                    multipartUtility.addFormField("is_document_rejected", "NO");
                }


                multipartUtility.addFormField("floor_no", edtFloor.getText().toString());
                multipartUtility.addFormField("plot_no", edtPlotNo.getText().toString());
                multipartUtility.addFormField("wing", edtWing.getText().toString());
                multipartUtility.addFormField("road_no", edtRoadNo.getText().toString());
                //multipartUtility.addFormField("landmark", edtLandmark.getText().toString());
                multipartUtility.addFormField("district", edtDistrict.getText().toString());

                multipartUtility.addFormField("society_name", edtSocietyName.getText().toString());

                String response = multipartUtility.finish(); // response from server.

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
                    JSONObject response = new JSONObject(result);
                    if (response.getString(getString(R.string.result)).equalsIgnoreCase(getString(R.string.success))) {

                        nscId = response.getString("nsc_id");
                        dismissLoadingDialog();
                        DocumentIdAdapter.checkParamsListId.clear();
                        DocumentIdAdapter.checkParamsListAddress.clear();
                        if (comingFrom.equals(getString(R.string.edit_nsc))) {
                            todayModel.completedOn = CommonUtility.getCompletionDate();
                            DatabaseManager.updateEnquiryCardStatus(todayModel.completedOn, todayModel.getEnquiryNumber(), AppConstants.CARD_STATUS_CLOSED, nscId);
                            DatabaseManager.updateAllEnquiryCardStatus(todayModel.completedOn, todayModel.getEnquiryNumber(), AppConstants.CARD_STATUS_CLOSED, nscId);
                        } else if (comingFrom.equals(getString(R.string.new_nsc))) {
                            newTodayModel.nscId = nscId;
                            newTodayModel.screen = getString(R.string.enquiry);
                            newTodayModel.completedOn = CommonUtility.getCompletionDate();
                            DatabaseManager.saveNewEnquiryJobCards(mContext, newTodayModel, nscId, getString(R.string.enquiry), AppConstants.CARD_STATUS_CLOSED);
                            DatabaseManager.saveAllNewEnquiryJobCards(mContext, newTodayModel, nscId, getString(R.string.enquiry), AppConstants.CARD_STATUS_CLOSED);
                        } else {
                            newRegistrationModel.completedOn = CommonUtility.getCompletionDate();
                            DatabaseManager.updateRejectedCardStatus(newRegistrationModel.completedOn, newRegistrationModel.registrationNo,
                                    AppConstants.CARD_STATUS_CLOSED);
                            DatabaseManager.deleteRejectedRegistration(mContext, newRegistrationModel.registrationNo,
                                    AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
                        }
                        CustomDialog customDialog = new CustomDialog((Activity) mContext,
                                getString(R.string.nsc_service_added_successfully_with_us) + response.getString("nsc_id"),
                                getString(R.string.nsc), false);
                        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        customDialog.show();
                        customDialog.setCancelable(false);


                        if (File1 != null)
                            File1.delete();
                        if (File2 != null)
                            File2.delete();
                        if (File3 != null)
                            File3.delete();
                        if (File4 != null)
                            File4.delete();
                        if (File5 != null)
                            File5.delete();
                        if (fileCheque != null)
                            fileCheque.delete();
                        if (fileDD != null)
                            fileDD.delete();
                        if (fileConsumerPhoto != null)
                            fileConsumerPhoto.delete();

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
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    private void checkAadharNumber() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("aadhar_no", edtAadharNumber.getText().toString().trim());
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.CHECK_AADHAR_NO, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.CHECK_AADHAR_NO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
    }

    public void showDialogForAadhar() {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.dialog_cancel, null);
        Button btnYes, btnNo;

        btnYes = promptView.findViewById(R.id.btn_yes);
        btnNo = promptView.findViewById(R.id.btn_no);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeViewGeneralInfo.setVisibility(View.GONE);
                relativeViewAddressInfo.setVisibility(View.VISIBLE);
                getArea();
                alert.dismiss();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArea();
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.setCancelable(false);
        alert.show();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle(getString(R.string.select_action));
        String[] pictureDialogItems = {
                getString(R.string.select_photo_from_gallery),
                getString(R.string.capture_photo_from_camera)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                callCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri file = getOutputMediaFileUri(PROFILE_IMAGE_DIRECTORY_NAME, ProfileImageName);
        List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(galleryIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;
            mContext.grantUriPermission(packageName, file, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(galleryIntent, GALLERY);
    }

    /*Dialog to show option to upload registration now and in background by Saloni*/
    public void showDialogForUploadOption(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_upload, null);


        Button btnNow, btnBackground;
        btnNow = promptView.findViewById(R.id.btn_upload_manually);
        btnBackground = promptView.findViewById(R.id.btn_upload_background);
        btnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable(mContext)) {
                    istoUpload = true;
                    doSubmitOps();
                    alert1.dismiss();
                } else {
                    istoUpload = false;
                    Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
                    alert1.dismiss();
                }

            }
        });
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                istoUpload = false;
                doSubmitOps();
                alert1.dismiss();

            }
        });
        alert1.setView(promptView);
        alert1.setCancelable(false);
        alert1.show();
    }

    public void setValues() {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = originalFormat.parse(newRegistrationModel.dateOfPayment);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        edtConsumerName.setText(newRegistrationModel.name);
        edtMobile.setText(newRegistrationModel.mobile);
        edtAadharNumber.setText(newRegistrationModel.addhaar);
        edtEmailId.setText(newRegistrationModel.emailId);
        edtFlatNumber.setText(newRegistrationModel.flatNo);
        edtFloor.setText(newRegistrationModel.floorNo);
        edtPlotNo.setText(newRegistrationModel.plotNo);
        edtWing.setText(newRegistrationModel.wing);
        edtSocietyName.setText(newRegistrationModel.societyName);
        edtRoadNo.setText(newRegistrationModel.roadNo);
        edtLandmark.setText(newRegistrationModel.landmark);
        edtDistrict.setText(newRegistrationModel.district);
        edtSocietyBuildingName.setText(newRegistrationModel.buildingName);
        edtLocation.setText(newRegistrationModel.location);

        if (newRegistrationModel.connectivity.equals("YES")) {
            radioYes.setChecked(true);
        } else {
            radioNo.setChecked(true);
        }

        if (newRegistrationModel.paymentMethod.equals(getString(R.string.cheque))) {
            edtChequeNo.setText(newRegistrationModel.chequeDDNumber);
            edtChequeBranch.setText(newRegistrationModel.branchName);
            edtIFSCCheque.setText(newRegistrationModel.micrCode);
            edtChequeDate.setText(formattedDate);
        } else {
            edtDDNo.setText(newRegistrationModel.chequeDDNumber);
            edtDDBranch.setText(newRegistrationModel.branchName);
            edtIFSCDD.setText(newRegistrationModel.micrCode);
            edtDDDate.setText(formattedDate);
        }


    }

    //OTP Changes by Jayshree on 13-01-2020
    private void getOTP() {
        Random otp = new Random();

        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 3; count++) {
            builder.append(otp.nextInt(3));
        }
        mStrOTP = builder.toString();

        multimsg("OTP for BGL New Registration is " + mStrOTP, "" + edtMobile.getText().toString());


    }

    private void multimsg(String message, String phoneNumber) {
        //mProgressBar.setVisibility(View.VISIBLE);
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        Log.e("TAG", "SMS:" + message + "NUMBER:" + phoneNumber);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(RegistrationFormActivity.this, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        Toast.makeText(RegistrationFormActivity.this, "Generic failure",
                                Toast.LENGTH_SHORT).show();

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(RegistrationFormActivity.this, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(RegistrationFormActivity.this, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(RegistrationFormActivity.this, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(RegistrationFormActivity.this, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        //txtSMSVerification.setEnabled(false);

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(RegistrationFormActivity.this, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        ///sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        SmsManager smsMgr = SmsManager.getDefault();
        SmsManager sm = SmsManager.getDefault();
        ArrayList<String> parts = sm.divideMessage(message);
        int numParts = parts.size();
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        for (int i = 0; i < numParts; i++) {
            sentIntents.add(PendingIntent.getBroadcast(RegistrationFormActivity.this, 0,
                    new Intent(SENT), 0));
            deliveryIntents.add(PendingIntent.getBroadcast(RegistrationFormActivity.this, 0,
                    new Intent(DELIVERED), 0));
        }
        if (ContextCompat.checkSelfPermission(RegistrationFormActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            int permissionCheck = ContextCompat.checkSelfPermission(RegistrationFormActivity.this, Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) RegistrationFormActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            } else {
                //TODO

            }
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) RegistrationFormActivity.this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions((Activity) RegistrationFormActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
        } else {
            //already has permission granted
            if (phoneNumber.length() != 0) {

                sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
            }
            //smsManager.sendTextMessage(phonenumber, null,smsbody, null, null);
            Toast.makeText(RegistrationFormActivity.this, "SMS SENT.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void defaultConsumerPhoto() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_default_user_icon);
        imgConsumerPhoto.setImageBitmap(icon);
        bitmapConsumerPhoto = icon;
        Uri tempUri = getImageUri(mContext, icon);
        fileConsumerPhoto = new File(getRealPathFromURI(tempUri));
    }

    // OTP Change by Jayshree Completed

    /*Function to upload registration from within the activity by Saloni*/

    public void uploadRegistration() {

        if (isNetworkAvailable(mContext)) {
            registrationToUpload = DatabaseManager.getRegistration(mContext,
                    AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""), ApiConstants.UPLOAD_COUNT,
                    AppConstants.CARD_STATUS_COMPLETED);
            if (registrationToUpload != null && registrationToUpload.size() > 0) {
                for (int i = 0; i < registrationToUpload.size(); i++) {
                    List<String> checklist11 = new ArrayList<>();
                    String data = registrationToUpload.get(0).documents;
                    String[] items = data.split("\\|");
                    Collections.addAll(checklist11, items);

                    List<String> documentOne = new ArrayList<>();
                    for (int j = 0; j < checklist11.size(); j++) {
                        documentOne.add(j, checklist11.get(j));
                    }
                    registrationToUpload.get(0).documents = documentOne.get(0);

                    ArrayList<String> checklist12 = new ArrayList<>();
                    String data1 = registrationToUpload.get(0).documentsAdd;
                    String[] items1 = data1.split("\\|");
                    Collections.addAll(checklist12, items1);

                    ArrayList<String> documentTwo = new ArrayList<>();

                    for (int k = 0; k < DocumentIdAdapter.checkParamsListAddress.size(); k++) {
                        checklist12.add(k, DocumentIdAdapter.checkParamsListAddress.get(k));
                    }

                    for (int l = 0; l < checklist12.size(); l++) {
                        documentTwo.add(l, checklist12.get(l));
                    }
                    registrationToUpload.get(0).documentsAdd = documentTwo.get(0);

                    JSONObject jObject = getReadingJson(registrationToUpload);
                    uploadMeterReading(jObject);
                }
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_SHORT).show();
        }

    }

    /*FUnction to create json object to upload by Saloni*/

    public JSONObject getReadingJson(ArrayList<RegistrationModel> readings) {
        JSONObject jsonObject = null;
        try {

            Gson gson = new Gson();
            String jsonString = gson.toJson(readings);
            JSONArray jsonArray = new JSONArray(jsonString);
            jsonObject = new JSONObject();
            jsonObject.put("values", jsonArray);


            /*

            Gson gson = new Gson();
            String jsonString = gson.toJson(readings);
            JSONObject tempJsonObject = new JSONObject(jsonString);*/


//            jsonObject = new JSONObject(S);
//            jsonObject.put("readings", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*Function to call API to upload registration by Saloni*/
    private void uploadMeterReading(JSONObject object) {
        showLoadingDialog();
        JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, object, ApiConstants.UPLOAD_NSC_FORM, this, null);
        App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_NSC_FORM);
    }


}