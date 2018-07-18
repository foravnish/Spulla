package com.riseintech.spulla.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.riseintech.spulla.R;

/**
 * Created by adm on 9/15/2016.
 */
public class MyPreference {
    public static SharedPreferences mySharedPreferencesToken;
    public static SharedPreferences mySharedPreferences;
    public static SharedPreferences mySharedPreferences1;
    public static SharedPreferences mySharedPreferences2;
    public static SharedPreferences mySharedPreferences3;
    public static String loginUsername = "login_username";
    public static String loginUserid = "login_userid";
    public static String loginUserstatus = "login_userstatus";
    public static String loginUsermobile = "login_usermobile";
    public static String logintype = "logintype";
    public static String prof_pic = "profic";
    public static String user_email = "user_email";
    public static String Is_Add="isadd";


    public static void signOut(Context context) {
        saveUsername(context, "");
        saveUserid(context, "");
        saveUserPicUrl(context, "");
        saveUserEmail(context, "");
        saveUserstatus(context, false);
        saveLoginType(context, "");
    }

    public static void saveUsername(Context context, String Value) {
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
        sharedpreferenceeditor.putString(loginUsername, Value);
        sharedpreferenceeditor.commit();
    }

    public static String loadUsername(Context context) {
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences.getString(loginUsername, " ");
    }
    public static void save_IsAdd(Context context, int Value) {
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
        sharedpreferenceeditor.putInt(Is_Add, Value);
        sharedpreferenceeditor.commit();
    }

    public static int loadIsAdd(Context context) {
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences.getInt(Is_Add,0);
    }



    public static void saveUserid(Context context, String Value) {
        mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences1.edit();
        sharedpreferenceeditor.putString(loginUserid, Value);
        sharedpreferenceeditor.commit();
    }

    public static String loadUserid(Context context) {
        mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences1.getString(loginUserid, "0");
    }

    public static void saveLoginType(Context context, String Value) {
        mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences1.edit();
        sharedpreferenceeditor.putString(logintype, Value);
        sharedpreferenceeditor.commit();
    }

    public static String loadLoginType(Context context) {
        mySharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences1.getString(logintype, "");
    }

    public static void saveUserPicUrl(Context context, String Value) {
        mySharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences3.edit();
        sharedpreferenceeditor.putString(prof_pic, Value);
        sharedpreferenceeditor.commit();
    }

    public static String loadUserPicUrl(Context context) {
        mySharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences3.getString(prof_pic,"gfdg");
    }

    public static void saveUserEmail(Context context, String Value) {
        mySharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences3.edit();
        sharedpreferenceeditor.putString(user_email, Value);
        sharedpreferenceeditor.commit();
    }

    public static String loadUserEmail(Context context) {
        mySharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences3.getString(user_email, "");
    }

    public static void saveUsermobile(Context context, String Value) {
        mySharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences3.edit();
        sharedpreferenceeditor.putString(loginUsermobile, Value);
        sharedpreferenceeditor.commit();
    }

    public static String loadUsermobile(Context context) {
        mySharedPreferences3 = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences3.getString(loginUsermobile, "");
    }


    public static void saveUserstatus(Context context, boolean Value) {
        mySharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences2.edit();
        sharedpreferenceeditor.putBoolean(loginUserstatus, Value);
        sharedpreferenceeditor.commit();
    }

    public static boolean loadUserstatus(Context context) {
        mySharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences2.getBoolean(loginUserstatus, false);
    }

   /* public static void saveMatch(Context context, String Value) {
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferences.edit();
        sharedpreferenceeditor.putString(loginUserid, Value);
        sharedpreferenceeditor.commit();
    }

    public static ArrayList<HashMap<String,String>> loadMatch(Context context)
    {
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferences.getStringSet(loginUserid, ;
    }*/

    public static void showProgressDialog(ProgressDialog mProgressDialog, Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public static void hideProgressDialog(ProgressDialog mProgressDialog, Context context) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public static String getgcm_token(Context context) {
        mySharedPreferencesToken = PreferenceManager.getDefaultSharedPreferences(context);
        return mySharedPreferencesToken.getString("gcm_token", "");
    }
    public static void setgcm_token(Context context, String Value) {
        mySharedPreferencesToken = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedpreferenceeditor = mySharedPreferencesToken.edit();
        sharedpreferenceeditor.putString("gcm_token", Value);
        sharedpreferenceeditor.commit();
    }
}
