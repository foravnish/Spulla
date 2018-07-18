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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.AsyncTaskService.FindPlayerAsyncTask;
import com.riseintech.spulla.AsyncTaskService.SendRequestAsync;
import com.riseintech.spulla.ChatDetailsActivity;
import com.riseintech.spulla.FindPlayer;
import com.riseintech.spulla.FindPlayerDetailsActivity;
import com.riseintech.spulla.MainActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.connection.InternetStatus;

import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;
import com.riseintech.spulla.utils.Util;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adm on 9/13/2016.
 */


public class FindPlayerAdapter extends PagerAdapter {

    static class ViewHolderItem {
        ImageView imgrequest,imgdecline,imgblock,imgreq;
    }
    ViewHolderItem viewHolder;

    private ArrayList<HashMap<String, String>> playerItems;
    private String isRequest;
    private static final String TAG = "MyPagerAdapter";
    private String rType = "send_request";
    private Context mContext;
    private LinearLayout lytfind;
    private LayoutInflater mLayoutInflater;
    private TextView txtfacts, p_h_title;


    public FindPlayerAdapter(Context activity, ArrayList<HashMap<String, String>> playerItems,String isRequest) {
        this.mContext = activity;
        this.playerItems = playerItems;
        this.isRequest=isRequest;
        viewHolder = new ViewHolderItem();
    }

    @Override
    public int getCount() {
        return playerItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {


        ImageView imgchat,profile_image;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.findplayer_items, container, false);
        /*Define txtLoadFail from the layout*/



        txtfacts = (TextView) view.findViewById(R.id.txttname);
        lytfind = (LinearLayout) view.findViewById(R.id.lytfind);

        viewHolder.imgrequest = (ImageView) view.findViewById(R.id.imgrequest);
        viewHolder.imgdecline = (ImageView) view.findViewById(R.id.imgdecline);
        viewHolder.imgblock = (ImageView) view.findViewById(R.id.imgblock);
        imgchat = (ImageView) view.findViewById(R.id.imgchat);
        viewHolder.imgreq = (ImageView) view.findViewById(R.id.imgreq);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);

        txtfacts.setText(playerItems.get(position).get("name"));
        Util.showImage(mContext, playerItems.get(position).get("images").toString(), profile_image);

        Log.d("gdfgdfsdshdfgdhdgb1",playerItems.get(position).get("name"));
        Log.d("gdfgdfsdshdfgdhdgb2",playerItems.get(position).get("images").toString());

        Log.e("getRequestNo"," "+PlayerDetails.getRequestNo());

        if( PlayerDetails.getRequestNo().equals("0")){
            viewHolder.imgreq.setClickable(false);
            viewHolder.imgreq.setImageResource(R.drawable.req);
        }
        else {
            viewHolder.imgreq.setImageResource(R.drawable.reqs);
            viewHolder.imgreq.setClickable(true);
            viewHolder.imgreq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Util.Sender_ID = playerItems.get(position).get("id");
                    Util.Sender_Name = playerItems.get(position).get("name");
                    Util.Sender_Img = playerItems.get(position).get("images").toString();
                    Intent intent = new Intent(mContext, ChatDetailsActivity.class);
                    intent.putExtra("player_id", playerItems.get(position).get("id"));
                    mContext.startActivity(intent);

                }
            });
        }


        try {
            if (playerItems.get(position).get("request_approval").toString().equalsIgnoreCase("0")){
                viewHolder.imgrequest.setImageResource(R.drawable.r_accept);
                viewHolder.imgrequest.setEnabled(false);
            }
            else{
                viewHolder.imgrequest.setImageResource(R.drawable.acpt_btn_press);
                viewHolder.imgrequest.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // p_h_title = (TextView) view.findViewById(R.id.p_h_title);

        //p_h_title.setText(mContext.getString(R.string.findplayer));

       /* p_h_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        //view.setTag(gossipItems);
        // Add the newly created View to the ViewPager
        container.addView(view);



        lytfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyPreference.loadIsAdd(mContext) == 1) {
                    Intent intent = new Intent(view.getContext(), FindPlayerDetailsActivity.class);
                    intent.putExtra("player_id", playerItems.get(position).get("id"));
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(mContext, "First Add Player Details", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(view.getContext(), FindPlayer.class);
                    view.getContext().startActivity(in);
                }

            }
        });


        viewHolder.imgrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rType = "send_request";
                if (InternetStatus.isConnectingToInternet(mContext)) {
                    new SendRequestAsync(mContext, playerItems.get(position).get("id"), rType).execute();

//                    if (Util.getFindPlayerAsynccounting().equalsIgnoreCase("1")) {
                    if (Util.FindPlayerAsynccountingVaue==1) {
                        new FindPlayerAsyncTask(mContext, "", "", "",
                                "", "", "", MainActivity.player_viewPager,
                                "", "SeeAll", "", MainActivity.player_viewPager.getCurrentItem() + "").execute();
                    }
                    else if (Util.FindPlayerAsynccountingVaue==2){
                        new FindPlayerAsyncTask(mContext, MainActivity.play_as, MainActivity.gender, MainActivity.location,
                                MainActivity.sports, MainActivity.minage, MainActivity.maxage, MainActivity.player_viewPager,
                                MainActivity.search_player.getText().toString(), "123","",MainActivity.player_viewPager.getCurrentItem()+"").execute();
                    }
                 else {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);

                    }

                }
            }
        });



        viewHolder.imgdecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rType = "reject";
                if (InternetStatus.isConnectingToInternet(mContext)) {
                    new SendRequestAsync(mContext, playerItems.get(position).get("id"), rType).execute();
                    playerItems.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        viewHolder.imgblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rType = "block";
                if (InternetStatus.isConnectingToInternet(mContext)) {
                    new SendRequestAsync(mContext, playerItems.get(position).get("id"), rType).execute();
                    playerItems.remove(position);
                    notifyDataSetChanged();
                }
            }
        });


        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.Sender_ID = playerItems.get(position).get("id");
                Util.Sender_Name = playerItems.get(position).get("name");
                Util.Sender_Img = playerItems.get(position).get("images").toString();
                Intent intent = new Intent(mContext, ChatDetailsActivity.class);
                intent.putExtra("player_id", playerItems.get(position).get("id"));
                mContext.startActivity(intent);

            }
        });

      /*  if (playerItems.get(position).get("request_status").equalsIgnoreCase("done")){

            onRequestClick(playerItems.get(position).get("request_status"),position);
        }
        else if (playerItems.get(position).get("request_status").equalsIgnoreCase("pending")){
            onRequestClick(playerItems.get(position).get("request_status"),position);

        }else if (playerItems.get(position).get("request_status").equalsIgnoreCase("notsend")){
            onRequestClick(playerItems.get(position).get("request_status"),position);

        }*/

        //Log.i(TAG, "instantiateItem() [position: " + position + "]" + " childCount:" + playerItems.get(position).get("name"));
        // Return the View
        return view;
    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }




//    public void onRequestClick(final String msg, final int pos) {
//
//        imgchat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (msg.equalsIgnoreCase("done")) {
//                    Toast.makeText(mContext, "you are already add", Toast.LENGTH_SHORT).show();
//                } else if (msg.equalsIgnoreCase("pending")) {
//                    Toast.makeText(mContext, "Friend request already sent", Toast.LENGTH_SHORT).show();
//                } else if (msg.equalsIgnoreCase("notsend")) {
//                    Intent intent = new Intent(mContext, ChatDetailsActivity.class);
//                    intent.putExtra("player_id", playerItems.get(pos).get("id"));
//                    mContext.startActivity(intent);
//                }
//
//
//            }
//        });
//
//    }


//    class SendRequestAsync1 extends AsyncTask<String, String, JSONObject> {
//        JSONParser jsonParser = new JSONParser();
//        HashMap<String, String> params = new HashMap<>();
//
//        ProgressDialog pDialog;
//        String reciver_id, rType;
//        Context mContext;
//
//        public  SendRequestAsync1(Context mContext, String reciver_id, String rType) {
//            this.reciver_id = reciver_id;
//            this.mContext = mContext;
//            this.rType = rType;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            final String sender_id = MyPreference.loadUserid(mContext);
//            params.put("type", rType);
//            params.put("user_id", sender_id);
//            params.put("sender_id", reciver_id);
//
//
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            JSONObject json;
//            json = jsonParser.makeHttpRequest(
//                    Api.URL_SEND_REQUEST, "POST", params);
//
//            try {
//                //Log.d("JSON ", "------------" + json);
//                if (json != null) {
//                    //Log.d("JSON result", json.toString());
//
//                    return json;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        protected void onPostExecute(JSONObject json) {
//            String status = "";
//            String data = "";
//            if (json != null) {
//                try {
//                    status = json.getString("status");
//                    if (rType.equalsIgnoreCase("send_request")) {
//                        if (status.equalsIgnoreCase("1")) {
//                            Log.d("dfgdfgdfgdfgh","true");
//                            Toast.makeText(mContext, "Request Sent", Toast.LENGTH_LONG).show();
//                        }
//                        else if (status.equalsIgnoreCase("0")) {
//                            Log.d("dfgdfgdfgdfgh","false");
//                            Toast.makeText(mContext, "Request Already Sent", Toast.LENGTH_LONG).show();
//                            //viewHolder.imgrequest.setImageResource(R.drawable.r_accept);
//
//                        }
//
//                    } else if (rType.equalsIgnoreCase("reject")) {
//                        if (status.equalsIgnoreCase("1")) {
//                            Log.d("dfgdfgdfgdfgh","true");
//                            Toast.makeText(mContext, "Block Already irrelevant profile", Toast.LENGTH_LONG).show();
//                        }
//                        else if (status.equalsIgnoreCase("0")) {
//                            Log.d("dfgdfgdfgdfgh","false");
//                            Toast.makeText(mContext, "Block irrelevant profile", Toast.LENGTH_LONG).show();
//
//                        }
//
//                    } else if (rType.equalsIgnoreCase("block")) {
//
//                        if (status.equalsIgnoreCase("1")) {
//                            Log.d("dfgdfgdfgdfgh","true");
//                            Toast.makeText(mContext, "Block permanently", Toast.LENGTH_LONG).show();
//                        }
//                        else if (status.equalsIgnoreCase("0")) {
//                            Log.d("dfgdfgdfgdfgh","false");
//                            Toast.makeText(mContext, "Already Block  permanently", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

}