package com.riseintech.spulla.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riseintech.spulla.R;

/**
 * Created by adm on 9/12/2016.
 */
public class GossipFragment extends Fragment {

    TextView gossipTxt;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ID_GOID = "ID_GOSSID";
    public static final String ID_TOPIC = "ID_TOPIC";
    private int mPage;
    private String sGssipTopic,sGossipId;

    public static GossipFragment newInstance(int page , String gossipId,String gossiptopic) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ID_GOID,gossipId);
        args.putString(ID_TOPIC,gossiptopic);
        GossipFragment fragment = new GossipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        sGossipId=getArguments().getString(ID_GOID);
        sGssipTopic=getArguments().getString(ID_TOPIC);
        //Log.e("match id is>>>>", sGssipTopic);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.gossip_fragment, container, false);
       /* tvLabel = (TextView) view.findViewById(R.id.team1);
        tvLabe2 = (TextView) view.findViewById(R.id.team2);
        tvscore = (TextView) view.findViewById(R.id. txtscore);
        tvscore1 = (TextView) view.findViewById(R.id. txtscore1);*/

        gossipTxt=(TextView) view.findViewById(R.id.txtTopic);
        gossipTxt.setText(sGssipTopic);

        return view;
    }



}
