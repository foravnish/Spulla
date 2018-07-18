package com.riseintech.spulla;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.riseintech.spulla.AsyncTaskService.FindPlayerAsyncTask;
import com.riseintech.spulla.AsyncTaskService.FindTournamentAsync;
import com.riseintech.spulla.AsyncTaskService.GetPlayerDetailsAsync;
import com.riseintech.spulla.adapter.FactsAdapter;
import com.riseintech.spulla.adapter.FindPlayerAdapter;
import com.riseintech.spulla.adapter.GossipAdapterdata;
import com.riseintech.spulla.adapter.ShoppingAdapter;
import com.riseintech.spulla.adapter.SportCatAdapter;
import com.riseintech.spulla.adapter.TournamentAdapter;
import com.riseintech.spulla.adapter.VideoAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.gcm.GCMRegistrationIntentService;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.CircularViewPagerHandler;
import com.riseintech.spulla.utils.MultiSpinner;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    // SwipeRefreshLayout mSwipeRefreshLayout;
    private static String numRequest = "0";
    private static String idcat;
    private static String cat_type;
    public static ArrayList<HashMap<String, String>> sFactsCategory;
    public static ArrayList<HashMap<String, String>> sFactsCategory2;
    private static ArrayList<HashMap<String, String>> sFindSportCategory;
    ArrayList<String> minAgeList;
    ArrayList<String> maxAgeList;
    ArrayList<String> sports_list;
    ArrayList<String> sports_list2;
    int selminage;
    ImageView livemenu, gossipmenu, factsmenu, coachingmenu, findmenu, shopingmenu, tournamentmenu;
    LinearLayout challengelyt, zonelyt, turnamentlyt;
    TextView sTxtRegister, sTxtLogin;
    private Toolbar toolbar;
    ArrayList<HashMap<String, String>> scoretList;
    String loginURL = "http://spulla.com/admin/spulla_api/jsonhome.php";
    ViewPager score_viewPager;
    public ViewPager gossip_viewPager;
    ViewPager facts_viewPager;
    ViewPager video_viewPager;
    public static  ViewPager player_viewPager;
    ViewPager shopping_viewPager;
    ViewPager tournament_viewPager;
    RecyclerView shoping_list;
    ArrayList<HashMap<String, String>> gossip_list;
    ArrayList<HashMap<String, String>> spullaFacts;
    ArrayList<HashMap<String, String>> spullaTournament;
    ArrayList<HashMap<String, String>> spullaVideo;
    ArrayList<HashMap<String, String>> spullaPlayer;
    ArrayList<HashMap<String, String>> shop_cat_list;
    ArrayList<HashMap<String, String>> hotdealList;
    ArrayList<String> tage_grp_list;
    ArrayList<String> coh_ag_list;

    public  static String gender ;
    public  static String minage ;
    public  static String maxage ;
    public  static String sports ;
    public  static String play_as;
    public  static String location;
    public  static EditText search_player;
    public  static JSONArray player;

    ImageView live_left, live_right, gossip_left, gossip_right, fact_left, facts_right,
            coach_left, coach_right, fp_left, fp_right, shop_left, shop_right, tourn_left, tourn_right;
    boolean isdata;
    ImageView gossip_info, facts_info, coach_info, fp_info, shop_info, tour_info;

    String data = "";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton btn_drawer = (ImageButton) findViewById(R.id.buttonimage);
        /////////show info image intialize
        gossip_info = (ImageView) findViewById(R.id.gossip_info);
        facts_info = (ImageView) findViewById(R.id.facts_info);
        coach_info = (ImageView) findViewById(R.id.coach_info);
        fp_info = (ImageView) findViewById(R.id.fp_info);
        shop_info = (ImageView) findViewById(R.id.shop_info);
        tour_info = (ImageView) findViewById(R.id.tour_info);


        sTxtRegister = (TextView) findViewById(R.id.rForm);
        gossip_viewPager = (ViewPager) findViewById(R.id.gossip_pager);
        facts_viewPager = (ViewPager) findViewById(R.id.facts);
        tournament_viewPager = (ViewPager) findViewById(R.id.tournamentpager);
        player_viewPager = (ViewPager) findViewById(R.id.findplayer);
        video_viewPager = (ViewPager) findViewById(R.id.coaching);
        shopping_viewPager = (ViewPager) findViewById(R.id.shopping);


        gossip_info.setOnClickListener(this);
        facts_info.setOnClickListener(this);
        coach_info.setOnClickListener(this);
        fp_info.setOnClickListener(this);
        shop_info.setOnClickListener(this);
        tour_info.setOnClickListener(this);
      /*  mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ed037c"),
                Color.parseColor("#c40053"),
                Color.parseColor("#ffffff"),
                Color.parseColor("#51af50"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (PlayerDetails.current_lat == 0 && PlayerDetails.current_long == 0) {
                    Util.getCurrentLatLong(MainActivity.this);
                }
                if (InternetStatus.isConnectingToInternet(MainActivity.this)) {
                    getData();
                }}

        });*/
        if (PlayerDetails.current_lat == 0 && PlayerDetails.current_long == 0) {
            Util.getCurrentLatLong(MainActivity.this);
        }
        if (InternetStatus.isConnectingToInternet(MainActivity.this)) {
            //  getData();
            new GetAllDataAsync().execute();



        }
        ///////////////////////////////////////////
/////////intialize  image of swipe left or right

        gossip_left = (ImageView) findViewById(R.id.gossip_left1);
        gossip_right = (ImageView) findViewById(R.id.gossip_right1);

        fact_left = (ImageView) findViewById(R.id.fact_left);
        facts_right = (ImageView) findViewById(R.id.facts_right);

        coach_left = (ImageView) findViewById(R.id.coach_left1);
        coach_right = (ImageView) findViewById(R.id.coach_right1);

        fp_left = (ImageView) findViewById(R.id.fp_left);
        fp_right = (ImageView) findViewById(R.id.fp_right);

        shop_left = (ImageView) findViewById(R.id.shop_left);
        shop_right = (ImageView) findViewById(R.id.shop_right);

        tourn_left = (ImageView) findViewById(R.id.tourn_left);
        tourn_right = (ImageView) findViewById(R.id.tourn_right);

        // find menu image

        gossipmenu = (ImageView) findViewById(R.id.gossip_menu);
        factsmenu = (ImageView) findViewById(R.id.facts_menu);

        coachingmenu = (ImageView) findViewById(R.id.coaching_menu);
        findmenu = (ImageView) findViewById(R.id.find_menu);

        shopingmenu = (ImageView) findViewById(R.id.shopping_menu);
        tournamentmenu = (ImageView) findViewById(R.id.tournament_menu);

        gossipmenu.setOnClickListener(this);

        factsmenu.setOnClickListener(this);
        coachingmenu.setOnClickListener(this);

        findmenu.setOnClickListener(this);
        shopingmenu.setOnClickListener(this);
        tournamentmenu.setOnClickListener(this);


       /* live_left.setOnClickListener(this);
        live_right.setOnClickListener(this);*/

        gossip_left.setOnClickListener(this);
        gossip_right.setOnClickListener(this);

        fact_left.setOnClickListener(this);
        facts_right.setOnClickListener(this);

        coach_left.setOnClickListener(this);
        coach_right.setOnClickListener(this);

        fp_left.setOnClickListener(this);
        fp_right.setOnClickListener(this);

        shop_left.setOnClickListener(this);
        shop_right.setOnClickListener(this);

        tourn_left.setOnClickListener(this);
        tourn_right.setOnClickListener(this);


        //////////////////
        clickImage(btn_drawer);


        // setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);



        //Initializing our broadcast receiver
        setmRegistrationBroadcastReceiver();

        PlayerDetails.setRequestNo(numRequest);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        shoping_list = (RecyclerView) findViewById(R.id.shoping_list);
        shoping_list.setLayoutManager(layoutManager);

/////////////////enable circular swiping at pager
        gossip_viewPager.addOnPageChangeListener(new CircularViewPagerHandler(gossip_viewPager,MainActivity.this));
        facts_viewPager.addOnPageChangeListener(new CircularViewPagerHandler(facts_viewPager,MainActivity.this));
        tournament_viewPager.addOnPageChangeListener(new CircularViewPagerHandler(tournament_viewPager,MainActivity.this));
        player_viewPager.addOnPageChangeListener(new CircularViewPagerHandler(player_viewPager,MainActivity.this));
        video_viewPager.addOnPageChangeListener(new CircularViewPagerHandler(video_viewPager,MainActivity.this));
        shopping_viewPager.addOnPageChangeListener(new CircularViewPagerHandler(shopping_viewPager,MainActivity.this));

///////////////////////////////
        spullaTournament = new ArrayList<HashMap<String, String>>();
        spullaVideo = new ArrayList<HashMap<String, String>>();
        spullaPlayer = new ArrayList<HashMap<String, String>>();
        shop_cat_list = new ArrayList<HashMap<String, String>>();
        sFactsCategory = new ArrayList<HashMap<String, String>>();
        sFactsCategory2 = new ArrayList<HashMap<String, String>>();
        minAgeList = new ArrayList<>();
        maxAgeList = new ArrayList<>();
        sports_list = new ArrayList<>();
        sports_list2 = new ArrayList<>();
        gossip_list = new ArrayList<HashMap<String, String>>();
        spullaFacts = new ArrayList<HashMap<String, String>>();
        hotdealList = new ArrayList<>();
        tage_grp_list = new ArrayList<>();
        coh_ag_list = new ArrayList<>();


        if (isdata == true) {
            player_viewPager.setAdapter(new FindPlayerAdapter(MainActivity.this, spullaPlayer,numRequest));
            // Log.e("get intent data>>", spullaPlayer.get(2).get("id"));
        }


        sTxtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent regisIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(regisIntent);
            }
        });


    }


    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        //Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));

        if (InternetStatus.isConnectingToInternet(MainActivity.this)) {
            new GetFrequest().execute();

        }
        }
    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        //Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }





    class GetFrequest extends AsyncTask<String, String, JSONObject> {
        ProgressDialog loading;
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();
        @Override
        protected void onPreExecute() {
            //loading = ProgressDialog.show(MainActivity.this, "Loading Data", "Please wait...", false, false);

            params.put("user_id", MyPreference.loadUserid(MainActivity.this));

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.GET_FREQUEST, "POST", params);
                //Log.d("JSON ", "------------" + json);
                if (json != null) {
                    Log.w("JSON result", json.toString());

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
                    numRequest = json.getString("frequest");
                    PlayerDetails.setRequestNo(json.getString("frequest"));

                    //Toast.makeText(MainActivity.this,"request is :"+numRequest,Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }
    }


    class GetCatItems extends AsyncTask<String, String, JSONObject> {
        ProgressDialog loading;
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(MainActivity.this, "Loading Data", "Please wait...", false, false);

            params.put("cat_id", idcat);
            params.put("cat_type", cat_type);

            Log.d("fdgfdhfghfgh",idcat);
            Log.d("fdgfdhfghfgh",cat_type);
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.Select_Cat, "GET", params);
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
            String success = "";

            if (loading.isShowing())
                loading.dismiss();

            if (json != null) {
                try {
                    //success = json.getString("status");
                    if (json.getString("status").equalsIgnoreCase("1")) {
                        // Toast.makeText(MainActivity.this, "Information " + json, Toast.LENGTH_SHORT).show();

                        JSONArray sportcategory = json.getJSONArray("item");
                        Log.d("gfdggdfghddfhdf_filter",sportcategory.toString());
                        if (cat_type.equalsIgnoreCase("gossip")) {
                            parseGossipData(sportcategory);

                        } else if (cat_type.equalsIgnoreCase("facts")) {
                            parseFactsData(sportcategory);
                        }
                        else if (cat_type.equalsIgnoreCase("facts2")) {
                            parseFactsData(sportcategory);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Information not available", Toast.LENGTH_SHORT).show();

                    }// Toast.makeText(LoginActivity.this, "user name " + userName +" id is " +userId, Toast.LENGTH_SHORT).show();
                    // Log.d("message login>>>>", success);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //This method will parse json data
    private void parseGossipData(JSONArray array) {
        if (array != null) {
            gossip_list.clear();
            ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists = new
                    ArrayList<ArrayList<HashMap<String, String>>>();
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    String id = json.optString("p.id");
                    String topic = json.optString("p.topic");
                    String type = json.optString("type");

                    //Log.e("id is= " + id + " topic is = " + topic, "");
                    hashMap.put("id", id);
                    hashMap.put("topic", topic);
                    hashMap.put("topic", topic);
                    hashMap.put("subtitle", json.optString("p.subtitle"));
                    hashMap.put("description", json.optString("p.description"));
                    hashMap.put("img", json.optString("p.img"));
                    hashMap.put("icon", json.optString("icon"));
                    hashMap.put("icon_name", json.optString("icon_name"));
                    hashMap.put("date_time", json.optString("p.date_time"));
                    hashMap.put("types", json.optString("type"));
                    Log.d("gbfddhbfghjfgjn",json.optString("p.img"));
                    Log.d("gbfddhbfghjfgjn2",json.optString("type"));

                   JSONArray cmntArray1 = json.optJSONArray("c.comments");
                    ArrayList<HashMap<String, String>> cmmtList = new ArrayList<>();

                    for (int k = 0; k < cmntArray1.length(); k++) {
                        HashMap<String, String> cmmtMap = new HashMap<>();
                        cmmtMap.put("comment", cmntArray1.getJSONObject(k).optString("comment"));
                        cmmtMap.put("user_title", cmntArray1.getJSONObject(k).optString("user_title"));
                        cmmtMap.put("cimg", cmntArray1.getJSONObject(k).optString("cimg"));
                        cmmtMap.put("date_time", cmntArray1.getJSONObject(k).optString("date_time"));
                        cmmtList.add(cmmtMap);
                    }

                    total_cmmtLists.add(cmmtList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gossip_list.add(hashMap);
            }
            Collections.shuffle(gossip_list);
            //set gossip adapterspullaList
            gossip_viewPager.setAdapter(new GossipAdapterdata(MainActivity.this, gossip_list, total_cmmtLists));
            Log.d("dfgdfgddfhbgfddhdbfgd",gossip_list.toString());
            gossip_viewPager.getAdapter().notifyDataSetChanged();

        } else {
            Toast.makeText(MainActivity.this, "No Information available for Gossips", Toast.LENGTH_SHORT).show();
        }
    }

    ////////////function for parse sports type
    private void parseSportsType(JSONArray catarray) {
        if (catarray != null) {
            sports_list.add("See All");
            sFactsCategory.clear();
            for (int i = 0; i < catarray.length(); i++) {
                HashMap<String, String> sportMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = catarray.getJSONObject(i);
                    String id = json.getString("id");
                    String categoryname = json.getString("category_name");
                    sportMap.put("id", id);
                    sportMap.put("category_name", categoryname);
                    sports_list.add(categoryname);
                    //Log.e("id", id);
                    //Log.e("category_name", categoryname);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sFactsCategory.add(sportMap);
            }
        }

    }


    private void parseSportsType2(JSONArray catarray2) {
        if (catarray2 != null) {
            sports_list2.add("See All");
            sFactsCategory2.clear();
            for (int i = 0; i < catarray2.length(); i++) {
                HashMap<String, String> sportMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = catarray2.getJSONObject(i);
                    String id = json.getString("id");
                    String categoryname = json.getString("category_name");
                    sportMap.put("id", id);
                    sportMap.put("category_name", categoryname);
                    sports_list2.add(categoryname);
                    //Log.e("id", id);
                    //Log.e("category_name", categoryname);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sFactsCategory2.add(sportMap);
            }
        }

    }


    /////////////////////parse age group

    private void parseAgeGroup(JSONArray ageArray) {
        if (ageArray != null) {
            minAgeList.add("Any");
            maxAgeList.add("Any");
            for (int k = 0; k < ageArray.length(); k++) {
                try {
                    minAgeList.add(ageArray.getJSONObject(k).getString("min"));
                    maxAgeList.add(ageArray.getJSONObject(k).getString("max"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ///
    //This method will parse facts json data
    private void parseFactsData(JSONArray array) {
        if (array != null) {

            ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists = new
                    ArrayList<ArrayList<HashMap<String, String>>>();
            spullaFacts.clear();

            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    String id = json.optString("id");
                    String topic = json.optString("topic");

                    hashMap.put("id", id);
                    hashMap.put("topic", topic);
                    hashMap.put("subtitle", json.optString("subtitle"));
                    hashMap.put("description", json.optString("description"));
                    hashMap.put("img", json.optString("img"));
                    hashMap.put("icon", json.optString("icon"));
                    hashMap.put("icon_name", json.optString("icon_name"));
                    hashMap.put("date_time", json.optString("date_time"));
                    hashMap.put("types", json.optString("type"));

                    Log.d("gbfddhbfghjfgjn2",json.optString("type"));
                    //////getting comment arrays
                    JSONArray cmntArray1 = json.getJSONArray("c.comments");
                    ArrayList<HashMap<String, String>> cmmtList = new ArrayList<>();

                    for (int k = 0; k < cmntArray1.length(); k++) {
                        HashMap<String, String> cmmtMap = new HashMap<>();
                        cmmtMap.put("comment", cmntArray1.getJSONObject(k).optString("comment"));
                        cmmtMap.put("user_title", cmntArray1.getJSONObject(k).optString("user_title"));
                        cmmtMap.put("cimg", cmntArray1.getJSONObject(k).optString("cimg"));
                        cmmtMap.put("date_time", cmntArray1.getJSONObject(k).optString("date_time"));
                        cmmtList.add(cmmtMap);
                    }

                    total_cmmtLists.add(cmmtList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spullaFacts.add(hashMap);
            }
            Collections.shuffle(spullaFacts);
            //set facts adapter
            facts_viewPager.setAdapter(new FactsAdapter(MainActivity.this, spullaFacts, total_cmmtLists));
            facts_viewPager.getAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "No Information available for Facts", Toast.LENGTH_SHORT).show();

        }
    }


    //This method will parse tournament json data
    private void parseTournamentData(JSONArray array) {
        spullaTournament.clear();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    String id = json.getString("id");
                    String Stadium = json.optString("Stadium");
                    String Schedule = json.optString("Schedule");
                    hashMap.put("id", id);
                    hashMap.put("Stadium", Stadium);
                    hashMap.put("Schedule", Schedule);
                    hashMap.put("t_name", json.optString("name"));
                    hashMap.put("t_icon", json.optString("tour_pic"));
                    hashMap.put("category", json.optString("category"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spullaTournament.add(hashMap);
            }
            Collections.shuffle(spullaTournament);

            //set facts adapter
            tournament_viewPager.setAdapter(new TournamentAdapter(MainActivity.this, spullaTournament));
            tournament_viewPager.getAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "No Information available for Tournaments", Toast.LENGTH_SHORT).show();

        }
    }

    //This method will parse player json data
    private void parsePlayerData(JSONArray array) {
        if (array != null) {
            spullaPlayer.clear();
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    String id = json.optString("id");
                    String name = json.optString("name");
                    String images = json.optString("images");
                    String flag = json.optString("request_approval");
                    //String frequest = json.optString("frequest");
                    hashMap.put("id", id);
                    hashMap.put("name", name);
                    hashMap.put("images", images);
                    hashMap.put("request_approval", flag);
                    //hashMap.put("frequest", frequest);
                    spullaPlayer.add(hashMap);

                  /*  if (!json.getString("mobile").isEmpty()) {
                        Log.e("main", "name id is " + id + "]" + " name is:" + name);
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            //set facts adapter
            player_viewPager.setAdapter(new FindPlayerAdapter(MainActivity.this, spullaPlayer,numRequest));
            player_viewPager.getAdapter().notifyDataSetChanged();
            //Log.e("player size", ": " + spullaPlayer.size());
        } else {
            Toast.makeText(MainActivity.this, "No Information available for Players", Toast.LENGTH_SHORT).show();
        }
    }

    //This method will parse coaching json data
    private void parseCoachingData(JSONArray array) {
        if (array != null) {
            spullaVideo.clear();
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    String id = json.getString("id");
                    String video = json.getString("video").replaceAll("\\s", "");
                    String coach_icon = json.optString("coach_icon");
                    String coach_thumbnail = json.getString("thumbnail");
                    //type.replaceAll("\\s", "");

                    hashMap.put("id", id);
                    hashMap.put("video", video);
                    hashMap.put("coach_icon", coach_icon);
                    if (json.optString("icon_name").equalsIgnoreCase("null") || json.optString("icon_name").matches("")) {
                        hashMap.put("icon_name", "");
                    } else {
                        hashMap.put("icon_name", json.optString("icon_name"));
                    }
                    if (json.optString("coach_name").equalsIgnoreCase("null") || json.optString("coach_name").matches("")) {
                        hashMap.put("coach_name", getString(R.string.notavail));
                    } else {
                        hashMap.put("coach_name", json.optString("coach_name"));
                    }
                    hashMap.put("coach_thumbnail", coach_thumbnail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spullaVideo.add(hashMap);
            }
            //set facts adapter
            video_viewPager.setAdapter(new VideoAdapter(MainActivity.this, spullaVideo));
            video_viewPager.getAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "No Information available for Coaching", Toast.LENGTH_SHORT).show();

        }
    }

    //This method will parse shopping json data
    private void parseShoppingData(JSONArray array) {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                try {
                    JSONObject json = array.getJSONObject(i);
                    String id = json.getString("id");
                    String category_name = json.getString("category_name");
                    hashMap.put("id", id);
                    hashMap.put("category_name", category_name);
                    hashMap.put("icon", json.getString("icon"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                shop_cat_list.add(hashMap);
            }
        } else {
            Toast.makeText(MainActivity.this, "No Information available for Shoping", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
        // finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

////////////////////clcik events for info
            case R.id.coach_info:
                Util.showInfoAlertDialog(MainActivity.this, "Coach Info", getString(R.string.coach_info));
                break;
            case R.id.facts_info:
                Util.showInfoAlertDialog(MainActivity.this, "Fitness Info", getString(R.string.fact_info));
                break;


            case R.id.fp_info:
                Util.showInfoAlertDialog(MainActivity.this, "FindPlayer Info", getString(R.string.fp_info));
                break;
            case R.id.gossip_info:
                Util.showInfoAlertDialog(MainActivity.this, "Sportopedia Info", getString(R.string.gossip_info));
                break;

            case R.id.shop_info:
                Util.showInfoAlertDialog(MainActivity.this, "Sports Shop Info", getString(R.string.shop_info));
                break;


            case R.id.tour_info:
                Util.showInfoAlertDialog(MainActivity.this, "Tournaments Info", getString(R.string.tour_info));
                break;

            //////////////////////////////////

            case R.id.gossip_left1:
                if (gossip_list.size() > 0) {
                    handleSetPreviousItem(gossip_viewPager);
                }
                break;
            case R.id.gossip_right1:
                if (gossip_list.size() > 0) {
                    handleSetNextItem(gossip_viewPager);
                }
                break;


            case R.id.fact_left:
                if (spullaFacts.size() > 0) {
                    handleSetPreviousItem(facts_viewPager);
                }
                break;
            case R.id.facts_right:
                if (spullaFacts.size() > 0) {
                    handleSetNextItem(facts_viewPager);
                }
                break;


            case R.id.coach_right1:
                if (spullaVideo.size() > 0) {
                    handleSetPreviousItem(video_viewPager);
                }
                break;
            case R.id.coach_left1:
                if (spullaVideo.size() > 0) {
                    handleSetNextItem(video_viewPager);
                }
                break;


            case R.id.shop_left:
                if (hotdealList.size() > 0) {
                    handleSetPreviousItem(shopping_viewPager);
                }
                break;
            case R.id.shop_right:
                if (hotdealList.size() > 0) {
                    handleSetNextItem(shopping_viewPager);
                }
                break;


            case R.id.fp_left:
                if (spullaPlayer.size() > 0) {
                    handleSetPreviousItem(player_viewPager);
                }

                break;
            case R.id.fp_right:
                if (spullaPlayer.size() > 0) {
                    handleSetNextItem(player_viewPager);
                }
                break;


            case R.id.tourn_right:
                if (spullaTournament.size() > 0) {
                    handleSetNextItem(tournament_viewPager);
                }
                break;
            case R.id.tourn_left:
                if (spullaTournament.size() > 0) {
                    handleSetPreviousItem(tournament_viewPager);
                }
                break;


            case R.id.gossip_menu:
                openMenuDialog(MainActivity.this, "gossip", sFactsCategory);
                break;

            case R.id.facts_menu:

                openMenuDialog(MainActivity.this, "facts2", sFactsCategory2);

//                final Dialog dialog = new Dialog(MainActivity.this);
//
//                dialog.setContentView(R.layout.dialogbrand_layout);
//
//                Button button1=(Button)dialog.findViewById(R.id.cat1);
//                Button button2=(Button)dialog.findViewById(R.id.cat2);
//                button1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        openMenuDialog(MainActivity.this, "facts", sFactsCategory);
//                        dialog.dismiss();
//                    }
//                });
//                button2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        openMenuDialog(MainActivity.this, "facts2", sFactsCategory2);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();

                break;

            case R.id.coaching_menu:
                Util.isSportBgChn = true;
                //opencoachMenuDialog(MainActivity.this, sFactsCategory, minAgeList, maxAgeList, sports_list);
                // findAllDetails(MainActivity.this, "coaching", minAgeList, maxAgeList, sports_list);
                getAgeGrp(false, Api.Coach_AgeGrp);

                break;

            case R.id.find_menu:
                // openMenuDialog(MainActivity.this, "find", sFactsCategory);
                findAllDetails(MainActivity.this, "find player", minAgeList, maxAgeList, sports_list);

                break;

            case R.id.shopping_menu:
                openMenuDialog(MainActivity.this, "findhotdeals", shop_cat_list);
                break;

            case R.id.tournament_menu:
                //  openMenuDialog(MainActivity.this, "tournament", sFactsCategory);
                //
                getAgeGrp(true, Api.Tourn_Age_Group);
                break;

        }

    }


    private void openMenuDialog(final Context context, final String checktype,
                                final ArrayList<HashMap<String, String>> categoryItem) {
       /*
       /////////////show  static list  from string array
        ArrayAdapter<String> adapter;
        String[] mTestArray;

        mTestArray = context.getResources().getStringArray(R.array.choose_sports_gossip);

        adapter = new ArrayAdapter<String>(
                context.getApplicationContext(),
                android.R.layout.select_dialog_singlechoice,
                mTestArray);
        adapter.setDropDownViewResource(R.layout.listtextview);*/
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoglivescoremenu);
        dialog.setCancelable(true);
        Button submit = (Button) dialog.findViewById(R.id.disimiss);
        TextView see_all_txv = (TextView) dialog.findViewById(R.id.see_all_txv);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        see_all_txv.setVisibility(View.VISIBLE);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final ListView listView = (ListView) dialog.findViewById(R.id.choose_sportlv);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(new SportCatAdapter(context, categoryItem));
        if (checktype.contentEquals("facts")) {
            title.setText("Find Facts");
        } else if (checktype.contentEquals("gossip")) {
            title.setText("Find Gossips");
        } else {
            title.setText("Find HotDeals");

        }
        see_all_txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idcat = "all";
                cat_type = checktype;
                //Toast.makeText(context,"this is gossip "+idcat,Toast.LENGTH_SHORT).show();
                if (checktype.equalsIgnoreCase("findhotdeals")) {
                  //
                    getHotDeals();

                } else {
                    new GetCatItems().execute();

                }
                //getCategory(idcat);
                dialog.cancel();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (checktype.contentEquals("facts")) {
                                              dialog.cancel();
                                             // Toast.makeText(context, "this is facts", Toast.LENGTH_SHORT).show();
                                          } else if (checktype.contentEquals("gossip")) {
                                              //Toast.makeText(context, "this is gossip", Toast.LENGTH_SHORT).show();
                                              dialog.cancel();
                                          } else {
                                              //Toast.makeText(context, "this is gossip", Toast.LENGTH_SHORT).show();
                                              dialog.cancel();
                                          }
                                      }
                                  }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                idcat = categoryItem.get(i).get("id");
                cat_type = checktype;
                //Toast.makeText(context,"this is gossip "+idcat,Toast.LENGTH_SHORT).show();
                if (checktype.equalsIgnoreCase("findhotdeals")) {
                    Log.d("gdfhbdfgdhfghgfg","up");
                    searchHotDeal(categoryItem.get(i).get("id"), categoryItem.get(i).get("category_name"));
                } else {
                    Log.d("gdfhbdfgdhfghgfg","down");
                    new GetCatItems().execute();
                }
                //getCategory(idcat);
                dialog.cancel();

            }
        });
        dialog.show();
    }

    public void findAllDetails(final Context context, final String findType,
                               ArrayList<String> minAgeList,
                               ArrayList<String> maxAgeList, ArrayList<String> sports_list) {
        String playas[] = context.getResources().getStringArray(R.array.play_as_array);
        final List<String> playasList = new ArrayList<>();
        final ArrayList<String> sel_play_as_list = new ArrayList<>();
        for (int i = 0; i < playas.length; i++) {
            playasList.add(playas[i]);
        }
        ArrayAdapter<String> minageadapter, maxageAdapter, g_Adapter1, sel_sport;
        minageadapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                minAgeList);
        minageadapter.setDropDownViewResource(R.layout.listtextview);
        maxageAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                maxAgeList);
        maxageAdapter.setDropDownViewResource(R.layout.listtextview);
        g_Adapter1 = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                context.getResources().getStringArray(R.array.gender));
        g_Adapter1.setDropDownViewResource(R.layout.listtextview);
        sel_sport = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item, sports_list
        );
        sel_sport.setDropDownViewResource(R.layout.listtextview);
        ;
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogcoachmenu);
        dialog.setCancelable(true);
        Button fc_btn = (Button) dialog.findViewById(R.id.fc_btn);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        LinearLayout age_grp = (LinearLayout) dialog.findViewById(R.id.age_grp);
        LinearLayout min_grp_lin = (LinearLayout) dialog.findViewById(R.id.age_grp);
        TextView ma_title = (TextView) dialog.findViewById(R.id.ma_title);
        TextView see_all = (TextView) dialog.findViewById(R.id.see_all);
        final Spinner spinner_gender = (Spinner) dialog.findViewById(R.id.spinner_gender);
        final Spinner min_age_spn = (Spinner) dialog.findViewById(R.id.min_age_spn);
        LinearLayout sel_playas = (LinearLayout) dialog.findViewById(R.id.sel_playas);
        final Spinner max_age_spn = (Spinner) dialog.findViewById(R.id.max_age_spn);
        search_player= (EditText) dialog.findViewById(R.id.search_player);
        search_player.setVisibility(View.GONE);
        MultiSpinner multiSpinner = (MultiSpinner) dialog.findViewById(R.id.multi_spinner);
        MultiSpinner.MultiSpinnerListener multiSpinnerListener = new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

                for (int j = 0; j < selected.length; j++) {
                    if (selected[j] == true) {
                        sel_play_as_list.add("'" + playasList.get(j).toString() + "'");
                       // Log.e("its true", "at pos:  " + sel_play_as_list.toString());
                    } else {
                        //Log.e("its false", "at pos:  " + j);

                    }
                }

            }
        };
        if (findType.equalsIgnoreCase("find player")) {
            sel_playas.setVisibility(View.VISIBLE);
            title.setText("Find player");
            search_player.setVisibility(View.VISIBLE);
        } else if (findType.equalsIgnoreCase("coaching")) {
            sel_playas.setVisibility(View.GONE);
            age_grp.setVisibility(View.GONE);
            ma_title.setText("Select Age");
            title.setText("Find Coach");
        } else if (findType.equalsIgnoreCase("tournament")) {
            sel_playas.setVisibility(View.VISIBLE);
            age_grp.setVisibility(View.GONE);
            ma_title.setText("Select Age");
            title.setText("Find tournament");
        }
        multiSpinner.setItems(context, playasList, context.getString(R.string.for_all), multiSpinnerListener);
        final Spinner sport_sel = (Spinner) dialog.findViewById(R.id.sport_sel);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        spinner_gender.setAdapter(g_Adapter1);

        min_age_spn.setAdapter(minageadapter);
        max_age_spn.setAdapter(maxageAdapter);

        min_age_spn.getAdapter().getCount();
        min_age_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selminage = min_age_spn.getSelectedItemPosition();
                Log.e("min age position", "min age: " + selminage + ", length of min age " + min_age_spn.getCount());
                if (selminage == 0) {
                    max_age_spn.setSelection(0);
                } else if (selminage + 5 < min_age_spn.getCount()) {
                    max_age_spn.setSelection(selminage + 5);
                } else {
                    max_age_spn.setSelection(selminage);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sport_sel.setAdapter(sel_sport);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.isSportBgChn = false;
                dialog.cancel();

            }
        });




        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (InternetStatus.isConnectingToInternet(context)) {
                    if (findType.equalsIgnoreCase("coaching")) {
                        new SearchCoachAsyncTask(context, "", "",
                                "", "", "", "SeeAll").execute();
                    } else if (findType.equalsIgnoreCase("tournament")) {
                        new FindTournamentAsync(context, "",
                                "", "", "", "", "", tournament_viewPager, "SeeAll").execute();

                    } else if (findType.equalsIgnoreCase("find player")) {
//                        Util.setFindPlayerAsynccounting("1");
                        Util.FindPlayerAsynccountingVaue=1;
                        new FindPlayerAsyncTask(context, "", "", "",
                                "", "", "", player_viewPager,
                                search_player.getText().toString(), "SeeAll",numRequest,"0").execute();

                    }
                }

            }
        });
        fc_btn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Util.isSportBgChn = false;
                                          dialog.cancel();
                                          if (InternetStatus.isConnectingToInternet(context)) {
                                             gender = spinner_gender.getSelectedItem().toString();
                                               minage = min_age_spn.getSelectedItem().toString();
                                              maxage = max_age_spn.getSelectedItem().toString();
                                               sports = sport_sel.getSelectedItem().toString();
                                              sports = sports.replaceAll(" ", "");
                                              location= "" +
                                                      "Delhi";
                                              //Log.e("coach details", "g: " + gender + ",mina " + minage + ",max age" + maxage + ",sp " + sports);
                                              if (findType.equalsIgnoreCase("coaching")) {
                                                  if (!minage.equalsIgnoreCase("Any") && minage.contains("-")) {
                                                      minage = minage.replaceAll("\\(", "").replaceAll("\\)", "");
                                                      String[] splitage = minage.split("-");
                                                      //Log.e("min & max age", ": min age " + splitage[0] + ", max age =" + splitage[1]);
                                                      minage = splitage[0];
                                                      maxage = splitage[1];
                                                  }
                                                  new SearchCoachAsyncTask(context, gender, location,
                                                          sports, minage, maxage, "123").execute();
                                              } else if (findType.equalsIgnoreCase("find player")) {
                                                  play_as= sel_play_as_list.toString()
                                                          .replaceAll("\\[", "").replaceAll("\\]", "");
                                                  //Log.e("selecet play as", ": " + play_as);
                                                 /* String play_as = "";
                                                  if (sel_play_as_list.size() > 0) {
                                                      play_as = sel_play_as_list.get(0);
                                                  }*/
//                                                  Util.setFindPlayerAsynccounting("2");
                                                  Util.FindPlayerAsynccountingVaue=2;
                                                  new FindPlayerAsyncTask(context, play_as, gender, location,
                                                          sports, minage, maxage, player_viewPager,
                                                          search_player.getText().toString(), "123",numRequest,"0").execute();
                                              } else if (findType.equalsIgnoreCase("tournament")) {
                                                  String play_as = sel_play_as_list.toString()
                                                          .replaceAll("\\[", "").replaceAll("\\]", "");
                                                  if (!minage.equalsIgnoreCase("Any") && minage.contains("-")) {
                                                      minage = minage.replaceAll("\\(", "").replaceAll("\\)", "");
                                                      String[] splitage = minage.split("-");
                                                      //Log.e("min & max age", ": min age " + splitage[0] + ", max age =" + splitage[1]);
                                                      minage = splitage[0];
                                                      maxage = splitage[1];
                                                  }
                                                 // Log.e("selecet play as", ": " + play_as);

                                                /*  String play_as = "";
                                                  if (sel_play_as_list.size() > 0) {
                                                      play_as = sel_play_as_list.get(0);
                                                  }*/
                                                  //  String join = "'" + StringUtils.join(arr,"','") + "'";
                                                  new FindTournamentAsync(context, play_as, gender, location, sports,
                                                          minage, maxage, tournament_viewPager, "123").execute();
                                              }
                                          }
                                      }
                                  }
        );

        dialog.show();
    }



/*


    public void opencoachMenuDialog(final Context context, ArrayList<HashMap<String, String>> sportlist,
                                    ArrayList<String> minAgeList,
                                    ArrayList<String> maxAgeList, ArrayList<String> sports_list) {
        ArrayAdapter<String> minageadapter, maxageAdapter, g_Adapter1, sel_sport;
        minageadapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                minAgeList);
        minageadapter.setDropDownViewResource(R.layout.listtextview);
        maxageAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                maxAgeList);
        maxageAdapter.setDropDownViewResource(R.layout.listtextview);
        g_Adapter1 = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                context.getResources().getStringArray(R.array.gender));
        g_Adapter1.setDropDownViewResource(R.layout.listtextview);
        sel_sport = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item, sports_list
        );
        sel_sport.setDropDownViewResource(R.layout.listtextview);
        ;
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogcoachmenu);
        dialog.setCancelable(true);
        Button fc_btn = (Button) dialog.findViewById(R.id.fc_btn);
        final Spinner spinner_gender = (Spinner) dialog.findViewById(R.id.spinner_gender);
        final Spinner min_age_spn = (Spinner) dialog.findViewById(R.id.min_age_spn);

        final Spinner max_age_spn = (Spinner) dialog.findViewById(R.id.max_age_spn);

        final Spinner sport_sel = (Spinner) dialog.findViewById(R.id.sport_sel);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        spinner_gender.setAdapter(g_Adapter1);

        min_age_spn.setAdapter(minageadapter);
        max_age_spn.setAdapter(maxageAdapter);
        sport_sel.setAdapter(sel_sport);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.isSportBgChn = false;
                dialog.cancel();

            }
        });
        fc_btn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Util.isSportBgChn = false;
                                          dialog.cancel();
                                          if (InternetStatus.isConnectingToInternet(context)) {
                                              String gender = spinner_gender.getSelectedItem().toString();
                                              String minage = min_age_spn.getSelectedItem().toString();
                                              String maxage = max_age_spn.getSelectedItem().toString();
                                              String sports = sport_sel.getSelectedItem().toString();
                                              String location = "new Delhi";
                                              Log.e("coach details", "g: " + gender + ",mina " + minage + ",max age" + maxage + ",sp " + sports);
                                              new SearchCoachAsyncTask(context, gender, location, sports, minage, maxage).execute();

                                          }
                                      }
                                  }
        );

        dialog.show();
    }
*/


    //////////////functions for swipe circular means a>b>c then reach at a
    private void handleSetNextItem(ViewPager mViewPager) {
        if (mViewPager != null) {
            int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if (mViewPager.getCurrentItem() == lastPosition) {
                mViewPager.setCurrentItem(0, true);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        }
    }

    private void handleSetPreviousItem(ViewPager mViewPager) {
        if (mViewPager != null) {

            if (mViewPager.getCurrentItem() == 0) {
                mViewPager.setCurrentItem(mViewPager.getAdapter().getCount(), true);
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
            }
        }
    }


///////////////////////////////////////////////////  search coach


    public class SearchCoachAsyncTask extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        String gender, location, sports_cate, age_group_min, age_group_max;
        HashMap<String, String> params = new HashMap<>();
        Context context;
        String seeAll;

        public SearchCoachAsyncTask(Context context, String gender, String location, String sports_cate,
                                    String age_group_min, String age_group_max, String seeAll) {
            this.gender = gender;
            this.location = location;
            this.sports_cate = sports_cate;
            this.age_group_min = age_group_min;
            this.age_group_max = age_group_max;
            this.context = context;
            this.seeAll = seeAll;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.show();
            params.put("gender", gender);
            //  params.put("location", location);
            params.put("latitude", "" + PlayerDetails.current_lat);
            params.put("longitute", "" + PlayerDetails.current_long);
            params.put("sports_cate", sports_cate);
            params.put("age_group_min", age_group_min);
            params.put("age_group_max", age_group_max);
            params.put("see_all", seeAll);


            Log.d("fdgfdgdfgd",gender);
            Log.d("fdgfdgdfgd", String.valueOf(PlayerDetails.current_lat));
            Log.d("fdgfdgdfgd", String.valueOf(PlayerDetails.current_long));
            Log.d("fdgfdgdfgd",sports_cate);
            Log.d("fdgfdgdfgd",age_group_min);
            Log.d("fdgfdgdfgd",age_group_max);
            Log.d("fdgfdgdfgd",seeAll);
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

        protected void onPostExecute(JSONObject jsonObject) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            if (jsonObject != null) {
                try {
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        // Toast.makeText(context, "Information " + jsonObject, Toast.LENGTH_SHORT).show();

                        String url = "";
                        JSONArray array = jsonObject.getJSONArray("items");
                        //parseCoachingData(coachArray);
                        if (array != null) {
                            spullaVideo.clear();
                            for (int i = 0; i < array.length(); i++) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                JSONObject json = null;
                                try {
                                    json = array.getJSONObject(i);
                                    String id = json.getString("id");
                                    String video = json.getString("video").replaceAll("\\s", "");
                                    String coach_icon = json.optString("coach_icon");
                                    String coach_thumbnail = json.getString("thumbnail");
                                    //type.replaceAll("\\s", "");

                                    hashMap.put("id", id);
                                    hashMap.put("video", video);
                                    hashMap.put("coach_icon", coach_icon);
                                    if (json.optString("icon_name").equalsIgnoreCase("null") || json.optString("icon_name").matches("")) {
                                        hashMap.put("icon_name", "");
                                    } else {
                                        hashMap.put("icon_name", json.optString("icon_name"));
                                    }
                                    if (json.optString("coachname").equalsIgnoreCase("null") || json.optString("coachname").matches("")) {
                                        hashMap.put("coach_name", getString(R.string.notavail));
                                    } else {
                                        hashMap.put("coach_name", json.optString("coachname"));
                                    }
                                    hashMap.put("coach_thumbnail", coach_thumbnail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                spullaVideo.add(hashMap);
                            }
                            //set facts adapter
                            video_viewPager.setAdapter(new VideoAdapter(MainActivity.this, spullaVideo));
                            video_viewPager.getAdapter().notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "No Information available for Coaching", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context, "Information not available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }
////////////////////
    /**/

    ////////////////////////////////////////////////////////////// Async task for tournament age group
    public void getAgeGrp(final boolean isTou_ag, String Url) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait....");
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        parseAgeGroupResponse(jsonObject, isTou_ag);
                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("Volley", "Error");
                        //hidePDialog();
                       Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jor);
    }
/////////////////////parse function of Age Group for Search tournament & Coach

    private void parseAgeGroupResponse(JSONObject jsonObject, boolean isTou_ag) {
        //Log.e("Age_Grp response", "" + jsonObject);
        if (jsonObject != null) {
            if (jsonObject.optString("status").equalsIgnoreCase("1")) {
                JSONArray tagegrepArray = jsonObject.optJSONArray("t_age_grp");
                if (tagegrepArray != null && isTou_ag) {
                    tage_grp_list.clear();
                    tage_grp_list.add("Any");
                    for (int a = 0; a < tagegrepArray.length(); a++) {
                        try {
                            tage_grp_list.add(tagegrepArray.getJSONObject(a).optString("age"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.e("Tourn_Age_Group error", "" + e);
                        }
                    }

                    findAllDetails(MainActivity.this, "tournament", tage_grp_list, maxAgeList, sports_list);
                } else if (tagegrepArray != null && isTou_ag == false) {
                    coh_ag_list.clear();
                    coh_ag_list.add("Any");
                    for (int a = 0; a < tagegrepArray.length(); a++) {
                        try {
                            String[] ag_length = tagegrepArray.getJSONObject(a).optString("age").split(",");
                            for (int g = 0; g < ag_length.length; g++) {
                                if (!ag_length[g].equalsIgnoreCase("(-)")) {
                                    coh_ag_list.add(ag_length[g]);
                                    //Log.e("add coach age ",": "+ag_length[g]);
                                }
                                //Log.e("coach age ",": "+ag_length[g]);

                            }
                            //
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.e("Age_Group error", "" + e);
                        }
                    }
                    findAllDetails(MainActivity.this, "coaching", coh_ag_list, maxAgeList, sports_list);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Age group not available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this,
                        "Age group not available", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this,
                    "Age group not available", Toast.LENGTH_SHORT).show();

        }


    }

    ////////////////////////////////

    //////////////////////////async task for get  all data
    public class GetAllDataAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        HashMap<String, String> params = new HashMap<>();


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait....");
            pDialog.setCancelable(false);
            pDialog.show();
            params.put("user_id", MyPreference.loadUserid(MainActivity.this));
            //  params.put("location", location);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.Spulla_Home_URL, "GET", params);
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

        protected void onPostExecute(JSONObject response) {

            if (pDialog.isShowing())
                pDialog.dismiss();
            getHotDeals();
            new GetPlayerDetailsAsync(MainActivity.this).execute();
            if (response != null) {
                try {
                    //Log.e("respone", ": " + response);
//                    if (response.getString("status").equalsIgnoreCase("1")) {
                    response.getString("status");
                    // Log.e("main responce", "" + response.optString("status"));
                    JSONArray gossipdata = response.optJSONArray("item");
                    JSONArray facts = response.optJSONArray("facts");
                    player= response.optJSONArray("player");
                    JSONArray video = response.optJSONArray("coach_video");
                    JSONArray tournament = response.optJSONArray("tournament");
                    JSONArray sportcategory = response.optJSONArray("sports");
                    JSONArray sportcategory2 = response.optJSONArray("sports2");
                    JSONArray ageGroup = response.optJSONArray("agegroup");
                    JSONArray shopingArray = response.optJSONArray("category");
//                    Log.d("gvdfdgbdfgdf",player.toString());

                    try {
                        Log.d("gvdfdgbdfgdf_main", player.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    parseGossipData(gossipdata);
                    parseFactsData(facts);
                    parseCoachingData(video);
                    parseTournamentData(tournament);
                    parsePlayerData(player);
                    parseShoppingData(shopingArray);
                    parseSportsType(sportcategory);
                    parseSportsType2(sportcategory2);
                    parseAgeGroup(ageGroup);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }

        }

    }
//////////////////////////////////////////////////////////////////////

    private void setmRegistrationBroadcastReceiver() {
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    //////////////////////Get Hot Deal
    public void getHotDeals() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, Api.Hot_Deal_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        //Log.e("Hot deals>>>>", "" + response);
                        if (response != null) {
                            try {

                                if (response.optString("status").equalsIgnoreCase("1")) {
                                    JSONArray hotdealArray = response.optJSONArray("hotdeal");
                                    parseHotDealResponse(hotdealArray);
                                }

								/*Toast.makeText(BaseActivity.this,"cat"+dracategoryList.size(),Toast.LENGTH_LONG).show();*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Error " + "No hot deals avaiable", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        //hidePDialog();
                        Toast.makeText(MainActivity.this, "Error " + "Please try again", Toast.LENGTH_LONG).show();

                    }
                }
        );
        requestQueue.add(jor);
    }


    private void searchHotDeal(final String catid, final String catName) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.Search_Hot_Deal_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        //Log.e("Search_Hot_Deal_Url>>>>", "" + result);
                        JSONObject response = null;
                        try {
                            response = new JSONObject(result);
                            if (response != null && response.optString("status").equalsIgnoreCase("1")) {
                                JSONArray hotdealArray = response.optJSONArray("hotdeal");
                                parseHotDealResponse(hotdealArray);
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Hot deals not avaiable for " + catName, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Please check Internet Connection", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();
                map.put("cat_id", catid);
                Log.d("gdfgdfgdfhdf",catid);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    //////////parse hotdeals response
    private void parseHotDealResponse(JSONArray hotdealArrar) {
        if (hotdealArrar != null) {
            hotdealList.clear();
            for (int i = 0; i < hotdealArrar.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = hotdealArrar.getJSONObject(i);
                    hashMap.put("p_id", json.optString("p_id"));
                    hashMap.put("sub_cat_id", json.optString("sub_cat_id"));
                    hashMap.put("product_title", json.optString("product_title"));
                    hashMap.put("product_name", json.optString("product_name"));
                    hashMap.put("img", json.optString("img"));
                    hashMap.put("price_mrp", json.optString("price_mrp"));
                    hashMap.put("celling_price", json.optString("celling_price"));
                    hashMap.put("discount_percent", json.optString("discount_percent"));
                    hashMap.put("product_size", json.optString("product_size"));
                    hashMap.put("brand", json.optString("brand"));
                    hashMap.put("colour", json.optString("colour"));
                    hashMap.put("weight", json.optString("weight"));
                    hashMap.put("material", json.optString("material"));
                    hashMap.put("description", json.optString("description"));
                   // hashMap.put("types", json.optString("type"));
                    hotdealList.add(hashMap);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            shopping_viewPager.setAdapter(new ShoppingAdapter(MainActivity.this, hotdealList));
            shopping_viewPager.getAdapter().notifyDataSetChanged();
        }

    }
}
