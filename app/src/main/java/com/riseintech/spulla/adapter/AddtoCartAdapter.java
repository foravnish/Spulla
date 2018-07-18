package com.riseintech.spulla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.AsyncTaskService.RemoveCartProdAsync;
import com.riseintech.spulla.R;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 1/9/2017.
 */

public class AddtoCartAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> cartlist;
    private static LayoutInflater inflater = null;
    TextView pdt_name, remove_pdt, delv_dtls, prod_price, seller_name, pdt_color,pdt_size,pdt_qty;
    ImageView pdt_img;

    public AddtoCartAdapter(Context mainActivity, ArrayList<HashMap<String, String>> listfriend) {
        // TODO Auto-generated constructor stub
        cartlist = listfriend;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cartlist.size();
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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        rowView = inflater.inflate(R.layout.addtocartadapter, null);
        pdt_name = (TextView) rowView.findViewById(R.id.pdt_name);
        pdt_qty=(TextView) rowView.findViewById(R.id.pdt_qty);
        pdt_size=(TextView) rowView.findViewById(R.id.pdt_size);

        delv_dtls = (TextView) rowView.findViewById(R.id.delv_dtls);
        prod_price = (TextView) rowView.findViewById(R.id.prod_price);
        seller_name = (TextView) rowView.findViewById(R.id.seller_name);
        pdt_color = (TextView) rowView.findViewById(R.id.pdt_color);
        pdt_img = (ImageView) rowView.findViewById(R.id.pdt_img);
        remove_pdt = (TextView) rowView.findViewById(R.id.remove_pdt);

        pdt_name.setText(cartlist.get(position).get("product_name"));
        prod_price.setText(context.getString(R.string.Rs)+" "+cartlist.get(position).get("celling_price"));
        pdt_color.setText(cartlist.get(position).get("colour"));
        pdt_qty.setText(cartlist.get(position).get("p_quantity"));
        pdt_size.setText(cartlist.get(position).get("p_size"));

        if (cartlist.get(position).get("img").contains(",")) {
            String img[] = cartlist.get(position).get("img").split(",");
            Util.showImage(context, img[0], pdt_img);
        } else {
            Util.showImage(context, cartlist.get(position).get("img"), pdt_img);
        }
        remove_pdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetStatus.isConnectingToInternet(context)) {
                    new RemoveCartProdAsync(context, cartlist.get(position).get("p_id")).execute();
                    notifyDataSetChanged();
                    cartlist.remove(position);



                }
            }
        });
        return rowView;
    }
}
