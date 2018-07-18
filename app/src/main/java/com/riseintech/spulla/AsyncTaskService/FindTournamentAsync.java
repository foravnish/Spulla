package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.riseintech.spulla.adapter.TournamentAdapter;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.PlayerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/9/2016.
 */

public class FindTournamentAsync extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    public static JSONArray tournamnetInfo;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "msg";
    String gender, location, sports_cate, age_group_min, age_group_max, playas;
    HashMap<String, String> params = new HashMap<>();
    Context context;
    ViewPager findtournament;
    String seeAll;

    public FindTournamentAsync(Context context, String playas, String gender, String location, String sports_cate,
                               String age_group_min, String age_group_max, ViewPager findtournament, String seeAll) {
        this.gender = gender;
        this.location = location;
        this.sports_cate = sports_cate;
        this.age_group_min = age_group_min;
        this.age_group_max = age_group_max;
        this.context = context;
        this.findtournament = findtournament;
        this.playas = playas;
        this.seeAll = seeAll;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        // pDialog.setMessage("Progress..");
        pDialog.setCancelable(false);
        pDialog.show();
        params.put("gender", gender);
        params.put("sports_cate", sports_cate);
        params.put("game_type", playas);
        params.put("latitude", "" + PlayerDetails.current_lat);
        params.put("longitute", "" + PlayerDetails.current_long);
        params.put("age_group_min", age_group_min);
        params.put("age_group_max", age_group_max);
        params.put("see_all", seeAll);

    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            JSONObject json = jsonParser.makeHttpRequest(
                    Api.Tournament_Search, "GET", params);
            //Log.d("FindTouAsync JSON ", "------------" + json);
            if (json != null) {
                //Log.d("FindTouAsync result", json.toString());

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
                    // Toast.makeText(context, "Information " + json, Toast.LENGTH_SHORT).show();
                    ArrayList<HashMap<String, String>> playerItems = new ArrayList<>();
                    tournamnetInfo = json.optJSONArray("tournament");
                    //Toast.makeText(context, "tournamnetInfo array: " + tournamnetInfo, Toast.LENGTH_SHORT).show();
                    if (tournamnetInfo != null) {
                        for (int i = 0; i < tournamnetInfo.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            try {
                                json = tournamnetInfo.getJSONObject(i);
                                String id = json.optString("id");
                                String name = json.optString("name");


                                hashMap.put("id", id);
                                hashMap.put("Stadium", name);
                                hashMap.put("Schedule", json.optString("Schedule"));
                                hashMap.put("t_name", json.optString("name"));
                                hashMap.put("t_icon", json.optString("tour_pic"));
                                hashMap.put("category", json.optString("category"));

                                //Log.e("main", "name id is " + id + "]" + " name is:" + name);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Some problem exist in fetching data", Toast.LENGTH_SHORT).show();
                            }
                            playerItems.add(hashMap);
                        }
                        //set facts adapter
                        findtournament.setAdapter(new TournamentAdapter(context, playerItems));
                        findtournament.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(context, "Information not available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Some problem exist in fetching data", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(context, "Some problem exist in fetching data", Toast.LENGTH_SHORT).show();
        }

    }
}
