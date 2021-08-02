package com.project.nikhil.secfamfinal1.Notification;

public class Data {
    private String userId;
    private String userName;
    private String message;
    private String title;
    private String sent;
    private String type;
    private String pushId;
    private int notificationChannelId;

    public Data(String userId, String userName, String message, String title, String sent, String type, String pushId, int notificationChannelId) {
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.title = title;
        this.sent = sent;
        this.type=type;
        this.pushId=pushId;
        this.notificationChannelId = notificationChannelId;
    }

    public Data() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getNotificationChannelId() {
        return notificationChannelId;
    }

    public void setNotificationChannelId(int notificationChannelId) {
        this.notificationChannelId = notificationChannelId;
    }
}