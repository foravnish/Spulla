package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.adapter.TournamentAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TournamentDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager ch_des_pager;
    TextView sh_date, sports, sh_loc, pz_money, book_amt, book_amut1, shd_fes, ch_gend,
            ch_aggrp, t_type, team_size, end_date,sadd, s_city,s_state;
    Button macth_details;
    Button add_tourn, pay_book_amt, pay_full_amt;
    String t_id = "";
    String entry_fees = "0", book_fees = "0";
    public JSONArray matchArray, t_namearray, semi_array, final_array, knockout_array, quarter_final_array;
    LinearLayout tourn_ly, pay_lin;
    int responseSucess;
    //////////////////
    TextView tou_desc, tou_abt, tn_name, loc_tou, time, t_day, t_date,
            e_date, std_nm, st_add, t_name1, t_loc;
    ImageView t_ImageView, t_img1;
    RadioGroup pay_typerg;
    RadioButton paytyp_rb;
    String payfee = "";
    String pay_amount;
    int pos;
    Toolbar toolbar_tournmt;
    String teamsize = "";
    LinearLayout desc_ly, abt_ly;
    TextView abt_title, desc_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);
        t_id = TournamentAdapter.t_id;
        toolbar_tournmt = (Toolbar) findViewById(R.id.toolbar_tournmt);
        toolbar_tournmt.setTitle(getString(R.string.s_tournament));
        setSupportActionBar(toolbar_tournmt);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tourn_ly = (LinearLayout) findViewById(R.id.tourn_ly);
        desc_ly = (LinearLayout) findViewById(R.id.desc_ly);
        abt_ly = (LinearLayout) findViewById(R.id.abt_ly);

        pay_lin = (LinearLayout) findViewById(R.id.pay_lin);
        macth_details = (Button) findViewById(R.id.macth_details);
        pay_book_amt = (Button) findViewById(R.id.pay_book_amt);
        pay_full_amt = (Button) findViewById(R.id.pay_full_amt);
        pay_full_amt.setOnClickListener(this);
        pay_book_amt.setOnClickListener(this);


///////////////////////
        tou_desc = (TextView) findViewById(R.id.tou_desc);
        tou_abt = (TextView) findViewById(R.id.tou_abt);
        t_ImageView = (ImageView) findViewById(R.id.t_ImageView);
        t_img1 = (ImageView) findViewById(R.id.t_img1);

        pay_typerg = (RadioGroup) findViewById(R.id.pay_typerg);

        tn_name = (TextView) findViewById(R.id.tn_name);
        loc_tou = (TextView) findViewById(R.id.loc_tou);
        time = (TextView) findViewById(R.id.time);
        t_day = (TextView) findViewById(R.id.t_day);
        t_day.setText("Prize Money");
        t_date = (TextView) findViewById(R.id.t_date);
        e_date = (TextView) findViewById(R.id.e_date);
        std_nm = (TextView) findViewById(R.id.std_nm);
        st_add = (TextView) findViewById(R.id.st_add);

        t_name1 = (TextView) findViewById(R.id.t_name1);
        t_loc = (TextView) findViewById(R.id.t_loc);
        sadd= (TextView) findViewById(R.id.sadd);

////////////////////////

        ////////////////tournamment details
        add_tourn = (Button) findViewById(R.id.book);
        sh_date = (TextView) findViewById(R.id.sh_date);
        end_date = (TextView) findViewById(R.id.end_date);

        sports = (TextView) findViewById(R.id.sports);
        sh_loc = (TextView) findViewById(R.id.sh_loc);
        pz_money = (TextView) findViewById(R.id.pz_money);
        book_amt = (TextView) findViewById(R.id.book_amut);
        book_amut1 = (TextView) findViewById(R.id.book_amut1);
        shd_fes = (TextView) findViewById(R.id.shd_fes);
        ch_gend = (TextView) findViewById(R.id.ch_gend);
        ch_aggrp = (TextView) findViewById(R.id.ch_aggrp);
        t_type = (TextView) findViewById(R.id.t_type);
        team_size = (TextView) findViewById(R.id.team_size);
        abt_title = (TextView) findViewById(R.id.abt_title);
        desc_title = (TextView) findViewById(R.id.desc_title);
        s_city = (TextView) findViewById(R.id.s_city);
        s_state = (TextView) findViewById(R.id.s_state);

        ////////////////////////////////////////////
        macth_details.setOnClickListener(this);
        add_tourn.setOnClickListener(this);

        if (InternetStatus.isConnectingToInternet(TournamentDetailsActivity.this)) {
            new TouranmentDetailsAsync(TournamentDetailsActivity.this, t_id).execute();
        }

       /* ch_des_pager = (ViewPager) findViewById(R.id.ch_des_pager);
        String[] stringArray = getResources().getStringArray(R.array.choose_sports);
        ch_des_pager.setAdapter(new TournametScheduleAdapter(TournamentDetailsActivity.this, stringArray, true));
        ch_des_pager.setCurrentItem(1);
*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_book_amt:
                Intent pay_book_Intent = new Intent(this, BookCoach.class);
                pay_book_Intent.putExtra("fee_amount", book_fees);
                pay_book_Intent.putExtra("fee_type", "tournament fee");
                pay_book_Intent.putExtra("id", t_id);
                pay_book_Intent.putExtra("full_fees", entry_fees);
                startActivity(pay_book_Intent);
                break;
            case R.id.pay_full_amt:
                Intent pay_fullIntent = new Intent(this, BookCoach.class);
                pay_fullIntent.putExtra("fee_amount", entry_fees);
                pay_fullIntent.putExtra("fee_type", "tournament fee");
                pay_fullIntent.putExtra("id", t_id);
                pay_fullIntent.putExtra("full_fees", entry_fees);
                startActivity(pay_fullIntent);
                break;
            case R.id.book:
                Intent in = new Intent(this, BookCoach.class);
                in.putExtra("fee_amount", entry_fees);
                in.putExtra("fee_type", "tournament fee");
                in.putExtra("id", t_id);
                in.putExtra("full_fees", entry_fees);
                startActivity(in);

               /* if (pay_typerg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(TournamentDetailsActivity.this, "Please choose Pay Type", Toast.LENGTH_SHORT).show();
                } else {
                    paytyp_rb = (RadioButton) pay_typerg.findViewById(pay_typerg.getCheckedRadioButtonId());
                    payfee = paytyp_rb.getText().toString();
                    if (payfee.equalsIgnoreCase("Entry Fees")) {
                        pay_amount = shd_fes.getText().toString();
                        Intent bookIntent = new Intent(TournamentDetailsActivity.this, BookCoach.class);
                        bookIntent.putExtra("fee_amount", pay_amount);
                        bookIntent.putExtra("fee_type", "tournament fee");
                        bookIntent.putExtra("id", t_id);
                        bookIntent.putExtra("full_fees", shd_fes.getText().toString());
                        startActivity(bookIntent);
                    } else if (payfee.equalsIgnoreCase("Booking Amount")) {
                        pay_amount = book_amt.getText().toString();
                        Intent bookIntent = new Intent(TournamentDetailsActivity.this, BookCoach.class);
                        bookIntent.putExtra("fee_amount", pay_amount);
                        bookIntent.putExtra("fee_type", "tournament fee");
                        bookIntent.putExtra("id", t_id);
                        bookIntent.putExtra("full_fees", shd_fes.getText().toString());

                        startActivity(bookIntent);
                    }
                }*/
                break;
            case R.id.macth_details:
                if (teamsize.isEmpty() || teamsize.matches("")) {
                    Toast.makeText(TournamentDetailsActivity.this, "Teams not available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), MatchDetailActivity.class);

                    if (matchArray == null) {
                        Toast.makeText(TournamentDetailsActivity.this, "Match details not availables", Toast.LENGTH_SHORT).show();
                    } else if (final_array == null) {
                        Toast.makeText(TournamentDetailsActivity.this, "Match details not availables", Toast.LENGTH_SHORT).show();
                    } else if (semi_array == null) {
                        Toast.makeText(TournamentDetailsActivity.this, "Match details not availables", Toast.LENGTH_SHORT).show();
                    } else if (knockout_array == null) {
                        Toast.makeText(TournamentDetailsActivity.this, "Match details not availables", Toast.LENGTH_SHORT).show();
                    } else if (quarter_final_array == null) {
                        Toast.makeText(TournamentDetailsActivity.this, "Match details not availables", Toast.LENGTH_SHORT).show();
                    } else if (t_namearray == null) {
                        Toast.makeText(TournamentDetailsActivity.this, "Match details not availables", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.putExtra("match_array", matchArray.toString());
                        intent.putExtra("final_array", final_array.toString());
                        intent.putExtra("semifinal_array", semi_array.toString());
                        intent.putExtra("knckout_array", knockout_array.toString());
                        intent.putExtra("quarter_final_array", quarter_final_array.toString());
                        intent.putExtra("tname_array", t_namearray.toString());
                        intent.putExtra("team_size", teamsize);
                        view.getContext().startActivity(intent);
                    }
                    /// / checkTournamentDetails(view, intent, Integer.valueOf(teamsize));
                }
                break;
        }
    }

    //////checkTournamentDetails function

    private boolean checkTournamentDetails(View view, Intent intent, int teamsize) {
        boolean dtl_cmplt = false;

        return dtl_cmplt;
    }

    /////////////////////////tournamentsAsyncxTask

    class TouranmentDetailsAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        String id;
        HashMap<String, String> params = new HashMap<>();
        Context context;
        ViewPager findplaer;

        public TouranmentDetailsAsync(Context context, String id) {
            this.context = context;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.show();
            tourn_ly.setVisibility(View.GONE);
            params.put("id", id);


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.Tournament_Details, "GET", params);
                Log.d("JSON ", "------------" + json);
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
            pDialog.dismiss();
            tourn_ly.setVisibility(View.VISIBLE);

            if (json != null) {
                try {
                    if (json.getString("status").equalsIgnoreCase("1")) {
                        responseSucess = 1;
                        matchArray = json.optJSONArray("normal_stage");
                        t_namearray = json.optJSONArray("teamname");
                        knockout_array = json.optJSONArray("knockout");
                        quarter_final_array = json.optJSONArray("quarter_final");
                        Log.e("team size", ": " + t_namearray.length());
                        semi_array = json.optJSONArray("semi_final");
                        final_array = json.optJSONArray("final");
                        JSONObject itemObject = json.getJSONObject("items");
                        Util.showImage(TournamentDetailsActivity.this, itemObject.optString("tour_icon"), t_ImageView);
                        Util.showImage(TournamentDetailsActivity.this, itemObject.optString("img"), t_img1);
                        if (itemObject.optString("description").isEmpty() == false) {
                            desc_title.setText(Util.fromHtml("<b>" + "Description: " + "</b>"));
                            tou_desc.setText(Util.fromHtml(itemObject.optString("description")));
                        } else {
                            tou_desc.setVisibility(View.GONE);
                            desc_ly.setVisibility(View.GONE);
                        }
                        if (!itemObject.optString("about_tourna").equalsIgnoreCase("")
                                || !itemObject.optString("about_tourna").equalsIgnoreCase("null")) {
                            abt_title.setText(Util.fromHtml("<b>" + "About Tournament: " + "</b>"));
                            tou_abt.setText(Util.fromHtml(itemObject.optString("about_tourna")));
                        } else {
                            tou_abt.setVisibility(View.GONE);
                            tourn_ly.setVisibility(View.GONE);
                        }
                        tn_name.setText(itemObject.optString("name"));
                        t_name1.setText(Util.fromHtml("<b>" + "Sponsored By " + "</b>" + itemObject.optString("sponsor")));
                        loc_tou.setText(itemObject.optString("location"));
                        t_loc.setText(itemObject.optString("location"));

                        time.setText(itemObject.optString("prize"));
                        if (itemObject.optString("start_date").contains(" ")) {
                            String date[] = itemObject.optString("start_date").split(" ");
                            t_date.setText("From " + date[0]);
                            sh_date.setText(date[0]);

                        } else {
                            t_date.setText("From " + itemObject.optString("start_date"));
                            sh_date.setText(itemObject.optString("start_date"));

                        }
                        if (itemObject.getString("end_date").contains(" ")) {
                            String date[] = itemObject.optString("end_date").split(" ");
                            e_date.setText("To " + date[0]);
                            end_date.setText(date[0]);

                        } else {
                            e_date.setText("To " + itemObject.optString("end_date"));
                            end_date.setText(itemObject.optString("end_date"));
                        }
                        std_nm.setText(itemObject.optString("Stadium"));
                        st_add.setText(itemObject.optString("location"));

                        sports.setText(itemObject.optString("sport_type"));
                        sadd.setText(itemObject.optString("Stadium"));
                        sh_loc.setText(itemObject.optString("location"));
                        s_city.setText(itemObject.optString("city"));
                        s_state.setText(itemObject.optString("state"));

                        ch_aggrp.setText(itemObject.optString("age_group") + " (Years)");
                        ch_gend.setText(itemObject.optString("gender"));
                        shd_fes.setText(itemObject.optString("entry_fee"));
                        book_amt.setText(itemObject.optString("book_amount"));
                        team_size.setText(itemObject.optString("teamsize"));
                        if (!itemObject.optString("teamsize").isEmpty()
                                || !itemObject.optString("teamsize").equalsIgnoreCase("null")) {
                            teamsize = itemObject.optString("teamsize");
                        }
                        t_type.setText(itemObject.optString("game_type"));
                        pz_money.setText(itemObject.optString("prize"));
                        book_fees = itemObject.optString("book_amount");
                        entry_fees = itemObject.optString("entry_fee");
                        if (itemObject.optString("book_amount").equalsIgnoreCase("0") ||
                                itemObject.optString("book_amount").equalsIgnoreCase("null")
                                || itemObject.optString("book_amount").matches("")) {
                            entry_fees = itemObject.optString("full_amount");
                            shd_fes.setText(itemObject.optString("full_amount"));
                            book_amut1.setVisibility(View.GONE);
                            book_amt.setVisibility(View.GONE);
                            add_tourn.setVisibility(View.VISIBLE);
                            pay_lin.setVisibility(View.GONE);

                        } else {
                            pay_lin.setVisibility(View.VISIBLE);
                            add_tourn.setVisibility(View.GONE);
                          //  Toast.makeText(context, "matchArray: " + matchArray, Toast.LENGTH_SHORT).show();

                        }
                        if (itemObject.optString("teamsize").isEmpty()) {
                        } else if (Integer.valueOf(itemObject.optString("teamsize")) == t_namearray.length()) {
                            add_tourn.setClickable(false);
                            add_tourn.setText("Tournament booking close");
                            add_tourn.setVisibility(View.VISIBLE);
                            pay_lin.setVisibility(View.GONE);
                        }
                        //set facts adapter
                    } else {
                        responseSucess = 0;
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
