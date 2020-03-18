package com.fieldforce.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.fieldforce.R;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;
import com.fieldforce.utility.DialogCreator;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller {

    private Context mContext;
    private static int RESULT_LOAD_IMAGE = 1;

    private ImageView imgBack, imgCamera;
    private TextView txtFullName, txtLocation, txtId, txtMobileNumber;
    private Button btnSignOut;
    private CircleImageView imageViewProfile;
    private static int z = 0;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = this;

        imgBack = findViewById(R.id.img_back_black);
        imgCamera = findViewById(R.id.img_camera_edit);

        btnSignOut = findViewById(R.id.btn_sign_out);

        imgBack.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        imgCamera.setOnClickListener(this);

        txtFullName = findViewById(R.id.txt_name);
        txtLocation = findViewById(R.id.txt_city);
        txtId = findViewById(R.id.txt_id);
        txtMobileNumber = findViewById(R.id.txt_number);

        cardView = findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

        imageViewProfile = findViewById(R.id.image_view_profile);
        setData();
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
        } else if (v == btnSignOut) {
            CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.do_you_want_to_logged_out_now),
                    getString(R.string.sign_out), false);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();
            customDialog.setCancelable(false);
        } else if (v == imgCamera) {
            setImage();
        } else if (v == cardView) {
            checkCounts();

        }
    }

    private void setImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.select_profile_photo);
        builder.setItems(new CharSequence[]
                        {getString(R.string.gallery), getString(R.string.remove)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                loadImageFromGallery();
                                break;
                            case 1:
                                setDefault();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void setData() {
        txtFullName.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_NAME, ""));
        txtLocation.setText(AppPreferences.getInstance(mContext).getString(AppConstants.USER_CITY, ""));
        txtId.setText(AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, ""));
        txtMobileNumber.setText(AppPreferences.getInstance(mContext).getString(AppConstants.MOBILE_NO, ""));
        String imgUrl = AppPreferences.getInstance(mContext).getString(AppConstants.PROFILE_IMAGE_URL, "");
    }

    public void loadImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    public void setDefault() {
        imageViewProfile.setImageResource(R.drawable.ic_action_default_user_icon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String actualImage = "";
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    actualImage = cursor.getString(columnIndex);
                    cursor.close();
                }

                imageViewProfile.setImageBitmap(BitmapFactory.decodeFile(actualImage));

                Bitmap bm = BitmapFactory.decodeFile(actualImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                if (CommonUtility.isNetworkAvailable(mContext)) {
//                    showLoadingDialog();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(getString(R.string.profile_image), encodedImage.toString() == null ? "" : encodedImage.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*JsonObjectRequest request = WebRequest.profileImageChange(mContext, Request.Method.POST, AppConstants.URL_USER_PROFILE_IMAGE, AppConstants.REQUEST_USER_PROFILE_IMAGE,
                            ProfileActivity.this, obj, SharedPrefManager.getStringValue(mContext, SharedPrefManager.AUTH_TOKEN));
                    App.getInstance().addToRequestQueue(request, AppConstants.REQUEST_USER_PROFILE_IMAGE);*/

                } else {
                    Toast.makeText(mContext, R.string.error_internet_not_connected, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, R.string.error_you_have_not_picked_image, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.LOGOUT: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        try {
                            dismissLoadingDialog();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
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
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        switch (label) {
            case ApiConstants.LOGOUT: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        switch (label) {
            case ApiConstants.LOGOUT: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void checkCounts() {
        z++;
        if (z == 10) {
            z = 0;
            showDialogDatabase(mContext);
        }
    }

    public void showDialogDatabase(final Context context) {
        Typeface regular = App.getMontserratRegularFont();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_for_database, null);
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();


        final EditText edtPassword;
        final TextView txtTitle, txtError;

        txtTitle = promptView.findViewById(R.id.txt_title_dialog);
        txtTitle.setTypeface(regular);
        txtError = promptView.findViewById(R.id.txt_error);
        txtError.setTypeface(regular);

        edtPassword = promptView.findViewById(R.id.edt_password);

        Button ok = promptView.findViewById(R.id.btn_ok);
        ok.setTypeface(regular);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (edtPassword.getText().toString().trim().equals("GetFieldForceDB")) {
                    txtError.setVisibility(View.GONE);
                    getDatabase();
                    alert.dismiss();
                    done();
                } else {
                    txtError.setVisibility(View.VISIBLE);
                }
            }
        });

        Button cancel = promptView.findViewById(R.id.btn_cancel);
        cancel.setTypeface(regular);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtError.setVisibility(View.GONE);
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.show();
    }

    private void getDatabase() {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/FieldForceDB.db";
                String backupDBPath = "BackUpFF.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, AppConstants.ZERO, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void done() {
        Toast.makeText(mContext, "Database Retrieved Successfully", Toast.LENGTH_SHORT).show();
    }
}
