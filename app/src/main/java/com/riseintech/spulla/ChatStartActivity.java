package com.riseintech.spulla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riseintech.spulla.AsyncTaskService.SendMsgAsync;
import com.riseintech.spulla.AsyncTaskService.SendRequestAsync;
import com.riseintech.spulla.adapter.ThreadAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.helper.AppController;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.Message;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatStartActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView fnd_img;
    TextView fnd_name;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ArrayList<Message> messages;
    private RecyclerView.Adapter adapter;
    private EditText editTextMessage;
    private ImageView buttonSend, block_user;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);
        inIt();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSend:
                if (editTextMessage.getText().toString().isEmpty()) {
                    editTextMessage.setError(getString(R.string.empty_msg));
                } else if (!InternetStatus.isConnectingToInternet(ChatStartActivity.this)) {
                } else {

                    sendMessage();
                    new SendMsgAsync(ChatStartActivity.this, editTextMessage.getText().toString()).execute();
                    editTextMessage.setText("");

                }
                break;
            case R.id.fnd_img:
                Util.showFullImageDialog(ChatStartActivity.this, Util.Sender_Img, Util.Sender_Name);
                break;
            case R.id.block_user:
                if (InternetStatus.isConnectingToInternet(ChatStartActivity.this)) {
                    // sendRequest(Util.Sender_ID,"block");
                    new SendRequestAsync(ChatStartActivity.this, Util.Sender_ID, "block").execute();

                }
                break;
        }
    }

    private void inIt() {
        Util.ChatOn = 1;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fnd_img = (ImageView) toolbar.findViewById(R.id.fnd_img);
        block_user = (ImageView) toolbar.findViewById(R.id.block_user);
        fnd_name = (TextView) toolbar.findViewById(R.id.fnd_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Util.showImage(ChatStartActivity.this, Util.Sender_Img, fnd_img);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonSend = (ImageView) findViewById(R.id.buttonSend);
        buttonSend.setVisibility(View.GONE);

        //Initializing recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        fnd_name.setText(Util.Sender_Name);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        messages = new ArrayList<>();
        adapter = new ThreadAdapter(ChatStartActivity.this, messages, Integer.valueOf(MyPreference.loadUserid(ChatStartActivity.this)));
        recyclerView.setAdapter(adapter);
        scrollToBottom();
////////////////////////////
        fetchMessages();

        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    buttonSend.setVisibility(View.GONE);

                }
            }
        });

        buttonSend.setOnClickListener(this);
        fnd_img.setOnClickListener(this);
        block_user.setOnClickListener(this);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //This method will send the new message to the thread
    private void sendMessage() {
        final String message = editTextMessage.getText().toString().trim();
        if (message.equalsIgnoreCase(""))
            return;
        int userId = Integer.valueOf(MyPreference.loadUserid(ChatStartActivity.this));
        String sentAt = getTimeStamp();

        Message m = new Message(userId, message, sentAt);
        messages.add(m);
        adapter.notifyDataSetChanged();
        scrollToBottom();
    }

    /////////////////
    private void fetchMessages() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_FETCH_MESSAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   dialog.dismiss();
                        //Log.e("fetch msg rsp", ": " + response);
                        //Toast.makeText(ChatStartActivity.this, "response " + response, Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getString("status").equalsIgnoreCase("1")) {
                                JSONArray thread = res.getJSONArray("items");
                                for (int i = 0; i < thread.length(); i++) {
                                    JSONObject obj = thread.getJSONObject(i);
                                    String message = obj.getString("message");
                                    String sentAt = obj.getString("date_time");
                                    String sender_id = obj.getString("sender_id");
                                    Message messagObject = new Message(Integer.valueOf(sender_id),
                                            message, sentAt);
                                    messages.add(messagObject);
                                }
                                adapter = new ThreadAdapter(ChatStartActivity.this, messages, Integer.valueOf(MyPreference.loadUserid(ChatStartActivity.this)));
                                recyclerView.setAdapter(adapter);
                                scrollToBottom();
                            } else {
                                Toast.makeText(ChatStartActivity.this, "" + res.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatStartActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", MyPreference.loadUserid(ChatStartActivity.this));
                map.put("sender_id", Util.Sender_ID);

                Log.d("gfvgdgfdgdf",MyPreference.loadUserid(ChatStartActivity.this));
                Log.d("gfvgdgfdgdf",Util.Sender_ID);
                return map;

            }
        };
        //Log.e("request ", " " + stringRequest);
        addToRequestQueue(stringRequest);

    }


    /////////////////
      BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Util.Noti_Msg)) {
                //Getting message data
                String date_time = intent.getStringExtra("date_time");
                String message = intent.getStringExtra("message");
                String id = intent.getStringExtra("sender_id");

                //processing the message to add it in current thread
                processMessage(date_time, message, id);

            }
        }
    };

    //Processing message to add on the thread
    private void processMessage(String timestamp, String message, String id) {
        Message m = new Message(Integer.parseInt(id), message, timestamp);
        messages.add(m);
        scrollToBottom();
    }

    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1) {
            // recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    //This method will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("ChatActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Util.Noti_Msg));
    }

    @Override
    protected void onDestroy() {
        Util.ChatOn = 0;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onDestroy();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    //This method would add the requeust to the queue for execution
    public <T> void addToRequestQueue(Request<T> req) {
        //Setting a tag to the request
        req.setTag("ajskaj");

        //calling the method to get the request queue and adding the requeust the the queuue
        getRequestQueue().add(req);
    }

    private void sendRequest(final String reciver_id, final String rType) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        final String sender_id = MyPreference.loadUserid(ChatStartActivity.this);
        Log.w("GCMRegIntentService", "loadUserid:" + sender_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_SEND_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.w("response is : ", "" + s);
                            JSONObject jsonObject = new JSONObject(s);
                            // String msg = jsonObject.getString("msg");
                            if (rType.equalsIgnoreCase("block")) {
                                Toast.makeText(ChatStartActivity.this, "Block user", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ChatStartActivity.this, "Error " + volleyError, Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", rType);
                params.put("user_id", sender_id);
                params.put("sender_id", reciver_id);

                return params;
            }
        };
        Log.e("send request url>>", " " + stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
