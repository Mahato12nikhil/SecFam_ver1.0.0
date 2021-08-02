package com.project.nikhil.secfamfinal1.Emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import  com.project.nikhil.secfamfinal1.*;
import com.project.nikhil.secfamfinal1.Post.PostActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class EmergencyActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    ImageView main,notification,history;
    TextView h1;
    MaterialCardView cardView;


    private ViewPager mViewPager;
    private Emergency_Pager_Adapter mSectionsPagerAdapter;

    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;
    private Switch simpleSwitch;

    private RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        Log.i("!!!Activity", "EmergencyActivity");

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top

        }

        back=findViewById(R.id.emerg_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        //  mToolbar = (Toolbar) findViewById(R.id.emergencymain_page_toolbar);
        //  simpleSwitch=findViewById(R.id.simpleSwitch);
        // simpleSwitch.setTextOff("off");
        //simpleSwitch.setTextOff("on");

        //      setSupportActionBar(mToolbar);

//        getSupportActionBar().setTitle("emergency portal");


     /*   ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);*/


        main = (ImageView)findViewById(R.id.main) ;
        notification =(ImageView) findViewById(R.id.notification);
        history = (ImageView) findViewById(R.id.history);
        h1 = findViewById(R.id.h1);
        //      cardView = findViewById(R.id.history_notification);
//

        mViewPager =(ViewPager)findViewById(R.id.emergencymain_tabPager);
        mSectionsPagerAdapter = new Emergency_Pager_Adapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

/*
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), history_notification.class);
                startActivity(intent);            }
        });
*/







        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            main.setImageDrawable(getDrawable(R.drawable.house_w));
        }




        mViewPager.setCurrentItem(0);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });



        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    onChangeTab(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            String extra = getIntent().getExtras().getString("type");
            if (extra != null && extra.equals("EMERGENCY")) {
                mViewPager.setCurrentItem(1);
            }
        }catch (Exception e){}
    }



    private void onChangeTab(int position) {
        if (position == 0) {
            h1.setText("Home");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                main.setImageDrawable(getDrawable(R.drawable.house_w));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setImageDrawable(getDrawable(R.drawable.notification_b));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                history.setImageDrawable(getDrawable(R.drawable.history_b));
            }

        }


        if (position == 1) {

            h1.setText("Notifications");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                main.setImageDrawable(getDrawable(R.drawable.home));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setImageDrawable(getDrawable(R.drawable.notification_w));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                history.setImageDrawable(getDrawable(R.drawable.history_b));
            }
        }

        if (position == 2) {
            h1.setText("History");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                main.setImageDrawable(getDrawable(R.drawable.home));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setImageDrawable(getDrawable(R.drawable.notification_b));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                history.setImageDrawable(getDrawable(R.drawable.history_w));
            }

        }


    }



    //Tabs


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.emergencychat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.clearusers){


            clearusers();

        }

        if(item.getItemId() == R.id.clearpolice){

            clearpolice();


        }

/*
        if(item.getItemId() == R.id.main_all_btn){

            Intent settingsIntent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(settingsIntent);

        }*/

        return true;
    }

    private void clearpolice() {
        DatabaseReference clear=FirebaseDatabase.getInstance().getReference()
                .child("EmergencyChat").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        clear.removeValue();
    }

    private void clearusers() {

        DatabaseReference clear=FirebaseDatabase.getInstance().getReference()
                .child("Emergency").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        clear.removeValue();
    }
    public  void setSnackBar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isTaskRoot()){
            startActivity(new Intent(this,PostActivity.class));
        }else {
            PostActivity.selectHome();
        }
    }
}
