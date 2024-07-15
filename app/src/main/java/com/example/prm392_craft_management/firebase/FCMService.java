package com.example.prm392_craft_management.firebase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMService {
    String token = "ya29.a0AXooCgvwyUDDE3iQfX9LByHBBgEugJVdz2KmRKqLgKdQqIrLs8_yOq8_dwbI8N-k7-IbDMZm81tU9ZIy3hfu3SHLWfEvQ2IbhLr7m_p3A58mQwQSHAZtk2LR0Cq1NZ0mt8Lmp0zcJsEkdbr3CeH9ubr89yD5cZ1n0i7oaCgYKAZMSARISFQHGX2MiK87QGezc4yVriYmfxjAazg0171";

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer " + token // Replace with your actual FCM server key
    })
    @POST("v1/projects/prm392-craft-management/messages:send")
    Call<FCMResponse> sendMessage(@Body FCMMessage message);
}
