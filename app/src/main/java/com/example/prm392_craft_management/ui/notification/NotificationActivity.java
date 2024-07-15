package com.example.prm392_craft_management.ui.notification;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.api.RetrofitClientInstance;
import com.example.prm392_craft_management.firebase.FCMMessage;
import com.example.prm392_craft_management.firebase.FCMResponse;
import com.example.prm392_craft_management.firebase.FCMService;
import com.example.prm392_craft_management.models.notification.NotificationModel;
import com.example.prm392_craft_management.models.notification.NotificationResponseModel;
import com.example.prm392_craft_management.repositories.NotificationRepository;
import com.example.prm392_craft_management.services.NotificationService;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private Socket mSocket;
    RecyclerView rvNotification;
    ImageView btnBack;
    NotificationService notificationService;
    NotificationResponseModel notificationResponseModel;
    NotificationAdapter notificationAdapter;
    String userId;
    List<NotificationModel> notificationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initSocketConnection();
        initComponent();
        initListener();
    }

    private void initSocketConnection() {
        try {
            String SERVER_PATH = "http://34.126.177.133:5500";
            mSocket = IO.socket(SERVER_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> {
            Log.d("SOCKET", "Connected to the Socket.io server");
            SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
            String userId = sharedPreferences.getString("USER_ID", "");
            JSONObject subscribeObject = new JSONObject();
            try {
                subscribeObject.put("room_id", userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSocket.emit("subscribe", subscribeObject);
            mSocket.off("notification");
            mSocket.on("notification", onNewNotification);
        })).on(Socket.EVENT_DISCONNECT, args -> runOnUiThread(() ->
                Log.d("SOCKET", "Disconnected from the Socket.io server ")
        )).on(Socket.EVENT_CONNECT_ERROR, args -> runOnUiThread(() ->
                Log.d("SOCKET", "Connection error: " + args[0])
        ));
        mSocket.connect();
    }

    private final Emitter.Listener onNewNotification = args -> runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        try {
            String message = data.getString("message");
            Log.d("SOCKET", "New message: " + message);
            initListener();
            sendFCMNotification(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    private void sendFCMNotification(String message) {
        String token = getToken();
        if (token == null) {
            Log.d("FCM", "Token not found");
            return;
        }

        FCMService fcmService = RetrofitClientInstance.getRetrofitInstance().create(FCMService.class);
        FCMMessage.Message.Notification notification = new FCMMessage.Message.Notification("New Message", message);
        FCMMessage.Message fcmMessage = new FCMMessage.Message(token, notification);
        FCMMessage messageToSend = new FCMMessage(fcmMessage);

        Call<FCMResponse> call = fcmService.sendMessage(messageToSend);
        call.enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(@NonNull Call<FCMResponse> call, @NonNull Response<FCMResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("FCM", "Notification sent successfully");
                } else {
                    Log.d("FCM", "Notification failed to send");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FCMResponse> call, @NonNull Throwable t) {
                Log.d("FCM", "Error: " + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("FCM_PREFS", MODE_PRIVATE);
        return sharedPreferences.getString("FCM_TOKEN", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
        }
    }

    private void initComponent() {
        btnBack = findViewById(R.id.button_back);
        rvNotification = findViewById(R.id.rvNotification);
        notificationService = NotificationRepository.getNotificationService();
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());
        notificationService.getNotificationsByUserID(Integer.parseInt(userId)).enqueue(new Callback<NotificationResponseModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NotificationResponseModel> call, @NonNull Response<NotificationResponseModel> response) {
                if (response.isSuccessful()) {
                    notificationResponseModel = response.body();
                    assert notificationResponseModel != null;
                    notificationList = notificationResponseModel.getNotifications();
                    notificationAdapter = new NotificationAdapter(notificationList);
                    rvNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                    rvNotification.setAdapter(notificationAdapter);
                    notificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponseModel> call, @NonNull Throwable throwable) {

            }
        });
    }
}
