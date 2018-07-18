package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 12/9/2016.
 */

public class SendMsgAsync extends AsyncTask<String, String, JSONObject> {
    private Context context;
    ProgressDialog progressDialog;
    String message;

    public SendMsgAsync(Context context, String message) {
        this.context = context;
        this.message = message;

    }

    protected void onPreExecute() {
        // // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Send token");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        //  progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params1) {
        // constructor
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", MyPreference.loadUserid(context));
        params.put("message", message);
        params.put("sender_id", Util.Sender_ID);
        params.put("sender_name", MyPreference.loadUsername(context));
        params.put("sender_image", PlayerDetails.getImage_url());
        params.put("date_time", Util.getTimeStamp());

        Log.d("fdgfdgdfgd1",MyPreference.loadUserid(context));
        Log.d("fdgfdgdfgd2",message);
        Log.d("fdgfdgdfgd3",Util.Sender_ID);
        Log.d("fdgfdgdfgd4",MyPreference.loadUsername(context));
        Log.d("fdgfdgdfgd5",PlayerDetails.getImage_url());
        Log.d("fdgfdgdfgd6",Util.getTimeStamp());

        JSONParser parser = new JSONParser();
        JSONObject json = parser.makeHttpRequest(Api.URL_SEND_MESSAGE, "GET", params);
        //Log.e("response ", "" + json);
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.onPostExecute(jsonObject);
        // progressDialog.cancel();
        try {
            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
            } else {
                Toast.makeText(context, "response " + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            Log.d("draw root", "" + e);
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();

        }
    }
}
