package com.riseintech.spulla;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.riseintech.spulla.adapter.SDrawerAdapter;
import com.riseintech.spulla.adapter.ShopingDetailsPagerAdapter;
import com.riseintech.spulla.adapter.SportCatAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.fragment.ShopRentFragment;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.SoppingItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopingDetailsActivity extends AppCompatActivity {
    //navigation event
    public static boolean isActivityopen;
    SoppingItem newsItem;
    // Insert your Video URL
    private static final String TAG_CATEGORY_NAME = "category_name";
    private static final String TAG_IMG_URL = "img_url";
    RequestQueue requestQueue;
    String JsonURL = "http://spulla.com/admin/spulla_api/category_list.php";


    // end

    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tooltext, newhead;
    private NavigationView navigationView;
    private ListView mDrawerList;
    private DrawerLayout drawerLayout;
    protected FrameLayout frameLayout;
//Context context;

    public static String subcatId;

    protected static int position;
    private static boolean isLaunch = true;

    public static boolean isSubcat;

    ArrayList<SoppingItem> allcatdata;
    ArrayList<HashMap<String, String>> subcatlist;
    //end navgation event


    private ProgressDialog pDialog;
    Context context = ShopingDetailsActivity.this;
    ImageButton btn_drawer;
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<HashMap<String, String>> rentlist, firsthand, secondhand;
    private String type = "0";
    ShopingDetailsPagerAdapter adapter;
    int crt_pg_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Spulla");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

// all nav_drawer
        mDrawerList = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(toolbar);

        addDrawerItems();
        setupDrawer();
        allcatdata = new ArrayList<SoppingItem>();
        secondhand = new ArrayList<>();
        firsthand = new ArrayList<>();
        rentlist = new ArrayList<>();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        viewPager = (ViewPager) findViewById(R.id.shoping_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        newscategorybase();

        adapter = new ShopingDetailsPagerAdapter(ShopingDetailsActivity.this,
                getSupportFragmentManager());
        adapter.addFragment(new ShopRentFragment(), "First Hand", firsthand);
        adapter.addFragment(new ShopRentFragment(), "Second Hand", secondhand);
        adapter.addFragment(new ShopRentFragment(), "Rent", rentlist);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                crt_pg_pos = position;
            }

            @Override
            public void onPageSelected(int position) {
                crt_pg_pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //tooltext.setTextColor(getResources().getColor(R.color.colorPrimary));
        // toolbar.setSubtitle("Lavakush mishra");
        //Initializing NavigationView

        if (InternetStatus.isConnectingToInternet(ShopingDetailsActivity.this)) {

            if (isSubcat == true) {
                new ShopingDetailsActivity.RegisterPage().execute();
                isSubcat = false;
            } else {
                new ShopingDetailsActivity.RegisterPage().execute();
            }
        }
        //getSoppingDetail();
        //setupViewPager(viewPager);

     /*   for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);
            Button tabTextView = (Button) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();
        }*/
    }

    //nav all methods

    private void addDrawerItems() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                if (InternetStatus.isConnectingToInternet(ShopingDetailsActivity.this)) {
                    new SubCatData().execute();

                }
                drawerLayout.closeDrawer(navigationView);


                //Toast.makeText(context, "Time for an upgrade!", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setupDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                //Log.e("CategoryListActivity", "onDrawerOpened");

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }


    public void newscategorybase() {
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, JsonURL, null,
                new Response.Listener<JSONObject>() {
                    String data = "";

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            //hidePDialog();
                            try {

                                JSONArray ja = response.getJSONArray("items");
                                for (int i = 0; i < ja.length(); i++) {

                                    newsItem = new SoppingItem();
                                      JSONObject jsonObject = ja.getJSONObject(i);

                                    int id = Integer.parseInt(jsonObject.optString("id").toString());
                                    String title = jsonObject.getString("category_name");
                                    String url = jsonObject.getString("icon");
                                    data += "node= " + i + " \n category name= " + title + " \n image url= " + url + " \n category id= " + id;
                                    newsItem.setCategoryName(title);
                                    newsItem.setLink(url);
                                    newsItem.setCatId(id);
                                    allcatdata.add(newsItem);

                                }

								/*Toast.makeText(BaseActivity.this,"cat"+dracategoryList.size(),Toast.LENGTH_LONG).show();*/
                                //Log.d("All category data>>>>", data);
                                mDrawerList.setAdapter(new SDrawerAdapter(ShopingDetailsActivity.this, allcatdata));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        //hidePDialog();
                    }
                }
        );
        requestQueue.add(jor);
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


    class SubCatData extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ShopingDetailsActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("category_id", String.valueOf(allcatdata.get(position).getCatId()));
            //params.put("category_id","4");

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.Sopping_SubCat, "GET", params);
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
                    //String msg = obj.getString("msg");


                    if (status.equals("1")) {
                        JSONArray sRentArray = json.getJSONArray("items");
                        subcatlist = new ArrayList<>();
                        for (int i = 0; i < sRentArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            JSONObject c = sRentArray.getJSONObject(i);

                            hashMap.put("sub_cat_id", c.getString("subcategory_id"));
                            hashMap.put("category_name", c.getString("ecommerce_subcategory"));

                            subcatlist.add(hashMap);
                            //Toast.makeText(context, "sRentArray price>> "+ c.getString("ecommerce_subcategory"), Toast.LENGTH_LONG).show();
                        }

                        openMenuDialog(context, "facts", subcatlist);
                    } else {
                        Toast.makeText(context, "Products not available for " + allcatdata.get(position).getCategoryName(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {
                Toast.makeText(context, "Some problem exist", Toast.LENGTH_LONG).show();

            }

        }
    }


    private void openMenuDialog(final Context context, final String checktype, final ArrayList<HashMap<String, String>> categoryItem) {
       /* ArrayAdapter<String> adapter;
        String[] mTestArray;

        mTestArray = context.getResources().getStringArray(R.array.choose_sports_gossip);

        adapter = new ArrayAdapter<String>(
                context.getApplicationContext(),
                android.R.layout.select_dialog_singlechoice,
                mTestArray);
        adapter.setDropDownViewResource(R.layout.listtextview);*/
        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoglivescoremenu);
        dialog.setCancelable(true);
        Button submit = (Button) dialog.findViewById(R.id.disimiss);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final ListView listView = (ListView) dialog.findViewById(R.id.choose_sportlv);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(new SportCatAdapter(context, categoryItem));
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (checktype.contentEquals("facts")) {
                                              dialog.cancel();
                                              // Toast.makeText(context, "this is facts", Toast.LENGTH_SHORT).show();
                                          } else if (checktype.contentEquals("gossip")) {
                                              // Toast.makeText(context, "this is gossip", Toast.LENGTH_SHORT).show();
                                              dialog.cancel();
                                          } else {
                                              //Toast.makeText(context, "this is gossip", Toast.LENGTH_SHORT).show();
                                              dialog.cancel();
                                          }
                                      }
                                  }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                isSubcat = true;
                type = subcatlist.get(pos).get("sub_cat_id");
                //Toast.makeText(context,"this is subcat id "+subcatId,Toast.LENGTH_SHORT).show();
                if (isSubcat == true) {
                    new ShopingDetailsActivity.RegisterPage().execute();
                    isSubcat = false;
                } else {
                    new ShopingDetailsActivity.RegisterPage().execute();
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }

// all nav data


   /* public static void getSubCatId (String subcatId){

        // Get the Camera instance as the activity achieves full user focus
        if (isSubcat==true) {

            //Intent intent = getIntent();

            //String subcId = intent.getStringExtra("sub_cat_id");
            type = subcatId;
            Toast.makeText(ShopingDetailsActivity.this, "main Activity subcatId is : " +type, Toast.LENGTH_SHORT).show();

            new ShopingDetailsActivity.RegisterPage().execute();
            isSubcat=false;
        }
        else {
            type="0";
            //new ShopingDetailsActivity.RegisterPage().execute();
        }
    }*/

    class RegisterPage extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(ShopingDetailsActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("type", type);
            Log.d("gdfgdfhdfgdh",type);

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(Api.Sopping_Details, "GET", params);
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
                    //String msg = obj.getString("msg");


                    if (status.equals("1")) {

                        JSONArray sRentArray = json.optJSONArray("rent");
                        JSONArray sSecondArray = json.optJSONArray("second");
                        JSONArray first_hand_array = json.optJSONArray("first_hand");

                        parseShoppingData(sRentArray, sSecondArray, first_hand_array);
                        // Toast.makeText(ShopingDetailsActivity.this, status, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Information not available", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {
                Toast.makeText(context, "Information not available", Toast.LENGTH_LONG).show();
            }

        }
    }


    //This method will parse shopping json data
    private void parseShoppingData(JSONArray rent, JSONArray second, JSONArray first_hand_array) {

        if (first_hand_array != null) {
            firsthand.clear();
            for (int i = 0; i < first_hand_array.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                try {
                    JSONObject json = first_hand_array.getJSONObject(i);
                    hashMap.put("p_id", json.optString("p_id"));
                    hashMap.put("sub_cat_id", json.optString("sub_cat_id"));
                    hashMap.put("product_title", json.optString("product_title"));
                    hashMap.put("product_name", json.optString("product_name"));
                    hashMap.put("img", json.optString("img"));
                    hashMap.put("price_mrp", json.optString("price_mrp"));
                    hashMap.put("celling_price", json.optString("celling_price"));
                    hashMap.put("discount_percent", json.optString("discount_percent"));
                    hashMap.put("product_size", json.optString("product_size"));
                    hashMap.put("brand", json.optString("brand"));
                    hashMap.put("colour", json.optString("colour"));
                    hashMap.put("weight", json.optString("weight"));
                    hashMap.put("material", json.optString("material"));
                    hashMap.put("description", json.optString("description"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                firsthand.add(hashMap);
            }
           // Log.e("firsthandList", ": " + firsthand);
        } else {
            if (crt_pg_pos == 0) {
                Toast.makeText(context, "Products not available for First hand Category", Toast.LENGTH_LONG).show();
            }
        }
        if (rent != null) {
            rentlist.clear();
            for (int i = 0; i < rent.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                try {
                    JSONObject json = rent.getJSONObject(i);
                    hashMap.put("p_id", json.optString("p_id"));
                    hashMap.put("sub_cat_id", json.optString("sub_cat_id"));
                    hashMap.put("product_title", json.optString("product_title"));
                    hashMap.put("product_name", json.optString("product_name"));
                    hashMap.put("img", json.optString("img"));
                    hashMap.put("price_mrp", json.optString("price_mrp"));
                    hashMap.put("celling_price", json.optString("celling_price"));
                    hashMap.put("discount_percent", json.optString("discount_percent"));
                    hashMap.put("product_size", json.optString("product_size"));
                    hashMap.put("brand", json.optString("brand"));
                    hashMap.put("colour", json.optString("colour"));
                    hashMap.put("weight", json.optString("weight"));
                    hashMap.put("material", json.optString("material"));
                    hashMap.put("description", json.optString("description"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rentlist.add(hashMap);
            }
            //Log.e("rentlist", ": " + rentlist);

        } else {
            if (crt_pg_pos == 2) {
                Toast.makeText(context, "Products not available for Rent Category", Toast.LENGTH_LONG).show();
            }
        }
        if (second != null) {
            secondhand.clear();
            for (int i = 0; i < second.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                try {
                    JSONObject json = second.getJSONObject(i);

                    hashMap.put("p_id", json.optString("p_id"));
                    hashMap.put("sub_cat_id", json.optString("sub_cat_id"));
                    hashMap.put("product_title", json.optString("product_title"));
                    hashMap.put("product_name", json.optString("product_name"));
                    hashMap.put("img", json.optString("img"));
                    hashMap.put("price_mrp", json.optString("price_mrp"));
                    hashMap.put("celling_price", json.optString("celling_price"));
                    hashMap.put("discount_percent", json.optString("discount_percent"));
                    hashMap.put("product_size", json.optString("product_size"));
                    hashMap.put("brand", json.optString("brand"));
                    hashMap.put("colour", json.optString("colour"));
                    hashMap.put("weight", json.optString("weight"));
                    hashMap.put("material", json.optString("material"));
                    hashMap.put("description", json.optString("description"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                secondhand.add(hashMap);
            }
           //Log.e("secondhand", ": " + secondhand);
        } else {
            if (crt_pg_pos == 1) {
                Toast.makeText(context, "Products not available for Second hand Category", Toast.LENGTH_LONG).show();
            }

        }
        viewPager.getAdapter().notifyDataSetChanged();
    }


    private void setupViewPager(ViewPager viewPager) {
        ShopingDetailsPagerAdapter adapter = new ShopingDetailsPagerAdapter(ShopingDetailsActivity.this,
                getSupportFragmentManager());
        rentlist = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", "id");
            rentlist.add(map);
        }
        firsthand = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", "id");
            firsthand.add(map);
        }

        secondhand = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", "id");
            secondhand.add(map);
        }

        adapter.addFragment(new ShopRentFragment(), "Rent", rentlist);
        adapter.addFragment(new ShopRentFragment(), "First Hand", firsthand);
        adapter.addFragment(new ShopRentFragment(), "Second Hand", secondhand);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
