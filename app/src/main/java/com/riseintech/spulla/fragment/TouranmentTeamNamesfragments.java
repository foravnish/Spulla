package com.riseintech.spulla.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.adapter.TouTeamNamesAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/15/2016.
 */

public class TouranmentTeamNamesfragments extends Fragment {
    ListView listView;
    ArrayList<HashMap<String, String>> matchSchedulelist;
    private JSONArray tname_array;

    public TouranmentTeamNamesfragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            tname_array = new JSONArray(getArguments().getString("tname_array"));
            //Log.w("final match array", ": " + tname_array);
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
        if (tname_array != null) {
            {
                for (int i = 0; i < tname_array.length(); i++) {
                    boolean isBlank = false;
                    HashMap<String, String> map = new HashMap<>();

                    try {
                        if (tname_array.getJSONObject(i).getString("id").equalsIgnoreCase("null")
                                || tname_array.getJSONObject(i).getString("id").isEmpty()) {
                            isBlank = true;
                        } else if (tname_array.getJSONObject(i).getString("team_name").equalsIgnoreCase("null")
                                || tname_array.getJSONObject(i).getString("team_name").isEmpty()) {
                            isBlank = true;

                        }

                        if (isBlank) {
                            map.put("id", getActivity().getString(R.string.notavail));
                            map.put("team_name", getActivity().getString(R.string.notavail));
                            map.put("team_icon", getActivity().getString(R.string.notavail));


                        } else {
                            map.put("id", tname_array.getJSONObject(i).getString("id"));
                            map.put("team_name", tname_array.getJSONObject(i).getString("team_name"));
                            map.put("team_icon",  tname_array.getJSONObject(i).getString("team_icon"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    matchSchedulelist.add(map);
                }
            }
        }
        TouTeamNamesAdapter temNamesAdapter = new TouTeamNamesAdapter(getActivity(), matchSchedulelist);
        listView.setAdapter(temNamesAdapter);
        temNamesAdapter.notifyDataSetChanged();
        return view;
    }
}
