package com.example.prm392_craft_management.ui.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.message.MessageModel;
import com.example.prm392_craft_management.models.message.MessageRequestModel;
import com.example.prm392_craft_management.models.message.MessageResponseModel;
import com.example.prm392_craft_management.repositories.MessageRepository;
import com.example.prm392_craft_management.services.MessageService;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity implements TextWatcher {
    private Socket mSocket;
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ImageView backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeView();
        initListeners();
        initiateSocketConnection();
    }

    private void initiateSocketConnection() {
        try {
            String SERVER_PATH = "http://34.126.177.133:5500";
            mSocket = IO.socket(SERVER_PATH);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.on(Socket.EVENT_CONNECT, args -> runOnUiThread(() -> {
            Log.d("SOCKET", "Connected to the Socket.io server");
            JSONObject subscribeObject = new JSONObject();
            try {
                subscribeObject.put("room_id", "0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("subscribe", subscribeObject);
            mSocket.off("message");

            mSocket.on("message", onNewMessage);
        })).on(Socket.EVENT_DISCONNECT, args -> runOnUiThread(() ->
                Log.d("SOCKET", "Disconnected from the Socket.io server ")
        )).on(Socket.EVENT_CONNECT_ERROR, args -> runOnUiThread(() ->
                Log.d("SOCKET", "Connection error: " + args[0])
        ));

        mSocket.connect();
    }

    private final Emitter.Listener onNewMessage = args -> runOnUiThread(this::initListeners);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString().trim();
        if (string.isEmpty()) {
            resetMessageEdit();
        } else {
            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void resetMessageEdit() {
        messageEdit.removeTextChangedListener(this);
        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);
    }

    private void initializeView() {
        messageEdit = findViewById(R.id.edittext_chat);
        sendBtn = findViewById(R.id.text_send);
        pickImgBtn = findViewById(R.id.image_send);
        backBtn = findViewById(R.id.button_back);
        recyclerView = findViewById(R.id.recycleview_chat);
    }

    private void initListeners() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");
        MessageService messageService = MessageRepository.getChatService();

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        messageService.getChat(Integer.parseInt(userId)).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MessageModel>> call, @NonNull Response<List<MessageModel>> response) {
                if (response.isSuccessful()) {
                    List<MessageModel> messageList = response.body();
                    messageAdapter = new MessageAdapter(messageList, userId);
                    recyclerView.setAdapter(messageAdapter);
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                } else {
                    Toast.makeText(MessageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MessageModel>> call, @NonNull Throwable throwable) {
                Toast.makeText(MessageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v -> {
            String message = messageEdit.getText().toString();
            int receiverId = 0;
            MessageRequestModel messageRequestModel = new MessageRequestModel(Integer.parseInt(userId), receiverId, message);
            messageService.postChat(messageRequestModel).enqueue(new Callback<MessageResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<MessageResponseModel> call, @NonNull Response<MessageResponseModel> response) {
                    if (response.isSuccessful()) {
                        resetMessageEdit();
                    } else {
                        Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MessageResponseModel> call, @NonNull Throwable throwable) {
                    Toast.makeText(MessageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}
