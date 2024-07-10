package com.example.prm392_craft_management.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.prm392_craft_management.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NotificationService extends Service {
    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        initiateSocketConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initiateSocketConnection() {
        try {
            String SERVER_PATH = "http://34.126.177.133:5500";
            mSocket = IO.socket(SERVER_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on(Socket.EVENT_CONNECT, args -> {
                    Log.d("SOCKET", "Connected to the Socket.io server");
                    SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
                    String userId = sharedPreferences.getString("USER_ID", "");
                    JSONObject subscribeObject = new JSONObject();
                    try {
                        subscribeObject.put("room_id", userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("subscribe", subscribeObject);
                    mSocket.on("message", onNewMessage);
                }).on(Socket.EVENT_DISCONNECT, args -> Log.d("SOCKET", "Disconnected from the Socket.io server"))
                .on(Socket.EVENT_CONNECT_ERROR, args -> Log.d("SOCKET", "Connection error: " + args[0]));

        mSocket.connect();
    }

    private final Emitter.Listener onNewMessage = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            String message = data.getString("message");
            Log.d("SOCKET", "New message: " + message);
            showNotification(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    private void showNotification(String message) {
        NotificationUtils notificationUtils = new NotificationUtils(this, "CHANNEL_ID");
        notificationUtils.showNotification("You have a new message from Admin!", message, true);
    }
}

