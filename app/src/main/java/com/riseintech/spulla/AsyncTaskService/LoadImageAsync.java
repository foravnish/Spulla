package com.riseintech.spulla.AsyncTaskService;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by user on 10/12/2016.
 */


public class LoadImageAsync extends AsyncTask<String, String, Bitmap> {
    private ProgressDialog pDialog;
    Context context;
    Bitmap bitmap;
    String imageurl;
    boolean isGossip;

    public LoadImageAsync(Context context, String url, ImageView imageView) {
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
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageurl).getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {

        if (image != null) {
            pDialog.dismiss();

        } else {
            pDialog.dismiss();
            Toast.makeText(context, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
        }


    }
}