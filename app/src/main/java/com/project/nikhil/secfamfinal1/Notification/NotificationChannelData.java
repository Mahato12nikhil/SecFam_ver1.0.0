package com.project.nikhil.secfamfinal1.Notification;

public class NotificationChannelData {
    int channelId;
    int count;

    public NotificationChannelData(int channelId, int count) {
        this.channelId = channelId;
        this.count = count;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
