package com.riseintech.spulla.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.riseintech.spulla.fragment.FactsFragments;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/10/2016.
 */

public class FactDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<HashMap<String, String>> factslist;
    String type;

    public FactDetailsPagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> factslist, String type) {
        super(fm);
        this.factslist = factslist;
        this.type=type;
        Log.d("fgdfghdfgdhd",factslist.toString());
    }


    @Override
    public int getCount() {
        return factslist.size();
    }

    @Override
    public Fragment getItem(int position) {

        return FactsFragments.newInstance(position, factslist,type);
    }

   /*@Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        *//*return tabTitles[position];*//*
        return pagerItems.get(position).get("title");
    }*/

}
