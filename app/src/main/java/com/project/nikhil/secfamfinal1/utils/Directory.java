/*
 * Created by Devlomi on 2021
 */

package com.project.nikhil.secfamfinal1.utils;

import android.os.Build;
import android.os.Environment;

import com.project.nikhil.secfamfinal1.MyApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Directory {
    private static final String IMAGE_EXTENSION = ".jpg";
    private static final String VIDEO_EXTENSION = ".mp4";
    private static final String APP_FOLDER_NAME = "SecFam";


    public static String mainAppFolder() {
        File file;
        if (Build.VERSION.SDK_INT >= 30) {
            file = new File(MyApplication.context().getExternalFilesDir(null) + "/" + APP_FOLDER_NAME + "/");
        } else {
            file = new File(Environment.getExternalStorageDirectory() + "/" + APP_FOLDER_NAME + "/");
        }
        //if the directory is not exists create it
        if (!file.exists())
            file.mkdir();


        return file.getAbsolutePath();
    }

    public static String getImagesFolder() {
        File file = new File(mainAppFolder()  + "Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
    public static String getVideosFolder() {
        File file = new File(mainAppFolder()  + "Videos");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
    public static String generateNewImageName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddSSSS", Locale.US); //the Locale us is to use english numbers
        return "IMG-" + sdf.format(date)+ IMAGE_EXTENSION;
    }
    public static String generateNewVideoName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddSSSS", Locale.US); //the Locale us is to use english numbers
        return "VID-" + sdf.format(date)+ VIDEO_EXTENSION;
    }

}
