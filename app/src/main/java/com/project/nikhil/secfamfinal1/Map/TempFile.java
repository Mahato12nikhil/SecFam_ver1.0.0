package com.project.nikhil.secfamfinal1.Map;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Emergency.MapVideoActivity;
import com.project.nikhil.secfamfinal1.LocationService.BackgroundMapDataService;
import com.project.nikhil.secfamfinal1.LocationService.TrackingService;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class TempFile extends Fragment implements OnMapReadyCallback, RoutingListener {

    // private static TrackuserActivity mInstance;
    MapView mapView;

    List<LatLng> latLongPoints;
    private Handler handler;
    private GoogleMap mMap;
    Marker markerx;
    Marker marker;

    private int monitor = 0;
    LocationManager locationManager;
    private DatabaseReference vitimLocationRef;
    private LinearLayout startloc, route;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastlocation;
    private LatLng pickupltlng;

    Bitmap smallMarker, userMarker;
    private DatabaseReference refAvailable;
    LatLng latLng;
    LatLng oldLatLong;
    private double locationLatitude;
    private double locationLongitude;
    LocationListener locationListener;
    Location prevLocation, newLocation;
    Location nLocation;
    float start_rotation = 0;

    Routing routing;
    List<Address> addresse;
    private String idd;
    private GeoFire geoFireAvailable;
    FusedLocationProviderClient mFusedLocationProviderClient;
    TextView time_kl, dist_kl, adddd;
    private String victimId;
    LatLng myLatLng = new LatLng(22.8636375,88.292529), victimLatLng = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_map, container, false);
        victimId= ((MapVideoActivity) getActivity()).getVictimId();
        // polylines = new ArrayList<>();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        vitimLocationRef = FirebaseDatabase.getInstance().getReference("TrackLocation").child(victimId).child("l");
        refAvailable = FirebaseDatabase.getInstance().getReference("LocationReady");
        geoFireAvailable = new GeoFire(refAvailable);


        String languageToLoad = "bn_";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,
                this.getResources().getDisplayMetrics());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        time_kl = view.findViewById(R.id.tvAddress);
       /* dist_kl =view. findViewById(R.id.dist_kl);
        adddd = view.findViewById(R.id.adddd);*/
        startloc =view. findViewById(R.id.centerView);
        route = view.findViewById(R.id.route_on_googleMap);
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
        Bitmap b = bitmapdraw.getBitmap();
        bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.navigator);
        Bitmap c = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        userMarker = Bitmap.createScaledBitmap(c, width, height, false);
        Intent mu = getActivity().getIntent();
        idd = mu.getStringExtra("publisherid");
        mFusedLocationProviderClient = new FusedLocationProviderClient(getContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                /*if (pickupltlng != null) {
                    oldLatLong = pickupltlng;
                    findRoutes(pickupltlng);
                    getRegularUpdateOfVictim();
                    Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
                }*/
                //  oldLatLong = pickupltlng;
/*
                if(oldLatLong!=null)
                {*/
                Intent intent=new Intent(getContext(), TrackingService.class);
                getContext().stopService(intent);
                Intent intent1=new Intent(getContext(), BackgroundMapDataService.class);

                intent1.putExtra("userID",victimId);
                intent1.putExtra("lat",String.valueOf(victimLatLng.latitude));
                intent1.putExtra("longi",String.valueOf(victimLatLng.longitude));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getContext().startForegroundService(intent1);
                }
                else {
                    getContext().startService(intent1);
                }
                  /*  String uri ="http://maps.google.com/maps?q=loc:" + oldLatLong.latitude + "," + oldLatLong.longitude + " (" + "Victim" + ")";
                    Intent intentx = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intentx);*/
                String uri ="google.navigation:q=" + victimLatLng.latitude + "," + victimLatLng.longitude + "&mode=l";
                Intent intentx = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intentx.setPackage("com.google.android.apps.maps");
                if (intentx.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                    startActivity(intentx);
                }else {
                    Toast.makeText(getActivity(), "You do not have Google Map installed", Toast.LENGTH_SHORT).show();
                }
                // }
            }
        });

        startloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceInten1= new Intent(getContext(), BackgroundMapDataService.class);
                // ContextCompat.startForegroundService(getApplicationContext(),serviceInten1 );

                //  startNavigation(latLng);
            }
        });
        return view;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        //  mMap.setTrafficEnabled(false);
        //mMap.setBuildingsEnabled(false);
        mMap.getFocusedBuilding();
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        /*  mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                 getContext(), R.raw.stylemap));*/

        //   mMap.getUiSettings().setCompassEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

                getRegularUpdateOfVictim();
                route.setEnabled(true);
                mMap.setMyLocationEnabled(true);
            } else {
                checkpermission();
            }
        }

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                myLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                findRoutes();
            }
        });
    }

    private void checkpermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))

            {
                new AlertDialog.Builder(getContext())
                        .setTitle("Give permission")
                        .setMessage("Give Permission Message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 27);
                            }
                        })
                        .create()
                        .show();


            }
        }

    }

    private void getRegularUpdateOfVictim() {
        locationLatitude=0;
        locationLongitude=0;
        vitimLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    if(map.get(0) != null){
                        locationLatitude = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLongitude = Double.parseDouble(map.get(1).toString());
                    }
                    victimLatLng = new LatLng(locationLatitude,locationLongitude);
                    findRoutes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void findRoutes() {
        Log.i("!!!!VictimLatLong", ""+victimLatLng.latitude+", "+victimLatLng.longitude);
        mMap.clear();
        if (myLatLng != null && victimLatLng != null) {
            routing = new Routing.Builder()
                    .withListener(this)
                    .alternativeRoutes(false)
                    .travelMode(Routing.TravelMode.DRIVING)
                    .key("AIzaSyAei-0przVDN0uwzHuW8fF5XAyZAHeceTE")
                    .waypoints(myLatLng, victimLatLng)
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
        Log.i("!!!!!!!Route Exc",e.toString());
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        zoomRoute();
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;
        for (int i=0; i< route.size(); i++) {
            if (i == shortestRouteIndex) {
                polyOptions.color(Color.BLUE);
                polyOptions.width(15f);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
            }
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        //Bitmap myIcon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_navigation);
        //startMarker.icon(BitmapDescriptorFactory.fromBitmap(myIcon));
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Victim Location");
        // Bitmap victimIcon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_person_pin);
        // endMarker.icon(BitmapDescriptorFactory.fromBitmap(victimIcon));
        mMap.addMarker(endMarker);


//        for(int i=0;i<route.size();i++) {
//            latLongPoints = route.get(i).getPoints();
//        }
//        setAnimation(mMap,latLongPoints,smallMarker,route.get(route.size()-1).getDurationValue());
    }

    private void zoomRoute() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(myLatLng);
        builder.include(victimLatLng);
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                CameraUpdate zOut = CameraUpdateFactory.zoomBy(-1.0f);
                mMap.animateCamera(zOut);
            }
            @Override
            public void onCancel() {
            }
        });

    }

    @Override
    public void onRoutingCancelled() {
    }


   /* private void setAnimation(GoogleMap myMap, List<LatLng> directionPoint, Bitmap smallMarker, int durationValue) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(directionPoint.get(0));
        builder.include(directionPoint.get(directionPoint.size()-1));
        LatLngBounds bounds = builder.build();
        if(directionPoint.size()>1){
           // markerx.remove();
            marker = myMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .position(directionPoint.get(0))
                    .flat(true));
            animateMarker(myMap, marker, directionPoint, false,durationValue);
        }
    }


    private static void animateMarker(final GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker, int durationValue) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final int duration = durationValue;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size()) {
                    marker.setPosition(directionPoint.get(i));
                    myMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (new CameraPosition.Builder().target((directionPoint.get(i)))
                                    .zoom(12f).build()));                }
                i++;


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {

                        marker.setVisible(true);
                    }
                }
            }
        });
    }




*/
  /* private void distanceCalculation(double locationLatitude, double locationLongitude) {
       if(monitor==0)
       {

           oldLatLong=new LatLng(locationLatitude,locationLongitude);
           if(markerx!=null)
               markerx.remove();

*//*            markerx=  mMap.addMarker(new MarkerOptions()

                    .position((new LatLng(locationLatitude,locationLongitude)))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("target")
                    .anchor(0.5f, 0.5f)
                    .flat(true));*//*

           monitor++;
       }


       if(oldLatLong==null)
           mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(locationLatitude,locationLongitude)));
       else {
           mMap.moveCamera(CameraUpdateFactory.newLatLng(oldLatLong));

       }
       //mMap.animateCamera(CameraUpdateFactory.zoomTo(12f));

       // animateMarker(new  LatLng(locationLatitude,locationLongitude),new LatLng(22.5814,88.4505),false);


       //findRoutes(new LatLng(locationLatitude,locationLongitude));
       oldLatLong=new LatLng(locationLatitude,locationLongitude);

   }*/
}
