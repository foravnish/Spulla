package com.riseintech.spulla.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.ChatStartActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein on 12/12/2016.
 */

public class FriendListAdapter extends BaseAdapter {
    String[] result;
    Context context;
    int[] imageId;
    ArrayList<HashMap<String, String>> listfriend1;
    private static LayoutInflater inflater = null;

    String type1, maddType, senderId;

    public FriendListAdapter(Context mainActivity, ArrayList<HashMap<String, String>> listfriend, String type) {
        // TODO Auto-generated constructor stub
        listfriend1 = listfriend;
        context = mainActivity;
        type1 = type;


   /* public CategoryListAdapter(Context mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;*/
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listfriend1.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public class Holder {
        TextView friend_name, tv3;
        ImageView img;
        Button accept, cancel, unblock;
        LinearLayout lyt_adapter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.request_chat_items, null);
        holder.friend_name = (TextView) rowView.findViewById(R.id.friend_name);
        holder.img = (ImageView) rowView.findViewById(R.id.friendspic);

        holder.accept = (Button) rowView.findViewById(R.id.accept);
        holder.cancel = (Button) rowView.findViewById(R.id.cancel);
        holder.unblock = (Button) rowView.findViewById(R.id.unblock);
        holder.lyt_adapter = (LinearLayout) rowView.findViewById(R.id.lyt_adapter);


        //Toast.makeText(context, "FriendListAdapter "+type1, Toast.LENGTH_SHORT).show();
        if (type1.equalsIgnoreCase("RequestTo") || type1.equalsIgnoreCase("Chat")
                || type1.equalsIgnoreCase("IrrelevantProfiles")) {
            holder.lyt_adapter.setVisibility(View.GONE);
        }
        if (type1.equalsIgnoreCase("IrrelevantProfiles")) {
            holder.unblock.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(listfriend1.get(position).get("image").toString())
                .placeholder(R.color.colorPrimary).error(R.drawable.propic)
                .into(holder.img);
        holder.friend_name.setText(listfriend1.get(position).get("name").toString());

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (type1.equalsIgnoreCase("Chat")) {
                    Util.Sender_ID = listfriend1.get(position).get("id");
                    Util.Sender_Name = listfriend1.get(position).get("name");
                    Util.Sender_Img = listfriend1.get(position).get("image").toString();
                    Intent intent = new Intent(context, ChatStartActivity.class);
                    context.startActivity(intent);

                }

                //Toast.makeText(context, "Click on list . ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                maddType = "addfriend";
                senderId = listfriend1.get(position).get("id");

                if (InternetStatus.isConnectingToInternet(context)) {
                    new AddFriendsList().execute();
                    notifyDataSetChanged();

                }

                listfriend1.remove(position);
                Toast.makeText(context, "Request accept . ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                maddType = "cancel";
                senderId = listfriend1.get(position).get("id");

                if (InternetStatus.isConnectingToInternet(context)) {
                    new AddFriendsList().execute();
                    notifyDataSetChanged();
                }

                listfriend1.remove(position);

                Toast.makeText(context, "Request cancel . ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maddType = "unblock";
                senderId = listfriend1.get(position).get("id");
                if (InternetStatus.isConnectingToInternet(context)) {
                    new AddFriendsList().execute();
                    notifyDataSetChanged();
                }

                Toast.makeText(context, "Unblock successfully", Toast.LENGTH_SHORT).show();
                listfriend1.remove(position);

            }
        });
        return rowView;
    }


    class AddFriendsList extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        final String id = MyPreference.loadUserid(context);
        HashMap<String, String> params = new HashMap<>();


        @Override
        protected void onPreExecute() {
           /* pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait...");
            pDialog.show();*/
            params.put("type", maddType);
            params.put("user_id", id);
            params.put("sender_id", senderId);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.URL_ADD_REQUEST, "POST", params);
                //Log.d("JSON ", "------------" + json);
                if (json != null) {
                    //Log.d("JSON result", json.toString());
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String status = "";
            String msg = "";
           /* if (pDialog.isShowing())
                hidePDialog();*/

            if (json != null) {
                try {
                    status = json.getString("status");
                    msg = json.getString("msg");
                    Toast.makeText(context, " " + msg, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}