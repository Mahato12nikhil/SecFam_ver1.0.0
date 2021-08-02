package com.project.nikhil.secfamfinal1.LocationService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.R;


import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class BackgroundMapDataService extends Service {

    String userId;
    private LatLng oldLatLong;
    String lat,lng;
    private double locationLatitude;
    private  Handler handler;
    private double locationLongitude;
    private DatabaseReference vitimLocationRef;
    RemoteViews notificationLayout;
    NotificationManager mNotificationManager;
    Notification builder = null;
    public BackgroundMapDataService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId=intent.getStringExtra("userID").toString();
        lat=intent.getStringExtra("lat");
        lng=intent.getStringExtra("longi");
        oldLatLong=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
       // Toast.makeText(getApplicationContext(), ""+lat, Toast.LENGTH_SHORT).show();

        showToast();
        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

       // Intent intent=Intent.getIntent()
       // userId
       // oldLatLong=
        vitimLocationRef = FirebaseDatabase.getInstance().getReference("TrackLocation").child("Mj7d1gE6bQgt3wk4bPqQukVgTnX2").child("l");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //   Toast.makeText(getApplicationContext(),"Its greater than Oreo",Toast.LENGTH_LONG).show();
            buildnotificationoreo();
        }
        else {

            buildNotification();
        }

        updateVitimInfo();
    }
    @SuppressLint("RemoteViewLayout")
    private void buildnotificationoreo() {
      //  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String stop="stop";
        registerReceiver(stopReceiver,new IntentFilter(stop));

        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this,0,new Intent(stop),PendingIntent.FLAG_UPDATE_CURRENT);
        String Channel_id="OREO_CHANNEL_p";
        CharSequence name="Location v track";
        int importance= 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_MAX;
        }
        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(Channel_id,name,importance);
        }

         notificationLayout= new RemoteViews(getPackageName(), R.layout.map_data_notification);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        //bigTextStyle.setBigContentTitle("Secfam");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this,Channel_id)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    //Make this notification ongoing so it can’t be dismissed by the user//
                    .setOngoing(true)
                     .setColor(getResources().getColor(R.color.bg))

                    .setSmallIcon(R.drawable.sample)
                    .setContentIntent(broadcastIntent)

                    .setCustomContentView(notificationLayout)
                    .build();
        }
        startForeground(1, builder);


         mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }


    private void buildNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

            unregisterReceiver(stopReceiver);

//Stop the Service//

             stopSelf();
        }
    };

    public void updateVitimInfo(){


        vitimLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                //****************************
                Log.i("!!!!!Location","Changed");
               /* String uri ="google.navigation:q=" + oldLatLong.latitude + "," + oldLatLong.longitude + "&mode=l";
                Intent intentx = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intentx.setPackage("com.google.android.apps.maps");
                if (intentx.resolveActivity(Objects.requireNonNull(getApplicationContext()).getPackageManager()) != null) {
                    startActivity(intentx);
                }else {
                    Toast.makeText(getApplicationContext(), "You do not have Google Map installed", Toast.LENGTH_SHORT).show();
                }*/
                //*********************

                if(dataSnapshot.exists()){

                    List<Object> map = (List<Object>) dataSnapshot.getValue();

                    if(map.get(0) != null){
                        locationLatitude = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLongitude = Double.parseDouble(map.get(1).toString());
                    }

                }

                handler=new Handler();
                final int delay=1000;
                handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if(locationLatitude !=0 && locationLongitude !=0)
                                            {
                                                   /*     double theta = locationLongitude - oldLt.longitude;
                                                        double dist = Math.sin(Math.toRadians(locationLatitude)) * Math.sin(Math.toRadians(oldLt.latitude)) + Math.cos(Math.toRadians(locationLatitude)) * Math.cos(Math.toRadians(oldLt.latitude)) * Math.cos(Math.toRadians(theta));
                                                        dist = Math.acos(dist);
                                                        dist = Math.toDegrees(dist);
                                                        dist = dist * 60 * 1.1515;
                                                        dist = dist * 1.609344;*/


                                               // Toast.makeText(getApplicationContext(),""+oldLatLong,Toast.LENGTH_LONG).show();

                                                distanceCalculation(locationLatitude,locationLongitude);
                                            }

                                            else {

                                                handler.postDelayed(this,delay);
                                            }


                                        }
                                    },delay
                );
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void showToast()
    {
    }

    private void distanceCalculation(double locationLatitude, double locationLongitude) {
        double theta = locationLongitude - oldLatLong.longitude;
        double dist = Math.sin(Math.toRadians(locationLatitude)) * Math.sin(Math.toRadians(oldLatLong.latitude)) + Math.cos(Math.toRadians(locationLatitude)) * Math.cos(Math.toRadians(oldLatLong.latitude)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        Toast.makeText(getApplicationContext(),""+oldLatLong+"="+locationLatitude+","+locationLongitude,Toast.LENGTH_LONG).show();
       String d= String.format("%.04f", dist);
       // Toast.makeText(getApplicationContext(),"he moved "+d+" meter.",Toast.LENGTH_LONG).show();

        if((int)(Double.parseDouble(d)*1000)>50) {
            if (Double.compare(Double.parseDouble(d), 1.0) > 0.0) {
              //  Toast.makeText(getApplicationContext(), "he moved " + Double.parseDouble(d) + " km", Toast.LENGTH_LONG).show();
                notificationLayout.setTextViewText(R.id.positionChangedDistance, "" + Double.parseDouble(d) + " KM");
                mNotificationManager.notify(1, builder);

            } else {

                notificationLayout.setTextViewText(R.id.positionChangedDistance, "" + (int) (Double.parseDouble(d) * 1000) + " meter");
                mNotificationManager.notify(1, builder);

             //   Toast.makeText(getApplicationContext(), "he moved " + Double.parseDouble(d) * 1000 + " meter", Toast.LENGTH_LONG).show();


            }
        }
        oldLatLong=new LatLng(locationLatitude,locationLongitude);



        //notificationLayout.
      /*  if(100<Double.parseDouble(d)*1000)
        {
            notificationLayout.setTextViewText(R.id.positionChangedDistance,String.valueOf(dist));
            mNotificationManager.notify(1, builder);

        }*/


    }

    LocationCallback locationCallback=new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {


        }
    };
}
