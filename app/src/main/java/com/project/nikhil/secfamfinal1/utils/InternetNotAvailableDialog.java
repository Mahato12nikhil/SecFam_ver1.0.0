package com.project.nikhil.secfamfinal1.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.nikhil.secfamfinal1.R;

public class InternetNotAvailableDialog extends DialogFragment {
    public static final String TAG = "InternetNotAvailableDialog";
    Dialog myDialog;
    Button btRetry;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_no_internet_connection,container, false);
        btRetry = view.findViewById(R.id.bt_retry);
        initView();
        return view;
    }

    private void initView() {
        btRetry.setOnClickListener(view -> {
            Activity activity;
            activity = myDialog.getOwnerActivity();
            if (activity == null) return;
            if (!MyUtil.internetCheck(activity)){
                Toast.makeText(activity, "No Network Available!", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = activity.getIntent().getExtras();
            Intent intent = new Intent(activity, activity.getClass());
            if (bundle != null) intent.putExtras(bundle);
            startActivity(intent);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        myDialog = getDialog();
        assert myDialog != null;
        myDialog.setCancelable(false);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        myDialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        myDialog.getOwnerActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

