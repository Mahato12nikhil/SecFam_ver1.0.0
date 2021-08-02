package com.project.nikhil.secfamfinal1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.project.nikhil.secfamfinal1.Notification.NotificationChannelData;

import org.json.JSONException;
import org.json.JSONObject;


public class MySharedPreferences {
    public enum UserData {PREF}

    public SharedPreferences pref;
    public MySharedPreferences(Context context) {
        pref = context.getSharedPreferences(UserData.PREF.name(), Context.MODE_PRIVATE);
    }

    public void setNotificationChannelData(String userId, NotificationChannelData data){
        pref.edit().putString(userId, new Gson().toJson(data)).apply();
    }

    public NotificationChannelData getNotificationChannelData(String userId){
        String stringData = "{\"channelId\":8332,\"count\":1}";
        NotificationChannelData data ;
        try {
            JSONObject jsonObject = new JSONObject(pref.getString(userId, stringData));
            data = new NotificationChannelData(jsonObject.getInt("channelId"),jsonObject.getInt("count"));
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isNotificationChannelIdExist(String userId){
        String value = pref.getString(userId,null);
        if (value == null) {
            return false;
        } else {
            return true;
        }

    }

    public void removeChannelId(String userId){
        pref.edit().remove(userId).apply();
    }
}
