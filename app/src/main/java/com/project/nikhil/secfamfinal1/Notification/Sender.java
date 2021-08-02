package com.project.nikhil.secfamfinal1.Notification;


public class Sender {
    public Data data;
    public String to;
    public String priority = "high";

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}