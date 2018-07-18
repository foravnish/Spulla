package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.AsyncTaskService.SendRequestAsync;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FindPlayerDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    String userGender_val, userCategory_val, userSchool_val, userClz_val,
            image_url, userclub1_val, userclub1_va2, user_name_val, user_dob, user_desi,
            user_comp, user_prof, user_city, user_state, user_country, sports1, sports2, sport3, sports1_sts, sports2_sts, sports3_sts, user_address;
    // CollapsingToolbarLayout collapsingToolbar;
    ImageView propic;
    Bitmap bitmap;
    ImageView imgchat, imgrequest, imgdecline, imgblock, profile_image;
    Toolbar toolbar_find;
    String id;
    TextView gameplay1_expertise, gameplay2_expt, gameplay3_expt;

    ////////////declaire all layout
    TextView spt_hd, clb_hd;
    LinearLayout gp3_ly, gp2_ly, gp1_ly, clb_ly,
            country_ly, state_ly, city_ly, work_ly, clg_ly, sch_ly,
            cate_ly, address_ly, gender_ly, age_ly, name_ly;
    /////////parent layout
    LinearLayout detail_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_player_details);
        id = getIntent().getStringExtra("player_id");
        detail_content = (LinearLayout) findViewById(R.id.pl_dtl_ly);
        if (InternetStatus.isConnectingToInternet(FindPlayerDetailsActivity.this)) {
            new findPlayerAsync(FindPlayerDetailsActivity.this, getIntent().getStringExtra("player_id")).execute();
        }
//        init(FindPlayerDetailsActivity.this);
    }

    public void init(final Context context) {


        //////////////////intialize all layout for fields  player details
        gp3_ly = (LinearLayout) findViewById(R.id.gp3_ly);
        gp2_ly = (LinearLayout) findViewById(R.id.gp2_ly);
        gp1_ly = (LinearLayout) findViewById(R.id.gp1_ly);
        clb_ly = (LinearLayout) findViewById(R.id.clb_ly);
        country_ly = (LinearLayout) findViewById(R.id.country_ly);
        state_ly = (LinearLayout) findViewById(R.id.state_ly);
        city_ly = (LinearLayout) findViewById(R.id.city_ly);
        work_ly = (LinearLayout) findViewById(R.id.work_ly);
        clg_ly = (LinearLayout) findViewById(R.id.clg_ly);
        sch_ly = (LinearLayout) findViewById(R.id.sch_ly);
        cate_ly = (LinearLayout) findViewById(R.id.cate_ly);
        gender_ly = (LinearLayout) findViewById(R.id.gender_ly);
        age_ly = (LinearLayout) findViewById(R.id.age_ly);
        name_ly = (LinearLayout) findViewById(R.id.name_ly);
        address_ly = (LinearLayout) findViewById(R.id.address_ly);

        spt_hd = (TextView) findViewById(R.id.spt_hd);
        clb_hd = (TextView) findViewById(R.id.clb_hd);

        ///////////////////////////////////////////
        toolbar_find = (Toolbar) findViewById(R.id.toolbar_find);
        toolbar_find.setTitle(user_name_val);
        setSupportActionBar(toolbar_find);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ImageButton ib = (ImageButton) toolbar_find.findViewById(R.id.buttonimagpro);
        propic = (ImageView) findViewById(R.id.propic);
        Util.showImage(context, image_url, propic);
        ;
        gameplay1_expertise = (TextView) findViewById(R.id.gameplay1_expertise);
        gameplay2_expt = (TextView) findViewById(R.id.gameplay2_expt);
        gameplay3_expt = (TextView) findViewById(R.id.gameplay3_expt);
        gameplay1_expertise.setVisibility(View.GONE);
        gameplay3_expt.setVisibility(View.GONE);
        gameplay2_expt.setVisibility(View.GONE);
        TextView userAge = (TextView) findViewById(R.id.profile_age);
        TextView name = (TextView) findViewById(R.id.name);
        TextView userGender = (TextView) findViewById(R.id.profile_gender);
        TextView userCategory = (TextView) findViewById(R.id.profile_category);
        TextView userSchool = (TextView) findViewById(R.id.profile_school);
        TextView userClz = (TextView) findViewById(R.id.profile_clz);
        TextView userWork = (TextView) findViewById(R.id.profile_work);
        TextView userclub1 = (TextView) findViewById(R.id.club1);
        TextView userclub2 = (TextView) findViewById(R.id.club2);
        TextView usergame1 = (TextView) findViewById(R.id.gameplay1);
        TextView usergame2 = (TextView) findViewById(R.id.gameplay2);
        TextView usergame3 = (TextView) findViewById(R.id.gameplay3);
        TextView address = (TextView) findViewById(R.id.address);

        TextView u_country = (TextView) findViewById(R.id.u_country);
        TextView u_state = (TextView) findViewById(R.id.u_state);
        TextView u_city = (TextView) findViewById(R.id.u_city);
        imgrequest = (ImageView) findViewById(R.id.imgaccept);
        imgdecline = (ImageView) findViewById(R.id.imgdecline);
        imgblock = (ImageView) findViewById(R.id.imgblock);
        imgchat = (ImageView) findViewById(R.id.imgchat);

        propic.setOnClickListener(this);
        imgrequest.setOnClickListener(this);
        imgblock.setOnClickListener(this);
        imgdecline.setOnClickListener(this);
        imgchat.setOnClickListener(this);

        if (user_name_val.equalsIgnoreCase("") || user_name_val.equalsIgnoreCase("null")) {
            name_ly.setVisibility(View.GONE);
        } else {
            name.setText(user_name_val);
        }
        if (user_dob.equalsIgnoreCase("") || user_dob.equalsIgnoreCase("null")) {
            age_ly.setVisibility(View.GONE);
        } else {
            userAge.setText(user_dob);
        }
        if (userGender_val.equalsIgnoreCase("") || userGender_val.equalsIgnoreCase("null")) {
            gender_ly.setVisibility(View.GONE);
        } else {
            userGender.setText(userGender_val);
        }
        if (userSchool_val.equalsIgnoreCase("") || userSchool_val.equalsIgnoreCase("null")) {
            sch_ly.setVisibility(View.GONE);
        } else {
            userSchool.setText(userSchool_val);
        }
        if (userClz_val.equalsIgnoreCase("") || userClz_val.equalsIgnoreCase("null")) {
            clg_ly.setVisibility(View.GONE);
        } else {
            userClz.setText(userClz_val);
        }
        if (user_comp.equalsIgnoreCase("") || user_comp.equalsIgnoreCase("null")) {
            work_ly.setVisibility(View.GONE);
        } else {
            userWork.setText(user_comp);
        }
        if (user_city.equalsIgnoreCase("") || user_city.equalsIgnoreCase("null")) {
            city_ly.setVisibility(View.GONE);
        } else {
            u_city.setText(user_city);
        }
        if (user_state.equalsIgnoreCase("") || user_state.equalsIgnoreCase("null")) {
            state_ly.setVisibility(View.GONE);
        } else {
            u_state.setText(user_state);
        }
        if (user_country.equalsIgnoreCase("") || user_country.equalsIgnoreCase("null")) {
            country_ly.setVisibility(View.GONE);
        } else {
            u_country.setText(user_country);
        }
        if (userCategory_val.equalsIgnoreCase("") || userCategory_val.equalsIgnoreCase("null")) {
            cate_ly.setVisibility(View.GONE);
        } else {
            userCategory.setText(userCategory_val);
        }
        if (user_address.equalsIgnoreCase("") || user_address.equalsIgnoreCase("null")
                || user_address.equalsIgnoreCase(getString(R.string.notavail))) {
            address_ly.setVisibility(View.GONE);
        } else {
            address.setText(user_address);
        }

        //////////////////validation for club member
        if (userclub1_val.equalsIgnoreCase("") || userclub1_val.equalsIgnoreCase("null")) {
            userclub1.setVisibility(View.GONE);
        } else {
            userclub1.setText(userclub1_val);
        }
        if (userclub1_va2.equalsIgnoreCase("") || userclub1_va2.equalsIgnoreCase("null")) {
            userclub2.setVisibility(View.GONE);
        } else {
            userclub2.setText(userclub1_va2);
        }
        if (userclub1_va2.equalsIgnoreCase("") && userclub1_val.equalsIgnoreCase("")) {
            clb_ly.setVisibility(View.GONE);
            clb_hd.setVisibility(View.GONE);
        } else if (userclub1_va2.equalsIgnoreCase("null") && userclub1_val.equalsIgnoreCase("null")) {
            clb_ly.setVisibility(View.GONE);
            clb_hd.setVisibility(View.GONE);
        }
        ////////////////////////// validation for sports type

        if (sports1_sts.equalsIgnoreCase(getString(R.string.select_expertise))) {
            sports1_sts = "";
            gameplay1_expertise.setVisibility(View.GONE);
        } else {
            gameplay1_expertise.setVisibility(View.VISIBLE);
            gameplay1_expertise.setText(sports1_sts);

        }
        if (sports2_sts.equalsIgnoreCase(getString(R.string.select_expertise))) {
            sports2_sts = "";
            gameplay2_expt.setVisibility(View.GONE);
        } else {
            gameplay2_expt.setVisibility(View.VISIBLE);
            gameplay2_expt.setText(sports2_sts);
        }
        if (sports3_sts.equalsIgnoreCase(getString(R.string.select_expertise))) {
            sports3_sts = "";
            gameplay3_expt.setVisibility(View.GONE);
        } else {
            gameplay3_expt.setVisibility(View.VISIBLE);
            gameplay3_expt.setText(sports3_sts);
        }

        if (sports1.equalsIgnoreCase("") || sports1.equalsIgnoreCase("null")) {
            gp1_ly.setVisibility(View.GONE);
        } else {
            usergame1.setText(sports1);
        }

        if (sports2.equalsIgnoreCase("") || sports2.equalsIgnoreCase("null")) {
            gp2_ly.setVisibility(View.GONE);
        } else {
            usergame2.setText(sports2);
        }

        if (sport3.equalsIgnoreCase("") || sport3.equalsIgnoreCase("null")) {
            gp3_ly.setVisibility(View.GONE);
        } else {
            usergame3.setText(sport3);
        }

        if (sport3.equalsIgnoreCase("") && sports2.equalsIgnoreCase("") && sports1.equalsIgnoreCase("")) {
            gp3_ly.setVisibility(View.GONE);
            gp2_ly.setVisibility(View.GONE);
            gp1_ly.setVisibility(View.GONE);
            spt_hd.setVisibility(View.GONE);
        } else if (sport3.equalsIgnoreCase("null") && sports2.equalsIgnoreCase("null")
                && sports1.equalsIgnoreCase("null")) {
            gp3_ly.setVisibility(View.GONE);
            gp2_ly.setVisibility(View.GONE);
            gp1_ly.setVisibility(View.GONE);
            spt_hd.setVisibility(View.GONE);

        }
        //////////////////////


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.propic:
                Util.showFullImageDialog(FindPlayerDetailsActivity.this, image_url, user_name_val);
                break;
            case R.id.imgaccept:
                if (InternetStatus.isConnectingToInternet(FindPlayerDetailsActivity.this)) {
                    new SendRequestAsync(FindPlayerDetailsActivity.this, id, "send_request").execute();
                }
                break;
            case R.id.imgblock:
                if (InternetStatus.isConnectingToInternet(FindPlayerDetailsActivity.this)) {
                    new SendRequestAsync(FindPlayerDetailsActivity.this, id, "block").execute();
                }
                break;
            case R.id.imgchat:
                Util.Sender_ID = id;
                Util.Sender_Name = user_name_val;
                Util.Sender_Img = image_url;
                Intent intent = new Intent(this, ChatDetailsActivity.class);
                intent.putExtra("player_id", id);
                startActivity(intent);
                break;
            case R.id.imgdecline:
                if (InternetStatus.isConnectingToInternet(FindPlayerDetailsActivity.this)) {
                    new SendRequestAsync(FindPlayerDetailsActivity.this, id, "reject").execute();
                }
                break;
        }
    }

    public class findPlayerAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;

        private static final String TAG_STATUS = "findPlayerAsync";
        private static final String TAG_MESSAGE = "msg";
        String player_id;
        HashMap<String, String> params = new HashMap<>();
        Context context;

        public findPlayerAsync(Context context, String id) {
            this.player_id = id;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            detail_content.setVisibility(View.GONE);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("user_id", player_id);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.Get_Player_Details, "GET", params);
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
            detail_content.setVisibility(View.VISIBLE);

            if (json != null) {
                try {
                    String url = "";
                    ArrayList<HashMap<String, String>> commtList = new ArrayList<>();

                    if (json.getString("status").equalsIgnoreCase("1")) {

                        user_name_val = json.optString("name");
                        userGender_val = json.optString("gender");
                        userClz_val = json.optString("college");
                        user_dob = json.optString("dob");
                        if (!json.getString("images").isEmpty()) {
                            image_url = json.getString("images");
                        } else {
                            image_url = "fd";
                        }

                        userclub1_val = json.optString("clubmember1");
                        userclub1_va2 = json.optString("clubmember2");

                        sports1 = json.optString("sports1");
                        sports2 = json.optString("sports2");
                        sport3 = json.optString("sports3");

                        sports1_sts = json.optString("sports1_status");
                        sports2_sts = json.optString("sports2_status");
                        sports3_sts = json.optString("sports3_status");
                        userSchool_val = json.optString("schooling");
                        user_desi = json.optString("designation");
                        user_comp = json.optString("company");
                        user_prof = json.optString("profession");
                        user_city = json.optString("city");
                        user_address = Util.getAddress(context, json.getString("lat"),
                                json.optString("long"));
                        user_state = json.optString("state");
                        user_country = json.optString("country");
                        userCategory_val = json.optString("playas");
                        init(FindPlayerDetailsActivity.this);
                    } else {
                        Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    ////////////////////////////////
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
