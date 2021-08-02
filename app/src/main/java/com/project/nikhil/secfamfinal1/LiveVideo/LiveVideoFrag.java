package com.project.nikhil.secfamfinal1.LiveVideo;

import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bambuser.broadcaster.BackendApi;
import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Emergency.MapVideoActivity;
import com.project.nikhil.secfamfinal1.R;


import org.json.JSONObject;

import java.io.IOException;


public class LiveVideoFrag extends Fragment {
    SeekBar mSeekBar;
    boolean opened;
    double current_pos, total_duration;
    LinearLayout showProg;
    private Handler handler;
    private DatabaseReference reference;
    private String dataS;
    private Runnable runnable;
    private ImageView exo_play;

    private static final String APPLICATION_ID = "YL663IfQ8SR0OByoW4eOrA";
    private static final String API_KEY = "JEAruHb6yjAFW4RxkRQMR2";
    TextView totalTime, currentTime;
    DatabaseReference broadCastRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_video, container, false);
        Window window = getActivity().getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        handler = new Handler();

        mSeekBar = view.findViewById(R.id.seekBar2x);
        showProg = view.findViewById(R.id.showProg);
        totalTime = view.findViewById(R.id.totalTime);
        currentTime = view.findViewById(R.id.currentTime);
        exo_play = view.findViewById(R.id.exo_play);
        mSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.bg), PorterDuff.Mode.MULTIPLY);
        // mSeekBar.getProgressDrawable().setColorFilter();
        mSeekBar.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        mSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.bg), PorterDuff.Mode.SRC_ATOP);

        mDefaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        mVideoSurface = view.findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView = view.findViewById(R.id.PlayerStatusTextView);
        mPlayerContentView = view.findViewById(R.id.PlayerContentView);

        final String pushId = ((MapVideoActivity) getActivity()).getPushId();

        exo_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(), "Working!!", Toast.LENGTH_LONG).show();
                if (mBroadcastPlayer != null) {
               //     Toast.makeText(getContext(), "fine!!", Toast.LENGTH_LONG).show();

                    if (exo_play.getTag() == "paused") {
                        exo_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                        exo_play.setTag("playing");
                        mBroadcastPlayer.start();
                    }
                    if (exo_play.getTag() == "playing") {
                        exo_play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                        exo_play.setTag("paused");
                        mBroadcastPlayer.pause();

                    }
                }
            }
        });

        if (pushId != null) {
            broadCastRef = FirebaseDatabase.getInstance().getReference().child("EmergencyLive").child(pushId);
            broadCastRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        dataS = dataSnapshot.child("broadcastId").getValue().toString();
                        getLatestResourceUri(dataS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        return view;
    }

    private void getLatestResourceUri(String dataS) {
       // Toast.makeText(getContext(), "" + dataS, Toast.LENGTH_LONG).show();

        final Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts/" + dataS)
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayerStatusTextView != null)
                            mPlayerStatusTextView.setText("Http exception: " + e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {

                    JSONObject json = new JSONObject(body);
                    //  JSONArray results = json.getJSONArray("results");
                    if (json.optString("type").equals("live")) {
                        resourceUri = json.optString("resourceUri");
                    } else {
                        resourceUri = "end";
                    }
                } catch (Exception ignored) {
                }
                final String uri = resourceUri;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (uri != null) {
                            if (uri.equals("end")) {
                                Toast.makeText(getContext(), "live has ended", Toast.LENGTH_SHORT).show();
                            } else {
                                initPlayer(uri);
                            }
                        }
                    }
                });
            }
        });
    }

    private void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(getContext(), resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        String s = BackendApi.TICKET_FILE_AUTHOR;
        mBroadcastPlayer.setTimeshiftMode(false);

        //  mBroadcastPlayer.setTimeshiftMode(true);

        mBroadcastPlayer.load();

    }


    final OkHttpClient mOkHttpClient = new OkHttpClient();
    BroadcastPlayer mBroadcastPlayer;
    MediaController mMediaController = null;
    SurfaceViewWithAutoAR mVideoSurface;
    TextView mPlayerStatusTextView;
    View mPlayerContentView;
    Display mDefaultDisplay;
    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {

            if (playerState == PlayerState.LOADING) {

            }
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("" + playerState);
            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null) {
                    mMediaController = new MediaController(getContext());
                    //  mMediaController.setAnchorView(mPlayerContentView);
                    mVideoSurface.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (!opened) {
                                showProg.setVisibility(View.VISIBLE);
                                TranslateAnimation animate = new TranslateAnimation(
                                        0,
                                        0,
                                        500,
                                        0);
                                animate.setDuration(500);
                                animate.setFillAfter(true);
                                showProg.startAnimation(animate);
                            } else {
                                showProg.setVisibility(View.INVISIBLE);
                                TranslateAnimation animate = new TranslateAnimation(
                                        0,
                                        0,
                                        0
                                        ,
                                        500);
                                animate.setDuration(500);
                                animate.setFillAfter(true);
                                showProg.startAnimation(animate);
                            }
                            opened = !opened;

                            return false;
                        }
                    });

                    setVideoProgress();
                }
                if (mMediaController != null) {
//                    mMediaController.setEnabled(true);
                    // mMediaController.hide();
                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
            }
        }

        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);

        }
    };

    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }

    private void setVideoProgress() {

        //get the video duration
        current_pos = mBroadcastPlayer.getCurrentPosition();
        total_duration = mBroadcastPlayer.getDuration();


        //display video duration
        totalTime.setText(timeConversion((long) total_duration));
        currentTime.setText(timeConversion((long) current_pos));
        mSeekBar.setMax((int) total_duration);


        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mBroadcastPlayer != null)
                        current_pos = mBroadcastPlayer.getCurrentPosition();
                    currentTime.setText(timeConversion((long) current_pos));
                    mSeekBar.setProgress((int) current_pos);
                    mSeekBar.setSecondaryProgress(mBroadcastPlayer.getBufferPercentage());
                    //  Toast.makeText(getApplicationContext(),""+mBroadcastPlayer.getBufferPercentage(),Toast.LENGTH_LONG).show();

                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override


            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                mBroadcastPlayer.seekTo((int) current_pos);
            }
        });
    }

    private String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBroadcastPlayer != null)

            mBroadcastPlayer.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.pause();
    }
}
