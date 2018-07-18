package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 10/28/2016.
 */

public class SearchCoachAsyncTask extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    public static JSONArray coachArray;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "msg";
    String gender, location, sports_cate, age_group_min, age_group_max;
    HashMap<String, String> params = new HashMap<>();
    Context context;

    public SearchCoachAsyncTask(Context context, String gender, String location, String sports_cate,
                                String age_group_min, String age_group_max) {
        this.gender = gender;
        this.location = location;
        this.sports_cate = sports_cate;
        this.age_group_min = age_group_min;
        this.age_group_max = age_group_max;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.show();
        params.put("gender", gender);
        params.put("location", location);
        params.put("sports_cate", sports_cate);
        params.put("age_group_min", age_group_min);
        params.put("age_group_max", age_group_max);



    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            JSONObject json = jsonParser.makeHttpRequest(
                    Api.Coach_Search, "GET", params);
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
        if (pDialog.isShowing())
            pDialog.dismiss();

        if (json != null) {
            try {
                if (json.getString("status").equalsIgnoreCase("1")) {
                    String url = "";
                    coachArray = json.getJSONArray("items");
                    Toast.makeText(context, "coach array: " + coachArray, Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
