package com.riseintech.spulla;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerutubeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{
    private YouTubePlayerView youTubeView;
    String DEVELOPER_KEY="AIzaSyAu9qOUDGokwgJ_GrrFxE4V4Efn5LOUDWM",
            YOUTUBE_VIDEO_CODE="";
    //AIzaSyAu9qOUDGokwgJ_GrrFxE4V4Efn5LOUDWM
    //AIzaSyAEYb0pkeDUxgnD-aGPBmKkwRO297Wpa3o
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YOUTUBE_VIDEO_CODE= getIntent().getStringExtra("video_url");
        Log.d("hfghfghfghfg",YOUTUBE_VIDEO_CODE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_playerutube);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        // Initializing video player with developer key
        youTubeView.initialize(DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        //Log.e("onInitializationSuccess",": "+wasRestored);

        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
//            player.cueVideo(YOUTUBE_VIDEO_CODE);
            player.loadVideo(YOUTUBE_VIDEO_CODE);
            // Hiding player controls
          //  player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        //Log.e("on failure",": "+errorReason);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("getting error", errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.e("onActivityResult",": "+requestCode);

        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

}
