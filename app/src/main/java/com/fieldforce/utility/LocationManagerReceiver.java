package com.fieldforce.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocationManagerReceiver extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    private boolean hasGps;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    //    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 5 Seconds

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    //save data vars
    private Location mLastLocation;

    public LocationManagerReceiver(Context context) {
        this.mContext = context;

        PackageManager pm = context.getPackageManager();
        hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

        if (hasGps) {
            //Toast.makeText(mContext,"GPS is available in your device.",Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(mContext,"GPS is not available in your device.",Toast.LENGTH_LONG).show();
        }

        if (!isGooglePlayServicesAvailable((Activity) mContext))
            Toast.makeText(mContext, "Please Update Google Play services.", Toast.LENGTH_LONG).show();
        else if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && hasGps) {
                // no GPS is enabled
                //Toast.makeText(mContext,"GPS not enabled",Toast.LENGTH_LONG).show();
                showLocationEnableDialog();
            } else if (!isNetworkEnabled && !hasGps) {
                // no network provider is enabled
                //Toast.makeText(mContext,"Network Provider not enabled",Toast.LENGTH_LONG).show();
                showLocationEnableDialog();
            } else {
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    //Toast.makeText(mContext,"Get GPS Location",Toast.LENGTH_LONG).show();
                    //if (mLastLocation == null) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLastLocation != null) {
                                latitude = mLastLocation.getLatitude();
                                longitude = mLastLocation.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (mLastLocation != null) {
                                latitude = mLastLocation.getLatitude();
                                longitude = mLastLocation.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

                //  Toast.makeText(mContext,"LAT: "+location.getLatitude()+" LONG: "+location.getLongitude(),Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLastLocation;

    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(LocationManagerReceiver.this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(mContext,"LAT: "+location.getLatitude()+" LONG: "+location.getLongitude(),Toast.LENGTH_LONG).show();
        stopUsingGPS();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(mContext,"provider disabled ",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Toast.makeText(mContext,"provider enabled ",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(mContext,"on status changed",Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
                        // requests here.
//                        Toast.makeText(mContext, "result SUCCESS", Toast.LENGTH_LONG).show();
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
//                        Toast.makeText(mContext, "result RESOLUTION_REQUIRED", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext, "LOCATION_SETTINGS_CHANGE_UNAVAILABLE", Toast.LENGTH_LONG).show();
                        //  DialogCreator.showMessageDialog(mContext, getString(R.string.location_settings_unavailable_please_change_manually), getString(R.string.error));
                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(mContext,"Google Service Connected",Toast.LENGTH_LONG).show();
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(mContext,"Google Service not Connected",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(mContext,"Google Not Connected",Toast.LENGTH_LONG).show();
    }
}