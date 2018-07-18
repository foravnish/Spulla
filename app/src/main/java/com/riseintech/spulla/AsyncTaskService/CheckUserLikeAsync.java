package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 12/22/2016.
 */

public class CheckUserLikeAsync extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pDialog;
    Context context;
    String ctype, ctype_id;
    JSONParser jsonParser;
    HashMap<String, String> params = new HashMap<>();

    public CheckUserLikeAsync(Context context, String ctype, String ctype_id) {
        this.context = context;
        this.ctype = ctype;
        this.ctype_id = ctype_id;
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
          params.put("u_id", MyPreference.loadUserid(context));
        params.put("type_id", ctype_id);
        params.put("type", ctype);
        JSONObject json = jsonParser.makeHttpRequest(Api.Checkuser_like_Url, "GET", params);
        if (json != null) {
            //Log.e("json response", ": " + json);
            return json;
        }
        return null;
    }

    protected void onPostExecute(JSONObject json) {
        Util.Set_Total_Like=1;
        try {
            if (json != null && json.getString("status").equals("1")) {
                if (!json.getString("like_status").equalsIgnoreCase("null")) {
                    Util.User_Like = Integer.valueOf(json.getString("like_status"));
                    Util.Total_Like = Integer.valueOf(json.getString("like_facts"));
                }
            } else {
                Toast.makeText(context, "dnt get status", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
