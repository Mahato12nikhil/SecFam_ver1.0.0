package com.project.nikhil.secfamfinal.Post;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
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
import com.project.nikhil.secfamfinal.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VideoExample  extends AppCompatActivity {
    SimpleExoPlayer simpleExoPlayer;
    PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        final String url=getIntent().getStringExtra("url");
        final String postId=getIntent().getStringExtra("postid");
        if(url!=null)
           video_view(url);
        else if(postId!=null){
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Posts").child(postId)
                                         .child("site");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String url=snapshot.getValue().toString();
                    Toast.makeText(getApplicationContext(),""+url,Toast.LENGTH_LONG).show();
                    video_view(url);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void video_view(String url) {
        playerView=findViewById ( R.id.video_viewXLX );
        simpleExoPlayer = new SimpleExoPlayer.Builder ( this )
                .build ();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory( this, Util.getUserAgent ( this,"Cum" ) );
        MediaSource mediaSource= new ProgressiveMediaSource.Factory ( dataSourceFactory ).createMediaSource ( Uri.parse (url) );
        simpleExoPlayer.setMediaItem ( MediaItem.fromUri ( Uri.parse ( url ) ) );
        simpleExoPlayer.prepare (mediaSource);
        simpleExoPlayer.setPlayWhenReady ( true );

        playerView.setPlayer ( simpleExoPlayer );
    }
}
