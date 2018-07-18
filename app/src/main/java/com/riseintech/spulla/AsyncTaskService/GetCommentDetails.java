package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.riseintech.spulla.adapter.GossipCommentAdapter;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/8/2016.
 */

public class GetCommentDetails extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pDialog;
    Context context;
    String ctype, ctype_id;
    JSONParser jsonParser;
    HashMap<String, String> params = new HashMap<>();
    ArrayList<HashMap<String, String>> hashMaps;
    //time_format 2016-10-24 04:39:18
    ListView listView;

    public GetCommentDetails(Context context, String ctype, String ctype_id,
                             ArrayList<HashMap<String, String>> hashMaps, ListView listView) {
        this.context = context;
        this.ctype = ctype;
        this.ctype_id = ctype_id;
        this.listView = listView;
        this.hashMaps = hashMaps;
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
        params.put("comments", "");
        params.put("cate_id", ctype_id);
        params.put("user_type", ctype);
        JSONObject json = jsonParser.makeHttpRequest(Api.Get_Comments, "GET", params);
        if (json != null) {
            //Log.e("json response", ": " + json);
            return json;
        }
        return null;
    }

    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getString("status").equals("1")) {
                //Log.e("current time", ": " + Util.currentDateTime());
                JSONArray jsonArray = json.getJSONArray("items");
                hashMaps.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_title", jsonArray.getJSONObject(i).getString("title"));
                    map.put("comment", jsonArray.getJSONObject(i).getString("comments"));
                    map.put("date_time", jsonArray.getJSONObject(i).getString("date_time"));
                    map.put("cimg", jsonArray.getJSONObject(i).getString("img"));
                    hashMaps.add(map);
                }
                GossipCommentAdapter gossipCommentAdapter = new GossipCommentAdapter(context, hashMaps, true);
                listView.setAdapter(gossipCommentAdapter);
                gossipCommentAdapter.notifyDataSetChanged();


            } else {
                Toast.makeText(context, "Comments list not get", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


}
