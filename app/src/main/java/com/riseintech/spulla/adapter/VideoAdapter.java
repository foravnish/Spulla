package com.riseintech.spulla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.riseintech.spulla.CochScheduleActivity;
import com.riseintech.spulla.FindPlayer;
import com.riseintech.spulla.PlayerutubeActivity;
import com.riseintech.spulla.R;
import com.riseintech.spulla.utils.MyPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adm on 9/13/2016.
 */
public class VideoAdapter extends PagerAdapter implements View.OnClickListener {
    private ArrayList<HashMap<String, String>> videoItems;
    private static final String TAG = "MyPagerAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    String DEVELOPER_KEY = "AIzaSyAEYb0pkeDUxgnD-aGPBmKkwRO297Wpa3o", VIDEO_ID = "2zNSgSzhBfM";
    private TextView txtsBooknow, coach_iconname, coach_name;
    ImageView play;
    ImageView videoView;
    RatingBar ratingBar;
    int pos;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;
    private YouTubeThumbnailView thumbnailView;

    public VideoAdapter(Context activity, ArrayList<HashMap<String, String>> videoItems) {
        this.mContext = activity;
        this.videoItems = videoItems;
    }

    @Override
    public int getCount() {
        return videoItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coach_items, container, false);

        thumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.thumbnailview);
        Log.d("gfdhfghfgh",videoItems.get(position).get("video").toString());
        thumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeThumbnailLoader thumbnailLoader) {
                youTubeThumbnailLoader = thumbnailLoader;
                thumbnailLoader.setOnThumbnailLoadedListener(new ThumbnailListener());
                youTubeThumbnailLoader.setVideo(videoItems.get(position).get("video").toString());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                String errorMessage =
                        String.format("onInitializationFailure (%1$s)",
                youTubeInitializationResult.toString());
                //  Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        //p_h_title = (TextView) view.findViewById(R.id.p_h_title);
        txtsBooknow = (TextView) view.findViewById(R.id.txtsBooknow);
        coach_name = (TextView) view.findViewById(R.id.coach_name);
        coach_iconname = (TextView) view.findViewById(R.id.coach_iconname);
        coach_name.setText(videoItems.get(position).get("coach_name"));
        coach_iconname.setText(videoItems.get(position).get("icon_name"));
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.WHITE);
        //p_h_menu = (ImageView) view.findViewById(R.id.p_h_menu);
        play = (ImageView) view.findViewById(R.id.play);
        videoView = (ImageView) view.findViewById(R.id.videoView);
        Picasso.with(mContext).load(videoItems.get(position).get("coach_thumbnail").toString())
                .placeholder(R.color.black).error(R.drawable.bg)
                .into(videoView);
        txtsBooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("coach id", ": " + videoItems.get(position).get("id"));
                if (MyPreference.loadIsAdd(mContext) == 1) {
                    Intent in = new Intent(view.getContext(), CochScheduleActivity.class);
                    in.putExtra("cid", videoItems.get(position).get("id"));
                    view.getContext().startActivity(in);
                } else {
                    Toast.makeText(mContext, "First Add Player Details", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(view.getContext(), FindPlayer.class);
                    view.getContext().startActivity(in);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String VideoURL = videoItems.get(position).get("video").toString();
                Log.d("gdfdhbfdgdhbfh",videoView.toString());
                view.getContext().startActivity(new Intent(view.getContext(), PlayerutubeActivity.class).putExtra("video_url", VideoURL));

              /*  Log.e("video", ": " + videoItems.get(position).get("video").toString());
                String VideoURL = videoItems.get(position).get("video").toString();
                Intent videoIntent = new Intent(view.getContext(), VideoViewActivity.class);
                videoIntent.putExtra("video_url", VideoURL);
                view.getContext().startActivity(videoIntent);*/
            }
        });
        videoView.setOnClickListener(this);
        container.addView(view);

        // Return the View
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.videoView:
               /* if (videoView.isPlaying()) {
                    videoView.pause();
                    play.setVisibility(View.VISIBLE);
                }*/
                break;
        }
    }

   public static final class ThumbnailListener implements
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView thumbnail, String videoId) {
          /*  Toast.makeText(mContext,
                    "onThumbnailLoaded", Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView thumbnail,
                                     YouTubeThumbnailLoader.ErrorReason reason) {
          /*  Toast.makeText(mContext,
                    "onThumbnailError", Toast.LENGTH_SHORT).show();*/
        }
    }
}