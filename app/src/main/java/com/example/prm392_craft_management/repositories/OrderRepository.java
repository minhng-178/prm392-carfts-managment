package com.example.prm392_craft_management.repositories;

import com.example.prm392_craft_management.api.APIClient;
import com.example.prm392_craft_management.services.OrderService;

public class OrderRepository {
    public static OrderService getOrderService() {
        return APIClient.getClient().create(OrderService.class);
    }
}
