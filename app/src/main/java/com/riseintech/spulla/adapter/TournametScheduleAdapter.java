package com.riseintech.spulla.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.BookCoach;
import com.riseintech.spulla.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein on 10/28/2016.
 */

public class TournametScheduleAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> tournamentItems;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView txtTname, txtTsname, txtTtime, p_h_title;
    ImageView p_h_menu;
    String[] strings;
    boolean isTou_Adapter;

    public TournametScheduleAdapter(Context activity, String[] strings, boolean isTou_Adapter) {
        this.mContext = activity;
        this.strings = strings;
        this.isTou_Adapter = isTou_Adapter;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coach_des_adapter, container, false);
        Button button = (Button) view.findViewById(R.id.book);
        //TextView ch_time = (TextView) view.findViewById(R.id.ch_time);
        TextView sh_time = (TextView) view.findViewById(R.id.sh_date);
        TextView sh_loc = (TextView) view.findViewById(R.id.sh_loc);
        //TextView days = (TextView) view.findViewById(R.id.days);
        TextView shd_pgm = (TextView) view.findViewById(R.id.shd_pgm);
        TextView shd_fes = (TextView) view.findViewById(R.id.shd_fes);
        TextView ch_gend = (TextView) view.findViewById(R.id.ch_gend);
        TextView ch_aggrp = (TextView) view.findViewById(R.id.ch_aggrp);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mContext,BookCoach.class);
                mContext.startActivity(in);
            }
        });
        if (isTou_Adapter) {
            button.setCompoundDrawablesWithIntrinsicBounds( R.drawable.addicon, 0, 0, 0);
            button.setText(mContext.getString(R.string.join_tournament));
           // ch_time.setText(mContext.getString(R.string.time));
            sh_time.setText(mContext.getString(R.string.date));
            sh_loc.setText(mContext.getString(R.string.location));
            shd_pgm.setText(mContext.getString(R.string.schedule));
            shd_fes.setText(mContext.getString(R.string.program));
           // days.setVisibility(View.GONE);
            ch_aggrp.setVisibility(View.GONE);
            ch_gend.setVisibility(View.GONE);


        }
        container.addView(view);
        // Return the View

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
