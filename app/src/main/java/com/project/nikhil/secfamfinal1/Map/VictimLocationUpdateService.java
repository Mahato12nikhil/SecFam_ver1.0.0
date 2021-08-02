package com.project.nikhil.secfamfinal1.Map;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.MainActivity;
import com.project.nikhil.secfamfinal1.R;

import java.util.List;

public class VictimLocationUpdateService extends Service {
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";


    private DatabaseReference victimLocationRef;
    private ValueEventListener valueEventListener;
    public static final String CHANNEL_ID = "VictimForegroundServiceChannel";
    public static final String UPDATE_CHANNEL_ID = "VictimUpdate";

    private double victimLatitude = 0;
    private double victimLongitude = 0;
    int check = 0;
    AlertDialog alertDialog=null;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    Log.i("!!!!Status","Service Started");
                    createNotificationChannel();
                    showNotificationForService();
                    //do heavy work on a background thread
                    getRegularUpdateOfVictim(intent);
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }

    private void stopForegroundService() {
        Log.i("!!!!Status","Service Stopped");
        if (victimLocationRef != null && valueEventListener != null){
            victimLocationRef.removeEventListener(valueEventListener);
        }
        // Stop foreground service and remove the notification.
        stopForeground(true);
        // Stop the foreground service.
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotificationForService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SecFam")
                .setContentText("Updating victim Location")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void getRegularUpdateOfVictim(Intent intent) {
        String victimId = intent.getStringExtra("victimId");
        victimLocationRef = FirebaseDatabase.getInstance().getReference("TrackLocation").child(victimId).child("l");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    if (map.get(0) != null) {
                        victimLatitude = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        victimLongitude = Double.parseDouble(map.get(1).toString());
                    }
                    //If Victim Location change Fire a  notification
                    if (check > 0) {
                        NotificationForVictimLocationChange();
                    }
                    check++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        victimLocationRef.addValueEventListener(valueEventListener);
    }

    private void NotificationForVictimLocationChange() {
        //Toast.makeText(this, "Victim location has been changed. Please Check Notification", Toast.LENGTH_SHORT).show();
        showDialog();
        getSimpleNotification();
    }

    public void showDialog() {
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_MaterialDialogTheme);
        builder.setTitle("Victim location has been changed.").setMessage("Please check notification for getting updated location of the victim").setIcon(R.drawable.map_loc)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        Log.i("!!!!Action","Clicked" );
                        /*String uri = "google.navigation:q=" + victimLatitude + "," + victimLongitude + "&mode=l";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    }
                });
        alertDialog = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else {
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        alertDialog.show();
    }

    public void getSimpleNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), UPDATE_CHANNEL_ID)
                .setSmallIcon(R.drawable.secfam_logo)
                .setContentTitle("Victim location has been changed.")
                .setContentText("Click here for get updated location")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.map_loc))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(setIntent())
                .setAutoCancel(true);
        mBuilder.addAction(R.drawable.map_loc, "Update", setIntent());

        setNotificationManager(mBuilder);
    }

    private PendingIntent setIntent() {
        String uri = "google.navigation:q=" + victimLatitude + "," + victimLongitude + "&mode=l";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(getApplication(), 0, intent, 0);
    }

    private void setNotificationManager(NotificationCompat.Builder mBuilder) {
        // Gets an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "victim";
            String description = "victim";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(UPDATE_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        //to post your notification to the notification bar
        notificationManager.notify(0, mBuilder.build());
    }
}
