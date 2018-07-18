package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.adapter.BuyingListAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.DeliveryAddressDetails;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein on 11/28/2016.
 */

public class BuyNow extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ListView prod_list;
    TextView change_address;
    Button button;
    TextView cancel, contine;
    LinearLayout address_ly;
    // address details
    public static double payable_amt;
    TextView delv_user_name, delv_user_mbn, del_add, del_add_lnd;
    // Product details
    TextView price_dtls, amtpay_dtls;
    ArrayList<HashMap<String, String>> prodlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitem_color_size);


        //addListenerOnButtonClick();
        toolbar = (Toolbar) findViewById(R.id.toolbar_csh);
        toolbar.setTitle("Delivery Details");
        //*add toolbar to actionbar*//
        setSupportActionBar(toolbar);
        payable_amt = 0;
        Util.Add_Qty = false;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prodlist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("prod_list");
        inIt();

    }

    private void inIt() {
        if (InternetStatus.isConnectingToInternet(BuyNow.this)) {
            new GetDelivAddress().execute();
        }
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.buylistfooter, null);
        prod_list = (ListView) findViewById(R.id.prod_list);
        prod_list.addFooterView(footerView, null, false);
        if (prodlist.size() > 0) {
            prod_list.setAdapter(new BuyingListAdapter(BuyNow.this, prodlist));
        }
        change_address = (TextView) footerView.findViewById(R.id.change_address);
        address_ly = (LinearLayout) footerView.findViewById(R.id.address_ly);
        amtpay_dtls = (TextView) footerView.findViewById(R.id.amtpay_dtls);
        price_dtls = (TextView) footerView.findViewById(R.id.price_dtls);

        address_ly.setVisibility(View.GONE);
        del_add_lnd = (TextView) footerView.findViewById(R.id.del_add_lnd);
        del_add = (TextView) footerView.findViewById(R.id.del_add);
        delv_user_mbn = (TextView) footerView.findViewById(R.id.delv_user_mbn);
        delv_user_name = (TextView) footerView.findViewById(R.id.delv_user_name);
        contine = (TextView) findViewById(R.id.contine);
        cancel = (TextView) findViewById(R.id.cancel);
        change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyNow.this, AddDelivAddressActivity.class));

            }
        });
        cancel.setOnClickListener(this);
        contine.setOnClickListener(this);
        for (int j = 0; j < prodlist.size(); j++) {
            payable_amt = payable_amt + Double.valueOf(prodlist.get(j).get("celling_price").toString());
        }

        amtpay_dtls.setText(getString(R.string.Rs) + " " + payable_amt);
        price_dtls.setText(getString(R.string.Rs) + " " + payable_amt);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.contine:
                startActivity(new Intent(BuyNow.this, MakePaymentActivity.class));
                break;
        }
    }


   /* public void addListenerOnButtonClick(){
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);
        button=(Button)findViewById(R.id.button1);
        //Performing action on Button Click
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating=String.valueOf(ratingbar1.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }

        });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isAddressSave) {
            delv_user_name.setText(DeliveryAddressDetails.getName());
            del_add.setText(DeliveryAddressDetails.getAddress());
            del_add_lnd.setText(DeliveryAddressDetails.getLandMark());
            delv_user_mbn.setText(DeliveryAddressDetails.getPhone_number());
            address_ly.setVisibility(View.VISIBLE);
        }
       /* if (Util.Add_Qty == true) {

        }*/

    }

    public void totalPrice() {
        for (int j = 0; j < prodlist.size(); j++) {
            payable_amt = 0;
            payable_amt = payable_amt + Double.valueOf(prodlist.get(j).get("celling_price").toString());
           // Log.e("payable amount", ": " + payable_amt);
        }
        amtpay_dtls.setText(getString(R.string.Rs) + " " + payable_amt);
        price_dtls.setText(getString(R.string.Rs) + " " + payable_amt);
    }

/////////////////////////////////////////////////////////////


    public class GetDelivAddress extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        HashMap<String, String> params = new HashMap<>();


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(BuyNow.this);
            pDialog.setMessage("Please wait....");
            pDialog.setCancelable(false);
            pDialog.show();
            params.put("u_id", MyPreference.loadUserid(BuyNow.this));
            //  params.put("location", location);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.Get_Deliv_AddressUrl, "GET", params);
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

        protected void onPostExecute(JSONObject result) {

            if (pDialog.isShowing())
                pDialog.dismiss();
            //  new GetPlayerDetailsAsync(BuyNow.this).execute();
            if (result != null) {
                try {
                    //Log.e("respone", ": " + result);
                    if (result.getString("status").equalsIgnoreCase("1")) {

                        JSONObject response = result.getJSONObject("items");
                        DeliveryAddressDetails.setAdd_type(response.optString("add_type"));
                        DeliveryAddressDetails.setAddress(response.optString("address"));
                        DeliveryAddressDetails.setAlternate_phone_number(response.optString("alternet_mobile"));
                        DeliveryAddressDetails.setCity(response.optString("city"));
                        DeliveryAddressDetails.setPhone_number(response.optString("mobile"));
                        DeliveryAddressDetails.setLandMark(response.optString("landmark"));
                        DeliveryAddressDetails.setState(response.optString("state"));
                        DeliveryAddressDetails.setPincode(response.optString("pin_code"));
                        DeliveryAddressDetails.setName(response.optString("name"));

                        delv_user_name.setText(DeliveryAddressDetails.getName());
                        del_add.setText(DeliveryAddressDetails.getAddress());
                        del_add_lnd.setText(DeliveryAddressDetails.getLandMark());
                        delv_user_mbn.setText(DeliveryAddressDetails.getPhone_number());

                        address_ly.setVisibility(View.VISIBLE);
                    } else {
                        change_address.setText("Add Address");
                        address_ly.setVisibility(View.GONE);

                    }
                    ;
                    // Log.e("main responce", "" + response.optString("status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BuyNow.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }
}
