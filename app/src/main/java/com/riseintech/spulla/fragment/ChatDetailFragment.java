package com.riseintech.spulla.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riseintech.spulla.R;
import com.riseintech.spulla.adapter.FriendListAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ChatDetailFragment extends Fragment implements View.OnClickListener {
    public static final String KEY_IDCATEGORY = "id";
    public static final String KEY_PASSWORD = "password";
    ArrayList<HashMap<String, String>> listfreg;
    private ProgressDialog pDialog;

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ID_CATEGORY = "ID_CAT";

    private int mPage;
    private String mType;

    LinearLayout lyt_online;
    Button btn_frendlist, btn_frendonline;
    ListView list_frend;
    FriendListAdapter adpter;
    RequestQueue mRequestQueue;

    public static ChatDetailFragment newInstance(int page, String catId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ID_CATEGORY, catId);
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        String type = getArguments().getString(ID_CATEGORY);

        mType = type.replaceAll("\\s", "");

        //Log.w("ChatDetailFragment", "mCatId:" + mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_chat_fragment, container, false);

        lyt_online = (LinearLayout) view.findViewById(R.id.lyt_online);
        list_frend = (ListView) view.findViewById(R.id.list_frend);
        list_frend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        btn_frendlist = (Button) view.findViewById(R.id.btn_frendlist);
        btn_frendonline = (Button) view.findViewById(R.id.btn_frendonline);

        if (InternetStatus.isConnectingToInternet(getActivity())) {
            new GetFriendsList().execute();
        }


        if (mType.equalsIgnoreCase("Chat")) {
            lyt_online.setVisibility(View.VISIBLE);
            btn_frendlist.setOnClickListener(this);
            btn_frendonline.setOnClickListener(this);
            /*btn_frendlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });*/

        }


        // sendRequestFriends();
        listfreg = new ArrayList<HashMap<String, String>>();

        return view;
    }


    private void sendRequestFriends() {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        final String id = MyPreference.loadUserid(getActivity());
        //Log.w("GCMRegIntentService", "loadUserid:" + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_FRIEND_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String status = jsonObject.getString("status");
                            // Toast.makeText(getActivity(), "" + status, Toast.LENGTH_LONG).show();

                            if (status.equalsIgnoreCase("1")) {
                                //Log.w("ChatDetailFragment", "loadUserid:" + id);
                                JSONArray jsonArray = jsonObject.getJSONArray("items");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonOb = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    //int id = Integer.parseInt(jsonObject.optString(TAG_CATEGORY_ID).toString());

                                    hashMap.put("id", jsonOb.getString("id"));
                                    hashMap.put("name", jsonOb.getString("name"));
                                    hashMap.put("image", jsonOb.getString("image"));
                                    hashMap.put("date_time", jsonOb.getString("date_time"));
                                    listfreg.add(hashMap);
                                }
                                list_frend.setAdapter(new FriendListAdapter(getActivity(), listfreg, mType));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.w("ChatDetailFragment", "comurl:");
                        Toast.makeText(getActivity(), "sendRegistrationTokenToServer! ErrorListener", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", "sendfrom");
                params.put("id", id);

                return params;
            }
        };
        //Log.w("ChatDetailFragment", "ChatDetailFragment url" + stringRequest);
        addToRequestQueue(stringRequest);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_frendlist:

                list_frend.setAdapter(new FriendListAdapter(getActivity(), listfreg, mType));
                //Toast.makeText(getActivity(), "chat detail . "+mType, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_frendonline:
                list_frend.setAdapter(new FriendListAdapter(getActivity(), listfreg, mType));
                //startActivity(new Intent(getActivity(), ChatStartActivity.class));

                //Toast.makeText(getActivity(), "btn_frendonline.. "+mType, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    class GetFriendsList extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";
        final String id = MyPreference.loadUserid(getActivity());
        HashMap<String, String> params = new HashMap<>();


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait...");
            pDialog.show();
            params.put("type", mType);
            params.put("id", id);

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject json;
            if (mType.equalsIgnoreCase("IrrelevantProfiles")) {
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", id);
                json = jsonParser.makeHttpRequest(
                        Api.Irrelevant_Profile_LIST, "POST", params);
            } else {
                json = jsonParser.makeHttpRequest(
                        Api.URL_FRIEND_LIST, "POST", params);
            }
            try {
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
            String status = "";
            String data = "";
            //if (pDialog.isShowing())
            hidePDialog();

            if (json != null) {
                try {
                    status = json.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        listfreg = new ArrayList<HashMap<String, String>>();

                        JSONArray ja = json.getJSONArray("items");

                        Log.d("gdfgdfhbdfhdf",json.toString());
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jsonObject = ja.getJSONObject(i);
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", jsonObject.getString("id"));
                            hashMap.put("name", jsonObject.getString("name"));
                            hashMap.put("image", jsonObject.getString("image"));
                            hashMap.put("date_time", jsonObject.getString("date_time"));

                            Log.d("fdgvdfgdfghdbdfgdhd",jsonObject.getString("id"));
                            Log.d("fdgvdfgdfghdbdfgdhd",jsonObject.getString("name"));
                            Log.d("fdgvdfgdfghdbdfgdhd",jsonObject.getString("image"));
                            Log.d("fdgvdfgdfghdbdfgdhd",jsonObject.getString("date_time"));

                            listfreg.add(hashMap);
                        }
                        list_frend.setAdapter(new FriendListAdapter(getActivity(), listfreg, mType));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}






   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARG_PAGE);
        //mCatId=getArguments().getString(ID_CATEGORY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_chat_fragment, container, false);
       // pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
       // pDialog.setMessage("Loading...");
       // pDialog.show();
       // newscategorydet();
       *//* setListAdapter(new CategoryListAdapter(getActivity(), newsDrawerArray,newsDrawerimg));*//*
       // new Sign().execute();
        return view;
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }*/

// start volley
/*    public void newscategorydet() {

        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, CAT_URL, null,
                new Response.Listener<JSONObject>() {
                    NewsItem newsItem;
                    String data = "";
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                hidePDialog();
                                listfreg=new ArrayList<HashMap<String,String>>();

                                JSONArray ja = response.getJSONArray("item");

                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jsonObject = ja.getJSONObject(i);
                                    //int id = Integer.parseInt(jsonObject.optString(TAG_CATEGORY_ID).toString());
                                    String title = jsonObject.getString("title");
                                    String dis = jsonObject.getString("discription");
                                    String url = jsonObject.getString("img");
                                    data += "node= "+i +" \n category name= "+ title +" \n image url= "+ url +" \n category imgurl= "+ url;
                                    HashMap<String ,String> hashMap=new HashMap<String, String>();
                                    hashMap.put("title",title);
                                    hashMap.put("dis",dis);
                                    hashMap.put("url",url);
                                    listfreg.add(hashMap);
                                }
                                Log.d("All category data>>>>", data);
                                setListAdapter(new CategoryListAdapter(getActivity(), listfreg));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        hidePDialog();
                    }
                }
        ) { @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put(KEY_IDCATEGORY,"18");
            return params;
        }

    };
        requestQueue.add(jor);
    }*/
// end


   /* @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }*/


