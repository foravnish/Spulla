package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 11/17/2016.
 */

public class Customer_supportAsync extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    String type_complaint, complaint_details;
    HashMap<String, String> params = new HashMap<>();
    Context context;

    public Customer_supportAsync(Context context, String type_complaint, String complaint_details) {
        this.type_complaint = type_complaint;
        this.complaint_details = complaint_details;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        // pDialog.setCancelable(false);
       // pDialog.show();
        params.put("p_id", MyPreference.loadUserid(context));
        params.put("type_complaint", type_complaint.toLowerCase().toString());
        params.put("complaint_details", complaint_details);
        //Log.e("params0", ": " + params);


    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            JSONObject json = jsonParser.makeHttpRequest(
                    Api.Customer_Support, "GET", params);
            //Log.d("JSON ", "------------" + json);
            if (json != null) {
               // Log.d("JSON result", json.toString());

                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(JSONObject json) {
        if (json != null) {
            try {
                if (json.getString("status").equalsIgnoreCase("1")) {
                    Toast.makeText(context, "Successfully " + json.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "" + json.getString("msg"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, "Some Problem in Add Tournament", Toast.LENGTH_SHORT).show();
        }

    }
}
