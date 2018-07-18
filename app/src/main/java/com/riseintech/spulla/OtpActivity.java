package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.riseintech.spulla.AsyncTaskService.OtpsendAsync;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OtpActivity extends AppCompatActivity {
    EditText otp_screen;
    Button otp_sub;
    String mobile_number;
    TextView resend_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp_sub = (Button) findViewById(R.id.otp_sub);
        otp_screen = (EditText) findViewById(R.id.otp_screen);
        resend_otp = (TextView) findViewById(R.id.resend_otp);
        resend_otp.setPaintFlags(resend_otp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        otp_screen.requestFocus(0);
        mobile_number = getIntent().getStringExtra("mobile_num");
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetStatus.isConnectingToInternet(OtpActivity.this)) {
                    new ResendOtpAsync().execute();
                }
            }
        });
        otp_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp_screen.getText().toString().isEmpty()) {
                    otp_screen.setError(getString(R.string.empty_msg));
                } else if (!InternetStatus.isConnectingToInternet(OtpActivity.this)) {
                } else {
                    new OtpsendAsync(OtpActivity.this, otp_screen.getText().toString(),mobile_number).execute();
                }

            }
        });
    }

    ////////////resend otp

    class ResendOtpAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        ProgressDialog pDialog;
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(OtpActivity.this);
            // pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("mobile", mobile_number);
            params.put("login_type", "mobiles");

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.Login, "GET", params);
                Log.d("JSON ", "------------" + json);
                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            Toast.makeText(OtpActivity.this, "" + json, Toast.LENGTH_SHORT).show();
            try {
                if (json.getString("status").equals("1")) {
                    //  Toast.makeText(OtpActivity.this, "Otp send", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtpActivity.this, "Some problem in resend otp", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    ///////////////////////////
}
