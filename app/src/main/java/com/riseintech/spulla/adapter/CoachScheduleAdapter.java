package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riseintech.spulla.BookCoach;
import com.riseintech.spulla.CochScheduleActivity;
import com.riseintech.spulla.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 10/7/2016.
 */

public class CoachScheduleAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> scheduleItems;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView txtTname, txtTsname, txtTtime, p_h_title, shd_edate;
    ImageView p_h_menu;
    String[] strings;
    Button pay_book_amt, pay_full_amt;
    boolean isTou_Adapter;
    String fees_amount = "0";
    String full_fees = "0";
    LinearLayout day_lin, time_lin, pay_lin;

    public CoachScheduleAdapter(Context activity, ArrayList<HashMap<String, String>> scheduleItems, boolean isTou_Adapter) {
        this.mContext = activity;
        this.scheduleItems = scheduleItems;
        this.isTou_Adapter = isTou_Adapter;
    }

    @Override
    public int getCount() {
        return scheduleItems.size();
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
        TextView schd_dts = (TextView) view.findViewById(R.id.schd_dts);
        TextView sh_date = (TextView) view.findViewById(R.id.sh_date);
        shd_edate = (TextView) view.findViewById(R.id.shd_edate);
        TextView sh_loc = (TextView) view.findViewById(R.id.sh_loc);
        TextView shd_pgm = (TextView) view.findViewById(R.id.shd_pgm);
        TextView shd_pgm1 = (TextView) view.findViewById(R.id.shd_pgm1);
        pay_lin = (LinearLayout) view.findViewById(R.id.pay_lin);
        time_lin = (LinearLayout) view.findViewById(R.id.time_lin);
        day_lin = (LinearLayout) view.findViewById(R.id.day_lin);
        pay_full_amt = (Button) view.findViewById(R.id.pay_full_amt);
        pay_book_amt = (Button) view.findViewById(R.id.pay_book_amt);

        TextView shd_fes = (TextView) view.findViewById(R.id.shd_fes);
        TextView ch_gend = (TextView) view.findViewById(R.id.ch_gend);
        TextView ch_aggrp = (TextView) view.findViewById(R.id.ch_aggrp);
        TextView book_amut = (TextView) view.findViewById(R.id.book_amut);
        LinearLayout bkamt_lin = (LinearLayout) view.findViewById(R.id.bkamt_lin);
        int schedule = CochScheduleActivity.currentpos + 1;
        //  schd_dts.setText(""+schedule);
        //Log.e("scheduleItems array", ">>" + scheduleItems.get(0).get("schedule"));
        shd_edate.setText(scheduleItems.get(position).get("schedule_end_date"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFeeDetails(position);
                Intent in = new Intent(mContext, BookCoach.class);
                in.putExtra("fee_amount", full_fees);
                in.putExtra("fee_type", "tournament");
                in.putExtra("id", "1");
                in.putExtra("full_fees", full_fees);
                mContext.startActivity(in);
            }
        });
        pay_book_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFeeDetails(position);
                Intent in = new Intent(mContext, BookCoach.class);
                in.putExtra("fee_amount", fees_amount);
                in.putExtra("fee_type", "booking fee");
                in.putExtra("id", "1");
                in.putExtra("full_fees", full_fees);
                mContext.startActivity(in);
            }
        });
        pay_full_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFeeDetails(position);
                Intent in = new Intent(mContext, BookCoach.class);
                in.putExtra("fee_amount", full_fees);
                in.putExtra("fee_type", "tournament");
                in.putExtra("id", "1");
                in.putExtra("full_fees", full_fees);
                mContext.startActivity(in);
            }
        });
        //if (isTou_Adapter) {

        //button.setCompoundDrawablesWithIntrinsicBounds( R.drawable.addicon, 0, 0, 0);
        //Log.e("scheduleItemsarraysize", ">>" + scheduleItems.size());

        button.setText(mContext.getString(R.string.join_coach));
        // String locinUper =
        sh_loc.setText(scheduleItems.get(position).get("schedule_location"));
        if (scheduleItems.get(position).get("schedule_gender").length() > 1) {
            String inUpercase = scheduleItems.get(position).get("schedule_gender").substring(0, 1).toUpperCase() + scheduleItems.get(position).get("schedule_gender").substring(1);
            ch_gend.setText(inUpercase);
        }

        if (scheduleItems.get(position).get("off").equals("0%") || scheduleItems.get(position).get("off").equals("0")
                || scheduleItems.get(position).get("off").equals("null")) {
            // schd_dts.setText("Schedule : " + scheduleItems.get(position).get("schedule"));
            sh_date.setText(scheduleItems.get(position).get("schedule_start_date"));
            ch_aggrp.setText(scheduleItems.get(position).get("min") + " - " + scheduleItems.get(position).get("max") + " (Years)");
            shd_fes.setTextColor(mContext.getResources().getColor(R.color.black));
            shd_fes.setText(mContext.getResources().getString(R.string.Rs) + " " + scheduleItems.get(position).get("full_fees"));

            shd_pgm.setVisibility(View.GONE);
            shd_pgm1.setVisibility(View.GONE);
            book_amut.setText(mContext.getResources().getString(R.string.Rs) + " " + scheduleItems.get(position).get("pay"));
            //Toast.makeText(mContext, "in if>> " + full_fees, Toast.LENGTH_LONG).show();

        } else {
            //Log.e("scheduleItems array", ">>" + scheduleItems.get(0).get("schedule"));
            shd_pgm.setVisibility(View.VISIBLE);
            shd_pgm1.setVisibility(View.VISIBLE);
            // schd_dts.setText("Schedule : " + scheduleItems.get(position).get("schedule"));
            sh_date.setText(scheduleItems.get(position).get("schedule_start_date"));
            ch_aggrp.setText(scheduleItems.get(position).get("min") + " - " + scheduleItems.get(position).get("max") + " (Years)");
            shd_fes.setText(mContext.getResources().getString(R.string.Rs) + " " + scheduleItems.get(position).get("full_fees"));
            shd_fes.setPaintFlags(shd_fes.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            shd_pgm.setText(scheduleItems.get(position).get("off") + " " + " - " + mContext.getResources().getString(R.string.Rs) + " " + scheduleItems.get(position).get("total"));
            book_amut.setText(mContext.getResources().getString(R.string.Rs) + " " + scheduleItems.get(position).get("pay"));
            //   Toast.makeText(mContext, "in else>>" + full_fees, Toast.LENGTH_LONG).show();
        }

        if (scheduleItems.get(position).get("day").contains(",")) {
            String[] daysize = scheduleItems.get(position).get("day").split(",");
            //Log.e("day size", "size: " + daysize.length + ",  " + daysize);
            for (int i = 0; i < daysize.length; i++) {
                TextView day = new TextView(mContext);
                day.setText(daysize[i]);
                day.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                day.setPadding(5, 5, 5, 5);
                day.setGravity(Gravity.CENTER);
                day_lin.addView(day);
                //Log.e("day", ": " + daysize[i] + " , " + i);
                //Toast.makeText(mContext,"day in if>>",Toast.LENGTH_LONG).show();

            }
        } else if (!scheduleItems.get(position).get("day").contains(",")) {

            TextView day = new TextView(mContext);
            day.setText(scheduleItems.get(position).get("day"));
            day.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            day.setPadding(5, 5, 5, 5);
            day.setGravity(Gravity.CENTER);
            day_lin.addView(day);
            //Log.e("day", ": " + daysize[i] + " , " + i);
            //Toast.makeText(mContext,"day in elseif>>",Toast.LENGTH_LONG).show();


        }
        if (scheduleItems.get(position).get("time_in").contains(",") && scheduleItems.get(position).get("time_out").contains(",")) {
            String[] timein_size = scheduleItems.get(position).get("time_in").split(",");
            String[] time_out = scheduleItems.get(position).get("time_out").split(",");
            Log.e("time in", "suze: " + timein_size.length + ", " + timein_size);
            Log.e("time out", "size: " + time_out.length + ", " + time_out);

            for (int j = 0; j < timein_size.length; j++) {
                TextView time = new TextView(mContext);
                time.setText(timein_size[j] + "-" + time_out[j]);
                time.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                time.setPadding(5, 5, 5, 5);
                time.setGravity(Gravity.CENTER);
                time_lin.addView(time);
                Log.e("Time", "int: " + timein_size[j] + ", out: " + time_out[j] + " , " + j);

            }
        } else if (!scheduleItems.get(position).get("time_in").contains(",") && !scheduleItems.get(position).get("time_out").contains(",")) {

            TextView time = new TextView(mContext);
            time.setText(scheduleItems.get(position).get("time_in") + " - " + scheduleItems.get(position).get("time_out"));
            time.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            time.setPadding(5, 0, 5, 0);
            time.setGravity(Gravity.CENTER);
            time_lin.addView(time);
        }

        /////////////////condition for  check pay booking amount available or not

        if (scheduleItems.get(position).get("pay").equalsIgnoreCase("")
                || scheduleItems.get(position).get("pay").equalsIgnoreCase("0")) {
            pay_lin.setVisibility(View.GONE);
            bkamt_lin.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        } else {
            bkamt_lin.setVisibility(View.VISIBLE);
            pay_lin.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }
        //////////////////////////////////////

        container.addView(view);
        // Return the View

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void getFeeDetails(int position) {
        //Toast.makeText(mContext, "Position " + position, Toast.LENGTH_SHORT).show();

        if (scheduleItems.get(position).get("off").equals("0%") || scheduleItems.get(position).get("off").equals("0")
                || scheduleItems.get(position).get("off").equals("null")) {
            full_fees = scheduleItems.get(position).get("full_fees");
            fees_amount = scheduleItems.get(position).get("pay");
        } else {
            full_fees = scheduleItems.get(position).get("total");
            fees_amount = scheduleItems.get(position).get("pay");
        }
      //  Toast.makeText(mContext, "Full Fess " + full_fees + ", pay fees " + fees_amount, Toast.LENGTH_LONG).show();
    }

    ;
}
