package com.fieldforce.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.adapters.DocumentIdAdapter;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.MultipartUtility;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Service extends android.app.Service implements ApiServiceCaller {


    protected static final int NOTIFICATION_ID = 1337;
    private static String TAG = "Service";
    private static Service mCurrentService;
    private int counter = 0;
    private Context mContext;


    private ArrayList<RegistrationModel> registrationToUpload;
    private TodayModel todayModel, newTodayModel;

    public Service() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        counter = 0;
        mContext = this;
        todayModel = new TodayModel();
        newTodayModel = new TodayModel();

        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * it starts the process in foreground. Normally this is done when screen goes off
     * THIS IS REQUIRED IN ANDROID 8 :
     * "The system allows apps to call Context.startForegroundService()
     * even while the app is in the background.
     * However, the app must call that service's startForeground() method within five seconds
     * after the service is created."
     */
    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "restarting foreground");
            try {
                Notification notification = new Notification();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, "Background Service Running", "Field Force App Background Service is Running", R.drawable.ic_sleep));
                Log.i(TAG, "restarting foreground successful");
                startTimer();

            } catch (Exception e) {
                Log.e(TAG, "Error in notification " + e.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i(TAG, "onDestroy called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(AppConstants.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }


    /**
     * this is called when the process is killed by Android
     *
     * @param rootIntent
     */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
//        Log.i(TAG, "onTaskRemoved called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(AppConstants.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        // do not call stoptimertask because on some phones it is called asynchronously
        // after you swipe out the app and therefore sometimes
        // it will stop the timer after it was restarted
        // stoptimertask();
    }


    /**
     * static to avoid multiple timers to be created when the service is called several times
     */
    private static Timer timer;
    private static TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
//        Log.i(TAG, "Starting timer");

        //set a new Timer - if one is already running, cancel it to avoid two running at the same time
        stoptimertask();
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

//        Log.i(TAG, "Scheduling...");
        //schedule the timer, to wake up every 1 second
//        timer.schedule(timerTask, 90000, 1800000); // 30 minutes
//        timer.schedule(timerTask, 1000, 600000); // 2 minutes
//        timer.schedule(timerTask, 1000, 60000); // 1 minutes
//        timer.schedule(timerTask, 1000, 15000); // 15 seconds
        timer.schedule(timerTask, 1000, 300000); // 5 minutes
        //timer.schedule(timerTask, 60000, 3600000); // 1 hour
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
//        Log.i(TAG, "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
                if (isNetworkAvailable()) {
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
                                documentOne.add(j,checklist11.get(j));
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
                                documentTwo.add(l,checklist12.get(l));
                            }

                            registrationToUpload.get(0).documentsAdd = documentTwo.get(0);

                            JSONObject jObject = getReadingJson(registrationToUpload);

                            uploadMeterReading(jObject);
                        }
                    }
                }
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static Service getmCurrentService() {
        return mCurrentService;
    }

    public static void setmCurrentService(Service mCurrentService) {
        Service.mCurrentService = mCurrentService;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.UPLOAD_NSC_FORM: {
                try {
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
        }

    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {

    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {

    }

    /*public class UploadData extends AsyncTask<String, String, String> {


        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {

                MultipartUtility multipartUtility = new MultipartUtility(ApiConstants.UPLOAD_NSC_FORM, "UTF-8");
                if (registrationToUpload.get(0).enquiryId != null) {
                    multipartUtility.addFormField("enquiry_id", registrationToUpload.get(0).enquiryId);
                }
                multipartUtility.addFormField("consumer_category", registrationToUpload.get(0).consumerCategory);
                multipartUtility.addFormField("consumer_subcategory", registrationToUpload.get(0).consumerSubCategory);
                multipartUtility.addFormField("state", registrationToUpload.get(0).state);
                multipartUtility.addFormField("name", registrationToUpload.get(0).name);
                multipartUtility.addFormField("addhaar", registrationToUpload.get(0).addhaar);
                multipartUtility.addFormField("email_id", registrationToUpload.get(0).emailId);
//                multipartUtility.addFormField("occupation", spinnerOccupation.getSelectedItem().toString());
//                multipartUtility.addFormField("other", edtOther.getText().toString());
                multipartUtility.addFormField("flat_no", registrationToUpload.get(0).flatNo);
                multipartUtility.addFormField("building_name", registrationToUpload.get(0).buildingName);
                multipartUtility.addFormField("location", registrationToUpload.get(0).location);
                multipartUtility.addFormField("area", registrationToUpload.get(0).area);
                multipartUtility.addFormField("city", registrationToUpload.get(0).city);
                multipartUtility.addFormField("pincode", registrationToUpload.get(0).pincode);
                multipartUtility.addFormField("mobile", registrationToUpload.get(0).mobile);
                multipartUtility.addFormField("premise", registrationToUpload.get(0).premise);
                multipartUtility.addFormField("req_load", "");
                multipartUtility.addFormField("cont_load", "");


                multipartUtility.addFormField("enquiry_no", registrationToUpload.get(0).enquiryNo);
                multipartUtility.addFormField("consumer_no", registrationToUpload.get(0).consumerNo);


                multipartUtility.addFormField("save_type", "FF Mobile");
                multipartUtility.addFormField("scheme_name", registrationToUpload.get(0).schemeName);
                multipartUtility.addFormField("scheme_amount", registrationToUpload.get(0).schemeAmount);

                if (!registrationToUpload.get(0).schemeAmount.equals("0")) {
                    multipartUtility.addFormField("payment_method", registrationToUpload.get(0).schemeAmount);
                } else {
                    multipartUtility.addFormField("payment_method", "");
                }


                multipartUtility.addFormField("cheque_no", registrationToUpload.get(0).chequeNo);
                multipartUtility.addFormField("cheque_branch", registrationToUpload.get(0).chequeBranch);
                multipartUtility.addFormField("cheque_date", registrationToUpload.get(0).chequeDate);
                multipartUtility.addFormField("cheque_bank", registrationToUpload.get(0).chequeBank);
                multipartUtility.addFormField("cheque_micr", registrationToUpload.get(0).chequeMicr);
                multipartUtility.addFormField("dd_no", registrationToUpload.get(0).ddNo);
                multipartUtility.addFormField("bank_name", registrationToUpload.get(0).bankName);
                multipartUtility.addFormField("dd_date", registrationToUpload.get(0).ddDate);
                multipartUtility.addFormField("dd_micr", registrationToUpload.get(0).ddMicr);
                multipartUtility.addFormField("dd_branch", registrationToUpload.get(0).ddBranch);
                multipartUtility.addFormField("latitude", registrationToUpload.get(0).latitude);
                multipartUtility.addFormField("longitude", registrationToUpload.get(0).longitude);
                multipartUtility.addFormField("connectivity", registrationToUpload.get(0).connectivity);

                multipartUtility.addFormField("is_nsc_new", registrationToUpload.get(0).isNscNew);
                multipartUtility.addFormField("plot_no", registrationToUpload.get(0).plotNo);
                multipartUtility.addFormField("wing", registrationToUpload.get(0).wing);
                multipartUtility.addFormField("road_no", registrationToUpload.get(0).roadNo);
                multipartUtility.addFormField("landmark", registrationToUpload.get(0).landmark);
                multipartUtility.addFormField("district", registrationToUpload.get(0).district);
                multipartUtility.addFormField("society_name", registrationToUpload.get(0).societyName);
                multipartUtility.addFormField("floor_no", registrationToUpload.get(0).floorNo);


                ArrayList<String> checklist11 = new ArrayList<>();
                String data = registrationToUpload.get(0).documents;
                String[] items = data.split("\\|");
                Collections.addAll(checklist11, items);

                for (int i = 0; i < checklist11.size(); i++) {
                    multipartUtility.addFormField("documents", checklist11.get(i));

                }

                ArrayList<String> checklist12 = new ArrayList<>();
                String data1 = registrationToUpload.get(0).documentsAdd;
                String[] items1 = data1.split("\\|");
                Collections.addAll(checklist12, items1);

                for (int i = 0; i < DocumentIdAdapter.checkParamsListAddress.size(); i++) {
                    checklist12.add(i, DocumentIdAdapter.checkParamsListAddress.get(i));

                }

                for (int i = 0; i < checklist12.size(); i++) {
                    multipartUtility.addFormField("documents_add", checklist12.get(i));
                }


                if (!registrationToUpload.get(0).File0.isEmpty()) {
                    multipartUtility.addFormField("File0", registrationToUpload.get(0).File0);
                } else {
                    multipartUtility.addFormField("File0", "");
                }
                if (!registrationToUpload.get(0).File1.isEmpty()) {
                    multipartUtility.addFilePart("File1", getImage(registrationToUpload.get(0).File1));
                } else {
                    multipartUtility.addFormField("File1", "");
                }
                if (!registrationToUpload.get(0).FileAddProof0.isEmpty()) {
                    multipartUtility.addFilePart("File_add_proof0", getImage(registrationToUpload.get(0).FileAddProof0));
                } else {
                    multipartUtility.addFormField("File_add_proof0", "");
                }
                if (!registrationToUpload.get(0).FileAddProof1.isEmpty()) {
                    multipartUtility.addFilePart("File_add_proof1", getImage(registrationToUpload.get(0).FileAddProof1));
                } else {
                    multipartUtility.addFormField("File_add_proof1", "");
                }
                if (!registrationToUpload.get(0).FileNocProof.isEmpty()) {
                    multipartUtility.addFilePart("File_noc_proof", getImage(registrationToUpload.get(0).FileNocProof));
                } else {
                    multipartUtility.addFormField("File_noc_proof", "");
                }

                if (!registrationToUpload.get(0).FileChequeDD.isEmpty()) {
                    multipartUtility.addFilePart("File_cheque_dd", getImage(registrationToUpload.get(0).FileChequeDD));
                } else {
                    multipartUtility.addFormField("File_cheque_dd", "");
                }
                if (!registrationToUpload.get(0).FileSign.isEmpty()) {
                    multipartUtility.addFilePart("File_sign", getImage(registrationToUpload.get(0).FileSign));
                } else {
                    multipartUtility.addFormField("File_sign", "");
                }

                multipartUtility.addFormField("image_count", registrationToUpload.get(0).imageCount);
                multipartUtility.addFormField("image_count_add", registrationToUpload.get(0).imageCountAdd);


                multipartUtility.addFormField("vendor_id", registrationToUpload.get(0).vendorId);
                multipartUtility.addFormField("field_force_id", registrationToUpload.get(0).fieldForceId);


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

                        String nscId = response.getString("nsc_id");
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




                        *//*if (File1 != null)
                            File1.delete();
                        if (File2 != null)
                            File2.delete();
                        if (File3 != null)
                            File3.delete();
                        if (File4 != null)
                            File4.delete();
                        if (File5 != null)
                            File5.delete();*//*



                        *//*if (fileCheque != null)
                            fileCheque.delete();
                        if (fileDD != null)
                            fileDD.delete();*//*

                    } else {
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

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

    public File getImage(String bitmapString) {

        Bitmap bitmap = CommonUtility.getBitmapDecodedString(bitmapString);
        Uri tempUri = getImageUri(mContext, bitmap);
        File file = new File(getRealPathFromURI(tempUri));
        return file;

    }


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


    private void uploadMeterReading(JSONObject object) {
        JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, object, ApiConstants.UPLOAD_NSC_FORM, this, null);
        App.getInstance().addToRequestQueue(request, ApiConstants.UPLOAD_NSC_FORM);
    }

}
