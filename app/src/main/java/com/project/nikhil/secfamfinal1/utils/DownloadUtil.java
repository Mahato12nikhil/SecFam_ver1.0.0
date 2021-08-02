package com.project.nikhil.secfamfinal1.utils;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.project.nikhil.secfamfinal1.R;

import java.io.File;


public class DownloadUtil {

    public void checkAndLoad(Context context, String url, String fileType) {
        String fileExtension = "."+getMimeType(context, Uri.parse(url));
        File file = new File(getFileBase(context) + "/"+fileType+"/SecFam_"+System.currentTimeMillis()+fileExtension);

        if (file.exists()) {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            //newIntent.setDataAndType(FileProvider.getUriForFile(context, context.getString(R.string.authority), file), Helper.getMimeType(context, downloadFileEvent.getAttachment().getData()));
            newIntent.setDataAndType(FileProvider.getUriForFile(context, context.getString(R.string.authority), file), fileExtension);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
            }
        } else {
            downloadFile(context, url,fileType, "SecFam_"+String.valueOf(System.currentTimeMillis())+fileExtension);
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(Context context, String url, String fileType,String fileName) {
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setTitle(context.getString(R.string.app_name))
                .setDescription("downloading" + " " + fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, context.getString(R.string.app_name)+ "/"+fileType + "/" + fileName);
        mgr.enqueue(request);
    }

    public static String getFileBase(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + context.getString(R.string.app_name);
    }
    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }
}
