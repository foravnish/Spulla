package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.riseintech.spulla.AsyncTaskService.GetPlayerDetailsAsync;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.DeliveryAddressDetails;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddDelivAddressActivity extends AppCompatActivity {
    Toolbar toolbar_add;
    EditText delv_add_name, delv_add_phn, delv_add_pnc, delv_add_cit, delv_add_state, delv_add, land_mark_edt, delv_altnt_add_phn;
    RadioGroup sGroup_addt;
    RadioButton add_work, add_home, address_rd;
    Button save_Add;
    String address_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deliv_address);
        toolbar_add = (Toolbar) findViewById(R.id.toolbar_add);
        toolbar_add.setTitle("Add Address");
        Util.isAddressSave = false;
        //*add toolbar to actionbar*//
        setSupportActionBar(toolbar_add);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inIt();
    }

    private void inIt() {
        delv_add_name = (EditText) findViewById(R.id.delv_add_name);
        land_mark_edt = (EditText) findViewById(R.id.land_mark_edt);
        delv_altnt_add_phn = (EditText) findViewById(R.id.delv_altnt_add_phn);
        delv_add_phn = (EditText) findViewById(R.id.delv_add_phn);
        delv_add_pnc = (EditText) findViewById(R.id.delv_add_pnc);
        delv_add_cit = (EditText) findViewById(R.id.delv_add_cit);
        delv_add_state = (EditText) findViewById(R.id.delv_add_state);
        delv_add = (EditText) findViewById(R.id.delv_add);
        save_Add = (Button) findViewById(R.id.save_Add);
        sGroup_addt = (RadioGroup) findViewById(R.id.sGroup_addt);
        add_work = (RadioButton) findViewById(R.id.add_work);
        add_home = (RadioButton) findViewById(R.id.add_home);


        delv_add_name.setText(DeliveryAddressDetails.getName());
        land_mark_edt.setText(DeliveryAddressDetails.getLandMark());
        delv_altnt_add_phn.setText(DeliveryAddressDetails.getAlternate_phone_number());
        delv_add_phn.setText(DeliveryAddressDetails.getPhone_number());
        delv_add_pnc.setText(DeliveryAddressDetails.getPincode());
        delv_add_cit.setText(DeliveryAddressDetails.getCity());
        delv_add_state.setText(DeliveryAddressDetails.getState());
        delv_add.setText(DeliveryAddressDetails.getAddress());

        if (DeliveryAddressDetails.getAdd_type().equalsIgnoreCase("Home")) {
            sGroup_addt.check(add_home.getId());
        } else if (DeliveryAddressDetails.getAdd_type().equalsIgnoreCase("work")) {
            sGroup_addt.check(add_work.getId());
        } else {
            sGroup_addt.clearCheck();
        }

        save_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sGroup_addt.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(AddDelivAddressActivity.this, "Please choose Address Type", Toast.LENGTH_SHORT).show();
                } else if (delv_add_name.getText().toString().isEmpty()) {
                    delv_add_name.setError(getString(R.string.empty_msg));
                } else if (delv_add_phn.getText().toString().isEmpty()) {
                    delv_add_phn.setError(getString(R.string.empty_msg));
                } else if (delv_add_phn.getText().toString().length() < 10) {
                    delv_add_phn.setError("Phone number not valid");
                } else if (delv_add_pnc.getText().toString().isEmpty()) {
                    delv_add_phn.setError(getString(R.string.empty_msg));
                } else if (delv_add.getText().toString().isEmpty()) {
                    delv_add_phn.setError(getString(R.string.empty_msg));
                } else if (!InternetStatus.isConnectingToInternet(AddDelivAddressActivity.this)) {
                } else {
                    address_rd = (RadioButton) sGroup_addt.findViewById(sGroup_addt.getCheckedRadioButtonId());
                    if (address_rd.getText().toString() != null) {
                        address_type = address_rd.getText().toString();
                    } else {
                        address_type = "Not available";
                    }
                    // p_addt.findViewById(sGroup_addt.getCheckedRadioButtonId());

                    new GetDelivAddress().execute();
                }
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

/////////////////////////////////////////////////////////////


    public class GetDelivAddress extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        HashMap<String, String> params = new HashMap<>();


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AddDelivAddressActivity.this);
            pDialog.setMessage("Please wait....");
            pDialog.setCancelable(false);
            pDialog.show();
            params.put("u_id", MyPreference.loadUserid(AddDelivAddressActivity.this));
            params.put("add_type", address_type);
            params.put("name", delv_add_name.getText().toString());
            params.put("mobile", delv_add_phn.getText().toString());
            params.put("alternet_mobile", delv_altnt_add_phn.getText().toString());
            params.put("landmark", land_mark_edt.getText().toString());
            params.put("pin_code", delv_add_pnc.getText().toString());
            params.put("address", delv_add.getText().toString());
            params.put("state", delv_add_state.getText().toString());
            params.put("city", delv_add_cit.getText().toString());


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.Add_Deliv_AddressUrl, "GET", params);
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

        protected void onPostExecute(JSONObject response) {

            if (pDialog.isShowing())
                pDialog.dismiss();
            new GetPlayerDetailsAsync(AddDelivAddressActivity.this).execute();
            if (response != null) {
                try {
                    //Log.e("respone", ": " + response);
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        Util.isAddressSave = true;
                        Toast.makeText(AddDelivAddressActivity.this, "Address save", Toast.LENGTH_SHORT).show();
                        DeliveryAddressDetails.setAdd_type(address_type);
                        DeliveryAddressDetails.setAddress(delv_add.getText().toString());
                        DeliveryAddressDetails.setAlternate_phone_number(delv_altnt_add_phn.getText().toString());
                        DeliveryAddressDetails.setCity(delv_add_cit.getText().toString());
                        DeliveryAddressDetails.setPhone_number(delv_add_phn.getText().toString());
                        DeliveryAddressDetails.setLandMark(land_mark_edt.getText().toString());
                        DeliveryAddressDetails.setState(delv_add_state.getText().toString());
                        DeliveryAddressDetails.setPincode(delv_add_pnc.getText().toString());
                        DeliveryAddressDetails.setName(delv_add_name.getText().toString());

                    } else {
                        Toast.makeText(AddDelivAddressActivity.this, "Address not save", Toast.LENGTH_SHORT).show();
                        Util.isAddressSave = false;

                    }
                    // Log.e("main responce", "" + response.optString("status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddDelivAddressActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }
}
