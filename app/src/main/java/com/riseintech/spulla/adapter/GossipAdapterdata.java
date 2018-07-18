package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.GossipActivity;
import com.riseintech.spulla.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adm on 9/13/2016.
 */
public class GossipAdapterdata extends PagerAdapter {
    private ArrayList<HashMap<String, String>> gossipItems;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView txtgossip;
    ImageView p_h_menu, gossip_icon;
    TextView gossip_iconname, gsp_rm;
    ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists;

    public GossipAdapterdata(Context activity, ArrayList<HashMap<String, String>> gossipItems,
                             ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists) {
        this.mContext = activity;
        this.total_cmmtLists = total_cmmtLists;
        this.gossipItems = gossipItems;
        Log.d("gfdfgddfhgdfhd","true");
    }

    @Override
    public int getCount() {
        return gossipItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gossip_fragment, container, false);
        /*Define txtLoadFail from the layout*/
        txtgossip = (TextView) view.findViewById(R.id.txtTopic);
        gsp_rm = (TextView) view.findViewById(R.id.gsp_rm);
        gossip_iconname = (TextView) view.findViewById(R.id.gossip_iconname);


        Log.d("gdfgdfsdshdfgdhdgb1",gossipItems.get(position).get("icon_name"));

        if (gossipItems.get(position).get("icon_name").equalsIgnoreCase("null")
                || gossipItems.get(position).get("icon_name").equalsIgnoreCase("")) {
            gossip_iconname.setText(mContext.getString(R.string.notavail));

        }else {
            gossip_iconname.setText(gossipItems.get(position).get("icon_name"));
        }
        txtgossip.setText(gossipItems.get(position).get("topic"));
        txtgossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        gsp_rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GossipActivity.class);
                intent.putExtra("gossip_list", gossipItems);
                intent.putExtra("total_cmmnts", total_cmmtLists);

                intent.putExtra("pos", position);

                mContext.startActivity(intent);
                Log.d("ghfhfghjfgjfgjf",gossipItems.toString());
            }
        });
        //view.setTag(gossipItems);
        // Add the newly created View to the ViewPager
        container.addView(view);
        Log.i(TAG, "instantiateItem() [position: " + position + "]" + " childCount:" + gossipItems.get(position).get("topic"));
        // Return the View
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


   /* @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }*/
}
