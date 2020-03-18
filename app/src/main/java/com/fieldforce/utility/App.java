package com.fieldforce.utility;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static App mInstance;
    public ArrayList<String> permissions;
    private static Typeface mRegularType, mMediumType, mSemiBold;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Fabric.with(this, new Crashlytics());
        permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.CALL_PHONE);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_SMS);

        mRegularType = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Montserrat-Regular.ttf");
        mMediumType = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Montserrat-Medium.ttf");
        mSemiBold = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Montserrat-SemiBold.ttf");
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public static Typeface getMontserratRegularFont() {
        return mRegularType;
    }

    public static Typeface getMontserratMediumFont() {
        return mMediumType;
    }

    public static Typeface getMontserratBoldFont() {
        return mSemiBold;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void initPicassoSetup() {
        //Setup Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        builder.defaultBitmapConfig(Bitmap.Config.RGB_565);
        Picasso.setSingletonInstance(built);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
