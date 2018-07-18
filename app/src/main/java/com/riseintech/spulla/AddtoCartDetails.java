package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.adapter.AddtoCartAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddtoCartDetails extends AppCompatActivity implements View.OnClickListener {
    Toolbar atc_tool;
    ListView addcart_lv;
    TextView contine, cancel;
    ArrayList<HashMap<String, String>> cartlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart_details);
        inIt();
    }

    private void inIt() {
        atc_tool = (Toolbar) findViewById(R.id.atc_tool);
        atc_tool.setTitle("AddCart Items");
        //*add toolbar to actionbar*//
        setSupportActionBar(atc_tool);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cartlist = new ArrayList<>();
        addcart_lv = (ListView) findViewById(R.id.addcart_lv);
        contine = (TextView) findViewById(R.id.contine);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        contine.setOnClickListener(this);
        if (InternetStatus.isConnectingToInternet(AddtoCartDetails.this)) {
            new CartListAsync(AddtoCartDetails.this).execute();
        }
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
            case R.id.contine:
                startActivity(new Intent(AddtoCartDetails.this, BuyNow.class).putExtra("prod_list", cartlist));
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }


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
            pDialog.setMessage("Loading  ....");
            //  pDialog.show();

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
            try {
                if (jsonObject != null && jsonObject.getString("status").equals("1")) {
                    JSONArray cartArray = jsonObject.optJSONArray("item");
                    for (int j = 0; j < cartArray.length(); j++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("p_id", cartArray.getJSONObject(j).optString("p_id"));
                        map.put("product_name", cartArray.getJSONObject(j).optString("product_name"));
                        map.put("celling_price", cartArray.getJSONObject(j).optString("celling_price"));
                        map.put("colour", cartArray.getJSONObject(j).optString("p_color"));
                        map.put("p_size", cartArray.getJSONObject(j).optString("p_size"));
                        map.put("p_quantity", cartArray.getJSONObject(j).optString("p_quantity"));
                        map.put("img", cartArray.getJSONObject(j).optString("img"));
                        cartlist.add(map);
                    }
                    if (cartlist.size() > 0) {
                        addcart_lv.setAdapter(new AddtoCartAdapter(AddtoCartDetails.this, cartlist));
                    }
                } else {
                    Toast.makeText(context, "CartList not Available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    ///////////////////////////
}
