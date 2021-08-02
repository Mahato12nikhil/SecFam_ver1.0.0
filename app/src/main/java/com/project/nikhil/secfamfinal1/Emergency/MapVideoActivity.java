package com.project.nikhil.secfamfinal1.Emergency;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

public class MapVideoActivity extends BaseActivity {

    ImageView map,video;
    TextView text;
    String lastKey,victimId;
    ViewPager viewPager;
    VideoMapPagerAdapter pagerViewAdepter;
    RelativeLayout back;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_video);
        Log.i("!!!Activity", "MapVideoActivity");

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg_light)); //status bar or the time bar at the top
        }

        map=findViewById ( R.id.map );
        video=findViewById (R.id.videoxy);
        text=findViewById (R.id.text);
        back=findViewById(R.id.map_video_back);

        viewPager =(ViewPager)findViewById(R.id.fragment_container);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pagerViewAdepter = new VideoMapPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdepter);

        lastKey=getIntent().getStringExtra("pushId");
        victimId = getIntent().getStringExtra("victim_id");
        setLastCount(lastKey);

        map.setImageDrawable ( getDrawable ( R.drawable.map_1 ) );

        viewPager.setCurrentItem(0);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });


       video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    }

    @SuppressLint({"UseCompatLoadingForDrawables" , "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onChangeTab(int position) {
        if (position == 0) {
            text.setText("Map");
            map.setImageDrawable ( getDrawable ( R.drawable.map_1 ) );
            video.setImageDrawable(getDrawable(R.drawable.video_2 ));
        }
        /*if (position == 1) {
            text.setText("Live Video");
            map.setImageDrawable(getDrawable(R.drawable.map_2 ));
            video.setImageDrawable(getDrawable(R.drawable.video_1));
        }*/
    }

    public String getPushId() {
        return this.lastKey;
    }
    public String getVictimId() {
        return this.victimId;
    }
    public void setLastCount(String newValue) {
        this.lastKey = newValue;
    }
}


