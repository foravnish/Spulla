package com.riseintech.spulla.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.adapter.MatchScheduleAapter;
import com.riseintech.spulla.adapter.TouTeamNamesAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/9/2016.
 */

public class QuarterfinalFragment extends Fragment {
    ListView listView;
    ArrayList<HashMap<String, String>> matchSchedulelist;
    String title = "";

    public QuarterfinalFragment() {
        // Required empty public constructor
    }

    public static QuarterfinalFragment newInstance(ArrayList<HashMap<String, String>> itemList, String type) {
        Bundle args = new Bundle();
        args.putSerializable("itemList", itemList);
        args.putString("title", type);
        QuarterfinalFragment fragment = new QuarterfinalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchSchedulelist = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("itemList");
        title = getArguments().getString("title");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.match_schedule, container, false);
        listView = (ListView) view.findViewById(R.id.match_list);
        /*matchSchedulelist = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", "" + i);
            matchSchedulelist.add(map);
        }*/
        if (title.equalsIgnoreCase("Teams Name")) {
            TouTeamNamesAdapter teamNamesAdapter = new TouTeamNamesAdapter(getActivity(), matchSchedulelist);
            listView.setAdapter(teamNamesAdapter);
            teamNamesAdapter.notifyDataSetChanged();
        } else {
            MatchScheduleAapter matchScheduleAapter = new MatchScheduleAapter(getActivity(), matchSchedulelist);
            listView.setAdapter(matchScheduleAapter);
            matchScheduleAapter.notifyDataSetChanged();
        }
        return view;
    }
}
