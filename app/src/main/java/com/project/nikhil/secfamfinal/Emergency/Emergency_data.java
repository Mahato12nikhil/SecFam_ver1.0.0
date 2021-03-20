package com.project.nikhil.secfamfinal.Emergency;

import java.util.HashMap;
import java.util.Map;

public class Emergency_data {
    String pushId,status,sender;
    Double latitude,longitude;
    public Map<String,Boolean> ids=new HashMap<String,Boolean>();
    long time;

    public Emergency_data() {
    }

    public Emergency_data(String pushId, String status, Double latitude, Double longitude, long time,String sender,Map<String,Boolean> ids) {
        this.pushId = pushId;
        this.status = status;
        this.sender=sender;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.ids=ids;
    }

    public Map<String, Boolean> getIds() {
        return ids;
    }

    public void setIds(Map<String, Boolean> ids) {
        this.ids = ids;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
