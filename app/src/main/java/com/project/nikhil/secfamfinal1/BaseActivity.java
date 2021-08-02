package com.project.nikhil.secfamfinal1;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.nikhil.secfamfinal1.Map.VictimLocationUpdateService;
import com.project.nikhil.secfamfinal1.utils.InternetNotAvailableDialog;
import com.project.nikhil.secfamfinal1.utils.MyUtil;

import java.util.List;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public String myid;
    Dialog dialog;
    public MyApplication app;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            myid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void showProgressDialog(){
        if (!dialog.isShowing()) {
            TextView tvMessage = dialog.findViewById(R.id.tvMessage);
            tvMessage.setText(getResources().getString(R.string.please_wait));
            dialog.show();
        }
    }
    public void showProgressDialog(String message){
        if (!dialog.isShowing()) {
            TextView tvMessage = dialog.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            dialog.show();
        }
    }
    public void hideProgressDialog(){
        if (dialog.isShowing())
        dialog.dismiss();
    }
    public boolean isInternetAvailable() {
        if (MyUtil.internetCheck(this)) {
            return true;
        }else {
            DialogFragment dialog = new InternetNotAvailableDialog();
            FragmentManager fm = getSupportFragmentManager();
            dialog.show(fm, InternetNotAvailableDialog.TAG);
        }
        return false;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        stopService();
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
        //Update User status
        if (myid != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(myid).child("isOnline");
            ref.setValue("online");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (isAppIsInBackground(this)) {
            if (myid != null) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(myid).child("isOnline");
                //ref.setValue(ServerValue.TIMESTAMP);//It will store firebase server timestamp
                ref.setValue(String.valueOf(System.currentTimeMillis()));
            }
       // }
    }

    public static void ShowAnimationFadeOut(View view){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
        view.startAnimation(animation);
    }
    public static void ShowAnimationFadeIn(View view){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
        view.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    public void stopService(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (VictimLocationUpdateService.class.getName().equals(service.service.getClassName())) {
                Intent intent = new Intent(this, VictimLocationUpdateService.class);
                intent.setAction(VictimLocationUpdateService.ACTION_STOP_FOREGROUND_SERVICE);
                startService(intent);
            }
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = Objects.requireNonNull(am).getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = Objects.requireNonNull(am).getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (Objects.requireNonNull(componentInfo).getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
