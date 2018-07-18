package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/15/2016.
 */

public class TouTeamNamesAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> hashMaps;
    Context context;

    public TouTeamNamesAdapter(Context context, ArrayList<HashMap<String, String>> hashMaps) {
        this.hashMaps = hashMaps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return hashMaps.size();
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
        view = inflater.inflate(R.layout.teamnamesadapter, null);
        ImageView teamIcon = (ImageView) view.findViewById(R.id.icon);
        TextView t_name = (TextView) view.findViewById(R.id.t_name);
        t_name.setText(hashMaps.get(i).get("team_name"));
        //Log.e("team icon", ": " + hashMaps.get(i).get("team_icon"));
        Util.showImage(context, hashMaps.get(i).get("team_icon"), teamIcon);

        return view;
    }
}

