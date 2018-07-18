package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 10/10/2016.
 */

public class GossipCommentAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> commtList;
    boolean allcomment;

    public GossipCommentAdapter(Context context, ArrayList<HashMap<String, String>> commtList, boolean allcomment) {
        this.context = context;
        this.commtList = commtList;
        this.allcomment = allcomment;
    }

    @Override
    public int getCount() {
        if (allcomment) {
            return commtList.size();
        }
        return commtList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.gossipcmntsadapter, null);
        ImageView circleImageView = (ImageView) view.findViewById(R.id.circleImageView);
        final TextView cn_txv = (TextView) view.findViewById(R.id.cn_txv);
        TextView cmt_time = (TextView) view.findViewById(R.id.cmt_time);
        TextView cmtns_txv = (TextView) view.findViewById(R.id.cmtns_txv);
        if (commtList.get(0).get("comment").equalsIgnoreCase("null")) {
            cn_txv.setVisibility(View.GONE);
            cmt_time.setVisibility(View.GONE);
            cmtns_txv.setVisibility(View.GONE);
            circleImageView.setVisibility(View.GONE);
        } else {
            cn_txv.setVisibility(View.VISIBLE);
            cmt_time.setVisibility(View.VISIBLE);
            cmtns_txv.setVisibility(View.VISIBLE);
            circleImageView.setVisibility(View.VISIBLE);
            cn_txv.setText(commtList.get(i).get("user_title"));
            cmt_time.setText(commtList.get(i).get("date_time"));
            cmtns_txv.setText(commtList.get(i).get("comment"));
            Util.showImage(context, commtList.get(i).get("cimg"), circleImageView);
        }
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showFullImageDialog(context,commtList.get(i).get("cimg"),cn_txv.getText().toString());
            }
        });
        return view;
    }
}
