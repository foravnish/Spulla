package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 1/7/2017.
 */

public class SendRequestAsync extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    HashMap<String, String> params = new HashMap<>();

    ProgressDialog pDialog;
    String reciver_id, rType;
    Context mContext;

    public  SendRequestAsync(Context mContext, String reciver_id, String rType) {
        this.reciver_id = reciver_id;
        this.mContext = mContext;
        this.rType = rType;

    }

    @Override
    protected void onPreExecute() {
        final String sender_id = MyPreference.loadUserid(mContext);
        params.put("type", rType);
        params.put("user_id", sender_id);
        params.put("sender_id", reciver_id);


    }

    @Override
    protected JSONObject doInBackground(String... args) {
        JSONObject json;
        json = jsonParser.makeHttpRequest(
                Api.URL_SEND_REQUEST, "POST", params);

        try {
            //Log.d("JSON ", "------------" + json);
            if (json != null) {
                //Log.d("JSON result", json.toString());

                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(JSONObject json) {
        String status = "";
        String data = "";
        if (json != null) {
            try {
                status = json.getString("status");
                if (rType.equalsIgnoreCase("send_request")) {
                    if (status.equalsIgnoreCase("1")) {
                        Log.d("dfgdfgdfgdfgh","true");
                        Toast.makeText(mContext, "Request Sent", Toast.LENGTH_LONG).show();
                    }
                    else if (status.equalsIgnoreCase("0")) {
                        Log.d("dfgdfgdfgdfgh","false");
                        Toast.makeText(mContext, "Request Already Sent", Toast.LENGTH_LONG).show();
                        //FindPlayerAdapter.imgrequest.setImageResource(R.drawable.r_accept);
                    }

                } else if (rType.equalsIgnoreCase("reject")) {
                    if (status.equalsIgnoreCase("1")) {
                        Log.d("dfgdfgdfgdfgh","true");
                        Toast.makeText(mContext, "Block Already irrelevant profile", Toast.LENGTH_LONG).show();
                    }
                    else if (status.equalsIgnoreCase("0")) {
                        Log.d("dfgdfgdfgdfgh","false");
                        Toast.makeText(mContext, "Block irrelevant profile", Toast.LENGTH_LONG).show();

                    }

                } else if (rType.equalsIgnoreCase("block")) {

                    if (status.equalsIgnoreCase("1")) {
                        Log.d("dfgdfgdfgdfgh","true");
                        Toast.makeText(mContext, "Block permanently", Toast.LENGTH_LONG).show();
                    }
                    else if (status.equalsIgnoreCase("0")) {
                        Log.d("dfgdfgdfgdfgh","false");
                        Toast.makeText(mContext, "Already Block  permanently", Toast.LENGTH_LONG).show();

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
