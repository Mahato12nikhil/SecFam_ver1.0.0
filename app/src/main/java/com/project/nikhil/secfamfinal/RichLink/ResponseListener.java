package com.project.nikhil.secfamfinal.RichLink;


import java.net.MalformedURLException;

public interface ResponseListener {

    void onData(MetaData metaData) throws MalformedURLException;

    void onError(Exception e);
}