package com.riseintech.spulla.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.BuyNow;
import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 1/23/2017.
 */

public class BuyingListAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> cartlist;
    private static LayoutInflater inflater = null;
    TextView pdt_name, prod_price, seller_name, pdt_color;
    ImageView pdt_img;
    Button pdt_qty;
    String quantity = "1";

    public BuyingListAdapter(Context mainActivity, ArrayList<HashMap<String, String>> listfriend) {
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
        rowView = inflater.inflate(R.layout.buyinglistadapter, null);
        pdt_name = (TextView) rowView.findViewById(R.id.pdt_name);
        pdt_qty = (Button) rowView.findViewById(R.id.pdt_qty);
        prod_price = (TextView) rowView.findViewById(R.id.prod_price);
        seller_name = (TextView) rowView.findViewById(R.id.seller_name);
        pdt_color = (TextView) rowView.findViewById(R.id.pdt_color);
        pdt_img = (ImageView) rowView.findViewById(R.id.pdt_img);

        pdt_name.setText(cartlist.get(position).get("product_name"));
        prod_price.setText(context.getString(R.string.Rs) + " " + cartlist.get(position).get("celling_price"));


        pdt_color.setText("Color: " + cartlist.get(position).get("colour") + "\n" + "Size: " + cartlist.get(position).get("p_size"));
        pdt_qty.setText("Qty: " + cartlist.get(position).get("p_quantity"));

        if (cartlist.get(position).get("img").contains(",")) {
            String img[] = cartlist.get(position).get("img").split(",");
            Util.showImage(context, img[0], pdt_img);
        } else {
            Util.showImage(context, cartlist.get(position).get("img"), pdt_img);
        }
        pdt_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  addQtyDialog(position);
            }
        });
        return rowView;
    }

    private void addQtyDialog(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.addqtydialog);
        dialog.setCancelable(false);
        final EditText qty_edt = (EditText) dialog.findViewById(R.id.qty_edt);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button save = (Button) dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty_edt.getText().toString().isEmpty()) {
                    qty_edt.setError(context.getString(R.string.empty_msg));
                } else {
                    quantity = qty_edt.getText().toString();
                    if (cartlist.get(position).get("celling_price").equalsIgnoreCase("Nan") == false) {
                        double totalprice = Double.valueOf(quantity) * Double.valueOf(cartlist.get(position).get("celling_price"));
                        //Log.w("total price", ": " + totalprice);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("p_id", cartlist.get(position).get("p_id"));
                        map.put("product_name", cartlist.get(position).get("product_name"));
                        map.put("celling_price", String.valueOf(totalprice));
                        map.put("colour", cartlist.get(position).get("colour"));
                        map.put("p_size", cartlist.get(position).get("p_size"));
                        map.put("p_quantity", quantity);
                        map.put("img", cartlist.get(position).get("img"));
                       /* map.put("celling_price", String.valueOf(totalprice));
                        map.put("p_quantity", quantity);
                       */
                        cartlist.set(position, map);
                        pdt_qty.setText(context.getString(R.string.Rs) + " " + totalprice);
                        Util.Add_Qty = true;
                        if (context instanceof BuyNow) {
                            ((BuyNow) context).totalPrice();
                        }
                        notifyDataSetChanged();

                    }
                    // pdt_qty.setText("Qty: " + quantity);
                    dialog.cancel();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        dialog.show();

    }
}
