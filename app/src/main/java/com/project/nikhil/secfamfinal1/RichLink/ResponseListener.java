package com.project.nikhil.secfamfinal1.RichLink;


import java.net.MalformedURLException;

public interface ResponseListener {

    void onData(MetaData metaData) throws MalformedURLException;

    void onError(Exception e);
}