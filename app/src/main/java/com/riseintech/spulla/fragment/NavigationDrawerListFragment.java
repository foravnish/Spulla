package com.riseintech.spulla.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.riseintech.spulla.AboutActivity;
import com.riseintech.spulla.CustomerSupportActivity;
import com.riseintech.spulla.FindPlayer;
import com.riseintech.spulla.LoginActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.MyPreference;
import com.riseintech.spulla.utils.Util;
import com.squareup.picasso.Picasso;

/**
 * Created by adm on 8/16/2016.
 */
public class NavigationDrawerListFragment extends Fragment implements View.OnClickListener {

    private static View vNavigation;
    TextView userName, moboleNo, find_player, about, cus_supt;
    ImageView profile_image;
    Button logout;
    private ArrayAdapter arrayAdapter;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        // Inflate the layout for this fragment
        vNavigation = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        logout = (Button) vNavigation.findViewById(R.id.logout);
        userName = (TextView) vNavigation.findViewById(R.id.txt_uname);
        moboleNo = (TextView) vNavigation.findViewById(R.id.txt_mobile);
        find_player = (TextView) vNavigation.findViewById(R.id.find_player);
        if (MyPreference.loadIsAdd(getActivity()) == 1) {
            find_player.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
            find_player.setText("Edit Player Profile");
        }
        about = (TextView) vNavigation.findViewById(R.id.about);
        cus_supt = (TextView) vNavigation.findViewById(R.id.cus_supt);

        profile_image = (ImageView) vNavigation.findViewById(R.id.profile_image);
        // LinearLayout itemLyt=(LinearLayout) vNavigation.findViewById(R.id.lytitem);
        if (!MyPreference.loadUserPicUrl(getActivity()).isEmpty()
                && (MyPreference.loadLoginType(getActivity()).equalsIgnoreCase("google")
                || MyPreference.loadLoginType(getActivity()).equalsIgnoreCase("fb"))) {
            Picasso.with(getActivity())
                    .load(MyPreference.loadUserPicUrl(getActivity()))
                    .placeholder(R.color.colorRedt)
                    .error(R.drawable.propic).into(profile_image);
        } else if (!MyPreference.loadUserPicUrl(getActivity()).isEmpty()) {
            Picasso.with(getActivity())
                    .load(MyPreference.loadUserPicUrl(getActivity()))
                    .placeholder(R.color.colorRedt)
                    .error(R.drawable.propic)
                    .into(profile_image);
        } else {
            profile_image.setImageResource(R.drawable.propic);
        }

        if (MyPreference.loadUserstatus(getActivity()) == true) {
            Log.w("if condition on create", "" + MyPreference.loadUserstatus(getActivity()));
            userName.setText(MyPreference.loadUsername(getActivity()));
            moboleNo.setText(MyPreference.loadUserEmail(getActivity()));

        }

        find_player.setOnClickListener(this);
        logout.setOnClickListener(this);
        about.setOnClickListener(this);
        cus_supt.setOnClickListener(this);

// GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
*/
        return vNavigation;
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        //Log.w("if condition on resume", "" + MyPreference.loadUserstatus(getActivity()));
        if (MyPreference.loadUserstatus(getActivity()) == true) {
            //Log.w("if condition>>", "" + MyPreference.loadUserstatus(getActivity()));
            userName.setText(MyPreference.loadUsername(getActivity()));
            moboleNo.setText(MyPreference.loadUsermobile(getActivity()));
        }

        if (Util.LoadPro_Image == 1 && !MyPreference.loadUserPicUrl(getActivity()).isEmpty()) {
            //Log.w("LoadPro_Image>>", "val: " + Util.LoadPro_Image + ", url: " + MyPreference.loadUserPicUrl(getActivity()));
            Picasso.with(getActivity())
                    .load(MyPreference.loadUserPicUrl(getActivity()))
                    .placeholder(R.color.colorRedt)
                    .error(R.drawable.propic)
                    .into(profile_image);
        }

        super.onResume();
    }

    private void signOut() {

        ///////////logout for facebook
        if (MyPreference.loadLoginType(getContext()).equalsIgnoreCase("fb")) {
            if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                //Logged in so show the login button
                LoginManager.getInstance().logOut();
                Toast.makeText(getContext(), "fb Signout Suceesfully", Toast.LENGTH_SHORT).show();

            }
        } else if (MyPreference.loadLoginType(getContext()).equalsIgnoreCase("google")) {
            ///////////////////////////

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Toast.makeText(getContext(), "google Signout Suceesfully", Toast.LENGTH_SHORT).show();
                            }
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_player:
                Intent in = new Intent(getActivity(), FindPlayer.class);
                startActivity(in);
                break;
            case R.id.logout:
                signOut();
                MyPreference.signOut(getActivity());
                Intent log = new Intent(getActivity(), LoginActivity.class);
                startActivity(log);
                getActivity().finish();
                break;
            case R.id.about:
                Intent about = new Intent(getActivity(), AboutActivity.class);
                startActivity(about);
                break;
            case R.id.cus_supt:
                Intent cus_supt = new Intent(getActivity(), CustomerSupportActivity.class);
                startActivity(cus_supt);
                break;
        }
    }

   /* public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    *//* The Activity hosting the drawer. Should have an ActionBar *//*
                mDrawerLayout,                    *//* The DrawerLayout to link to the given Activity's ActionBar *//*
                toolbar,                               *//* The toolbar to use if you have an independent Toolbar *//*
                R.string.navigation_drawer_open,  *//* "open drawer" description for accessibility *//*
                R.string.navigation_drawer_close  *//* "close drawer" description for accessibility *//*
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;

                   *//* Gets a SharedPreferences instance that points to the default file that is
                    * used by the preference framework in the given context*//*
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());

                    *//*Modify the values in this SharedPreferences object with key @PREF_USER_LEARNED_DRAWER*//*
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                *//*Synchronize the indicator with the state of the linked DrawerLayout after onRestoreInstanceState has occurred*//*
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }*/

}