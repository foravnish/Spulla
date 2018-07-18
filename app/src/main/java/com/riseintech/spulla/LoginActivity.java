package com.riseintech.spulla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.riseintech.spulla.connection.InternetStatus;
import com.riseintech.spulla.connection.JSONParser;
import com.riseintech.spulla.utils.Api;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.PlayerDetails;
import com.riseintech.spulla.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by adm on 9/16/2016.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    // uicontrols
    private EditText mob_num,login_pin;
    private static final String TAG = "SignInActivity";
    private Button sSignIn;
    private ProgressDialog pDialog;
    ProfileTracker profileTracker;
    Button fbloginButton;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    String mobile_number,pin_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.login_page);
     /* Initialize Radio Group and attach click handler */
        Util.printKeyHash(LoginActivity.this);
        initLogin();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.risein.suplla",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


      /* if (MyPreference.loadUserstatus(LoginActivity.this) &&
                InternetStatus.isConnectingToInternet(LoginActivity.this)) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();



        }*/


    }

    private void initLogin() {
        //find all id

        mob_num = (EditText) findViewById(R.id.mob_num);
        login_pin= (EditText) findViewById(R.id.login_pin);
        sSignIn = (Button) findViewById(R.id.s_btnLogin);


// [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        Button signInButton = (Button) findViewById(R.id.google_login);
        signInButton.setOnClickListener(this);
        fbloginButton = (Button) findViewById(R.id.fb_login);
        fbloginButton.setOnClickListener(this);
        sSignIn.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this, "onCancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "onError " + exception, Toast.LENGTH_SHORT).show();

            }
        });
        // [END customize_button]
        // If using in a fragment
        // Other app specific specialization

        //////SET REQUEST [ERMISSION

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    //get user details function for fb login
    public static Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        Bitmap bitmap = null;

        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void setFacebookData(final LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            //Log.i("Response", response.toString());

                            final String email = response.getJSONObject().optString("email");

                            final String firstName = response.getJSONObject().optString("first_name");
                            String lastName = response.getJSONObject().optString("last_name");
                            String gender = response.getJSONObject().optString("gender");
                            String ids = response.getJSONObject().getString("id");
                            //String id = profile.getId();
                            MyPreference.saveLoginType(LoginActivity.this, "fb");
                            if (Profile.getCurrentProfile() != null) {
                                Profile profile = Profile.getCurrentProfile();
                              /*  MyPreference.saveUserPicUrl(LoginActivity.this, "" + Profile.getCurrentProfile().
                                        getProfilePictureUri(200, 200));
                                Log.i("Login", "ProfilePic..." + Profile.getCurrentProfile().getProfilePictureUri(200, 200)+", id "+profile.getId());
                                MyPreference.saveUsername(LoginActivity.this, profile.getFirstName()
                                        + " " + profile.getLastName());
                          */
                                new LoginPage(email,"", "facebook").execute();

                            } else {
                                profileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                       /* MyPreference.saveUserPicUrl(LoginActivity.this, "" + currentProfile.getCurrentProfile().
                                                getProfilePictureUri(200, 200));
                                        MyPreference.saveUsername(LoginActivity.this, currentProfile.getFirstName()
                                                + " " + currentProfile.getLastName());
                                     */
                                        //Log.i("Login", "ProfilePic" + currentProfile.getCurrentProfile().getProfilePictureUri(200, 200) + ", id " + currentProfile.getId());
                                        new LoginPage(email,"", "facebook").execute();
                                        profileTracker.stopTracking();
                                    }
                                };
                                profileTracker.startTracking();
                            }


                            //Log.i("Login" + "Email", email);
                            //Log.i("Login" + "FirstName", firstName);
                            //Log.i("Login" + "LastName", lastName);
                            //Log.i("Login" + "Gender", gender);
                            //Log.i("Login" + "id", ids);
                            // MyPreference.saveUserEmail(LoginActivity.this, email);

                           /* SplashScreen splashScreen = new SplashScreen();
                            SplashScreen.CricketPage cricketPage = splashScreen.new CricketPage(LoginActivity.this);
                            cricketPage.execute();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
////////////////////////google sign  in functions for get details of user

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //Log.i("Login" + "Email", acct.getEmail());
            //Log.i("Login" + "FirstName", acct.getDisplayName());
            //Log.i("Login" + "Given name", acct.getGivenName());
            //Log.i("Login" + "photo", "" + acct.getPhotoUrl());
            //Log.i("Login" + "id", acct.getId() + "token id " + acct.getIdToken());
           /* MyPreference.saveLoginType(LoginActivity.this, "google");
            MyPreference.saveUserEmail(LoginActivity.this, acct.getEmail());
            MyPreference.saveUsername(LoginActivity.this, acct.getDisplayName());
            MyPreference.saveUserPicUrl(LoginActivity.this, "" + acct.getPhotoUrl());*/
            new LoginPage(acct.getEmail(),"", "google").execute();
           /* SplashScreen splashScreen = new SplashScreen();
            SplashScreen.CricketPage cricketPage = splashScreen.new CricketPage(LoginActivity.this);
            cricketPage.execute();*/

            // updateUI(true);
        } else {
            Toast.makeText(LoginActivity.this, "Login Fail ", Toast.LENGTH_SHORT).show();
        }
    }
    // [END handleSignInResult]


    // [START signIn]
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [END signIn]
// [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    // [END revokeAccess]
    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]


    ///////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.d("gfdggdfgdfg",result.toString());
            Log.d("gfdggdfgdfg2", String.valueOf(result.getStatus().getStatusCode()));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_login:
                LoginManager.getInstance().logInWithReadPermissions(this,
                        Arrays.asList("user_friends", "email", "public_profile", "user_birthday"));
                break;
            case R.id.google_login:
                if (InternetStatus.isConnectingToInternet(LoginActivity.this)) {
                    signIn();
                }
                break;
            case R.id.s_btnLogin:
                if (mob_num.getText().toString().isEmpty()) {
                    mob_num.setError(getString(R.string.empty_msg));
                } else if (mob_num.getText().toString().length() < 10) {
                    mob_num.setError("Not valid mobile number");
                } else {
                    mobile_number = mob_num.getText().toString();
                    pin_login = login_pin.getText().toString();
                    if (InternetStatus.isConnectingToInternet(LoginActivity.this)) {
                        hideKeyboard();
                        MyPreference.saveLoginType(LoginActivity.this, "mobiles");
                        new LoginPage(mobile_number,pin_login, "mobiles").execute();
                    }
                }
                break;
        }
    }


    ///////////////////
    class LoginPage extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        String val, type,pin;
        HashMap<String, String> params = new HashMap<>();

        public LoginPage(String val,String pin, String type) {
            this.val = val;
            this.pin = pin;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginActivity.this);
            // pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            params.put("mobile", val);
            params.put("otp_code", pin);
            params.put("login_type", type);


            Log.e("gdfgdfdghdfdhd",val);
            Log.e("gdfgdfdghdfdhd",pin);
            Log.e("gdfgdfdghdfdhd",type);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject json = null;
            try {
                json = jsonParser.makeHttpRequest(Api.Login, "GET", params);
                //Log.d("JSON ", "------------" + json);
                if (json != null) {
                    Log.w("JSON result", json.toString());

                    return json;
                } else {
                    json = new JSONObject("");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            //Toast.makeText(LoginActivity.this, "" + json, Toast.LENGTH_SHORT).show();

            try {
                String msg=json.getString("msg");
                if (json.getString("status").equals("1")) {
                    MyPreference.saveUserid(LoginActivity.this, json.optString("id"));
                    MyPreference.saveUserstatus(LoginActivity.this, true);
                    int isadd = json.getInt("isadd");
                    MyPreference.save_IsAdd(LoginActivity.this, isadd);
                    String image_url = json.optString("images");
                    if (!image_url.isEmpty() && MyPreference.loadLoginType(LoginActivity.this).equalsIgnoreCase("mobiles")) {
                        MyPreference.saveUsername(LoginActivity.this, json.optString("name"));
                        PlayerDetails.setImage_url(image_url);
                        MyPreference.saveUserPicUrl(LoginActivity.this, image_url);

                    }
/*                    String playas = json.getString("playas");
                    String sports1 = json.getString("sports1");
                    String sports2 = json.getString("sports2");
                    String sports3 = json.getString("sports3");
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

                    PlayerDetails.setName(name);
                    PlayerDetails.setAge(dob);
                    PlayerDetails.setGender(gender);
                    PlayerDetails.setCountry(country);
                    PlayerDetails.setState(state);
                    PlayerDetails.setCity(city);

                    PlayerDetails.setClub_member(clubmember1);
                    PlayerDetails.setClub_member1(clubmember2);
                    PlayerDetails.setSports_Interest1(sports1);
                    PlayerDetails.setSports_Interest2(sports2);
                    PlayerDetails.setSports_Interest3(sports3);
                    PlayerDetails.setSprs(playas);
                    PlayerDetails.setPlay_As(playas);

                    PlayerDetails.setCollege(college);
                    PlayerDetails.setSchooling(schooling);
                    PlayerDetails.setCompany(company);
                    PlayerDetails.setDesignation(designation);
                    PlayerDetails.setMasters(masters);
                    PlayerDetails.setProfession(profession);*/
                    Toast.makeText(LoginActivity.this, "" +msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "" +msg, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this, OtpActivity.class).putExtra("mobile_num", mobile_number));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
