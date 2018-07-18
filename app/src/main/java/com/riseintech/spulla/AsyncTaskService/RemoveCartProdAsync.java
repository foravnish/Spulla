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
 * Created by user on 1/23/2017.
 */

public class RemoveCartProdAsync extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pDialog;
    Context context;
    JSONParser jsonParser;
    HashMap<String, String> params = new HashMap<>();
    String item_id;

    public RemoveCartProdAsync(Context context, String item_id) {
        this.context = context;
        this.item_id = item_id;
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
        params.put("user_id", MyPreference.loadUserid(context));
        params.put("item_id", item_id);

        JSONObject json = jsonParser.makeHttpRequest(Api.Remove_Pdt_Url, "GET", params);
        if (json != null) {
            //Log.e("json response", ": " + json);
            return json;
        }
        return null;
    }

    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject != null && jsonObject.getString("status").equals("1")) {

            } else {
                Toast.makeText(context, "Otp not valid", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
