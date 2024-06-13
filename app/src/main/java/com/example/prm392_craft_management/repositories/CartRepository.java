package com.example.prm392_craft_management.repositories;

import com.example.prm392_craft_management.api.APIClient;
import com.example.prm392_craft_management.services.CartService;

public class CartRepository {
    public static CartService getCartService() {
        return APIClient.getClient().create(CartService.class);
    }
}
