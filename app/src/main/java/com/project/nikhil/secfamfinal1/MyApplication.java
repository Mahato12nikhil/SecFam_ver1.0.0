package com.project.nikhil.secfamfinal1;

import android.app.Application;
import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ResourceBundle;

public class MyApplication extends Application {
    private static MyApplication mApp = null;
    public MySharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        pref = new MySharedPreferences(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
        mApp = this;
        AudienceNetworkAds.initialize(this);
    }
    public static Context context() {
        return mApp.getApplicationContext();
    }
}
