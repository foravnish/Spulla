package com.riseintech.spulla.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riseintech.spulla.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 11/9/2016.
 */

public class ShopingRecyclerviewAdapter extends RecyclerView.Adapter<ShopingRecyclerviewAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> shopingItems;

    public ShopingRecyclerviewAdapter(ArrayList<HashMap<String, String>> shopingItems) {
        this.shopingItems = shopingItems;

    }

    @Override
    public ShopingRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ShopingRecyclerviewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return shopingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
