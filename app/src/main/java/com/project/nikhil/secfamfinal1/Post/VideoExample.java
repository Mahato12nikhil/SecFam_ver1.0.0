package com.project.nikhil.secfamfinal1.Post;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class VideoExample  extends BaseActivity {
    SimpleExoPlayer player;
    PlayerView playerView;
    ProgressBar progressBar;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    LinearLayout finshL;
    private String url = "";
    ImageView image_profile;
    TextView username, time, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Log.i("!!!Activity", "VideoExample");
        url=getIntent().getStringExtra("url");
        Log.i("!!!url", url);
        final String postId=getIntent().getStringExtra("postid");

        playerView = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progressBar);
        finshL = findViewById(R.id.finshL);
        image_profile = findViewById(R.id.image_profile);
        username = findViewById(R.id.username);
        time = findViewById(R.id.time);
        description = findViewById(R.id.description);


        if(url!=null) {
            //video_view(url);
        }
        else if(postId!=null){
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Posts").child(postId)
                                         .child("site");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    url=snapshot.getValue().toString();
                    //Toast.makeText(getApplicationContext(),""+url,Toast.LENGTH_LONG).show();
                    //video_view(url);
                    initializePlayer();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        finshL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void video_view(String url) {
        player = new SimpleExoPlayer.Builder ( this ).build();

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory( this, Util.getUserAgent ( this,"Cum" ) );
        MediaSource mediaSource= new ProgressiveMediaSource.Factory ( dataSourceFactory ).createMediaSource ( Uri.parse (url) );

        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
        player.prepare (mediaSource);
        player.setPlayWhenReady ( true );

        playerView.setPlayer(player);
    }

    private void initializePlayer() {
        if (!url.equals("")) {
            player = new SimpleExoPlayer.Builder(this).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            player.setMediaItem(mediaItem);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare();
            player.addListener(new Player.EventListener() {
                @Override
                public void onIsLoadingChanged(boolean isLoading) {
                    if (isLoading){
                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying){
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

}
