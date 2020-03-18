package com.fieldforce.ui.activities;


/**
 * Created by Saloni on 25-02-2017.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Helper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.models.TodayModel;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.DirectionsJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionCallback {

    private GoogleMap gMap;
    private double markerLatitude = 0.0, markerLongitude = 0.0;
    private String name = "", nscId = "", jobId = "";
    private ArrayList<HashMap<String, String>> arrayListLocation = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> map;
    private HashMap<String, String> map1;
    private Context mContext;
    private TodayModel todayModel = new TodayModel();
    private int mCards = 0;
    private static String userId;
    private ImageView imgBack;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private String currentLat = "", currentLong = "";

    private Double newMarkerLat = 0.0, newMarkerLong = 0.0;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mContext = this;
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        this.setFinishOnTouchOutside(false);
        userId = AppPreferences.getInstance(this).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }


        ArrayList<TodayModel> jobCards = new ArrayList<>();

        if (MainActivity.filterType == 0) {
            jobCards = DatabaseManager.getSiteVerificationLatLong(userId, AppConstants.CARD_STATUS_OPEN);
        } else if (MainActivity.filterType == 1) {
            jobCards = DatabaseManager.getSiteVerificationLatLongToday(userId, AppConstants.CARD_STATUS_OPEN);
        } else if (MainActivity.filterType == 2) {
            jobCards = DatabaseManager.getSiteVerificationLatLongWeek(userId, AppConstants.CARD_STATUS_OPEN);
        } else {
            jobCards = DatabaseManager.getSiteVerificationLatLongMonth(userId, AppConstants.CARD_STATUS_OPEN);
        }

        if (jobCards != null) {

            for (int i = 0; i < jobCards.size(); i++) {
                map = new HashMap<String, String>();
                if (!jobCards.get(i).getLatitude().isEmpty() && !jobCards.get(i).getLongitude().isEmpty()) {

                    map.put(getString(R.string.latitude), jobCards.get(i).getLatitude());
                    map.put(getString(R.string.longitude), jobCards.get(i).getLongitude());
                    map.put(getString(R.string.request_id), jobCards.get(i).getSite_verification_id());
                    map.put(getString(R.string.consumer_name), jobCards.get(i).getConsumerName());
                    map.put(getString(R.string.nsc_id), jobCards.get(i).getRequestId());
                    arrayListLocation.add(map);
                } else {
                    if (i == 0 && jobCards.size() == 1) {
                        Toast.makeText(this, "Location For this Job Card is not Available", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else {
            Toast.makeText(mContext, " There are no job cards", Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < arrayListLocation.size(); i++) {
            markerLatitude = Double.parseDouble(arrayListLocation.get(i).get(getString(R.string.latitude)));
            markerLongitude = Double.parseDouble(arrayListLocation.get(i).get(getString(R.string.longitude)));
            name = arrayListLocation.get(i).get(getString(R.string.consumer_name));
            jobId = arrayListLocation.get(i).get(getString(R.string.request_id));
            nscId = arrayListLocation.get(i).get(getString(R.string.nsc_id));
//            String name = arrayListLocation.get(i).get("LocationName");
            MarkerOptions marker = new MarkerOptions().position(new LatLng(markerLatitude, markerLongitude)).title(name + ", " + nscId).visible(true);

            gMap.addMarker(marker).showInfoWindow();
            LatLng coordinate = new LatLng(markerLatitude, markerLongitude);
            gMap.animateCamera((CameraUpdateFactory.newLatLngZoom(coordinate, 17)));

            /*handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            LatLng position = marker.getPosition(); //
                            newMarkerLat = position.latitude;
                            newMarkerLong = position.longitude;
                            mapOperations(currentLat, currentLong, String.valueOf(newMarkerLat), String.valueOf(newMarkerLong));
                            return true;
                        }
                    });
                }
            };
            handler.postDelayed(runnable, 5000);*/
        }
    }


    public void mapOperations(String startLat, String startLong, String endLat, String endLong) {
        /*LatLng origin = new LatLng(Double.parseDouble(startLat), -Double.parseDouble(startLat));
        LatLng destination = new LatLng(Double.parseDouble(endLat), -Double.parseDouble(endLong));
        GoogleDirection.withServerKey("AIzaSyA6NpYTKDQhzJsAjHrNXyqcjNyvsFuoQ1w")
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);*/

        LatLng current = new LatLng(Double.parseDouble(startLat), Double.parseDouble(startLong));
        LatLng destination = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLong));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(15).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        gMap.setMyLocationEnabled(true);

        MarkerOptions options = new MarkerOptions();

        if (Double.parseDouble(endLat) == 0 && Double.parseDouble(endLong) == 0) {
            options.position(current);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            Toast.makeText(mContext, "Lat Long not available", Toast.LENGTH_SHORT).show();

        } else {
            for (int i = 0; i < 2; i++) {
                // Setting the position of the marker
                if (i == 0) {
                    options.position(current);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                } else {
                    options.position(destination);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                }
            }

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(current, destination);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            gMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);

        } else {
            Toast.makeText(mContext, "" + direction.getStatus(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            currentLat = String.valueOf(mLastLocation.getLatitude());
            currentLong = String.valueOf(mLastLocation.getLongitude());

            LatLng latLng = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLong));
            if (gMap != null) {
                gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
//            mapOperations();
        } else {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        gMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onLocationChanged(Location location) {
        currentLat = String.valueOf(location.getLatitude());
        currentLong = String.valueOf(location.getLongitude());
        LatLng latLng = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLong));
        if (gMap != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of txtBinderCode
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of txtBinderCode
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false&key=AIzaSyAOpJlsnHENt0tQ5RVIjrJJniOPYM2iYxw";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
//            Log.d("ExWhileDownloadingUrl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th txtBinderCode
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th txtBinderCode
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the txtBinderCode to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th txtBinderCode
            if (lineOptions != null)
                gMap.addPolyline(lineOptions);

        }
    }
}