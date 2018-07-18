package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/11/2016.
 */

public class ShopItemAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> hashMaps;
    Context context;
    TextView offer, lable, rate, offer_value;
    ImageView imageView, fab_icon;
    LinearLayout offer_ly;

    boolean isfab;

    public ShopItemAdapter(Context context, ArrayList<HashMap<String, String>> hashMaps) {
        this.hashMaps = hashMaps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return hashMaps.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.shopingitemadapter, null);

        lable = (TextView) view.findViewById(R.id.lable);
        rate = (TextView) view.findViewById(R.id.rate);
        offer = (TextView) view.findViewById(R.id.offer);
        offer_value = (TextView) view.findViewById(R.id.offer_value);
        imageView = (ImageView) view.findViewById(R.id.icon);

        //fab_icon= (ImageView) view.findViewById(R.id.fab_icon);
        rate.setText(context.getString(R.string.Rs) +" " + hashMaps.get(i).get("celling_price"));
        lable.setText(hashMaps.get(i).get("product_name"));
        if (hashMaps.get(i).get("img").contains(",")) {
            String img[] = hashMaps.get(i).get("img").split(",");
            Util.showImage(context, img[0], imageView);
        } else {
            Util.showImage(context,hashMaps.get(i).get("img"), imageView);
        }
      //  Log.d("gfdgdfgdfdhdf",hashMaps.get(i).get("img"));
        offer.setPaintFlags(offer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        offer.setText(context.getString(R.string.Rs) +" "+ hashMaps.get(i).get("price_mrp"));
        offer_value.setText(hashMaps.get(i).get("discount_percent") + "% off");

       /* fab_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isfab){
                    //fab_icon.setBackgroundResource(R.drawable.heart_fill);
                         fab_icon.setImageResource(R.drawable.heart_fill);
                    isfab=false;
                        }
                else {
                    //fab_icon.setBackgroundResource(R.drawable.heart);
                    fab_icon.setImageResource(R.drawable.heart_fill);
                    isfab=true;
                }


            }
        });*/

        return view;
    }
}
