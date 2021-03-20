package com.project.nikhil.secfamfinal.Post;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private long id;
    public Map<String,String> links=new HashMap<String,String>();
    private String postid;
    private String description;
    private String publisher;
    private  long time;
    private String imageUrl;
    private String linkTitle;
    private String linkDesc;

    private String type;
    private String site;
    private String thumb;
    private long likes_count;
    private long comment_count;



    public long getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(long likes_count) {
        this.likes_count = likes_count;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Post(String postid, String description, String publisher, long time,String type,String site,Map<String,String> links,String imageUrl,String linkTitle,String linkDesc,long likes_count,long comment_count,String thumb) {

        this.postid = postid;
        this.description = description;
        this.publisher = publisher;
        this.time=time;
        this.type=type;
        this.site=site;
        this.links=links;
        this.imageUrl=imageUrl;
        this.linkTitle=linkTitle;
        this.linkDesc=linkDesc;
        this.likes_count=likes_count;
        this.comment_count=comment_count;
        this.thumb=thumb;

    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public long getComment_count() {
        return comment_count;
    }

    public void setComment_count(long comment_count) {
        this.comment_count = comment_count;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


}