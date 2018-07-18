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
 * Created by user on 11/8/2016.
 */

public class AddCommentAsync extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pDialog;
    Context context;
    String ctype, comments, ctype_id;
    JSONParser jsonParser;
    HashMap<String, String> params = new HashMap<>();


    public AddCommentAsync(Context context, String ctype, String ctype_id,
                           String comments) {
        this.context = context;
        this.ctype = ctype;
        this.ctype_id = ctype_id;
        this.comments = comments;
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
        params.put("title", MyPreference.loadUsername(context));
        params.put("comments", comments);
        params.put("user_type", ctype);
        params.put("cate_id", ctype_id);
        JSONObject json = jsonParser.makeHttpRequest(Api.Add_Comments, "GET", params);
        if (json != null) {
            //Log.e("json response", ": " + json);
            return json;
        }
        return null;
    }

    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getString("status").equals("1")) {
                Util.Get_Comments = 1;
            } else {
                Toast.makeText(context, "Comment not send", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


}

