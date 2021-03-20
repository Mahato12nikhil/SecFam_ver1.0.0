package com.project.nikhil.secfamfinal.Chat;

public class ConversationActivity_Data_Model {
    private String message, type,latitude,longitude,id;
    private long  time;
    private  String image;
    private String pushid;
    String lastText;

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    boolean isSeen;
    //private boolean isSent=false;
    private String messageDesc;

    public String getMessageDesc() {
        return messageDesc;
    }

    public void setMessageDesc(String messageDesc) {
        this.messageDesc = messageDesc;
    }

  /*public boolean isSent() {
    return isSent;
  }

  public void setSent(boolean sent) {
    isSent = sent;
  }*/

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String from,to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }



    public ConversationActivity_Data_Model(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConversationActivity_Data_Model(String message, String type, long time, boolean isSeen, String latitude, String longitude , String image, boolean isSent, String messageDesc, String id) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.isSeen = isSeen;
        this.latitude=latitude;
        this.longitude=longitude;
        this.image=image;
        // this.isSent=isSent;
        this.messageDesc=messageDesc;
        this.id=id;

    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean seen) {
        this.isSeen = seen;
    }

    public ConversationActivity_Data_Model(){

    }

}
