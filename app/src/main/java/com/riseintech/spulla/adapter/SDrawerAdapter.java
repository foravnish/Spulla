package com.riseintech.spulla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.SoppingItem;

import java.util.ArrayList;

/**
 * Created by Risein on 11/30/2016.
 */
public class SDrawerAdapter extends BaseAdapter {
    Context context;
    ArrayList<SoppingItem> mydrawerlist;
    private static LayoutInflater inflater=null;


    public SDrawerAdapter(Context mainActivity, ArrayList<SoppingItem> mydrawerlist12) {
        // TODO Auto-generated constructor stub
        mydrawerlist=mydrawerlist12;
        context=mainActivity;


        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mydrawerlist.size();
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

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.sdrawer_list_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.dTxt);
        holder.tv.setText(mydrawerlist.get(position).getCategoryName());

       /* rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/

        return rowView;
    }


}

