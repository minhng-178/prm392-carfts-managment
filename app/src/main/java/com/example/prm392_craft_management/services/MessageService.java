package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.message.MessageModel;
import com.example.prm392_craft_management.models.message.MessageRequestModel;
import com.example.prm392_craft_management.models.message.MessageResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    String CHAT = "chat";

    @POST(CHAT)
    Call<MessageResponseModel> postChat(@Body MessageRequestModel chatRequestModel);

    @GET(CHAT + "/{user_id}")
    Call<List<MessageModel>> getChat(@Path("user_id") int userId);
}

