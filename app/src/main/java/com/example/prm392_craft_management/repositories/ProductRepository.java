package com.example.prm392_craft_management.repositories;


import com.example.prm392_craft_management.api.APIClient;
import com.example.prm392_craft_management.services.ProductService;

public class ProductRepository {
    public static ProductService getProductService() {
        return APIClient.getClient().create(ProductService.class);
    }
}
