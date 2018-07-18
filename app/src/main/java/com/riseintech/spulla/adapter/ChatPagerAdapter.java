package com.riseintech.spulla.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.riseintech.spulla.fragment.ChatDetailFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein on 12/9/2016.
 */

public class ChatPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private ArrayList<HashMap<String, String>> pagerItems;
    final int PAGE_COUNT = 11;
    private String tabTitles[] = new String[]{"Request To",
            "Request from", "Chat","IrrelevantProfiles"};

    public ChatPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return ChatDetailFragment.newInstance(position, tabTitles[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        /*return tabTitles[position];*/
        return tabTitles.toString();
    }
}

