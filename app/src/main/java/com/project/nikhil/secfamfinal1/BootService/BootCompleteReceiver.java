package com.project.nikhil.secfamfinal1.BootService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.project.nikhil.secfamfinal1.LocationService.TrackingService;


public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Service Started", Toast.LENGTH_LONG).show();

        Intent service = new Intent(context, TrackingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(service);
        }else {
            context.startService(service);
        }
    }
}
