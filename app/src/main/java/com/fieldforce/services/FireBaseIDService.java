package com.fieldforce.services;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;

public class FireBaseIDService extends FirebaseInstanceIdService {

    private Context mContext;

    @Override
    public void onTokenRefresh() {
        mContext = this;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AppPreferences.getInstance(mContext).putString(AppConstants.DEVICE_FCM_TOKEN, refreshedToken);
    }

}
