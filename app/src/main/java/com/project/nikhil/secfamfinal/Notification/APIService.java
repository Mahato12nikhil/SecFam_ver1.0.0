package com.project.nikhil.secfamfinal.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface APIService {
  @Headers(
          {
                  "Content-Type:application/json",
                  "Authorization:key=AAAA5bszPLo:APA91bGueBoT5u-feFJ6SywV9UIWLXDDbIMpBaTz6SNHVuTZua2pCGI1p7cGoi_LIpMquDwAf9NkGMtXaqGazL7IdjOy16pWzXltban1XXyywnJKJeed9811JutT9mf-Y3d20iR--Oos"
          }
  )

  @POST("fcm/send")
  Call<MyResponse> sendNotification(@Body Sender body);
}