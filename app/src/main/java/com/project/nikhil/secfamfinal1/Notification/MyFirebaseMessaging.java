package com.project.nikhil.secfamfinal1.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.nikhil.secfamfinal1.Chat.ConversationActivity;
import com.project.nikhil.secfamfinal1.Emergency.EmergencyActivity;
import com.project.nikhil.secfamfinal1.MySharedPreferences;
import com.project.nikhil.secfamfinal1.Post.PostActivity;
import com.project.nikhil.secfamfinal1.Post.VideoExample;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.constant.Constant;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "common";
    String userId = "";
    String userName = "";
    String message = "";
    String title = "";
    String sent = "";
    String type = "";
    String pushId = "";
    int notificationChannelId = 0;
    MySharedPreferences pref = null;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("!!!!!!Notification", remoteMessage.getData().toString());
        if (pref == null) {
            pref = new MySharedPreferences(this);
        }
        //type == chat, like, comment, follower , emergency
        type = remoteMessage.getData().get("type");
        switch (type) {
            case Constant.NOTIFICATION_TYPE_CHAT:
                //*********************
                userId = remoteMessage.getData().get("userId");
                userName = remoteMessage.getData().get("userName");
                message = remoteMessage.getData().get("message");
                title = remoteMessage.getData().get("title");
                sent = remoteMessage.getData().get("sent");
                pushId = remoteMessage.getData().get("pushId");
                notificationChannelId = Integer.parseInt(remoteMessage.getData().get("notificationChannelId"));
                //*********************
                //Show notification when user not in chat list of sender
                if (!Constant.CURRENT_CHAT_USER_ID.equals(userId)) {
                    if (!pref.isNotificationChannelIdExist(userId)) {
                        pref.setNotificationChannelData(userId, new NotificationChannelData(notificationChannelId, 1));
                    } else {
                        notificationChannelId = pref.getNotificationChannelData(userId).getChannelId();
                        int count = pref.getNotificationChannelData(userId).getCount() + 1;
                        pref.setNotificationChannelData(userId, new NotificationChannelData(notificationChannelId, count));
                    }
                    updateMessageStatus();
                    ShowNotification();
                }
                break;
            case Constant.NOTIFICATION_TYPE_COMMENT:
            case Constant.NOTIFICATION_TYPE_LIKE:
            case Constant.NOTIFICATION_TYPE_FOLLOW:
            case Constant.NOTIFICATION_TYPE_EMERGENCY:
                message = remoteMessage.getData().get("body");
                title = remoteMessage.getData().get("title");
                notificationChannelId = (int) (Math.random() * 10000);
                ShowNotification();
                break;
        }
    }

    private void updateMessageStatus() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userId).child(pushId).child("messageStatus");
            ref.setValue(Constant.MESSAGE_STATUS_DELIVERED);
        }
    }

    private void ShowNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
                .setSmallIcon(R.drawable.secfam_logo)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setContentIntent(setIntentToClass())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        if (type.equals(Constant.NOTIFICATION_TYPE_CHAT)) {
            mBuilder.setContentText(pref.getNotificationChannelData(userId).getCount() + message);
        } else {
            mBuilder.setContentText(message);
        }
        setNotificationManager(mBuilder);
    }

    private PendingIntent setIntentToClass() {
        // Create an explicit intent for an Activity in your app
        Intent intent = null;
        switch (type) {
            case Constant.NOTIFICATION_TYPE_CHAT:
                intent = new Intent(getApplication(), ConversationActivity.class);
                intent.putExtra("id", userId);
                intent.putExtra("user_name", userName);
                break;
            case Constant.NOTIFICATION_TYPE_COMMENT:
            case Constant.NOTIFICATION_TYPE_LIKE:
            case Constant.NOTIFICATION_TYPE_FOLLOW:
                intent = new Intent(getApplication(), PostActivity.class);
                intent.putExtra("type","NOTIFICATION");
                break;
            case Constant.NOTIFICATION_TYPE_EMERGENCY:
                intent = new Intent(getApplication(), EmergencyActivity.class);
                intent.putExtra("type","EMERGENCY");
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(getApplication(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setNotificationManager(NotificationCompat.Builder mBuilder) {
        // Gets an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());
        createNotificationChannel(notificationManager);
        //to post your notification to the notification bar
        notificationManager.notify(notificationChannelId, mBuilder.build());
    }

    private void createNotificationChannel(NotificationManagerCompat notificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SecFam_notification";
            String description = "common notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //sendOreoNotification(remoteMessage);
/*
       // String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");
       String type= remoteMessage.getData().get("type");
        if(type.equals("like") || type.equals("comment")){
            System.out.println("######----"+ remoteMessage.getData().get("postid"));
        }
        System.out.println("######----"+user);
       // Log.d("MY",remoteMessage);
        System.out.print(remoteMessage);

        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        {
            if (!currentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   // sendOreoNotification(remoteMessage);
                } else {
                  //  sendNotification(remoteMessage);
                }
            }
        }*/
    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String postType = remoteMessage.getData().get("type");
        System.out.println("#&^%$&^%#" + postType);
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        //  int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        Intent intent = null;
        if (remoteMessage.getData().get("type").equals("emergency")) {
            intent = new Intent(this, EmergencyActivity.class);
        } else {
            if (postType.equals("video")) {
                intent = new Intent(this, VideoExample.class);
                intent.putExtra("postid", remoteMessage.getData().get("postid"));
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);

            }


            intent = new Intent(this, PostActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString("userid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound);

       /* int i = 0;
        if (j > 0) {
            i = j;
        }*/

        oreoNotification.getManager().notify(100, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, PostActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("Userid", user);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)

                .setAutoCancel(true)
                .setSound(defaultSound)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        noti.notify(i, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            reference.child("tokenid").setValue(s);
        }
    }


}