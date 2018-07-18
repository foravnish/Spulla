package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riseintech.spulla.adapter.CoachScheduleAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CochScheduleActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager ch_des_pager;
    ImageView cImg, cImg1;
    TextView cName, cLocation, cName1, cLocation1, cAbout, schd_dts, coch_spclt, coch_teaches;
    Button join_me;
    String C_ID;
    private String URL_PATH = "http://spulla.com/admin/spulla_api/coachvideodetail.php";
    ArrayList<HashMap<String, String>> coachschedule;
    public static int currentpos;
    RelativeLayout parent_ly;
    Toolbar toolbar_coach;
    LinearLayout achiv_lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coachingdetails);
        schd_dts = (TextView) findViewById(R.id.schd_dts);
        coch_spclt = (TextView) findViewById(R.id.coch_spclt);
        coch_teaches = (TextView) findViewById(R.id.coch_teaches);
        achiv_lin = (LinearLayout) findViewById(R.id.achiv_lin);
        toolbar_coach = (Toolbar) findViewById(R.id.toolbar_coach);
        toolbar_coach.setTitle(getString(R.string.coach));
        setSupportActionBar(toolbar_coach);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        parent_ly = (RelativeLayout) findViewById(R.id.parent_ly);
        parent_ly.setVisibility(View.GONE);
        schd_dts.setText("Schedule : " + 1);

        //join_me= (Button) findViewById(R.id.join_me);
        cName = (TextView) findViewById(R.id.cn_txv);
        cLocation = (TextView) findViewById(R.id.loc_txv);
        cName1 = (TextView) findViewById(R.id.cn1_txv);
        cLocation1 = (TextView) findViewById(R.id.cnloc1_txv);
        cAbout = (TextView) findViewById(R.id.coch_desc);
        cImg = (ImageView) findViewById(R.id.circleImageView);
        cImg1 = (ImageView) findViewById(R.id.ch_img);
        coachschedule = new ArrayList<HashMap<String, String>>();
        C_ID = getIntent().getStringExtra("cid");
       //Log.e("coach id", ">>" + C_ID);

        if (InternetStatus.isConnectingToInternet(this)) {
            new GetCoachDetail().execute();
        }
        ch_des_pager = (ViewPager) findViewById(R.id.ch_des_pager);
        String[] stringArray = getResources().getStringArray(R.array.choose_sports);
        ch_des_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            ArrayList<HashMap<String, String>> cmtlist = new ArrayList<HashMap<String, String>>();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentpos = position;
                //Log.e("current pos", "" + currentpos);

            }

            @Override
            public void onPageSelected(int position) {

                int h = position + 1;
                schd_dts.setText("Schedule : " + h);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //join_me.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.Show_Coach_Dtls = 0;
    }

    class GetCoachDetail extends AsyncTask<String, String, JSONObject> {
        ProgressDialog loading;
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(CochScheduleActivity.this, "Loading Data", "Please wait...", false, false);

            params.put("id", C_ID);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(URL_PATH, "GET", params);
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
            String staus = "";
            if (loading.isShowing()) {
                Util.Show_Coach_Dtls = 1;
                loading.dismiss();
                parent_ly.setVisibility(View.VISIBLE);


            }
            if (json != null) {
                try {
                    staus = json.getString("status");
                    if (staus.equals("1")) {
                        JSONObject objitem = json.getJSONObject("items");
                        String coachName = objitem.optString("coachname");
                        String location = objitem.optString("locality");
                        String coachImg = objitem.optString("img");
                        String coachPic = objitem.optString("coach_pic");
                        String aboutcoach = objitem.optString("about");
                        Util.showImage(CochScheduleActivity.this, coachImg, cImg);
                        JSONArray sarray = objitem.optJSONArray("sch");
                        String term = "<b>" + getString(R.string.Terminology) + "</b>" + objitem.optString("coaching");
                        coch_teaches.setText(Util.fromHtml(term));
                        String spclty = "<b>" + getString(R.string.Speciality) + "</b>" + objitem.optString("sport_specialy");
                        coch_spclt.setText(Util.fromHtml(spclty));
                        if (objitem.optString("achive").contains(",")) {
                            String acheivs[] = objitem.optString("achive").split(",");
                            for (int j = 0; j < acheivs.length; j++) {
                                TextView time = new TextView(CochScheduleActivity.this);
                                String achiv = "<b>" + getString(R.string.Achievement) + " " + "</b>" + "\n" + acheivs[j];
                                time.setText(Util.fromHtml(achiv));
                                time.setTypeface(Typeface.SERIF);
                                time.setTextColor(ContextCompat.getColor(CochScheduleActivity.this, R.color.black));
                                time.setTextSize(16);
                                time.setPadding(5, 0, 5, 0);
                                time.setGravity(Gravity.CENTER_VERTICAL);
                                achiv_lin.addView(time);
                                achiv_lin.setVisibility(View.VISIBLE);
                            }
                        } else {
                            TextView time = new TextView(CochScheduleActivity.this);
                            String achiv = "<b>" + getString(R.string.Achievement) + ": " + "</b>" + "\n" + objitem.optString("achive");
                            time.setText(Util.fromHtml(achiv));
                            time.setTextColor(ContextCompat.getColor(CochScheduleActivity.this, R.color.black));
                            time.setTextSize(16);
                            time.setTypeface(Typeface.SERIF);
                            time.setPadding(5, 0, 5, 0);
                            time.setGravity(Gravity.CENTER_VERTICAL);
                            achiv_lin.addView(time);
                            achiv_lin.setVisibility(View.VISIBLE);
                        }
                        if (sarray != null) {
                            //Log.e("schedule details", ": " + sarray);
                            for (int i = 0; i < sarray.length(); i++) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("schedule", sarray.getJSONObject(i).optString("schedule"));
                                if (sarray.getJSONObject(i).optString("schedule_start_date").contains(" ")) {
                                    String s[] = sarray.getJSONObject(i).optString("schedule_start_date").split(" ");
                                    String e[] = sarray.getJSONObject(i).optString("schedule_end_date").split(" ");
                                    hashMap.put("schedule_start_date", s[0]);
                                    hashMap.put("schedule_end_date", e[0]);
                                } else {
                                    hashMap.put("schedule_start_date", sarray.getJSONObject(i).optString("schedule_start_date"));
                                    hashMap.put("schedule_end_date", sarray.getJSONObject(i).optString("schedule_end_date"));
                                }

                                hashMap.put("day", sarray.getJSONObject(i).optString("schedule_day"));
                                hashMap.put("time_in", sarray.getJSONObject(i).optString("time_in"));
                                hashMap.put("time_out", sarray.getJSONObject(i).optString("time_out"));
                                hashMap.put("schedule_location", sarray.getJSONObject(i).optString("schedule_location"));
                                hashMap.put("total", sarray.getJSONObject(i).optString("total"));
                                hashMap.put("full_fees", sarray.getJSONObject(i).optString("full_fees"));
                                hashMap.put("off", sarray.getJSONObject(i).optString("off"));
                                hashMap.put("total", sarray.getJSONObject(i).optString("total"));
                                hashMap.put("min", sarray.getJSONObject(i).optString("min"));
                                hashMap.put("max", sarray.getJSONObject(i).optString("max"));
                                hashMap.put("pay", sarray.getJSONObject(i).optString("pay"));
                                hashMap.put("schedule_gender", sarray.getJSONObject(i).optString("schedule_gender"));
                                if (!sarray.getJSONObject(i).optString("full_fees").equalsIgnoreCase("")
                                        && !sarray.getJSONObject(i).optString("time_in").equalsIgnoreCase("")
                                        && !sarray.getJSONObject(i).optString("schedule_day").equalsIgnoreCase("")
                                        && !sarray.getJSONObject(i).optString("schedule_gender").equalsIgnoreCase("")) {
                                    coachschedule.add(hashMap);
                                }
                               // coachschedule.add(hashMap);
                            }
                        }

                        if (coachschedule.size() > 0) {
                            //Log.e("coachschedule array", ">>" + coachschedule.size());
                            ch_des_pager.setAdapter(new CoachScheduleAdapter(CochScheduleActivity.this, coachschedule, false));
                            ch_des_pager.setCurrentItem(0);
                        }
                        cName.setText(coachName);
                        cLocation.setText(location);
                        cName1.setText(coachName);
                        cLocation1.setText(location);
                        cAbout.setText(Html.fromHtml("<b>" + "About us: " + "</b>"+ aboutcoach));
                        Util.showImage(CochScheduleActivity.this, coachPic, cImg1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    parent_ly.setVisibility(View.VISIBLE);


                }
            }


        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
