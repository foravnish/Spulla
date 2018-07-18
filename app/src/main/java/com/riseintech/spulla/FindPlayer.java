package com.riseintech.spulla;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.riseintech.spulla.adapter.SportCatAdapter;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;
import com.riseintech.spulla.utils.Util;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by adm on 9/14/2016.
 */
public class FindPlayer extends AppCompatActivity implements View.OnClickListener, CropImageView.OnGetCroppedImageCompleteListener, CropImageView.OnSetImageUriCompleteListener {

    private EditText sName, sDob, sProfession, sCompany, sDesignation, sSchool, sCollege, sMaster, sClub, sMember;
    private RadioGroup radioGroup, sRGroupGender, sGroupplay, sGroupRate2, sGroupRate3;
    private RadioButton rBtnGender, s_m, s_fe,s_all, rBtnGroup3;
    TextView sFindaPlayer, play_as, sel_interest1, sel_interest2, sel_interest3, sSport1, sSport2, sSport3;
    static String gender, group1 = "single", sports_sts1 = "", sports_sts2 = "", sports_sts3 = "";
    private Toolbar toolbar;
    private ProgressDialog pDialog;
    ImageView propic, upld_imbtn,img_info;
    ArrayList<HashMap<String, String>> spullaFindplayer;
    static ArrayList<HashMap<String, String>> sFormSportCategory;
    int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Bitmap bitmap;
    Uri fileUri;
    String filepath;
    String fileName;
    String name, sport1, sport2, sport3, country, state, city, sprs, profession, company, designation,
            school, college, master, club, club2, dob;
    AutoCompleteTextView add_edt;
    LatLng latLng;
    String localAddress;
    LinearLayout main_ly, crop_ly;
    Button crop_cancel, crop;
    private CropImageView cropImageView;
    LinearLayout linerLayout;
    TextView seeMore;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sfind_player);
      /*  if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_find);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Player");
        spullaFindplayer = new ArrayList<HashMap<String, String>>();
        sFormSportCategory = new ArrayList<HashMap<String, String>>();
        initView();
        sGroupplay.clearCheck();
        sFindaPlayer = (TextView) findViewById(R.id.textfindplayer);
        propic = (ImageView) findViewById(R.id.propic);
        upld_imbtn = (ImageView) findViewById(R.id.upld_imbtn);
        img_info= (ImageView) findViewById(R.id.img_info);
        linerLayout=(LinearLayout)findViewById(R.id.linerLayout);
        seeMore=(TextView)findViewById(R.id.seeMore);
        img_info.setOnClickListener(this);
        upld_imbtn.setOnClickListener(this);
        propic.setOnClickListener(this);
        sFindaPlayer.setOnClickListener(this);
        Picasso.with(FindPlayer.this).load(PlayerDetails.getImage_url())
                .placeholder(R.color.colorRedittxt)
                .error(R.drawable.propic)
                .into(propic);
        if (InternetStatus.isConnectingToInternet(FindPlayer.this)) {
            new LoadImageAsync(FindPlayer.this, PlayerDetails.getImage_url()).execute();
        }

        new GetFormCategory(FindPlayer.this, "Individual").execute();
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag==1) {
                    linerLayout.setVisibility(View.GONE);
                    seeMore.setText("See More");
                    flag=0;
                }
                else if(flag==0){
                    linerLayout.setVisibility(View.VISIBLE);
                    seeMore.setText("Hide");
                    flag=1;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnGetCroppedImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cropImageView.setOnSetImageUriCompleteListener(null);
        cropImageView.setOnGetCroppedImageCompleteListener(null);
    }

//////////////////open dialog for play as options

    public void openMenuDialog(Context context, final TextView play_as, final String type) {
        final String[] value = new String[1];
        final ArrayAdapter<String> adapter;
        final String[] mTestArray;
        if (type.equalsIgnoreCase("Sports status")) {
            mTestArray = context.getResources().getStringArray(R.array.spaorts_interest_array);
        } else {
            mTestArray = context.getResources().getStringArray(R.array.play_as_array);
        }
        adapter = new ArrayAdapter<String>(
                context.getApplicationContext(),
                android.R.layout.select_dialog_singlechoice,
                mTestArray);
        adapter.setDropDownViewResource(R.layout.listtextview);
        final Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
      /*
        ///////for open from left to right animaion and close from left to right
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
      */
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER_VERTICAL);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialoglivescoremenu);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        Button submit = (Button) dialog.findViewById(R.id.disimiss);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final ListView listView = (ListView) dialog.findViewById(R.id.choose_sportlv);
        listView.setAdapter(adapter);
        title.setText(type);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group1 = listView.getItemAtPosition(0).toString();
                if (play_as.getText().toString().equalsIgnoreCase("Click below")) {
                    play_as.setText(group1);
                } else if (play_as.getText().toString().equalsIgnoreCase("Select expertise")) {
                    play_as.setText(group1);
                }
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (type.equalsIgnoreCase("Play as")&& InternetStatus.isConnectingToInternet(FindPlayer.this)) {
                                              new GetFormCategory(FindPlayer.this, group1).execute();
                                              sSport1.setText("Sport 1");
                                              sSport2.setText("Sport 2");
                                              sSport3.setText("Sport 3");
                                          }

                                          Log.d("gfdgdfgdfdghdfdhd",group1);
                                          play_as.setText(group1);
                                          dialog.cancel();
                                      }
                                  }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                group1 = listView.getItemAtPosition(i).toString();
                // play_as.setText(group1);
            }
        });
        dialog.show();
    }


    //////////////
    private void initView() {
        ///////////////crtop widget
        main_ly = (LinearLayout) findViewById(R.id.main_ly);
        crop_ly = (LinearLayout) findViewById(R.id.crop_ly);
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        crop_cancel = (Button) findViewById(R.id.crop_cancel);
        crop = (Button) findViewById(R.id.crop);
        crop_cancel.setOnClickListener(this);
        crop.setOnClickListener(this);

        /////////////////////////////
        //find all id
        add_edt = (AutoCompleteTextView) findViewById(R.id.add_edt);
        add_edt.setAdapter(new GooglePlacesAutocompleteAdapter(FindPlayer.this, R.layout.list_item));
        add_edt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                localAddress = (String) adapterView.getItemAtPosition(position);
                Toast.makeText(FindPlayer.this, localAddress, Toast.LENGTH_SHORT).show();
            }
        });
        play_as = (TextView) findViewById(R.id.play_as);
        sel_interest3 = (TextView) findViewById(R.id.sel_interest3);
        sel_interest2 = (TextView) findViewById(R.id.sel_interest2);
        sel_interest1 = (TextView) findViewById(R.id.sel_interest1);

        sName = (EditText) findViewById(R.id.s_tName);
        sDob = (EditText) findViewById(R.id.s_tage);
        sDesignation = (EditText) findViewById(R.id.s_tDesi);
        //sState = (EditText) findViewById(R.id.s_tState);
        sSchool = (EditText) findViewById(R.id.s_tSchooling);
        //sCity = (EditText) findViewById(R.id.s_tCity);
        //sSprs = (EditText) findViewById(R.id.s_tSprs);
        sProfession = (EditText) findViewById(R.id.s_tProfession);
        sCompany = (EditText) findViewById(R.id.s_tCompany);
        sCollege = (EditText) findViewById(R.id.s_tCollege);
        sMaster = (EditText) findViewById(R.id.s_tMaster);
        //sCountry = (EditText) findViewById(R.id.s_tCountry);
        sClub = (EditText) findViewById(R.id.s_tClub);
        sMember = (EditText) findViewById(R.id.s_tMember);
        sSport1 = (TextView) findViewById(R.id.s_tinterest);
        sSport2 = (TextView) findViewById(R.id.s_tinterest2);
        sSport3 = (TextView) findViewById(R.id.s_tinterest3);

        sRGroupGender = (RadioGroup) findViewById(R.id.sGroup_Gender);
        sGroupplay = (RadioGroup) findViewById(R.id.sGroup_play);
        s_m = (RadioButton) findViewById(R.id.s_m);
        s_fe = (RadioButton) findViewById(R.id.s_fe);
        s_all= (RadioButton) findViewById(R.id.s_all);
        play_as.setOnClickListener(this);
        sSport1.setOnClickListener(this);
        sSport2.setOnClickListener(this);
        sSport3.setOnClickListener(this);
        sel_interest1.setOnClickListener(this);
        sel_interest2.setOnClickListener(this);
        sel_interest3.setOnClickListener(this);

        if (PlayerDetails.getGender().equalsIgnoreCase("male")) {
            sRGroupGender.check(s_m.getId());
        } else if (PlayerDetails.getGender().equalsIgnoreCase("female")) {
            sRGroupGender.check(s_fe.getId());

        } else if (PlayerDetails.getGender().equalsIgnoreCase("all")) {
            sRGroupGender.check(s_all.getId());

        }
        else {
            sRGroupGender.clearCheck();
        }
        // get selected radio button from radioGroup
        //int selectedId = sRGroupGender.getCheckedRadioButtonId();

//////////////set value for player details
        add_edt.setText(PlayerDetails.getAddress());
        if (PlayerDetails.getPlay_As().matches("")) {
            play_as.setText(getString(R.string.click_below));
        } else {
            play_as.setText(PlayerDetails.getPlay_As());
        }
        sName.setText(PlayerDetails.getName());
        sDob.setText(PlayerDetails.getAge());
        sDesignation.setText(PlayerDetails.getDesignation());
        sSchool.setText(PlayerDetails.getSchooling());
        //sState.setText(PlayerDetails.getState());
        //sCity.setText(PlayerDetails.getCity());
        //sSprs.setText(PlayerDetails.getSprs());


        if (PlayerDetails.getSports_Interest1().matches("")) {
            sSport1.setHint(getString(R.string.sports1));
        } else {
            sSport1.setText(PlayerDetails.getSports_Interest1());
        }
        if (PlayerDetails.getSports_Interest2().matches("")) {
            sSport2.setHint(getString(R.string.sports2));
        } else {
            sSport2.setText(PlayerDetails.getSports_Interest2());
        }
        if (PlayerDetails.getSports_Interest3().matches("")) {
            sSport3.setHint(getString(R.string.sports3));
        } else {
            sSport3.setText(PlayerDetails.getSports_Interest3());
        }
        if (PlayerDetails.getSports_Interest_status1().matches("")) {
            sel_interest1.setText(getString(R.string.select_expertise));
        } else {
            sel_interest1.setText(PlayerDetails.getSports_Interest_status1());
        }

        if (PlayerDetails.getSports_Interest_status2().matches("")) {
            sel_interest2.setText(getString(R.string.select_expertise));
        } else {
            sel_interest2.setText(PlayerDetails.getSports_Interest_status2());
        }
        if (PlayerDetails.getSports_Interest_status3().matches("")) {
            sel_interest3.setText(getString(R.string.select_expertise));
        } else {
            sel_interest3.setText(PlayerDetails.getSports_Interest_status3());
        }

        sProfession.setText(PlayerDetails.getProfession());
        sCompany.setText(PlayerDetails.getCompany());
        sCollege.setText(PlayerDetails.getCollege());
        sMaster.setText(PlayerDetails.getMasters());
        //sCountry.setText(PlayerDetails.getCountry());
        sClub.setText(PlayerDetails.getClub_member());
        sMember.setText(PlayerDetails.getClub_member1());

        localAddress = add_edt.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.propic:
                final Dialog dialog = new Dialog(FindPlayer.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.getWindow()
                        .getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.showfullimage);
                ImageView back_img = (ImageView) dialog.findViewById(R.id.back_img);
                ImageView fact_image = (ImageView) dialog.findViewById(R.id.fact_image);
                Picasso.with(FindPlayer.this).load(MyPreference.loadUserPicUrl(FindPlayer.this))
                        .placeholder(R.color.colorRedt)
                        .error(R.drawable.propic).into(fact_image);
                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText(sName.getText().toString());
                back_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.play_as:
                openMenuDialog(FindPlayer.this, play_as, "Play as");
                break;

            case R.id.s_tinterest:
                openSportDialog(FindPlayer.this, "sport",sSport1, sFormSportCategory);
                //openMenuDialog(FindPlayer.this, play_as, "Play as");
                break;

            case R.id.s_tinterest2:
                openSportDialog(FindPlayer.this, "sport",sSport2,sFormSportCategory);
                //openMenuDialog(FindPlayer.this, play_as, "Play as");
                break;

            case R.id.s_tinterest3:
                openSportDialog(FindPlayer.this, "sport",sSport3, sFormSportCategory);
                //openMenuDialog(FindPlayer.this, play_as, "Play as");
                break;

            case R.id.sel_interest1:
                openMenuDialog(FindPlayer.this, sel_interest1, "Sports status");
                break;
            case R.id.sel_interest2:
                openMenuDialog(FindPlayer.this, sel_interest2, "Sports status");
                break;
            case R.id.sel_interest3:
                openMenuDialog(FindPlayer.this, sel_interest3, "Sports status");
                break;
            case R.id.crop:
                cropImageView.getCroppedImageAsync(500, 500);
                main_ly.setVisibility(View.VISIBLE);
                crop_ly.setVisibility(View.GONE);
                break;
            case R.id.crop_cancel:
                main_ly.setVisibility(View.VISIBLE);
                crop_ly.setVisibility(View.GONE);
                break;

            case R.id.img_info:
                Util.showInfoAlertDialog(FindPlayer.this, "Player Info", getString(R.string.player_info));
                break;

            case R.id.upld_imbtn:
                    CropImage.startPickImageActivity(this);
              /*  Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i.createChooser(i, "Select Picture"), CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
       */
                break;
            case R.id.textfindplayer:
                if (sRGroupGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FindPlayer.this, "Please choose gender", Toast.LENGTH_SHORT).show();
                } else if (!InternetStatus.isConnectingToInternet(FindPlayer.this)) {
                } else if (filepath == null) {
                    Toast.makeText(FindPlayer.this, "Please upload image", Toast.LENGTH_SHORT).show();
                } else {
                    rBtnGender = (RadioButton) sRGroupGender.findViewById(sRGroupGender.getCheckedRadioButtonId());
                    //rBtnGroup1 = (RadioButton) sGroupplay.findViewById(sGroupplay.getCheckedRadioButtonId());
                    //group1 = rBtnGroup1.getText().toString();

                    if (rBtnGender.getText().toString() != null) {
                        gender = rBtnGender.getText().toString();
                    } else {
                        gender = "";
                    }
                    latLng = Util.getLocationFromAddress(this.getApplicationContext(), localAddress);
                    Toast.makeText(FindPlayer.this, "lat: "
                            + latLng.latitude + ", lang: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                    name = sName.getText().toString();
                    sport1 = sSport1.getText().toString();
                    sport2 = sSport2.getText().toString();
                    sport3 = sSport3.getText().toString();
                    country = "";
                    state = "";
                    city = "";
                    sprs = "";
                    profession = sProfession.getText().toString();
                    company = sCompany.getText().toString();
                    designation = sDesignation.getText().toString();
                    school = sSchool.getText().toString();
                    college = sCollege.getText().toString();
                    master = sMaster.getText().toString();
                    club = sClub.getText().toString();
                    club2 = sMember.getText().toString();
                    dob = sDob.getText().toString();
                    hideKeyboardFind();
                    new FindPlayerPage().execute();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            //Log.e("onActivityResult", ": " + imageUri);
            fileUri = imageUri;
            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                //Log.e("ReadExternalStoragePer", ": " + imageUri);
                fileUri = imageUri;
                            /* try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        propic.setImageBitmap(bitmap);
                        //  new UploadFileToServer().execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
            if (!requirePermissions) {
                //Log.e("requirePermissions", ": " + requirePermissions);
                cropImageView.setImageUriAsync(imageUri);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (fileUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Log.e("onRequestPermissi", "");
            cropImageView.setImageUriAsync(fileUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
            if (bitmap != null) {
                //Log.e("onGetCroppedImageCompl", " ");
                fileUri = Util.getImageUri(FindPlayer.this, bitmap);
                filepath = Util.getPathFromUri(FindPlayer.this, fileUri);
                fileName = Util.imagename(FindPlayer.this, fileUri);
                propic.setImageBitmap(bitmap);
                cropImageView.setImageBitmap(bitmap);
                main_ly.setVisibility(View.VISIBLE);
                crop_ly.setVisibility(View.GONE);
            }
        } else {
            //Log.e("Crop", "Failed to crop image", error);
            Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        //Log.e("onSetImageUriComplete", " ");

        main_ly.setVisibility(View.GONE);
        crop_ly.setVisibility(View.VISIBLE);
        if (error != null) {
            //Log.e("Crop", "Failed to load image for cropping", error);
            Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }

    class FindPlayerPage extends AsyncTask<String, String, JSONObject> {
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(FindPlayer.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = uploadImage(FindPlayer.this, Api.Add_Player, filepath, fileName);
//JSONObject json = jsonParser.makeHttpRequest(Api.Add_Player, "GET", params);
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
            String success = "";
            String message = "";
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (json != null) {
                try {
                    success = json.getString("status");
                    message = json.getString("msg");
                    //Toast.makeText(FindPlayer.this, "" + success, Toast.LENGTH_SHORT).show();
                    //Log.d("message login>>>>", message);
                    JSONObject playerdata = json.getJSONObject("data");
                    parseFindData(playerdata);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (success.equals("1")) {
                Intent intent;
                intent = new Intent(FindPlayer.this, MainActivity.class);
                intent.putExtra("findplayer", spullaFindplayer);
                intent.putExtra("isfind", true);
                startActivity(intent);


            } else {
                Toast.makeText(FindPlayer.this, "" + message, Toast.LENGTH_SHORT).show();
            }
        }

    }



    static class GetFormCategory extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        HashMap<String, String> params = new HashMap<>();
        String play_as;
        Context context;
        public GetFormCategory(Context context, String play_as) {
            this.play_as = play_as;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("playas", play_as);
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {
                JSONObject json = jsonParser.makeHttpRequest(
                        Api.Get_Form_Category, "GET", params);
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
            String success = "";
            String message = "";
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (json != null) {
                try {
                    success = json.getString("status");
                    message = json.getString("msg");
                    //Log.d("message login>>>>", message);
                    if (json.getString("status").equalsIgnoreCase("1")){
                        JSONArray jsonArray = json.getJSONArray("item");
                        parseSportsType(jsonArray);

                    }
                    else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }



    //This method will parse shopping json data
    private void parseFindData(JSONObject json) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            String id = json.getString("id");
            String name = json.getString("name");

            String playas = json.getString("playas");
            String sports1 = json.getString("sports1");
            String sports2 = json.getString("sports2");
            String sports3 = json.getString("sports3");
            String sports_status1 = json.optString("sports1_status");
            String sports_status2 = json.optString("sports2_status");
            String sports_status3 = json.optString("sports3_status");


            String mobile = json.getString("mobile");
            String country = json.getString("country");
            String city = json.getString("city");
            String state = json.getString("state");
            String profession = json.getString("profession");

            String company = json.getString("company");
            String designation = json.getString("designation");
            String schooling = json.getString("schooling");
            String college = json.getString("college");
            String masters = json.getString("masters");
            String clubmember1 = json.getString("clubmember1");
            String clubmember2 = json.getString("clubmember2");
            String dob = json.getString("dob");
            String gender = json.getString("gender");
            String image_url = json.getString("images");
            if (!image_url.isEmpty()) {
                MyPreference.saveUserPicUrl(FindPlayer.this, image_url);
                PlayerDetails.setImage_url(image_url);
            }
            MyPreference.save_IsAdd(FindPlayer.this, 1);
            MyPreference.saveUsername(FindPlayer.this, name);
            PlayerDetails.setName(name);
            PlayerDetails.setAge(dob);
            PlayerDetails.setGender(gender);

            PlayerDetails.setAddress(localAddress);
            PlayerDetails.setCity(city);
            PlayerDetails.setState(state);
            PlayerDetails.setCountry(country);

            PlayerDetails.setClub_member(clubmember1);
            PlayerDetails.setClub_member1(clubmember2);
            PlayerDetails.setSports_Interest1(sports1);
            PlayerDetails.setSports_Interest2(sports2);
            PlayerDetails.setSports_Interest3(sports3);
            PlayerDetails.setSports_Interest_status1(sports_status1);
            PlayerDetails.setSports_Interest_status2(sports_status2);
            PlayerDetails.setSports_Interest_status3(sports_status3);

            PlayerDetails.setSprs(playas);
            PlayerDetails.setPlay_As(playas);

            PlayerDetails.setCollege(college);
            PlayerDetails.setSchooling(schooling);
            PlayerDetails.setCompany(company);
            PlayerDetails.setDesignation(designation);
            PlayerDetails.setMasters(masters);
            PlayerDetails.setProfession(profession);
            if (!json.getString("lat").equalsIgnoreCase("null")
                    || !json.getString("long").equalsIgnoreCase("null")) {
                PlayerDetails.setLat(json.getString("lat"));
                PlayerDetails.setLang(json.getString("long"));

            } else {
                PlayerDetails.setLat("0");
                PlayerDetails.setLang("0");
            }
            PlayerDetails.setAddress(Util.getAddress(FindPlayer.this,
                    PlayerDetails.getLat(), PlayerDetails.getLang()));


            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("playas", playas);
            hashMap.put("sports1", sports1);
            hashMap.put("sports2", sports2);
            hashMap.put("sports3", sports3);
            hashMap.put("mobile", mobile);
            hashMap.put("country", country);
            hashMap.put("city", city);
            hashMap.put("state", state);
            hashMap.put("profession", profession);
            hashMap.put("company", company);
            hashMap.put("designation", designation);
            hashMap.put("schooling", schooling);
            hashMap.put("college", college);
            hashMap.put("masters", masters);
            hashMap.put("clubmember1", clubmember1);
            hashMap.put("clubmember2", clubmember2);
            hashMap.put("dob", dob);
            hashMap.put("gender", gender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        spullaFindplayer.add(hashMap);


    }

    public JSONObject uploadImage(Context context, String url, String sourceImageFile,
                                  String fileName) {
        sports_sts1 = sel_interest1.getText().toString();
        sports_sts2 = sel_interest2.getText().toString();
        sports_sts3 = sel_interest3.getText().toString();
        group1 = play_as.getText().toString();
        //Log.e("sourceImageFile", ": " + sourceImageFile);
        File sourceFile = new File(sourceImageFile);
        String result = null;
        //Log.e("FindPlayerPageAsync", "File...::::" + sourceFile + " : " + sourceFile.exists());
        //Log.e("file name", ": " + fileName);
        JSONObject jsonObject = null;
        try {
            final MediaType MEDIA_TYPE_PNG = sourceImageFile.endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/*");
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("image_name", fileName, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("user_id", MyPreference.loadUserid(context))
                    .addFormDataPart("lat", "" + latLng.latitude)
                    .addFormDataPart("name", name)
                    .addFormDataPart("long", "" + latLng.longitude)
                    .addFormDataPart("playas", group1)
                    .addFormDataPart("sports1", sport1)
                    .addFormDataPart("sports1_status", sports_sts1)
                    .addFormDataPart("sports2", sport2)
                    .addFormDataPart("sports2_status", sports_sts2)
                    .addFormDataPart("sports3", sport3)
                    .addFormDataPart("sports3_status", sports_sts3)
                    .addFormDataPart("country", country)
                    .addFormDataPart("city", city)
                    .addFormDataPart("state", state)
                    .addFormDataPart("profession", profession)
                    .addFormDataPart("company", company)
                    .addFormDataPart("designation", designation)
                    .addFormDataPart("schooling", school)
                    .addFormDataPart("college", college)
                    .addFormDataPart("masters", master)
                    .addFormDataPart("clubmember1", club)
                    .addFormDataPart("clubmember2", club2)
                    .addFormDataPart("dob", dob)
                    .addFormDataPart("gender", gender)
                    .addFormDataPart("isadd", "1")

                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(10, TimeUnit.SECONDS);
            //Log.e("request", ":url:  " + request.urlString() + ", header: " + request.headers() + ", body " + request.body());
            Response response = client.newCall(request).execute();
            result = response.body().string();
            jsonObject = new JSONObject(result);
            //Log.e("response", ": " + result);
            return jsonObject;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("FindPlayerPageAsync", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("FindPlayerPageAsync", "Other Error: " + e.getLocalizedMessage());
        }
        return jsonObject;
    }

    public void hideKeyboardFind() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    /////////////////////////Google place Adapter

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    ////////////////////////////////////////////////

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Api.PLACES_API_BASE);
            sb.append("?key=" + Util.API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

           // Log.e("place search url", ": " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("places Api", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("places Api", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                //System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("places Api", "Cannot process JSON results", e);
        }

        return resultList;
    }

    /////////////////////
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////////////


    public class LoadImageAsync extends AsyncTask<String, String, Bitmap> {
        private ProgressDialog pDialog;
        Context context;
        Bitmap bitmap;
        String imageurl;
        boolean isGossip;

        public LoadImageAsync(Context context, String url) {
            this.context = context;
            this.imageurl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading  ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                // bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageurl).getContent());
                bitmap = Util.getBitmapFromURL(PlayerDetails.getImage_url());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {
                pDialog.dismiss();
                fileUri = Util.getImageUri(FindPlayer.this, bitmap);
                filepath = Util.getPathFromUri(FindPlayer.this, fileUri);
                fileName = Util.imagename(FindPlayer.this, fileUri);

            } else {
                pDialog.dismiss();
                Toast.makeText(context, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }


        }
    }



    private void openSportDialog(final Context context, final String checktype, final TextView tSport,
                                 final ArrayList<HashMap<String, String>> categoryItem) {
       /*
       /////////////show  static list  from string array
        ArrayAdapter<String> adapter;
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
        TextView see_all_txv = (TextView) dialog.findViewById(R.id.see_all_txv);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        see_all_txv.setVisibility(View.GONE);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final ListView listView = (ListView) dialog.findViewById(R.id.choose_sportlv);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(new SportCatAdapter(context, categoryItem));
        if (checktype.contentEquals("sport")) {
            title.setText("Click Below");
        } /*else if (checktype.contentEquals("gossip")) {
            title.setText("Find Gossips");
        } else {
            title.setText("Find HotDeals");

        }*/
       /* see_all_txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                dialog.cancel();
            }
        });*/
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                              dialog.cancel();

                                      }
                                  }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                tSport.setText(categoryItem.get(i).get("category_name").toString());
                //Toast.makeText(context,"this is categoryItem "+categoryItem.get(i).get("category_name"),Toast.LENGTH_SHORT).show();

                dialog.cancel();
            }
        });
        dialog.show();
    }



    private static void parseSportsType(JSONArray catarray) {
        if (catarray != null) {

            sFormSportCategory.clear();
            for (int i = 0; i < catarray.length(); i++) {
                HashMap<String, String> sportMap = new HashMap<String, String>();
                JSONObject json = null;
                try {
                    json = catarray.getJSONObject(i);
                    sportMap.put("cat_img", json.getString("cat_img"));
                    sportMap.put("category_name", json.getString("category_name"));
                    //Log.e("id", id);
                    //Log.e("category_name", categoryname);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sFormSportCategory.add(sportMap);
            }
        }

    }

    ///////////////////////////////////
}
