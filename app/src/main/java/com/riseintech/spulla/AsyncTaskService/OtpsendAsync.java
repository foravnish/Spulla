package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.riseintech.spulla.SplashScreen;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 11/4/2016.
 */

public class OtpsendAsync extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pDialog;
    Context context;
    String otp;
    JSONParser jsonParser;
    HashMap<String, String> params = new HashMap<>();
    String mobile_num;

    public OtpsendAsync(Context context, String otp, String mobile_num) {
        this.context = context;
        this.otp = otp;
        this.mobile_num = mobile_num;
        jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading  ....");
        //  pDialog.show();

    }

    protected JSONObject doInBackground(String... args) {
        params.put("otp_code", otp);
        params.put("mobile", mobile_num);

        JSONObject json = jsonParser.makeHttpRequest(Api.Send_Otp, "GET", params);
        if (json != null) {
            //Log.e("json response", ": " + json);
            return json;
        }
        return null;
    }

    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject != null && jsonObject.getString("status").equals("1")) {
                MyPreference.saveUserid(context, jsonObject.getString("id"));
                SplashScreen splashScreen = new SplashScreen();
                SplashScreen.CricketPage cricketPage = splashScreen.new CricketPage(context);
                cricketPage.execute();
            } else {
                Toast.makeText(context, "Otp not valid", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

