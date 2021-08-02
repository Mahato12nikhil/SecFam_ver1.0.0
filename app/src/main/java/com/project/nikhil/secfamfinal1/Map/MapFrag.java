package com.project.nikhil.secfamfinal1.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Emergency.MapVideoActivity;
import com.project.nikhil.secfamfinal1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MapFrag extends Fragment implements OnMapReadyCallback, RoutingListener {
    private static final int REQUEST_LOCATION_PERMISSION = 120;
    private static final String TAG = "MapFrag";
    private static final int REQUEST_CODE = 25;
    private GoogleMap mMap;
    private String victimId;
    private double victimLatitude = 0;
    private double victimLongitude = 0;
    private LatLng myCurrentLatLng = null;
    private LatLng victimPrevLatLng = null;
    private LatLng victimCurrentLatLng = null;
    Routing routing;
    private int monitor = 0;
    private DatabaseReference victimLocationRef;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient mFusedLocationClient;
    MarkerOptions victimMarkerOption;
    Marker victimMarker;
    String GOOGLE_MAP_API_KEY;
    TextView tvAddress;
    Activity activity;
    Handler handler = new Handler();
    private LinearLayout centerView, routeOnGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        activity = (MapVideoActivity) getActivity();
        centerView = view.findViewById(R.id.centerView);
        routeOnGoogleMap = view.findViewById(R.id.route_on_googleMap);
        tvAddress = view.findViewById(R.id.tvAddress);
        GOOGLE_MAP_API_KEY = ((MapVideoActivity) Objects.requireNonNull(getActivity())).getResources().getString(R.string.google_map_api_key);
        victimId = ((MapVideoActivity) getActivity()).getVictimId();
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_victim_locatin);
        victimMarkerOption = new MarkerOptions().title("Victim").icon(BitmapDescriptorFactory.fromBitmap(icon));
        victimLocationRef = FirebaseDatabase.getInstance().getReference("TrackLocation").child(victimId).child("l");


        routeOnGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findRouteAndStartService();
            }
        });

        centerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomRoute();
            }
        });
        return view;
    }

    private void findRouteAndStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName()));
                // request permission via start activity for result
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                String uri = "google.navigation:q=" + victimCurrentLatLng.latitude + "," + victimCurrentLatLng.longitude + "&mode=l";
                Intent intentX = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intentX.setPackage("com.google.android.apps.maps");
                if (intentX.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                    startActivity(intentX);//Open Google Map Application
//                  Call Background Service
                    startService();
                } else {
                    Toast.makeText(getActivity(), "You do not have Google Map installed", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            String uri = "google.navigation:q=" + victimCurrentLatLng.latitude + "," + victimCurrentLatLng.longitude + "&mode=l";
            Intent intentX = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intentX.setPackage("com.google.android.apps.maps");
            if (intentX.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                startActivity(intentX);
//                  Call Background Service
                startService();
            } else {
                Toast.makeText(getActivity(), "You do not have Google Map installed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startService() {
        Intent serviceIntent = new Intent(getActivity(), VictimLocationUpdateService.class);
        serviceIntent.putExtra("victimId", victimId);
        serviceIntent.setAction(VictimLocationUpdateService.ACTION_START_FOREGROUND_SERVICE);
        ContextCompat.startForegroundService(getActivity(), serviceIntent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("App", "OnActivity Result.");
        //check if received result code
        //  is equal our requested code for draw permission
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getActivity())) {
                    findRouteAndStartService();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MapVideoActivity) getActivity();
    }

    private boolean checkForPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            myCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            if (victimCurrentLatLng != null) {
                                Log.i("!!!!!Location", location.getLatitude() + ", " + location.getLongitude());
                                //zoomRoute();
                            }
                        }
                    });
            mMap.setMyLocationEnabled(true);
            getRegularUpdateOfVictim();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForPermission();
                } else {
                    Toast.makeText(getActivity(),
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getFocusedBuilding();
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        checkForPermission();

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        PolylineOptions polyOptions = new PolylineOptions();
        for (int i = 0; i < route.size(); i++) {
            if (i == shortestRouteIndex) {
                polyOptions.color(Color.BLUE);
                polyOptions.width(10f);
                polyOptions.startCap(new SquareCap());
                polyOptions.endCap(new SquareCap());
                polyOptions.jointType(JointType.ROUND);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                if (route.get(shortestRouteIndex).getPoints().size() > 0) {
                    setAnimation(victimMarker, route.get(shortestRouteIndex).getPoints());
                }
            }
        }
    }

    private void setAnimation(final Marker marker, final List<LatLng> directionPoint) {
        handler.removeCallbacksAndMessages(null);
        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                marker.setPosition(directionPoint.get(i));
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition
                        (new CameraPosition.Builder().target((directionPoint.get(i)))
                                .zoom(15f).build()));
                /*if (i == (directionPoint.size() - 1)) {
                    zoomRoute();
                }*/
                i++;
                if (i < directionPoint.size()) {
                    handler.postDelayed(this, 20);
                }
            }
        });
    }


    @Override
    public void onRoutingCancelled() {

    }

    private void getRegularUpdateOfVictim() {
        victimLocationRef.addValueEventListener(new ValueEventListener() {
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
                    victimCurrentLatLng = new LatLng(victimLatitude, victimLongitude);
                    getAddress(victimLatitude, victimLongitude);
                    if (victimPrevLatLng != null) {
                        FindRoutesOnMap();
                    } else {//When first time opening the screen
                        zoomRoute();
                        victimMarkerOption.position(victimCurrentLatLng);
                        victimMarker = mMap.addMarker(victimMarkerOption);
                    }
                    victimPrevLatLng = victimCurrentLatLng;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void zoomRoute() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (myCurrentLatLng != null) {
            builder.include(myCurrentLatLng);
        }
        builder.include(new LatLng(victimLatitude, victimLongitude));
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

    private void FindRoutesOnMap() {
        routing = new Routing.Builder()
                .withListener(this)
                .alternativeRoutes(false)
                .travelMode(Routing.TravelMode.DRIVING)
                .key(GOOGLE_MAP_API_KEY)
                .waypoints(victimPrevLatLng, victimCurrentLatLng)
                .build();
        routing.execute();
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            //add = add + "\n" + obj.getSubThoroughfare();
            //add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();
            //add = add + "\n" + obj.getPostalCode();
            //add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + "\n" + obj.getAdminArea();
            tvAddress.setText(add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
