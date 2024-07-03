package com.example.prm392_craft_management.repositories;

import com.example.prm392_craft_management.api.APIClient;
import com.example.prm392_craft_management.services.MessageService;

public class MessageRepository {
    public static MessageService getChatService() {
        return APIClient.getClient().create(MessageService.class);
    }
}
