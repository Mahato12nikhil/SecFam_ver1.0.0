package com.project.nikhil.secfamfinal.LocationService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.NetworkStats;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.BootService.BootCompleteReceiver;
import com.project.nikhil.secfamfinal.Location.Location_get;
import com.project.nikhil.secfamfinal.R;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


public class TrackingService extends Service {

    String data;
    FirebaseAuth firebaseAuth;

    private String userdata;
    public TrackingService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       firebaseAuth=FirebaseAuth.getInstance();

        Toast.makeText(getApplicationContext(),"Its greater than Oreo",Toast.LENGTH_LONG).show();

        userdata=  FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference   mUserRef = FirebaseDatabase.getInstance().getReference().child("Status").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUserRef.child("online").setValue("true");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            buildnotificationoreo();
        }
        else {

            buildNotification();
        }
        requestLocationUpdates();


    }

    private void buildnotificationoreo() {

        String stop="stop";
        registerReceiver(stopReceiver,new IntentFilter(stop));

        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
          this,0,new Intent(stop),PendingIntent.FLAG_UPDATE_CURRENT);


        String Channel_id="OREO_CHANNELM";
        CharSequence name="Location track";
        int importance= 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }
        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(Channel_id,name,importance);
        }
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);

       Notification builder = null;
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        //bigTextStyle.setBigContentTitle("Secfam");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this,Channel_id)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setContentTitle("Make your life safe")
    //Make this notification ongoing so it can’t be dismissed by the user//
                    .setOngoing(true)

                    .setContentIntent(broadcastIntent)
                    .setSmallIcon(R.drawable.secfam_logo)
            .build();
        }
        startForeground(109, builder);





    }


    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

// Create the persistent notification//
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.tracking_enabled_notif))

//Make this notification ongoing so it can’t be dismissed by the user//

                .setShowWhen(false)
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.target)
                ;

       // builder.flags = notification.flags | Notification.FLAG_NO_CLEAR;
        startForeground(1, builder.build());
    }
    protected BroadcastReceiver stopReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//Unregister the BroadcastReceiver when the notification is tapped//

          //  unregisterReceiver(stopReceiver);

//Stop the Service//

            //stopSelf();
        }
    };

 void requestLocationUpdates() {


        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(1800000);
        request.setFastestInterval(1800000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
       // final String path = getString(R.string.firebase_path);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {


                    DatabaseReference ref = null;
                    DatabaseReference ref2 = null;



            try {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Latitude");
                ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Longitude");


            } catch (Exception e) {

                e.printStackTrace();
            }


                    Location location = locationResult.getLastLocation();

                    if (location != null && ref != null && ref2 != null) {

//Save the location data to the database//
                        Double    latitude= null;
                        Double     longitude= null;
                        try {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();


                            Location_get location_get=new Location_get(latitude, longitude);
                            location_get.setlongitude(latitude);
                            location_get.setlongitude(longitude);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ref.setValue(latitude);
                        ref2.setValue(longitude);


                       }

//Save the location data to the database//



                }
            }, null);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, BootCompleteReceiver.class);
      //  this.sendBroadcast(broadcastIntent);
/*
        DatabaseReference   mUserRef = FirebaseDatabase.getInstance().getReference().child("Status").child(userdata);
        mUserRef.child("online").setValue(ServerValue.TIMESTAMP);*/

    }

}
