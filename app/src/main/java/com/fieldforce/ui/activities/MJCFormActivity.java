package com.fieldforce.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fieldforce.R;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.models.TodayModel;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;
import com.fieldforce.utility.DecimalInputFilter;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static com.fieldforce.webservices.ApiConstants.SIGNATURE_IMAGE_UPLOAD;
import static com.fieldforce.webservices.ApiConstants.UPLOAD_MJC_CHECK_LIST;

public class MJCFormActivity extends ParentActivity implements View.OnClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ApiServiceCaller {

    private Context mContext;
    private Button btnNext1, btnNext2, btnNext3, btnSubmit, btnComplete, btnUpload, btnNextContractor;
    private Button btnBackContractor, btnClearContractor, btnBackEngg, btnClearEngg, btnClearConsumer;
    private EditText edtTitle, edtMjcNo, edtName, edtFlatNo, edtBuildingName, edtLocation, edtCity, edtPinCode,
            edtMobileNo, edtEmailId, edtInstallationDate, edtMeterNo, edtTestDate, edtTestPressure, edtTestDuration,
            edtInitialReading, edtConversionDate, edtContractorName;
    private RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5, radioGroup6, radioGroup7,
            radioGroup8;
    private RadioButton radioYes1, radioYes2, radioYes3, radioYes4, radioYes5, radioYes6, radioYes7, radioYes8,
            radioNo1, radioNo2, radioNo3, radioNo4, radioNo5, radioNo6, radioNo7, radioNo8;
    private RelativeLayout relativeLayoutOne, relativeLayoutTwo, relativeLayoutThree, relativeLayoutFour;

    private LinearLayout viewEnggSignature, viewContractorSignature, viewConsumerSignature, linearContractorSign,
            linearConsumerSign, linearEnggSign;

    private SignatureView signatureViewEngg, signatureViewContractor, signatureViewConsumer;
    private ImageView customerSign, imgTpiSign, imgBack;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Typeface mRegularBold, mRegular;

    private File fileContractorSign = null, fileEnggSign = null, fileConsumerSign = null;

    private TodayModel todayModel;

    private String jobId, conversionDate, newConversionDate, initialReading;

    private String one = "", two = "", three = "", four = "", five = "",
            six = "", seven = "", eight = "";

    //    private ArrayList<String> radioCheck = new ArrayList<>();
    private HashMap<String, String> radioCheck = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mjcform);

        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            todayModel = (TodayModel) intent.getSerializableExtra(AppConstants.MODEL);
            jobId = (String) intent.getSerializableExtra("image_sign_id");
            conversionDate = (String) intent.getSerializableExtra("conversion_date");
            initialReading = (String) intent.getSerializableExtra("initial_reading");
        }

        mRegularBold = App.getMontserratMediumFont();
        mRegular = App.getMontserratRegularFont();

        relativeLayoutOne = findViewById(R.id.relative_layout_step_one);
        relativeLayoutTwo = findViewById(R.id.relative_layout_step_two);
        relativeLayoutThree = findViewById(R.id.relative_layout_step_three);
        relativeLayoutFour = findViewById(R.id.relative_layout_step_four);

        radioGroup1 = findViewById(R.id.radio_group_one);
        radioGroup2 = findViewById(R.id.radio_group_two);
        radioGroup3 = findViewById(R.id.radio_group_three);
        radioGroup4 = findViewById(R.id.radio_group_four);
        radioGroup5 = findViewById(R.id.radio_group_five);
        radioGroup6 = findViewById(R.id.radio_group_six);
        radioGroup7 = findViewById(R.id.radio_group_seven);
        radioGroup8 = findViewById(R.id.radio_group_eight);

        btnNext1 = findViewById(R.id.btn_next1);
        btnNext2 = findViewById(R.id.btn_next2);
        btnNext3 = findViewById(R.id.btn_next3);

        btnComplete = findViewById(R.id.btn_complete);
        btnNextContractor = findViewById(R.id.btn_next_contractor);
        btnUpload = findViewById(R.id.btn_upload);
        btnClearContractor = findViewById(R.id.btn_clear_contractor);
        btnClearEngg = findViewById(R.id.btn_clear_engg);
        btnClearConsumer = findViewById(R.id.btn_clear_consumer);
        btnBackContractor = findViewById(R.id.btn_back_contractor);
        btnBackEngg = findViewById(R.id.btn_back_engg);


//        btnSubmit = findViewById(R.id.btn_submit);

//        btnClearContractor = findViewById(R.id.btn_clear_contractor);
//        btnClearEngg = findViewById(R.id.btn_clear_engg);

        btnNext1.setOnClickListener(this);
        btnNext2.setOnClickListener(this);
        btnNext3.setOnClickListener(this);
//        btnSubmit.setOnClickListener(this);

        btnComplete.setOnClickListener(this);
        btnNextContractor.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnClearContractor.setOnClickListener(this);
        btnClearEngg.setOnClickListener(this);
        btnClearConsumer.setOnClickListener(this);
        btnBackEngg.setOnClickListener(this);
        btnBackContractor.setOnClickListener(this);


//        btnClearEngg.setOnClickListener(this);
//        btnClearContractor.setOnClickListener(this);


        edtTitle = findViewById(R.id.edt_title);
        edtMjcNo = findViewById(R.id.edt_mjc_no);
        edtName = findViewById(R.id.edt_name);
        edtFlatNo = findViewById(R.id.edt_flat_no);
        edtBuildingName = findViewById(R.id.edt_building_name);
        edtLocation = findViewById(R.id.edt_location);
        edtCity = findViewById(R.id.edt_city);
        edtPinCode = findViewById(R.id.edt_pin_code);
        edtMobileNo = findViewById(R.id.edt_mobile_no);
        edtEmailId = findViewById(R.id.edt_email_id);
        edtInstallationDate = findViewById(R.id.edt_meter_installation_date);
        edtMeterNo = findViewById(R.id.edt_meter_no);
        edtTestDate = findViewById(R.id.edt_test_date);
        edtTestPressure = findViewById(R.id.edt_test_pressure);
        edtTestDuration = findViewById(R.id.edt_test_duration);
        edtInitialReading = findViewById(R.id.edt_initial_reading);
        edtConversionDate = findViewById(R.id.edt_conversion_date);

        edtTitle.setTypeface(mRegular);
        edtMjcNo.setTypeface(mRegular);
        edtName.setTypeface(mRegular);
        edtFlatNo.setTypeface(mRegular);
        edtBuildingName.setTypeface(mRegular);
        edtLocation.setTypeface(mRegular);
        edtCity.setTypeface(mRegular);
        edtPinCode.setTypeface(mRegular);
        edtMobileNo.setTypeface(mRegular);
        edtEmailId.setTypeface(mRegular);
        edtInstallationDate.setTypeface(mRegular);
        edtMeterNo.setTypeface(mRegular);
        edtTestDate.setTypeface(mRegular);
        edtTestPressure.setTypeface(mRegular);
        edtTestDuration.setTypeface(mRegular);
        edtInitialReading.setTypeface(mRegular);
        edtConversionDate.setTypeface(mRegular);

        edtTitle.setClickable(false);
        edtTitle.setEnabled(false);
        edtMjcNo.setClickable(false);
        edtMjcNo.setEnabled(false);
        edtName.setClickable(false);
        edtName.setEnabled(false);
        edtFlatNo.setClickable(false);
        edtFlatNo.setEnabled(false);
        edtBuildingName.setClickable(false);
        edtBuildingName.setEnabled(false);
        edtLocation.setClickable(false);
        edtLocation.setEnabled(false);
        edtCity.setClickable(false);
        edtCity.setEnabled(false);
        edtPinCode.setClickable(false);
        edtPinCode.setEnabled(false);
        edtMobileNo.setClickable(false);
        edtMobileNo.setEnabled(false);
        edtEmailId.setClickable(false);
        edtEmailId.setEnabled(false);
        edtInstallationDate.setClickable(false);
        edtInstallationDate.setEnabled(false);
        edtMeterNo.setClickable(false);
        edtMeterNo.setEnabled(false);
        edtTestDate.setClickable(false);
        edtTestDate.setEnabled(false);
        edtTestPressure.setClickable(false);
        edtTestPressure.setEnabled(false);
        edtTestDuration.setClickable(false);
        edtTestDuration.setEnabled(false);
        edtInitialReading.setClickable(false);
        edtInitialReading.setEnabled(false);
        edtConversionDate.setClickable(false);
        edtConversionDate.setEnabled(false);


        btnNext1.setTypeface(mRegular);
        btnNext2.setTypeface(mRegular);
        btnNext3.setTypeface(mRegular);
//        btnSubmit.setTypeface(mRegular);

//        btnClearContractor.setTypeface(mRegular);
//        btnClearEngg.setTypeface(mRegular);

        imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.GONE);

        viewContractorSignature = findViewById(R.id.view_signature_contractor);
        signatureViewContractor = new SignatureView(mContext);
        viewContractorSignature.addView(signatureViewContractor);

        viewEnggSignature = findViewById(R.id.view_signature_engg);
        signatureViewEngg = new SignatureView(mContext);
        viewEnggSignature.addView(signatureViewEngg);

        viewConsumerSignature = findViewById(R.id.view_signature_consumer);
        signatureViewConsumer = new SignatureView(mContext);
        viewConsumerSignature.addView(signatureViewConsumer);

        linearConsumerSign = findViewById(R.id.linear_signature_view_consumer);
        linearContractorSign = findViewById(R.id.linear_signature_view_contractor);
        linearEnggSign = findViewById(R.id.linear_signature_view_engg);

        imgTpiSign = findViewById(R.id.img_tpi_sign);


        edtTitle.setText("Title :- Meter Job Card");
        edtMjcNo.setText("Registration No:- " + todayModel.registrationNo);
        edtName.setText("Name:- " + todayModel.consumerName);
        edtFlatNo.setText("Flat No:- " + todayModel.flatNo);
        edtBuildingName.setText("Building:- " + todayModel.buildingName);
        edtLocation.setText("Location:- " + todayModel.location);
        edtCity.setText("City:- " + todayModel.city);
        edtPinCode.setText("Pin Code:- " + todayModel.pinCode);
        edtMobileNo.setText("Phone No:- " + todayModel.getMobileNumber());
        edtEmailId.setText("Email Id:- " + todayModel.email);

        if (conversionDate.contains(" ")) {
            conversionDate = conversionDate.substring(0, conversionDate.indexOf(" "));
            newConversionDate = conversionDate.replace("/", "-");

        }


        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat oldFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Date conDate = null;
        Date testDate = null;


        try {
            date = originalFormat.parse(todayModel.installationDate);
            conDate = originalFormat.parse(newConversionDate);
            testDate = oldFormat.parse(todayModel.testDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        String formattedConDate = targetFormat.format(conDate);
        String formattedTestDate = targetFormat.format(testDate);

        edtInstallationDate.setText("Installation Date:- " + formattedDate);
        edtConversionDate.setText("Conversion Date:- " + formattedConDate);
        edtMeterNo.setText("Meter No:- " + todayModel.meterNo);
        edtTestDate.setText("Test Date:- " + formattedTestDate);
        edtTestPressure.setText("Test Pressure:- " + todayModel.pressureGuage);
        edtInitialReading.setText("Initial Reading:- " + initialReading);
        edtTestDuration.setText("Test Duration:- " + todayModel.testDuration);





        if (todayModel.imgTpi != null && !todayModel.imgTpi.equals("")) {
            Picasso.get().load(todayModel.imgTpi).placeholder(R.drawable.layout_background).error(R.drawable.default_image).into(imgTpiSign);
        }


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_one_yes:
                        one = "YES";
                        radioCheck.put("1", "1");
                        break;
                    case R.id.radio_one_no:
                        one = "NO";
                        radioCheck.remove("1");
                        break;
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_two_yes:
                        two = "YES";
                        radioCheck.put("2", "2");
                        break;
                    case R.id.radio_two_no:
                        two = "NO";
                        radioCheck.remove("2");
                        break;
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_three_yes:
                        three = "YES";
                        radioCheck.put("3", "3");
                        break;
                    case R.id.radio_three_no:
                        three = "NO";
                        radioCheck.remove("3");
                        break;
                }
            }
        });
        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_four_yes:
                        four = "YES";
                        radioCheck.put("4", "4");
                        break;
                    case R.id.radio_four_no:
                        four = "NO";
                        radioCheck.remove("4");
                        break;
                }
            }
        });
        radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_five_yes:
                        five = "YES";
                        radioCheck.put("5", "5");
                        break;
                    case R.id.radio_five_no:
                        five = "NO";
                        radioCheck.remove("5");
                        break;
                }
            }
        });
        radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_six_yes:
                        six = "YES";
                        radioCheck.put("6", "6");
                        break;
                    case R.id.radio_six_no:
                        six = "NO";
                        radioCheck.remove("6");
                        break;
                }
            }
        });
        radioGroup7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_seven_yes:
                        seven = "YES";
                        radioCheck.put("7", "7");
                        break;
                    case R.id.radio_seven_no:
                        seven = "NO";
                        radioCheck.remove("7");
                        break;
                }
            }
        });
        radioGroup8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_eight_yes:
                        eight = "YES";
                        radioCheck.put("8", "8");
                        break;
                    case R.id.radio_eight_no:
                        eight = "NO";
                        radioCheck.remove("8");
                        break;
                }
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


    }

    @Override
    public void onClick(View v) {

        if (v == btnNext1) {
            relativeLayoutOne.setVisibility(View.GONE);
            relativeLayoutTwo.setVisibility(View.VISIBLE);
            relativeLayoutThree.setVisibility(View.GONE);
            relativeLayoutFour.setVisibility(View.GONE);
        } else if (v == btnNext2) {
            relativeLayoutOne.setVisibility(View.GONE);
            relativeLayoutTwo.setVisibility(View.GONE);
            relativeLayoutThree.setVisibility(View.VISIBLE);
            relativeLayoutFour.setVisibility(View.GONE);
        } else if (v == btnNext3) {
            relativeLayoutOne.setVisibility(View.GONE);
            relativeLayoutTwo.setVisibility(View.GONE);
            relativeLayoutThree.setVisibility(View.GONE);
            relativeLayoutFour.setVisibility(View.VISIBLE);
        } else if (v == btnClearContractor) {
            signatureViewContractor.clearSignature();
        } else if (v == btnClearEngg) {
            signatureViewEngg.clearSignature();
        } else if (v == btnClearConsumer) {
            signatureViewConsumer.clearSignature();
        } else if (v == btnComplete) {
            if (signatureViewConsumer.getSignature() != null) {
                Uri tempUriConsumer = getImageUri(mContext, signatureViewConsumer.getSignature());

                fileConsumerSign = new File(getRealPathFromURI(tempUriConsumer));
                linearContractorSign.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(mContext, getString(R.string.please_add_consumer_signature), Toast.LENGTH_SHORT).show();
            }
        } else if (v == btnUpload) {
            if (signatureViewEngg.getSignature() != null) {
                // TODO: 11-03-2019 Add upload function

                Uri tempUriEngg = getImageUri(mContext, signatureViewEngg.getSignature());

                fileEnggSign = new File(getRealPathFromURI(tempUriEngg));

                uploadData();

            } else {
                Toast.makeText(mContext, getString(R.string.add_bgl_engineer_signature), Toast.LENGTH_SHORT).show();
            }
        } else if (v == btnBackContractor) {
            linearContractorSign.setVisibility(View.GONE);
        } else if (v == btnBackEngg) {
            linearEnggSign.setVisibility(View.GONE);
            linearContractorSign.setVisibility(View.VISIBLE);
        } else if (v == btnNextContractor) {
            if (signatureViewContractor.getSignature() != null) {
                Uri tempUriContractor = getImageUri(mContext, signatureViewContractor.getSignature());

                fileContractorSign = new File(getRealPathFromURI(tempUriContractor));

                linearContractorSign.setVisibility(View.GONE);
                linearEnggSign.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(mContext, getString(R.string.please_add_contractor_signature), Toast.LENGTH_SHORT).show();
            }
        }
        /* else if (v == btnClearEngg) {
            signatureViewEngg.clearSignature();
        } else if (v == btnClearContractor) {
            signatureViewContractor.clearSignature();
        }*/ /*else if (v == btnSubmit) {
            if (signatureViewContractor.getSignature() != null) {
                Uri tempContractorUri = getImageUri(mContext, signatureViewContractor.getSignature());
                fileContractorSign = new File(getRealPathFromURI(tempContractorUri));
                if (fileContractorSign != null) {
                    if (signatureViewEngg.getSignature() != null) {
                        Uri tempEnggSign = getImageUri(mContext, signatureViewEngg.getSignature());
                        fileEnggSign = new File(getRealPathFromURI(tempEnggSign));
                        if (fileEnggSign != null) {
                            // TODO: 11-03-2019 Add upload function
                        }
                    } else {
                        Toast.makeText(mContext, R.string.add_bgl_engineer_signature, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(mContext, R.string.add_contractor_signature, Toast.LENGTH_SHORT).show();
            }
        }*/

    }


    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.UPLOAD_MJC_CHECK_LIST: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        dismissLoadingDialog();
                        if (jsonResponse.message.equalsIgnoreCase(CommonUtility.getString(mContext, R.string.error_download_data))) {
                            CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.error_download_data),
                                    getString(R.string.main_activity), false);
                            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            customDialog.show();
                            customDialog.setCancelable(false);
                        } else {
                            new UploadImage().execute();
                            Toast.makeText(mContext, getString(R.string.mjc_for_uploaded_successfully), Toast.LENGTH_SHORT).show();
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
                break;
            }

        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {

    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {

    }

    private void uploadData() {
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            try {
                showLoadingDialog();
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArrayRadio = new JSONArray();

                List<String> checkKeyList = new ArrayList<>(radioCheck.keySet());
                List<String> checkValueList = new ArrayList<>(radioCheck.values());

                for (int i = 0; i < radioCheck.size(); i++) {
                    jsonArrayRadio.put(checkKeyList.get(i));
                }
                try {
                    jsonObject.put("radio_check_list", jsonArrayRadio);
                    jsonObject.put("list_id", jobId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        UPLOAD_MJC_CHECK_LIST, this, "");
                App.getInstance().addToRequestQueue(request, UPLOAD_MJC_CHECK_LIST);
            } catch (Exception e) {
                dismissLoadingDialog();
                e.printStackTrace();
            }


        }
    }





    /*private void captureLocation() {
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
    }*/

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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

    private class UploadImage extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);

                MultipartUtility multipartUtility = new MultipartUtility(SIGNATURE_IMAGE_UPLOAD, "UTF-8");

                multipartUtility.addFormField("image_sign_id", jobId);


                if (fileConsumerSign != null)
                    multipartUtility.addFilePart("customer_signature", fileConsumerSign);
                if (fileContractorSign != null)
                    multipartUtility.addFilePart("contractor_signature", fileContractorSign);
                if (fileEnggSign != null)
                    multipartUtility.addFilePart("bgl_engg_signature", fileEnggSign);


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
                        if (fileEnggSign != null)
                            fileEnggSign.delete();
                        if (fileContractorSign != null)
                            fileContractorSign.delete();
                        if (fileEnggSign != null)
                            fileEnggSign.delete();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        DialogCreator.showBackPressDialog(mContext);
    }

}