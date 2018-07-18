package com.riseintech.spulla;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.riseintech.spulla.utils.Util;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Risein on 11/25/2016.
 */

public class ZoomPhoto extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView ;
Context context;
    PhotoViewAttacher photoViewAttacher ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_picture_view);
       toolbar = (Toolbar) findViewById(R.id.toolbar_zoom);
 /*add toolbar to actionbar*/
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);*/
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("url");

        imageView = (ImageView)findViewById(R.id.imageView);

        Util.showImage(context,imgUrl,imageView);

       /* Drawable drawable = getResources().getDrawable(R.drawable.profilepic);

        imageView.setImageDrawable(drawable);*/

        photoViewAttacher = new PhotoViewAttacher(imageView);
        //photoViewAttacher.onDrag(2,2);
        photoViewAttacher.update();
    }

    public  boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
