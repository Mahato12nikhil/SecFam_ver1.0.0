package com.project.nikhil.secfamfinal1.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideoActivity extends BaseActivity {


    PlayerView playerView;
    ProgressBar progressBar;
    //LikeButton likeFool;
    FrameLayout janina;
    private DatabaseReference reference;

    SimpleExoPlayer simpleExoPlayer;
    TextView like_count, view_count, comment_count, download_count;
    ImageView more, share,judt,emni;
    LinearLayout download,finshL,comment_holderFool,likeHolderFool,down_holderFool;
    private static final int MY_PERMISSION = 1;
    ProgressDialog progressDialog;
    String videoPath;

    double file_size = 0;
    String file_name;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Log.i("!!!Activity", "VideoActivity");

        janina=findViewById(R.id.janina);
        like_count = findViewById(R.id.like_countFool);
        //view_count = findViewById(R.id.view_countFool);
        comment_count = findViewById(R.id.comment_countFool);
        download_count = findViewById(R.id.download_countFool);
        more = findViewById(R.id.more);
      //  share = findViewById(R.id.share);
        likeHolderFool=findViewById(R.id.likeHolderFool);
        progressBar=findViewById(R.id.progressBar);
        emni=findViewById(R.id.emni);
        playerView=findViewById(R.id.video_view);
       // likeFool=findViewById(R.id.likeFool);
        finshL=findViewById(R.id.finshL);
        finshL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String url=getIntent().getStringExtra("url");
        comment_holderFool=findViewById(R.id.comment_holderFool);
        down_holderFool=findViewById(R.id.down_holderFoolp);
        reference= FirebaseDatabase.getInstance().getReference();
        Glide.with(getApplicationContext())
                .load(url)
                .into(emni);
        final String postId=getIntent().getStringExtra("postId");
        final String publisher=getIntent().getStringExtra("publisher");

        if(postId!=null) {
           // isLiked(postId,likeFool);
            nrLikes(like_count, postId);
            getCommetns(postId,comment_count);
            getViews(postId);
        }
     /*   likeFool.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                reference.child("Likes").child(postId)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                addNotification(publisher,postId);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                reference.child("Likes").child(postId)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            }
        });*/

/*        playerView= new PlayerView(getApplic
ationContext());
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        View view1= LayoutInflater.from(getApplicationContext()).inflate(R.layout.exoplayer,null,false);
        playerView= (PlayerView) view1.getRootView();

        janina.addView(playerView);
        playerView.requestFocus();*/
        // download = findViewById(R.id.down_holder);

        uri=Uri.parse(url);

        //playerView = findViewById(R.id.video_view);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                videoPath=dataSnapshot.child("thumb").getValue().toString();
                initializePlayer(videoPath);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Toast.makeText(getApplicationContext(),""+videoPath,Toast.LENGTH_LONG).show();




        // judt=findViewById(R.id.judt);


        down_holderFool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION);
                } else {
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/SecFam/videos/");
                    try {
                        dir.mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(VideoActivity.this, "Cannot create folder", Toast.LENGTH_SHORT).show();
                    }
                    new DownloadTask().execute(videoPath);
                }
            }
        });


        String like = prettyCount(1500);
        final String comment = prettyCount(5300);
        String view = prettyCount(5646);
        String share = prettyCount(84800);
        String download = prettyCount(8787);
        //like_count.setText(like);
        //  comment_count.setText(comment);
        //view_count.setText(view);
        download_count.setText(download);

    }

    private void getViews(String postId) {
        reference= FirebaseDatabase.getInstance().getReference().child("PostView").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // view_count.setText(prettyCount(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.00").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat().format(numValue);
        }

    }

    private void initializePlayer(String url) {

        simpleExoPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();


        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(url);
// Set the media item to be played.
        simpleExoPlayer.setMediaItem(mediaItem);
// Prepare the player.
        simpleExoPlayer.prepare();
// Start the playback.
      //  simpleExoPlayer.play();


        playerView.setPlayer(simpleExoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        simpleExoPlayer.setPlayWhenReady(true);
        // judt.setVisibility(View.GONE);

        simpleExoPlayer.addListener(new Player.EventListener() {
        });

        simpleExoPlayer.addListener(new Player.EventListener() {


            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }



            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        if (progressBar != null) {
                            progressBar.setVisibility(VISIBLE);
                        }

                        break;
                    case Player.STATE_ENDED:
                        simpleExoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                            emni.setVisibility(GONE);
                        }

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getCurrentPosition();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();

                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/SecFam/videos/");
                    try {
                        dir.mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(VideoActivity.this, "Cannot create folder", Toast.LENGTH_SHORT).show();
                    }
                    new DownloadTask().execute(videoPath);

                }
            }
        }
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {


        protected String doInBackground(String... strings) {
            file_name = strings[0].substring(strings[0].lastIndexOf("/") + 1);
            try {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(strings[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP" + connection.getResponseCode() + " " +
                                connection.getResponseMessage();
                    }
                    int fileLenght = connection.getContentLength();
                    file_size = fileLenght;

                    input = connection.getInputStream();
                    output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/SecFam/videos" + file_name);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        if (isCancelled()) {
                            return null;
                        }
                        total += count;
                        if (fileLenght > 0) {
                            publishProgress((int) (total * 100 / fileLenght));
                        }
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                        }
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            } finally {

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VideoActivity.this);
            progressDialog.setTitle("Downloading...");
            progressDialog.setMessage("File size : 0 MB");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(VideoActivity.this, "Download cancelled", Toast.LENGTH_SHORT).show();
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/SecFam/videos/" + file_name);
                    try {
                        dir.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(values[0]);
            progressDialog.setMessage("File size : " + new DecimalFormat("##.##").format(file_size / 1000000) + "MB");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                Toast.makeText(VideoActivity.this, "ERROR" + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(VideoActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
            }

        }

    }
    private void nrLikes(final TextView likes, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                likes.setText(prettyCount(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getCommetns(String postId, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.setText(" "+prettyCount(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addNotification(String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);
    }
    private void isLiked(final String postid, final CheckBox imageView){

        //  imageView.setLiked(true);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setLiked(true);
                    imageView.setTag("liked");
                } else{
                    imageView.setLiked(false);
                    imageView.setTag("like");
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}