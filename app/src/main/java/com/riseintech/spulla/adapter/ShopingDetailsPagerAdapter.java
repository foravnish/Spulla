package com.riseintech.spulla.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.riseintech.spulla.fragment.ShopRentFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 11/11/2016.
 */

public class ShopingDetailsPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    ArrayList<ArrayList<HashMap<String, String>>> itemList = new ArrayList<ArrayList<HashMap<String, String>>>();

    public ShopingDetailsPagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        //Log.e("position getItem", ":>>> " + itemList.get(position).size());
        return ShopRentFragment.newInstance(position, mFragmentTitleList.get(position).toString(),
                "sports", itemList.get(position));
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title, ArrayList<HashMap<String, String>> itemlist) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        itemList.add(itemlist);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Log.e("position gettitle", ": " + position);
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

    }
}
