package com.project.nikhil.secfamfinal1.Notification;


public class EmergencySender {
    public EmergencyData data;
    public String to;
    public String priority = "high";

    public EmergencySender(EmergencyData data, String to) {
        this.data = data;
        this.to = to;
    }
}