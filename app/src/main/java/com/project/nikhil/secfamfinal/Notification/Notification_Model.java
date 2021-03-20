package com.project.nikhil.secfamfinal.Notification;

public class Notification_Model {
    private String userid;
    private String text;
    private String postid;
    private boolean ispost;
    private String notificationID;
    private long timestamp;
    private  String postType;

    public Notification_Model(String userid, String text, String postid, boolean ispost,String notificationID,long timestamp,String postType) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.ispost = ispost;
        this.notificationID=notificationID;
        this.timestamp=timestamp;
        this.postType=postType;
    }

    public Notification_Model() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
