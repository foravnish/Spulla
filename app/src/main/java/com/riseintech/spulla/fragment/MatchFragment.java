package com.riseintech.spulla.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.adapter.MatchScheduleAapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/8/2016.
 */

public class MatchFragment extends Fragment {
    ListView listView;
    ArrayList<HashMap<String, String>> matchSchedulelist;
    public JSONArray matchArray;

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            matchArray = new JSONArray(getArguments().getString("match_array"));
            //Log.w("name", ": " + matchArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.match_schedule, container, false);
        listView = (ListView) view.findViewById(R.id.match_list);
        matchSchedulelist = new ArrayList<>();
        if (matchArray != null) {
            for (int i = 0; i < matchArray.length(); i++) {
                boolean isBlank = false;
                HashMap<String, String> map = new HashMap<>();

                try {
                    if (matchArray.getJSONObject(i).getString("id").equalsIgnoreCase("null")
                            || matchArray.getJSONObject(i).getString("id").isEmpty()) {
                        isBlank = true;
                    } else if (matchArray.getJSONObject(i).getString("team_name1").equalsIgnoreCase("null")
                            || matchArray.getJSONObject(i).getString("team_name1").isEmpty()) {
                        isBlank = true;

                    } else if (matchArray.getJSONObject(i).getString("team_name2").equalsIgnoreCase("null")
                            || matchArray.getJSONObject(i).getString("team_name2").isEmpty()) {
                        isBlank = true;

                    } else if (matchArray.getJSONObject(i).getString("date").equalsIgnoreCase("null")
                            || matchArray.getJSONObject(i).getString("date").isEmpty()) {
                        isBlank = true;

                    } else if (matchArray.getJSONObject(i).getString("location").equalsIgnoreCase("null")
                            || matchArray.getJSONObject(i).getString("location").isEmpty()) {
                        isBlank = true;

                    } else if (matchArray.getJSONObject(i).getString("time").equalsIgnoreCase("null")
                            || matchArray.getJSONObject(i).getString("time").isEmpty()) {
                        isBlank = true;
                    }

                    if (isBlank) {
                        map.put("id", getActivity().getString(R.string.notavail));
                        map.put("team_name1", getActivity().getString(R.string.notavail));
                        map.put("team_name2", getActivity().getString(R.string.notavail));
                        map.put("date", getActivity().getString(R.string.notavail));
                        map.put("location", getActivity().getString(R.string.notavail));
                        map.put("time", getActivity().getString(R.string.notavail));

                    } else {
                        map.put("id", matchArray.getJSONObject(i).getString("id"));
                        map.put("team_name1", matchArray.getJSONObject(i).getString("team_name1"));
                        map.put("team_name2", matchArray.getJSONObject(i).getString("team_name2"));
                        map.put("date", matchArray.getJSONObject(i).getString("date"));
                        map.put("location", matchArray.getJSONObject(i).getString("location"));
                        map.put("time", matchArray.getJSONObject(i).getString("time"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                matchSchedulelist.add(map);
            }
        }
        MatchScheduleAapter matchScheduleAapter = new MatchScheduleAapter(getActivity(), matchSchedulelist);
        listView.setAdapter(matchScheduleAapter);

        return view;
    }

}
