package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein Tech on 10/25/2016.
 */

public class SportCatAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> sportList;

    public SportCatAdapter(Context context, ArrayList<HashMap<String, String>> sportList) {
        this.context = context;
        this.sportList = sportList;

    }

    @Override
    public int getCount() {
        return sportList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.sportcatadapter, null);

        TextView txt_scat = (TextView) view.findViewById(R.id.txt_scat);
        if (Util.isSportBgChn) {
            txt_scat.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_light_blue));
        }


        txt_scat.setText(sportList.get(i).get("category_name"));

        return view;
    }
}