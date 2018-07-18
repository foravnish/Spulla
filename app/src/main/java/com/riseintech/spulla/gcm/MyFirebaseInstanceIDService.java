package com.riseintech.spulla.gcm;


import android.content.Context;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.riseintech.spulla.utils.MyPreference;

/**
 * Created by user on 11/2/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseIIDService";
    Context context;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);
        MyPreference.setgcm_token(this,refreshedToken);
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);



        //Displaying token on logcat
    }
}
