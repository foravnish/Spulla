package com.riseintech.spulla;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.riseintech.spulla.adapter.SportCatAdapter;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.SoppingItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Risein on 11/30/2016.
 */

public class SBaseActivity extends AppCompatActivity {
    public static boolean isActivityopen;
    SoppingItem newsItem;
    // Insert your Video URL
    private ProgressDialog pDialog;
    private static final String TAG_CATEGORY_NAME="category_name";
    private static final String TAG_IMG_URL = "img_url";
    RequestQueue requestQueue;
    String JsonURL ="http://spulla.com/admin/spulla_api/category_list.php";
    // end

    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView tooltext,newhead;
    private NavigationView navigationView;
    private ListView mDrawerList;
    private DrawerLayout drawerLayout;
    protected FrameLayout frameLayout;
//Context context;

    Context context = SBaseActivity.this ;

    public static String subcatId;

    protected static int position;
    private static boolean isLaunch = true;

    public static boolean isSubcat;

    ArrayList<SoppingItem> allcatdata;
    ArrayList<HashMap<String, String>> subcatlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sopping_navigation_drawer);
        //getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar12);
        frameLayout = (FrameLayout)findViewById(R.id.frame);

        mDrawerList = (ListView)findViewById(R.id.navList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        addDrawerItems();
        setupDrawer();
        allcatdata=new ArrayList<SoppingItem>();
        newscategorybase();
        //tooltext.setTextColor(getResources().getColor(R.color.colorPrimary));
        // toolbar.setSubtitle("Lavakush mishra");
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        //pDialog.setMessage("Loading...");
        //pDialog.show();

    }

    private void addDrawerItems() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position=pos;

                new SBaseActivity.SubCatData().execute();

                drawerLayout.closeDrawer(navigationView);

                Toast.makeText(context, "Time for an upgrade!", Toast.LENGTH_SHORT).show();

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

                //Log.e("CategoryListActivity","onDrawerOpened");

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();



    }


    protected void openActivity(int position) {



        drawerLayout.closeDrawer(navigationView);
        //BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                if (isActivityopen){


                    //MainActivity mainActivity=new MainActivity();
                   // mainActivity.openMenuDialog(this,"facts",mainActivity.spullaFacts);
                    //startActivity(new Intent(this,ShopItemDetailsActivity.class));
                    Toast.makeText(this,"Working on category api",Toast.LENGTH_SHORT).show();
                    isActivityopen=false;
                }else{
                    //startActivity(new Intent(this, CategoryListActivity.class));
                }
                break;
            case 1:
                //startActivity(new Intent(this, ShopItemDetailsActivity.class));
                Toast.makeText(this,"Working on category api",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //startActivity(new Intent(this, ShopItemDetailsActivity.class));
                Toast.makeText(this,"Working on category api",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                //startActivity(new Intent(this, ShopItemDetailsActivity.class));
                Toast.makeText(this,"Working on category api",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                //startActivity(new Intent(this, ShopItemDetailsActivity.class));
                Toast.makeText(this,"Working on category api",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        //Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
    }

    public void clickImage(Toolbar toolbar) {

        setSupportActionBar(toolbar);
    }


/*    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Locate MenuItem with ShareActionProvider
        *//*MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider =(ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());
        // Return true to display menu
        return true;

    }
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "http://apee.info/");*//*

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {

                // perform query here

                searchView.clearFocus();

                return true;

            }

            @Override

            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }*/


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
                                    newsItem=new SoppingItem();
                                    JSONObject jsonObject = ja.getJSONObject(i);

                                    int id = Integer.parseInt(jsonObject.optString("id").toString());
                                    String title = jsonObject.getString("category_name");
                                    String url = jsonObject.getString("icon");

                                    data += "node= "+i +" \n category name= "+ title +" \n image url= "+ url +" \n category id= "+ id;

                                    newsItem.setCategoryName(title);
                                    newsItem.setLink(url);
                                    newsItem.setCatId(id);
                                    allcatdata.add(newsItem);

                                }

								/*Toast.makeText(BaseActivity.this,"cat"+dracategoryList.size(),Toast.LENGTH_LONG).show();*/
                                //Log.d("All category data>>>>", data);
                                mDrawerList.setAdapter(new SDrawerAdapter(SBaseActivity.this,allcatdata));
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
                        //hidePDialog();
                    }
                }
        );
        requestQueue.add(jor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

	/*public void baseCatData(ArrayList<NewsItem> mybasedata){
		mDrawerList.setAdapter(new DrawerAdapter(BaseActivity.this,mybasedata));

	}
*/



    class SubCatData extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        //String stype=type;
        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("category_id",String.valueOf(allcatdata.get(position).getCatId()));
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
                    //String msg = obj.getString("msg");


                    if (status.equals("1")){

                        JSONArray sRentArray = json.getJSONArray("items");
                        subcatlist=new ArrayList<>();
                       for (int i = 0; i < sRentArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            JSONObject c = sRentArray.getJSONObject(i);

                            hashMap.put("sub_cat_id",c.getString("subcategory_id"));
                            hashMap.put("category_name",c.getString("ecommerce_subcategory"));

                           subcatlist.add(hashMap);
                            //Toast.makeText(context, "sRentArray price>> "+ c.getString("ecommerce_subcategory"), Toast.LENGTH_LONG).show();
                        }

                        openMenuDialog(context,"facts",subcatlist);

                        Toast.makeText(context,status, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(context,status, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
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
                                              Toast.makeText(context, "this is facts", Toast.LENGTH_SHORT).show();
                                          } else if (checktype.contentEquals("gossip")) {
                                              Toast.makeText(context, "this is gossip", Toast.LENGTH_SHORT).show();
                                              dialog.cancel();
                                          } else {
                                              Toast.makeText(context, "this is gossip", Toast.LENGTH_SHORT).show();
                                              dialog.cancel();
                                          }
                                      }
                                  }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                isSubcat=true;
                subcatId=subcatlist.get(pos).get("sub_cat_id");
               Toast.makeText(context,"this is subcat id "+subcatId,Toast.LENGTH_SHORT).show();
                /*Intent intent=new Intent(context,ShopingDetailsActivity.class);
                intent.putExtra("sub_cat_id",subcatlist.get(pos).get("sub_cat_id"));
                startActivity(intent);*/
               /* idcat = categoryItem.get(i).get("id");
                cat_type = checktype;
                //Toast.makeText(context,"this is gossip "+idcat,Toast.LENGTH_SHORT).show();
                new MainActivity.GetCatItems().execute();*/

                //getCategory(idcat);
                dialog.cancel();
            }
        });
        dialog.show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Toast.makeText(this, "SBase Activity onRestart : ", Toast.LENGTH_SHORT).show();
        // Activity being restarted from stopped state
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Toast.makeText(this, "SBase Activity onResume : ", Toast.LENGTH_SHORT).show();
        // Get the Camera instance as the activity achieves full user focus
        if (isSubcat==true) {

            //Intent intent = getIntent();

            //String subcId = intent.getStringExtra("sub_cat_id");
            //type = subcatId;


           // new ShopingDetailsActivity.RegisterPage().execute();
            isSubcat=false;
        }
        else {
            //type="0";
            //new ShopingDetailsActivity.RegisterPage().execute();
        }
    }

}