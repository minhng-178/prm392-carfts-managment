package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.notification.NotificationResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NotificationService {
    String NOTIFICATIONS = "notification";

    @GET(NOTIFICATIONS)
    Call<NotificationResponseModel> getNotificationsByUserID(@Query("user_id") int userId);
}
