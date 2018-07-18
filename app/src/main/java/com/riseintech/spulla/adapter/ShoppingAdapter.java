package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseintech.spulla.R;
import com.riseintech.spulla.ShopItemDetailsActivity;
import com.riseintech.spulla.ShopingDetailsActivity;
import com.riseintech.spulla.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adm on 9/13/2016.
 */
public class ShoppingAdapter extends PagerAdapter {
    private ArrayList<HashMap<String, String>> shopiItems;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private TextView item_name, view_more,offer,hot_deal;
    ImageView shop_itme_img;
    public static String CatName = "not available";

    public ShoppingAdapter(Context activity, ArrayList<HashMap<String, String>> shopiItems) {
        this.mContext = activity;
        this.shopiItems = shopiItems;
    }

    @Override
    public int getCount() {
        return shopiItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.shopping_items, container, false);
        shop_itme_img = (ImageView) view.findViewById(R.id.shop_itme_img);
        if (shopiItems.get(position).get("img").contains(",")) {
            String img[] = shopiItems.get(position).get("img").split(",");
            Util.showImage(mContext, img[0], shop_itme_img);
        } else {
            Util.showImage(mContext, shopiItems.get(position).get("img"), shop_itme_img);
        }
        item_name = (TextView) view.findViewById(R.id.item_name);
        view_more = (TextView) view.findViewById(R.id.view_more);

        offer = (TextView) view.findViewById(R.id.offer);
        item_name.setText(shopiItems.get(position).get("product_name"));
        offer.setText(shopiItems.get(position).get("discount_percent") + "% off");
        hot_deal = (TextView) view.findViewById(R.id.hot_deal);

       /* Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        hot_deal.setAnimation(animation);*/

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(170);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        hot_deal.startAnimation(anim);

        view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "position " + position, Toast.LENGTH_SHORT).show();
                CatName = shopiItems.get(position).get("product_name");
                Intent intent = new Intent(view.getContext(), ShopingDetailsActivity.class);
                view.getContext().startActivity(intent);

            }
        });
        shop_itme_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShopItemDetailsActivity.class);
                intent.putExtra("myshop", shopiItems);
                intent.putExtra("pos", position);
                view.getContext().startActivity(intent);
            }
        });
        container.addView(view);

        // Return the View
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
