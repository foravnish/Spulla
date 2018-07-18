package com.riseintech.spulla;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PermissionUtils;
import com.riseintech.spulla.utils.PlayerDetails;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adm on 8/5/2016.
 */
public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>,
        ActivityCompat.OnRequestPermissionsResultCallback {
    Thread timerThread;
    private ProgressDialog pDialog;
    Context context;
    double mLatitude = 0;
    double mLongitude = 0;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    String provider;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 3;
    int REQUEST_CHECK_SETTINGS = 100;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    private GPSTracker gpsTracker;

    private boolean mPermissionDenied = false;
    private boolean getlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        context = SplashScreen.this;
        gpsTracker = new GPSTracker(context);
        checkGpsPermission();
        checkPermissions();
       /* GPSTracker gpsTracker=new GPSTracker(context);
        gpsTracker.canGetLocation();
        Location location=gpsTracker.getLocation();
        Log.w("my current location ",""+gpsTracker.canGetLocation());
        if(location!=null){

            Toast.makeText(SplashScreen.this,"lat and long: "+gpsTracker.getLatitude()+" "+gpsTracker.getLongitude(),Toast.LENGTH_SHORT).show();
        }*/

    }

    public void checkGpsPermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            checkGpsStatus();

        }
    }

    private void checkGpsStatus() {
        try {
            if (!gpsTracker.canGetLocation()) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);

            } else {
                startHandler();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startHandler() {
        // Toast.makeText(context, "lat: " + gpsTracker.getLatitude() + ", long " + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        PlayerDetails.current_lat = gpsTracker.getLatitude();
        PlayerDetails.current_long = gpsTracker.getLongitude();
        if (MyPreference.loadUserstatus(SplashScreen.this)) {
            //new CricketPage(SplashScreen.this).execute();
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        } else {
            timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getlocation) {

        } else {
        }

    }




   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        // finish();
                        return;
                    }
                }
                //all permissions were granted
                checkGpsStatus();
                break;
        }
    }



   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkGpsStatus();
                }
                break;
        }
    }*/

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(
                            mGoogleApiClient,
                            builder.build()
                    );
            result.setResultCallback(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(SplashScreen.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                startHandler();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                // startActivity(intent);
            }
        }
    }


    public class CricketPage extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

       /* String schoolName=editSchoolView.getText().toString();
        String collegeName=editcollegeView.getText().toString();
        String workplace=editworkView.getText().toString();
        String playingArea1=editarea1View.getText().toString();
        String playingArea2=editarea2View.getText().toString();
        String playingArea3=editarea3View.getText().toString();*/

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        Context context;
        HashMap<String, String> params = new HashMap<>();

        public CricketPage(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            //pDialog = new ProgressDialog(SplashScreen.this);
            //pDialog.setCancelable(false);
            // pDialog.setMessage("Please wait...");
            //pDialog.show();

           /* params.put("image_name", " ");
            params.put("school", schoolName);
            params.put("college",collegeName);
            params.put("workplace", workplace);
            params.put("playing_area1", playingArea1);
            params.put("playing_area2",playingArea2);
            params.put("playing_area3", playingArea3);*/
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest("http://cricapi.com/api/cricket/", "GET", params);
                Log.d("upload ", "------------" + json);
                if (json != null) {
                    Log.d("upload result", json.toString());

                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(JSONObject json) {
            String success = "";
            final ArrayList<HashMap<String, String>> contactList = new ArrayList<>();
            String message = "";
            String data = "";
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
            success = "true";

            HashMap<String, String> contact = new HashMap<>();
            contact.put("unique_id", "1");
            contact.put("title", "ame");
            contact.put("description", "desc");
            if (json != null) {
            /*
                try {

                 //   success = json.getString("cache");

                    JSONArray ja = json.getJSONArray("data");

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jsonObject = ja.getJSONObject(i);
                        HashMap<String, String> contact = new HashMap<>();

                        // int id = Integer.parseInt(jsonObject.optString("id").toString());
                        String uid = jsonObject.getString("unique_id");
                        String title = jsonObject.getString("title");
                        String description = jsonObject.getString("description");
                        contact.put("unique_id", uid);
                        contact.put("title", title);
                        contact.put("description", description);
                        // adding contact to contact list
                        contactList.add(contact);
                        data += "Blog Number " + (i + 1) + " \n Blog Name= " + title + " \n unique_id= " + uid + " \n\n\n\n ";
                    }
                    // Toast.makeText(SplashScreen.this, "" + data, Toast.LENGTH_SHORT).show();
                    //Log.d("unique_id>>>>",contactList.get(0).get("unique_id"));

                   *//* success = json.getString("status");
                    message = json.getString("msg");
                    Toast.makeText(UpgradeActivity.this, "" + success, Toast.LENGTH_SHORT).show();
                    Log.d("message login>>>>",message);*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
            } else {
                Toast.makeText(SplashScreen.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                /*Thread timerThread = new Thread() {
                    public void run() {
                        try {

                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("myscore", contactList);
                            context.startActivity(intent);

                        }
                    }
                };
                timerThread.start();*/
            }

            if (success.equals("true")) {
                Thread timerThread = new Thread() {
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("myscore", contactList);
                            context.startActivity(intent);
                            finish();
                        }
                    }
                };
                timerThread.start();


            } else {
                Toast.makeText(SplashScreen.this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

}
