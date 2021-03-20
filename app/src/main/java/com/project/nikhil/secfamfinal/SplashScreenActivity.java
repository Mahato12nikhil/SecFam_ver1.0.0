package com.project.nikhil.secfamfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.project.nikhil.secfamfinal.Post.PostActivity;

public class SplashScreenActivity extends AppCompatActivity {

    Animation rotateAnimation;
    ImageView logo_top,intro_bg,logo_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor( ContextCompat.getColor(this,R.color.bg_light)); //status bar or the time bar at the top
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        logo_top=findViewById ( R.id.logo_top );
        intro_bg=findViewById ( R.id.intro_bg );
        logo_bottom=findViewById ( R.id.logo_bottom );

        rotateAnimation();

    }

    private void rotateAnimation() {

        rotateAnimation= AnimationUtils.loadAnimation ( this,R.anim.rotation );
        logo_top.setAnimation ( rotateAnimation );
        intro_bg.animate ().translationY ( -4000 ).setDuration ( 1000 ).setStartDelay ( 1000 );
        logo_top.setVisibility ( View.INVISIBLE );
        //logo_top.animate ().translationY ( 4000 ).setDuration ( 1000 ).setStartDelay ( 2000 );
        logo_bottom.animate ().translationY ( 4000 ).setDuration ( 1000 ).setStartDelay ( 1000 );

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this,PostActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, 1000);
    }
}