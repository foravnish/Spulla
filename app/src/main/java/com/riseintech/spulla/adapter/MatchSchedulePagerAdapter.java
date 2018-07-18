package com.riseintech.spulla.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.riseintech.spulla.fragment.QuarterfinalFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 11/8/2016.
 */

public class MatchSchedulePagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    ArrayList<ArrayList<HashMap<String, String>>> match_array_List = new ArrayList<ArrayList<HashMap<String, String>>>();

    public MatchSchedulePagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        this.context = context;

    }


    @Override
    public Fragment getItem(int position) {
        //Log.e("position getItem", ": " + position);
        return QuarterfinalFragment.newInstance(match_array_List.get(position), mFragmentTitleList.get(position));
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title, ArrayList<HashMap<String, String>> matchlist) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        match_array_List.add(matchlist);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Log.e("position gettitle", ": " + position);
        return mFragmentTitleList.get(position);
    }
}

