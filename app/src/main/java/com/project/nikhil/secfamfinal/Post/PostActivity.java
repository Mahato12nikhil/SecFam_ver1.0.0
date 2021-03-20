package com.project.nikhil.secfamfinal.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.project.nikhil.secfamfinal.Chat.ChatHomeActivity;
import com.project.nikhil.secfamfinal.Emergency.EmergencyActivity;
import com.project.nikhil.secfamfinal.LocationService.TrackingService;
import com.project.nikhil.secfamfinal.MainActivity;
import com.project.nikhil.secfamfinal.Notification.NotificationFragment;
import com.project.nikhil.secfamfinal.Notification.Token;
import com.project.nikhil.secfamfinal.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal.R;
import com.project.nikhil.secfamfinal.Search.SearchActivity;
import com.project.nikhil.secfamfinal.Upload.UploadBottom_Sheet;

import java.util.List;

public class PostActivity extends AppCompatActivity   implements  CompoundButton.OnCheckedChangeListener {

    private static final int PERMISSIONS_REQUEST = 100;
    private AppBarLayout appBarLayout;
    private Button My_profile;
    String url, name;
    public static ProfileDetails profileDetails;
    String myid;
    private ImageView emergencytouch;
    private DatabaseReference reference;
    private Switch drawer_switch;
    private ImageView simpleSwitch;
    RelativeLayout userSearch;
    BottomNavigationView bottomNavigationView;
    Fragment selectedfragment = null;
    private CircleImageView drawimage99;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    //private  ArrayList<Nav_data_model> data;
    private static final String SELECTED_ITEM = "arg_selected_item";

    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        AudienceNetworkAds.initialize(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top
        }
        FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();

        if (USER == null) {
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        } else {
            myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            updateToken(FirebaseInstanceId.getInstance().getToken());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(myid).child("isOnline");
            ref.setValue(true);

        }
        drawimage99 = findViewById(R.id.drawimage99);
        layoutManager = new LinearLayoutManager(this);

        appBarLayout = findViewById(R.id.bvvar);
        userSearch = findViewById(R.id.userSearchPo);

        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            //  selectedItem = bottomNavigationView.getMenu().findItem(mSelectedItem);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        ((AppCompatActivity) PostActivity.this).setSupportActionBar(toolbar);

        drawimage99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, ProfileActivity.class);

                intent.putExtra("profileid", myid);
                startActivity(intent);
            }
        });


        // Toast.makeText(getApplicationContext(),""+FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation1);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        //
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        //Check whether GPS tracking is enabled//
        {
            checkLocationPermission();

            myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(myid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    url = dataSnapshot.child("image").getValue(String.class);
                    name = dataSnapshot.child("name").getValue(String.class);

                    if (url != null || name != null) {
                        profileDetails = new ProfileDetails(name, url);
                    }

                    if (url != null) {
                        Glide.with(getApplicationContext()).load(url).into(drawimage99);

                    } else {
                        try {
                            https:
//firebasestorage.googleapis.com/v0/b/secfam-87ea2.appspot.com/o/Thumbnails%2Ffemale_placeholder.png?alt=media&token=818bd61a-49f1-4aaf-b137-8ced43b99cb2
                            Glide.with(getApplicationContext()).load(R.drawable.man_placeholder)
                                    .into(drawimage99);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        bottomNavigationView = findViewById(R.id.bottom_navigation1);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //  Bundle intent = getIntent().getExtras();
        Bundle intent = getIntent().getExtras();
        String publisher = null;
        try {
            publisher = intent.getString("publisherid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newInstance()).commit();

    }

    private void checkLocationPermission() {


        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "turn on gps", Toast.LENGTH_SHORT).show();
        }
        //Check whether this app has access to the location permission      check app itself    //
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//andoid permission for upper level API


        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
//If the app doesn’t currently have access to the user’s location, then request access//

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    PERMISSIONS_REQUEST);
        }

    }

    public static String getName() {


        return profileDetails.getName();

    }

    public static String getImage() {
        return profileDetails.getImage();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()) {

                case R.id.nav_home:
                    selectedfragment = null;
                    //Toast.makeText(getApplicationContext(),"HomeFragment",Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, newInstance()).commit();

                    //  selectedfragment=new HomeFragment();
                    break;
                case R.id.nav_message:
                    Intent iny = new Intent(PostActivity.this, ChatHomeActivity.class);
                    startActivity(iny);
                    //startActivity(new Intent(PostActivity.this,LiveVideoBroadcast.class));


                    //  selectedfragment=new SearchFragment();
                    break;

                case R.id.nav_add:
                    UploadBottom_Sheet bottomSheet_fragment = UploadBottom_Sheet.newInstance();

                    bottomSheet_fragment.show(getSupportFragmentManager(), "fragment_bottom_sheet");
                  /*  selectedfragment=null;
                    startActivity(new Intent(ShowActivity.this,GalleryFace.class));*/
                    break;

                case R.id.nav_heart:
                    //   startActivity(new Intent(ShowActivity.this,GetLiveVideo.class));
                    selectedfragment = new NotificationFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedfragment).commit();
                    break;
                case R.id.nav_emergency:
                    startActivity(new Intent(PostActivity.this, EmergencyActivity.class));

                    break;


            }

         /*   if(selectedfragment!=null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedfragment).commit();
            }*/

            return true;
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//

            startTrackerService();
        } else {

            //If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }


    private void startTrackerService() {
        if (!checkServiceRunning(TrackingService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                Intent serviceIntent = new Intent(getApplicationContext(), TrackingService.class);
                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
                // Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
                if (checkServiceRunning(TrackingService.class)) {
                    Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

                }

            }


            //Notify the user that tracking has been enabled//
        } else {
            //  Toast.makeText(this, "Already running..", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    // Here you will enable Multidex
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //  MultiDex.install(getBaseContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myid != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(myid).child("isOnline");
            ref.setValue(false);
        }

    }


    private Boolean checkServiceRunning(Class<TrackingService> trackingServiceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (trackingServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Token token1 = new Token(token);


        // FirebaseAuth.getInstance().get
        reference.child("tokenid").setValue(token);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Fragment fragment = new NotificationFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        if (intent != null) {

            String data = intent.getStringExtra("userid");

            if (data != null) {


            } else {
                Toast.makeText(getApplicationContext(), "hello Notification", Toast.LENGTH_SHORT).show();

            }

        }
  }

    public static HomeFragment newInstance() {

        HomeFragment photoShow = new HomeFragment();

        return photoShow;
    }

}