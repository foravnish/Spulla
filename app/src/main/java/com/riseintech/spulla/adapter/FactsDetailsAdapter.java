package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.AsyncTaskService.AddCommentAsync;
import com.riseintech.spulla.FactDetailsActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 10/19/2016.
 */

public class FactsDetailsAdapter extends PagerAdapter implements View.OnClickListener {
    Context context;
    ArrayList<HashMap<String, String>> factList;
    String url = "dkd";
    ImageView heart;
    ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists;
    int pos;
    ImageView fact_bg, com_send;
    EditText cmts_hd;
    ArrayList<HashMap<String, String>> myComments;

    public FactsDetailsAdapter(Context context, ArrayList<HashMap<String, String>> commtList,
                               ArrayList<HashMap<String, String>> myComments) {
        this.context = context;
        this.factList = commtList;
        this.myComments = myComments;
       // myComments = new ArrayList<>();

    }
    @Override
    public int getCount() {
        return factList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
       /* Log.e("isViewFromObject", "isViewFromObject");
        if (Util.comment_api_call == 1) {
            Log.e("isViewFromObject in if", "isViewFromObject");
            myComments.clear();
            no_cmmts.setText("No comments Available");
            cn_name.setText(context.getString(R.string.notavail));
            cmt_time.setText(context.getString(R.string.notavail));
            cmtns_txv.setText(context.getString(R.string.notavail));
        }*/
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        //  Toast.makeText(context,"instantiateItem",Toast.LENGTH_SHORT).show();
        //Log.e("instantiateItem", "instantiateItem");
        pos = position;
        LinearLayout cmnt_ly;
        //comments details
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.factsdetailsadapter, container, false);
        url = factList.get(position).get("img");

       /* if (position == 0 || position == factList.size()) {
            FactDetailsActivity.currentpos = position;
        }*/
//comment details
        final TextView gosip_hd, gsp_date1, gssp_dtls, no_cmmts, subtit;
        final TextView cn_name, cmt_time, cmtns_txv;
        ImageView cmt_img;
        LinearLayout add_cmt = (LinearLayout) view.findViewById(R.id.add_cmt);
        add_cmt.setOnClickListener(this);
        no_cmmts = (TextView) view.findViewById(R.id.no_cmmts);
        cmnt_ly = (LinearLayout) view.findViewById(R.id.cmnt_ly);
        cmt_img = (ImageView) view.findViewById(R.id.cmt_img);
        heart = (ImageView) view.findViewById(R.id.heart);
        cn_name = (TextView) view.findViewById(R.id.cn_name);
        cmt_time = (TextView) view.findViewById(R.id.cmt_time);
        cmtns_txv = (TextView) view.findViewById(R.id.cmtns_txv);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heart.setImageResource(R.drawable.heart_fill);
                cn_name.setText(myComments.get(0).get("user_title"));
                no_cmmts.setText(myComments.get(0).get("comments"));
                cmt_time.setText(myComments.get(0).get("date_time"));
                cmtns_txv.setText(myComments.get(0).get("comment"));

            }
        });
        ////////////////////////////////////////////////////////////
        cmts_hd = (EditText) view.findViewById(R.id.cmts_hd);
        cmts_hd.setOnClickListener(this);
        fact_bg = (ImageView) view.findViewById(R.id.fact_bg);
        fact_bg.setOnClickListener(this);
        com_send = (ImageView) view.findViewById(R.id.com_send);
        com_send.setVisibility(View.GONE);
        com_send.setOnClickListener(this);
        gosip_hd = (TextView) view.findViewById(R.id.gosip_hd);
        gsp_date1 = (TextView) view.findViewById(R.id.gsp_date1);
        gssp_dtls = (TextView) view.findViewById(R.id.gssp_dtls);
        subtit = (TextView) view.findViewById(R.id.subtit);
        cmnt_ly.setOnClickListener(this);
        // ArrayList<HashMap<String, String>> hashMaps = total_cmmtLists.get(FactDetailsActivity.currentpos);
        Util.showImage(context, url, fact_bg);
        // new LoadImageAsync(FactDetailsActivity.this, url, fact_bg).execute();
        gosip_hd.setText(factList.get(position).get("topic"));
        gsp_date1.setText(factList.get(position).get("date_time"));
        gssp_dtls.setText(Html.fromHtml(factList.get(position).get("description")));
        subtit.setText(Html.fromHtml(factList.get(position).get("subtitle")));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //Log.e("destroyItem", "destroyItem");
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cmts_hd:
                showAllComments(context);
                break;
            case R.id.fact_bg:
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.getWindow()
                        .getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.showfullimage);
                ImageView back_img = (ImageView) dialog.findViewById(R.id.back_img);
                ImageView fact_image = (ImageView) dialog.findViewById(R.id.fact_image);
                Util.showImage(context, factList.get(FactDetailsActivity.currentpos).get("img"), fact_image);
                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText(factList.get(FactDetailsActivity.currentpos).get("topic"));
                back_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.com_send:

                break;
            case R.id.cmnt_ly:
                showAllComments(context);
                break;
        }
    }

    private void showAllComments(final Context context) {
        final Dialog dialog1 = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog1.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.setContentView(R.layout.showallgossipcomments);
        TextView title1 = (TextView) dialog1.findViewById(R.id.title);
        final EditText cmts_hd = (EditText) dialog1.findViewById(R.id.cmts_hd);
        final ListView g_cmts_lv = (ListView) dialog1.findViewById(R.id.g_cmts_lv);
        final ImageView com_send = (ImageView) dialog1.findViewById(R.id.com_send);
        com_send.setVisibility(View.GONE);
      /*  if (InternetStatus.isConnectingToInternet(context)) {
            new GetCommentDetails(context, "facts", factList.get(FactDetailsActivity.currentpos).get("id").toString(),
                    total_cmmtLists.get(FactDetailsActivity.currentpos),g_cmts_lv).execute();
        } else {
            g_cmts_lv.setAdapter(new GossipCommentAdapter(context, total_cmmtLists.get(FactDetailsActivity.currentpos), true));
        }*/
        g_cmts_lv.setAdapter(new GossipCommentAdapter(context, myComments, true));
        cmts_hd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    com_send.setVisibility(View.VISIBLE);
                } else {
                    com_send.setVisibility(View.GONE);

                }

            }
        });
        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();
            }
        });
        com_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cmts_hd.getText().toString().isEmpty()) {
                    cmts_hd.setError(context.getString(R.string.empty_msg));
                } else if (InternetStatus.isConnectingToInternet(context)) {
                    new AddCommentAsync(context, "facts", factList.get(FactDetailsActivity.currentpos).get("id").toString(),
                            cmts_hd.getText().toString()).execute();
                    cmts_hd.setText("");
                    cmts_hd.setHint(context.getString(R.string.write_comment));
                }
                // dialog1.cancel();
            }
        });
        dialog1.show();

    }

    //////////////////////////
    public class GetCommentDetails1 extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String ctype, ctype_id;
        TextView cn_name, cmt_time, cmtns_txv, no_cmmts;
        ImageView cmt_img;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        //time_format 2016-10-24 04:39:18

        public GetCommentDetails1(Context context, String ctype, String ctype_id,
                                 ImageView cmt_img,
                                 TextView cn_name, TextView cmt_time,
                                 TextView cmtns_txv, TextView no_cmmts) {
            this.cmt_img = cmt_img;
            this.cn_name = cn_name;
            this.cmt_time = cmt_time;
            this.cmtns_txv = cmtns_txv;
            this.no_cmmts = no_cmmts;
            this.context = context;
            this.ctype = ctype;
            this.ctype_id = ctype_id;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Util.comment_api_call = 1;
            pDialog = new ProgressDialog(context);
            // pDialog.setMessage("Loading  ....");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            params.put("comments", "");
            params.put("cate_id", ctype_id);
            params.put("user_type", ctype);
            JSONObject json = jsonParser.makeHttpRequest(Api.Get_Comments, "GET", params);
            if (json != null) {
                Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                if (json.getString("status").equals("1")) {
                    ArrayList<HashMap<String, String>> hashMaps = new ArrayList<HashMap<String, String>>();
                    //Log.e("current time", ": " + Util.currentDateTime());
                    myComments = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("items");
                    //  hashMaps.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_title", jsonArray.getJSONObject(i).getString("title"));
                        map.put("comment", jsonArray.getJSONObject(i).getString("comments"));
                        map.put("date_time", jsonArray.getJSONObject(i).getString("date_time"));
                        map.put("cimg", jsonArray.getJSONObject(i).getString("img"));
                        //  hashMaps.add(map);
                        myComments.add(map);
                        hashMaps.add(map);
                    }
                    // total_cmmtLists.add(hashMaps);
                    Util.comment_api_call = 0;
                    Util.showImage(context, hashMaps.get(0).get("cimg"), cmt_img);
                    no_cmmts.setText(hashMaps.size() + " comments");
                    cn_name.setText(hashMaps
                            .get(0).get("user_title"));
                    cmt_time.setText(hashMaps
                            .get(0).get("date_time"));
                    cmtns_txv.setText(hashMaps
                            .get(0).get("comment"));
                } else {
                    Toast.makeText(context, "Comments list not get", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


    }
/////////////////////////
}
