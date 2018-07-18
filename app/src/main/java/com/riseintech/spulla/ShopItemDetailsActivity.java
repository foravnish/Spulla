package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.adapter.ShopItemDetailsPagerAdapter;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.model.MainCardModel;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    private MainCardModel mainCardModel;
    int pos;
    ViewPager shop_detail_pager;
    ArrayList<HashMap<String, String>> shopItem;

    public int cartNotificationCount;
    public TextView txtCartNotificationCount;
    private View viewCartBadge;
    private MenuItem cartBadgeNotificationItem;
    private MenuItem favoriteBadgeNotificationItem;

    /* TODO:
    * Type: View Elements
    * Description:
    * It's the layout of viewFavoriteBadge
    * layout: res/layout/favorite_badge.xml
    * */
    private View viewFavoriteBadge;

    /* TODO:
    * Type: View Elements
    * Description:
    * Display text notification on Actionbar item (Favorite Actionbar Item)
    * --- txtFavoriteNotificationCount => layout: res/layout/favorite_badge.xml, id = txt_favorite_badge
    * */
    public TextView txtFavoriteNotificationCount;

    /* TODO:
     * Type: Variable - Integer
	 * Description:
	 * favoriteNotificationCount used to count the number of favorite item with max value 99
	 * */
    private int favoriteNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_shop_item_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar_sh);
 /*add toolbar to actionbar*/
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shopItem = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("myshop");
        pos = getIntent().getIntExtra("pos", 0);

        shop_detail_pager = (ViewPager) findViewById(R.id.shop_detail_pager);


        if (shopItem != null && shopItem.size() > 0) {
            // facts_pager.setAdapter(new FactsDetailsAdapter(FactDetailsActivity.this, factList, total_cmmtLists.get(0)));

            shop_detail_pager.setAdapter(new ShopItemDetailsPagerAdapter(getSupportFragmentManager(), shopItem, "shopitem"));
            shop_detail_pager.setCurrentItem(pos, true);
        }
        shop_detail_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //ArrayList<HashMap<String, String>> cmtlist = new ArrayList<HashMap<String, String>>();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //currentpos = position;
            }

            @Override
            public void onPageSelected(int position) {
                //mCurrentPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //handleScrollState(state);
                //mScrollState = state;
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*case R.id.share_offer:
                shareTextUrl();
                break;

            case  R.id.icon:
                Intent intent=new Intent(ShopItemDetailsActivity.this,ZoomPhoto.class);
                startActivity(intent);
                break;

            default:
                break;
*/

        }

    }

    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();

        /* Remove all existing items from the menu, leaving it empty as if it had just been created*/
        menu.clear();

        // getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        inflater.inflate(R.menu.menu_product_detail, menu);

       /* favoriteBadgeNotificationItem = menu.findItem(R.id.favorite_badge_notification);

        *//*TODO: Set an action view for favorite menu item.*//*
        MenuItemCompat.setActionView(favoriteBadgeNotificationItem, R.layout.favorite_badge);

        *//*TODO: returns the currently set action view for favorite menu item.*//*
        viewFavoriteBadge = MenuItemCompat.getActionView(favoriteBadgeNotificationItem);

        *//*TODO: set Click listener for the favorite view*//*
        viewFavoriteBadge.setOnClickListener(listener);

        *//* TODO:
         * Define txtFavoriteNotificationCount from layout,
         * txtFavoriteNotificationCount visibility = "invisible", it's visible when favoriteNotificationCount > 0
         * see line 95.
         * --> res/layout/favorite_badge.xml
         * *//*
        txtFavoriteNotificationCount = (TextView)viewFavoriteBadge.findViewById(R.id.txt_favorite_badge);

        *//*TODO: set Click listener for the favorite textview*//*
        txtFavoriteNotificationCount.setOnClickListener(listener);

        *//* TODO: check if favoriteNotificationCount > 0 then it will display favorite text count on Actionbar Item *//*
        if(favoriteNotificationCount>0) {
            txtFavoriteNotificationCount.setVisibility(View.VISIBLE);

            *//* TODO:
            * favoriteNotificationCount has max value 99
            * and if favoriteNotificationCount >=100 Favorite Badge on actionbar will display +99
            * *//*
            if(favoriteNotificationCount>=100)
                txtFavoriteNotificationCount.setText("+99");
            else

            *//* TODO:
            * favoriteNotificationCount has max value 99
            * and if favoriteNotificationCount < 100 Favorite Badge on actionbar will display number 1-99
            * *//*
                txtFavoriteNotificationCount.setText(String.valueOf(favoriteNotificationCount));
        }*/

        cartBadgeNotificationItem = menu.findItem(R.id.cart_badge_notification);

        /*TODO: Set an action view for cart menu item.*/
        MenuItemCompat.setActionView(cartBadgeNotificationItem, R.layout.cart_badge);

        /*TODO: returns the currently set action view for cart menu item.*/
        viewCartBadge = MenuItemCompat.getActionView(cartBadgeNotificationItem);

        viewCartBadge.setOnClickListener(listener);

        /* TODO:
         * Define txtCartNotificationCount from layout,
         * txtCartNotificationCount visibility = "invisible", it's visible when cartNotificationCount > 0
         * see line 127.
         * --> res/layout/favorite_badge.xml
         * */
        txtCartNotificationCount = (TextView) viewCartBadge.findViewById(R.id.txt_cart_badge);
        txtCartNotificationCount.setOnClickListener(listener);

        if (cartNotificationCount > 0) {
            txtCartNotificationCount.setVisibility(View.VISIBLE);

            /* TODO:
            * cartNotificationCount has max value 99
            * and if cartNotificationCount >=100 Cart Badge on actionbar will display +99
            * */
            if (cartNotificationCount >= 100)
                txtCartNotificationCount.setText("+99");
            else

            /* TODO:
            * cartNotificationCount has max value 99
            * and if cartNotificationCount < 100 Cart Badge on actionbar will display number 1-99
            * */
                txtCartNotificationCount.setText(String.valueOf(cartNotificationCount));
        }

        return super.onCreateOptionsMenu(menu);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            badgeOnClickListener(v);
        }
    };


    public void badgeOnClickListener(View v) {
        /*TODO:
        *  isLogin = false; ==> the user has not logged in then the application will show Login activity
        *  isLogin = true; ==> the user has logged in then the application will show ProductDetailActivity
        * */
        if ((MyPreference.loadUserstatus(ShopItemDetailsActivity.this))) {

            if (v.getId() == R.id.cart_badge_layout || v.getId() == R.id.txt_cart_badge) {
                /*TODO :txt_cart_badget.
                ** startActivityForResult is to get item brand from tag of btnMoreProduct at ProductDetailActivity line: 404 - 415
                ** get the result (onActivityResult) on MainActivity and load list by criteria on CriteriaPlaceholderFragment
                **/
                Intent intent = new Intent(getApplicationContext(), AddtoCartDetails.class);
                startActivity(intent);
                Toast.makeText(ShopItemDetailsActivity.this, "cart_badge_layout", Toast.LENGTH_SHORT).show();
                /*Bundle extras = new Bundle();

                *//*TODO: ProductDetailActivity.PRODUCT_CART_FRAGMENT: it's display CartFragment when ProductDetailActivity is start.*//*
                extras.putInt("fragment",ProductDetailActivity.PRODUCT_CART_FRAGMENT);

                intent.putExtras(extras);
                startActivityForResult(intent, MainActivity.GET_CRITERIA);*/

            }
        } else {
            /*show LoginActivity if the user is not logged in*/
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    //TODO: set badge value and will be added to the previous value
    public void setCartNotificationCountAdd(int count, boolean addCart) {
        if (addCart == true) {
            cartNotificationCount = count;
        } else {
            cartNotificationCount = cartNotificationCount + count;

        }
        /*This function allows you to invalidate the options menu*/
        supportInvalidateOptionsMenu();
    }



    //TODO: set badge with value
    public void setCartNotificationCountDefault(int count) {
        cartNotificationCount = count;

        /*This function allows you to invalidate the options menu*/
        supportInvalidateOptionsMenu();
    }


    public int getCartNotificationCount() {
        return cartNotificationCount;
    }


    public MainCardModel getMainCardModel() {
        return mainCardModel;
    }

    public void setMainCardModel(MainCardModel mainCardModel) {
        this.mainCardModel = mainCardModel;
    }


    private class GetShopItemDetails extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        String ctype, ctype_id;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        ArrayList<HashMap<String, String>> hashMaps;
        //time_format 2016-10-24 04:39:18
        ListView listView;

        public GetShopItemDetails(Context context, String ctype, String ctype_id,
                                  ArrayList<HashMap<String, String>> hashMaps) {
            this.context = context;
            this.ctype = ctype;
            this.ctype_id = ctype_id;
            this.listView = listView;
            this.hashMaps = hashMaps;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading  ....");
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
            pDialog.dismiss();
            try {
                if (json.getString("status").equals("1")) {
                    Log.e("current time", ": " + Util.currentDateTime());
                    JSONArray jsonArray = json.getJSONArray("items");
                    hashMaps.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_title", jsonArray.getJSONObject(i).getString("title"));
                        map.put("comment", jsonArray.getJSONObject(i).getString("comments"));
                        map.put("date_time", jsonArray.getJSONObject(i).getString("date_time"));
                        map.put("cimg", jsonArray.getJSONObject(i).getString("img"));
                        hashMaps.add(map);
                    }
                    shop_detail_pager.setAdapter(new ShopItemDetailsPagerAdapter(getSupportFragmentManager(), shopItem, "shopitem"));
                    shop_detail_pager.setCurrentItem(pos, true);
                    shop_detail_pager.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Comments list not get", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }


}
