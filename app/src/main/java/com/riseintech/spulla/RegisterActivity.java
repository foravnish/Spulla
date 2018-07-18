package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.inputcontrols.InputSecurity;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by adm on 8/6/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    // uicontrols
    private EditText sEmail,sPass,sCPass,sName,sMobile;

    private Button sRegister;
    private ProgressDialog pDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_register);
     /* Initialize Radio Group and attach click handler */

        init();





        sRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (InputSecurity.validateInput(sName,sMobile,sEmail,sPass,sCPass,RegisterActivity.this)) {

                    hideKeyboard();
                    new RegisterPage().execute();

                    Intent in=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(in);
                }


               /* onSubmit();*/





            }
        });


    }


    private void init(){
        //find all id


        sName= (EditText) findViewById(R.id.s_name);
        sEmail= (EditText) findViewById(R.id.s_email);
        sPass= (EditText) findViewById(R.id.s_pass);
        sCPass= (EditText) findViewById(R.id.s_cpass);
        sMobile= (EditText) findViewById(R.id.s_contact);









        sRegister= (Button) findViewById(R.id.s_btnRegister);


    }



    class RegisterPage extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        String mobile=sMobile.getText().toString();
        String name=sName.getText().toString();
        String email=sEmail.getText().toString();
        String pass=sPass.getText().toString();
        String cpass=sCPass.getText().toString();


        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("mobile", mobile);
            params.put("name", name);
            params.put("email", email);
            params.put("password", pass);
            params.put("cpass", cpass);


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest("http://spulla.com/admin/spulla_api/regi.php", "GET", params);
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
            String userMobile = "";
            String userId = "";
            String userName = "";
            String success = "";
            String message = "";
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (json != null) {
                try {
                    userId=json.getString("user_id");
                    userName=json.getString("name");
                    userMobile=json.getString("mobile");
                    success = json.getString("status");
                    message = json.getString("msg");
                    ///Toast.makeText(RegisterActivity.this, "user name " + userName +" id is " +userId, Toast.LENGTH_SHORT).show();
                    //Log.d("message login>>>>",message);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            if (success.equals("1")) {
                //Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                MyPreference.saveUserstatus(RegisterActivity.this,true);
                MyPreference.saveUserid(RegisterActivity.this,userId);
                MyPreference.saveUsername(RegisterActivity.this,userName);
                MyPreference.saveUsermobile(RegisterActivity.this,userMobile);

            } else {
                Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void hideKeyboard()
    {
        if(getCurrentFocus()!=null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}

