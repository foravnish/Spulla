package com.riseintech.spulla;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.riseintech.spulla.adapter.ChatPagerAdapter;
import com.riseintech.spulla.utils.Util;

/**
 * Created by Risein on 12/9/2016.
 */

public class ChatDetailsActivity extends AppCompatActivity {

    Toolbar toolbar_dchat;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_details);

        toolbar_dchat = (Toolbar) findViewById(R.id.toolbar_dchat);
        //*add toolbar to actionbar*//
        toolbar_dchat.setTitle("Chat Details");
        setSupportActionBar(toolbar_dchat);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_dchat);
        tabLayout.addTab(tabLayout.newTab().setText("Request To"));
        tabLayout.addTab(tabLayout.newTab().setText("Request from"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Irrelevant Profiles"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Util.ChatDtl_On = 1;
         viewPager = (ViewPager) findViewById(R.id.pager_dchat);
        final ChatPagerAdapter adapter = new ChatPagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override

            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public  boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        if (Util.Get_Rquest_InBg == 1) {
            viewPager.setCurrentItem(1);
            Util.Get_Rquest_InBg = 0;
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Util.Noti_Msg));
        super.onResume();
    }


   BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Util.Get_Rquest)) {
                //Getting message data
                String date_time = intent.getStringExtra("date_time");
                String message = intent.getStringExtra("message");
                String id = intent.getStringExtra("sender_id");
                viewPager.setCurrentItem(1);

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.ChatDtl_On = 0;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
