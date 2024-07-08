package com.example.prm392_craft_management.models.order;

import java.util.List;

public class OrderResponseModel {
    private List<OrderModel> orders;

    // Getters and Setters
    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }
}
