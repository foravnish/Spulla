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
 * Created by user on 11/9/2016.
 */

public class SemiFinalFragment extends Fragment {
    ListView listView;
    ArrayList<HashMap<String, String>> matchSchedulelist;
    JSONArray semi_array;

    public SemiFinalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            semi_array = new JSONArray(getArguments().getString("semifinal_array"));
            //Log.e("semi array", ": " + semi_array);
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
        if (semi_array != null) {

            {
                for (int i = 0; i < semi_array.length(); i++) {
                    boolean isBlank = false;
                    HashMap<String, String> map = new HashMap<>();

                    try {
                        if (semi_array.getJSONObject(i).getString("id").equalsIgnoreCase("null")
                                || semi_array.getJSONObject(i).getString("id").isEmpty()) {
                            isBlank = true;
                        } else if (semi_array.getJSONObject(i).getString("team_name1").equalsIgnoreCase("null")
                                || semi_array.getJSONObject(i).getString("team_name1").isEmpty()) {
                            isBlank = true;

                        } else if (semi_array.getJSONObject(i).getString("team_name2").equalsIgnoreCase("null")
                                || semi_array.getJSONObject(i).getString("team_name2").isEmpty()) {
                            isBlank = true;

                        } else if (semi_array.getJSONObject(i).getString("date").equalsIgnoreCase("null")
                                || semi_array.getJSONObject(i).getString("date").isEmpty()) {
                            isBlank = true;

                        } else if (semi_array.getJSONObject(i).getString("location").equalsIgnoreCase("null")
                                || semi_array.getJSONObject(i).getString("location").isEmpty()) {
                            isBlank = true;

                        } else if (semi_array.getJSONObject(i).getString("time").equalsIgnoreCase("null")
                                || semi_array.getJSONObject(i).getString("time").isEmpty()) {
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
                            map.put("id", semi_array.getJSONObject(i).getString("id"));
                            map.put("team_name1", semi_array.getJSONObject(i).getString("team_name1"));
                            map.put("team_name2", semi_array.getJSONObject(i).getString("team_name2"));
                            map.put("date", semi_array.getJSONObject(i).getString("date"));
                            map.put("location", semi_array.getJSONObject(i).getString("location"));
                            map.put("time", semi_array.getJSONObject(i).getString("time"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    matchSchedulelist.add(map);
                }
            }

        }
        MatchScheduleAapter matchScheduleAapter = new MatchScheduleAapter(getActivity(), matchSchedulelist);
        listView.setAdapter(matchScheduleAapter);
        matchScheduleAapter.notifyDataSetChanged();
        return view;
    }
}
