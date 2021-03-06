package com.project.nikhil.secfamfinal.Model;


public class User {
    private String id;
    private String name;
    private long time;
    private String image;
    private String sex;
    private String bio;
    private String details;
    private String thumb;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public User(String name, String id, String image, long time, String bio, String details, String sex, String thumb) {
        this.id = id;
        this.name =name;
        this.time=time;
        this.image = image;
        this.thumb=thumb;

    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}