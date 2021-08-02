package com.project.nikhil.secfamfinal1.Profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

public class ProfilePostActivity extends BaseActivity {

    RelativeLayout media_layout,posts_layout;
    TextView media,posts;
    ViewPager viewPager;
    String profId;
    Profile_section_pager_adapter pagerViewAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_post);
        Log.i("!!!Activity", "ProfilePostActivity");

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top
        }
        profId=getIntent().getStringExtra("id");
        getId();

        media_layout=findViewById(R.id.media_layout);
        posts_layout=findViewById(R.id.posts_layout);

        media=findViewById(R.id.media);
        posts=findViewById(R.id.posts);
        back=findViewById(R.id.profile_post_back);


        viewPager =findViewById(R.id.fragment_container);
        pagerViewAdapter = new Profile_section_pager_adapter (getSupportFragmentManager ());
        viewPager.setAdapter(pagerViewAdapter);
        viewPager.setCurrentItem(0);


back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});





        media_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });



        posts_layout.setOnClickListener(new View.OnClickListener() {
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



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeTab(int position) {
        if (position == 0) {
            media.setTextSize ( 22 );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                media.setTextColor ( getColor ( R.color.white ) );
            }
            posts.setTextSize ( 20 );
            posts.setTextColor ( getColor ( R.color.gray ) );
        }


        if (position == 1) {

            media.setTextSize ( 20 );
            media.setTextColor ( getColor ( R.color.gray ) );

            posts.setTextSize ( 22 );
            posts.setTextColor ( getColor ( R.color.white ) );
        }

    }
    public  String getId(){

        return profId;
    }

}
