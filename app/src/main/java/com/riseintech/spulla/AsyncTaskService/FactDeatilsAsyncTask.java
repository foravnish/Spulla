package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.FactsDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 10/12/2016.
 */

public class FactDeatilsAsyncTask extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "msg";
    String fact_id;
    HashMap<String, String> params = new HashMap<>();
    Context context;

    public FactDeatilsAsyncTask(Context context, String id) {
        this.fact_id = id;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.show();
        params.put("id", fact_id);

    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            JSONObject json = jsonParser.makeHttpRequest(
                    Api.FACT_DETAILS, "GET", params);
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
                String url = "";
                ArrayList<HashMap<String, String>> commtList = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getString("status").equalsIgnoreCase("1")) {
                        JSONArray cmmt_array = json.getJSONArray("comments");
                        if (cmmt_array != null && cmmt_array.length() >= 0) {
                            for (int k = 0; k < cmmt_array.length(); k++) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("title", cmmt_array.getJSONObject(k).getString("title"));
                                map.put("comments", cmmt_array.getJSONObject(k).getString("comments"));
                                map.put("date_time", cmmt_array.getJSONObject(k).getString("date_time"));
                                map.put("img", cmmt_array.getJSONObject(k).getString("img"));
                                commtList.add(map);

                            }
                        }


                        url = jsonArray.getJSONObject(i).getString("img");
                        FactsDetails.setGossipTopic(jsonArray.getJSONObject(i).getString("topic"));
                        FactsDetails.setGossipDesc(jsonArray.getJSONObject(i).getString("description"));
                        FactsDetails.setGossipTime(jsonArray.getJSONObject(i).getString("date_time"));


                    } else {
                        Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

