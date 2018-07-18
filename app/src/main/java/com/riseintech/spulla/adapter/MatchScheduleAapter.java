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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/2/2016.
 */

public class MatchScheduleAapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> matchSchedulelist;

    public MatchScheduleAapter(Context context, ArrayList<HashMap<String, String>> sportList) {
        this.context = context;
        this.matchSchedulelist = sportList;

    }

    @Override
    public int getCount() {
        return matchSchedulelist.size();
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
        view = inflater.inflate(R.layout.match_schedule_adapter, null);
        ImageView team1_img = (ImageView) view.findViewById(R.id.team1_img);
        ImageView team2_img = (ImageView) view.findViewById(R.id.team2_img);
        TextView match_statusteam1 = (TextView) view.findViewById(R.id.match_statusteam1);
        //TextView match_statusteam2 = (TextView) view.findViewById(R.id.match_statusteam2);

        TextView team2_name = (TextView) view.findViewById(R.id.team2_name);
        TextView team_name = (TextView) view.findViewById(R.id.team_name);
        TextView match_date = (TextView) view.findViewById(R.id.match_date);
        TextView match_time = (TextView) view.findViewById(R.id.match_time);
        TextView match_vennue = (TextView) view.findViewById(R.id.match_vennue);
        if (!matchSchedulelist.get(i).get("team1_icon").equalsIgnoreCase("")) {
            Picasso.with(context).load(matchSchedulelist.get(i).get("team1_icon"))
                    .placeholder(R.color.grey_light).error(R.drawable.tournament).into(team1_img);
        }
        if (!matchSchedulelist.get(i).get("team2_icon").equalsIgnoreCase("")) {
            Picasso.with(context).load(matchSchedulelist.get(i).get("team2_icon"))
                    .placeholder(R.color.grey_light).error(R.drawable.tournament).into(team2_img);
        }
        if (!matchSchedulelist.get(i).get("team_name1").equalsIgnoreCase("null")) {
            team_name.setText(matchSchedulelist.get(i).get("team_name1"));
        }
        if (!matchSchedulelist.get(i).get("team_name2").equalsIgnoreCase("null")) {
            team2_name.setText(matchSchedulelist.get(i).get("team_name2"));
        }
        if (!matchSchedulelist.get(i).get("location").equalsIgnoreCase("null")) {
            match_vennue.setText(matchSchedulelist.get(i).get("location"));
        }
        match_date.setText(matchSchedulelist.get(i).get("date"));
        match_time.setText(matchSchedulelist.get(i).get("time"));


        if (matchSchedulelist.get(i).get("winner").matches("")
                || matchSchedulelist.get(i).get("winner").equalsIgnoreCase("null")) {
            match_statusteam1.setVisibility(View.GONE);
        } else {
            match_statusteam1.setText(matchSchedulelist.get(i).get("winner"));
        }

        return view;
    }
}
