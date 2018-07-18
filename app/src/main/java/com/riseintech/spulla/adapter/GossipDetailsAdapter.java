package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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

import com.riseintech.spulla.AsyncTaskService.AddCommentAsync;
import com.riseintech.spulla.GossipActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 10/19/2016.
 */

public class GossipDetailsAdapter extends PagerAdapter implements View.OnClickListener {
    Context context;
    ArrayList<HashMap<String, String>> commtList;
    String url = "df";
    int pos;
    ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists;
     ImageView fact_bg, com_send;
    TextView gosip_hd, gsp_date1, gssp_dtls, no_cmmts, subtit;
    EditText cmts_hd;

    LinearLayout cmnt_ly;
    //comments details
    ImageView cmt_img, heart;
    TextView cn_name, cmt_time, cmtns_txv;
    public GossipDetailsAdapter(Context context, ArrayList<HashMap<String, String>> commtList,
                                ArrayList<ArrayList<HashMap<String, String>>> total_cmmtLists) {
        this.context = context;
        this.commtList = commtList;
        this.total_cmmtLists = total_cmmtLists;
    }

    @Override
    public int getCount() {
        return commtList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        pos = position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.factsdetailsadapter, container, false);
        //Log.e("position is", ": " + position);
        url = commtList.get(position).get("img");

//comment details
        cmnt_ly = (LinearLayout) view.findViewById(R.id.cmnt_ly);
        cmt_img = (ImageView) view.findViewById(R.id.cmt_img);
        cn_name = (TextView) view.findViewById(R.id.cn_name);
        cmt_time = (TextView) view.findViewById(R.id.cmt_time);
        cmtns_txv = (TextView) view.findViewById(R.id.cmtns_txv);
      /*  Picasso.with(context).load(total_cmmtLists.get(GossipActivity.currentpos)
                .get(0).get("cimg")).into(cmt_img);
*/

////////////////////////////////////////////////////////////
        cmts_hd = (EditText) view.findViewById(R.id.cmts_hd);
        cmts_hd.setOnClickListener(this);
        fact_bg = (ImageView) view.findViewById(R.id.fact_bg);
        fact_bg.setOnClickListener(this);
        com_send = (ImageView) view.findViewById(R.id.com_send);
        com_send.setVisibility(View.GONE);
        gosip_hd = (TextView) view.findViewById(R.id.gosip_hd);
        gsp_date1 = (TextView) view.findViewById(R.id.gsp_date1);
        gssp_dtls = (TextView) view.findViewById(R.id.gssp_dtls);
        subtit = (TextView) view.findViewById(R.id.subtit);

        no_cmmts = (TextView) view.findViewById(R.id.no_cmmts);
        ArrayList<HashMap<String, String>> hashMaps = total_cmmtLists.get(GossipActivity.currentpos);
        no_cmmts.setText(hashMaps.size() + " comments");
        Util.showImage(context, url, fact_bg);
        // Picasso.with(context).load(url).into(fact_bg);
        // new LoadImageAsync(FactDetailsActivity.this, url, fact_bg).execute();
        gosip_hd.setText(commtList.get(position).get("topic"));
        gsp_date1.setText(commtList.get(position).get("date_time"));
        gssp_dtls.setText(Html.fromHtml(commtList.get(position).get("description")));
        subtit.setText(Html.fromHtml(commtList.get(position).get("subtitle")));
        cn_name.setText(total_cmmtLists.get(GossipActivity.currentpos)
                .get(0).get("user_title"));
        cmt_time.setText(total_cmmtLists.get(GossipActivity.currentpos)
                .get(0).get("date_time"));
        cmtns_txv.setText(total_cmmtLists.get(GossipActivity.currentpos)
                .get(0).get("comment"));
        Util.showImage(context, total_cmmtLists.get(GossipActivity.currentpos)
                .get(0).get("cimg"), cmt_img);

        cmnt_ly.setOnClickListener(this);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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
                Util.showImage(context, commtList.get(GossipActivity.currentpos).get("img"), fact_image);
                // Picasso.with(context).load(commtList.get(GossipActivity.currentpos).get("img")).into(fact_image);

                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText(commtList.get(GossipActivity.currentpos).get("topic"));
                back_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
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
        /*if (InternetStatus.isConnectingToInternet(context)) {
            new GetCommentDetails(context, "gosip", commtList.get(GossipActivity.currentpos).get("id").toString(),
                    total_cmmtLists.get(GossipActivity.currentpos),g_cmts_lv).execute();
        } else {
            g_cmts_lv.setAdapter(new GossipCommentAdapter(context, total_cmmtLists.get(FactDetailsActivity.currentpos), true));
        }*/
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
                      new AddCommentAsync(context, "gosip", commtList.get(GossipActivity.currentpos).get("id").toString(),
                            cmts_hd.getText().toString()).execute();

                    cmts_hd.setText("");
                    cmts_hd.setHint(context.getString(R.string.write_comment));

                }
                // dialog1.cancel();
            }
        });
        dialog1.show();

    }
}
