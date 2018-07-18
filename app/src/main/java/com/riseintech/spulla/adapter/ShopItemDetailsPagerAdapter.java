package com.riseintech.spulla.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.riseintech.spulla.fragment.ShopItemDetailFragments;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein on 11/28/2016.
 */
public class ShopItemDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<HashMap<String, String>> shoplist;
    String type;

    public ShopItemDetailsPagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> shoplist, String type) {
        super(fm);
        this.shoplist = shoplist;
        this.type=type;
    }


    @Override
    public int getCount() {
        return shoplist.size();
    }


    @Override
    public Fragment getItem(int position) {

        return ShopItemDetailFragments.newInstance(position, shoplist,type);
    }

   /*@Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        *//*return tabTitles[position];*//*
        return pagerItems.get(position).get("title");
    }*/

}
