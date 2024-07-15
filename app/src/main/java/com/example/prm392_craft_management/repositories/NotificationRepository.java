package com.example.prm392_craft_management.repositories;

import com.example.prm392_craft_management.api.APIClient;
import com.example.prm392_craft_management.services.NotificationService;

public class NotificationRepository {
    public static NotificationService getNotificationService() {
        return APIClient.getClient().create(NotificationService.class);
    }
}
