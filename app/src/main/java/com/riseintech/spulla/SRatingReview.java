package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Risein on 12/2/2016.
 */

public class SRatingReview extends AppCompatActivity {

    Toolbar toolbar_rating;
    String p_id;
    EditText edt_review;
    private ProgressDialog pDialog;
    RatingBar ratingbar1;
    Button btnrate;
    String rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rating);
        edt_review= (EditText) findViewById(R.id.edt_review);
        addListenerOnButtonClick();
        toolbar_rating = (Toolbar) findViewById(R.id.toolbar_rating);
        //*add toolbar to actionbar*//
        setSupportActionBar(toolbar_rating);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        p_id = intent.getStringExtra("pro_id");
        // Set ChangeListener to Rating Bar
        /*ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();

            }
        });*/



    }

    public  boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addListenerOnButtonClick(){
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);
        btnrate=(Button)findViewById(R.id.btnrate);
        //Performing action on Button Click
        btnrate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                rating=String.valueOf(ratingbar1.getRating());

                new SRatingReview.GiveRating().execute();

                //Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }

        });
    }




    class GiveRating extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        //String stype=type;
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SRatingReview.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("user_id", MyPreference.loadUserid(SRatingReview.this));
            params.put("product_id",p_id);
            params.put("ratting",rating);
            params.put("review",edt_review.getText().toString());
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.Sopping_SRating, "GET", params);
                //Log.d("JSON ", "------------" + json);
                if (json != null) {
                   /// Log.d("JSON result", json.toString());

                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(JSONObject json) {
            String userMobile = "";
            String userId = "";
            String userName = "";
            String success = "";
            String message = "";
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (json != null) {
                try {
                    String status = json.getString("status");
                    String msg = json.getString("msg");

                    if (status.equals("1")){

                       /* Intent intent=new Intent(SRatingReview.this,ShopItemDetailsActivity.class);
                        startActivity(intent);*/
                        Toast.makeText(SRatingReview.this,msg, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(SRatingReview.this,msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        }
    }


}
