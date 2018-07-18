package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 11/5/2016.
 */

public class GetPlayerDetailsAsync extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pDialog;
    Context context;
    String otp;
    JSONParser jsonParser;
    HashMap<String, String> params = new HashMap<>();

    public GetPlayerDetailsAsync(Context context) {
        this.context = context;
        jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading  ....");
        pDialog.show();

    }

    protected JSONObject doInBackground(String... args) {
        JSONObject json;
        params.put("user_id", MyPreference.loadUserid(context));
        json = jsonParser.makeHttpRequest(Api.Get_Player_Details, "GET", params);
        if (json != null) {
            //Log.e("json response", ": " + json);
            return json;
        } else {
            pDialog.cancel();
            json = new JSONObject();
            return json;
        }
    }

    protected void onPostExecute(JSONObject json) {
        Util.PageRefreshDismis = 1;
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }

        try {
            if (json.getString("status").equals("1")) {
                String name = json.optString("name");
                String playas = json.optString("playas");
                String sports1 = json.optString("sports1");
                String sports2 = json.optString("sports2");
                String sports3 = json.getString("sports3");
                String sports_status1 = json.optString("sports1_status");
                String sports_status2 = json.optString("sports2_status");
                String sports_status3 = json.optString("sports3_status");


                String mobile = json.optString("mobile");
                String country = json.optString("country");
                String city = json.optString("city");
                String state = json.optString("state");
                String profession = json.optString("profession");

                String company = json.optString("company");
                String designation = json.optString("designation");
                String schooling = json.optString("schooling");
                String college = json.optString("college");
                String masters = json.optString("masters");
                String clubmember1 = json.optString("clubmember1");
                String clubmember2 = json.optString("clubmember2");
                String dob = json.getString("dob");
                String gender = json.optString("gender");
                String image_url = json.optString("images");

                if (!image_url.isEmpty()) {
                    PlayerDetails.setImage_url(image_url);
                    MyPreference.saveUserPicUrl(context, image_url);
                    Util.LoadPro_Image = 1;

                }
                MyPreference.saveUsername(context, name);
                PlayerDetails.setName(name);
                PlayerDetails.setAge(dob);
                PlayerDetails.setGender(gender);
                PlayerDetails.setCountry(country);
                PlayerDetails.setState(state);
                PlayerDetails.setCity(city);

                PlayerDetails.setClub_member(clubmember1);
                PlayerDetails.setClub_member1(clubmember2);
                PlayerDetails.setSports_Interest1(sports1);
                PlayerDetails.setSports_Interest2(sports2);
                PlayerDetails.setSports_Interest3(sports3);
                PlayerDetails.setSports_Interest_status1(sports_status1);
                PlayerDetails.setSports_Interest_status2(sports_status2);
                PlayerDetails.setSports_Interest_status3(sports_status3);


                PlayerDetails.setSprs(playas);
                PlayerDetails.setPlay_As(playas);

                PlayerDetails.setCollege(college);
                PlayerDetails.setSchooling(schooling);
                PlayerDetails.setCompany(company);
                PlayerDetails.setDesignation(designation);
                PlayerDetails.setMasters(masters);
                PlayerDetails.setProfession(profession);
                if (!json.optString("lat").equalsIgnoreCase("null")
                        || !json.optString("long").equalsIgnoreCase("null")) {
                    PlayerDetails.setLat(json.optString("lat"));
                    PlayerDetails.setLang(json.optString("long"));

                } else {
                    PlayerDetails.setLat("0");
                    PlayerDetails.setLang("0");
                }
                PlayerDetails.setAddress(Util.getAddress(context,
                        json.getString("lat"), json.getString("long")));

                //pDialog.cancel();
                // Toast.makeText(context, "lat: " + PlayerDetails.getLat() + ", lang: " + PlayerDetails.getLang(), Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

