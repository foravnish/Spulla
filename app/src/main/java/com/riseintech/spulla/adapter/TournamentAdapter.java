package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.FindPlayer;
import com.riseintech.spulla.R;
import com.riseintech.spulla.TournamentDetailsActivity;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adm on 9/13/2016.
 */
public class TournamentAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> tournamentItems;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView txtTname, txtTsname, txtTtime, p_h_title;
    ImageView logotour;
    LinearLayout tnmt_lin;
    public static String t_id;

    public TournamentAdapter(Context activity, ArrayList<HashMap<String, String>> tournamentItems) {
        this.mContext = activity;
        this.tournamentItems = tournamentItems;
    }

    @Override
    public int getCount() {
        return tournamentItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tournament_items, container, false);
        /*Define txtLoadFail from the layout*/
        logotour = (ImageView) view.findViewById(R.id.logotour);
        txtTname = (TextView) view.findViewById(R.id.txttname);
        txtTsname = (TextView) view.findViewById(R.id.txtsname);
        txtTtime = (TextView) view.findViewById(R.id.txtsTime);
        txtTname.setText(tournamentItems.get(position).get("t_name"));
        txtTsname.setText(tournamentItems.get(position).get("category"));
        Util.showImage(mContext,tournamentItems.get(position).get("t_icon"),logotour);
        //  txtTtime.setText(tournamentItems.get(position).get("Schedule"));
        //p_h_title = (TextView) view.findViewById(R.id.p_h_title);
        // p_h_menu = (ImageView) view.findViewById(R.id.p_h_menu);
        tnmt_lin = (LinearLayout) view.findViewById(R.id.tnmt_lin);
        tnmt_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyPreference.loadIsAdd(mContext) == 1) {
                    t_id = tournamentItems.get(position).get("id");
                    view.getContext().startActivity(new Intent(view.getContext(),
                            TournamentDetailsActivity.class));
                } else {
                    Toast.makeText(mContext, "First Add Player Details", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(view.getContext(), FindPlayer.class);
                    view.getContext().startActivity(in);
                }
            }

        });
        txtTtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyPreference.loadIsAdd(mContext) == 1) {
                    t_id = tournamentItems.get(position).get("id");
                    view.getContext().startActivity(new Intent(view.getContext(),
                            TournamentDetailsActivity.class));
                } else {
                    Toast.makeText(mContext, "First Add Player Details", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(view.getContext(), FindPlayer.class);
                    view.getContext().startActivity(in);
                }
            }

        });
        //p_h_title.setText(mContext.getString(R.string.s_tournament));

        //view.setTag(gossipItems);
        // Add the newly created View to the ViewPager
        container.addView(view);
        //Log.i(TAG, "instantiateItem() [position: " + position + "]" + " childCount:" + tournamentItems.get(position).get("Schedule"));
        // Return the View
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}