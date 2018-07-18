package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.riseintech.spulla.adapter.FactDetailsPagerAdapter;
import com.riseintech.spulla.adapter.FactsDetailsAdapter;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class FactDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    int pos;
    ViewPager facts_pager;
    ArrayList<HashMap<String, String>> factList;
    ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists;
    public static int currentpos;
    private int mCurrentPosition;
    private int mScrollState;
    Toolbar toolbar_facts;
    ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact_details);
        toolbar_facts = (Toolbar) findViewById(R.id.toolbar_facts);
        toolbar_facts.setTitle(R.string.facts);
        setSupportActionBar(toolbar_facts);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        factList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("cmmt_list");
        total_cmmtLists = (ArrayList<ArrayList<HashMap<String, String>>>) getIntent().getSerializableExtra("total_cmmnts");
        pos = getIntent().getIntExtra("pos", 0);
        facts_pager = (ViewPager) findViewById(R.id.facts_pager);

        if (factList != null) {
            // facts_pager.setAdapter(new FactsDetailsAdapter(FactDetailsActivity.this, factList, total_cmmtLists.get(0)));

            facts_pager.setAdapter(new FactDetailsPagerAdapter(getSupportFragmentManager(), factList, "facts"));
            facts_pager.setCurrentItem(pos, true);
        }
        facts_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            ArrayList<HashMap<String, String>> cmtlist = new ArrayList<HashMap<String, String>>();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentpos = position;
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                handleScrollState(state);
                mScrollState = state;
            }
        });
    }
///////////////////function for circular swiping

    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setNextItemIfNeeded();
        }
    }

    private void setNextItemIfNeeded() {
        if (!isScrollStateSettling()) {
            handleSetNextItem();
        }
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = facts_pager.getAdapter().getCount() - 1;
        if (mCurrentPosition == 0) {
            facts_pager.setCurrentItem(lastPosition, false);
        } else if (mCurrentPosition == lastPosition) {
            facts_pager.setCurrentItem(0, false);
        }
    }

    /////////////////////
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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
    ///////////////////

    private class GetCommentDetails extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String ctype, ctype_id;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        ArrayList<HashMap<String, String>> hashMaps;
        //time_format 2016-10-24 04:39:18
        ListView listView;

        public GetCommentDetails(Context context, String ctype, String ctype_id,
                                 ArrayList<HashMap<String, String>> hashMaps) {
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
            pDialog.show();

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
            pDialog.dismiss();
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
                    facts_pager.setAdapter(new FactsDetailsAdapter(FactDetailsActivity.this, factList, hashMaps));
                    facts_pager.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Comments list not get", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
/////////////////////////////////////
    }
}

