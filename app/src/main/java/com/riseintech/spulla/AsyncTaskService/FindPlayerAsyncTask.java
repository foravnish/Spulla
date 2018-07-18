package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.riseintech.spulla.adapter.FindPlayerAdapter;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 10/29/2016.
 */

 public class FindPlayerAsyncTask extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    public static JSONArray coachArray;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "msg";
    String gender, location, sports_cate, age_group_min, age_group_max, playas, search_player;
    HashMap<String, String> params = new HashMap<>();
    Context context;
    ViewPager findplaer;
    String seeAll;
    String reqNo;
    String currentPosition;

    public FindPlayerAsyncTask(Context context, String playas, String gender, String location, String sports_cate,
                               String age_group_min, String age_group_max, ViewPager findplaer,
                               String search_player, String seeAll,String reqNo,String currentPosition) {
        this.gender = gender;
        this.location = location;
        this.sports_cate = sports_cate;
        this.age_group_min = age_group_min;
        this.age_group_max = age_group_max;
        this.context = context;
        this.findplaer = findplaer;
        this.playas = playas;
        this.search_player = search_player;
        this.seeAll = seeAll;
        this.reqNo = reqNo;
        this.currentPosition = currentPosition;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.show();
        params.put("playas", playas);
        params.put("gender", gender);
        // params.put("location", location);
        params.put("latitude", "" + PlayerDetails.current_lat);
        params.put("longitute", "" + PlayerDetails.current_long);
        params.put("sports_cate", sports_cate);
        params.put("age_group_min", age_group_min);
        params.put("age_group_max", age_group_max);
        params.put("search_player", search_player);
        params.put("see_all", seeAll);
        params.put("id", MyPreference.loadUserid(context));

        Log.d("dfgdfghbfdhgfghf",playas);
        Log.d("dfgdfghbfdhgfghf",gender);
        Log.d("dfgdfghbfdhgfghf", String.valueOf(PlayerDetails.current_lat));
        Log.d("dfgdfghbfdhgfghf", String.valueOf(PlayerDetails.current_long));
        Log.d("dfgdfghbfdhgfghf",sports_cate);
        Log.d("dfgdfghbfdhgfghf",age_group_min);
        Log.d("dfgdfghbfdhgfghf",age_group_max);
        Log.d("dfgdfghbfdhgfghf",search_player);
        Log.d("dfgdfghbfdhgfghf",seeAll);
        Log.d("dfgdfghbfdhgfghf",MyPreference.loadUserid(context));
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {
            JSONObject json = makeHttpRequest(context,
                    Api.Player_Search, "GET", params);
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
        if (pDialog.isShowing())
            pDialog.dismiss();

        if (json != null) {
            try {
                if (json.getString("status").equalsIgnoreCase("1")) {
                    String url = "";
                    ArrayList<HashMap<String, String>> playerItems = new ArrayList<HashMap<String, String>>();
                    coachArray = json.getJSONArray("items");
                    //Toast.makeText(context, "player array: " + coachArray, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < coachArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        try {
                            json = coachArray.getJSONObject(i);
                            String id = json.getString("id");
                            String name = json.getString("name");


                            hashMap.put("id", id);
                            hashMap.put("name", name);
                            hashMap.put("images", json.getString("image"));
                            hashMap.put("request_approval", json.getString("request_approval"));

               /* hashMap.put("age", json.getString("dob"));
                hashMap.put("gender", json.getString("gender"));
                hashMap.put("category", json.getString("playas"));
                hashMap.put("school", json.getString("schooling"));
                hashMap.put("college", json.getString("college"));
                hashMap.put("workat", json.getString("company"));
           */
                            //Log.e("main", "name id is " + id + "]" + " name is:" + name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        playerItems.add(hashMap);
                    }
                    //set facts adapter
                   // findplaer.getAdapter().notifyDataSetChanged();
                    findplaer.setAdapter(new FindPlayerAdapter(context, playerItems,reqNo));
                    findplaer.getAdapter().notifyDataSetChanged();
                    findplaer.setCurrentItem(Integer.parseInt(currentPosition));

                } else {
                    Toast.makeText(context, "Information not available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    ////////////////////////


    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;

    public JSONObject makeHttpRequest(Context context, String url, String method,
                                      HashMap<String, String> params) {

        sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("JSON Parser", "Error in add param  " + e.toString());

            }
            i++;
        }

        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);
                Log.e("complete url", ": " + url + sbParams.toString());

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("JSON Parser", "Error in make connection  " + e.toString());
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();

            }
        } else if (method.equals("GET")) {
            // request method is GET

            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
                Log.e("complete url", ": " + url);
            }

            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(false);

                conn.setRequestMethod("GET");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setConnectTimeout(15000);

                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("JSON Parser", "Error in make connection  " + e.toString());
                //Toast.makeText(context,"Check Internet Connection",Toast.LENGTH_LONG).show();

            }

        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON Parser", "Error in getting stream " + e.toString());
            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();

        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            Toast.makeText(context, "Problem in fetching data", Toast.LENGTH_LONG).show();

        }

        // return JSON Object
        return jObj;
    }
    ///////////////////////////
}
