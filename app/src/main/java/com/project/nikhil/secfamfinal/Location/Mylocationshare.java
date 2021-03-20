package com.project.nikhil.secfamfinal.Location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.content.ContextCompat;

public class Mylocationshare extends Service {

  private FusedLocationProviderClient mFusedLocationProviderClient;
  private LocationRequest mLocationRequest;

  @Override
  public void onCreate() {
    super.onCreate();
    Toast.makeText(getApplicationContext(),"MyLocation service started",Toast.LENGTH_LONG).show();

    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(3000);
    mLocationRequest.setFastestInterval(3000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    mFusedLocationProviderClient = new FusedLocationProviderClient(this);

    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        // mMap.setMyLocationEnabled(true);
      } else {
        // checkpermission();
      }
    }
  }





  public Mylocationshare() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");

  }

  LocationCallback mLocationCallback = new LocationCallback() {
    @Override
    public void onLocationResult(LocationResult locationResult) {
      for (Location location : locationResult.getLocations()) {
        if (getApplicationContext() != null) {


          String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
          DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("TrackLocation");
          GeoFire geoFireAvailable = new GeoFire(refAvailable);
          geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
          // pickupltlng = new LatLng(23.0091339, 86.3714119);
          Marker pickupMarker;
          // pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupltlng).title("victim's location").icon(BitmapDescriptorFactory.fromResource(R.drawable.orca_attach_location_pressed)));

          //getRouteToMarker(pickupltlng);

                    /*switch (customerId){
                        case "":*/
          // geoFireWorking.removeLocation(userId);


                       /* default:
                            geoFireAvailable.removeLocation(userId);
                            geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;*/
        }
      }
    }

  };


  @Override
  public void onDestroy() {
    super.onDestroy();
    stopSelf();

    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);

  }
}

