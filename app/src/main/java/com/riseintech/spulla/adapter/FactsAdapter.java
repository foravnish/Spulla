package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.FactDetailsActivity;
import com.riseintech.spulla.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adm on 9/13/2016.
 */
public class FactsAdapter extends PagerAdapter {
    private static ArrayList<HashMap<String, String>> categoryItem;
    private ArrayList<HashMap<String, String>> factsItems;
    //private ArrayList<HashMap<String, String>> categoryItem;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView txtfacts, facts_iconname, fct_rm;
    ImageView p_h_menu;
    ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists;
    String url = "";

    public FactsAdapter(Context activity, ArrayList<HashMap<String, String>> factsItems,
                        ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists) {
        this.mContext = activity;
        this.factsItems = factsItems;
        this.total_cmmtLists = total_cmmtLists;
    }

    @Override
    public int getCount() {
        return factsItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.facts_items, container, false);
        /*Define txtLoadFail from the layout*/
        txtfacts = (TextView) view.findViewById(R.id.txtFacts);
        facts_iconname = (TextView) view.findViewById(R.id.facts_iconname);
        if (factsItems.get(position).get("icon_name").equalsIgnoreCase("null")
                || factsItems.get(position).get("icon_name").equalsIgnoreCase("")) {
            facts_iconname.setText(mContext.getString(R.string.notavail));

        }else {
            facts_iconname.setText(factsItems.get(position).get("icon_name"));
        }
        fct_rm = (TextView) view.findViewById(R.id.fct_rm);
        txtfacts.setText(factsItems.get(position).get("topic"));
        url = factsItems.get(position).get("icon");
        fct_rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FactDetailsActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("cmmt_list", factsItems);
                intent.putExtra("total_cmmnts", total_cmmtLists);
                mContext.startActivity(intent);
            }
        });
       /* p_h_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenuDialog(mContext,"facts");
            }
        });*/

        //view.setTag(gossipItems);
        // Add the newly created View to the ViewPager
        container.addView(view);
        //Log.i(TAG, "instantiateItem() [position: " + position + "]" + " childCount:" + factsItems.get(position).get("topic"));
        // Return the View
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /*public static void openMenuDialog(final Context context, final String checktype) {
       *//* ArrayAdapter<String> adapter;
        String[] mTestArray;

        mTestArray = context.getResources().getStringArray(R.array.choose_sports_gossip);

        adapter = new ArrayAdapter<String>(
                context.getApplicationContext(),
                android.R.layout.select_dialog_singlechoice,
                mTestArray);
        adapter.setDropDownViewResource(R.layout.listtextview);*//*
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoglivescoremenu);
        dialog.setCancelable(true);
        Button submit = (Button) dialog.findViewById(R.id.disimiss);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final ListView listView = (ListView) dialog.findViewById(R.id.choose_sportlv);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(new SportCatAdapter(context,categoryItem ));
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (checktype.contentEquals("facts")){
                                                dialog.cancel();
                                                Toast.makeText(context,"this is facts",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context,"this is gossip",Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                        }
                                    }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        dialog.show();
    }*/


}


