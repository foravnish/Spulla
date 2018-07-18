package com.riseintech.spulla;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.riseintech.spulla.adapter.FactDetailsPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class GossipActivity extends AppCompatActivity implements View.OnClickListener {


    ArrayList<HashMap<String, String>> gsp_list;
    ViewPager goss_pager;
    int pos;
    public static int currentpos;
    private int mCurrentPosition;
    private int mScrollState;
    ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists;
    Toolbar toolbar_gossip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gossip);
        gsp_list = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("gossip_list");
        Log.d("gdfhbfhfghfg",gsp_list.toString());


        total_cmmtLists = (ArrayList<ArrayList<HashMap<String, String>>>) getIntent().getSerializableExtra("total_cmmnts");
        toolbar_gossip = (Toolbar) findViewById(R.id.toolbar_gossip);
        toolbar_gossip.setTitle("Gossips");
        setSupportActionBar(toolbar_gossip);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pos = getIntent().getIntExtra("pos", 0);
        goss_pager = (ViewPager) findViewById(R.id.goss_pager);

        if (gsp_list != null) {
            //goss_pager.setAdapter(new GossipDetailsAdapter(GossipActivity.this, gsp_list, total_cmmtLists));
            goss_pager.setAdapter(new FactDetailsPagerAdapter(getSupportFragmentManager(), gsp_list, "gosip"));

            goss_pager.setCurrentItem(pos, true);
        }
        goss_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        final int lastPosition = goss_pager.getAdapter().getCount() - 1;
        if (mCurrentPosition == 0) {
            goss_pager.setCurrentItem(lastPosition, false);
        } else if (mCurrentPosition == lastPosition) {
            goss_pager.setCurrentItem(0, false);
        }
    }

    /////////////////////

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

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
}
