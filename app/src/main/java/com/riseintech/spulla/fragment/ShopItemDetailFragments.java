package com.riseintech.spulla.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.riseintech.spulla.LoginActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.SRatingReview;
import com.riseintech.spulla.ShopItemDetailsActivity;
import com.riseintech.spulla.BuyNow;
import com.riseintech.spulla.ZoomPhoto;
import com.riseintech.spulla.adapter.SingleListAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.model.MainCardModel;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.ObjectStoreUtil;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Risein on 11/28/2016.
 */

public class ShopItemDetailFragments extends Fragment implements View.OnClickListener {

    public static MainCardModel mainCardModel;
    ShopItemDetailsActivity productDetailActivity;
    /* TODO:
    * Type: View Element - View
    * Description:
    * Positive Button for size dialog
    * */
    private View positiveActionSize;

    /* TODO:
     * Type: View Element - ListView
	 * Layout: res/layout/size_listview_dialog_layout.xml
	 * Description:
	 * ListView for size dialog
	 * */
    private ListView listViewSizeDialog;


    RatingBar ratingBar;
    ImageView icon;
    Context context;
    private ProgressDialog pDialog;
    int cartlist_size;
    private ArrayList<HashMap<String, String>> shopItemlist;
    TextView share_offer, buynow, cart;
    TextView pdt_name, pdt_detls, avl_color, avl_size;
    Button rate_btn, btnSize, select_color;
    int position;
    String url = "dkd";
    EditText cmts_hd;
    LinearLayout cmnt_ly;
    ArrayList<HashMap<String, String>> cmtlist = new ArrayList<>();
    String type;
    private String selectedSize = "", selected_color = "", quantity = "1";
    ////seller info
    TextView rtl_sellp;
    ////price dtls
    TextView sellprice, mrp_rate, offer;
    Button qty;
    /// specification
    TextView item_color, item_desc, item_type, bnd_name;
    String[] sizelist = {"small"}, colorlist = {"black"};
    List<String> addtoCartList = new ArrayList<>();

    public static ShopItemDetailFragments newInstance(int position, ArrayList<HashMap<String, String>> shopItemlist, String type) {
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putSerializable("shopItemlist", shopItemlist);
        args.putString("type", type);
        ShopItemDetailFragments fragment = new ShopItemDetailFragments();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        shopItemlist = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("shopItemlist");
        position = getArguments().getInt("pos", 0);
        type = getArguments().getString("type");

        // Log.e("factlist", "" + factslist);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_item_detail_fragments, container, false);
        //Log.e("shop item list", ": " + shopItemlist);
        mainCardModel = new MainCardModel();
        productDetailActivity = (ShopItemDetailsActivity) getActivity();

        if (InternetStatus.isConnectingToInternet(context)) {
            new CartListSizeAsync(context).execute();
        }

        if (shopItemlist.get(position).get("product_size").contains(",")) {
            sizelist = shopItemlist.get(position).get("product_size").split(",");
            selectedSize = sizelist[0];
        } else {
            // sizelist = {"amit"};
            selectedSize = shopItemlist.get(position).get("product_size");
        }

        if (shopItemlist.get(position).get("colour").contains(",")) {
            colorlist = shopItemlist.get(position).get("colour").split(",");
            selected_color = colorlist[0];
        } else {
            selected_color = shopItemlist.get(position).get("colour").toString();
        }
/////////rate details\
        sellprice = (TextView) view.findViewById(R.id.sellprice);
        mrp_rate = (TextView) view.findViewById(R.id.mrp_rate);
        offer = (TextView) view.findViewById(R.id.offer);
        pdt_name = (TextView) view.findViewById(R.id.pdt_name);
        pdt_detls = (TextView) view.findViewById(R.id.pdt_detls);
        avl_size = (TextView) view.findViewById(R.id.avl_size);
        avl_color = (TextView) view.findViewById(R.id.avl_color);
        qty = (Button) view.findViewById(R.id.qty);

        ratingBar= (RatingBar) view.findViewById(R.id.ratingBar);
        qty.setOnClickListener(this);
        avl_color.setText("Color Available = " + colorlist.length);
        avl_size.setText("Size Available = " + sizelist.length);

        //////////////////
        //////////////////rtaillers details
        rtl_sellp = (TextView) view.findViewById(R.id.rtl_sellp);
        rtl_sellp.setText(context.getString(R.string.Rs) + " " + shopItemlist.get(position).get("celling_price"));

        ///////////////
        ////////////////////specifications dtls
        item_color = (TextView) view.findViewById(R.id.item_color);
        item_desc = (TextView) view.findViewById(R.id.item_desc);
        item_type = (TextView) view.findViewById(R.id.item_type);
        bnd_name = (TextView) view.findViewById(R.id.bnd_name);

        item_color.setText(Util.fromHtml("<b>" + "Color: " + "</b>" + shopItemlist.get(position).get("colour")));
        item_desc.setText(Util.fromHtml("<b>" + "About: " + "</b>" + shopItemlist.get(position).get("description")));
        bnd_name.setText(Util.fromHtml("<b>" + "Brand: " + "</b>" + shopItemlist.get(position).get("brand")));
        item_type.setText(Util.fromHtml("<b>" + "Material: " + "</b>" + shopItemlist.get(position).get("material")));
        /////////////////////////////////////////////
        share_offer = (TextView) view.findViewById(R.id.share_offer);
        icon = (ImageView) view.findViewById(R.id.icon);
        rate_btn = (Button) view.findViewById(R.id.rate_btn);
        buynow = (TextView) view.findViewById(R.id.buynow);
        cart = (TextView) view.findViewById(R.id.cart);
        btnSize = (Button) view.findViewById(R.id.btn_add_size);
        select_color = (Button) view.findViewById(R.id.select_color);
        select_color.setOnClickListener(this);
        if (shopItemlist.get(position).get("img").contains(",")) {
            String img[] = shopItemlist.get(position).get("img").split(",");
            Util.showImage(context, img[0], icon);
        } else {
            Util.showImage(context, shopItemlist.get(position).get("img"), icon);
        }
        sellprice.setText(context.getString(R.string.Rs) + " " + shopItemlist.get(position).get("celling_price"));
        mrp_rate.setPaintFlags(offer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mrp_rate.setText(context.getString(R.string.Rs) + " " + shopItemlist.get(position).get("price_mrp"));
        offer.setText(shopItemlist.get(position).get("discount_percent") + "% off");
        pdt_name.setText(shopItemlist.get(position).get("product_name"));
        pdt_detls.setText(shopItemlist.get(position).get("product_title"));

        icon.setOnClickListener(this);
        share_offer.setOnClickListener(this);
        buynow.setOnClickListener(this);
        rate_btn.setOnClickListener(this);
        cart.setOnClickListener(this);
        btnSize.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       /* if (ObjectStoreUtil.countCart() > 0) {
            for (MainCardModel model : ObjectStoreUtil.getCartModel()) {

                Log.w("MainCardModel",""+ mainCardModel.getId().toString());

                if (model.getId().equals(mainCardModel.getId())) {
                    btnSize.setText(model.getSize());
                    btnSize.setTag(model.getSize());
                    ShopItemDetailsActivity detailActivity = (ShopItemDetailsActivity) getActivity();
                       *//* TODO:
                        * if the item has in cart list,
                        * setAddToCart value #mainCardModel @ProductDetailActivity to true, so that the user cannot add same item to cart list
                        * *//*
                    mainCardModel.setAddToCart(true);
                    detailActivity.getMainCardModel().setAddToCart(true);
                    break;
                }
            }
        }*/
    }

    @Override
    public void onResume() {
       if (InternetStatus.isConnectingToInternet(context)) {
            new GetRatingAsync(context).execute();
        }
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_offer:
                shareTextUrl();
                break;

            case R.id.icon:
                Intent intent = new Intent(context, ZoomPhoto.class).putExtra("url", shopItemlist.get(position).get("img"));
                startActivity(intent);
                break;

            case R.id.rate_btn:
                Intent inrating = new Intent(context, SRatingReview.class).putExtra("pro_id",shopItemlist.get(position).get("p_id"));
                startActivity(inrating);
                break;
            case R.id.select_color:
                showColorDialog(colorlist);
                break;
            case R.id.qty:
                //addQtyDialog();
                break;
            case R.id.btn_add_size:
                if (mainCardModel.isAddToCart()) {
                    //showWarningCart(shopItemlist.get(position).get("pTitle"));
                    //showWarningCart(mainCardModel.getHeaderTitle());
                } else {
                    showSizeDialog(sizelist);
                    //mainCardModel.setAddToCart(false);
                }
                //showSizeDialog();
                mainCardModel.setAddToCart(false);

                break;

            case R.id.cart:

                //TODO: check if the user has logged in to app. only logged in user allowed add item to cart list
                if (MyPreference.loadUserstatus(context) &&
                        InternetStatus.isConnectingToInternet(context)) {
                    String size = (String) btnSize.getTag();
                    //String size ="S";
                    boolean IsAlready_AddToCart = false;
                    if (size == null) {
                        showWarningEmptySize();
                    } else {
                        for (int g = 0; g < addtoCartList.size(); g++) {
                            //Log.w("cart list pid", ": " + addtoCartList.get(g));
                            //Log.w("shopItemlist", ": " + shopItemlist.get(position).get("p_id"));
                            if (addtoCartList.get(g)
                                    .equalsIgnoreCase(shopItemlist.get(position).get("p_id").toString())) {
                                IsAlready_AddToCart = true;
                                //Log.w("IsAlready_AddToCart", ": " + IsAlready_AddToCart);
                                break;
                            }

                        }


                        if (IsAlready_AddToCart == true) {
                            showWarningCart(shopItemlist.get(position).get("product_name"));
                        } else {
                            ShopItemDetailsActivity productDetailActivity = (ShopItemDetailsActivity) getActivity();

                            /*Add the item into cart list with integer parameter */
                            productDetailActivity.setCartNotificationCountAdd(1, false);

                            /* Update the item value => setAddToCart(true)
                            * */
                            mainCardModel.setAddToCart(false);
                            /*Add the item into Cart List*/
                            ObjectStoreUtil.getCartModel().add(mainCardModel);
                            new ItemAddCard().execute();
                        }
                    }
                } else {
                    /* Show Login form if the user is not Logged in*/
                    Intent frint = new Intent(context, LoginActivity.class);
                    startActivity(frint);
                }
                break;

            case R.id.buynow:
                ArrayList<HashMap<String, String>> prodlist = new ArrayList<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("p_id", shopItemlist.get(position).get("p_id"));
                map.put("product_name", shopItemlist.get(position).get("product_name"));
                map.put("celling_price", shopItemlist.get(position).get("celling_price"));
                map.put("colour", selected_color);
                map.put("p_size", selectedSize);
                map.put("p_quantity", quantity);
                map.put("img", shopItemlist.get(position).get("img"));
                prodlist.add(map);
                Log.d("gfdgdfgdf",prodlist.toString());
                Intent in = new Intent(context, BuyNow.class).putExtra("prod_list", prodlist);
                startActivity(in);
                break;

            default:
                break;
        }
    }


    private void showWarningFavorite(String itemName) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.drawable.ic_warning_black)
                .title("Warning")
                .content(itemName + " has added to favorite list!!")
                //.typeface(FontTypefaceUtils.getRobotoCondensedBold(getActivity()),FontTypefaceUtils.getRobotoCondensedRegular(getActivity()))
                .positiveText("Ok")
                .show();
    }

    private void showWarningCart(String itemName) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.drawable.ic_warning_black)
                .title("Warning")
                .content(itemName + " has already added to cart list!!")
                //.typeface(FontTypefaceUtils.getRobotoCondensedBold(getActivity()),FontTypefaceUtils.getRobotoCondensedRegular(getActivity()))
                .positiveText("Ok")
                .show();
    }

    private void showWarningEmptySize() {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.drawable.ic_warning_black)
                .title("Warning")
                .content("Please select size to add cart!!")
                //.typeface(FontTypefaceUtils.getRobotoCondensedBold(getActivity()),FontTypefaceUtils.getRobotoCondensedRegular(getActivity()))
                .positiveText("Ok")
                .show();
    }

    private void showSizeDialog(String[] strings) {

        /*Load value of size from resource -> res/values/arrays.xml*/
        // final List<String> keys = Arrays.asList(getResources().getStringArray(R.array.size_key));
        final List<String> keys = new ArrayList<>();
        for (int k = 0; k < strings.length; k++) {
            keys.add(strings[k]);
        }
        /*Create new MaterialDialog object with custom layout
        * Description:
        *  Layout => res/layout/size_listview_dialog_layout.xml
        * */
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Size")
                .customView(R.layout.size_listview_dialog_layout, false)
                .positiveText("Done")
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        btnSize.setText(selectedSize);
                        btnSize.setTag(selectedSize);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        btnSize.setText(R.string.btn_size);
                        btnSize.setTag(null);
                    }
                }).build();

         /*Disable button (positiveActionSize) of MaterialDialog
        * */
        positiveActionSize = dialog.getActionButton(DialogAction.POSITIVE);
        positiveActionSize.setEnabled(false);

        /*Define listview from layout of MaterialDialog*/
        listViewSizeDialog = (ListView) dialog.getCustomView().findViewById(R.id.size_dialog_listview);

        /*Load String Array size from resource and cast to String Array List*/
        List<String> asList = keys;

        /*Create new Adapter object for listViewSizeDialog
        * and set @asList as parameter value of SingleListAdapter*/
        SingleListAdapter adapter = new SingleListAdapter(getActivity(), asList);
        listViewSizeDialog.setAdapter(adapter);
        listViewSizeDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {

                /*Set selectedSize value from position of @keys*/
                selectedSize = keys.get(position);


                mainCardModel.setSize(selectedSize);

                /* Save size index/position to mainCardModel.
                * this index/position is for spinner-size at CartCardList.java
                * see => CartCardList.java -> spinnerSize.setSelection(mainCardModel.getSizeIndex());
                * */
                mainCardModel.setSizeIndex(position);

                /*Enabled button (positiveActionSize) when user clicked ListView */
                positiveActionSize.setEnabled(true);
            }
        });

        dialog.show();
    }

    private void showColorDialog(String[] strings) {

        /*Load value of size from resource -> res/values/arrays.xml*/
        // final List<String> keys = Arrays.asList(getResources().getStringArray(R.array.size_key));
        final List<String> keys = new ArrayList<>();
        for (int k = 0; k < strings.length; k++) {
            keys.add(strings[k]);
        }
        /*Create new MaterialDialog object with custom layout
        * Description:
        *  Layout => res/layout/size_listview_dialog_layout.xml
        * */
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Color")
                .customView(R.layout.size_listview_dialog_layout, false)
                .positiveText("Done")
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        select_color.setText(selected_color);
                        select_color.setTag(selected_color);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        select_color.setText("Choose Color");
                        select_color.setTag(null);
                    }
                }).build();

         /*Disable button (positiveActionSize) of MaterialDialog
        * */
        positiveActionSize = dialog.getActionButton(DialogAction.POSITIVE);
        positiveActionSize.setEnabled(false);

        /*Define listview from layout of MaterialDialog*/
        listViewSizeDialog = (ListView) dialog.getCustomView().findViewById(R.id.size_dialog_listview);

        /*Load String Array size from resource and cast to String Array List*/
        // List<String> asList = Arrays.asList(getResources().getStringArray(R.array.size_text));

        /*Create new Adapter object for listViewSizeDialog
        * and set @asList as parameter value of SingleListAdapter*/
        SingleListAdapter adapter = new SingleListAdapter(getActivity(), keys);
        listViewSizeDialog.setAdapter(adapter);
        listViewSizeDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {

                /*Set selectedSize value from position of @keys*/
                selected_color = keys.get(position);


                // mainCardModel.set(selectedSize);

                /* Save size index/position to mainCardModel.
                * this index/position is for spinner-size at CartCardList.java
                * see => CartCardList.java -> spinnerSize.setSelection(mainCardModel.getSizeIndex());
                * */
                // mainCardModel.setSizeIndex(position);

                /*Enabled button (positiveActionSize) when user clicked ListView */
                positiveActionSize.setEnabled(true);
            }
        });

        dialog.show();
    }

        /*Disable button (positiveActionSize) of MaterialDialog
        * */


    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    class ItemAddCard extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        //String stype=type;
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        final String id = MyPreference.loadUserid(getActivity());
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("user_id", id);
            params.put("item_id", shopItemlist.get(position).get("p_id"));
            params.put("p_color", selected_color);
            params.put("p_size", selectedSize);
            params.put("p_quantity", quantity);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.Sopping_Addcard, "GET", params);
                //Log.d("JSON ", "------------" + json);
                if (json != null) {
                    //Log.d("JSON result", json.toString());

                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(JSONObject json) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (json != null) {
                try {
                    String status = json.getString("status");
                    String msg = json.getString("msg");

                    if (status.equals("1")) {

                       /* Intent intent=new Intent(SRatingReview.this,ShopItemDetailsActivity.class);
                        startActivity(intent);*/
                        Toast.makeText(context, "Item added", Toast.LENGTH_LONG).show();
                        new CartListAsync(getActivity()).execute();
                    } else {
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }
    }


    ////////////////////////////
    public class CartListSizeAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        String item_id;

        public CartListSizeAsync(Context context) {
            this.context = context;
            this.item_id = item_id;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading  ....");
            //  pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            params.put("user_id", MyPreference.loadUserid(context));

            JSONObject json = jsonParser.makeHttpRequest(Api.CartList_Size_Url, "GET", params);
            if (json != null) {
                //Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) {

            try {
                if (jsonObject != null && jsonObject.getString("status").equals("1")
                        && !jsonObject.getString("num_item").matches("")) {
                            /*Add the item into cart list with integer parameter */
                    cartlist_size = Integer.parseInt(jsonObject.getString("num_item"));
                    //Log.e("num of item", ": " + jsonObject.getString("num_item"));
                    productDetailActivity.setCartNotificationCountAdd(cartlist_size, true);
                         /* Update the item value => setAddToCart(true)
                            * */

                    new CartListAsync(getActivity()).execute();
                    if (Integer.parseInt(jsonObject.getString("num_item")) > 0) {
                        mainCardModel.setAddToCart(false);
                    }

                            /*Add the item into Cart List*/
                    ObjectStoreUtil.getCartModel().add(mainCardModel);
                       /* Intent intent=new Intent(SRatingReview.this,ShopItemDetailsActivity.class);
                        startActivity(intent);*/

                } else {
                    cartlist_size = 0;
                    Toast.makeText(context, "Item not added in Add to cart", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //////////////////////
    ////////////////////////////
    public class CartListAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        String item_id;

        public CartListAsync(Context context) {
            this.context = context;
            this.item_id = item_id;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading  ....");
             pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            params.put("user_id", MyPreference.loadUserid(context));

            JSONObject json = jsonParser.makeHttpRequest(Api.CartList_Url, "GET", params);
            if (json != null) {
                //Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            try {
                if (jsonObject != null && jsonObject.getString("status").equals("1")) {
                    JSONArray cartArray = jsonObject.optJSONArray("item");
                    addtoCartList.clear();
                    for (int j = 0; j < cartArray.length(); j++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("p_id", cartArray.getJSONObject(j).optString("p_id"));
                        addtoCartList.add(cartArray.getJSONObject(j).optString("p_id"));
                        //Log.w("addcart item id", ": " + cartArray.getJSONObject(j).optString("p_id"));
                    }
                } else {
                    //Toast.makeText(context, "CartList not Available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    public class GetRatingAsync extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        Context context;
        JSONParser jsonParser;
        HashMap<String, String> params = new HashMap<>();
        String item_id;

        public GetRatingAsync(Context context) {
            this.context = context;
            this.item_id = item_id;
            jsonParser = new JSONParser();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  pDialog.show();

        }

        protected JSONObject doInBackground(String... args) {
            params.put("user_id", MyPreference.loadUserid(context));
            params.put("product_id",shopItemlist.get(position).get("p_id"));

            Log.d("fgddfgdhgdhdgd",MyPreference.loadUserid(context));
            Log.d("fgddfgdhgdhdgd",shopItemlist.get(position).get("p_id"));

            JSONObject json = jsonParser.makeHttpRequest(Api.Sopping_SgetRating, "GET", params);
            if (json != null) {
                Log.e("json response", ": " + json);
                return json;
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) {
            try {
                Log.w("jsonObject","my value :"+jsonObject);
                if (jsonObject != null && jsonObject.getString("status").equals("1")) {
                    if( !jsonObject.getString("rating").equalsIgnoreCase("0")|| !jsonObject.getString("rating").equalsIgnoreCase("0")) {
//                        String s =  jsonObject.getString("rating");
//                        float f = Float.parseFloat(s);
//                        //Toast.makeText(context, "Rating value is : " + s, Toast.LENGTH_SHORT).show();
//                        //Log.w("rating value is","my value :"+f);
//                        ratingBar.setRating(f);
//                        //ratingBar.setRating(Float.parseFloat(jsonObject.getString("rating")));
                    }
                    else {
                        Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Rating not Available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    ///////////////////////////

}
