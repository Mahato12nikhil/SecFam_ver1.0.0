package com.project.nikhil.secfamfinal1.LiveVideo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Emergency.HistoryDetailsAdapter;
import com.project.nikhil.secfamfinal1.Notification.APIService;
import com.project.nikhil.secfamfinal1.Notification.Client;
import com.project.nikhil.secfamfinal1.Notification.Data;
import com.project.nikhil.secfamfinal1.Notification.EmergencyData;
import com.project.nikhil.secfamfinal1.Notification.EmergencySender;
import com.project.nikhil.secfamfinal1.Notification.MyResponse;
import com.project.nikhil.secfamfinal1.Notification.Sender;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.project.nikhil.secfamfinal1.constant.Constant.NOTIFICATION_TYPE_CHAT;
import static com.project.nikhil.secfamfinal1.constant.Constant.NOTIFICATION_TYPE_EMERGENCY;

public class LiveVideoBroadcast extends BaseActivity {

    String liveFireBaseUser, currUserName="Somebody";
    private static final String LOGTAG = "Mybroadcastingapp";
    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    Button mBroadcastButton;
    ImageButton switchCamera;
    private DatabaseReference reference;
    ImageView liveVidBack;
    String pushId;
    private static final String APPLICATION_ID = "YL663IfQ8SR0OByoW4eOrA";

    private List<String> idsList;
    private RecyclerView allSendRecyclerview;
    private HistoryDetailsAdapter historyDetailsAdapter;
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video_broadcast);
        Log.i("!!!Activity", "LiveVideoBroadcast");
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
          //  window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        pushId=getIntent().getExtras().getString("pushId");
        //firebase declare

        reference= FirebaseDatabase.getInstance().getReference().child("EmergencyLive");
        liveVidBack=findViewById(R.id.liveVidBack);
        liveFireBaseUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(liveFireBaseUser).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUserName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //  String Uid=liveFireBaseUser.getUid();

        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        switchCamera=findViewById(R.id.switchCamera);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        mBroadcastButton = findViewById(R.id.BroadcastButton);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        liveVidBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBroadcaster.switchCamera();
            }
        });
        //mBroadcastButton.setPadding(width/2,height/2,width/2,10);

        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting()){
                    mBroadcaster.startBroadcast();
                    mBroadcastButton.setBackground(getResources().getDrawable(R.drawable.stop));
                } else {
                    mBroadcaster.stopBroadcast();
                    mBroadcastButton.setBackground(getResources().getDrawable(R.drawable.start));
                }
            }
        });

        idsList=new ArrayList<>();
        allSendRecyclerview=findViewById(R.id.allSend);
        allSendRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        historyDetailsAdapter = new HistoryDetailsAdapter(this, idsList);
        allSendRecyclerview.setAdapter(historyDetailsAdapter);
        reference= FirebaseDatabase.getInstance().getReference().child("Emergency").child(liveFireBaseUser).child("sent").
                child(pushId).child("ids");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren())
                {
                    for(DataSnapshot sn :snapshot.getChildren())
                    {
                        idsList.add(sn.getKey().toString());

                    }
                }
                historyDetailsAdapter.notifyDataSetChanged();
                sendEmergencyNotification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendEmergencyNotification() {
        for(int i=0; i<idsList.size(); i++){
            Log.i("!!!!!id",idsList.get(i));
            FirebaseDatabase.getInstance().getReference().child("Users").child(idsList.get(i)).child("tokenid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String token = snapshot.getValue(String.class);
                    Log.i("!!!!!Token",token);
                    EmergencyData data = new EmergencyData(currUserName,"is looking for your help", NOTIFICATION_TYPE_EMERGENCY);
                    EmergencySender sender = new EmergencySender(data, token);
                    apiService.sendEmergencyNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    Log.i("!!!!!Response","Successs");
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.i("!!!!!Response","Failed");
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
//        mBroadcaster.startBroadcast();
//        mBroadcastButton.setBackground(getResources().getDrawable(R.drawable.stop));
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i(LOGTAG, "Received status change: " + broadcastStatus);
            if (broadcastStatus == BroadcastStatus.STARTING){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mBroadcaster.setAuthor(liveFireBaseUser);
                mBroadcaster.setSendPosition(true);
            }
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "Start" : "Stop");
        }
        @Override
        public void onStreamHealthUpdate(int i) {
        }
        @Override
        public void onConnectionError(ConnectionError connectionError, String s){
            Log.w(LOGTAG, "Received connection error: " + connectionError + ", " + s);
        }
        @Override
        public void onCameraError(CameraError cameraError) {
            Log.w(LOGTAG, "Received camera error: " + cameraError);
        }
        @Override
        public void onChatMessage(String s) {
        }
        @Override
        public void onResolutionsScanned() {
        }
        @Override
        public void onCameraPreviewStateChanged() {
        }
        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {

        }
        @Override
        public void onBroadcastIdAvailable(String s) {
            reference.child(pushId).child("broadcastId").setValue(s);
           // Toast.makeText(getApplicationContext(),""+pushId+" "+s,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }
}
