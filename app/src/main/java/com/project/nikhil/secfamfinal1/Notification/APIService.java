package com.project.nikhil.secfamfinal1.Notification;

import com.project.nikhil.secfamfinal1.constant.Constant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface APIService {
  @Headers(
          {
                  "Content-Type:application/json",
                  "Authorization:"+"key="+ Constant.FIREBASE_SERVER_KEY
          }
  )
  @POST("fcm/send")
  Call<MyResponse> sendNotification(@Body Sender body);



    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:"+"key="+ Constant.FIREBASE_SERVER_KEY
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendEmergencyNotification(@Body EmergencySender body);
}