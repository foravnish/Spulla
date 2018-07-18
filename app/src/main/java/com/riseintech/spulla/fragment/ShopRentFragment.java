package com.riseintech.spulla.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.ShopItemDetailsActivity;
import com.riseintech.spulla.adapter.ShopItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/11/2016.
 */

public class ShopRentFragment extends Fragment {

    TextView gossipTxt;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ID_GOID = "ID_GOSSID";
    public static final String ID_TOPIC = "ID_TOPIC";
    private int mPage;
    private String sGssipTopic, sGossipId;
    GridView shop_gridview;
    ArrayList<HashMap<String, String>> itemList;


    public static ShopRentFragment newInstance(int page, String gossipId,
                                               String gossiptopic,
                                               ArrayList<HashMap<String, String>> itemList) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable("itemList", itemList);
        args.putString(ID_GOID, gossipId);
        args.putString(ID_TOPIC, gossiptopic);
        ShopRentFragment fragment = new ShopRentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("itemList");
        mPage = getArguments().getInt(ARG_PAGE);
        sGossipId = getArguments().getString(ID_GOID);
        sGssipTopic = getArguments().getString(ID_TOPIC);
//        Log.e("match id is>>>>", itemList.get(0).get("category_name"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopfragment, container, false);
        shop_gridview = (GridView) view.findViewById(R.id.shop_gridview);
        if (itemList.size() > 0) {
            shop_gridview.setAdapter(new ShopItemAdapter(getActivity(), itemList));
        }
        shop_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ShopItemDetailsActivity.class).putExtra("myshop", itemList).putExtra("pos", i));
            }
        });
        return view;
    }

}
